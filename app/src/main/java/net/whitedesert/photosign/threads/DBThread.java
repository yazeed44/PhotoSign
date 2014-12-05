package net.whitedesert.photosign.threads;

import android.util.Log;

import net.whitedesert.photosign.database.SignsDB;
import net.whitedesert.photosign.utils.Signature;

import java.util.ArrayList;

/**
 * Created by yazeed44 on 9/8/14.
 */
public final class DBThread {


    public static class GetSignThread extends Thread {

        public final static String GET_LATEST_SIGN = "get_latest_sign_44";
        public final static String GET_ALL_SIGNS = "get_all_signs_44";
        public final static String GET_ALL_SIGNS_NO_DEFAULT = "get_all_signs_44_no_default";
        public final static String GET_DEFAULT_SIGN = "get_default_sign_44";
        private final String name;
        private Signature signature;
        private ArrayList<Signature> signatures;

        public GetSignThread(final String name) {
            this.name = name;
            this.setName("Get Sign Thread - " + name);
        }

        @Override
        public void run() {
            SignsDB db = SignsDB.getInstance();
            db.openDatabase();
            if (name != null) {

                //


                if (name.equals(GET_LATEST_SIGN)) {
                    signature = db.getLatestSign();
                } else if (name.equals(GET_ALL_SIGNS)) {
                    signatures = db.getSigns(true);
                } else if (name.equals(GET_ALL_SIGNS_NO_DEFAULT)) {
                    signatures = db.getSigns(false);
                    return;
                } else if (name.equals(GET_DEFAULT_SIGN)) {
                    signature = db.getDefaultSignature();
                } else {
                    signature = db.getSign(name);
                }
            }
            db.closeDatabase();
        }

        public Signature getSignature() {
            return this.signature;
        }

        public ArrayList<Signature> getSignatures() {

            return this.signatures;

        }
    }


    public static class AddSignThread extends Thread {

        private final Signature signature;
        private long id;

        public AddSignThread(final Signature signature) {
            this.signature = signature;
            this.setName("Add sign thread - " + signature.getName());
        }

        @Override
        public void run() {
            SignsDB db = SignsDB.getInstance();
            db.openDatabase();
            id = db.insertSign(signature);

            db.closeDatabase();
        }

        public long getId() {
            return this.id;
        }
    }

    public static class IsDuplicatedSignThread extends Thread {

        private final String name;
        private boolean duplicated = false;

        public IsDuplicatedSignThread(final String name) {
            this.name = name;
            this.setName("is Duplicated Sign Thread - " + name);
        }

        @Override
        public void run() {
            SignsDB db = SignsDB.getInstance();
            db.openDatabase();
            duplicated = db.isDuplicatedSign(name);
            db.closeDatabase();
        }

        public boolean isDuplicated() {
            return this.duplicated;
        }

    }

    public static class SetDefaultSignThread extends Thread {
        private final String signature;

        public SetDefaultSignThread(final String signature) {
            this.signature = signature;
        }

        @Override
        public void run() {
            final SignsDB db = SignsDB.getInstance();
            db.openDatabase();
            db.setDefaultSignature(signature);
            db.closeDatabase();
        }
    }

    public static class DeleteSignThread extends Thread {

        private final String name;
        private int result;

        public DeleteSignThread(final String name) {
            this.name = name;
        }

        @Override
        public void run() {
            final SignsDB db = SignsDB.getInstance();
            db.openDatabase();
            result = db.deleteSign(name);
            Log.d("DeleteSignThread", "Deleting  " + name + "   ,  result  = " + result);
            db.closeDatabase();
        }

        public int getResult() {
            return result;
        }
    }

}
