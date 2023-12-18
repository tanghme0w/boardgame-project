package deprecated.entity;

import deprecated.logger.Logger;
import deprecated.ruleset.Ruleset;

import java.util.List;
import java.util.Random;

public class ChessGame {
    public Board gameboard;
    public PlayerManager playerManager;
    public List<Board> boardHistory;
    public List<Move> moveHistory;
    public Ruleset ruleset;
    public Player playerJoin(Player player) {
        return playerManager.addNewPlayer(player);
    }
    public void playerLeave(Player player) {
        playerManager.removePlayer(player);
    }
    public Player getCurrentActingPlayer() {
        return gameboard.currentActingPlayer;
    }
    public Player getPlayerInfo(Integer index) {
        return playerManager.getPlayerWithIndex(index);
    }
    public void startGame() {
        //check if players are enough, if not, generate random player
        if (!playerManager.roomFull()) {
            Logger.log("Adding random player to the game.");
            while (!playerManager.roomFull()) {
                playerManager.addNewPlayer(PlayerFactory.createPlayer("guest_"+ new Random().nextInt(10000)));
            }
        }
        setInitialPlayer();
    }
    void setInitialPlayer() {
        gameboard.currentActingPlayer = playerManager.getPlayerWithIdentity(Identity.BLACK);
    }
    void saveGame() {
    }
    void loadGame(String fileName) {
    }
}
