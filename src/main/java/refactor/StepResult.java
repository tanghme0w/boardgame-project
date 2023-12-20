package refactor;

import globals.ChessType;

public class StepResult {
    boolean stepSuccess;
    Board boardAfterStep;
    boolean gameOver;
    ChessType winner;
    StepResult(boolean stepSuccess, boolean gameOver, Board boardAfterStep, ChessType winner) {
        this.stepSuccess = stepSuccess;
        this.boardAfterStep = boardAfterStep;
        this.gameOver = gameOver;
        this.winner = winner;
    }
}
