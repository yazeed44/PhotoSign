package net.whitedesert.photosign.threads;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.utils.ToastUtil;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by yazeed44 on 9/3/14.
 */
public final class BitmapThread {


    public static class DecodeFileThread extends Thread {
        private final String path;
        private final int width, height;
        private Bitmap bitmap;

        public DecodeFileThread(String path, int width, int height) {
            this.path = path;
            this.width = width;
            this.height = height;
            this.setName("Bitmap Thread - " + path);
        }

        @Override
        public void run() {
            bitmap = BitmapFactory.decodeFile(path);
            if (width != -1 && height != -1) {
                bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
            }


        }

        public Bitmap getBitmap() {
            return this.bitmap;
        }

    }

    public static class SaveBitmapThread extends Thread {
        private final Bitmap bitmap;
        private final Activity activity;
        private final String dir, name;
        private final boolean toast;
        private String path;

        public SaveBitmapThread(final Bitmap bitmap, final Activity activity, final String dir, final String name, final boolean toast) {
            this.bitmap = bitmap;
            this.activity = activity;
            this.dir = dir;
            this.name = name;
            this.toast = toast;
        }

        @Override
        public void run() {

            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + dir);
            myDir.mkdirs();
            final String savedFileStr = activity.getResources().getString(R.string.file_saved);
            final String fname = name + ".png";
            File file = new File(myDir, fname);
            if (file.exists()) file.delete();
            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.flush();
                out.close();
                Log.d("Photo Util : ", "saved the photo successfully to  " + myDir + "/" + fname);
                if (toast)
                    ToastUtil.toastShort(savedFileStr + myDir + "/" + fname);

                MediaScannerConnection.scanFile(activity, new String[]{myDir + "/" + fname}, new String[]{"image/jpeg"}, null);
                path = myDir + "/" + fname;

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("BitmapThread : SaveBitmapThread", e.getMessage());
            }
        }

        public String getPath() {
            return path;
        }
    }

    public static class UpdateOpacity extends Thread {

        private final Bitmap bitmap;
        private final int opacity;
        private Bitmap updatedBitmap;

        public UpdateOpacity(final Bitmap bitmap, final int opacity) {
            this.bitmap = bitmap;
            this.opacity = opacity;
        }

        @Override
        public void run() {

            int width = bitmap.getWidth(), height = bitmap.getHeight();
            updatedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(updatedBitmap);
            canvas.drawARGB(0, 0, 0, 0);
            // config paint
            final Paint paint = getPaint();
            canvas.drawBitmap(bitmap, 0, 0, paint);
        }

        private Paint getPaint() {
            Paint paint = new Paint();
            paint.setAlpha(opacity);
            return paint;
        }

        public Bitmap getUpdatedBitmap() {
            return updatedBitmap;
        }
    }
}
