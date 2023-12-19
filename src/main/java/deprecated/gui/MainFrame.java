package deprecated.gui;

import deprecated.entity.Identity;
import deprecated.entity.Player;
import refactor.client.handler.MouseClickHandler;
import refactor.client.handler.NewGoGameButtonListener;
import refactor.client.handler.NewGomokuGameButtonListener;
import deprecated.logger.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainFrame extends JFrame {
    private int boardSize = 20;
    private final int cellSize = 30; // size of each cell in the grid
    private JTextArea player1TextArea = new JTextArea("Player1:\n\nN/A");
    private JTextArea player2TextArea = new JTextArea("Player2:\n\nN/A");
    private JTextArea logTextArea = new JTextArea("Log:\n\nN/A");
    private String inputPrompt = "Input Box: Please delete this message before input.";
    private JTextArea inputBoxArea = new JTextArea(inputPrompt);
    private JPanel sidePanel = createSidePanel();
    private JPanel bottomPanel = createBottomPanel();
    private JPanel boardPanel = new BoardPanel();


    public MainFrame() {
        setTitle("Game Board");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize((20 * 30) + 1000, ((20 + 2) * cellSize));
        setLocationRelativeTo(null); // center the window
        add(boardPanel, BorderLayout.CENTER);
        add(sidePanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    public String getInput() {
        String input = inputBoxArea.getText().strip();
        if (input.equals(inputPrompt)) return null;
        inputBoxArea.setText(inputPrompt);
        return input;
    }
    public void refresh() {
        //refresh players info
        if (Client.chessGame != null) {
            //update p1 info
            String p1Info = "Player1\n";
            Player player1 = Client.chessGame.getPlayerInfo(0);
            if (player1 == null) {
                p1Info = p1Info + "N/A";
            } else {
                if (player1.equals(Client.chessGame.getCurrentActingPlayer())) {
                    p1Info = "Player1 (Currently Acting)\n";
                }
                String p1Id = player1.id == Identity.BLACK ? "Identity: Black" : "Identity: White";
                p1Info = p1Info + "Name: " + player1.name + "\n" + p1Id + "\n";
            }
            player1TextArea.setText(p1Info);
            //update p2 info
            String p2Info = "Player2\n";
            Player player2 = Client.chessGame.getPlayerInfo(1);
            if (player2 == null) {
                p2Info = p2Info + "N/A";
            } else {
                if (player2.equals(Client.chessGame.getCurrentActingPlayer())) {
                    p2Info = "Player2 (Currently Acting)\n";
                }
                String p2Id = player2.id == Identity.BLACK ? "Identity: Black" : "Identity: White";
                p2Info = p2Info + "Name: " + player2.name + "\n" + p2Id + "\n";
            }
            player2TextArea.setText(p2Info);
        }
        //refresh log
        String logText = "Log:\n";
        for (String logEntry: Logger.getLog(7)) {
            logText = logText.concat(logEntry);
            logText = logText.concat("\n");
        }
        logTextArea.setText(logText);
        //repaint board
        if (Client.chessGame != null) {
            if (Client.chessGame.gameboard != null) {
                boardSize = Client.chessGame.gameboard.size + 1;
                boardPanel.repaint();
            }
        }
    }
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        bottomPanel.setPreferredSize(new Dimension(getWidth() - 200, 30));
        bottomPanel.add(new JButton("Abstain"));
        bottomPanel.add(new JButton("Surrender"));
        bottomPanel.add(new JButton("Withdraw"));
        return bottomPanel;
    }

    private JPanel createSidePanel() {
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setPreferredSize(new Dimension(1000, getHeight()));

        // Refactor repeated code into a method
        sidePanel.add(createTextAreaWithScrollPane(player1TextArea, 160, 100, false));
        sidePanel.add(createTextAreaWithScrollPane(player2TextArea, 160, 100, false));
        sidePanel.add(createTextAreaWithScrollPane(logTextArea, 160, 150, false));

        // Add save/load box & buttons
        sidePanel.add(createTextAreaWithScrollPane(inputBoxArea, 160, 20, true));
        sidePanel.add(new JButton("             load             "));
        sidePanel.add(new JButton("             save             "));
        sidePanel.add(createButton("     new game - Go    ", new NewGoGameButtonListener()));
        sidePanel.add(createButton("new game - Gomoku", new NewGomokuGameButtonListener()));

        return sidePanel;
    }

    private JButton createButton(String label, ActionListener actionListener) {
        JButton button = new JButton(label);
        button.addActionListener(actionListener);
        return button;
    }

    // New method to create a text area with scroll pane
    private JScrollPane createTextAreaWithScrollPane(JTextArea textArea, int width, int height, boolean editable) {
        textArea.setEditable(editable);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(width, height));
        return scrollPane;
    }

    private class BoardPanel extends JPanel {
        public BoardPanel() {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    int x = (int) Math.round((double) e.getX() / cellSize);
                    int y = (int) Math.round((double) e.getY() / cellSize);
                    Logger.log("Clicked on " + x + ", " + y);
                    MouseClickHandler.handle(x, y);
                }
            });
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawBoard(g);
        }

        private void drawBoard(Graphics g) {
            for (int i = 1; i < boardSize - 1; i++) {
                for (int j = 1; j < boardSize - 1; j++) {
                    int x = i * cellSize;
                    int y = j * cellSize;
                    g.drawRect(x, y, cellSize, cellSize);
                }
            }
        }
    }
}
