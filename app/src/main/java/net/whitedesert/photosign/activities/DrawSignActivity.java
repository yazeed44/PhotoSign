package net.whitedesert.photosign.activities;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.utils.SaveUtil;
import net.whitedesert.photosign.utils.ToastUtil;
import net.whitedesert.photosign.views.DrawSignView;

/**
 * Created by yazeed44 on 8/15/14.
 */
public class DrawSignActivity extends AdActivity {

    private DrawSignView draw;

    @Override
    public void onCreate(Bundle savedInstance) {

        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign_drawing);
        draw = (DrawSignView) this.findViewById(R.id.drawView);


    }


    public void onClickTextCustomize(View view) {
        ToastUtil.toastUnsupported();
        //TODO
    }

    public void onClickReset(View view) {
        draw.reset();
    }

    public void onClickDone(View view) {
        SaveUtil.askNameAndAddSign(draw, this);

    }


}
