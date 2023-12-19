package refactor.client.components;

import globals.Config;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SidePanel extends JPanel {
    SidePanel(List<JTextArea> playerInfoAreas, JTextArea logInfoArea, List<JButton> buttons) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(Config.SIDE_PANEL_WIDTH, getHeight()));

        // Refactor repeated code into a method
        for (JTextArea playerInfoArea: playerInfoAreas) {
            add(createTextAreaWithScrollPane(playerInfoArea, Config.SIDE_PANEL_WIDTH, getHeight() / 4, false));
        }
        add(createTextAreaWithScrollPane(logInfoArea, Config.SIDE_PANEL_WIDTH, getHeight() / 2, false));

        // Add buttons
        for (JButton button: buttons) {
            add(button);
        }
    }

    private JScrollPane createTextAreaWithScrollPane(JTextArea textArea, int width, int height, boolean editable) {
        textArea.setEditable(editable);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(width, height));
        return scrollPane;
    }
}
