package net.whitedesert.photosign.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.luminous.pick.Action;
import com.luminous.pick.CustomGallery;

import java.util.ArrayList;

/**
 * Created by yazeed44 on 8/8/14.
 */
public class GalleryActivity extends com.luminous.pick.MainActivity {

   private String path;
   private ArrayList<String> paths = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = this.getIntent();
        String type = i.getStringExtra("type");
        if (type.equals("single")){
            openGallerySingle();
        }
        else {
            openGalleryMulti();
        }
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
           openBlendSingle();
            Log.i("Gallery Activity : " , "Got picture  " + path);

        } else if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            String[] all_path = data.getStringArrayExtra("all_path");

            ArrayList<CustomGallery> dataT = new ArrayList<CustomGallery>();

            for (String string : all_path) {
                CustomGallery item = new CustomGallery();
                item.sdcardPath = string;
                paths.add(item.sdcardPath);
                Log.i("Gallery Activity : " , "Got picture  " + string);
                dataT.add(item);
            }

            viewSwitcher.setDisplayedChild(0);
            adapter.addAll(dataT);
        }
    }

    public  String openGallerySingle(){

        // For single image
        Intent i = new Intent(Action.ACTION_PICK);
      startActivityForResult(i, 100);
        return path != null ? path : null;
    }

    public  ArrayList<String> openGalleryMulti(){

        // For multiple images
        Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
        startActivityForResult(i, 200);
        return !paths.isEmpty() ? paths : null;

    }

    private void openBlendSingle(){
        Intent i = new Intent(this,BlendActivity.class);
        i.putExtra("path",this.path);
        startActivity(i);
    }

}
