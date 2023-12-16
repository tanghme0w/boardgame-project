package entity;

import ruleset.GoRule;

import java.util.ArrayList;
import java.util.HashMap;

public class GoGame extends BaseGame {
    void initGoGame() {
        this.playerManager = new PlayerManager();
        this.boardHistory = new ArrayList<>(256);
        this.moveHistory = new ArrayList<>(256);
        this.ruleset = new GoRule();
    }
    //use default board size
    GoGame() {
        this.gameboard = new Board(19);
        initGoGame();
    }
    //use custom board size
    GoGame(Integer boardSize) {
        this.gameboard = new Board(boardSize);
        initGoGame();
    }
}
