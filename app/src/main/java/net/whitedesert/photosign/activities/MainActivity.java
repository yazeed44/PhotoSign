package net.whitedesert.photosign.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.utils.Sign;
import net.whitedesert.photosign.utils.SignUtil;


public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView view = (ImageView) this.findViewById(R.id.yoNigga);

 // yo my nigga


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
      /*  Intent i = new Intent(this,GalleryActivity.class);
        i.putExtra("type","single");
        startActivity(i);*/

            for(Sign sign : SignUtil.getSigns(this)){
                Log.i("Main Activity : " , sign.getName());
            }
        Intent i = new Intent(this,DrawSignActivity.class);
        startActivity(i);

    }

}
