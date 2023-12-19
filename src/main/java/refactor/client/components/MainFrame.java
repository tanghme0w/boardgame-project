package refactor.client.components;

import globals.Config;
import refactor.Board;
import refactor.client.handler.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {
    List<JTextArea> playerInfoArea;
    JTextArea logArea;
    BoardPanel boardPanel;
    SidePanel sidePanel;
    BottomPanel bottomPanel;
    JButton saveGameButton;
    JButton loadGameButton;
    JButton newGoGameButton;
    JButton newGomokuGameButton;
    JButton abstainButton;
    JButton surrenderButton;
    JButton withdrawButton;
    public MainFrame() {
        setTitle("Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(((Config.MAX_BOARD_SIZE + 3) * Config.CELL_SIZE) + Config.SIDE_PANEL_WIDTH,
                (Config.MAX_BOARD_SIZE + 3) * Config.CELL_SIZE);
        setLocationRelativeTo(null); //center the window

        initBoardPanel();
        initSidePanel();
        initBottomPanel();
        add(boardPanel, BorderLayout.CENTER);
        add(sidePanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    public void setPlayerText(Integer id, List<String> textLines) {
        StringBuilder finalText = new StringBuilder("Player" + id + "\n");
        for (String text: textLines) {
            finalText.append(text).append("\n");
        }
        playerInfoArea.get(id).setText(String.valueOf(finalText));
    }

    public void setLogText(List<String> textLines) {
        StringBuilder finalText = new StringBuilder("Log\n");
        for (String text: textLines) {
            finalText.append("**").append(text).append("\n");
        }
        logArea.setText(String.valueOf(finalText));
    }

    public void repaintBoard(Board board) {
        boardPanel.board = board;
        boardPanel.repaint();
    }

    private void initBoardPanel() {
        boardPanel = new BoardPanel();
        boardPanel.board = new Board(Config.MAX_BOARD_SIZE, Config.MAX_BOARD_SIZE);
    }

    private void initSidePanel() {
        //init player info area
        playerInfoArea = new ArrayList<>();
        for (int i = 0; i < Config.MAX_PLAYERS; i++) {
            playerInfoArea.add(new JTextArea("Player" + i));
        }
        //init log area
        logArea = new JTextArea("Log");
        //init buttons & register action listeners
        List<JButton> sidePanelButtons = new ArrayList<>();
        saveGameButton = new JButton("save game"); saveGameButton.addActionListener(new SaveGameButtonListener());
        loadGameButton = new JButton("load game"); loadGameButton.addActionListener(new LoadGameButtonListener());
        newGoGameButton = new JButton("new Go game"); newGoGameButton.addActionListener(new NewGoGameButtonListener());
        newGomokuGameButton = new JButton("new Gomoku game"); newGomokuGameButton.addActionListener(new NewGomokuGameButtonListener());
        sidePanelButtons.add(saveGameButton);
        sidePanelButtons.add(loadGameButton);
        sidePanelButtons.add(newGoGameButton);
        sidePanelButtons.add(newGomokuGameButton);
        //initialize side panel
        sidePanel = new SidePanel(
                playerInfoArea,
                logArea,
                sidePanelButtons
        );
    }

    private void initBottomPanel() {
        //init buttons
        List<JButton> bottomPanelButtons = new ArrayList<>();
        abstainButton = new JButton("abstain step"); abstainButton.addActionListener(new AbstainButtonListener());
        withdrawButton = new JButton("withdraw step"); withdrawButton.addActionListener(new WithdrawButtonListener());
        surrenderButton = new JButton("surrender"); surrenderButton.addActionListener(new SurrenderButtonListener());
        bottomPanelButtons.add(abstainButton);
        bottomPanelButtons.add(withdrawButton);
        bottomPanelButtons.add(surrenderButton);
        bottomPanel = new BottomPanel(bottomPanelButtons);
    }
}
