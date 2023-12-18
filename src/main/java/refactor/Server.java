package refactor;

import globals.ChessType;

import java.util.List;
import java.util.Random;

public class Server {
    static ChessGame game;

    static List<Player> players;

    static boolean isGameActive = false;

    public static void newGame(String rule, int[] boardSize) {
        //set state flag
        isGameActive = true;

        //instantiate new game
        switch (rule.strip().toLowerCase()) {
            case "go": game = new ChessGame(boardSize[0], boardSize[1], new GoRules()); break;
            case "gomoku": game = new ChessGame(boardSize[0], boardSize[1], new GomokuRules()); break;
        }

        //generate players if room is not full
        if (players.size() < 2) {
            //generate a guest player for every vacant identity in current game.
            for (Identity identity: game.identities) {
                if (identity.player == null) {
                    Player newGuestPlayer = new Player("Guest" + new Random().nextInt(1000));
                    players.add(newGuestPlayer);
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

        //render
        render();
    }

    public static void endGame(Identity winner) {
        if (winner == null) {
            Logger.log("Game Over: tie.");
            Client.showEndgameMessage(new EndGameVO("It's a tie!"));
        } else {
            String winnerName = winner.player.name;
            String winningSide = switch (winner.chessType) {
                case BLACK -> "black";
                case WHITE -> "white";
            };
            String winningIdentity = winnerName + "(" + winningSide + ")";
            Logger.log("Game Over: " + winningIdentity + "wins");
            Client.showEndgameMessage(new EndGameVO(winningIdentity + "wins!"));
        }
        isGameActive = false;
    }

    public static void playerLogin(Player player) {
        players.add(player);
    }

    public static void move(Position position) {
        //do nothing if the game hasn't started or has already ended.
        if (!isGameActive) return;

        //execute step on the board
        if (game.ruleset.take_step(game.board, position)) {
            //if step success, switch turn, update board history, and reset abstain counter to 0
            game.currentActingIdentity.hasAbstained = false;
            game.switchTurn();
            game.updateBoardHistory();
        }

        //render
        render();
    }

    public static void abstain() {
        //do nothing if the game hasn't started or has already ended.
        if (!isGameActive) return;

        //if all players have abstained, game ends
        game.currentActingIdentity.hasAbstained = true;
        boolean gameEnd = true;
        for(Identity id: game.identities) {
            gameEnd &= id.hasAbstained;
        }

        //if the game ends, go to endGame operation
        if (gameEnd) {
            Logger.log("All players have abstained, the game ends.");
            //for gomoku game, abstain twice means tie
            if (game.ruleset.getClass() == GomokuRules.class) {
                endGame(null);
            }
            //for go game, abstain twice means we can begin determining the winner
            else if (game.ruleset.getClass() == GoRules.class) {
                ChessType winnerChessType = ((GoRules)game.ruleset).determine_winner(game.board);
                for (Identity id: game.identities) {
                    if (id.chessType.equals(winnerChessType)) {
                        endGame(id);
                    }
                }
            }
        }

        //if the game does not end, change the current acting player
        game.switchTurn();
        Logger.log(game.currentActingIdentity.player.name + "abstained, the turn goes to next player");

        //render
        render();
    }

    public static void surrender() {
        //do nothing if the game hasn't started or has already ended.
        if (!isGameActive) return;

        //find the other side and declare victory
        for (Identity id: game.identities) {
            if(!id.equals(game.currentActingIdentity)) {
                endGame(id);
            }
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
                game.moveHistory,
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
        Client.render(new RenderVO(game.identities, game.board, Logger.getLog(Config.MaxLogEntries)));
    }
}
