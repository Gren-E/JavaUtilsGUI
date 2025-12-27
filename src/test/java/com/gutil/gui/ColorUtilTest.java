package com.gutil.gui;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.awt.Color;
import java.util.stream.Stream;

public class ColorUtilTest {

    @ParameterizedTest
    @MethodSource("rangeToleranceTestSource")
    public void rangeToleranceTest(Boolean expectedResult, Color color1, Color color2, int threshold) {
        Assertions.assertEquals(expectedResult, ColorUtil.isColorWithinRange(color1, color2, threshold));
    }

    private static Stream<Arguments> rangeToleranceTestSource() {
        return Stream.of(
                Arguments.of(true, new Color(0,0,0), new Color(10,10,10), 15),
                Arguments.of(true, new Color(35,20,90), new Color(5,40,120), 35),
                Arguments.of(true, new Color(120, 40, 20, 30), new Color(130, 50, 10, 100), 20),
                Arguments.of(true, new Color(200, 240, 220, 30), new Color(180, 250, 210), 30),
                Arguments.of(false, new Color(10,10,10), new Color(200,200,200), 50),
                Arguments.of(false, new Color(0,50,0), new Color(0,70,0), 10),
                Arguments.of(false, new Color(0,0,70), new Color(0,0,30), 15),
                Arguments.of(false, null, Color.BLUE, 20),
                Arguments.of(false, Color.BLUE, null, 20),
                Arguments.of(false, null, null, 4)
        );
    }

    @ParameterizedTest
    @MethodSource("inversionTestSource")
    public void inversionTest(Color expectedColor, Color originalColor) {
        Assertions.assertEquals(expectedColor, ColorUtil.inverted(originalColor));
    }

    private static Stream<Arguments> inversionTestSource() {
        return Stream.of(
                Arguments.of(new Color(255,255,0), Color.BLUE),
                Arguments.of(new Color(155,55,55), new Color(100,200,200)),
                Arguments.of(Color.WHITE, Color.BLACK),
                Arguments.of(new Color(5,155,255,200), new Color(250,100,0,200)),
                Arguments.of(new Color(10, 15, 75, 150), new Color(245,240,180, 150)),
                Arguments.of(new Color(0,0,0,0), new Color(255,255,255,0)),
                Arguments.of(null, null)
        );
    }

    @ParameterizedTest
    @MethodSource("grayscaleTestSource")
    public void grayscaleTest(Color expectedColor, Color originalColor) {
        Assertions.assertEquals(expectedColor, ColorUtil.grayscale(originalColor));
    }

    private static Stream<Arguments> grayscaleTestSource() {
        return Stream.of(
                Arguments.of(new Color(85,85,85), Color.BLUE),
                Arguments.of(new Color(200,200,200), new Color(200,250,150)),
                Arguments.of(Color.WHITE, Color.WHITE),
                Arguments.of(Color.BLACK, Color.BLACK),
                Arguments.of(new Color(76,76,76,200), new Color(130,100,0,200)),
                Arguments.of(new Color(93, 93, 93, 150), new Color(90,60,130, 150)),
                Arguments.of(new Color(0,0,0,0), new Color(0,0,0,0)),
                Arguments.of(null, null)
        );
    }

    @ParameterizedTest
    @MethodSource("transparencyTestSource")
    public void transparencyTest(Color expectedColor, Color originalColor, int alpha) {
        Assertions.assertEquals(expectedColor, ColorUtil.semiTransparent(originalColor, alpha));
    }

    private static Stream<Arguments> transparencyTestSource() {
        return Stream.of(
                Arguments.of(new Color(0,0,255, 50), Color.BLUE, 50),
                Arguments.of(new Color(100,200,200, 200), new Color(100,200,200), 200),
                Arguments.of(Color.BLACK, Color.BLACK, 255),
                Arguments.of(new Color(250,100,0, 50), new Color(250,100,0), 50),
                Arguments.of(new Color(245,240,180, 150), new Color(245,240,180), 150),
                Arguments.of(new Color(255,255,255, 0), new Color(255,255,255), 0),
                Arguments.of(null, null, 150)
        );
    }

    @Test
    public void exceptionTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> ColorUtil.isColorWithinRange(Color.BLUE, Color.RED, -27));
        Assertions.assertThrows(IllegalArgumentException.class, () -> ColorUtil.isColorWithinRange(Color.BLUE, Color.RED, 270));

        Assertions.assertThrows(IllegalArgumentException.class, () -> ColorUtil.semiTransparent(Color.BLUE, -27));
        Assertions.assertThrows(IllegalArgumentException.class, () -> ColorUtil.semiTransparent(Color.BLUE, 270));
    }

}
