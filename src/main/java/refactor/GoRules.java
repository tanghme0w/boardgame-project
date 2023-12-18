package refactor;

import globals.ChessType;

public class GoRules implements Ruleset {
    @Override
    public boolean take_step(Board board, Position position) {
        //validate step
            //suicide prohibited
            //repeating step prohibited
        //remove pieces
        return true;
    }
    ChessType determine_winner(Board board) {
        return null;
    }
}
