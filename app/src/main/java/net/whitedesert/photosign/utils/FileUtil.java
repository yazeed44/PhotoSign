package net.whitedesert.photosign.utils;

import android.content.Context;
import android.graphics.Typeface;
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
}
