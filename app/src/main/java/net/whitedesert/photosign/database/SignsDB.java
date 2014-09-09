package net.whitedesert.photosign.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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

    private static SQLiteOpenHelper mHelper;
    private static SignsDB instance;
    private SQLiteDatabase db;
    private int mOpenCounter;


    public static synchronized void initializeInstance(SQLiteOpenHelper helper) {
        if (instance == null) {
            instance = new SignsDB();
            mHelper = helper;
        }
    }

    public static synchronized SignsDB getInstance() {
        if (instance == null) {
            throw new IllegalStateException(SignsDB.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }

        Log.i("SignsDB : getInstance", Thread.currentThread().getName() + "  is gonna take an instance!");

        return instance;
    }

    public synchronized SQLiteDatabase openDatabase() {
        mOpenCounter++;
        if (mOpenCounter == 1) {
            // Opening new database
            db = mHelper.getWritableDatabase();
        }
        Log.i("SignsDB : openDatabase", Thread.currentThread().getName() + "  is gonna open it's database");
        return db;
    }

    public synchronized void closeDatabase() {
        mOpenCounter--;
        if (mOpenCounter == 0) {
            // Closing database
            db.close();
            Log.i("SignsDB : closeDatabase", Thread.currentThread().getName() + "  did close it's database");
        }
    }


    public long insertSign(Sign sign) throws SQLiteConstraintException {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PATH, sign.getPath());
        values.put(COLUMN_NAME, sign.getName());
        return db.insert(TABLE_SIGNS, null, values);

    }


    public Sign getSign(int id) throws SQLiteException {
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SIGNS + " WHERE " + COLUMN_ID + " = " + id, null);
        cursor.moveToFirst();
        Sign sign = initSign(cursor);
        cursor.close();
        return sign;
    }

    public Sign getSign(String name) throws SQLiteException {
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SIGNS + " WHERE " + COLUMN_NAME + " = " + "'" + name + "'" + ";", null);
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

    public ArrayList<Sign> getSigns() throws SQLiteException {
        ArrayList<Sign> signs = new ArrayList<Sign>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SIGNS, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Sign sign = initSign(cursor);
            signs.add(sign);
            cursor.moveToNext();
        }
        cursor.close();

        return signs;
    }


    public Sign initSign(Cursor cursor) {
        Sign sign = new Sign();
        sign.setPath(cursor.getString(cursor.getColumnIndex(COLUMN_PATH)));
        sign.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
        return sign;
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
