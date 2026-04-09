package com.gutil.gui.component.stats;

import com.gutil.gui.ColorUtil;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

/**
 * A {@code ProgressIndicator} implementation that displays progress on a circular chart.
 * @author Ewelina Gren
 * @version 1.0
 */
public class ProgressChart extends ProgressIndicator {

    private int diameter;

    /**
     * Creates a {@code ProgressChart} with current value and maximum value set to 0.
     */
    public ProgressChart() {
        super(0);
    }

    /**
     * Creates a {@code ProgressChart} with default current value (0) and a specified maximum value.
     * @param maxValue maximum value on the chart
     */
    public ProgressChart(int maxValue) {
        super(0, maxValue);
    }

    /**
     * Creates a {@code ProgressChart} with specified current value and maximum value.
     * @param currentValue current value displayed on the chart
     * @param maxValue maximum value on the chart
     */
    public ProgressChart(int currentValue, int maxValue) {
        super(currentValue, maxValue);
    }

    /**
     * Returns the preferred size of the progress chart.
     * @return preferred {@code Dimension} of the chart
     */
    @Override
    public Dimension getPreferredSize() {
        Graphics g = getGraphics();
        FontMetrics metrics = g.getFontMetrics();
        int textDiameter = Math.max(metrics.getHeight(), metrics.stringWidth(getValueText()));
        int preferredDiameter = diameter != 0 ? diameter : (textDiameter + 20) * 3/2 ;
        return new Dimension(preferredDiameter, preferredDiameter);
    }

    /**
     * Returns the minimum size of the progress chart.
     * @return minimum {@code Dimension} of the chart
     */
    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    /**
     * Sets target diameter of the chart,
     * otherwise the chart adjusts accordingly to the component size.
     * @param diameter target diameter of the chart
     */
    public void setDiameter(int diameter) {
        this.diameter = diameter;
    }

    /**
     * Paints the {@code Progress Chart} based on the specified parameters.
     * @param g the {@code Graphics} object to protect
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = Math.max(getWidth(), getMinimumSize().width);
        int height = Math.max(getHeight(), getMinimumSize().height);

        diameter = diameter != 0 ? Math.min(Math.min(width, height), diameter) : Math.min(width, height);

        if (width <= 10 || height <= 10) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int progressArchAngle = maxValue != 0 ? (int)(getProgressRatio() * 360) : 0;

        int x = (width - diameter) / 2;
        int y = (height - diameter) / 2;

        g2.setColor(ColorUtil.semiTransparent(progressColor, 50));
        g2.fillOval(x, y, diameter, diameter);

        g2.setColor(progressColor);
        g2.fillArc(x, y, diameter, diameter, 90, - progressArchAngle);

        int innerDiameter = diameter * 2/3;
        int innerX = x + diameter / 6;
        int innerY = y + diameter / 6;

        g2.setColor(getBackground());
        g2.fillOval(innerX, innerY, innerDiameter, innerDiameter);

        drawText(new Rectangle(innerX, innerY, innerDiameter, innerDiameter), g2);
    }

}
