package net.whitedesert.photosign.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import net.whitedesert.photosign.R;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by yazeed44 on 02/07/14.
 */
public final class PhotoUtil {


    public static final String SIGNS_DIR = "/signs";

    public static final String SIGNED_PHOTO_DIR = "/signed_photos";


    public static String savePicFromBitmap(Bitmap finalBitmap, Activity activity, String dir, String name, boolean toast) {


        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + dir);
        myDir.mkdirs();
        final String savedFileStr = activity.getResources().getString(R.string.file_saved);
        final String fname = name + ".png";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 90, out); // ERROR 341 LINE
            out.flush();
            out.close();
            Log.i("Photo Util : ", "saved the photo successfully to  " + myDir + "/" + fname);
            if (toast)
                ToastUtil.toastShort(savedFileStr + myDir + "/" + fname, activity);

            MediaScannerConnection.scanFile(activity, new String[]{myDir + "/" + fname}, new String[]{"image/jpeg"}, null);
            return myDir + "/" + fname;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String savePicFromView(View drawView, Activity activity, String dir, String name, boolean toast) {

        drawView.setDrawingCacheEnabled(true);
        String path = savePicFromBitmap(drawView.getDrawingCache(true), activity, dir, name, toast);
        drawView.setDrawingCacheEnabled(false);
        return path;
    }


    public static void openPhoto(String path, final Activity a) {


        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + path), "image/*");

        a.startActivity(intent);


    }
}
