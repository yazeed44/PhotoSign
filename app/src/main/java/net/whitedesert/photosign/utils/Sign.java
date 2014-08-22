package net.whitedesert.photosign.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static net.whitedesert.photosign.utils.SigningUtil.DEFAULT_SIGN_HEIGHT;
import static net.whitedesert.photosign.utils.SigningUtil.DEFAULT_SIGN_WIDTH;

/**
 * Created by yazeed44 on 8/9/14.
 */
public final class Sign {

    private String path;
    private String name;

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

    public Bitmap getBitmap() {
        return Bitmap.createScaledBitmap(BitmapFactory.decodeFile(getPath()), DEFAULT_SIGN_WIDTH, DEFAULT_SIGN_HEIGHT, true);

    }

    public Bitmap getBitmap(int width, int height) {
        return Bitmap.createScaledBitmap(BitmapFactory.decodeFile(getPath()), width, height, true);
    }
}
