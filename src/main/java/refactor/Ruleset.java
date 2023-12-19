package refactor;

import java.util.List;

public interface Ruleset {
    boolean take_step(Board board, List<Board> boardHistory, List<Move> moveHistory, Position position);
}