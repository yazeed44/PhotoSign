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
    public static Toast toast;

    private ToastUtil() {
        throw new AssertionError();
    }

    public static synchronized void initializeInstance(Activity pActivity) {

        activity = pActivity;
        toast = new Toast(activity);

    }


    public static void toastShort(final String message) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                toast.setText(message);
                toast.show();
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
                toast.setText(message);
                toast.show();
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
