package refactor.client.handler;

import refactor.Logger;
import refactor.Position;
import refactor.Server;

public class MouseClickHandler {
    public static void handle(int x, int y) {
        Server.move(new Position(x, y));
    }
}
