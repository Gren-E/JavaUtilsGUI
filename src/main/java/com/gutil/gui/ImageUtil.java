package com.gutil.gui;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

/**
 * Class providing image tools such as deep copy and color replacement.
 * @author Ewelina Gren
 * @version 1.0
 */
public class ImageUtil {

    /**
     * Reads image from file. If the file cannot be read, an exception is not thrown and the return value is null.
     * @param imageFile a File to read from.
     * @return Image from a specified file, or null.
     */
    public static Image readImage(File imageFile) {
        try {
            return ImageIO.read(imageFile);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Creates a deep copy of a provided BufferedImage.
     * @param bi a BufferedImage to be copied.
     * @return a copy of the input as BufferedImage.
     */
    public static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    /**
     * Identifies a specified color in an image and replaces it with another. The tolerance threshold is set to 0,
     * therefore only the pixels with the exact value of the original color will be altered, while all others stay unchanged.
     * @param image an Image to be altered.
     * @param originalColor a color to be replaced.
     * @param newColor a target color to replace the original one.
     * @return an altered version of an Image with the original color replaced with the new one.
     */
    public static Image replaceColor(Image image, Color originalColor, Color newColor) {
        return replaceColor(image, originalColor, newColor, 0);
    }

    /**
     * Identifies a specified color in an image and replaces it with another.
     * Colors of similar value to the original color might still be replaced depending on the provided threshold value.
     * @param image an Image to be altered.
     * @param originalColor a color to be replaced.
     * @param newColor a target color to replace the original one.
     * @param threshold an acceptable difference from the original color to still qualify for a replacement.
     * @return an altered version of an Image with the original color replaced with the new one.
     */
    public static Image replaceColor(Image image, Color originalColor, Color newColor, int threshold) {
        BufferedImage img = deepCopy((BufferedImage) image);
        int width = img.getWidth();
        int height = img.getHeight();

        int origRed = originalColor.getRed();
        int origGreen = originalColor.getGreen();
        int origBlue = originalColor.getBlue();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color xyColor = new Color(img.getRGB(x, y), true);
                if (Math.abs(xyColor.getRed() - origRed) <= threshold) {
                    if (Math.abs(xyColor.getGreen() - origGreen) <= threshold) {
                        if (Math.abs(xyColor.getBlue() - origBlue) <= threshold) {
                            img.setRGB(x, y, new Color(newColor.getRed(), newColor.getGreen(), newColor.getBlue(), xyColor.getAlpha()).getRGB());
                        }
                    }
                }
            }
        }

        return img;
    }

}
