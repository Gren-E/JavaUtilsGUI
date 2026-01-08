package com.gutil.gui;

import java.awt.Color;

/**
 * Class providing color adjustment tools.
 * @author Ewelina Gren
 * @version 1.0
 */
public class ColorUtil {

    /**
     * Checks if two colors are within a specified tolerance range. Returns {@code false} if the colors are too different
     * or when one or both colors are {@code null}.
     * @param firstColor a {@code Color} to be compared to the other.
     * @param secondColor a second {@code Color} to be compared to the first one.
     * @param threshold a specified tolerance threshold.
     * @return a {@code boolean} describing whether the two colors are similar enough.
     */
    public static boolean isColorWithinRange(Color firstColor, Color secondColor, int threshold) {
        if (firstColor == null || secondColor == null) {
            return false;
        }

        if (threshold < 0 || threshold > 255) {
            throw new IllegalArgumentException("Threshold: " + threshold + " - out of range.");
        }

        return Math.abs(firstColor.getRed() - secondColor.getRed()) <= threshold
                && Math.abs(firstColor.getGreen() - secondColor.getGreen()) <= threshold
                && Math.abs(firstColor.getBlue() - secondColor.getBlue()) <= threshold;
    }

    /**
     * Creates a {@code Color} by inverting all the RGB values of the original color. Returns {@code null} if the original color is {@code null}.
     * @param color a {@code Color} to be inverted.
     * @return a {@code Color} opposite to the color provided.
     */
    public static Color inverted(Color color) {
        if (color == null) {
            return null;
        }
        return new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue(), color.getAlpha());
    }

    /**
     * Creates a {@code Color} by replacing the RGB values of the provided color with their mean value. Returns {@code null} if the original color is {@code null}.
     * @param color a {@code Color} to be turned to grayscale.
     * @return a grayscale {@code Color} closest in value to the original color.
     */
    public static Color grayscale(Color color) {
        if (color == null) {
            return null;
        }
        int meanValue = (color.getRed() + color.getBlue() + color.getGreen()) / 3;
        return new Color(meanValue, meanValue, meanValue, color.getAlpha());
    }

    /**
     * Creates a new {@code Color} by setting a new transparency value of the provided color. Returns {@code null} if the original color is {@code null}.
     * @param color an original {@code Color}.
     * @param alpha a transparency level of the new color, takes values between 0 and 255.
     * @return a {@code Color} with the specified transparency level.
     */
    public static Color semiTransparent(Color color, int alpha) {
        if (color == null) {
            return null;
        }
        if (alpha < 0 || alpha > 255) {
            throw new IllegalArgumentException(String.format("Alpha value: %d - out of range.", alpha));
        }

        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

}
