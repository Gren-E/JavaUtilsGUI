package com.gutil.gui.component.panel;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;

/**
 * A {@code JPanel} implementation with rounded edges and optional shading around them.
 * @author Ewelina Gren
 * @version 1.0
 */
public class RoundRectPanel extends JPanel {

    /**
     * Should the panel have shaded edges.
     */
    private boolean framed;

    /**
     * Creates a default {@code RoundRectPanel}.
     */
    public RoundRectPanel() {
        this(null, false);
    }

    /**
     * Creates a {@code RoundRectPanel} with a specific layout.
     * @param layout panel's layout
     */
    public RoundRectPanel(LayoutManager layout) {
        this(layout, false);
    }

    /**
     * Creates a {@code RoundRectPanel} while specifying if the edges should be shaded.
     * @param framed should the edges be shaded
     */
    public RoundRectPanel(boolean framed) {
        this(null, framed);
    }

    /**
     * Creates a {@code RoundRectPanel} with a specific layout, while specifying if the edges should be shaded.
     * @param layout panel's layout
     * @param framed should the edges be shaded
     */
    public RoundRectPanel(LayoutManager layout, boolean framed) {
        super(layout);
        setOpaque(false);
        setBorder(new EmptyBorder(5,5,7,5));

        this.framed = framed;
    }

    /**
     * Specifies if the {@code RoundRectPanel}'s edges should be shaded.
     * @param framed should the edges be shaded
     */
    public void setFramed(boolean framed) {
        this.framed = framed;
    }

    /**
     * Paints the {@code RoundRectPanel}.
     * @param g the {@code Graphics} object to protect
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = Math.max(getWidth(), getMinimumSize().width);
        int height = Math.max(getHeight(), getMinimumSize().height);
        if (width <= 10 || height <= 10) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (framed) {
            Color shadowFrame = new Color(getBackground().getRed(), getBackground().getGreen(), getBackground().getBlue(), 170);
            g2.setColor(shadowFrame);
            g2.fillRoundRect(0, 0, width, height, 40, 40);
        }

        g2.setColor(getBackground());
        g2.fillRoundRect(5, 5, width - 10, height - 12, 40, 40);
    }

}
