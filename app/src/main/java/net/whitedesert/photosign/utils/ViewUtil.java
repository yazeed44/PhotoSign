package net.whitedesert.photosign.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Point;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.WindowManager;
import android.widget.EditText;

import net.whitedesert.photosign.R;

/**
 * Created by yazeed44 on 9/12/14.
 */
public final class ViewUtil {

    /**
     * Factor applied to session color to derive the background color on panels and when
     * a session photo could not be downloaded (or while it is being downloaded)
     */
    public static final float SIGN_BG_COLOR_SCALE_FACTOR = 0.75f;
    private static final float SIGN_PHOTO_SCRIM_ALPHA = 0.25f; // 0=invisible, 1=visible image
    private static final float SIGN_PHOTO_SCRIM_SATURATION = 0.2f; // 0=gray, 1=color image

    private ViewUtil() {
        throw new AssertionError();
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

    public static EditText getEditTextForAskingName(final String text, final Activity activity) {
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

    public static int scaleColor(int color, float factor, boolean scaleAlpha) {
        return Color.argb(scaleAlpha ? (Math.round(Color.alpha(color) * factor)) : Color.alpha(color),
                Math.round(Color.red(color) * factor), Math.round(Color.green(color) * factor),
                Math.round(Color.blue(color) * factor));
    }

    public static int scaleSessionColorToDefaultBG(int color) {
        return scaleColor(color, SIGN_BG_COLOR_SCALE_FACTOR, false);
    }

    // Desaturates and color-scrims the image
    public static ColorFilter makeSessionImageScrimColorFilter(int signColor) {
        float a = SIGN_PHOTO_SCRIM_ALPHA;
        float sat = SIGN_PHOTO_SCRIM_SATURATION; // saturation (0=gray, 1=color)
        return new ColorMatrixColorFilter(new float[]{
                ((1 - 0.213f) * sat + 0.213f) * a, ((0 - 0.715f) * sat + 0.715f) * a, ((0 - 0.072f) * sat + 0.072f) * a, 0, Color.red(signColor) * (1 - a),
                ((0 - 0.213f) * sat + 0.213f) * a, ((1 - 0.715f) * sat + 0.715f) * a, ((0 - 0.072f) * sat + 0.072f) * a, 0, Color.green(signColor) * (1 - a),
                ((0 - 0.213f) * sat + 0.213f) * a, ((0 - 0.715f) * sat + 0.715f) * a, ((1 - 0.072f) * sat + 0.072f) * a, 0, Color.blue(signColor) * (1 - a),
                0, 0, 0, 0, 255
        });

    }

    public static int[] getColorsForSigns(final Resources r) {
        return new int[]{r.getColor(R.color.sign_blue), r.getColor(R.color.sign_orange),
                r.getColor(R.color.sign_purple), r.getColor(R.color.sign_red)
                , r.getColor(R.color.sign_tyan)};
    }


}
