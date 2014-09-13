package net.whitedesert.photosign.utils;

import android.graphics.Bitmap;

/**
 * Created by yazeed44 on 8/9/14.
 * Signature class
 */
public final class Sign {

    private String path;
    private String name;

    private Bitmap bitmap;
    private int counter = 0;

    public Sign(String path, String name) {
        this.path = path;
        this.name = name;
    }

    public Sign() {

    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Bitmap getBitmap(boolean originalSize) {
        if (originalSize) {
            return BitmapUtil.decodeFile(getPath());
        } else {
            return getBitmap();
        }
    }

    public Bitmap getBitmap() {
        return getBitmap(SignUtil.DEFAULT_SIGN_WIDTH, SignUtil.DEFAULT_SIGN_HEIGHT);
    }

    public Bitmap getBitmap(int width, int height) {

        if (counter == 0) {
            bitmap = BitmapUtil.decodeFile(getPath(), width, height);

            counter++;
            return bitmap;
        } else if (width == bitmap.getWidth() && height == bitmap.getHeight()) {
            return bitmap;
        } else {
            bitmap = BitmapUtil.decodeFile(getPath(), width, height);
            return bitmap;
        }

    }

    public Bitmap getBitmap(XY xy) {
        return getBitmap(xy.getX(), xy.getY());
    }
}
