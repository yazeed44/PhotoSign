package net.whitedesert.photosign.utils;

import android.app.Activity;
import android.widget.Toast;

import net.whitedesert.photosign.R;

/**
 * Created by yazeed44 on 8/22/14.
 * Class for showing toast messages
 */
public final class ToastUtil {
    public static Activity activity;

    private ToastUtil() {
        throw new AssertionError();
    }

    public static synchronized void initializeInstance(Activity pActivity) {

        activity = pActivity;

    }


    public static void toastShort(final String message) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static void toastShort(final int resId) {
        toastShort(activity.getResources().getString(resId));
    }

    public static void toastLong(final String message) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void toastLong(final int resId) {
        toastLong(activity.getResources().getString(resId));
    }

    public static void toastUnsupported() {
        toastShort(R.string.unsupported);
    }

    public static void toastSavedSignSuccess() {
        toastShort(R.string.saved_sign_success);
    }

    public static void toastWaitPlease() {
        toastShort(R.string.wait_please_title);
    }

}
