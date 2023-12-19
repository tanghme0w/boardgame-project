package refactor.client.handler;

import refactor.Server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WithdrawButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        Server.withdraw();
    }
}
