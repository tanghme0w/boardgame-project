package refactor.client.components;

import globals.Config;
import refactor.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayerPanel extends JPanel {
    Integer playerIndex;

    public PlayerPanel (JTextArea playerInfoArea, Integer playerIndex, boolean isCurrentActingPlayer) {
        this.playerIndex = playerIndex;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        playerInfoArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(playerInfoArea);

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEADING));
        header.add(new JLabel("Player" + playerIndex));
        JButton withdrawButton = new JButton("Withdraw");
        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Server.withdraw(playerIndex);
            }
        });
        JButton surrenderButton = new JButton("Surrender");
        surrenderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SurrenderConfirmWindow.pop(playerIndex);
            }
        });
        header.add(withdrawButton);
        header.add(surrenderButton);

        if(isCurrentActingPlayer) {
            JButton abstainButton = new JButton("Abstain");
            abstainButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Server.abstain();
                }
            });
            header.add(abstainButton);
        }

        header.setMaximumSize(new Dimension(Config.SIDE_PANEL_WIDTH, 20));
        scrollPane.setPreferredSize(new Dimension(Config.SIDE_PANEL_WIDTH, 200));
        scrollPane.setMaximumSize(new Dimension(Config.SIDE_PANEL_WIDTH, 200));

        add(header);
        add(scrollPane);
    }

}
