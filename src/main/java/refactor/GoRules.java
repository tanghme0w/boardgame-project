package refactor;

import globals.ChessType;

import java.util.List;

public class GoRules implements Ruleset {
    ChessType determine_winner(Board board) {
        return null;
    }

    @Override
    public boolean take_step(Board board, List<Board> boardHistory, List<Move> moveHistory, Position position) {
        //validate step
        //suicide prohibited
        //repeating step prohibited
        //remove pieces if needed
        //update board history
        boardHistory.add(new Board(board));
        //update move history
        Move move = new Move(board.nextChessType, position, moveHistory.size() + 1);
        moveHistory.add(move);
        return true;
    }
}
