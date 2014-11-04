package net.whitedesert.photosign.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.database.MyDBHelper;
import net.whitedesert.photosign.database.SignsDB;
import net.whitedesert.photosign.utils.AskUtil;
import net.whitedesert.photosign.utils.BitmapUtil;
import net.whitedesert.photosign.utils.FileUtil;
import net.whitedesert.photosign.utils.SigningUtil;
import net.whitedesert.photosign.utils.ThreadUtil;
import net.whitedesert.photosign.utils.ToastUtil;


public class MainActivity extends AdActivity {

    private ImageView lastPhotoView;

    private String lastImagePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUtils();
        lastPhotoView = (ImageView) this.findViewById(R.id.lastImage);

    }

    private void initUtils() {
        SignsDB.initializeInstance(new MyDBHelper(getApplicationContext()));
        ToastUtil.initializeInstance(this);
        ThreadUtil.initalizeInstance(this);
    }

    @Override
    protected void onResume() {
        final Runnable setLastPhoto = new Runnable() {
            @Override
            public void run() {
                lastImagePath = FileUtil.getLatestPhoto(MainActivity.this);
                lastPhotoView.setImageBitmap(BitmapUtil.decodeFile(lastImagePath, (int) (lastPhotoView.getWidth() / 1.5), (int) (lastPhotoView.getHeight() / 1.5)));
            }
        };

        super.onResume();
        lastPhotoView.post(setLastPhoto);
    }


    public void onClickChoose(View view) {


        SigningUtil.openGalleryToSignSingle(this);


    }

    public void onClickCreateSign(View view) {
        AskUtil.selectMethodSign(this);
    }

    public void onClickLastImage(View view) {
        SigningUtil.signSingle(lastImagePath, this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_settings:
                openSettings();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openSettings() {
        //TODO
    }

}
