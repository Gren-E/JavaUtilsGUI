package com.gutil.gui.component.button;

import com.gutil.gui.ImageUtil;
import com.gutil.gui.ResizeQuality;
import com.gutil.gui.adapters.CustomHighlight;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

/**
 * A {@code HighlightedButton} implementation with an icon. Supports color adjustments for any part of the icon
 * originally colored in blue, so it can be changed to a specified color or highlighted.
 * Implements {@code CustomHighlight} interface which allows the {@code HighlightingMouseAdapter} to perform specified
 * highlight effect rather than highlighting the background like in the original {@code HighlightedButton} implementation.
 * Any potential text is displayed on top of the icon {@code Image}.
 * @author Ewelina Gren
 * @version 1.0
 */
public class IconButton extends HighlightedButton implements CustomHighlight {

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
        if (icon != null) {
            icon = ImageUtil.resize(icon, width, height, ResizeQuality.HIGH);
        }
    }

    /**
     * Creates the button's default non-highlighted icon by replacing all blue (r:0, g:0, b:255) with the default color.
     */
    private void colorDefaultIcon() {
        if (icon != null) {
            defaultIcon = ImageUtil.replaceColor(icon, new Color(0, 0, 255), defaultColor, 10);
        }
    }

    /**
     * Creates the highlighted button icon by replacing all blue (r:0, g:0, b:255) with the highlight color.
     */
    private void colorHighlightIcon() {
        if (icon != null) {
            highlightIcon = ImageUtil.replaceColor(icon, new Color(0, 0, 255), highlightColor, 10);
        }
    }

    /**
     * Sets the button's icon and adjusts the size and color.
     * @param icon an {@code Image} to be set as the button's icon
     */
    public void setIcon(Image icon) {
        this.icon = icon;
        resizeIcon();
        colorDefaultIcon();
        colorHighlightIcon();
    }

    /**
     * Sets the default {@code Color} of the icon's parts that support color adjustments.
     * @param color a target color of the icon's adjustable part
     */
    public void setDefaultColor(Color color) {
        if (color == null || defaultColor.equals(color)) {
            return;
        }

        defaultColor = color;
        if (icon != null) {
            colorDefaultIcon();
            repaint();
        }
    }

    /**
     * Sets the highlight color in the {@code HighlightingMouseAdapter}.
     */
    public void setHighlightColor(Color color) {
        getHighlightingMouseAdapter().setBackgroundHighlightColor(color);
        updateHighlightColor();
    }

    /**
     * Sets the button's highlight color to the one specified in the {@code HighlightMouseAdapter}.
     */
    private void updateHighlightColor() {
        Color color =  getHighlightingMouseAdapter().getCustomHighlightColor();
        if (color == null || highlightColor.equals(color)) {
            return;
        }

        highlightColor = color;
        if (icon != null) {
            colorHighlightIcon();
            repaint();
        }
    }

    /**
     * Returns the resized version of the original icon.
     * @return button's original icon
     */
    public Image getIcon() {
        return icon;
    }

    /**
     * Returns the default {@code Color} of the icon's adjustable part.
     * @return the default color
     */
    public Color getDefaultColor() {
        return defaultColor;
    }

    /**
     * Returns the highlight {@code Color} as specified in the {@code HighlightingMouseAdapter}.
     * @return the highlight color
     */
    public Color getHighlightColor() {
        return getHighlightingMouseAdapter().getBackgroundHighlightColor();
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

    /**
     * Overrides the {@code HighlightedButton}'s method in order to change the highlight from background to custom.
     */
    @Override
    public void initMouseListener() {
        super.initMouseListener();
        getHighlightingMouseAdapter().setHighlightBackgrounds(false);
        getHighlightingMouseAdapter().setCustomHighlight(true);
    }

    /**
     * Calls the {@link #highlight(boolean)} method based on the target element color, in order to repaint the component as default or highlighted.
     * @param color what color should the element be set to
     */
    @Override
    public void toggleHighlight(Color color) {
        if (color == null || defaultColor == null || highlightColor == null) {
            return;
        }
        updateHighlightColor();

        if (color.equals(defaultColor)) {
            highlight(false);
        } else if (color.equals(highlightColor)) {
            highlight(true);
        }
    }

    /**
     * Returns the default color of the icon's adjustable part.
     * @return the default color
     */
    @Override
    public Color getOriginalColor() {
        return getDefaultColor();
    }

}