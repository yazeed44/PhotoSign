package net.whitedesert.photosign.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by yazeed44 on 8/8/14.
 */
public class MyDBHelper extends SQLiteOpenHelper {


    public final static String DB_NAME = "signs.db";
    public final static int DB_VERSION = 1;


    public static final String TABLE_SIGNS = "signs";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PATH = "path";


    public MyDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);


    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        String createSigns = "CREATE TABLE " + TABLE_SIGNS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME + " TEXT NOT NULL UNIQUE," + COLUMN_PATH + " TEXT NOT NULL  " + ");";

        sqLiteDatabase.execSQL(createSigns);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        Log.w(MyDBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SIGNS + ";");
        onCreate(sqLiteDatabase);
    }
}
