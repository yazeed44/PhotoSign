package net.whitedesert.photosign.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.utils.AskUtil;
import net.whitedesert.photosign.utils.Sign;
import net.whitedesert.photosign.utils.SignUtil;
import net.whitedesert.photosign.utils.SigningUtil;


public class MainActivity extends AdActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AskUtil.getWannaSignDialog(this).show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public void onClickChoose(View view){
        SigningUtil.openGalleryToSignSingle(this);

            for(Sign sign : SignUtil.getSigns(this)){
                Log.i("Main Activity : ", sign.getName());
            }


    }

    public void onClickCreateSign(View view) {
        AskUtil.selectMethodSign(this);
    }

}
