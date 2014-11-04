package net.whitedesert.photosign.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import net.whitedesert.photosign.utils.Signature;

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


    public long insertSign(Signature signature) throws SQLiteConstraintException {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PATH, signature.getPath());
        values.put(COLUMN_NAME, signature.getName());
        return db.insert(TABLE_SIGNS, null, values);

    }


    public Signature getSign(int id) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SIGNS + " WHERE " + COLUMN_ID + " = " + id, null);
        cursor.moveToFirst();
        Signature signature = initSign(cursor);
        cursor.close();
        return signature;
    }

    public Signature getSign(String name) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SIGNS + " WHERE " + COLUMN_NAME + " = " + "'" + name + "'" + ";", null);
        cursor.moveToFirst();
        Signature signature = initSign(cursor);
        cursor.close();
        return signature;
    }

    public Signature getLatestSign() {
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SIGNS + " WHERE " + COLUMN_ID + " = (SELECT MAX(" + COLUMN_ID + ") FROM " + TABLE_SIGNS + ");", null);
        cursor.moveToFirst();
        Signature signature = initSign(cursor);
        cursor.close();
        return signature;
    }

    public ArrayList<Signature> getSigns() throws SQLiteException {
        ArrayList<Signature> signatures = new ArrayList<Signature>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SIGNS, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Signature signature = initSign(cursor);
            signatures.add(signature);
            cursor.moveToNext();
        }
        cursor.close();
        return signatures;
    }


    public Signature initSign(Cursor cursor) {
        Signature signature = new Signature();
        signature.setPath(cursor.getString(cursor.getColumnIndex(COLUMN_PATH)));
        signature.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
        return signature;
    }


    public boolean isDuplicatedSign(String name) {
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_NAME + " FROM " + TABLE_SIGNS + " WHERE " + COLUMN_NAME + " = '" + name + "'" + ";", null);
        cursor.moveToFirst();
        boolean isDuplicated;
        try {
            isDuplicated = cursor.isNull(cursor.getColumnIndex(COLUMN_ID));
        } catch (CursorIndexOutOfBoundsException ex) {
            isDuplicated = false;
        }

        Log.i("SignsDB : is DuplicatedSign", "name  =  " + name + "   ,  is Duplicated ?  = " + isDuplicated);

        return isDuplicated;


    }


}
