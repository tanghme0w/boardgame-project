package refactor.components;

import globals.BoardMode;
import globals.Config;
import refactor.Client;
import refactor.server.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PlayerPanel extends JPanel {
    Integer playerIndex;

    public PlayerPanel (JTextArea playerInfoArea, Integer playerIndex, boolean isCurrentActingPlayer) {
        this.playerIndex = playerIndex;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        playerInfoArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(playerInfoArea);

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEADING));
        header.add(new JLabel("Player" + playerIndex));

        JButton loginButton = new JButton("Login");
        JButton guestButton = new JButton("Join as guest");
        JButton AIButton = new JButton("Add AI");
        AIButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Server.generateAIPlayer(playerIndex);
            }
        });
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginWindow.pop(playerIndex);
            }
        });
        guestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Server.generateGuestPlayer(playerIndex);
            }
        });

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LogoutConfirmWindow.pop(playerIndex);
            }
        });

        if (Client.boardMode == BoardMode.WAIT) {
            if (Client.playerLoginStatus.get(playerIndex)) {
                header.add(logoutButton);
            } else {
                header.add(loginButton);
                header.add(guestButton);
                header.add(AIButton);
            }
        }

        if (Client.boardMode == BoardMode.WAIT && Client.playerISAI.get(playerIndex)) {
            JLabel jLabel = new JLabel("AI Level ");
            JComboBox<Integer> jComboBox = new JComboBox<>(new Integer[]{1, 2});
            jComboBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                     Server.setAILevel(playerIndex, (Integer) e.getItem());
                }
            });
            header.add(jLabel);
            header.add(jComboBox);
        }

        if (Client.boardMode == BoardMode.IN_GAME && !Client.playerISAI.get(playerIndex)) {
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
        }

        header.setMaximumSize(new Dimension(Config.SIDE_PANEL_WIDTH, 20));
        scrollPane.setPreferredSize(new Dimension(Config.SIDE_PANEL_WIDTH, 200));
        scrollPane.setMaximumSize(new Dimension(Config.SIDE_PANEL_WIDTH, 200));

        add(header);
        add(scrollPane);
    }

}
