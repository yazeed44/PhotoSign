package net.whitedesert.photosign.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import net.whitedesert.photosign.database.MyDBHelper;
import net.whitedesert.photosign.database.SignsDB;

import java.util.ArrayList;

/**
 * Created by yazeed44 on 8/8/14.
 */
public final class SignUtil {

    public static final int DEFAULT_SIGN_HEIGHT = 200;
    public static final int DEFAULT_SIGN_WIDTH = 200;

    private SignUtil() {
        throw new AssertionError();
    }

    public static long addSign(Sign sign, Activity activity) {
        SignsDB db = new SignsDB(activity);
        db.open();

        long id = db.insertSign(sign);
        if (id != -1) {
            Log.i("SignUtil : addSign : ", "Added Sign successfully  : " + sign.getName());
            ToastUtil.toastSavedSignSuccess(activity);
        } else {
            Log.e("SignUtil : addSign : ", "failed to add sign !!");
        }
        db.close();
        return id;
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
        paint.setAlpha(255);

        return paint;
    }

    public static boolean isDuplicatedSign(String name, Context context) {
        SignsDB db = new SignsDB(context);
        db.open();
        boolean duplicated = db.isDuplicatedSign(name, MyDBHelper.TABLE_SIGNS);
        db.close();
        return duplicated;
    }

    public static Sign getLatestSign(Activity activity) {
        SignsDB db = new SignsDB(activity);
        db.open();
        Sign sign = db.getLatestSign();
        db.close();
        return sign;
    }

    public static Bitmap createBitmap(SignRaw signRaw, int width, int height) {
        Paint paint = signRaw.getPaint();

        float baseline = (int) (-paint.ascent() + 0.5f); // ascent() is negative
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(signRaw.getText(), 0, baseline, paint);
        return image;
    }

    public static Bitmap createBitmap(SignRaw signRaw) {
        return createBitmap(signRaw, signRaw.getMeasuredWidth(), signRaw.getMeasuredHeight());
    }


}
