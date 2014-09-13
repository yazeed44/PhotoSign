package net.whitedesert.photosign.utils;

import android.graphics.Bitmap;

import net.whitedesert.photosign.threads.BitmapThread;

/**
 * Created by yazeed44 on 9/3/14.
 * Class for dealing with bitmap
 */
public final class BitmapUtil {
    private BitmapUtil() {
        throw new AssertionError();
    }


    public static Bitmap decodeFile(String path) {
        BitmapThread.DecodeFileThread thread = new BitmapThread.DecodeFileThread(path, -1, -1);
        ThreadUtil.startAndJoin(thread);
        return thread.getBitmap();
    }

    public static Bitmap decodeFile(String path, int width, int height) {
        BitmapThread.DecodeFileThread thread = new BitmapThread.DecodeFileThread(path, width, height);
        ThreadUtil.startAndJoin(thread);
        return thread.getBitmap();
    }

    public static Bitmap getUpdatedOpacity(final Bitmap bitmap, final int opacity) {
        BitmapThread.UpdateOpacity thread = new BitmapThread.UpdateOpacity(bitmap, opacity);
        ThreadUtil.startAndJoin(thread);
        return thread.getUpdatedBitmap();
    }
}
