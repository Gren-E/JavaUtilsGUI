package com.gutil.gui;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

/**
 * Class providing image tools for resizing, cropping, flipping and color adjustment.
 * @author Ewelina Gren
 * @version 1.0
 */
public class ImageUtil {

    public static final int RESIZE_QUALITY_LOW = 0;
    public static final int RESIZE_QUALITY_HIGH = 1;

    /**
     * Reads image from file without throwing exceptions on failure.
     * @param imageFile a File to read from.
     * @return {@code Image} from a specified file, or null.
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
     * @param bufferedImage a BufferedImage to be copied.
     * @return a copy of the input as {@code BufferedImage}.
     */
    public static BufferedImage deepCopy(BufferedImage bufferedImage) {
        ColorModel colorModel = bufferedImage.getColorModel();
        boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
        WritableRaster raster = bufferedImage.copyData(null);
        return new BufferedImage(colorModel, raster, isAlphaPremultiplied, null);
    }

    /**
     * Creates a resized version of the input. If one of the dimensions provided is not a positive number,
     * the resulting Image gets resized according to the other target dimension, while preserving original proportions.
     * @param image an Image to be resized.
     * @param targetWidth a target width of the resized image.
     * @param targetHeight a target height of the resized image.
     * @param quality a constant value determining either a low quality fast result or more time-consuming quality scaling.
     * @return a resized version of an {@code Image}.
     */
    public static Image resize(Image image, int targetWidth, int targetHeight, int quality) {
        if (quality != ImageUtil.RESIZE_QUALITY_LOW && quality != ImageUtil.RESIZE_QUALITY_HIGH) {
            throw new IllegalArgumentException("Quality parameter out of range: " + quality);
        }

        int imageHeight = image.getHeight(null);
        int imageWidth = image.getWidth(null);

        targetWidth = targetWidth <= 0 ? (int) (imageWidth * ((double) targetHeight / imageHeight)) : targetWidth;
        targetHeight = targetHeight <= 0 ? (int) (imageHeight * ((double) targetWidth / imageWidth)) : targetHeight;

        if (targetWidth == imageWidth && targetHeight == imageHeight) {
            return image;
        }

        if (quality == 0) {
            return instantResize(image, targetWidth, targetHeight, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        } else {
            return progressiveResize(image, targetWidth, targetHeight, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        }
    }

    /**
     * Creates a progressively resized version of the provided Image.
     */
    private static Image progressiveResize(Image image, int targetWidth, int targetHeight, Object hint) {
        BufferedImage newImage = (BufferedImage) image;

        int type = (newImage.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        int imageWidth = newImage.getWidth();
        int imageHeight = newImage.getHeight();

        do {
            if (imageWidth > targetWidth) {
                imageWidth /= 2;
                if (imageWidth < targetWidth) {
                    imageWidth = targetWidth;
                }
            }

            if (imageWidth < targetWidth) {
                imageWidth *= 2;
                if (imageWidth > targetWidth) {
                    imageWidth = targetWidth;
                }
            }

            if (imageHeight > targetHeight) {
                imageHeight /= 2;
                if (imageHeight < targetHeight) {
                    imageHeight = targetHeight;
                }
            }

            if (imageHeight < targetHeight) {
                imageHeight *= 2;
                if (imageHeight > targetHeight) {
                    imageHeight = targetHeight;
                }
            }

            BufferedImage temporaryImage = new BufferedImage(imageWidth, imageHeight, type);
            Graphics2D g2 = temporaryImage.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
            g2.drawImage(newImage, 0, 0, imageWidth, imageHeight, null);
            g2.dispose();

            newImage = temporaryImage;
        } while (imageWidth != targetWidth || imageHeight != targetHeight);

        return newImage;
    }

    /**
     * Creates a resized version of the provided Image.
     */
    public static Image instantResize(Image image, int targetWidth, int targetHeight, Object hint) {
        BufferedImage bufferedImage = (BufferedImage) image;

        int type = (bufferedImage.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;

        BufferedImage temporaryImage = new BufferedImage(targetWidth, targetHeight, type);
        Graphics2D g2 = temporaryImage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
        g2.drawImage(bufferedImage, 0, 0, targetWidth, targetHeight, null);
        g2.dispose();

        return temporaryImage;
    }

    /**
     * Creates a flipped version of an image.
     * @param image an Image to be flipped.
     * @param flipHorizontally should the image be flipped horizontally.
     * @param flipVertically should the image be flipped vertically.
     * @return a new {@code Image} which is a flipped version of the old one.
     */
    public static Image flip(Image image, boolean flipHorizontally, boolean flipVertically) {
        if (!flipHorizontally && !flipVertically) {
            return image;
        }

        BufferedImage originalImage = (BufferedImage) image;
        BufferedImage newImage = deepCopy((BufferedImage) image);
        int width = newImage.getWidth();
        int height = newImage.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int newX = flipHorizontally ? width - x - 1 : x;
                int newY = flipVertically ? height - y - 1 : y;
                newImage.setRGB(newX, newY, originalImage.getRGB(x, y));
            }
        }

        return newImage;
    }

    /**
     * Creates a cropped version of an image. Parameters specify a portion of the image to be cropped from each side,
     * as a percentage of the appropriate image dimension (values between 0 and 100). The sum of top and bottom crop,
     * as well as the sum of right and left crop, cannot be over 100.
     * @param image an image to be cropped.
     * @param top a value to be cropped at the top.
     * @param right a value to be cropped on the right side.
     * @param bottom a value to be cropped at the bottom.
     * @param left a value to be cropped on the left side.
     * @return a new {@code Image} which is a cropped version of the old one.
     */
    public static Image crop(Image image, int top, int right, int bottom, int left) {
        if (top < 0 || right < 0 || bottom < 0 || left < 0) {
            throw new IllegalArgumentException("Cropping parameters value must be between 0 and 100.");
        }

        if (top > 100 || right > 100 || bottom > 100 || left > 100) {
            throw new IllegalArgumentException("Cropping parameters value must be between 0 and 100.");
        }

        if (top + bottom > 100) {
            throw new IllegalArgumentException("Cannot crop image by more than 100% - invalid top and bottom parameters: " + top + ", " + bottom);
        }

        if (right + left > 100) {
            throw new IllegalArgumentException("Cannot crop image by more than 100% - invalid right and left parameters: " + right + ", " + left);
        }

        BufferedImage originalImage = (BufferedImage) image;
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        int topCrop = (int) Math.rint(originalHeight * (double) top/100);
        int rightCrop = (int) Math.rint(originalWidth * (double) right/100);
        int bottomCrop = (int) Math.rint(originalHeight * (double) bottom/100);
        int leftCrop = (int) Math.rint(originalWidth * (double) left/100);

        int newWidth = originalWidth - rightCrop - leftCrop;
        int newHeight = originalHeight - topCrop - bottomCrop;
        BufferedImage newImage = new BufferedImage(newWidth, newHeight, originalImage.getType());

        for (int x = 0; x < newWidth; x++) {
            for (int y = 0; y < newHeight; y++) {
                int originalX = x + leftCrop;
                int originalY = y + topCrop;
                newImage.setRGB(x, y, originalImage.getRGB(originalX, originalY));
            }
        }

        return newImage;
    }

    /**
     * Inverts all colors of an image.
     * @param image an original image to be altered.
     * @return a new {@code Image} which is an inverted version of the original one.
     */
    public static Image invertColors(Image image) {
        BufferedImage newImage = deepCopy((BufferedImage) image);
        int width = newImage.getWidth();
        int height = newImage.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color originalColor = new Color(newImage.getRGB(x, y), true);
                Color newColor = new Color(255 - originalColor.getRed(), 255 - originalColor.getGreen(), 255 - originalColor.getBlue(), originalColor.getAlpha());
                newImage.setRGB(x, y, newColor.getRGB());
            }
        }

        return newImage;
    }

