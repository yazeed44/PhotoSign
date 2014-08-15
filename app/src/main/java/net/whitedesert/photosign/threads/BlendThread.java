package net.whitedesert.photosign.threads;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by yazeed44 on 8/7/14.
 */
public class BlendThread extends Thread {


    private Activity a;
    private String imgPath1,imgPath2;
    private String pathBlended;
    public BlendThread(String c,String s,Activity activity){
this.imgPath1 = c;
        this.imgPath2 = s;
        this.a = activity;
    }
    @Override
    public void run(){
       Bitmap c = BitmapFactory.decodeFile(imgPath1), s = BitmapFactory.decodeFile(imgPath2);

      //Bitmap blended =  BlendUtil.combineImages(c, s);
        //pathBlended = PhotoUtil.saveBitmap(blended, a);
        try {
            this.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public String getPath(){
        return pathBlended;
    }
}
