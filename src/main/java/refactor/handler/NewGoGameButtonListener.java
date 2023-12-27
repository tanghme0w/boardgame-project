package refactor.handler;

import globals.Config;
import refactor.server.Server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewGoGameButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        //pop input box
        //start game
        Server.newGame("go", new int[]{Config.DEFAULT_BOARD_SIZE, Config.DEFAULT_BOARD_SIZE});
    }
}
