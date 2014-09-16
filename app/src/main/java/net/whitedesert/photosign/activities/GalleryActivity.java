package net.whitedesert.photosign.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.luminous.pick.Action;
import com.luminous.pick.CustomGallery;

import net.whitedesert.photosign.utils.SaveUtil;

import java.util.ArrayList;

/**
 * Created by yazeed44 on 8/8/14.
 */
public class GalleryActivity extends com.luminous.pick.MainActivity {

    private final ArrayList<String> paths = new ArrayList<String>();
    private String path;
    private String type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = this.getIntent();
        String type = i.getStringExtra(Types.TYPE);
        setType(type);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            adapter.clear();

            viewSwitcher.setDisplayedChild(1);
            String single_path = data.getStringExtra("single_path");
            imageLoader.displayImage("file://" + single_path, imgSinglePick);
            this.path = single_path;
            if (type.equals(Types.OPEN_GALLERY_SINGLE_SIGNING_TYPE)) {
                openSignSingle();
            } else if (type.equals(Types.OPEN_GALLERY_SINGLE_CHOOSE_TYPE)) {
                SaveUtil.askNameAndAddSign(path, this);
            }
            Log.i("Gallery Activity : ", "Got picture  " + path);

        } else if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            //isn't complete yet
            String[] all_path = data.getStringArrayExtra("all_path");

            ArrayList<CustomGallery> dataT = new ArrayList<CustomGallery>();

            for (String string : all_path) {
                CustomGallery item = new CustomGallery();
                item.sdcardPath = string;
                paths.add(item.sdcardPath);
                dataT.add(item);
            }


            viewSwitcher.setDisplayedChild(0);
            adapter.addAll(dataT);


        }
    }

    public String openGallerySingle() {

        // For single image
        Intent i = new Intent(Action.ACTION_PICK);
        startActivityForResult(i, 100);
        return path != null ? path : null;
    }

    public void openGalleryMulti() {

        // For multiple images
        Intent intent = new Intent(Action.ACTION_MULTIPLE_PICK);
        startActivityForResult(intent, 200);

    }


    private void openSignSingle() {
        Intent i = new Intent(this, SigningActivity.class);
        i.putExtra(Types.PATH_TYPE, this.path);
        startActivity(i);
    }


    private void setType(String type) {
        this.type = type;
        if (type.equals(Types.OPEN_GALLERY_MULTI_SIGNING_TYPE)) {
            openGalleryMulti();
        } else if (type.equals(Types.OPEN_GALLERY_MULTI_CHOOSE_TYPE)) {
            openGalleryMulti();
        } else if (type.equals(Types.OPEN_GALLERY_SINGLE_SIGNING_TYPE)) {
            openGallerySingle();
        } else if (type.equals(Types.OPEN_GALLERY_SINGLE_CHOOSE_TYPE)) {
            openGallerySingle();
        }

    }
}
