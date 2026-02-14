package com.gutil.gui.adapters;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

/**
 * Mouse adapter class which provides various highlight effects, as well as performs an action.
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
    private Color originalBackgroundColor;

    /**
     * The component's element color while not highlighted.
     */
    private Color originalElementColor;

    /**
     * The component's background color while highlighted.
     */
    private Color backgroundHighlightColor;

    /**
     * The component's element color while highlighted.
     */
    private Color elementHighlightColor;

    /**
     * Should the borders be highlighted.
     */
    private boolean highlightBorders;

    /**
     * Should the background be highlighted.
     */
    private boolean highlightBackgrounds;

    /**
     * Should another part of the component be highlighted.
     */
    private boolean customHighlight;

    /**
     * The action to be performed when the mouse button is released.
     */
    private Consumer<MouseEvent> action;

    /**
     * Is the component in a highlighted or regular state.
     */
    private boolean isHighlighted;

    /**
     * Is the adapter enabled.
     */
    private boolean isEnabled;

    /**
     * Should the highlight be active for the component.
     */
    private boolean isHighlightEnabled;

    /**
     * Assigns the {@code Component} to be highlighted and which parts of the component (if any) should be highlighted.
     * Enables the adapter.
     * @param highlightBorders should the component's borders be highlighted
     * @param highlightBackground should the component's background be highlighted
     */
    public HighlightingMouseAdapter(boolean highlightBorders, boolean highlightBackground, boolean customHighlight) {
        this.highlightBorders = highlightBorders;
        this.highlightBackgrounds = highlightBackground;
        this.customHighlight = customHighlight;

        this.isEnabled = true;
        this.isHighlightEnabled = true;
    }


    /**
     * Turns on or off the highlight effect for the component's borders.
     * @param highlightBorders should the borders be highlighted
     */
    public void setHighlightBorders(boolean highlightBorders) {
        this.highlightBorders = highlightBorders;
    }

    /**
     * Turns on or off the highlight effect for the component's background.
     * @param highlightBackgrounds should the borders be highlighted
     */
    public void setHighlightBackgrounds(boolean highlightBackgrounds) {
        this.highlightBackgrounds = highlightBackgrounds;
    }


    /**
     * Turns on or off the custom highlight effect for the component.
     * @param customHighlight should the borders be highlighted
     */
    public void setCustomHighlight(boolean customHighlight) {
        this.customHighlight = customHighlight;
    }

    /**
     * Assigns a user picked color for highlighted background.
     * @param color what {@code Color} should the background be while highlighted
     */
    public void setBackgroundHighlightColor(Color color) {
        backgroundHighlightColor = color;
    }

    /**
     * Assigns a user picked color for custom highlighted element.
     * @param color what {@code Color} should the element be while highlighted
     */
    public void setCustomHighlightColor(Color color) {
        elementHighlightColor = color;
    }

    /**
     * Sets the {@linkplain #mouseReleased(MouseEvent)} action to be performed.
     * @param action the action to be performed
     */
    public void setMouseReleasedAction(Consumer<MouseEvent> action) {
        this.action = action;
    }

    /**
     * Enables or disables the adapter.
     * @param enabled should the adapter be enabled
     */
    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    /**
     * Enables or disables the highlight effect for the component, while the adapter is enabled.
     * Disabling the adapter disables the highlight automatically.
     * @param enabled should the highlight be enabled
     */
    public void setHighlightEnabled(boolean enabled) {
        isHighlightEnabled = enabled;
    }

    /**
     * Returns the highlight {@code Color} of the background.
     * @return the highlight color
     */
    public Color getBackgroundHighlightColor() {
        return backgroundHighlightColor;
    }

    /**
     * Returns the highlight {@code Color} of the highlighted element.
     * @return the highlight color
     */
    public Color getCustomHighlightColor() {
        return elementHighlightColor;
    }

    /**
     * Performs an action when the mouse button is released.
     * @param event the event to be processed
     */
    @Override
    public void mouseReleased(MouseEvent event) {
        if(action != null && isEnabled) {
            action.accept(event);
        }
    }

    /**
     * Sets the highlighted {@code Component} to its original state.
     * @param event the event to be processed
     */
    @Override
    public void mouseExited(MouseEvent event) {
        if (!isEnabled || !isHighlightEnabled) {
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
            originalBackgroundColor = originalBackgroundColor != null ? originalBackgroundColor : Color.WHITE;
            component.setBackground(originalBackgroundColor);
        }

        if (customHighlight && component instanceof CustomHighlight customComponent) {
            customComponent.toggleHighlight(originalElementColor);
        }

        isHighlighted = false;
    }

    /**
     * Highlights the {@code Component} according to specified parameters, unless the adapter or highlight effect
     * is disabled, or the component highlighted already. If the border is to be highlighted, the adapter changes it to white.
     * If the background or a custom element is to be highlighted, the right color is determined according to
     * {@link #updateBackgroundHighlightColor()} or {@link #updateCustomHighlightColor()}.
     * @param event the event to be processed
     */
    @Override
    public void mouseEntered(MouseEvent event) {
        if(!isEnabled || !isHighlightEnabled) {
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
            originalBackgroundColor = component.getBackground();
            component.setBackground(updateBackgroundHighlightColor());
        }

        if (customHighlight && component instanceof CustomHighlight customComponent) {
            originalElementColor = customComponent.getOriginalColor();
            customComponent.toggleHighlight(updateCustomHighlightColor());
        }

        isHighlighted = true;
    }

    /**
     * Returns the highlighted background color or calculates it by increasing each RGB value
     * of the original background by 30 (within the range - for very bright colors the effect might be diminished).
     * @return the {@code Color} of the highlighted background
     */
    private Color updateBackgroundHighlightColor() {
        if (backgroundHighlightColor == null) {
            backgroundHighlightColor = getAutomaticHighlight(originalBackgroundColor);
        }

        return backgroundHighlightColor;
    }

    /**
     * Returns the highlighted element color or calculates it by increasing each RGB value
     * of the original element by 30 (within the range - for very bright colors the effect might be diminished).
     * @return the {@code Color} of the highlighted element
     */
    private Color updateCustomHighlightColor() {
        if (elementHighlightColor == null) {
            elementHighlightColor = getAutomaticHighlight(originalElementColor);
        }

        return elementHighlightColor;
    }

    /**
     * Calculates an automatic highlight color value if the color isn't otherwise specified.
     * @param originalColor the standard non-highlighted color
     * @return the automatic highlight color
     */
    private Color getAutomaticHighlight(Color originalColor) {
        return new Color(
                Math.min(255, originalColor.getRed() + 30),
                Math.min(255, originalColor.getGreen() + 30),
                Math.min(255, originalColor.getBlue() + 30)
        );
    }

}
