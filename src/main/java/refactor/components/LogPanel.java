package refactor.components;

import globals.Config;

import javax.swing.*;
import java.awt.*;

public class LogPanel extends JPanel {
    LogPanel(JTextArea logInfoArea) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel header = new JPanel();
        header.setLayout(new FlowLayout(FlowLayout.LEADING));
        header.setMaximumSize(new Dimension(Config.SIDE_PANEL_WIDTH, 20));
        JLabel logLabel = new JLabel("Log");
        header.add(logLabel);

        logInfoArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logInfoArea);
        scrollPane.setPreferredSize(new Dimension(Config.SIDE_PANEL_WIDTH, 300));
        scrollPane.setMaximumSize(new Dimension(Config.SIDE_PANEL_WIDTH, 300));

        add(header);
        add(scrollPane);
    }
}
