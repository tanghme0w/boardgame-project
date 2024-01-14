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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Server {
    static Game game = null;

    static List<Player> players = new ArrayList<>();

    static boolean isGameActive = false;

    public static void newGame(String rule, int[] boardSize) {
        //set state flag
        isGameActive = true;

        //instantiate new game
        switch (rule.strip().toLowerCase()) {
            case "go": game = new Game(boardSize[0], boardSize[1], new GoRules()); break;
            case "gomoku": game = new Game(boardSize[0], boardSize[1], new GomokuRules()); break;
            case "reversi": game = new Game(boardSize[0], boardSize[1], new ReversiRules()); break;
        }

        //temporary: clear the room when each game starts. Will be removed after account system is implemented.
        players = new ArrayList<>();

        //generate players if room is not full
        if (players.size() < 2) {
            //generate a guest player for every vacant identity in current game.
            for (Identity identity: game.identities) {
                if (identity.player == null) {
                    Player newGuestPlayer = new Player("Guest_" + new Random().nextInt(1000));
                    playerLogin(newGuestPlayer);
                    identity.player = newGuestPlayer;
                    game.playerIdentityMap.put(newGuestPlayer, identity);
                }
            }
        }


        //set player with chessType BLACK as the initial player
        for (Identity id: game.identities) {
            if (id.stoneColor.equals(StoneColor.BLACK)) {
                game.currentActingIdentity = id;
            }
        }
        //and set next chess type of the board as well
        game.board.actingStoneColor = StoneColor.BLACK;

        //log & render
        Logger.log("New " + rule + " game with board size (" + boardSize[0] + ", " + boardSize[1] + ")");
        render();

        //for testing only
        if (Config.RANDOM_STEP_TEST) {
            int count = 0;
            while (isGameActive) {
                stepAt(new Position((new Random().nextInt(Config.DEFAULT_BOARD_SIZE)) + 1, (new Random().nextInt(Config.DEFAULT_BOARD_SIZE)) + 1));
                count++;
                if (count > Config.RANDOM_STEP_NUMBER) break;
            }
        }
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
            String winnerName = winner.player.name;
            String winningSide = switch (winner.stoneColor) {
                case BLACK -> "black";
                case WHITE -> "white";
            };
            String winningIdentity = winnerName + " (" + winningSide + ") ";
            Logger.log("Game Over: " + winningIdentity + " wins");
            render();
            Client.popUpMessage(new PromptVO(winningIdentity + "wins!"));
        }
        isGameActive = false;
    }

    public static void playerLogin(Player player) {
        players.add(player);
        Logger.log(player.name + " has joined the game.");
    }

    public static void stepAt(Position position) {
        //do nothing if the game hasn't started or has already ended.
        if (!isGameActive) return;

        //validate position coordinates
        if (game.board.outOfBound(position)) return;

        //execute step on the board
        StepResult stepResult = game.ruleset.takeStep(game.board, position, game.boardHistory);
        if (stepResult.stepSuccess) {
            //update board history
            game.boardHistory.push(new Board(game.board));
            //write log
            Logger.log(game.currentActingIdentity.player.name + game.currentActingIdentity.stoneColor.string() + " takes step at " + position.x + ", "+ position.y + " (Step #" + (game.boardHistory.size()) + ")");
            //reset abstain status
            game.currentActingIdentity.hasAbstained = false;
            //update board status
            game.board = stepResult.boardAfterStep;
            //switch turn
            game.switchTurn();
        }

        //if game is over, call endGame
        if (stepResult.gameOver) {
            endGame(stepResult.winner);
        }

        //render
        render();
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
            if (game.ruleset.getRuleName().equals("Go")) {
                Client.popUpMessage(new PromptVO("Game has ended, please click on dead pieces to remove them.\n" +
                        "Click on the confirm button on the bottom left when you are done."));
                render();
                Client.removeDeadPieces(new RenderVO(players, game.identities, game.currentActingIdentity, game.board, Logger.getLog(Config.MAX_LOG_ENTRIES)));
                return;
            }
            endGame(game.ruleset.scanBoard(game.board).winner);
            return;
        }

        //if the game does not end, change the current acting player
        Logger.log(game.currentActingIdentity.player.name + " abstained, the turn goes to the next player");
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
            Logger.log(actionIdentity.player.name + " cannot withdraw step: maximum withdraw limit reached.");
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

        Logger.log(actionIdentity.player.name + " (" + actionIdentity.stoneColor.string() + ") " + " has withdrawn step #" + (game.boardHistory.size() + 1));

        //reactivate the game if game has ended.
        isGameActive = true;
        render();
    }

    public static void removeDeadPiecesAt(Position position) {
        ((GoRules)game.ruleset).removePieces(game.board, position);
        Logger.log("Action: try to remove dead pieces at " + position.x + "," + position.y);
        render();
    }

    public static void confirmRemoveDeadPieces(Board board) {
        Logger.log("Action: confirm remove dead pieces");
        Client.boardMode = BoardMode.NORMAL;
        endGame(game.ruleset.scanBoard(game.board).winner);
        render();
    }

    public static void saveBoard(String path) {
        try (FileOutputStream fileOut = new FileOutputStream(path)) {
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(game.board);
        } catch (Exception e) {
            System.out.printf(e.getMessage());
        }
    }

    public static void loadBoard(String path) {
        //TODO
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

        //clear current existing players
        players = new ArrayList<>();

        //recover players & identity map
        for (Identity id: game.identities) {
            playerLogin(id.player);
            game.playerIdentityMap.put(id.player, id);
        }

        //activate game status
        isGameActive = true;

        //if game is already over, pop up message
        BoardScanResult scanResult = game.ruleset.scanBoard(game.board);
        if(scanResult.gameOver) endGame(scanResult.winner);

        render();
    }

    //render player info, game board, and logs.
    static void render() {
        Client.render(new RenderVO(players, game.identities, game.currentActingIdentity, game.board, Logger.getLog(Config.MAX_LOG_ENTRIES)));
    }

    private static Identity getCurrentActingIdentityWithChessType(StoneColor stoneColor) {
        for (Identity identity: game.identities) {
            if (identity.stoneColor == stoneColor) return identity;
        }
        return null;
    }
}
