package net.whitedesert.photosign.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.WindowCompat;
import android.support.v7.app.ActionBarActivity;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.utils.SaveUtil;
import net.yazeed44.imagepicker.PickerActivity;

/**
 * Created by yazeed44 on 8/22/14.
 */
public class BaseActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!toolbarUsed()) {
            supportRequestWindowFeature(WindowCompat.FEATURE_ACTION_BAR);
        }

        if (getSupportParentActivityIntent() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //When user import external images to use as signatures
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PickerActivity.PICK_REQUEST) {
            final String[] paths = data.getStringArrayExtra(PickerActivity.PICKED_IMAGES_KEY);
            SaveUtil.saveSignatures(paths, this);


        }
    }

    public boolean toolbarUsed() {
        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
