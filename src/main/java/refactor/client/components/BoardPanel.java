package refactor.client.components;

import globals.BoardMode;
import globals.ChessType;
import globals.Config;
import refactor.Board;
import refactor.Position;
import refactor.Server;
import refactor.client.handler.MouseClickHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class BoardPanel extends JPanel {
    Board board;
    MouseListener stepMouseListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            int x = (int) Math.round((double) e.getX() / Config.CELL_SIZE);
            int y = (int) Math.round((double) e.getY() / Config.CELL_SIZE);
            MouseClickHandler.handle(x, y);
        }
    };

    MouseListener removePieceMouseListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            int x = (int) Math.round((double) e.getX() / Config.CELL_SIZE);
            int y = (int) Math.round((double) e.getY() / Config.CELL_SIZE);
            Server.removeDeadPiecesAt(new Position(x, y));
        }
    };

    public BoardPanel(BoardMode mode) {
        setBackground(Color.ORANGE);
        addMouseListener(switch (mode) {
            case NORMAL -> stepMouseListener;
            case REMOVE -> removePieceMouseListener;
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
    }

    private void drawBoard(Graphics g) {
        for (int i = 1; i <= board.xSize; i++) {
            for (int j = 1; j <= board.ySize; j++) {
                int x = i * Config.CELL_SIZE;
                int y = j * Config.CELL_SIZE;
                g.setColor(Color.BLACK);
                if (i != board.xSize && j != board.ySize) g.drawRect(x, y, Config.CELL_SIZE, Config.CELL_SIZE);
                ChessType chessType = board.getChessTypeAt(new Position(i, j));
                if (chessType == null) continue;
                else if (chessType == ChessType.BLACK) {
                    g.setColor(Color.BLACK);
                } else if (chessType == ChessType.WHITE) {
                    g.setColor(Color.WHITE);
                }
                g.fillOval(x - Config.CELL_SIZE / 2, y - Config.CELL_SIZE / 2, Config.CELL_SIZE, Config.CELL_SIZE);
                //show step count
                if (Config.SHOW_STEP_COUNT) {
                    if (chessType == ChessType.BLACK) {
                        g.setColor(Color.WHITE);
                    } else if (chessType == ChessType.WHITE) {
                        g.setColor(Color.BLACK);
                    }
                    String stepIdString = Integer.toString(board.getStepIdAt(new Position(i, j)));
                    g.drawChars(stepIdString.toCharArray(), 0, stepIdString.length(), x - 4 * stepIdString.length(), y + 5);
                }
            }
        }
    }
}
