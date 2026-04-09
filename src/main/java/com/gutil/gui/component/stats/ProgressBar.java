package com.gutil.gui.component.stats;

import com.gutil.gui.ColorUtil;

import java.awt.Dimension;
import java.awt.FontMetrics;
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

    private int barWidth;
    private int barHeight;

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
     * Returns the preferred size of the progress bar.
     * @return preferred {@code Dimension} of the bar
     */
    @Override
    public Dimension getPreferredSize() {
        Graphics g = getGraphics();
        FontMetrics metrics = g.getFontMetrics();
        int preferredHeight = barHeight != 0 ? barHeight : metrics.getHeight() + 10;
        int preferredWidth = barWidth != 0 ? barWidth : metrics.stringWidth(getValueText()) + 10;
        return new Dimension(preferredWidth, preferredHeight);
    }

    /**
     * Returns the minimum size of the progress bar.
     * @return minimum {@code Dimension} of the bar
     */
    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    /**
     * Sets target width of the bar, otherwise the bar expands to the component width.
     * @param barWidth target width of the bar
     */
    public void setBarWidth(int barWidth) {
        this.barWidth = barWidth;
    }

    /**
     * Sets target height of the bar, otherwise the bar expands to the component height.
     * @param barHeight target height of the bar
     */
    public void setBarHeight(int barHeight) {
        this.barHeight = barHeight;
    }

    /**
     * Paints the {@code Progress Bar} based on the specified parameters.
     * @param g the {@code Graphics} object to protect
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = Math.max(getWidth(), getMinimumSize().width);
        int height = Math.max(getHeight(), getMinimumSize().height);

        barWidth = barWidth != 0 ? Math.min(barWidth, width) : width;
        barHeight = barHeight != 0 ? Math.min(barHeight, height) : height;

        if (barWidth <= 0 || barHeight <= 10) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int progressWidth = maxValue != 0 ? (int)(getProgressRatio() * barWidth) : 0;

        int x = (width - barWidth) / 2;
        int y = (height - barHeight) / 2;

        g2.setColor(ColorUtil.semiTransparent(progressColor, 50));
        g2.fillRect(x, y, barWidth, barHeight);

        g2.setColor(progressColor);
        g2.fillRect(x, y, progressWidth, barHeight);

        drawText(new Rectangle(x, y, barWidth, barHeight), g2);
    }

}
