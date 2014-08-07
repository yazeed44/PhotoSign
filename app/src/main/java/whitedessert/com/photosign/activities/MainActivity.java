package whitedessert.com.photosign.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.whitedesert.photosign.R;

import whitedessert.com.photosign.drawables.TextDrawable;
import whitedessert.com.photosign.threads.BlendThread;
import whitedessert.com.photosign.utils.PhotoUtil;


public class MainActivity extends Activity {
    private static final int SELECT_PICTURE = 1;
boolean gotFirst = false;


    private String selectedImagePath,selectedImagePath2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         TextView view = (TextView)this.findViewById(R.id.yoNigga);
view.setBackgroundDrawable(new TextDrawable("Yo nigga"));

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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                if (!gotFirst){
                selectedImagePath = PhotoUtil.getPath(selectedImageUri, this);
                gotFirst =true;}
                else {
                    selectedImagePath2 = PhotoUtil.getPath(selectedImageUri, this);
                    Log.i("PhotoSign : MainActivity :"," first photo = " + selectedImagePath + "\n" + "second photo : "+ selectedImagePath2);

                    BlendThread thread = new BlendThread(selectedImagePath,selectedImagePath2,this);
                    thread.setName("blend thread");
                    thread.start();

                    while(thread.getPath() == null){
                    }

                    PhotoUtil.openPhoto(thread.getPath(),this);
                    gotFirst = false;

                }
            }
        }
    }

    public void onClickChoose(View view){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");

        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);
        if(!gotFirst){
          // Toast.makeText(this,"تم اختيار الصورة الاولى" + "  " + selectedImagePath,Toast.LENGTH_SHORT).show();
            ((Button) view).setText("اختر الصورة الثانية");
        }


    }


}
