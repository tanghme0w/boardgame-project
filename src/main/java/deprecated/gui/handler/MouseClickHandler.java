package deprecated.gui.handler;

import deprecated.gui.Client;

import deprecated.entity.Position;

public class MouseClickHandler {
    public static void handle(int x, int y) {
        if (1 <= x && x <= Client.chessGame.gameboard.size && 1 <= y && y <= Client.chessGame.gameboard.size) {
            Client.moveCommand.execute(Client.chessGame, Client.chessGame.getCurrentActingPlayer(), new Position(x, y));
        }
    }
}
