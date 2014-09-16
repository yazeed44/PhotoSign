package net.whitedesert.photosign.utils;

import java.util.Random;

/**
 * Created by yazeed44 on 9/16/14.
 */
public final class RandomUtil {

    private RandomUtil() {
        throw new AssertionError();
    }

    public static String getRandomInt(int n) {
        Random random = new Random(System.currentTimeMillis());
        return random.nextInt(n) + "";
    }
}
