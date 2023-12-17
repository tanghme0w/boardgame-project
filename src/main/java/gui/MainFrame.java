package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.ResourceBundle;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class MainFrame extends JFrame {
    private static BoardRenderer boardRenderer;
    private JPanel mainPanel;
    private BufferedImage cachedImage;
    private BufferedImage cachedBackground;
    public int boardPositionProportion = 4;
    public static final ResourceBundle resourceBundle =
            ResourceBundle.getBundle("l10n.DisplayStrings");
    public MainFrame() {
        super("Game");
        boardRenderer = new BoardRenderer();
        setSize(960, 600);
        setLocationRelativeTo(null);
        mainPanel = new JPanel(true) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                paintMainPanel(g);
            }
        };
        getContentPane().add(mainPanel);
        mainPanel.setFocusable(true);
        setVisible(true);
    }

    public void paintMainPanel(Graphics g0) {
        int width = mainPanel.getWidth();
        int height = mainPanel.getHeight();
        Optional<Graphics2D> backgroundG = Optional.empty();
        int topInset = mainPanel.getInsets().top;
        int leftInset = mainPanel.getInsets().left;
        int rightInset = mainPanel.getInsets().right;
        int bottomInset = mainPanel.getInsets().bottom;
        int maxBound = max(width, height);
        // board
        int maxSize = (int) (min(width - leftInset - rightInset, height - topInset - bottomInset));
        int boardX = (width - maxSize) / 8 * boardPositionProportion;
        int boardY = topInset + (height - topInset - bottomInset - maxSize) / 2;

        int panelMargin = (int) (maxSize * 0.02);
        cachedImage = new BufferedImage(width, height, TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) cachedImage.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        boardRenderer.setLocation(boardX, boardY);
        boardRenderer.setBoardLength(maxSize, maxSize); // TODO boardSize
        boardRenderer.setupSizeParameters();
        boardRenderer.draw(g);
        g.dispose();
        g0.drawImage(cachedBackground, 0, 0, null);
        g0.drawImage(cachedImage, 0, 0, null);
        g0.dispose();
    }
}
