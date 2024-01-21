package refactor.components;

import refactor.server.Server;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginWindow extends BaseWindow {
    public static void pop(Integer playerIdx) {
        createBaseUI("Player" + playerIdx + " login");
        JTextField nameField = addTextField("User Name: ");
        JPasswordField passField = addPasswordField("Password: ");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String pass = HashUtil.Hash(passField.getText());
                Server.login(name, pass, playerIdx);
                frame.dispose();
            }
        });
    }
}
