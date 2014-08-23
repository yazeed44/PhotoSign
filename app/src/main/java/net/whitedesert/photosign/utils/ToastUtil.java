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

    public static void showToastShort(final String message, final Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static void showToastShort(final int resId, final Activity activity) {
        showToastShort(activity.getResources().getString(resId), activity);
    }

    public static void showToastLong(final String message, final Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void showToastLong(final int resId, final Activity activity) {
        showToastLong(activity.getResources().getString(resId), activity);
    }

    public static void toastUnsupported(final Activity activity) {
        showToastShort(R.string.unsupported, activity);
    }
}
