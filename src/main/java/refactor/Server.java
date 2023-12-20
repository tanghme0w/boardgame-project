package refactor;

import globals.ChessType;
import globals.Config;
import refactor.client.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Server {
    static ChessGame game = null;

    static List<Player> players = new ArrayList<>();

    static boolean isGameActive = false;

    public static void newGame(String rule, int[] boardSize) {
        //set state flag
        isGameActive = true;

        //instantiate new game
        switch (rule.strip().toLowerCase()) {
            case "go": game = new ChessGame(boardSize[0], boardSize[1], new GoRules()); break;
            case "gomoku": game = new ChessGame(boardSize[0], boardSize[1], new GomokuRules()); break;
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
            if (id.chessType.equals(ChessType.BLACK)) {
                game.currentActingIdentity = id;
            }
        }
        //and set next chess type of the board as well
        game.board.nextChessType = ChessType.BLACK;


        //log & render
        Logger.log("New " + rule + " game with board size (" + boardSize[0] + ", " + boardSize[1] + ")");
        render();
    }

    public static void endGame(ChessType winnerChessType) {
        //get winner identity
        Identity winner = null;
        for (Identity id: game.identities) {
            if (id.chessType.equals(winnerChessType))
                winner = id;
        }
        if (winner == null) {
            Logger.log("Game Over: tie.");
            render();
            Client.showEndgameMessage(new EndGameVO("It's a tie!"));
        } else {
            String winnerName = winner.player.name;
            String winningSide = switch (winner.chessType) {
                case BLACK -> "black";
                case WHITE -> "white";
            };
            String winningIdentity = winnerName + " (" + winningSide + ") ";
            Logger.log("Game Over: " + winningIdentity + " wins");
            render();
            Client.showEndgameMessage(new EndGameVO(winningIdentity + "wins!"));
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
        StepResult stepResult = game.ruleset.takeStep(game.board, position, game.stepHistory.size() + 1, game.boardHistory);
        if (stepResult.stepSuccess) {
            //write log
            String chessType = switch (game.currentActingIdentity.chessType) {
                case BLACK -> " (black) ";
                case WHITE -> " (white) ";
            };
            Logger.log(game.currentActingIdentity.player.name + chessType + " takes step at " + position.x + ", "+ position.y);
            //reset abstain status
            game.currentActingIdentity.hasAbstained = false;
            //update step history
            game.stepHistory.push(new Step(game.board.nextChessType, position, game.stepHistory.size() + 1));
            //update board status
            game.board = stepResult.boardAfterStep;
            //switch turn
            game.switchTurn();
            //update board history
            game.boardHistory.push(new Board(stepResult.boardAfterStep));
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
            endGame(game.ruleset.scanBoard(game.board).winner);
            return;
        }

        //if the game does not end, change the current acting player
        Logger.log(game.currentActingIdentity.player.name + " abstained, the turn goes to the next player");
        game.switchTurn();

        //render
        render();
    }

    public static void surrender() {
        //do nothing if the game hasn't started or has already ended.
        if (!isGameActive) return;

        //declare victory for the other side
        switch (game.currentActingIdentity.chessType) {
            case BLACK -> endGame(ChessType.WHITE);
            case WHITE -> endGame(ChessType.BLACK);
        }
    }

    public static void withdraw() {
        //if the last move is made by current player
        if(game.stepHistory.getLast().chessType.equals(game.currentActingIdentity.chessType)) {
            //TODO withdraw last move
        } else {
            //if the last move is made by other player
            //TODO withdraw two moves
        }
    }

    public static void saveBoard(String path) {
        //TODO
    }

    public static void loadBoard(String path) {
        //TODO
    }

    public static void saveGame(String path) {
        GameMemento memento = new GameMemento(
                game.board,
                game.identities,
                game.currentActingIdentity,
                game.boardHistory,
                game.stepHistory,
                game.ruleset
        );
    }

    public static void loadGame(String path) {
        //TODO load from file and acquire GameMemento
        GameMemento gameMemento = null;
        //recover game
        game = new ChessGame(gameMemento);
        //recover players & identity map
        for (Identity id: game.identities) {
            playerLogin(id.player);
            game.playerIdentityMap.put(id.player, id);
        }
    }

    //render player info, game board, and logs.
    private static void render() {
        Client.render(new RenderVO(game.identities, game.currentActingIdentity, game.board, Logger.getLog(Config.MAX_LOG_ENTRIES)));
    }
}
