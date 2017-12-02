package com.seeger.library.utils;

import android.graphics.Color;

import java.util.Random;

/**
 * color utils
 *
 * @author seeger
 */
public class ColorUtils {

    /**
     * get random color
     *
     * @return color
     */
    public static int getRandomColor() {
        Random random = new Random();
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return Color.rgb(r, g, b);
    }
}
