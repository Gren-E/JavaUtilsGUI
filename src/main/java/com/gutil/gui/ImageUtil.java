package com.gutil.gui;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.function.Function;

/**
 * Class providing image tools for resizing, cropping, flipping and color adjustment.
 * @author Ewelina Gren
 * @version 1.0
 */
public class ImageUtil {

    /**
     * Reads image from file without throwing exceptions on failure.
     * @param imageFile a {@code File} to read from.
     * @return {@code Image} from a specified file, or {@code null}.
     */
    public static Image readImage(File imageFile) {
        try {
            return ImageIO.read(imageFile);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Creates a deep copy of a provided {@code BufferedImage}.
     * @param bufferedImage a {@code BufferedImage} to be copied.
     * @return a copy of the input as {@code BufferedImage}.
     */
    public static BufferedImage deepCopy(BufferedImage bufferedImage) {
        ColorModel colorModel = bufferedImage.getColorModel();
        boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
        WritableRaster raster = bufferedImage.copyData(null);
        return new BufferedImage(colorModel, raster, isAlphaPremultiplied, null);
    }

    /**
     * Creates a resized version of the input. If one of the dimensions provided is zero,
     * the resulting {@code Image} gets resized according to the other target dimension, while preserving original proportions.
     * @param image an {@code Image} to be resized.
     * @param targetWidth a target width of the resized image.
     * @param targetHeight a target height of the resized image.
     * @param quality a constant value determining either a low quality fast result or more time-consuming quality scaling.
     * @return a resized version of an {@code Image}.
     */
    public static Image resize(Image image, int targetWidth, int targetHeight, ResizeQuality quality) {
        if (targetWidth < 0 || targetHeight < 0) {
            throw new IllegalArgumentException("Target width and target height must be positive numbers.");
        }

        if (targetWidth == 0 && targetHeight == 0) {
            throw new IllegalArgumentException("Target width and target height cannot be both zero");
        }

        int imageHeight = image.getHeight(null);
        int imageWidth = image.getWidth(null);

        //Calculating targetWidth or targetHeight based on the original proportions, if one of the dimensions is 0.
        targetWidth = targetWidth == 0 ? (int) (imageWidth * ((double) targetHeight / imageHeight)) : targetWidth;
        targetHeight = targetHeight == 0 ? (int) (imageHeight * ((double) targetWidth / imageWidth)) : targetHeight;

        if (targetWidth == imageWidth && targetHeight == imageHeight) {
            return image;
        }

        return switch (quality) {
            case LOW -> instantResize(image, targetWidth, targetHeight);
            case HIGH -> progressiveResize(image, targetWidth, targetHeight);
        };
    }

    /**
     * Creates a progressively resized version of the provided {@code Image}. Returns an image of a good quality but is time-consuming.
     */
    private static Image progressiveResize(Image image, int targetWidth, int targetHeight) {
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
            Graphics2D graphics2D = temporaryImage.createGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics2D.drawImage(newImage, 0, 0, imageWidth, imageHeight, null);
            graphics2D.dispose();

            newImage = temporaryImage;
        } while (imageWidth != targetWidth || imageHeight != targetHeight);

        return newImage;
    }

    /**
     * Creates a resized version of the provided {@code Image}. Very fast but might result in an image of a lower quality.
     */
    public static Image instantResize(Image image, int targetWidth, int targetHeight) {
        BufferedImage bufferedImage = (BufferedImage) image;

        int type = (bufferedImage.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;

        BufferedImage temporaryImage = new BufferedImage(targetWidth, targetHeight, type);
        Graphics2D graphics2D = temporaryImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        graphics2D.drawImage(bufferedImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();

        return temporaryImage;
    }

    /**
     * Creates an {@code Image} by horizontally flipping the image provided.
     * @param image an {@code Image} to be flipped.
     * @return a new {@code Image} which is a horizontally flipped version of the old one.
     */
    public static Image flipHorizontally(Image image) {
        return flip(image, true, false);
    }

    /**
     * Creates an {@code Image} by vertically flipping the image provided.
     * @param image an {@code Image} to be flipped.
     * @return a new {@code Image} which is a vertically flipped version of the old one.
     */
    public static Image flipVertically(Image image) {
        return flip(image, false, true);
    }

    /**
     * Creates a flipped version of an {@code Image}.
     * @param image an {@code Image} to be flipped.
     * @param flipHorizontally should the image be flipped horizontally.
     * @param flipVertically should the image be flipped vertically.
     * @return a new {@code Image} which is a flipped version of the old one.
     */
    private static Image flip(Image image, boolean flipHorizontally, boolean flipVertically) {
        if (!flipHorizontally && !flipVertically) {
            return image;
        }

        BufferedImage originalImage = (BufferedImage) image;
        BufferedImage newImage = deepCopy((BufferedImage) image);
        int width = newImage.getWidth();
        int height = newImage.getHeight();

        forEachPixel(newImage, point -> {
            int x = flipHorizontally ? width - point.x - 1 : point.x;
            int y = flipVertically ? height - point.y - 1 : point.y;
            return new Color(originalImage.getRGB(x, y));
        });

        return newImage;
    }

    /**
     * Creates an {@code Image} which is a version of the provided image, rotated by 90°.
     * @param image an {@code Image} to be rotated.
     * @return a new rotated {@code Image}.
     */
    public static Image rotateBy90Degrees(Image image) {
        BufferedImage originalImage = (BufferedImage) image;
        int width = originalImage.getHeight();
        int height = originalImage.getWidth();

        BufferedImage newImage = new BufferedImage(width, height, originalImage.getType());

        forEachPixel(newImage, point ->
                new Color(originalImage.getRGB(point.y, width - 1 - point.x)));

        return newImage;
    }

    /**
     * Creates an {@code Image} which is a version of the provided image, rotated by 180°.
     * @param image an {@code Image} to be rotated.
     * @return a new rotated {@code Image}.
     */
    public static Image rotateBy180Degrees(Image image) {
        return flip(image, true, true);
    }

    /**
     * Creates an {@code Image} which is a version of the provided image, rotated by 270°.
     * @param image an {@code Image} to be rotated.
     * @return a new rotated {@code Image}.
     */
    public static Image rotateBy270Degrees(Image image) {
        BufferedImage originalImage = (BufferedImage) image;
        int width = originalImage.getHeight();
        int height = originalImage.getWidth();

        BufferedImage newImage = new BufferedImage(width, height, originalImage.getType());

        forEachPixel(newImage, point ->
                new Color(originalImage.getRGB(height - 1 - point.y, point.x)));

        return newImage;
    }
    /**
     * Creates a cropped version of an {@code Image}. Parameters specify a portion of the image to be cropped from each side.
     * The sum of top and bottom crop cannot be greater than the image height, as well as the sum of right and left crop
     * cannot be greater than the image width.
     * @param image an {@code Image} to be cropped.
     * @param top a value to be cropped at the top.
     * @param right a value to be cropped on the right side.
     * @param bottom a value to be cropped at the bottom.
     * @param left a value to be cropped on the left side.
     * @return a new {@code Image} which is a cropped version of the old one.
     */
    public static Image crop(Image image, int top, int right, int bottom, int left) {
        BufferedImage originalImage = (BufferedImage) image;
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        if (top < 0 || right < 0 || bottom < 0 || left < 0) {
            throw new IllegalArgumentException("Cropping parameters value cannot be less than 0.");
        }

        if (top + bottom > originalHeight) {
            throw new IllegalArgumentException("Cannot crop image by more than its total height - invalid top and bottom parameters: " + top + ", " + bottom);
        }

        if (right + left > originalWidth) {
            throw new IllegalArgumentException("Cannot crop image by more than its total width - invalid right and left parameters: " + right + ", " + left);
        }

        int newWidth = originalWidth - right - left;
        int newHeight = originalHeight - top - bottom;
        BufferedImage newImage = new BufferedImage(newWidth, newHeight, originalImage.getType());

        forEachPixel(newImage, point -> {
            int originalX = point.x + left;
            int originalY = point.y + top;
            return new Color(originalImage.getRGB(originalX, originalY));
        });

        return newImage;
    }

    /**
     * Creates a cropped version of an {@code Image}. Parameters specify a portion of the image to be cropped from each side,
     * as a percentage of the appropriate image dimension (values between 0 and 100). The sum of top and bottom crop,
     * as well as the sum of right and left crop, cannot be over 100.
     * @param image an {@code Image} to be cropped.
     * @param top a value to be cropped at the top.
     * @param right a value to be cropped on the right side.
     * @param bottom a value to be cropped at the bottom.
     * @param left a value to be cropped on the left side.
     * @return a new {@code Image} which is a cropped version of the old one.
     */
    public static Image cropByPercentage(Image image, int top, int right, int bottom, int left) {
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

        return crop(image, topCrop, rightCrop, bottomCrop, leftCrop);
    }

    /**
     * Inverts all colors of an {@code Image}.
     * @param image an {@code Image} to be altered.
     * @return a new {@code Image} which is an inverted version of the original one.
     */
    public static Image invertColors(Image image) {
        BufferedImage newImage = deepCopy((BufferedImage) image);

        forEachPixel(newImage, point -> {
            Color xyColor = new Color(newImage.getRGB(point.x, point.y), true);
            return ColorUtil.inverted(xyColor);
        });

        return newImage;
    }

    /**
     * Converts the {@code Image} to grayscale.
     * @param image an {@code Image} to be altered.
     * @return a new {@code Image} which is a grayscale version of the original one.
     */
    public static Image convertToGrayscale(Image image) {
        BufferedImage newImage = deepCopy((BufferedImage) image);

        forEachPixel(newImage, point -> {
            Color xyColor = new Color(newImage.getRGB(point.x, point.y), true);
            return ColorUtil.grayscale(xyColor);
        });

        return newImage;
    }

    /**
     * Identifies a specified color in an {@code Image} and replaces it with another. The tolerance threshold is set to 0,
     * therefore only the pixels with the exact value of the original color will be altered, while all others stay unchanged.
     * @param image an {@code Image} to be altered.
     * @param originalColor a {@code Color} to be replaced.
     * @param newColor a target {@code Color} to replace the original one.
     * @return a new altered version of an {@code Image} with the original color replaced with the new one.
     */
    public static Image replaceColor(Image image, Color originalColor, Color newColor) {
        return replaceColor(image, originalColor, newColor, 0);
    }

    /**
     * Identifies a specified color in an {@code Image} and replaces it with another.
     * Colors of similar value to the original color might still be replaced depending on the provided threshold value.
     * @param image an {@code Image} to be altered.
     * @param originalColor a {@code Color} to be replaced.
     * @param newColor a target {@code Color} to replace the original one.
     * @param threshold an acceptable difference from the original color to still qualify for a replacement. The higher
     *                  the threshold, the bigger range of the color values are going to be replaced.
     * @return an altered version of an {@code Image} with the original color replaced with the new one.
     */
    public static Image replaceColor(Image image, Color originalColor, Color newColor, int threshold) {
        BufferedImage newImage = deepCopy((BufferedImage) image);

        forEachPixel(newImage, point -> {
            Color xyColor = new Color(newImage.getRGB(point.x, point.y), true);
            return (ColorUtil.isColorWithinRange(xyColor, originalColor, threshold))
                    ? ColorUtil.semiTransparent(newColor, xyColor.getAlpha()) : xyColor;
        });

        return newImage;
    }

    /**
     * Loops through each pixel of an image and repaints it according to the provided function.
     */
    private static void forEachPixel(BufferedImage newImage, Function<Point, Color> action) {
        int width = newImage.getWidth();
        int height = newImage.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                newImage.setRGB(x, y, action.apply(new Point(x, y)).getRGB());
            }
        }
    }

}
