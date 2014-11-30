package net.whitedesert.photosign.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import net.whitedesert.photosign.R;

/**
 * Created by yazeed44 on 8/22/14.
 */
public class AdActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getParentActivityIntent() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle(R.string.app_name);

    }


}
