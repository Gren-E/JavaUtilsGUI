package com.gutil.gui.component.stats;

import com.gutil.gui.adapters.CustomHighlight;

import javax.swing.JPanel;
import java.awt.Color;

/**
 * Abstract class defining basic framework for all {@code ProgressIndicator} subclasses, meant to display progress.
 * @author Ewelina Gren
 * @version 1.0
 */
public abstract class ProgressIndicator extends JPanel implements CustomHighlight {

    protected int maxValue;
    protected int currentValue;

    protected Color progressColor;

    /**
     * Creates a {@code ProgressIndicator} with current value and maximum value set to 0.
     */
    public ProgressIndicator() {
        this(0);
    }

    /**
     * Creates a {@code ProgressIndicator} with default current value (0) and a specified maximum value.
     * @param maxValue maximum value on the indicator
     */
    public ProgressIndicator(int maxValue) {
        this(0, maxValue);
    }

    /**
     * Creates a {@code ProgressIndicator} with specified current value and maximum value.
     * @param currentValue current value displayed on the indicator
     * @param maxValue maximum value on the indicator
     */
    public ProgressIndicator(int currentValue, int maxValue) {
        setOpaque(false);

        this.currentValue = currentValue;
        this.maxValue = maxValue;
        progressColor = Color.DARK_GRAY;
    }

    /**
     * Calculates what percentage of the maximum value is current value.
     * @return ratio of current value to maximum value as a percentage,
     * or 100% if current value is greater than maximum value
     */
    public float getProgressRatio() {
        return (float) Math.min(currentValue, maxValue) / maxValue;
    }

    /**
     * Sets the {@code Color} of the component's element displaying progress.
     * @param color the new progress color
     */
    public void setProgressColor(Color color) {
        progressColor = color;
    }

    /**
     * Sets the maximum value of the indicator.
     * @param value what the maximum value should be
     */
    public void setMaxValue(int value) {
        maxValue = value;
        repaint();
    }

    /**
     * Sets the current value of the indicator.
     * @param value what should be the current value
     */
    public void setCurrentValue(int value) {
        currentValue = value;
        repaint();
    }

    /**
     * Returns the current value displayed by the indicator.
     * @return the current value
     */
    public int getCurrentValue() {
        return currentValue;
    }

    /**
     * Points progress color as the one to be highlighted, if handled by the {@code HighlightingMouseAdapter}.
     * @param color what color should the element be set to
     */
    @Override
    public void toggleHighlight(Color color) {
        setProgressColor(color);
        repaint();
    }

    /**
     * Returns progress {@code Color} if needed by the {@code HighlightingMouseAdapter}.
     * @return progress color
     */
    @Override
    public Color getOriginalColor() {
        return progressColor;
    }

}
