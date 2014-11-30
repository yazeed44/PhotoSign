package net.whitedesert.photosign.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;

/**
 * Created by yazeed44 on 8/31/14.
 * Class for dealing with threads
 */
public final class ThreadUtil {

    private static Activity mActivity;

    private ThreadUtil() {
        throw new AssertionError();
    }

    public static void initInstance(final Activity activity) {
        mActivity = activity;
    }

    public static void join(Thread t) {
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e("Thread Util : Join", e.getMessage());
        }
    }

    public static void showDialog(final AlertDialog.Builder dialog) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        });
    }

    public static void showDialog(final AlertDialog dialog) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        });
    }

    public static void dismissDialog(final AlertDialog dialog) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        });
    }


    public static void startAndJoin(Thread t) {
        t.start();
        join(t);
    }


}
