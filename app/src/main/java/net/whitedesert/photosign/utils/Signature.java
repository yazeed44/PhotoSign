package net.whitedesert.photosign.utils;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Log;

import java.io.File;

/**
 * Created by yazeed44 on 8/9/14.
 * Signature class
 */
public final class Signature {

    public int counter = 0;
    private String mPath;
    private String mName;
    private Bitmap mBitmap;
    private boolean mIsDefault = false;


    public Signature(String path, String name, int isDefault) {
        this.mPath = path;
        this.mName = name;
        if (isDefault == 1)
            this.mIsDefault = true;


    }

    public Signature() {

    }

    public Signature(final String path) {
        this.mPath = path;
        this.mName = new File(path).getName();
        this.mIsDefault = false;
    }

    public String getPath() {
        return this.mPath;
    }

    public void setPath(String path) {
        this.mPath = path;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public void setDefault(int isDefault) {


        this.mIsDefault = isDefault == 1;
    }

    public boolean isDefault() {
        return mIsDefault;
    }

    public void setDefault(boolean isDefault) {

        this.mIsDefault = isDefault;

    }

    public Bitmap getBitmap(boolean originalSize) {
        if (originalSize) {
            return ViewUtil.decodeFile(getPath());
        } else {
            return getBitmap();
        }
    }

    public Bitmap getBitmap() {
        return getBitmap(SignatureUtil.DEFAULT_SIGN_WIDTH, SignatureUtil.DEFAULT_SIGN_HEIGHT);
    }

    public Bitmap getBitmap(int width, int height) {

        if (counter == 0) {

            mBitmap = ViewUtil.decodeFile(getPath(), width, height);


            handleNull(mBitmap);


            counter++;
            return mBitmap;
        } else if (mBitmap != null && width == mBitmap.getWidth() && height == mBitmap.getHeight()) {
            return mBitmap;
        } else {

            mBitmap = ViewUtil.decodeFile(getPath(), width, height);


            handleNull(mBitmap);

        }

        return mBitmap;

    }


    private void handleNull(final Bitmap bitmap) {

        if (bitmap == null) {
            Log.e("getBitmap", "There was problem with " + getName() + "  - Signature , deleting it ..");
            SignatureUtil.deleteSignature(this, true);
        }
    }

    public Bitmap getBitmap(Point xy) {
        return getBitmap(xy.x, xy.y);
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
        return "Name  :  " + getName() + "   ,    Path   :  " + getPath() + "   ,  mIsDefault  :  " + isDefault();
    }
}
