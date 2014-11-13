package net.whitedesert.photosign.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.database.MyDBHelper;
import net.whitedesert.photosign.database.SignsDB;
import net.whitedesert.photosign.utils.AskUtil;
import net.whitedesert.photosign.utils.SigningUtil;
import net.whitedesert.photosign.utils.ThreadUtil;
import net.whitedesert.photosign.utils.ToastUtil;


public class MainActivity extends AdActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUtils();


    }

    private void initUtils() {
        SignsDB.initializeInstance(new MyDBHelper(getApplicationContext()));
        ToastUtil.initializeInstance(this);
        ThreadUtil.initalizeInstance(this);
    }


    public void onClickChoose(View view) {


        SigningUtil.openGalleryToSignSingle(this);


    }

    public void onClickMySignatures(View view) {
        AskUtil.selectMethodSign(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_settings:
                openSettings();
                return true;

            case R.id.menu_about:
                openAbout();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openAbout() {
        //TODO
    }

    private void openSettings() {
        //TODO

        final Intent signatureIntent = new Intent(this, SignaturesActivity.class);
        startActivity(signatureIntent);
    }

}
