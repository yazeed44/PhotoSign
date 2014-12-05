package net.whitedesert.photosign.utils;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

/**
 * Created by yazeed44 on 11/29/14.
 */
public final class BitmapUtil {

    public static final String GLOBAL_PATH = "file://";
    private static Bitmap bitmap;

    private BitmapUtil() {
        throw new AssertionError();
    }

    public static Bitmap decodeFile(String path) throws NullPointerException {
        bitmap = ImageLoader.getInstance().loadImageSync(GLOBAL_PATH + path);
        return bitmap;
    }

    public static Bitmap decodeFile(String path, int width, int height) {

        bitmap = ImageLoader.getInstance().loadImageSync(GLOBAL_PATH + path, new ImageSize(width, height));
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }
}
