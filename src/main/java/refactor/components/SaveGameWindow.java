package refactor.components;
import refactor.server.Server;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class SaveGameWindow extends BaseWindow {
    public static void pop() {
        createBaseUI("Save Game");
        JTextField pathField = addTextField("Enter file path: ");

        // Add action listener to button
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputText = pathField.getText();
                Server.saveGame(inputText);
                frame.dispose();
            }
        });
    }
}
