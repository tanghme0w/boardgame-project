package gui;

import java.awt.*;
import java.awt.image.BufferedImage;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

public class BoardRenderer {
    private BufferedImage cachedBackgroundImage;
    public void draw(Graphics2D g) {
        drawGoban(g);
        drawStones();
        renderImages(g);

    }
    private void drawGoban(Graphics2D g0) {
        cachedBackgroundImage = new BufferedImage(width, height, TYPE_INT_ARGB);
        Graphics2D g = cachedBackgroundImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        drawWoodenBoard(g);
        g.setColor(Color.BLACK);
        for (int i = 0; i < 19; i++) {

        }
    }
    private void drawWoodenBoard(Graphics2D g) {

    }
    private void drawStones() {

    }
    private void renderImages(Graphics2D g) {

    }
}
