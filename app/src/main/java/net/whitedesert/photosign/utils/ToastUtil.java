package net.whitedesert.photosign.utils;

import android.app.Activity;
import android.widget.Toast;

import net.whitedesert.photosign.R;

/**
 * Created by yazeed44 on 8/22/14.
 */
public final class ToastUtil {
    private ToastUtil() {
        throw new AssertionError();
    }

    public static void toastShort(final String message, final Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static void toastShort(final int resId, final Activity activity) {
        toastShort(activity.getResources().getString(resId), activity);
    }

    public static void toastLong(final String message, final Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void toastLong(final int resId, final Activity activity) {
        toastLong(activity.getResources().getString(resId), activity);
    }

    public static void toastUnsupported(final Activity activity) {
        toastShort(R.string.unsupported, activity);
    }

    public static void toastSavedSignSuccess(final Activity activity) {
        toastShort(R.string.saved_sign_success, activity);
    }

}
