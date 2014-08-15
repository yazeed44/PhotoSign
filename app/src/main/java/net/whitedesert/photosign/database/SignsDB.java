package net.whitedesert.photosign.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.whitedesert.photosign.utils.Sign;
import net.whitedesert.photosign.utils.SignRaw;

import java.util.ArrayList;

import static net.whitedesert.photosign.database.MyDBHelper.*;


/**
 * Created by yazeed44 on 8/8/14.
 */
public final class SignsDB {

    private SQLiteDatabase db;
    private MyDBHelper helper;

      public SignsDB(Context context){
          helper = new MyDBHelper(context);

      }

    public void open(){
        db = helper.getWritableDatabase();
    }

    public void close(){
        helper.close();
    }

    public void insertSignRaw(SignRaw sign){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME,sign.getName());
        values.put(COLUMN_COLOR,sign.getColor());
        values.put(COLUMN_FONT,sign.getFont());
        values.put(COLUMN_OPACITY,sign.getOpacity());
        values.put(COLUMN_STYLE,sign.getStyle());
        values.put(COLUMN_TEXT,sign.getText());
        values.put(COLUMN_TEXT_SIZE,sign.getTextSize());
        db.insert(TABLE_SIGNS_RAW,null,values);
    }

    public void insertSign(Sign sign){
        ContentValues values = new ContentValues();
        values.put(COLUMN_PATH,sign.getPath());
        values.put(COLUMN_RAW_ID,sign.getRawId());
        values.put(COLUMN_NAME,sign.getName());
        db.insert(TABLE_SIGNS,null,values);
    }



    public SignRaw getSignRaw(String name){
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SIGNS_RAW + " WHERE " + COLUMN_NAME + " = " + "'" + name + "'" + ";",null);
        cursor.moveToFirst();
        SignRaw raw = initSignRaw(cursor);
        cursor.close();
        return raw;
    }

    public SignRaw getSignRaw(int id){
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SIGNS_RAW + " WHERE " + COLUMN_ID + " = " +  + id +  ";",null);
        cursor.moveToFirst();
        SignRaw raw = initSignRaw(cursor);
        cursor.close();
        return raw;
    }


    public Sign getSign(int id){
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SIGNS + " WHERE " + COLUMN_ID + " = " + id,null);
        cursor.moveToFirst();
        Sign sign = initSign(cursor);
        cursor.close();
        return sign;
    }

    public Sign getSign(String name){
        Cursor cursor = db.rawQuery("SELECT * FROM "+ TABLE_SIGNS + " WHERE " + COLUMN_NAME + " = " + "'" + name +"'" + ";",null);
        cursor.moveToFirst();
        Sign sign = initSign(cursor);
        cursor.close();
        return sign;
    }

    public ArrayList<Sign> getSigns(){
        ArrayList<Sign> signs = new ArrayList<Sign>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SIGNS ,null);

        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            Sign sign = initSign(cursor);
            signs.add(sign);
            cursor.moveToNext();
        }
        cursor.close();

        return signs;
    }


    public Sign initSign(Cursor cursor){
        Sign sign = new Sign();
        sign.setPath(cursor.getString(cursor.getColumnIndex(COLUMN_PATH)));
        sign.setRawId(cursor.getInt(cursor.getColumnIndex(COLUMN_RAW_ID)));
        sign.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
        return sign;
    }

    public SignRaw initSignRaw(Cursor cursor){
        SignRaw raw = new SignRaw();
        raw.setOpacity(cursor.getInt(cursor.getColumnIndex(COLUMN_OPACITY)));
        raw.setColor(cursor.getInt(cursor.getColumnIndex(COLUMN_COLOR)));
        raw.setFont(cursor.getString(cursor.getColumnIndex(COLUMN_FONT)));
        raw.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
        raw.setStyle(cursor.getInt(cursor.getColumnIndex(COLUMN_STYLE)));
        raw.setText(cursor.getString(cursor.getColumnIndex(COLUMN_TEXT)));
        raw.setTextSize(cursor.getInt(cursor.getColumnIndex(COLUMN_TEXT_SIZE)));
        raw.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
        raw.setWidth(cursor.getInt(cursor.getColumnIndex(COLUMN_WIDTH)));
        raw.setHeight(cursor.getInt(cursor.getColumnIndex(COLUMN_HEIGHT)));
        return raw;
    }
}
