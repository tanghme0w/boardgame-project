package refactor;

import globals.ChessType;

import java.util.List;

public class GoRules implements Ruleset {
    ChessType determine_winner(Board board) {
        return null;
    }

    @Override
    public boolean take_step(Board board, List<Board> boardHistory, List<Step> stepHistory, Position position) {
        //validate step
        //suicide prohibited
        //repeating step prohibited
        //remove pieces if needed
        //update board history
        boardHistory.add(new Board(board));
        //update move history
        Step step = new Step(board.nextChessType, position, stepHistory.size() + 1);
        stepHistory.add(step);
        return true;
    }
}
