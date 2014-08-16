package net.whitedesert.photosign.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.utils.DialogUtil;
import net.whitedesert.photosign.utils.PhotoUtil;
import net.whitedesert.photosign.utils.Sign;
import net.whitedesert.photosign.utils.SignUtil;
import net.whitedesert.photosign.views.DrawSignView;

/**
 * Created by yazeed44 on 8/15/14.
 */
public class DrawSignActivity extends Activity {

    private DrawSignView draw;
    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign_drawing);
        draw = (DrawSignView)this.findViewById(R.id.drawView);
        String signDrawReminder = getResources().getString(R.string.sign_draw_reminder);

        Toast.makeText(this,signDrawReminder,Toast.LENGTH_SHORT).show();


    }


    public void onClickTextCustomize(View view){

    }

    public void onClickReset(View view){
        draw.reset();
    }

    public void onClickSave(View view){

             final  AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle(R.string.save_title);
        dialog.setMessage(R.string.save_message);

        final EditText nameInput = new EditText(this);
        dialog.setView(nameInput);
        dialog.setPositiveButton(R.string.ok,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              String name = nameInput.getText().toString();

                if (name.length() == 0){
                    DialogUtil.createErrorDialog(R.string.error_name_empty,DrawSignActivity.this).show();

                }

              String path =   PhotoUtil.savePicFromView(draw, DrawSignActivity.this, PhotoUtil.SIGNS_DIR);
              Sign sign = new Sign();
                sign.setName(name);
                sign.setPath(path);
                Log.i("DrawSignActivity : onClickSave","sign name : " + sign.getName() + " ,  sign Path  :  " + sign.getPath());
                SignUtil.addSign(sign,DrawSignActivity.this);
            }
        });

        dialog.setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
           dialogInterface.dismiss();
            }
        });

        dialog.show();

    }
  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.draw_sign_menu, menu);
        return true;
    }*/
}
