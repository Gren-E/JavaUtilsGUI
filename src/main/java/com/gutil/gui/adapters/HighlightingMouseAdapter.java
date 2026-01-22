package com.gutil.gui.adapters;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

/**
 * Mouse adapter class highlighting the component or the component borders, as well as performing an action.
 * @author Ewelina Gren
 * @version 1.0
 */
public class HighlightingMouseAdapter extends MouseAdapter {

    /**
     * The {@code JComponent} to be highlighted.
     */
    private JComponent component;

    /**
     * The component's {@code Border} while not highlighted.
     */
    private Border originalBorder;

    /**
     * The component's background color while not highlighted.
     */
    private Color originalColor;

    /**
     * The component's highlighted color if specified by the user.
     */
    private Color highlightColor;

    /**
     * Is the adapter enabled.
     */
    private boolean isEnabled;

    /**
     * Should the borders be highlighted.
     */
    private final boolean highlightBorders;

    /**
     * Should the background be highlighted.
     */
    private final boolean highlightBackgrounds;

    /**
     * Is the component in a highlighted or regular state.
     */
    private boolean isHighlighted;

    /**
     * The action to be performed when the mouse button is released.
     */
    private Consumer<MouseEvent> action;

    /**
     * Assigns the {@code Component} to be highlighted and which parts of the component (if any) should be highlighted.
     * Enables the adapter.
     * @param highlightBorders should the component's borders be highlighted
     * @param highlightBackground should the component's background be highlighted
     */
    public HighlightingMouseAdapter(boolean highlightBorders, boolean highlightBackground) {
        this.highlightBorders = highlightBorders;
        this.highlightBackgrounds = highlightBackground;
        this.isEnabled = true;
    }

    /**
     * Sets the {@linkplain #mouseReleased(MouseEvent)} action to be performed.
     * @param action the action to be performed
     */
    public void setMouseReleasedAction(Consumer<MouseEvent> action) {
        this.action = action;
    }

    /**
     * Assigns a user picked color for highlighted background.
     * @param color what {@code Color} should the background be while highlighted
     */
    public void setHighlightColor(Color color) {
        highlightColor = color;
    }

    /**
     * Performs an action when the mouse button is released, unless the action is {@code null}.
     * @param event the event to be processed
     */
    @Override
    public void mouseReleased(MouseEvent event) {
        if(action != null && isEnabled) {
            action.accept(event);
        }
    }

    /**
     * Highlights the {@code Component} according to specified parameters, unless the adapter is disabled
     * or the component highlighted already. If the border is to be highlighted, the adapter changes it to white.
     * If the background is to be highlighted, the right background color is determined
     * according to {@link #getHighlightBackgroundColor()}.
     * @param event the event to be processed
     */
    @Override
    public void mouseEntered(MouseEvent event) {
        if(!isEnabled) {
            return;
        }

        if (isHighlighted) {
            return;
        }

        component = (JComponent) event.getSource();

        if (highlightBorders) {
            originalBorder = component.getBorder();
            component.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.WHITE));
        }

        if (highlightBackgrounds) {
            originalColor = component.getBackground();
            component.setBackground(getHighlightBackgroundColor());
        }

        isHighlighted = true;
    }

    /**
     * Returns the user picked color for the highlighted background or calculates it by increasing each RGB value
     * of the original background by 30 (within the range - for very bright colors the effect might be diminished).
     * @return the {@code Color} of the highlighted background
     */
    private Color getHighlightBackgroundColor() {
        if (highlightColor != null) {
            return highlightColor;
        }

        return new Color(
                Math.min(255, originalColor.getRed() + 30),
                Math.min(255, originalColor.getGreen() + 30),
                Math.min(255, originalColor.getBlue() + 30)
        );
    }

    /**
     * Returns the highlighted {@code Component} to its original state.
     * @param event the event to be processed
     */
    @Override
    public void mouseExited(MouseEvent event) {
        if (!isEnabled) {
            return;
        }

        if (!isHighlighted) {
            return;
        }

        component = (JComponent) event.getSource();

        if (highlightBorders) {
            component.setBorder(originalBorder);
        }

        if (highlightBackgrounds) {
            originalColor = originalColor != null ? originalColor : Color.WHITE;
            component.setBackground(originalColor);
        }

        isHighlighted = false;
    }

    /**
     * Enables or disables the adapter.
     * @param enabled should the adapter be enabled
     */
    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }

}
