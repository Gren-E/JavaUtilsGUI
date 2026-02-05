package com.gutil.gui.component.button;

import com.gutil.gui.ImageUtil;
import com.gutil.gui.ResizeQuality;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

/**
 * A {@code HighlightedButton} implementation with an icon. Any part of the icon in blue (r:0, g:0, b:255)
 * is interpreted as the button's background and changed or highlighted accordingly.
 * Any potential text is displayed on top of the icon {@code Image}.
 * @author Ewelina Gren
 * @version 1.0
 */
public class IconButton extends HighlightedButton {

    private Image icon;
    private Image defaultIcon;
    private Image highlightIcon;

    private final int width;
    private final int height;
    private int padx;
    private int pady;

    private Color defaultColor;
    private Color highlightColor;
    private boolean highlight;

    /**
     * Creates an instance of the button with a specific icon and target dimensions. Assigns default colors.
     * @param icon an {@code} Image to be displayed as the button
     * @param width target width of the button
     * @param height target height of the button
     */
    public IconButton(Image icon, int width, int height) {
        this.icon = icon;
        this.width = width;
        this.height = height;

        defaultColor = new Color(0, 0, 255);
        highlightColor = new Color(255, 255, 255);

        setOpaque(false);

        resizeIcon();
        colorDefaultIcon();
    }

    /**
     * Adjusts the size of the original icon to fit the target width and height.
     */
    private void resizeIcon() {
        if (icon == null) {
            return;
        }

        icon = ImageUtil.resize(icon, width, height, ResizeQuality.HIGH);
    }

    /**
     * Creates the button's default non-highlighted icon by replacing all blue (r:0, g:0, b:255) with a chosen color.
     */
    private void colorDefaultIcon() {
        if (icon == null) {
            return;
        }

        defaultIcon = ImageUtil.replaceColor(icon, new Color(0, 0, 255), defaultColor, 10);
    }

    /**
     * Creates the highlighted button icon by replacing all blue (r:0, g:0, b:255) with the highlight color.
     */
    private void colorHighlightIcon() {
        if (icon == null) {
            return;
        }

        highlightIcon = ImageUtil.replaceColor(icon, new Color(0, 0, 255), highlightColor, 10);
    }

    /**
     * Sets the button's icon and adjusts the size and color.
     * @param icon an {@code Image} to be set as the button's icon
     */
    public void setIcon(Image icon) {
        this.icon = icon;
        resizeIcon();
        colorDefaultIcon();
    }

    /**
     * Returns the default non-highlighted version of the button's icon.
     * @return button's default icon
     */
    public Image getIcon() {
        return defaultIcon;
    }

    /**
     * Sets the button's icon to the version that matches the parameter, 
     * effectively changing the parts considered the button's background to the desired {@code Color}.
     * If neither the default nor the highlighted icon match the parameter, no action is performed.
     * @param color the desired background {@code Color}
     */
    @Override
    public void setBackground(Color color) {
        if (color == null || defaultColor == null || highlightColor == null) {
            return;
        }

        if (color.equals(defaultColor)) {
            highlight(false);
        } else if (color.equals(highlightColor)) {
            highlight(true);
        } else {
            updateHighlightColor();
            if (color.equals(highlightColor)) {
                colorHighlightIcon();
                highlight(true);
            }
        }

    }

    /**
     * Sets the {@link #highlight} flag and repaints the button accordingly.
     * @param highlight is the button highlighted
     */
    private void highlight(boolean highlight) {
        this.highlight = highlight;
        repaint();
    }

    /**
     * Sets the button's highlight color to the one specified in the {@code HighlightMouseAdapter}.
     */
    private void updateHighlightColor() {
        Color color =  getHighlightingMouseAdapter().getHighlightColor();
        highlightColor = color != null ? color : Color.WHITE;
    }

    /**
     * Returns the current {@code Color} of the button's part considered as background (whether highlighted or not).
     * @return the button's default or highlighted background (depending on the current state)
     */
    @Override
    public Color getBackground() {
        return !highlight ? defaultColor : highlightColor;
    }

    /**
     * Sets the highlight color in the {@code HighlightingMouseAdapter}.
     */
    public void setHighlightColor(Color color) {
        getHighlightingMouseAdapter().setHighlightColor(color);
        updateHighlightColor();
    }

    /**
     * Returns the highlight {@code Color} as specified in the {@code HighlightingMouseAdapter}.
     * @return the highlight color
     */
    public Color getHighlightColor() {
        return getHighlightingMouseAdapter().getHighlightColor();
    }

    /**
     * Sets the default {@code Color} of the button's part considered as background.
     * @param color a color the background should be set to
     */
    public void setDefaultColor(Color color) {
        defaultColor = color;

        if (icon == null) {
            return;
        }

        colorDefaultIcon();
        repaint();
    }

    /**
     * Returns the default {@code Color} of the part of the button considered as background.
     * @return the default color
     */
    public Color getDefaultColor() {
        return defaultColor;
    }

    /**
     * Paints the button according to its current highlight status.
     * If there's any text specified, it gets displayed over the icon.
     * @param g the {@code Graphics} object to protect
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (icon == null) {
            return;
        }

        int x = Math.max((getWidth() - width) / 2, 0);
        int y = Math.max((getHeight() - height) / 2, 0);

        Graphics2D g2 = (Graphics2D) g;
        Image imageToDraw = highlight ? highlightIcon : defaultIcon;
        g2.drawImage(imageToDraw, x, y, null);

        drawText(g2);
    }

    /**
     * Returns the exact button size to fit the icon.
     * @return {@code Dimension} of the button
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(this.width + padx * 2, this.height + pady * 2);
    }

    /**
     * Returns the minimum button size to fit the icon.
     * @return {@code Dimension} of the button
     */
    @Override
    public Dimension getMinimumSize() {
        return this.getPreferredSize();
    }

    /**
     * Sets padding between the icon and the button's edges.
     * @param padx horizontal padding
     * @param pady vertical padding
     */
    public void setPad(int padx, int pady) {
        this.padx = padx;
        this.pady = pady;
        repaint();
    }

}