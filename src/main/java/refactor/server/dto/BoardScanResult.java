package refactor.server.dto;

import globals.StoneColor;

public class BoardScanResult {
    public boolean gameOver;
    public StoneColor winner;
    public BoardScanResult(boolean gameOver, StoneColor winner) {
        this.gameOver = gameOver;
        this.winner = winner;
    }
    public BoardScanResult() {
        this.gameOver = false;
        this.winner = null;
    }
}
