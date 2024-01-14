package refactor.server.dto;

import globals.StoneColor;

public class BoardScanResult {
    public boolean boardIsLegit;
    public boolean gameOver;
    public StoneColor winner;
    public BoardScanResult(boolean boardIsLegit, boolean gameOver, StoneColor winner) {
        this.boardIsLegit = boardIsLegit;
        this.gameOver = gameOver;
        this.winner = winner;
    }
}
