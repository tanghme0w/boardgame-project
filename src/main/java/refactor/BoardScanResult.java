package refactor;

import globals.ChessType;

public class BoardScanResult {
    boolean gameOver;
    ChessType winner;
    public BoardScanResult(boolean gameOver, ChessType winner) {
        this.gameOver = gameOver;
        this.winner = winner;
    }
    public BoardScanResult() {
        this.gameOver = false;
        this.winner = null;
    }
}
