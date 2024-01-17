package refactor.components;

import refactor.server.Server;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterWindow extends BaseWindow {
    public static void pop() {
        createBaseUI("Register");
        JTextField nameField = addTextField("User Name: ");
        JTextField passField = addTextField("Password: ");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String pass = HashUtil.Hash(passField.getText());
                Server.register(name, pass);
                frame.dispose();
            }
        });
    }
}
