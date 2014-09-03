package net.whitedesert.photosign.utils;

import android.util.Log;

/**
 * Created by yazeed44 on 8/31/14.
 */
public final class ThreadUtil {

    private ThreadUtil() {
        throw new AssertionError();
    }

    public static void join(Thread t) {
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e("Thread Util : Join", e.getMessage());
        }
    }
}
