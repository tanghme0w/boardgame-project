package refactor.client.handler;

import refactor.Server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SurrenderButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        //TODO pop double-check window
        //call server interface
        Server.surrender();
    }
}
