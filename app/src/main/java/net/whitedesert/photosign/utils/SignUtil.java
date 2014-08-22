package net.whitedesert.photosign.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;

import net.whitedesert.photosign.database.MyDBHelper;
import net.whitedesert.photosign.database.SignsDB;

import java.util.ArrayList;

/**
 * Created by yazeed44 on 8/8/14.
 */
public final class SignUtil {

    private SignUtil() {
        throw new AssertionError();
    }

    public static long addSign(Sign sign, Context context) {
        SignsDB db = new SignsDB(context);
        db.open();

        long id = db.insertSign(sign);
        db.close();
        return id;
    }

    public static long addSign(String name, String path, Context context) {
        Sign sign = new Sign();
        sign.setName(name);
        sign.setPath(path);
        return addSign(sign, context);
    }


    public static Sign getSign(String name, Context context) {
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

    public static void dropEverything(Context context) {
        SignsDB db = new SignsDB(context);
        db.open();
        db.dropAll();
        db.close();
    }

    public static Paint getDefaultPaintForDraw() {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(20);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);

        return paint;
    }

    public static boolean isDuplicatedSign(String name, Context context) {
        SignsDB db = new SignsDB(context);
        db.open();
        boolean duplicated = db.isDuplicatedSign(name, MyDBHelper.TABLE_SIGNS);
        db.close();
        return duplicated;
    }


}
