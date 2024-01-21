package refactor.handler;

import refactor.Client;
import refactor.server.entity.Position;
import refactor.server.Server;

public class MouseClickHandler {
    public static void handle(int x, int y) {
        if (!Client.isAIActing) {
            Server.stepAt(new Position(x, y));
        }
    }
}
