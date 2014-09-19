package net.whitedesert.photosign.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.EditText;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.threads.BitmapThread;
import net.whitedesert.photosign.threads.SigningThread;
import net.whitedesert.photosign.views.SigningView;

import java.io.File;

/**
 * Created by yazeed44 on 8/16/14.
 * Class for save and add
 */
public final class SaveUtil {

    public static final String SIGNS_DIR = "/signs";
    public static final String SIGNED_PHOTO_DIR = "/signed_photos";

    private SaveUtil() {
        throw new AssertionError();
    }

    public static void askNameAndAddSign(final String picPath, final Activity activity) {

        final File pic = new File(picPath);
        final EditText nameInput = ViewUtil.getEditText(pic.getName(), activity);
        nameInput.setText(pic.getName());
        final DialogInterface.OnClickListener posListener = SetListenUtil.getPosListenerForName(BitmapUtil.decodeFile(picPath), null, nameInput, activity);
        DialogUtil.getInputDialog(R.string.save_title, R.string.save_message, posListener, nameInput, activity).show();

    }

    public static void askNameAndAddSign(final Bitmap sign, final Activity activity) {
        // Don't waste your time on this
        final EditText nameInput = ViewUtil.getEditText(RandomUtil.getRandomInt(SIGNED_PHOTO_DIR.length() + SIGNS_DIR.length()), activity);
        final DialogInterface.OnClickListener posListener = SetListenUtil.getPosListenerForName(sign, null, nameInput, activity);
        DialogUtil.getInputDialog(R.string.save_title, R.string.save_message, posListener, nameInput, activity).show();
    }

    public static void askNameAndAddSign(final View drawView, final Activity activity) {
        final EditText nameInput = ViewUtil.getEditText(RandomUtil.getRandomInt(drawView.getWidth()), activity);

        final DialogInterface.OnClickListener posListener = SetListenUtil.getPosListenerForName(null, drawView, nameInput, activity);

        DialogUtil.getInputDialog(R.string.save_title, R.string.save_message, posListener, nameInput, activity).show();
    }


    public static void saveSignedPhoto(final SigningView signingView, final Activity activity) {
        final Sign sign = signingView.getSign();
        final Bitmap photo = signingView.getPhoto(), signBitmap = signingView.getSignBitmap();
        final XY.Float xy = signingView.getXY();
        final SigningThread signThread = new SigningThread(photo, signBitmap, sign.getName(), activity, xy, signingView.getOrgPhotoDimen());
        signThread.start();

    }

    public static String savePicFromBitmap(Bitmap finalBitmap, Activity activity, String dir, String name, boolean toast) {
        final BitmapThread.SaveBitmapThread thread = new BitmapThread.SaveBitmapThread(finalBitmap, activity, dir, name, toast);
        ThreadUtil.startAndJoin(thread);
        return thread.getPath();
    }

    public static String savePicFromView(View drawView, Activity activity, String dir, String name, boolean toast) {

        drawView.setDrawingCacheEnabled(true);
        final String path = savePicFromBitmap(drawView.getDrawingCache(true), activity, dir, name, toast);
        drawView.setDrawingCacheEnabled(false);
        return path;
    }


}
