package command;
import entity.*;

interface PositionalCommand {
    void execute(BaseGame game, Player player, Position position);
}
