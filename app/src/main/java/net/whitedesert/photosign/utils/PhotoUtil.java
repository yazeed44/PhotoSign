package net.whitedesert.photosign.utils;

import android.graphics.Bitmap;

/**
 * Created by yazeed44 on 02/07/14.
 * Class for dealing with photo(The photo to sign on) stuff
 */
public final class PhotoUtil {


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
