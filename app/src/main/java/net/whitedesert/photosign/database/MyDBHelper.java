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


    public static final String TABLE_SIGNS_RAW = "signsRaw";
    public static final String TABLE_SIGNS = "signs";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_TEXT = "signText";
    public static final String COLUMN_STYLE = "style";
    public static final String COLUMN_FONT = "font";
    public static final String COLUMN_COLOR = "color";
    public static final String COLUMN_TEXT_SIZE = "textSize";
    public static final String COLUMN_OPACITY = "opacity";
    public static final String COLUMN_PATH = "path";
    public static final String COLUMN_RAW_ID = "_idRaw";
    public static final String COLUMN_WIDTH = "width";
    public static final String COLUMN_HEIGHT = "height";



    public MyDBHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);


    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String createSignsRaw = "CREATE TABLE " + TABLE_SIGNS_RAW + "(" + COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME  +
                " TEXT NOT NULL UNIQUE , " + COLUMN_TEXT + " TEXT NOT NULL," + COLUMN_STYLE + " INTEGER NOT NULL , " + COLUMN_FONT + " TEXT NOT NULL , "
                + COLUMN_COLOR + " INTEGER NOT NULL , " + COLUMN_TEXT_SIZE + " INTEGER NOT NULL , " + COLUMN_OPACITY + " INTEGER NOT NULL " +", " + COLUMN_WIDTH + " INTEGER NOT NULL" +
                ", " + COLUMN_HEIGHT + " INTEGER NOT NULL" +");";


        sqLiteDatabase.execSQL(createSignsRaw);

        String createSigns =  "CREATE TABLE " + TABLE_SIGNS + "("+COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME  +" TEXT NOT NULL UNIQUE,"+COLUMN_PATH + " TEXT NOT NULL , " + COLUMN_RAW_ID +
                " INTEGER NOT NULL" +");";

        sqLiteDatabase.execSQL(createSigns);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        Log.w(MyDBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SIGNS_RAW + ";" + "DROP TABLE IF EXISTS " + TABLE_SIGNS + ";");
        onCreate(sqLiteDatabase);
    }
}
