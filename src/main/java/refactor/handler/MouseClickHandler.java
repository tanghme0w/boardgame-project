package refactor.handler;

import refactor.server.entity.Position;
import refactor.server.Server;

public class MouseClickHandler {
    public static void handle(int x, int y) {
        Server.stepAt(new Position(x, y));
    }
}
