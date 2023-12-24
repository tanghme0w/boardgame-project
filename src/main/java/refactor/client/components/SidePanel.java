package refactor.client.components;

import globals.Config;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SidePanel extends JPanel {
    SidePanel(List<JTextArea> playerInfoAreas, JTextArea logInfoArea, Integer actingPlayerIndex) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(Config.SIDE_PANEL_WIDTH, getHeight()));

        Integer index = 0;
        for (JTextArea playerInfoArea: playerInfoAreas) {
            PlayerPanel playerPanel = new PlayerPanel(playerInfoArea, index, index.equals(actingPlayerIndex));
            playerPanel.setPreferredSize(new Dimension(Config.SIDE_PANEL_WIDTH, 150));
            playerPanel.setMaximumSize(new Dimension(Config.SIDE_PANEL_WIDTH, 150)); // Set maximum size to prevent expansion
            add(playerPanel);
            index++;
        }

        add(Box.createVerticalGlue());

        LogPanel logPanel = new LogPanel(logInfoArea);
        logPanel.setMaximumSize(new Dimension(Config.SIDE_PANEL_WIDTH, 200));
        add(logPanel);
    }
}
