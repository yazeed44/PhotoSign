package whitedessert.com.photosign.utils;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Random;

/**
 * Created by yazeed44 on 02/07/14.
 */
public final class PhotoUtil {




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

    public static  String saveBitmap(Bitmap finalBitmap, final Activity activity) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/blended_images");
        myDir.mkdirs();
        Random generator = new Random();

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
                    Toast.makeText(activity,fname + " successfully saved!",Toast.LENGTH_SHORT).show();
                }
            });

            return myDir +"/"+fname;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void openPhoto(String path,final Activity a){


       final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + path), "image/*");

                a.startActivity(intent);


    }
}
