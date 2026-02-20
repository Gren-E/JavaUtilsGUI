package com.gutil.gui.component.stats;

import com.gutil.gui.GraphicsUtil;
import com.gutil.gui.HorizontalAlignment;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

/**
 * A {@code ProgressIndicator} implementation that displays progress on a rectangular bar.
 * @author Ewelina Gren
 * @version 1.0
 */
public class ProgressBar extends ProgressIndicator {

    private int width;
    private int height;

    /**
     * Creates a {@code ProgressBar} with current value and maximum value set to 0.
     */
    public ProgressBar() {
        super(0);
    }

    /**
     * Creates a {@code ProgressBar} with default current value (0) and a specified maximum value.
     * @param maxValue maximum value on the bar
     */
    public ProgressBar(int maxValue) {
        super(0, maxValue);
    }

    /**
     * Creates a {@code ProgressBar} with specified current value and maximum value.
     * @param currentValue current value displayed on the bar
     * @param maxValue maximum value on the bar
     */
    public ProgressBar(int currentValue, int maxValue) {
        super(currentValue, maxValue);
    }

    /**
     * Sets target width of the bar, otherwise the bar expands to the component width.
     * @param width target width of the bar
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Sets target height of the bar, otherwise the bar expands to the component height.
     * @param height target height of the bar
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Paints the {@code Progress Bar} based on the specified parameters.
     * @param g the {@code Graphics} object to protect
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int componentWidth = Math.max(getWidth(), getMinimumSize().width);
        int componentHeight = Math.max(getHeight(), getMinimumSize().height);
        int barWidth = width != 0 ? Math.min(width, componentWidth) : componentWidth;
        int barHeight = height != 0 ? Math.min(height, componentHeight) : componentHeight;
        if (barWidth <= 0 || barHeight <= 10) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int progressWidth = (maxValue != 0) ? (int)(getProgressRatio() * barWidth) : 0;
        int x = (getWidth() - barWidth) / 2;
        int y = (getHeight() - barHeight) / 2;

        g2.setColor(new Color(progressColor.getRed(), progressColor.getGreen(), progressColor.getBlue(), 50));
        g2.fillRect(x, y, barWidth, barHeight);

        g2.setColor(progressColor);
        g2.fillRect(x, y, progressWidth, barHeight);

        String value = currentValue + "/" + maxValue;

        g2.setColor(getForeground());
        g2.setFont(getFont());
        int stringWidth = g2.getFontMetrics().stringWidth(value);
        int stringHeight = g2.getFontMetrics().getHeight();
        if (stringWidth < barWidth && stringHeight < barHeight) {
            GraphicsUtil.drawString(value, new Rectangle(x, y, barWidth, barHeight), getFont(), HorizontalAlignment.CENTER, 0, g2);
        }
    }

}
