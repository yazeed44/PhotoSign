package net.whitedesert.photosign.threads;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.utils.ViewUtil;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by yazeed44 on 9/3/14.
 */
public final class SaveBitmapThread extends Thread {


    private final Bitmap mBitmap;
    private final Activity mActivity;
    private final String mDir, mName;
    private final boolean mToast;
    private String path;

    private SaveBitmapThread(final Builder builder) {
        this.mBitmap = builder.bitmap;
        this.mActivity = builder.activity;
        this.mDir = builder.dir;
        this.mName = builder.name;
        this.mToast = builder.toast;
    }

    @Override
    public void run() {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + mDir);
        myDir.mkdirs();
        final String savedFileStr = mActivity.getResources().getString(R.string.file_saved);
        final String fname = mName + ".png";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Log.d("Photo Util : ", "saved the photo successfully to  " + myDir + "/" + fname);
            if (mToast)
                ViewUtil.toastShort(savedFileStr + myDir + "/" + fname);

            MediaScannerConnection.scanFile(mActivity, new String[]{myDir + "/" + fname}, new String[]{"image/png"}, null);
            path = myDir + "/" + fname;

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("BitmapThread : SaveBitmapThread", e.getMessage());
        }
    }

    public String getPath() {
        return path;
    }


    public static class Builder {
        private final Activity activity;
        private Bitmap bitmap;
        private String dir, name;
        private boolean toast = false;

        public Builder(final Activity activity) {
            this.activity = activity;
        }

        public Builder bitmap(final Bitmap bitmap) {
            this.bitmap = bitmap;
            return this;
        }

        public Builder dir(final String dir) {
            this.dir = dir;
            return this;
        }

        public Builder name(final String name) {
            this.name = name;
            return this;
        }

        public Builder toast(final boolean toast) {
            this.toast = toast;
            return this;
        }

        public SaveBitmapThread build() {
            return new SaveBitmapThread(this);
        }


    }

}
