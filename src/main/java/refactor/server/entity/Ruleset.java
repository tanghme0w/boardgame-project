package refactor.server.entity;

import refactor.server.dto.BoardScanResult;
import refactor.server.dto.StepResult;

import java.io.Serializable;
import java.util.List;
import java.util.Stack;

public interface Ruleset extends Serializable {
    List<Action> evaluateActions(Board board);
    void init(Board board);
    String getRuleName();
    BoardScanResult scanBoard(Board board);
    StepResult takeStep(Board board, Position position, Stack<Board> boardHistory);
}