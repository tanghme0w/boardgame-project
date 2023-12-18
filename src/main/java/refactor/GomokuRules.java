package refactor;

public class GomokuRules implements Ruleset {
    @Override
    public boolean take_step(Board board, Position position) {
        return true;
    }
}
