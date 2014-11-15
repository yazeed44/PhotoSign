package net.whitedesert.photosign.utils;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

/**
 * Created by yazeed44 on 9/3/14.
 * Class for dealing with bitmap
 */
public final class BitmapUtil {
    public static final String GLOBAL_PATH = "file://";
    public static Bitmap bitmap;

    private BitmapUtil() {
        throw new AssertionError();
    }

    public static Bitmap decodeFile(String path) throws NullPointerException {
        /*BitmapThread.DecodeFileThread thread = new BitmapThread.DecodeFileThread(path, -1, -1);
        ThreadUtil.startAndJoin(thread);*/


        bitmap = ImageLoader.getInstance().loadImageSync(GLOBAL_PATH + path);


        return bitmap;
    }

    public static Bitmap decodeFile(String path, int width, int height) {
       /* BitmapThread.DecodeFileThread thread = new BitmapThread.DecodeFileThread(path, width, height);
        ThreadUtil.startAndJoin(thread);
        return thread.getBitmap();*/

        bitmap = ImageLoader.getInstance().loadImageSync(GLOBAL_PATH + path, new ImageSize(width, height));


        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }
}
