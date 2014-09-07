package net.whitedesert.photosign.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;

/**
 * Created by yazeed44 on 8/31/14.
 */
public final class ThreadUtil {

    private ThreadUtil() {
        throw new AssertionError();
    }

    public static void join(Thread t) {
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e("Thread Util : Join", e.getMessage());
        }
    }

    public static void showDialog(final AlertDialog.Builder dialog, final Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        });
    }
}
