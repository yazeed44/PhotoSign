package net.whitedesert.photosign.utils;

import android.content.Context;

import net.whitedesert.photosign.database.SignsDB;

import java.util.ArrayList;

/**
 * Created by yazeed44 on 8/8/14.
 */
public final class SignUtil {


    public static void addSignRaw(SignRaw sign,Context context)  {
       SignsDB db = new SignsDB(context);
        db.open();
        db.insertSignRaw(sign);
        db.close();
    }

    public static void addSignRaw(String name , String text ,String font , int color,int textSize,int opacity,int style , Context context ){
        SignRaw raw = new SignRaw();
        raw.setName(name);
        raw.setText(text);
        raw.setFont(font);
        raw.setColor(color);
        raw.setTextSize(textSize);
        raw.setOpacity(opacity);
        raw.setStyle(style);
        addSignRaw(raw,context);
    }

    public static void addSign(Sign sign , Context context){
        SignsDB db = new SignsDB(context);
        db.open();
        db.insertSign(sign);
        db.close();
    }

    public static void addSign(String name,String path , Context context){
        Sign sign = new Sign();
        sign.setName(name);
        sign.setPath(path);
        addSign(sign,context);
    }



    public static SignRaw getSignRaw(int id,Context context){

        SignsDB db= new SignsDB(context);
        db.open();
        SignRaw raw = db.getSignRaw(id);
        db.close();
        return raw;
    }

    public static SignRaw getSignRaw(String name,Context context){
        SignsDB db = new SignsDB(context);
        db.open();
        SignRaw raw = db.getSignRaw(name);
        db.close();
        return raw;
    }

    public static Sign getSign(String name , Context context){
        SignsDB db = new SignsDB(context);
        db.open();
        Sign sign = db.getSign(name);
        db.close();
        return sign;
    }

    public static ArrayList<Sign> getSigns(Context context) {

        SignsDB db = new SignsDB(context);
        db.open();
        ArrayList<Sign> signsList = db.getSigns();
        db.close();
        return signsList;
    }


}
