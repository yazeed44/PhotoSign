package net.whitedesert.photosign.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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
        return BitmapFactory.decodeFile(getPath());
    }
}
