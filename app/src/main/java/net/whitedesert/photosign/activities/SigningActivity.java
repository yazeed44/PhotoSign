package net.whitedesert.photosign.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.utils.SignUtil;
import net.whitedesert.photosign.views.SigningView;

/**
 * Created by yazeed44 on 8/10/14.
 */
public class SigningActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_blend);
        final SigningView signingView = (SigningView) this.findViewById(R.id.blendView);
        final Intent i = this.getIntent();

        final String path = i.getStringExtra("path");


        final Bitmap photo = BitmapFactory.decodeFile(path);


        final Bitmap sign = SignUtil.getSign("test", this).getBitmap();
        Log.i("Signing activity : Sign : ", "Width =  " + sign.getWidth() + "   ,  Height  =  " + sign.getHeight());
        signingView.setSign(sign);
        signingView.setPhoto(photo);


    }
}
