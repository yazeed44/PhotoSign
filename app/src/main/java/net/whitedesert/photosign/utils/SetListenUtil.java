package net.whitedesert.photosign.utils;

import android.graphics.Bitmap;
import android.widget.SeekBar;
import android.widget.TextView;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.views.SigningView;

/**
 * Created by yazeed44 on 8/31/14.
 */
public final class SetListenUtil {
    private SetListenUtil() {
        throw new AssertionError();
    }

    /**
     * @param opacitySeek , the seek bar that used in opacity
     * @param sign        the signature
     * @param opacityText the text view that will get updated too
     */
    public static void setUpOpacitySeek(final SeekBar opacitySeek, final TextView opacityText, final SigningView signView, final Sign sign) {
        opacitySeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Bitmap updatedSign = BitmapUtil.updateOpacity(sign.getBitmap(), progress);
                opacityText.setText(opacityText.getResources().getString(R.string.opacity_text) + " : " + progress);
                signView.setSign(updatedSign);
                signView.invalidate();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
