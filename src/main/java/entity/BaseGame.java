package entity;

import ruleset.Ruleset;

import java.util.List;

public class BaseGame {
    Board gameboard;
    PlayerManager playerManager;
    List<Board> boardHistory;
    List<Move> moveHistory;
    Ruleset ruleset;
    void registerPlayer(Player player) {
        playerManager.setPlayer(player);
    }
    void unregisterPlayer(Player player) {
        playerManager.removePlayer(player);
    }
    void saveGame() {

    }
    void loadGame(String fileName) {

    }
}
