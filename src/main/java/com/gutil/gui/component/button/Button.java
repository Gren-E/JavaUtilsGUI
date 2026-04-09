package com.gutil.gui.component.button;

import javax.swing.JPanel;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Abstract class defining basic framework for all {@code Button} subclasses.
 * @author Ewelina Gren
 * @version 1.0
 */
public abstract class Button extends JPanel {

    /**
     * Text to be displayed on the {@code Button}.
     */
    protected String text;

    /**
     * An action to be performed after clicking the {@code Button}.
     */
    private ActionListener mouseClickAction;

    /**
     * Creates a default {@code Button} with no text displayed.
     */
    public Button() {
        this("");
    }

    /**
     * Creates a {@code Button} with text displayed.
     * @param text text to be displayed
     */
    public Button(String text) {
        setOpaque(false);

        this.text = text;

        initMouseListener();
        setEnabled(true);
    }

    /**
     * Sets the text displayed on the {@code Button}.
     * @param text test to be displayed
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Sets the action to be performed after clicking the {@code Button}.
     * @param action an action to be performed
     */
    public void setActionListener(ActionListener action) {
        this.mouseClickAction = action;
    }

    /**
     * Defines the expected {@code Button} behaviour after releasing the mouse button.
     */
    public void handleMouseReleased() {
        if (mouseClickAction != null && isEnabled()) {
            mouseClickAction.actionPerformed(null);
        }
    }

    /**
     * Adds a {@code MouseListener} to the {@code Button}.
     */
    protected void initMouseListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                handleMouseReleased();
            }
        });
    }

    /**
     * Returns the exact button size to fit the whole text.
     * @return preferred {@code Dimension} of the button
     */
    @Override
    public Dimension getPreferredSize() {
        Graphics g = getGraphics();
        FontMetrics metrics = g.getFontMetrics();
        int height = metrics.getHeight() * 2;
        int width = metrics.stringWidth(text) + height;
        return new Dimension(width, height);
    }

    /**
     * Returns the minimum button size. Text might be cropped.
     * @return minimum {@code Dimension} of the button
     */
    @Override
    public Dimension getMinimumSize() {
        Graphics g = getGraphics();
        FontMetrics metrics = g.getFontMetrics();
        int size = metrics.getHeight() * 2;
        return new Dimension(size, size);
    }

    /**
     * Overrides the {@code JComponent}'s method in order to change the cursor's appearance according to the parameter.
     * @param enabled true if this component should be enabled, false otherwise
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setCursor(enabled ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) : Cursor.getDefaultCursor());
    }

}
