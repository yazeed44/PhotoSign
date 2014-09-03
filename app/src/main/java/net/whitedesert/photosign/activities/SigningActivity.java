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
import net.whitedesert.photosign.utils.SetListenUtil;
import net.whitedesert.photosign.utils.Sign;
import net.whitedesert.photosign.utils.SignUtil;
import net.whitedesert.photosign.views.SigningView;

/**
 * Created by yazeed44 on 8/10/14.
 */
public class SigningActivity extends AdActivity {

    SigningView signingView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_signing);
        signingView = (SigningView) this.findViewById(R.id.signingView);
        final SeekBar opacitySeek = (SeekBar) this.findViewById(R.id.opacity_seek);
        final TextView opacityText = (TextView) this.findViewById(R.id.opacity_text);
        opacitySeek.setMax(255);
        opacitySeek.setProgress(opacitySeek.getMax());
        opacityText.setText(this.getString(R.string.opacity_text) + " : " + opacitySeek.getMax());
        final Intent i = this.getIntent();

        final String path = i.getStringExtra("path");


        final Bitmap photo = BitmapUtil.decodeFile(path);


        final Sign sign = SignUtil.getLatestSign(this);


        signingView.setSign(sign);
        signingView.setPhoto(photo);

        SetListenUtil.setUpOpacitySeek(opacitySeek, opacityText, signingView, sign);

    }

    public void onClickDoneSigning(View view) {
        SaveUtil.saveSignedPhoto(signingView, this);
    }
}
