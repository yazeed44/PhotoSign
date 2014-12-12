package net.whitedesert.photosign.utils;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import net.whitedesert.photosign.threads.DBThread;
import net.yazeed44.imagepicker.PickerActivity;

import java.util.ArrayList;

/**
 * Created by yazeed44 on 8/8/14.
 * Class for dealing with signatures , almost of it is referencing to DB
 */
public final class SignatureUtil {

    public static final int DEFAULT_SIGN_HEIGHT = 200;
    public static final int DEFAULT_SIGN_WIDTH = 200;
    public static final Signature EMPTY_SIGNATURE = new Signature();


    private SignatureUtil() {
        throw new AssertionError();
    }


    public static long addSign(final Signature signature, boolean toast) {

        DBThread.AddSignThread thread = new DBThread.AddSignThread(signature);
        ThreadUtil.startAndJoin(thread);

        long id = thread.getId();
        CheckUtil.checkSign(id, toast);

        return id;
    }

    public static long[] addSigns(final String[] paths) {
        final long[] ids = new long[paths.length];

        for (int i = 0; i < paths.length; i++) {
            ids[i] = SignatureUtil.addSign(new Signature(paths[i]), false);
        }


        return ids;

    }

    public static long[] addSigns(final ArrayList<Signature> signatures) {
        final long[] ids = new long[signatures.size()];

        for (int i = 0; i < signatures.size(); i++) {
            final long id = addSign(signatures.get(i), false);
            ids[i] = id;

        }

        return ids;
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
        final DBThread.SetDefaultSignThread thread = new DBThread.SetDefaultSignThread(name);
        ThreadUtil.startAndJoin(thread);
    }

    public static void setDefaultSignature(final Signature signature) {
        setDefaultSignature(signature.getName());
        signature.setDefault(true);
        Log.d("setDefaultSignature", signature.getName() + "   is default now");
    }

    public static int deleteSignature(Signature signature, boolean deleteFile) {

        if (deleteFile) {
            FileUtil.deleteSignatureFile(signature.getName());
        }

        final DBThread.DeleteSignThread thread = new DBThread.DeleteSignThread(signature.getName());

        ThreadUtil.startAndJoin(thread);


        signature = null;
        return thread.getResult();
    }


    public static boolean noSigns() {

        return !CheckUtil.checkSign(getLatestSign());
    }


    public static void openGalleryToImport(final Activity activity) {
        final Intent i = new Intent(activity, PickerActivity.class);
        activity.startActivityForResult(i, PickerActivity.PICK_REQUEST);
    }
}
