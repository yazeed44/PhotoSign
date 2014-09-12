package net.whitedesert.photosign.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.database.MyDBHelper;
import net.whitedesert.photosign.database.SignsDB;
import net.whitedesert.photosign.utils.AskUtil;
import net.whitedesert.photosign.utils.SigningUtil;
import net.whitedesert.photosign.utils.ToastUtil;


public class MainActivity extends AdActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SignsDB.initializeInstance(new MyDBHelper(getApplicationContext()));
        ToastUtil.initializeInstance(this);

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


    public void onClickChoose(View view) {


        SigningUtil.openGalleryToSignSingle(this);


    }

    public void onClickCreateSign(View view) {
        AskUtil.selectMethodSign(this);
    }

}
