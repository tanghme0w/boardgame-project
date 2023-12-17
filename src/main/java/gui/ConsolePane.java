package gui;

import com.sun.tools.jconsole.JConsoleContext;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class ConsolePane extends JDialog {
    private static final ResourceBundle resourceBundle = MainFrame.resourceBundle;
    private final JTextField textCommand = new JTextField();
    private final JLabel labelCommand = new JLabel();
    private JScrollPane scrollPane;
    private final JTextPane console;

    public ConsolePane(Window owner) {
        super(owner);
        setTitle("Console Pane");

        Insets oi = owner.getInsets();
        setBounds(0,
                owner.getY() - oi.top,
                Math.max(owner.getX() - oi.left, 400),
                Math.max(owner.getHeight() + oi.top + oi.bottom, 300));

        console = new JTextPane();
        console.setBorder(BorderFactory.createEmptyBorder());
        console.setEditable(false);
        scrollPane = new JScrollPane();
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        textCommand.setBackground(Color.DARK_GRAY);
        textCommand.setForeground(Color.WHITE);
        labelCommand.setFont(new Font("Tahoma", Font.BOLD, 11));
        labelCommand.setOpaque(true);
        labelCommand.setBackground(Color.DARK_GRAY);
        labelCommand.setForeground(Color.WHITE);
        labelCommand.setText("HELLO WORLD");
        scrollPane.setViewportView(console);
        getRootPane().setBorder(BorderFactory.createEmptyBorder());
        getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
    }
}
