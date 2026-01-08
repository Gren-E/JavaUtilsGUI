package com.gutil.gui;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageUtilTest {

    private static Image image;

    private static int xyRGB1;
    private static int xyRGB2;

    @BeforeAll
    public static void readTest() {
        Assertions.assertNull(ImageUtil.readImage(new File("Moria.png")));

        File file = new File(ImageUtilTest.class.getResource("/Moria.png").getFile());
        image = ImageUtil.readImage(file);
        Assertions.assertNotNull(image);

        //Sample RGB
        xyRGB1 = ((BufferedImage) image).getRGB(5, 5);
        xyRGB2 = ((BufferedImage) image).getRGB(200, 175);
    }

    @Test
    public void deepCopyTest() {
        BufferedImage image1 = ImageUtil.deepCopy((BufferedImage) image);

        Assertions.assertNotNull(image1);
        Assertions.assertNotEquals(image, image1);
        Assertions.assertEquals(image.getWidth(null), image1.getWidth(null));
        Assertions.assertEquals(((BufferedImage) image).getColorModel(), image1.getColorModel());
    }

    @Test
    public void resizeTest() {
        Image image1 = ImageUtil.resize(image, 500,0, ResizeQuality.HIGH);
        Image image2 = ImageUtil.resize(image, 0, 250, ResizeQuality.LOW);
        Image image3 = ImageUtil.resize(image2, 1400, 1000, ResizeQuality.HIGH);
        Image image4 = ImageUtil.resize(image, 3474, 4632, ResizeQuality.LOW);

        Assertions.assertNotNull(image1);
        Assertions.assertEquals(500, image1.getWidth(null));
        Assertions.assertEquals(666, image1.getHeight(null));
        Assertions.assertNotNull(image2);
        Assertions.assertEquals(187, image2.getWidth(null));
        Assertions.assertEquals(250, image2.getHeight(null));
        Assertions.assertNotNull(image3);
        Assertions.assertEquals(1400, image3.getWidth(null));
        Assertions.assertEquals(1000, image3.getHeight(null));
        Assertions.assertEquals(image, image4);
        Assertions.assertThrows(IllegalArgumentException.class, () -> ImageUtil.resize(image, 0, 0, ResizeQuality.HIGH));
        Assertions.assertThrows(IllegalArgumentException.class, () -> ImageUtil.resize(image, -10, 20, ResizeQuality.LOW));
        Assertions.assertThrows(IllegalArgumentException.class, () -> ImageUtil.resize(image, 100, -20, ResizeQuality.LOW));
    }

    @Test
    public void flipTest() {
        Image image1 = ImageUtil.flipHorizontally(image);
        Image image2 = ImageUtil.flipVertically(image);

        Assertions.assertEquals(xyRGB1, ((BufferedImage) image1).getRGB(((BufferedImage) image1).getWidth() - 6, 5));
        Assertions.assertEquals(xyRGB2, ((BufferedImage) image1).getRGB(((BufferedImage) image1).getWidth() - 201, 175));
        Assertions.assertEquals(xyRGB1, ((BufferedImage) image2).getRGB(5, ((BufferedImage) image2).getHeight() - 6));
        Assertions.assertEquals(xyRGB2, ((BufferedImage) image2).getRGB(200, ((BufferedImage) image2).getHeight() - 176));
    }

    @Test
    public void rotateTest() {
        Image image1 = ImageUtil.rotateBy90Degrees(image);
        Image image2 = ImageUtil.rotateBy180Degrees(image);
        Image image3 = ImageUtil.rotateBy270Degrees(image);

        Assertions.assertEquals(xyRGB1, ((BufferedImage) image1).getRGB(((BufferedImage) image1).getWidth() - 6, 5));
        Assertions.assertEquals(xyRGB2, ((BufferedImage) image1).getRGB(((BufferedImage) image1).getWidth() - 176, 200));
        Assertions.assertEquals(image.getWidth(null), image1.getHeight(null));
        Assertions.assertEquals(image.getHeight(null), image1.getWidth(null));
        Assertions.assertEquals(xyRGB1, ((BufferedImage) image2).getRGB(((BufferedImage) image2).getWidth() - 6, ((BufferedImage) image2).getHeight() - 6));
        Assertions.assertEquals(xyRGB2, ((BufferedImage) image2).getRGB(((BufferedImage) image2).getWidth() - 201, ((BufferedImage) image2).getHeight() - 176));
        Assertions.assertEquals(xyRGB1, ((BufferedImage) image3).getRGB(5, ((BufferedImage) image3).getHeight() - 6));
        Assertions.assertEquals(xyRGB2, ((BufferedImage) image3).getRGB(175, ((BufferedImage) image3).getHeight() - 201));
        Assertions.assertEquals(((BufferedImage) image3).getRGB(10, 20), ((BufferedImage) ImageUtil.rotateBy180Degrees(image1)).getRGB(10, 20));
    }

    @Test
    public void cropTest() {
        Image image1 = ImageUtil.crop(image, 100, 50, 100, 50);
        Image image2 = ImageUtil.cropByPercentage(image, 30, 20, 30, 20);

        Assertions.assertEquals(image.getWidth(null) - 100, image1.getWidth(null));
        Assertions.assertEquals(image.getHeight(null) - 200, image1.getHeight(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> ImageUtil.crop(image, -1, 5, 5, 5));
        Assertions.assertThrows(IllegalArgumentException.class, () -> ImageUtil.crop(image, 2500, 5, 2500, 5));
        Assertions.assertThrows(IllegalArgumentException.class, () -> ImageUtil.crop(image, 5, 2000, 5, 2000));
        Assertions.assertEquals((int)(image.getWidth(null) * 0.6), image2.getWidth(null));
        Assertions.assertEquals((int)(image.getHeight(null) * 0.4), image2.getHeight(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> ImageUtil.cropByPercentage(image, -1, 5, 5, 5));
        Assertions.assertThrows(IllegalArgumentException.class, () -> ImageUtil.cropByPercentage(image, 101, 5, 5, 5));
        Assertions.assertThrows(IllegalArgumentException.class, () -> ImageUtil.cropByPercentage(image, 60, 5, 60, 5));
        Assertions.assertThrows(IllegalArgumentException.class, () -> ImageUtil.cropByPercentage(image, 5, 60, 5, 60));
    }

    @Test
    public void colorAdjustmentsTest() {
        //invert
        Image image1 = ImageUtil.invertColors(image);

        Assertions.assertEquals(ColorUtil.inverted(new Color(xyRGB1)), new Color(((BufferedImage) image1).getRGB(5, 5)));
        Assertions.assertEquals(ColorUtil.inverted(new Color(xyRGB2)), new Color(((BufferedImage) image1).getRGB(200, 175)));

        //grayscale
        Image image2 = ImageUtil.convertToGrayscale(image);

        Assertions.assertEquals(ColorUtil.grayscale(new Color(xyRGB1)), new Color(((BufferedImage) image2).getRGB(5, 5)));
        Assertions.assertEquals(ColorUtil.grayscale(new Color(xyRGB2)), new Color(((BufferedImage) image2).getRGB(200, 175)));

        //replace color
        Color rgb1Color = new Color(xyRGB1);
        Color toleranceTestColor = new Color(rgb1Color.getRed() + 15, rgb1Color.getGreen() + 15, rgb1Color.getBlue() + 15);
        Image image3 = ImageUtil.replaceColor(image, rgb1Color, Color.CYAN);
        Image image4 = ImageUtil.replaceColor(image, toleranceTestColor, Color.RED, 20);

        Assertions.assertEquals(Color.CYAN, new Color(((BufferedImage) image3).getRGB(5, 5)));
        Assertions.assertEquals(Color.RED, new Color(((BufferedImage) image4).getRGB(5, 5)));
    }

}
