package net.whitedesert.photosign.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import net.whitedesert.photosign.R;

/**
 * Created by yazeed44 on 9/12/14.
 */
public final class ViewUtil {

    public static Activity activity;

    private ViewUtil() {
        throw new AssertionError();
    }


    public static synchronized void initInstance(Activity pActivity) {

        activity = pActivity;


    }


    public static Point getDisplay(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    public static EditText createEditTextForAskingName(final String text, final Activity activity) {
        final EditText editText = new EditText(new ContextThemeWrapper(activity, R.style.text_edit));
        editText.setText(text);
        editText.setTextAppearance(activity, R.style.text_edit);

        editText.addTextChangedListener(new TextWatcher() {
            private void checkText(CharSequence s) {
                if (s.length() == 0) {
                    editText.setError(activity.getResources().getString(R.string.error_name_empty));
                } else if (SignatureUtil.isDuplicatedSign(s.toString())) {
                    editText.setError(activity.getResources().getString(R.string.error_name_repeated));
                } else {
                    //If there's nothing wrong

                    editText.setError(null);

                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                checkText(s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkText(s);
            }
        });

        return editText;
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

    public static void toastSavedSignSuccess() {
        toastShort(R.string.saved_sign_success);
    }

    public static PopupMenu createMenu(View anchor, int menuId) {
        final ContextThemeWrapper themeWrapper = new ContextThemeWrapper(anchor.getContext(), R.style.PopupMenu);

        final PopupMenu menu = new PopupMenu(themeWrapper, anchor);

        menu.getMenuInflater().inflate(menuId, menu.getMenu());

        return menu;

    }
}
