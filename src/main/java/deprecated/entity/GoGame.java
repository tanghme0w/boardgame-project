package deprecated.entity;

import ruleset.GoRule;
import java.util.ArrayList;

public class GoGame extends ChessGame {
    void initGoGame() {
        this.playerManager = new PlayerManager();
        this.boardHistory = new ArrayList<>(256);
        this.moveHistory = new ArrayList<>(256);
        this.ruleset = new GoRule();
    }
    //use default board size
    public GoGame() {
        this.gameboard = new Board(19);
        initGoGame();
    }
    //use custom board size
    public GoGame(Integer boardSize) {
        this.gameboard = new Board(boardSize);
        initGoGame();
    }
}
