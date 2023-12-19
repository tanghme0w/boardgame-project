package refactor;

import globals.ChessType;

public class Move {
    ChessType chessType;
    Position position;
    Integer count;
    //deep copy construct
    Move(Move move) {
        chessType = move.chessType;
        position = new Position(move.position.x, move.position.y);
        count = move.count;
    }
    Move(ChessType chessType, Position position, Integer count) {
        this.chessType = chessType;
        this.position = position;
        this.count = count;
    }
}
