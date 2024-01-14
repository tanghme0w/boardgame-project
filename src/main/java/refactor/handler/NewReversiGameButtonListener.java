package refactor.handler;

import refactor.server.Server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewReversiGameButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        Server.newGame("reversi", new int[]{8, 8});
    }
}
