package com.gutil.gui;

import java.awt.Color;

public class ColorUtil {

    /**
     * Creates a new Color by setting a new transparency value of the provided color.
     * @param color an original Color.
     * @param alpha a transparency level of the new color, takes values between 0 and 255.
     * @return a {@code Color} with the specified transparency level.
     */
    public static Color setColorTransparency(Color color, int alpha) {
        if (alpha < 0 || alpha > 255) {
            throw new IllegalArgumentException("Alpha value: " + alpha + " - out of range.");
        }

        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    /**
     * Checks if two colors are within a specified tolerance range. Returns false if the colors are too different.
     * @param firstColor a Color to be compared to the other.
     * @param secondColor a second Color to be compared to the first one.
     * @param threshold a specified tolerance threshold.
     * @return a {@code boolean} describing whether the two colors are similar enough.
     */
    public static boolean isColorWithinRange(Color firstColor, Color secondColor, int threshold) {
        if (threshold < 0) {
            throw new IllegalArgumentException("Threshold: " + threshold + " - cannot be a negative number.");
        }

        if (Math.abs(firstColor.getRed() - secondColor.getRed()) > threshold) {
            return false;
        }

        if (Math.abs(firstColor.getGreen() - secondColor.getGreen()) > threshold) {
            return false;
        }

        return Math.abs(firstColor.getBlue() - secondColor.getBlue()) <= threshold;
    }

    /**
     * Creates a Color by inverting all the RGB values of the original color.
     * @param color a Color to be inverted.
     * @return a {@code Color} opposite to the color provided.
     */
    public static Color invertColor(Color color) {
        return new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue(), color.getAlpha());
    }

    /**
     * Creates a Color by replacing the RGB values of the provided color with their mean value.
     * @param color a Color to be turned to grayscale.
     * @return a grayscale {@code Color} closest in value to the original color.
     */
    public static Color getGrayscaleEquivalent(Color color) {
        int meanValue = (color.getRed() + color.getBlue() + color.getGreen()) / 3;
        return new Color(meanValue, meanValue, meanValue, color.getAlpha());
    }

}
