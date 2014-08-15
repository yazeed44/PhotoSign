package net.whitedesert.photosign.utils;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import net.whitedesert.photosign.R;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * Created by yazeed44 on 02/07/14.
 */
public final class PhotoUtil {

  public static final String BLENDED_DIR = "/blended_images";

    public static final String SIGNS_DIR = "/signs";

    public static final String SIGNED_PHOTO_DIR = "/signed_photos";


    public static String getPath(Uri uri,Activity a) {

        if( uri == null ) {
            return null;
        }

        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = a.managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }

        return uri.getPath();
    }

    public static  String savePicFromBitmap(Bitmap finalBitmap, final Activity activity,String dir) {


        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + dir);
        myDir.mkdirs();
        Random generator = new Random();
        final String savedFileStr =  activity.getResources().getString(R.string.file_saved);
        Date date = new Date();
        final String fname = "Image-"+ date.getTime() +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // ERROR 341 LINE
            out.flush();
            out.close();
            Log.i("Photo Util : " , "saved the photo successfully to  " + myDir +"/"+fname);
            if(activity != null)
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(activity,savedFileStr + fname,Toast.LENGTH_SHORT).show();
                }
            });



            return myDir +"/"+fname;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String savePicFromView(View drawView,Activity activity){
        drawView.setDrawingCacheEnabled(true);
        String imgSaved = MediaStore.Images.Media.insertImage(
                activity.getContentResolver(), drawView.getDrawingCache(),
                UUID.randomUUID().toString()+".png", "Signed Photo ");

        String saved_file =  activity.getResources().getString(R.string.file_saved);
        if(imgSaved!=null){
            Toast savedToast = Toast.makeText(activity.getApplicationContext(),
                   saved_file + imgSaved + " !", Toast.LENGTH_SHORT);
            savedToast.show();


        }

        else {
            Toast.makeText(activity,"Error , couldn't save the photo",Toast.LENGTH_SHORT).show();

        }

        drawView.destroyDrawingCache();

        return imgSaved;
    }

    public static void openPhoto(String path,final Activity a){


       final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + path), "image/*");

                a.startActivity(intent);


    }
}
