package net.whitedesert.photosign.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import net.whitedesert.photosign.utils.Sign;

import java.util.ArrayList;

import static net.whitedesert.photosign.database.MyDBHelper.COLUMN_ID;
import static net.whitedesert.photosign.database.MyDBHelper.COLUMN_NAME;
import static net.whitedesert.photosign.database.MyDBHelper.COLUMN_PATH;
import static net.whitedesert.photosign.database.MyDBHelper.TABLE_SIGNS;


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


    public long insertSign(Sign sign) throws SQLiteConstraintException {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PATH,sign.getPath());
        values.put(COLUMN_NAME,sign.getName());
        return db.insert(TABLE_SIGNS, null, values);

    }


    public Sign getSign(int id) throws SQLiteException{
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SIGNS + " WHERE " + COLUMN_ID + " = " + id,null);
        cursor.moveToFirst();
        Sign sign = initSign(cursor);
        cursor.close();
        return sign;
    }

    public Sign getSign(String name) throws SQLiteException{
        Cursor cursor = db.rawQuery("SELECT * FROM "+ TABLE_SIGNS + " WHERE " + COLUMN_NAME + " = " + "'" + name +"'" + ";",null);
        cursor.moveToFirst();
        Sign sign = initSign(cursor);
        cursor.close();
        return sign;
    }

    public Sign getLatestSign() {
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SIGNS + " WHERE " + COLUMN_ID + " = (SELECT MAX(" + COLUMN_ID + ") FROM " + TABLE_SIGNS + ");", null);
        cursor.moveToFirst();
        Sign sign = initSign(cursor);
        cursor.close();
        return sign;
    }
    public ArrayList<Sign> getSigns() throws SQLiteException{
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
        sign.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
        return sign;
    }


    public void dropAll(){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SIGNS + ";");
    }

    public boolean isDuplicatedSign(String name, String table) {
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_NAME + " FROM " + table + " WHERE " + COLUMN_NAME + " = '" + name + "'" + ";", null);
        cursor.moveToFirst();
        boolean isDuplicated;
        try {
            isDuplicated = cursor.isNull(cursor.getColumnIndex(COLUMN_ID));
        } catch (CursorIndexOutOfBoundsException ex) {
            isDuplicated = false;
        }
        return isDuplicated;


    }


}
