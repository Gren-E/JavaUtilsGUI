package com.gutil.gui;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * Class providing graphics tools.
 * @author Ewelina Gren
 * @version 1.0
 */
public class GraphicsUtil {

    /**
     * Draws text within provided rectangular area.
     * @param text a {@code String} value to be drawn.
     * @param rectangle the area in which the text is to be drawn.
     * @param font a {@code Font} to be used.
     * @param alignment a {@code HorizontalAlignment} value determining should the text be centered or aligned to left or right.
     * @param padX horizontal padding to be added for text aligned to left or right.
     * @param g2 the graphic environment.
     */
    public static void drawString(String text, Rectangle rectangle, Font font, HorizontalAlignment alignment, int padX, Graphics2D g2) {
        FontMetrics metrics = g2.getFontMetrics(font);

        int x = switch (alignment) {
            case CENTER -> rectangle.x + (rectangle.width - metrics.stringWidth(text)) / 2;
            case LEFT -> rectangle.x + padX;
            case RIGHT -> rectangle.width - metrics.stringWidth(text) - padX;
        };

        int y = rectangle.y + (rectangle.height - metrics.getHeight()) / 2 + metrics.getAscent();
        g2.setFont(font);
        g2.drawString(text, x, y);
    }

    /**
     * Matches the {@code Font} size to the specified area in order to draw the biggest {@code String} possible.
     * @param text a {@code String} value to be drawn.
     * @param rectangle the area in which the text is to be drawn.
     * @param font a {@code Font} to be used.
     * @param alignment a {@code HorizontalAlignment} value determining should the text be centered or aligned to left or right.
     * @param padX horizontal padding to be added for text aligned to left or right.
     * @param g2 the graphic environment.
     */
    public static void drawSizeAdjustedString(String text, Rectangle rectangle, Font font, HorizontalAlignment alignment, int padX, Graphics2D g2) {
        int fontSize = 1;
        font = new Font(font.getFontName(), font.getStyle(), fontSize);

        FontMetrics metrics = g2.getFontMetrics(font);
        if (metrics.stringWidth(text) + padX > rectangle.width || metrics.getHeight() > rectangle.height) {
            throw new IllegalArgumentException(String.format("Invalid rectangle size, too small to match a font. Dimensions: %d x %d.", rectangle.width, rectangle.height));
        }

        while (true) {
            metrics = g2.getFontMetrics(font);
            if (metrics.stringWidth(text) + padX < rectangle.width && metrics.getHeight() < rectangle.height) {
                fontSize++;
                font = new Font(font.getFontName(), font.getStyle(), fontSize);
            } else {
                break;
            }
        }

        drawString(text, rectangle, new Font(font.getFontName(), font.getStyle(), fontSize - 1), alignment, padX, g2);
    }

    /**
     * Provides a shortened version of the text by cutting off the letters that fall outside the specified {@code String}
     * length, and replacing them with "...".
     * @param text the original text to be shortened.
     * @param font a specific font used, which determines the width of the text.
     * @param width target width for the text to fit within.
     * @param g2 the graphic environment.
     * @return a shortened version of the provided {@code String}.
     */
    public static String cropString(String text, Font font, int width, Graphics2D g2) {
        FontMetrics metrics = g2.getFontMetrics(font);
        if (metrics.stringWidth(text) <= width) {
            return text;
        }

        int dotsWidth = metrics.stringWidth("...");
        if (dotsWidth > width) {
            return "";
        }

        while (metrics.stringWidth(text) > (width - dotsWidth) && !text.isEmpty()) {
            text = text.substring(0, text.length() - 1);
        }

        return text + "...";
    }

}
