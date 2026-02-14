package com.gutil.gui.adapters;

import java.awt.Color;

/**
 * An interface defining framework for a custom highlight in {@code HighlightingMouseAdapter}.
 * @author Ewelina Gren
 * @version 1.0
 */
public interface CustomHighlight {

    /**
     * Sets the target element to the highlight or to the original color.
     * @param color what color should the element be set to
     */
    void toggleHighlight(Color color);

    /**
     * Returns the original {@code Color} of the element to be highlighted.
     * @return the original color of the element
     */
    Color getOriginalColor();

}
