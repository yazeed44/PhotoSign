package net.whitedesert.photosign.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.utils.BitmapUtil;
import net.whitedesert.photosign.utils.SaveUtil;
import net.whitedesert.photosign.utils.Signature;
import net.whitedesert.photosign.utils.SignatureUtil;
import net.whitedesert.photosign.utils.ToastUtil;
import net.whitedesert.photosign.views.SignatureView;
import net.whitedesert.photosign.views.SigningView;

/**
 * Created by yazeed44 on 8/10/14.
 */
public class SigningActivity extends AdActivity {

    private SigningView signingView;
    private SeekBar opacitySeek;
    private TextView opacityText;
    private SignatureView signatureView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_signing);
        signingView = (SigningView) this.findViewById(R.id.signingView);
        opacitySeek = (SeekBar) this.findViewById(R.id.opacity_seek);
        opacityText = (TextView) this.findViewById(R.id.opacity_text);
        signatureView = (SignatureView) findViewById(R.id.signatureView);
        final Intent i = this.getIntent();

        final String photoPath = i.getStringExtra(Types.PATH_TYPE);


        final Bitmap photo = BitmapUtil.decodeFile(photoPath);


        final Signature lastSignature = SignatureUtil.getLatestSign();

        signatureView.setSignature(lastSignature);

        ToastUtil.toastShort(R.string.default_sign_is_used_toast);


        signingView.setSignatureView(signatureView);
        signingView.setPhoto(photo);


        setUpOpacity(lastSignature);


    }

    private void setUpOpacity(final Signature signature) {
        opacitySeek.setMax(getResources().getInteger(R.integer.opacity_max));
        opacitySeek.setProgress(opacitySeek.getMax());
        final String opacityString = getString(R.string.opacity_text) + " : ";

        opacityText.setText(opacityString + opacitySeek.getProgress());

        opacitySeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                signatureView.setImageAlpha(progress); // Set opacity
                opacityText.setText(opacityString + progress);
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

    public void onClickDone() {
        SaveUtil.doneSigningPhoto(signingView, this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_done, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_done:
                onClickDone();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
