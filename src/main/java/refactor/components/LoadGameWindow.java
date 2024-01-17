package refactor.components;
import refactor.server.Server;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class LoadGameWindow extends BaseWindow {
    public static void pop() {
        createBaseUI("Load Game");
        JTextField pathField = addTextField("Enter file path: ");

        // Add action listener to button
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputText = pathField.getText();
                Server.loadGame(inputText);
                frame.dispose();
            }
        });
    }
}
