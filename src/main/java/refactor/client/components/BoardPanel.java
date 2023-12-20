package refactor.client.components;

import globals.ChessType;
import globals.Config;
import refactor.Board;
import refactor.Position;
import refactor.client.handler.MouseClickHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoardPanel extends JPanel {
    Board board;
    public BoardPanel() {
        setBackground(Color.ORANGE);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int x = (int) Math.round((double) e.getX() / Config.CELL_SIZE);
                int y = (int) Math.round((double) e.getY() / Config.CELL_SIZE);
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
        for (int i = 1; i < board.xSize; i++) {
            for (int j = 1; j < board.ySize; j++) {
                int x = i * Config.CELL_SIZE;
                int y = j * Config.CELL_SIZE;
                g.drawRect(x, y, Config.CELL_SIZE, Config.CELL_SIZE);
                ChessType chessType = board.getChessTypeAt(new Position(i, j));
                if (chessType == null) continue;
                else if (chessType == ChessType.BLACK) {
                    g.setColor(Color.BLACK);
                } else if (chessType == ChessType.WHITE) {
                    g.setColor(Color.WHITE);
                }
                g.fillOval(x - Config.CELL_SIZE / 2, y - Config.CELL_SIZE / 2, Config.CELL_SIZE, Config.CELL_SIZE);
                g.setColor(Color.BLACK);
            }
        }
    }
}
