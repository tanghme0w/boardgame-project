package command;
import entity.*;

interface NonPositionalCommand {
    void execute(BaseGame game, Player player);
}

