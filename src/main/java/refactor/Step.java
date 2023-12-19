package refactor;

import globals.ChessType;

public class Step {
    ChessType chessType;
    Position position;
    Integer count;
    //deep copy construct
    Step(Step step) {
        chessType = step.chessType;
        position = new Position(step.position.x, step.position.y);
        count = step.count;
    }
    Step(ChessType chessType, Position position, Integer count) {
        this.chessType = chessType;
        this.position = position;
        this.count = count;
    }
}
