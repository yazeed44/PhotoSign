package net.whitedesert.photosign.utils;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * Created by yazeed44 on 8/9/14.
 * Signature class
 */
public final class Signature {

    public int counter = 0;
    private String path;
    private String name;
    private Bitmap bitmap;
    private boolean isDefault = false;


    public Signature(String path, String name, int isDefault) {
        this.path = path;
        this.name = name;
        if (isDefault == 1)
            this.isDefault = true;


    }

    public Signature() {

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

    public void setDefault(int isDefault) {


        this.isDefault = isDefault == 1;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {

        this.isDefault = isDefault;

    }

    public Bitmap getBitmap(boolean originalSize) {
        if (originalSize) {
            return BitmapUtil.decodeFile(getPath());
        } else {
            return getBitmap();
        }
    }

    public Bitmap getBitmap() {
        return getBitmap(SignatureUtil.DEFAULT_SIGN_WIDTH, SignatureUtil.DEFAULT_SIGN_HEIGHT);
    }

    public Bitmap getBitmap(int width, int height) {

        if (counter == 0) {

            bitmap = BitmapUtil.decodeFile(getPath(), width, height);


            handleNull(bitmap);


            counter++;
            return bitmap;
        } else if (bitmap != null && width == bitmap.getWidth() && height == bitmap.getHeight()) {
            return bitmap;
        } else {

            bitmap = BitmapUtil.decodeFile(getPath(), width, height);


            handleNull(bitmap);

        }

            return bitmap;

    }


    private void handleNull(final Bitmap bitmap) {

        if (bitmap == null) {
            Log.e("getBitmap", "There was problem with " + getName() + "  - Signature , deleting it ..");
            SignatureUtil.deleteSignature(this, true);
        }
    }

    public Bitmap getBitmap(XY xy) {
        return getBitmap(xy.getX(), xy.getY());
    } // Width and height

    @Override
    public boolean equals(Object object) {
        if (object instanceof Signature) {
            final Signature sign = (Signature) object;
            return this == sign;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Name  :  " + getName() + "   ,    Path   :  " + getPath() + "   ,  isDefault  :  " + isDefault();
    }
}
