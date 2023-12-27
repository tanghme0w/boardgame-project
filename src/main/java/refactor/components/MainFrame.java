package refactor.components;

import globals.BoardMode;
import globals.Config;
import refactor.server.entity.Board;
import refactor.server.Server;
import refactor.handler.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    Integer currentActingPlayerIndex;
    public MainFrame() {
        setTitle("Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(((Config.MAX_BOARD_SIZE + 1) * Config.CELL_SIZE) + Config.SIDE_PANEL_WIDTH,
                (Config.MAX_BOARD_SIZE + 3) * Config.CELL_SIZE);
        currentActingPlayerIndex = -1;
        setLocationRelativeTo(null); //center the window

        initBoardPanel();
        initSidePanel();
        initBottomPanel();
        add(boardPanel, BorderLayout.CENTER);
        add(sidePanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    public void setPlayerText(Integer id, List<String> textLines) {
        StringBuilder finalText = new StringBuilder();
        for (String text: textLines) {
            finalText.append(text).append("\n");
        }
        playerInfoArea.get(id).setText(String.valueOf(finalText));
    }

    public void setLogText(List<String> textLines) {
        StringBuilder finalText = new StringBuilder();
        for (String text: textLines) {
            finalText.append("**").append(text).append("\n");
        }
        logArea.setText(String.valueOf(finalText));
    }

    public void repaintBoard(Board board, BoardMode mode) {
        remove(boardPanel);
        boardPanel = new BoardPanel(mode);
        boardPanel.board = board;
        add(boardPanel);
        this.revalidate();
        this.repaint();
    }

    private void initBoardPanel() {
        boardPanel = new BoardPanel(BoardMode.NORMAL);
        boardPanel.board = new Board(Config.MAX_BOARD_SIZE, Config.MAX_BOARD_SIZE);
    }

    private void initSidePanel() {
        //init player info area
        playerInfoArea = new ArrayList<>();
        for (int i = 0; i < Config.MAX_PLAYERS; i++) {
            playerInfoArea.add(new JTextArea());
        }
        //init log area
        logArea = new JTextArea();
        //initialize side panel
        sidePanel = new SidePanel(
                playerInfoArea,
                logArea,
                currentActingPlayerIndex
        );
    }

    public void refreshSidePanel() {
        remove(sidePanel);
        sidePanel = new SidePanel(playerInfoArea,
                logArea,
                currentActingPlayerIndex
        );
        add(sidePanel, BorderLayout.EAST);
        this.revalidate();
        this.repaint();
    }

    public void refreshBottomPanel(BoardMode mode) {
        remove(bottomPanel);
        if (mode.equals(BoardMode.NORMAL)) {
            initBottomPanel();
        } else if (mode.equals(BoardMode.REMOVE)) {
            remove(bottomPanel);
            JButton confirmRemoveButton = new JButton("Confirm. Let's see who is the winner!");
            confirmRemoveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Server.confirmRemoveDeadPieces(boardPanel.board);
                }
            });
            List<JButton> buttons = new ArrayList<>();
            buttons.add(confirmRemoveButton);
            bottomPanel = new BottomPanel(buttons);
        }
        add(bottomPanel, BorderLayout.SOUTH);
        this.revalidate();
        this.repaint();
    }

    public void setCurrentActingPlayerIndex(Integer index) {
        currentActingPlayerIndex = index;
    }

    private void initBottomPanel() {
        //init buttons
        List<JButton> bottomPanelButtons = new ArrayList<>();
        saveGameButton = new JButton("save game"); saveGameButton.addActionListener(new SaveGameButtonListener());
        loadGameButton = new JButton("load game"); loadGameButton.addActionListener(new LoadGameButtonListener());
        newGoGameButton = new JButton("new Go game"); newGoGameButton.addActionListener(new NewGoGameButtonListener());
        newGomokuGameButton = new JButton("new Gomoku game"); newGomokuGameButton.addActionListener(new NewGomokuGameButtonListener());

        bottomPanelButtons.add(newGoGameButton);
        bottomPanelButtons.add(newGomokuGameButton);
        bottomPanelButtons.add(saveGameButton);
        bottomPanelButtons.add(loadGameButton);

        bottomPanel = new BottomPanel(bottomPanelButtons);
    }
}
