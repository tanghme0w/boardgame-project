package refactor.handler;

import globals.Config;
import refactor.server.Server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewReversiGameButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        Server.newGame("reversi", new int[]{Config.DEFAULT_BOARD_SIZE, Config.DEFAULT_BOARD_SIZE});
    }
}