    /**
     * Identifies a specified color in an image and replaces it with another. The tolerance threshold is set to 0,
     * therefore only the pixels with the exact value of the original color will be altered, while all others stay unchanged.
     * @param image an Image to be altered.
     * @param originalColor a color to be replaced.
     * @param newColor a target color to replace the original one.
     * @return a new altered version of an {@code Image} with the original color replaced with the new one.
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
     * @param threshold an acceptable difference from the original color to still qualify for a replacement. The higher
     *                  the threshold, the bigger range of the color values are going to be replaced.
     * @return an altered version of an {@code Image} with the original color replaced with the new one.
     */
    public static Image replaceColor(Image image, Color originalColor, Color newColor, int threshold) {
        BufferedImage newImage = deepCopy((BufferedImage) image);
        int width = newImage.getWidth();
        int height = newImage.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color xyColor = new Color(newImage.getRGB(x, y), true);
                if (colorValueWithinRange(xyColor, originalColor, threshold)) {
                    newImage.setRGB(x, y, buildColor(newColor, xyColor.getAlpha()).getRGB());
                }
            }
        }

        return newImage;
    }

    /**
     * Checks if a color falls within the target range of acceptable values.
     */
    private static boolean colorValueWithinRange(Color color, Color targetColor, int threshold) {
        if (threshold < 0) {
            throw new IllegalArgumentException("Threshold: " + threshold + " - cannot be a negative number.");
        }

        if (Math.abs(color.getRed() - targetColor.getRed()) > threshold) {
            return false;
        }

        if (Math.abs(color.getGreen() - targetColor.getGreen()) > threshold) {
            return false;
        }

        return Math.abs(color.getBlue() - targetColor.getBlue()) <= threshold;
    }

    /**
     * Builds a new color by adding transparency to the original color.
     */
    private static Color buildColor(Color color, int alpha) {
        if (alpha < 0 || alpha > 255) {
            throw new IllegalArgumentException("Alpha value: " + alpha + " - out of range.");
        }

        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

}
