package com.gutil.gui.component.button;

import com.gutil.gui.GraphicsUtil;
import com.gutil.gui.HorizontalAlignment;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

/**
 * A {@code HighlightedButton} implementation with either a circular shape (according to the diameter) or a rectangular shape
 * with just two sides rounded (according to the component's width and height), and slight shading around the edges.
 * @author Ewelina Gren
 * @version 1.0
 */
public class RoundButton extends HighlightedButton {

    /**
     * Should the width and the height of the {@code RoundButton} be kept equal.
     */
    private boolean keepSymmetry;

    /**
     * Component's width.
     */
    private int width;
    /**
     * Component's height.
     */
    private int height;
    /**
     * The {@code RoundedButton}'s diameter if it's to be kept symmetrical.
     */
    private int diameter;

    /**
     * Creates a default {@code RoundButton} with no text displayed.
     */
    public RoundButton() {
        super();
    }

    /**
     * Creates a {@code RoundButton} with text displayed.
     * @param text text to be displayed
     */
    public RoundButton(String text) {
        super(text);
    }

    /**
     * Specifies should the width and the height of the {@code RoundButton} be kept equal,
     * resulting in a circular shape of the button.
     * @param keepSymmetry should the width and the height be kept equal
     */
    public void keepSymmetry(boolean keepSymmetry) {
        this.keepSymmetry = keepSymmetry;
    }

    /**
     * Paints the {@code RoundButton} that displays the specified button text.
     * @param g the {@code Graphics} object to protect
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        width = Math.max(getWidth(), getMinimumSize().width);
        height = Math.max(getHeight(), getMinimumSize().height);
        if (width <= 10 || height <= 10) {
            return;
        }

        diameter = Math.min(width, height);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color mainColor = getBackground();
        Color enabledColor = new Color(mainColor.getRed(), mainColor.getGreen(), mainColor.getBlue(), 150);
        Color disabledColor = new Color(mainColor.getRed(), mainColor.getGreen(), mainColor.getBlue(), 200);

        defineAndDrawShape(mainColor, enabledColor, disabledColor, g2);

        drawText(g2);
    }

    /**
     * Determines the required shape of the button based on whether the symmetry is to be kept, and draws it.
     * @param main the {@code RoundButton}'s main background color
     * @param enabled the shading color while enabled
     * @param disabled the shading color while disabled
     * @param g2 the {@code Graphics2D} object to protect
     */
    private void defineAndDrawShape(Color main, Color enabled, Color disabled, Graphics2D g2) {
        g2.setColor(isEnabled() ? enabled : disabled);
        if (keepSymmetry) {
            g2.fillOval((width - diameter) / 2, (height - diameter) / 2, diameter, diameter);
        } else {
            g2.fillRoundRect(0, 0, width, height, diameter, diameter);
        }

        g2.setColor(main);
        if (keepSymmetry) {
            g2.fillOval((width - diameter) / 2 + 5, (height - diameter) / 2, diameter - 10, diameter - 8);
        } else {
            g2.fillRoundRect(5, 0, width - 10, height - 8, diameter - 10, diameter - 8);
        }
    }

    /**
     * Draws the {@code RoundButton}'s message. If the text width is greater than the button's width,
     * the text gets cropped accordingly.
     * @param g2 the {@code Graphics2D} object to protect
     */
    @Override
    public void drawText(Graphics2D g2) {
        g2.setColor(getForeground());
        String adjustedText = GraphicsUtil.cropString(text, getFont(), (keepSymmetry ? diameter : width) - 10, g2);
        GraphicsUtil.drawString(adjustedText, new Rectangle(5, 0, width - 10, height), getFont(), HorizontalAlignment.CENTER, 0, g2);
    }

}
