package net.whitedesert.photosign.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.utils.BitmapUtil;
import net.whitedesert.photosign.utils.SaveUtil;
import net.whitedesert.photosign.utils.Sign;
import net.whitedesert.photosign.utils.SignUtil;
import net.whitedesert.photosign.views.SigningView;

/**
 * Created by yazeed44 on 8/10/14.
 */
public class SigningActivity extends AdActivity {

    private SigningView signingView;
    private SeekBar opacitySeek;
    private TextView opacityText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_signing);
        signingView = (SigningView) this.findViewById(R.id.signingView);
        opacitySeek = (SeekBar) this.findViewById(R.id.opacity_seek);
        opacityText = (TextView) this.findViewById(R.id.opacity_text);

        final Intent i = this.getIntent();

        final String photoPath = i.getStringExtra(Types.PATH_TYPE);


        final Bitmap photo = BitmapUtil.decodeFile(photoPath);


        final Sign lastSign = SignUtil.getLatestSign();


        signingView.setSign(lastSign);
        signingView.setPhoto(photo);

        setUpOpacity(lastSign);


    }

    private void setUpOpacity(final Sign sign) {
        opacitySeek.setMax(getResources().getInteger(R.integer.opacity_max));
        opacitySeek.setProgress(opacitySeek.getMax());
        final String opacityString = this.getString(R.string.opacity_text) + " : ";

        opacityText.setText(opacityString + opacitySeek.getProgress());

        opacitySeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                final Bitmap updatedSign = BitmapUtil.getUpdatedOpacity(sign.getBitmap(signingView.getSignWidthHeight()), progress);
                opacityText.setText(opacityString + progress);
                signingView.setSign(updatedSign);
                signingView.invalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public void onClickDone(View view) {
        SaveUtil.saveSignedPhoto(signingView, this);
    }
}
