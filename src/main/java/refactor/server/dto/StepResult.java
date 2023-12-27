package refactor.server.dto;

import globals.StoneColor;
import refactor.server.entity.Board;

public class StepResult {
    public boolean stepSuccess;
    public Board boardAfterStep;
    public boolean gameOver;
    public StoneColor winner;
    public StepResult(boolean stepSuccess, boolean gameOver, Board boardAfterStep, StoneColor winner) {
        this.stepSuccess = stepSuccess;
        this.boardAfterStep = boardAfterStep;
        this.gameOver = gameOver;
        this.winner = winner;
    }
}
