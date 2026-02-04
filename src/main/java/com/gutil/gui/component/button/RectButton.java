package com.gutil.gui.component.button;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * A {@code HighlightedButton} implementation with a rectangular shape and slight shading around the edges.
 * @author Ewelina Gren
 * @version 1.0
 */
public class RectButton extends HighlightedButton {

    /**
     * Creates a default {@code RectButton} with no text displayed.
     */
    public RectButton() {
        super();
    }

    /**
     * Creates a {@code RectButton} with text displayed.
     * @param text text to be displayed
     */
    public RectButton(String text) {
        super(text);
    }

    /**
     * Paints the {@code RectButton} that displays the specified button text.
     * @param g the {@code Graphics} object to protect
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = Math.max(getWidth(), getMinimumSize().width);
        int height = Math.max(getHeight(), getMinimumSize().height);
        if (width <= 10 || height <= 10) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color mainColor = getBackground();
        Color enabledColor = new Color(mainColor.getRed(), mainColor.getGreen(), mainColor.getBlue(), 210);
        Color disabledColor = new Color(mainColor.getRed(), mainColor.getGreen(), mainColor.getBlue(), 240);

        g2.setColor(isEnabled() ? enabledColor : disabledColor);
        g2.fillRect(0, 0, width, height);

        g2.setColor(mainColor);
        g2.fillRoundRect(5, 3, width - 10, height - 8, 25, 25);

        drawText(g2);
    }

}
