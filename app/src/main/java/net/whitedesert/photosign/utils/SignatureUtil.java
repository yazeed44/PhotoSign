package net.whitedesert.photosign.utils;

import android.graphics.Color;
import android.graphics.Paint;

import net.whitedesert.photosign.database.SignsDB;
import net.whitedesert.photosign.threads.DBThread;

import java.util.ArrayList;

/**
 * Created by yazeed44 on 8/8/14.
 * Class for dealing with signatures
 */
public final class SignatureUtil {

    public static final int DEFAULT_SIGN_HEIGHT = 200;
    public static final int DEFAULT_SIGN_WIDTH = 200;
    public static final Signature EMPTY_SIGNATURE = new Signature();


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

    public static ArrayList<Signature> getSigns(boolean includeDefault) {


        String getSignaturesOption = DBThread.GetSignThread.GET_ALL_SIGNS;

        if (!includeDefault) {
            getSignaturesOption = DBThread.GetSignThread.GET_ALL_SIGNS_NO_DEFAULT;
        }


        final DBThread.GetSignThread thread = new DBThread.GetSignThread(getSignaturesOption);
        ThreadUtil.startAndJoin(thread);

        return thread.getSignatures();
    }

    public static boolean isDuplicatedSign(String name) {
        DBThread.IsDuplicatedSignThread thread = new DBThread.IsDuplicatedSignThread(name);
        ThreadUtil.startAndJoin(thread);

        return thread.isDuplicated();
    }

    public static Signature getLatestSign() {
        final DBThread.GetSignThread thread = new DBThread.GetSignThread(DBThread.GetSignThread.GET_LATEST_SIGN);
        ThreadUtil.startAndJoin(thread);
        return thread.getSignature();
    }


    public static Signature getDefaultSignature() {
        final DBThread.GetSignThread thread = new DBThread.GetSignThread(DBThread.GetSignThread.GET_DEFAULT_SIGN);
        ThreadUtil.startAndJoin(thread);
        return thread.getSignature();
    }


    public static void setDefaultSignature(String name) {
        //TODO
        //Moving to a thread
        final SignsDB db = SignsDB.getInstance();
        db.openDatabase();
        db.setDefaultSignature(name);
        db.closeDatabase();


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


    public static boolean noSigns() {
        return SignatureUtil.getSigns(true).isEmpty();
    }



}
