package deprecated.command;
import deprecated.entity.*;

interface NonPositionalCommand {
    void execute(ChessGame chessGame, Player player);
}

