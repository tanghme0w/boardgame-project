package deprecated.command;
import deprecated.entity.*;

interface PositionalCommand {
    void execute(ChessGame chessGame, Player player, Position position);
}
