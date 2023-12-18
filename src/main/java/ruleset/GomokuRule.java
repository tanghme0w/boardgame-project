package ruleset;

import deprecated.entity.Board;
import deprecated.entity.Move;

import java.util.List;

public class GomokuRule implements Ruleset {
    @Override
    public Boolean validate_step(Board board, Move move) {
        return null;
    }

    @Override
    public Boolean validate_board(Board board) {
        return null;
    }

    @Override
    public Boolean check_endgame(Board board, List<Move> move_history) {
        return null;
    }

    @Override
    public Integer determine_winner(Board board, List<Move> move_history) {
        return null;
    }
}
