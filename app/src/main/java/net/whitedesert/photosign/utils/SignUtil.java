package net.whitedesert.photosign.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import net.whitedesert.photosign.threads.DBThread;

import java.util.ArrayList;

/**
 * Created by yazeed44 on 8/8/14.
 * Class for dealing with signs
 */
public final class SignUtil {

    public static final int DEFAULT_SIGN_HEIGHT = 200;
    public static final int DEFAULT_SIGN_WIDTH = 200;

    private SignUtil() {
        throw new AssertionError();
    }

    public static long addSign(final Sign sign) {

        DBThread.AddSignThread thread = new DBThread.AddSignThread(sign);
        ThreadUtil.startAndJoin(thread);
        long id = thread.getId();
        CheckUtil.checkSign(id);

        return id;
    }


    public static Sign getSign(String name) {
        DBThread.GetSignThread thread = new DBThread.GetSignThread(name);
        ThreadUtil.startAndJoin(thread);
        return thread.getSign();
    }

    public static ArrayList<Sign> getSigns() {

        DBThread.GetSignThread thread = new DBThread.GetSignThread(null);
        ThreadUtil.startAndJoin(thread);

        return thread.getSigns();
    }

    public static boolean isDuplicatedSign(String name) {
        DBThread.IsDuplicatedSignThread thread = new DBThread.IsDuplicatedSignThread(name);
        ThreadUtil.startAndJoin(thread);

        return thread.getIsDuplicated();
    }

    public static Sign getLatestSign() {
        DBThread.GetSignThread thread = new DBThread.GetSignThread(DBThread.GetSignThread.GET_LATEST_SIGN);
        ThreadUtil.startAndJoin(thread);
        return thread.getSign();
    }

    /**
     * @return the default paint for drawing a signature
     */
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
