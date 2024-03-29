package refactor.components;

import refactor.server.Server;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SurrenderConfirmWindow extends BaseWindow {
    public static void pop(Integer playerIndex) {
        createBaseUI("Surrender");
        addLabel("Are you sure you would like to surrender?");
        // Add action listener to button
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Server.surrender(playerIndex);
                frame.dispose();
            }
        });
    }
}
