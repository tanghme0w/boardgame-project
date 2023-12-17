package gui.handler;

import gui.Client;

import entity.Position;

public class MouseClickHandler {
    public static void handle(int x, int y) {
        if (1 <= x && x <= Client.game.gameboard.size && 1 <= y && y <= Client.game.gameboard.size) {
            Client.moveCommand.execute(Client.game, Client.game.getCurrentActingPlayer(), new Position(x, y));
        }
    }
}
