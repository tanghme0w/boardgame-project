package entity;

import ruleset.Ruleset;

import java.util.List;

public class BaseGame {
    Board gameboard;
    PlayerManager playerManager;
    List<Board> boardHistory;
    List<Move> moveHistory;
    Ruleset ruleset;
    Player playerJoin(Player player) {
        return playerManager.addNewPlayer(player);
    }
    void playerLeave(Player player) {
        playerManager.removePlayer(player);
    }
    void startGame() {
        //check if players are enough, if not, generate random player
    }
    void saveGame() {

    }
    void loadGame(String fileName) {

    }
    void Render() {

    }
}
