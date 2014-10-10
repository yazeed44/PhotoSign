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
import net.whitedesert.photosign.utils.Signature;
import net.whitedesert.photosign.utils.SignatureUtil;
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


        final Signature lastSignature = SignatureUtil.getLatestSign();


        signingView.setSignature(lastSignature);
        signingView.setPhoto(photo);

        setUpOpacity(lastSignature);


    }

    private void setUpOpacity(final Signature signature) {
        opacitySeek.setMax(getResources().getInteger(R.integer.opacity_max));
        opacitySeek.setProgress(opacitySeek.getMax());
        final String opacityString = this.getString(R.string.opacity_text) + " : ";

        opacityText.setText(opacityString + opacitySeek.getProgress());

        opacitySeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                final Bitmap updatedSign = BitmapUtil.getUpdatedOpacity(signature.getBitmap(signingView.getSignWidthHeight()), progress);
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
        SaveUtil.doneSigningPhoto(signingView, this);
    }
}
