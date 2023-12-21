package refactor.client.handler;

import refactor.Server;
import refactor.client.components.SurrenderConfirmWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SurrenderButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        //call server interface
        SurrenderConfirmWindow.pop();
    }
}
