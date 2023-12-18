package deprecated.command;
import deprecated.entity.*;

public class MoveCommand implements PositionalCommand {
    @Override
    public void execute(ChessGame chessGame, Player player, Position position) {
        //validate move
        //chessGame.ruleset.validate_step(chessGame.gameboard, new Move(player, position));
        //add move history
    }
}