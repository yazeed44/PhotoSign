package net.whitedesert.photosign.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;

import net.whitedesert.photosign.threads.BitmapThread;

/**
 * Created by yazeed44 on 02/07/14.
 * Class for dealing with photo(The photo to sign on) stuff
 */
public final class PhotoUtil {


    public static final String SIGNS_DIR = "/signs";

    public static final String SIGNED_PHOTO_DIR = "/signed_photos";


    public static String savePicFromBitmap(Bitmap finalBitmap, Activity activity, String dir, String name, boolean toast) {
        BitmapThread.SaveBitmapThread thread = new BitmapThread.SaveBitmapThread(finalBitmap, activity, dir, name, toast);
        ThreadUtil.startAndJoin(thread);
        return thread.getPath();
    }

    public static String savePicFromView(View drawView, Activity activity, String dir, String name, boolean toast) {

        drawView.setDrawingCacheEnabled(true);
        String path = savePicFromBitmap(drawView.getDrawingCache(true), activity, dir, name, toast);
        drawView.setDrawingCacheEnabled(false);
        return path;
    }

    public static XY getWidthHeight(Bitmap first, Bitmap second) {
        XY xy = new XY();
        int width, height;

        if (first.getWidth() > second.getWidth()) {
            width = first.getWidth();
            height = first.getHeight();
        } else {
            width = second.getWidth() + second.getWidth();
            height = first.getHeight();
        }

        xy.setX(width);
        xy.setY(height);
        return xy;
    }


    public static XY getCenter(int width, int height) {
        int x = (width) / 2;
        int y = height / 2;
        return new XY(x, y);
    }

    public static XY getCenter(Bitmap photo) {

        return getCenter(photo.getWidth(), photo.getHeight());
    }
}
