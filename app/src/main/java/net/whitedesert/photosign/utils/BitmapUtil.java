package net.whitedesert.photosign.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

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

    public static Paint getPaint(int opacity) {
        Paint paint = new Paint();
        paint.setAlpha(opacity);
        return paint;
    }

    public static Bitmap getUpdatedOpacity(final Bitmap bitmap, final int opacity) {
        int width = bitmap.getWidth(), height = bitmap.getHeight();
        Bitmap transBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(transBitmap);
        canvas.drawARGB(0, 0, 0, 0);
        // config paint
        final Paint paint = getPaint(opacity);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return transBitmap;

    }
}
