package refactor;

import java.io.Serializable;
import java.util.Stack;

public interface Ruleset extends Serializable {
    String getRuleName();
    BoardScanResult scanBoard(Board board);
    StepResult takeStep(Board board, Position position, Stack<Board> boardHistory);
}