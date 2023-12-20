package refactor;

import java.util.Stack;

public interface Ruleset {
    BoardScanResult scanBoard(Board board);
    StepResult takeStep(Board board, Position position, Integer stepCount, Stack<Board> boardHistory);
}