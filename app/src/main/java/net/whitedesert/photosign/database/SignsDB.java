package net.whitedesert.photosign.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import net.whitedesert.photosign.utils.CheckUtil;
import net.whitedesert.photosign.utils.FileUtil;
import net.whitedesert.photosign.utils.Signature;
import net.whitedesert.photosign.utils.SignatureUtil;

import java.util.ArrayList;

import static net.whitedesert.photosign.database.MyDBHelper.COLUMN_ID;
import static net.whitedesert.photosign.database.MyDBHelper.COLUMN_IS_DEFAULT;
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
    private ArrayList<Signature> signs = new ArrayList<Signature>(); // Used for optimization

    private Signature sign = new Signature(); // Object of signature class , used for optimization because creating new objects is expensives


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
        values.put(COLUMN_IS_DEFAULT, signature.isDefault() ? 1 : 0);
        return db.insert(TABLE_SIGNS, null, values);

    }


    public Signature getSign(int id) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SIGNS + " WHERE " + COLUMN_ID + " = " + id, null);
        cursor.moveToFirst();
        initSign(cursor);
        cursor.close();
        return sign;
    }

    public Signature getSign(String name) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SIGNS + " WHERE " + COLUMN_NAME + " = " + "'" + name + "'" + ";", null);
        cursor.moveToFirst();
        initSign(cursor);
        cursor.close();
        return sign;
    }

    public Signature getLatestSign() {
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SIGNS + " WHERE " + COLUMN_ID + " = (SELECT MAX(" + COLUMN_ID + ") FROM " + TABLE_SIGNS + ");", null);
        cursor.moveToFirst();
        initSign(cursor);
        cursor.close();
        return sign;
    }

    public ArrayList<Signature> getSigns(boolean includeDefault) {
        signs.clear();


        String query = "SELECT * FROM " + TABLE_SIGNS;

        if (!includeDefault) {
            query += " WHERE " + COLUMN_IS_DEFAULT + " = 0";
        }

        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            final Signature sign = createSign(cursor);
            signs.add(sign);
            cursor.moveToNext();
        }
        cursor.close();


        Log.d("Signs count", signs.size() + "");

        return signs;
    }

    public Cursor getSignsCursor(boolean includeDefault) {
        String query = "SELECT * FROM " + TABLE_SIGNS;

        if (!includeDefault) {
            query += " WHERE " + COLUMN_IS_DEFAULT + " = 0";
        }

        return db.rawQuery(query, null);

    }


    private void initSign(Cursor cursor) {

        try {
            sign.setPath(cursor.getString(cursor.getColumnIndex(COLUMN_PATH)));
            sign.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
            sign.setDefault(cursor.getInt(cursor.getColumnIndex(COLUMN_IS_DEFAULT)));
            sign.counter = 0;
        } catch (CursorIndexOutOfBoundsException ex) {
            sign = SignatureUtil.EMPTY_SIGNATURE;
        }

        //    Log.d("initSign", sign.toString());

    }

    //Use only inside array!! in other cases jst use initSign and sign object
    private Signature createSign(Cursor cursor) {

        Signature sign = new Signature();
        try {
            sign.setPath(cursor.getString(cursor.getColumnIndex(COLUMN_PATH)));
            sign.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
            sign.setDefault(cursor.getInt(cursor.getColumnIndex(COLUMN_IS_DEFAULT)));
        } catch (CursorIndexOutOfBoundsException ex) {
            sign = SignatureUtil.EMPTY_SIGNATURE;
            Log.e("createSign", "Exception happend !!" + " The signature is empty now");
        }

        //      Log.d("createSign", sign.toString());

        return sign;

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

//        Log.i("SignsDB : is DuplicatedSign", "name  =  " + name + "   ,  is Duplicated ?  = " + isDuplicated);

        return isDuplicated;


    }

    public Signature getDefaultSignature() {
        final Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SIGNS + " WHERE " + COLUMN_IS_DEFAULT + " = " + "1;", null);
        cursor.moveToFirst();

        initSign(cursor);

        return sign;

    }

    public void setDefaultSignature(String name) {
        unDefault();
        final Signature sign = getSign(name);

        final ContentValues updateValues = new ContentValues();
        updateValues.put(COLUMN_IS_DEFAULT, 1);
        updateValues.put(COLUMN_PATH, sign.getPath());
        updateValues.put(COLUMN_NAME, sign.getName());


        db.update(TABLE_SIGNS, updateValues, COLUMN_NAME + " = " + "'" + name + "'", null);

        //  Log.d("setDefaultSignature", name + "  Is the default signature" +  "  "+SignatureUtil.getSign(name).isDefault());

    }


    public void unDefault() {

        final Signature defSign = getDefaultSignature();

        if (!CheckUtil.checkSign(defSign))
            return;


        final ContentValues updateValues = new ContentValues();
        updateValues.put(COLUMN_IS_DEFAULT, 0);
        updateValues.put(COLUMN_PATH, defSign.getPath());
        updateValues.put(COLUMN_NAME, defSign.getName());


        db.update(TABLE_SIGNS, updateValues, COLUMN_NAME + " = " + "'" + defSign.getName() + "'", null);


        Log.d("unDefault", defSign.getName() + "  is Default ?   =  " + SignatureUtil.getSign(defSign.getName()).isDefault());


    }

    public boolean deleteSign(final Signature signature, boolean deleteFile) {

        int deleteResult;

        deleteResult = db.delete(TABLE_SIGNS, COLUMN_NAME + " = " + "'" + signature.getName() + "'", null);

        boolean fileDeleted = true;
        if (deleteFile) {
            fileDeleted = FileUtil.deleteSignature(signature.getName());
        }


        return deleteResult > 1 && fileDeleted;


    }


}
