package net.whitedesert.photosign.utils;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
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
        final String fname = "Image-"+ date.getTime() +".png";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 90, out); // ERROR 341 LINE
            out.flush();
            out.close();
            Log.i("Photo Util : " , "saved the photo successfully to  " + myDir +"/"+fname);
            if(activity != null)
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(activity,savedFileStr + fname,Toast.LENGTH_SHORT).show();
                }
            });


            MediaScannerConnection.scanFile(activity, new String[]{myDir + "/" + fname}, new String[]{"image/jpeg"}, null);
            return myDir +"/"+fname;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String savePicFromView(View drawView,Activity activity,String dir){

        Bitmap drawBitmap = getBitmapFromView(drawView);
        Canvas canvas = new Canvas(drawBitmap);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        drawBitmap.eraseColor(Color.TRANSPARENT);

        return savePicFromBitmap(drawBitmap,activity,dir);

    }

    public static Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

    public static void openPhoto(String path,final Activity a){


       final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + path), "image/*");

                a.startActivity(intent);


    }
}
