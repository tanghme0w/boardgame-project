package refactor.server;

import globals.BoardMode;
import globals.StoneColor;
import globals.Config;
import refactor.Client;
import refactor.server.dto.BoardScanResult;
import refactor.server.dto.StepResult;
import refactor.server.entity.*;
import refactor.vo.PromptVO;
import refactor.vo.RenderVO;

import java.io.*;
import java.util.*;

import static java.lang.Math.abs;
import static java.lang.Math.min;

public class Server {
    static Game game = null;

    static List<Player> players = new ArrayList<>(Arrays.asList(new Player[2]));

    static boolean isGameActive = false;

    static Account account = new Account(Config.ACCOUNT_DATA_PATH);

    public static void newGame(String rule, int[] boardSize) {

        //instantiate new game
        switch (rule.strip().toLowerCase()) {
            case "go": game = new Game(boardSize[0], boardSize[1], new GoRules()); break;
            case "gomoku": game = new Game(boardSize[0], boardSize[1], new GomokuRules()); break;
            case "reversi": game = new Game(boardSize[0], boardSize[1], new ReversiRules()); break;
        }

        //generate random AI players to fill vacancy
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i) == null) {
                generateGuestPlayer(i);
            }
        }

        //increase the join game counter for each player
        for (Player player: players) {
            player.joinRecord.put("total", player.joinRecord.getOrDefault("total", 0) + 1);
            player.joinRecord.put(rule.strip().toLowerCase(), player.joinRecord.getOrDefault(rule.strip().toLowerCase(), 0) + 1);
            account.flush();
        }

        //assign a player for every vacant identity in current game.
        List<Integer> idx = new ArrayList<>();
        for (int i = 0; i < game.identities.size(); i++) {
            idx.add(i);
        }
        Collections.shuffle(idx);
        for (int i = 0; i < game.identities.size(); i++) {
            Identity id = game.identities.get(i);
            Player p = players.get(i);
            id.player = p;
            game.playerIdentityMap.put(p, id);
        }

        //set state flag
        isGameActive = true;
        Client.boardMode = BoardMode.IN_GAME;

        //initiate game
        game.switchTurn();

        //do AI step if current acting player is AI
        AIStep(game.currentActingIdentity.player.AILevel);

        //log & render
        Logger.log("New " + rule + " game with board size (" + boardSize[0] + ", " + boardSize[1] + ")");
        render();

        //for testing only
        if (Config.RANDOM_STEP_TEST) {
            new Thread(() -> {
                for (int i = 0; i < Config.RANDOM_STEP_NUMBER; i++) {
                    stepAt(new Position((new Random().nextInt(Config.DEFAULT_GO_BOARD_SIZE)) + 1, (new Random().nextInt(Config.DEFAULT_GO_BOARD_SIZE)) + 1));
                    if (!isGameActive) break;
                }
            }).start();
        }
    }

    private static void AIStep(Integer level) {
        if (level <= 0) {
            Client.isAIActing = false;
            return;
        }
        Client.isAIActing = true;
        new Thread(() -> {
            try {
                Thread.sleep(Config.AI_STEP_INTERVAL_MS);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            //retrieve available steps
            List<Action> actions = game.ruleset.evaluateActions(game.board);
            //if no available steps, abstain
            if (actions.isEmpty()) abstain();
            if (level == 1) {
                //take random steps
                Random random = new Random();
                int actionIdx = random.nextInt(actions.size());
                stepAt(actions.get(actionIdx).position);
            } else if (level == 2) {
                if (actions.isEmpty()) abstain();
                //choose the highest score action
                actions.sort(new Comparator<Action>() {
                    @Override
                    public int compare(Action o1, Action o2) {
                        if (o2.score - o1.score < 0) return -1;
                        if (o2.score - o1.score == 0) return 0;
                        if (o2.score - o1.score > 0) return 1;
                        return 0;
                    }
                });
                Random random = new Random();
                if (random.nextInt(100) > 80) stepAt(actions.get(random.nextInt(min(5, actions.size()))).position);
                else {
                    if (actions.get(0).score < 0) abstain();
                    stepAt(actions.get(0).position);
                }
            }
        }).start();
    }

    public static void endGame(StoneColor winnerStoneColor) {
        //get winner identity
        Identity winner = null;
        for (Identity id: game.identities) {
            if (id.stoneColor.equals(winnerStoneColor))
                winner = id;
        }
        if (winner == null) {
            Logger.log("Game Over: tie.");
            render();
            Client.popUpMessage(new PromptVO("It's a tie!"));
        } else {
            String winnerName = winner.player.playerName;
            String winningSide = switch (winner.stoneColor) {
                case BLACK -> "black";
                case WHITE -> "white";
            };
            String winningIdentity = winnerName + " (" + winningSide + ") ";
            //add winning count
            winner.player.winRecord.put("total", winner.player.winRecord.getOrDefault("total", 0) + 1);
            winner.player.winRecord.put(game.ruleset.getRuleName(), winner.player.winRecord.getOrDefault(game.ruleset.getRuleName(), 0) + 1);
            account.flush();
            Logger.log("Game Over: " + winningIdentity + " wins");
            render();
            Client.popUpMessage(new PromptVO(winningIdentity + "wins!"));
        }
        isGameActive = false;
        Client.boardMode = BoardMode.WAIT;
        render();
    }

    public static void login(String name, String passHash, Integer position) {
        for (Player player: players) {
            if (player != null &&  Objects.equals(player.playerName, name)) {
                Client.popUpMessage(new PromptVO("Login fail: " + name + " has already logged in!"));
                render();
                return;
            }
        }
        Player player = account.loginWithCredentials(name, passHash);
        if (player != null) {
            players.set(position, player);
            Client.popUpMessage(new PromptVO("Login success. Welcome, " + name + "!"));
            render();
            return;
        }
        Client.popUpMessage(new PromptVO("Login fail: invalid username or password."));
        render();
    }

    public static void logout(Integer idx) {
        Player player = players.get(idx);
        players.set(idx, null);
        Client.popUpMessage(new PromptVO("Logout success. Goodbye, " + player.playerName + "!"));
        Client.playerISAI.put(idx, false);
        render();
    }

    public static void register(String name, String passHash) {
        if (account.accountExists(name)) {
            Client.popUpMessage(new PromptVO("Register failed! Player named " + name + " already exists. Please log in."));
        } else {
            account.addNewPlayer(new Player(name, passHash));
            account.flush();
            Client.popUpMessage(new PromptVO("Register success! Please log in to join the game."));
        }
    }

    public static void generateGuestPlayer(Integer playerIdx) {
        Player newGuestPlayer = new Player("Guest_" + new Random().nextInt(1000));
        players.set(playerIdx, newGuestPlayer);
        Logger.log(newGuestPlayer.playerName + " has joined the game.");
        render();
    }

    public static void generateAIPlayer(Integer playerIdx) {
        generateGuestPlayer(playerIdx);
        Client.playerISAI.put(playerIdx, true);
        players.get(playerIdx).isAI = true;
        players.get(playerIdx).AILevel = 1;
        players.get(playerIdx).playerName = players.get(playerIdx).playerName + " (AI) ";
        render();
    }

    public static void setAILevel(Integer playerIndex, Integer level) {
        Player player = players.get(playerIndex);
        if (!player.isAI) return;
        player.AILevel = level;
    }

    public static void stepAt(Position position) {
        //do nothing if the game hasn't started or has already ended or AI is playing.
        if (!isGameActive || Client.boardMode == BoardMode.REMOVE) return;

        //validate position coordinates
        if (game.board.outOfBound(position)) return;

        //execute step on the board
        StepResult stepResult = game.ruleset.takeStep(game.board, position, game.boardHistory);
        if (stepResult.stepSuccess) {
            //update board history
            game.boardHistory.push(new Board(game.board));
            //write log
            Logger.log(game.currentActingIdentity.player.playerName + " (" + game.currentActingIdentity.stoneColor.string() + ") " + " takes step at " + position.x + ", "+ position.y + " (Step #" + (game.boardHistory.size()) + ")");
            //reset abstain status
            game.currentActingIdentity.hasAbstained = false;
            //update board status
            game.board = stepResult.boardAfterStep;
            //switch turn
            game.switchTurn();
        }

        //if game is over, call endGame
        if (stepResult.gameOver) {
            game.board = stepResult.boardAfterStep;
            endGame(stepResult.winner);
        }

        //render
        render();

        //if next player is AI, do AI step
        if (isGameActive) AIStep(game.currentActingIdentity.player.AILevel);
    }

    public static void abstain() {
        //do nothing if the game hasn't started or has already ended.
        if (!isGameActive) return;

        //set abstain status for current player
        game.currentActingIdentity.hasAbstained = true;

        //if all players have abstained, game ends
        boolean gameEnd = true;
        for(Identity id: game.identities) {
            gameEnd &= id.hasAbstained;
        }

        //if the game ends, go to endGame operation
        if (gameEnd) {
            Logger.log("All players have abstained, the game ends.");
            // find the winner
            // if this is a go game, prompt users to remove dead pieces
            if (game.ruleset.getRuleName().equalsIgnoreCase("go")) {
                Client.popUpMessage(new PromptVO("Game has ended, please click on dead pieces to remove them.\n" +
                        "Click on the confirm button on the bottom left when you are done."));
                render();
                game.boardHistory.push(new Board(game.board));
                Client.removeDeadPieces(new RenderVO(players, game.identities, game.currentActingIdentity, game.board, Logger.getLog(Config.MAX_LOG_ENTRIES)));
                return;
            }
            endGame(game.ruleset.scanBoard(game.board).winner);
            return;
        }

        //if the game does not end, change the current acting player
        Logger.log(game.currentActingIdentity.player.playerName + " abstained, the turn goes to the next player");
        game.switchTurn();

        //render
        render();
    }

    public static void surrender(Integer playerIndex) {
        //do nothing if the game hasn't started or has already ended.
        if (!isGameActive) return;

        //declare victory for the other side
        switch (game.playerIdentityMap.get(players.get(playerIndex)).stoneColor) {
            case BLACK -> endGame(StoneColor.WHITE);
            case WHITE -> endGame(StoneColor.BLACK);
        }
    }

    public static void withdraw(Integer playerIndex) {
        //accumulate withdraw count
        Identity actionIdentity = game.playerIdentityMap.get(players.get(playerIndex));
        if (actionIdentity.withdrawCount.equals(Config.MAX_WITHDRAW_TIMES)) {
            Logger.log(actionIdentity.player.playerName + " cannot withdraw step: maximum withdraw limit reached.");
            render();
            return;
        }
        actionIdentity.withdrawCount++;
        //pop history until the popped out move belongs to the current player.
        if (game.boardHistory.isEmpty()) {
            Logger.log("cannot withdraw step: history is empty.");
            render();
            return;
        }
        Board boardHistoryEntry = game.boardHistory.pop();
        while (boardHistoryEntry.actingStoneColor != actionIdentity.stoneColor) {
            boardHistoryEntry = game.boardHistory.pop();
        }
        game.board = boardHistoryEntry;
        game.currentActingIdentity = getCurrentActingIdentityWithChessType(game.board.actingStoneColor);

        Logger.log(actionIdentity.player.playerName + " (" + actionIdentity.stoneColor.string() + ") " + " has withdrawn step #" + (game.boardHistory.size() + 1));

        //reactivate the game if game has ended.
        isGameActive = true;
        Client.boardMode = BoardMode.IN_GAME;
        render();
    }

    public static void removeDeadPiecesAt(Position position) {
        ((GoRules)game.ruleset).removePieces(game.board, position);
        Logger.log("Action: try to remove dead pieces at " + position.x + "," + position.y);
        render();
    }

    public static void cancelRemoveDeadPieces() {
        game.board = game.boardHistory.pop();
        Client.boardMode = BoardMode.IN_GAME;
        render();
    }

    public static void confirmRemoveDeadPieces(Board board) {
        Logger.log("Action: confirm remove dead pieces");
        endGame(game.ruleset.scanBoard(game.board).winner);
        render();
    }

    public static void saveGame(String path) {
        try (FileOutputStream fileOut = new FileOutputStream(path)) {
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(game);
        } catch (Exception e) {
            System.out.printf(e.getMessage());
        }
    }

    public static void loadGame(String path) {
        try (FileInputStream fileIn = new FileInputStream(path)) {
            ObjectInputStream in = new ObjectInputStream(fileIn);
            game = (Game) in.readObject();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        Logger.log("loading new" + game.ruleset.getRuleName() + " game from: " + path);
        Logger.log("All existing players will be cleared");

        //load the players
        players = new ArrayList<>();

        //recover players & identity map
        for (Identity id: game.identities) {
            players.add(id.player);
            game.playerIdentityMap.put(id.player, id);
        }

        //replay
        Thread replayThread = null;
        if (Config.REPLAY_WHEN_LOAD) {
            replayThread = new Thread(()->{
                for (Board historyBoard: game.boardHistory) {
                    Client.render(
                            new RenderVO(
                                    players,
                                    game.identities,
                                    game.currentActingIdentity,
                                    historyBoard,
                                    Logger.getLog(Config.MAX_LOG_ENTRIES)
                            )
                    );
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                //activate game status
                isGameActive = true;

                //if game is already over, pop up message
                BoardScanResult scanResult = game.ruleset.scanBoard(game.board);
                if(scanResult.gameOver) endGame(scanResult.winner);

                render();
            });
            replayThread.start();
        } else {
            //activate game status
            isGameActive = true;

            //if game is already over, pop up message
            BoardScanResult scanResult = game.ruleset.scanBoard(game.board);
            if(scanResult.gameOver) endGame(scanResult.winner);

            render();
        }
    }

    //render player info, game board, and logs.
    static void render() {
        RenderVO renderVO = new RenderVO();
        if (game == null) {
            Client.render(new RenderVO(players, null, null, null, Logger.getLog(Config.MAX_LOG_ENTRIES)));
        } else {
            Client.render(new RenderVO(players, game.identities, game.currentActingIdentity, game.board, Logger.getLog(Config.MAX_LOG_ENTRIES)));
        }
    }

    private static Identity getCurrentActingIdentityWithChessType(StoneColor stoneColor) {
        for (Identity identity: game.identities) {
            if (identity.stoneColor == stoneColor) return identity;
        }
        return null;
    }
}
