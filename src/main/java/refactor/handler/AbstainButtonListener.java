package refactor.handler;

import refactor.server.Server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AbstainButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        Server.abstain();
    }
}
