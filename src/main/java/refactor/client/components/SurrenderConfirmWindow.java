package refactor.client.components;

import refactor.Server;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SurrenderConfirmWindow {
    public static void pop(Integer playerIndex) {
        // Create frame
        JFrame frame = new JFrame("Surrender");

        // Create panel
        JPanel panel = new JPanel();

        // Create label
        JLabel label = new JLabel("Are you sure you would like to surrender?");

        // Create button
        JButton confirmButton = new JButton("Confirm");
        JButton cancelButton = new JButton("Cancel");

        // Add action listener to button
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Server.surrender(playerIndex);
                frame.dispose();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        // Add components to panel
        panel.add(label);
        panel.add(confirmButton);
        panel.add(cancelButton);

        // Add panel to frame
        frame.add(panel);

        // Set frame properties
        frame.setSize(300, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
