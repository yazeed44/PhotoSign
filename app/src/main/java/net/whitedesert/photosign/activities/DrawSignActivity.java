package net.whitedesert.photosign.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import net.whitedesert.photosign.R;

/**
 * Created by yazeed44 on 8/15/14.
 */
public class DrawSignActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign_drawing);
        String signDrawReminder = getResources().getString(R.string.sign_draw_reminder);

        Toast.makeText(this,signDrawReminder,Toast.LENGTH_SHORT).show();


    }


    public void onClickTextCustomize(View view){

    }

    public void onClickReset(View view){

    }

    public void onClickSave(View view){

    }
  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.draw_sign_menu, menu);
        return true;
    }*/
}
