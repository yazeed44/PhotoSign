package net.whitedesert.photosign.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import net.whitedesert.photosign.threads.DBThread;

import java.util.ArrayList;

/**
 * Created by yazeed44 on 8/8/14.
 * Class for dealing with signatures
 */
public final class SignatureUtil {

    public static final int DEFAULT_SIGN_HEIGHT = 200;
    public static final int DEFAULT_SIGN_WIDTH = 200;


    private SignatureUtil() {
        throw new AssertionError();
    }

    public static long addSign(final Signature signature) {

        DBThread.AddSignThread thread = new DBThread.AddSignThread(signature);
        ThreadUtil.startAndJoin(thread);
        long id = thread.getId();
        CheckUtil.checkSign(id);

        return id;
    }


    public static Signature getSign(String name) {
        DBThread.GetSignThread thread = new DBThread.GetSignThread(name);
        ThreadUtil.startAndJoin(thread);
        return thread.getSignature();
    }

    public static ArrayList<Signature> getSigns() {

        DBThread.GetSignThread thread = new DBThread.GetSignThread(DBThread.GetSignThread.GET_ALL_SIGNS);
        ThreadUtil.startAndJoin(thread);

        return thread.getSignatures();
    }

    public static boolean isDuplicatedSign(String name) {
        DBThread.IsDuplicatedSignThread thread = new DBThread.IsDuplicatedSignThread(name);
        ThreadUtil.startAndJoin(thread);

        return thread.getIsDuplicated();
    }

    public static Signature getLatestSign() {
        final DBThread.GetSignThread thread = new DBThread.GetSignThread(DBThread.GetSignThread.GET_LATEST_SIGN);
        ThreadUtil.startAndJoin(thread);
        return thread.getSignature();
    }

    /**
     * @return the default paint for drawing a signature
     */
    public static Paint getDefaultPaintForDraw() {
        final Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(20);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAlpha(255);

        return paint;
    }


    public static Bitmap createBitmap(SignatureRaw signatureRaw, int width, int height) {
        final Paint paint = signatureRaw.getPaint();

        final float baseline = (int) (-paint.ascent() + 0.5f); // ascent() is negative
        final Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(image);
        canvas.drawText(signatureRaw.getText(), 0, baseline, paint);
        return image;
    }


    public static Bitmap createBitmap(SignatureRaw signatureRaw, boolean measured) {

        if (measured)
            return createBitmap(signatureRaw, signatureRaw.getMeasuredWidth(), signatureRaw.getMeasuredHeight());
        else {
            return createBitmap(signatureRaw, signatureRaw.getWidth(), signatureRaw.getHeight());
        }
    }


    public static boolean noSigns() {
        return SignatureUtil.getSigns().isEmpty();
    }

}
