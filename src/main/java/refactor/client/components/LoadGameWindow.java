package refactor.client.components;
import refactor.Server;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class LoadGameWindow {
    public static void pop() {
        // Create frame
        JFrame frame = new JFrame("Load Game");

        // Create panel
        JPanel panel = new JPanel();

        // Create label
        JLabel label = new JLabel("Enter file path:");

        // Create text field
        JTextField textField = new JTextField(20);

        // Create button
        JButton confirmButton = new JButton("Confirm");
        JButton cancelButton = new JButton("Cancel");

        // Add action listener to button
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputText = textField.getText();
                Server.loadGame(inputText);
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
        panel.add(textField);
        panel.add(confirmButton);
        panel.add(cancelButton);

        // Add panel to frame
        frame.add(panel);

        // Set frame properties
        frame.setSize(300, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
