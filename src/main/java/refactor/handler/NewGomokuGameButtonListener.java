package refactor.handler;

import globals.Config;
import refactor.server.Server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewGomokuGameButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        //pop input window
        //new game
        Server.newGame("gomoku", new int[]{Config.DEFAULT_BOARD_SIZE, Config.DEFAULT_BOARD_SIZE});
    }
}
