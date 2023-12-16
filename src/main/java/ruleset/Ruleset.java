package ruleset;

import entity.*;

import java.util.List;
public interface Ruleset {
    Boolean validate_step(Board board, Move move);
    Boolean validate_board(Board board);
    Boolean check_endgame(Board board, List<Move> move_history);
    Integer determine_winner(Board board, List<Move> move_history);
}
