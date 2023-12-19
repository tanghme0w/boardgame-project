package refactor.client.handler;

import refactor.Position;
import refactor.Server;

public class MouseClickHandler {
    public static void handle(int x, int y) {
        Server.stepAt(new Position(x, y));
    }
}
