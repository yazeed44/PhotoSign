package net.whitedesert.photosign.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import net.whitedesert.photosign.R;

import java.io.File;

/**
 * Created by yazeed44 on 8/16/14.
 * Class for checking objects , if they have something wrong it will return false (Method) also it will shows Error dialog
 */
public final class CheckUtil {

    private CheckUtil() {
        throw new AssertionError();
    }


    public static boolean checkSign(final long id, boolean toast) {
        if (id != -1) {
            Log.i("CheckSign", "it has successfully added the sign");
            if (toast)
                ViewUtil.toastSavedSignSuccess();

            return true;
        } else {
            Log.e("CheckSign", "Failed to add sign");

            return false;
        }
    }

    public static boolean checkSigns(final long[] ids, boolean toast) {
        boolean noError = true;

        for (long id : ids) {
            if (id == -1) {
                noError = false;
                Log.e("checkSigns", "Failed to add sign ");

                if (toast)
                    ViewUtil.toastLong(R.string.error_save_sign);
            }
        }

        return noError;
    }

    public static boolean checkSign(final Signature signature) {
        assert (signature != null);

        if (!checkPath(signature.getPath())) {
            Log.e("CheckSign", "Path is wrong");
            SignatureUtil.deleteSignature(signature, true);
            return false;
        }


        return true;
    }

    public static boolean checkPath(final String path) {
        boolean exists;

        if (!checkString(path)) {
            return false;
        }

        try {
            exists = new File(path).exists();
        } catch (NullPointerException ex) {
            Log.e("checkPath", path + "     Dosen't exist");
            exists = false;
        }

        return exists;
    }

    public static boolean checkString(String s) {

        boolean isEmpty;


        try {
            isEmpty = s.isEmpty();
        } catch (NullPointerException ex) {
            Log.e("checkString", "String is NULL !!");
            return false;
        }

        return !isEmpty;
    }

    //Detrmine if app is opened for the first time
    public static boolean isFirstTimeOpened(final Activity activity) {

        final String PREFS_NAME = "MyPrefsFile";

        SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);

        final String firstTimeKey = "myFirstTime";

        final boolean firstTime = settings.getBoolean(firstTimeKey, true);

        settings.edit().putBoolean(firstTimeKey, false).apply();

        return firstTime;

    }

}
