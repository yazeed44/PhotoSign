package net.whitedesert.photosign.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.widget.PopupMenu;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.utils.StorageUtils;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.ui.DrawSignActivity;
import net.whitedesert.photosign.ui.TypeSignatureActivity;

import java.io.File;

/**
 * Created by yazeed44 on 9/12/14.
 */
public final class ViewUtil {

    public static final MaterialDialog.SimpleCallback DISMISS_CALLBACK = new MaterialDialog.FullCallback() {
        @Override
        public void onNeutral(MaterialDialog materialDialog) {


        }

        @Override
        public void onPositive(MaterialDialog materialDialog) {

        }

        @Override
        public void onNegative(MaterialDialog materialDialog) {
            materialDialog.dismiss();
        }
    };
    public static final String GLOBAL_PATH = "file://";
    private static Activity mActivity;
    private static MaterialDialog.Builder mDialog; //Used for optimzation
    private static Resources mResources;
    private static Bitmap mBitmap;

    private ViewUtil() {
        throw new AssertionError();
    }


    public static synchronized void initInstance(Activity pActivity) {

        mActivity = pActivity;


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


    public static void toastShort(final String message) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static void toastShort(final int resId) {
        toastShort(mActivity.getResources().getString(resId));
    }

    public static void toastLong(final String message) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void toastLong(final int resId) {
        toastLong(mActivity.getResources().getString(resId));
    }

    public static void toastSavedSignSuccess() {
        toastShort(R.string.saved_signatures_success);
    }

    public static PopupMenu createMenu(View anchor, int menuId) {


        final ContextThemeWrapper themeWrapper = new ContextThemeWrapper(anchor.getContext(), R.style.PopupMenu);

        final PopupMenu menu = new PopupMenu(themeWrapper, anchor);

        menu.getMenuInflater().inflate(menuId, menu.getMenu());

        return menu;

    }

    public static MaterialDialog.Builder createDialog(String title, String msg, final Activity activity) {


        mDialog = new MaterialDialog.Builder(activity)
                .title(title)
                .content(msg)
                .theme(Theme.LIGHT)
        ;


        return mDialog;
    }

    public static MaterialDialog.Builder createDialog(int titleId, int msgId, final Activity activity) {
        mResources = activity.getResources();
        return createDialog(mResources.getString(titleId), mResources.getString(msgId), activity);
    }

    public static MaterialDialog.Builder createErrorDialog(String msg, final Activity activity) {

        final String title = mResources.getString(R.string.error_title);
        return createDialog(title, msg, activity);


    }

    public static MaterialDialog.Builder createErrorDialog(int msgId, final Activity activity) {
        mResources = activity.getResources();
        return createErrorDialog(mResources.getString(msgId), activity);

    }

    public static MaterialDialog.Builder createSingleChooseDialog(String title, String[] choices, final Activity activity) {
        MaterialDialog.Builder dialog = createDialog(title, null, activity);
        //the choices won't appear if you set a message !!

        dialog.items(choices)
                .negativeText(android.R.string.cancel)
                .callback(DISMISS_CALLBACK);


        return dialog;
    }

    public static MaterialDialog.Builder createSingleChooseDialog(int titleId, int choicesId, final Activity activity) {

        mResources = activity.getResources();
        String title = mResources.getString(titleId);
        String[] choices = mResources.getStringArray(choicesId);
        return createSingleChooseDialog(title, choices, activity);
    }

    //Ask the user to make another signature or sign a photo
    public static MaterialDialog.Builder createWannaSignDialog(final Activity activity) {
        final Resources r = activity.getResources();
        final String[] choices = r.getStringArray(R.array.wanna_sign_choices);
        final String makeSign = choices[0];
        final String signPhoto = choices[1];

        final MaterialDialog.ListCallback listener = new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog materialDialog, View view, int which, CharSequence text) {
                if (text.equals(makeSign)) {
                    createChooseMethodToSignDialog(activity).show();
                } else if (text.equals(signPhoto)) {
                    SigningUtil.showGalleryToSign(activity);
                }
            }
        };

        final String title = r.getString(R.string.wanna_sign_title);

        return createSingleChooseDialog(null, choices, activity)
                .itemsCallbackSingleChoice(-1, listener);


    }

    //the user choose how to sign
    public static MaterialDialog.Builder createChooseMethodToSignDialog(final Activity activity) {
        final Resources res = activity.getResources();
        final String[] choices = res.getStringArray(R.array.select_method_signature_choices);

        //Methods to insert a signature
        final String draw = choices[0];
        final String external = choices[1];
        final String text = choices[2];


        final MaterialDialog.ListCallback listener = new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog materialDialog, View view, int which, CharSequence chosenMethod) {

                if (chosenMethod.equals(draw)) {
                    Intent i = new Intent(activity, DrawSignActivity.class);
                    activity.startActivity(i);
                } else if (chosenMethod.equals(text)) {
                    Intent i = new Intent(activity, TypeSignatureActivity.class);
                    activity.startActivity(i);
                } else if (chosenMethod.equals(external)) {
                    SignatureUtil.openGalleryToImport(activity);
                }
            }
        };

        return createSingleChooseDialog(res.getString(R.string.sign_method_title), choices, activity)
                .itemsCallbackSingleChoice(-1, listener)
                .positiveText(R.string.choose_btn)
                .negativeText(android.R.string.cancel);


    }

    public static Bitmap decodeFile(String path) throws NullPointerException {
        mBitmap = ImageLoader.getInstance().loadImageSync(GLOBAL_PATH + path);
        return mBitmap;
    }

    public static Bitmap decodeFile(String path, int width, int height) {

        mBitmap = ImageLoader.getInstance().loadImageSync(GLOBAL_PATH + path, new ImageSize(width, height));
        return Bitmap.createScaledBitmap(mBitmap, width, height, true);
    }

    public static void initImageLoader(final Activity activity) {
        try {
            String CACHE_DIR = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/.temp_tmp";
            new File(CACHE_DIR).mkdirs();

            File cacheDir = StorageUtils.getOwnCacheDirectory(activity.getBaseContext(),
                    CACHE_DIR);

            DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                    .cacheOnDisk(true).imageScaleType(ImageScaleType.EXACTLY)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .showImageOnFail(R.drawable.ic_error)
                    .resetViewBeforeLoading(true)
                    .build();


            ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                    activity.getBaseContext())
                    .defaultDisplayImageOptions(displayImageOptions)
                    .diskCache(new UnlimitedDiscCache(cacheDir))
                    .memoryCache(new WeakMemoryCache());


            final ImageLoaderConfiguration config = builder.build();
            ImageLoader.getInstance().init(config);

        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage());
        }
    }
}
