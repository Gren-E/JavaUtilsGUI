package com.gutil.gui.component.button;

import com.gutil.gui.temp.GraphicsUtil;
import com.gutil.gui.temp.HighlightingMouseAdapter;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * Abstract class defining basic framework for {@code Button} subclasses
 * that get highlighted while interacting with a cursor (if enabled).
 * @author Ewelina Gren
 * @version 1.0
 */
public abstract class HighlightedButton extends Button {

    /**
     * A {@code MouseAdapter} responsible for highlighting the {@code Button} as well as performing the specified action.
     */
    private HighlightingMouseAdapter mouseAdapter;

    /**
     * A baseline foreground color of the {@code Button} while enabled.
     */
    private Color foregroundColor;
    /**
     * A foreground color of the {@code Button} while disabled.
     */
    private Color foregroundDisabledColor;

    /**
     * Creates a default {@code HighlightedButton} with no text displayed.
     */
    public HighlightedButton() {
        this("");
    }

    /**
     * Creates a {@code HighlightedButton} with text displayed.
     * @param text text to be displayed
     */
    public HighlightedButton(String text) {
        super(text);
    }

    /**
     * Assigns dedicated colors for the button background, foreground, and foreground while disabled.
     * @param background a {@code Color} of the background
     * @param foreground a {@code Color} of the foreground
     * @param foregroundDisabled a {@code Color} of the foreground while disabled
     */
    public void updateButtonColors(Color background, Color foreground, Color foregroundDisabled) {
        setBackground(background);
        foregroundColor = foreground;
        foregroundDisabledColor = foregroundDisabled;
        updateForeground();
    }

    /**
     * Sets the foreground color depending on whether the {@code HighlightedButton} is enabled or not.
     */
    private void updateForeground() {
        setForeground(isEnabled() ? foregroundColor : foregroundDisabledColor);
    }

    /**
     * Draws the {@code HighlightedButton}'s message. If the text width is greater than the button's width,
     * the text gets cropped accordingly.
     * @param g2 the graphic environment
     */
    public void drawText(Graphics2D g2) {
        g2.setColor(getForeground());
        String adjustedText = GraphicsUtil.shortenString(g2, text, getFont(), getWidth() - 10);
        GraphicsUtil.drawString(adjustedText, new Rectangle(5, 0, getWidth() - 10, getHeight()), getFont(), GraphicsUtil.CENTER, 0, g2);
    }

    /**
     * Adds a {@code HighlightingMouseAdapter} to the {@code HighlightedButton}.
     */
    @Override
    public void setMouseListener() {
        mouseAdapter = new HighlightingMouseAdapter(this, false, true);
        mouseAdapter.setMouseReleasedAction(event -> handleMouseReleased());
        addMouseListener(mouseAdapter);
    }

    /**
     * Enables the {@code HighlightedButton}.
     * @param enabled true if this component should be enabled, false otherwise
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mouseAdapter.setEnabled(enabled);
        setCursor(enabled ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) : Cursor.getDefaultCursor());
        updateForeground();
    }

}
