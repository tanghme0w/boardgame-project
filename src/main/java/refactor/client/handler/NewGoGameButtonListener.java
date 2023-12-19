package refactor.client.handler;

import deprecated.entity.GoGame;
import deprecated.gui.Client;
import deprecated.logger.Logger;
import refactor.Server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewGoGameButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        //pop input box
        //start game
        Server.newGame("go", new int[]{19, 19});
    }
}
