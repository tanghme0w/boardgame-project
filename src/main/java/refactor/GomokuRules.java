package refactor;

import java.util.List;

public class GomokuRules implements Ruleset {
    @Override
    public boolean take_step(Board board, List<Board> boardHistory, List<Move> moveHistory, Position position) {
        return false;
    }
}
