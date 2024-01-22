package refactor.components;

import javax.swing.*;
import java.awt.*;

public abstract class BaseWindow {
    protected static JFrame frame;
    protected static JPanel buttonPanel;
    protected static JPanel formPanel;
    protected static JButton confirmButton;
    protected static JButton cancelButton;
    protected static Integer fieldCount;

    protected static void createBaseUI(String title) {
        fieldCount = 0;
        frame = new JFrame(title);
        frame.setLayout(new BorderLayout());
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        formPanel = new JPanel();
        formPanel.setLayout(new FlowLayout());
        confirmButton = new JButton("Confirm");
        cancelButton = new JButton("Cancel");

        // Common listener for cancel button
        cancelButton.addActionListener(e -> frame.dispose());

        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.add(formPanel, BorderLayout.CENTER);

        frame.setSize(300, 75 + fieldCount * 50);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    protected static JTextField addTextField(String label) {
        JLabel jLabel = new JLabel(label);
        JTextField jTextField = new JTextField(20);
        formPanel.add(jLabel);
        formPanel.add(jTextField);
        fieldCount++;
        frame.setSize(300, 75 + fieldCount * 50);
        return jTextField;
    }

    protected static JPasswordField addPasswordField() {
        JLabel jLabel = new JLabel("Password: ");
        JPasswordField jPasswordField = new JPasswordField(20);
        formPanel.add(jLabel);
        formPanel.add(jPasswordField);
        fieldCount++;
        frame.setSize(300, 75 + fieldCount * 50);
        return jPasswordField;
    }

    protected static void addLabel(String label) {
        JLabel jLabel = new JLabel(label);
        formPanel.add(jLabel);
        fieldCount++;
        frame.setSize(300, 75 + fieldCount * 50);
    }
}
