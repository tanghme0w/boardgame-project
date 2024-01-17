package refactor.components;

import refactor.server.Server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LogoutConfirmWindow extends BaseWindow {
    public static void pop(Integer playerIdx) {
        createBaseUI("Logout");
        addLabel("Player" + playerIdx + ", are you sure you would like to log out?");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Server.logout(playerIdx);
                frame.dispose();
            }
        });
    }
}
