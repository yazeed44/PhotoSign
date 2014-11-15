package net.whitedesert.photosign.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yazeed44 on 9/23/14.
 */
public final class FileUtil {

    private FileUtil() {
        throw new AssertionError();
    }


    public static List<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                inFiles.addAll(getListFiles(file));
            } else {
                if (file.getName().endsWith(".csv")) {
                    inFiles.add(file);
                }
            }
        }

        Log.i("AssestUtil : getListFiles", "There's  " + inFiles.size() + "   file inside  " + parentDir.getPath());

        return inFiles;
    }


    public static ArrayList<Typeface> getTfs(final Context context) {
        final ArrayList<Typeface> tfArray = new ArrayList<Typeface>();

        for (File font : getListFiles(new File("fonts"))) {
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/" + font.getName());
            tfArray.add(typeface);
        }

        return tfArray;
    }

    public static String getLatestPhoto(final Context context) {
        // Find the last picture
        String[] projection = new String[]{
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.MIME_TYPE
        };
        final Cursor cursor = context.getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
                        null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

// Put it in the image view
        String imageLocation = "";
        if (cursor.moveToFirst()) {

            imageLocation = cursor.getString(1);

        }
        File imageFile = new File(imageLocation);
        if (imageFile.exists()) {
            return imageLocation;

        } else {
            throw new NullPointerException("The latest image returns null ? !");
        }
    }

    public static boolean deleteDirectory(File path) {

        if (path.exists()) {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }


        }
        return (path.delete());
    }

    public static boolean deleteSignature(final String name) {
        final File signFile = new File(Environment.getExternalStorageDirectory(), SaveUtil.SIGNS_FOLDER_NAME + "/" + name);

        return signFile.delete();
    }

    public static File[] getFilesOfSigns() {
        final String path = Environment.getExternalStorageDirectory().toString() + SaveUtil.SIGNS_DIR;

        final File signsFolder = new File(Environment.getExternalStorageDirectory(), SaveUtil.SIGNS_FOLDER_NAME);
        Log.d("Path to /signs", path);
        return signsFolder.listFiles();


    }


}
