package net.whitedesert.photosign.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.utils.BlendUtil;
import net.whitedesert.photosign.utils.SignRaw;
import net.whitedesert.photosign.views.BlendView;

/**
 * Created by yazeed44 on 8/10/14.
 */
public class BlendActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_blend);
        final BlendView blendView = (BlendView)this.findViewById(R.id.blendView);
        Intent i = this.getIntent();

        String path = i.getStringExtra("path");



        final Bitmap photo = BitmapFactory.decodeFile(path);


        blendView.setPhoto(photo);
        SignRaw signRaw = new SignRaw();
        signRaw.setStyle(Typeface.BOLD);
        signRaw.setTextSize(40);
        signRaw.setText("Test - Yo nigga");
        Rect rect = new Rect();

        signRaw.getPaint().getTextBounds(signRaw.getText(),0,signRaw.getText().length(),rect);
        signRaw.setHeight(rect.height()+15);
        signRaw.setWidth(rect.width()+15);
        blendView.setSignRaw(signRaw);

       final Bitmap sign = BlendUtil.createSign(signRaw);
        blendView.setSign(sign);
        blendView.setImageBitmap(photo);






    }
}
