package net.whitedesert.photosign.threads;

import net.whitedesert.photosign.database.MyDBHelper;
import net.whitedesert.photosign.database.SignsDB;
import net.whitedesert.photosign.utils.Sign;

import java.util.ArrayList;

/**
 * Created by yazeed44 on 9/8/14.
 */
public class DBThread {


    public static class GetSignThread extends Thread {

        public final static String GET_LATEST_SIGN = "get_latest_sign";
        private final String name;
        private Sign sign;
        private ArrayList<Sign> signs;

        public GetSignThread(final String name) {
            this.name = name;
            this.setName("Get Sign Thread - " + name);
        }

        @Override
        public void run() {
            SignsDB db = SignsDB.getInstance();
            db.openDatabase();
            if (name != null && !name.equals(GET_LATEST_SIGN)) {
                sign = db.getSign(name);
            } else if (name != null && name.equals(GET_LATEST_SIGN)) {
                sign = db.getLatestSign();
            } else if (sign == null) {
                signs = db.getSigns();
            }
            db.closeDatabase();
        }

        public Sign getSign() {
            return this.sign;
        }

        public ArrayList<Sign> getSigns() {
            return this.signs;
        }
    }


    public static class AddSignThread extends Thread {

        private final Sign sign;
        private long id;

        public AddSignThread(final Sign sign) {
            this.sign = sign;
            this.setName("Add sign thread - " + sign.getName());
        }

        @Override
        public void run() {
            SignsDB db = SignsDB.getInstance();
            db.openDatabase();
            id = db.insertSign(sign);
            db.closeDatabase();
        }

        public long getId() {
            return this.id;
        }
    }

    public static class IsDuplicatedSignThread extends Thread {

        private String name;
        private boolean duplicated = false;

        public IsDuplicatedSignThread(final String name) {
            this.name = name;
            this.setName("is Duplicated Sign Thread - " + name);
        }

        @Override
        public void run() {
            SignsDB db = SignsDB.getInstance();
            db.openDatabase();
            duplicated = db.isDuplicatedSign(name, MyDBHelper.TABLE_SIGNS);
            db.closeDatabase();
        }

        public boolean getIsDuplicated() {
            return this.duplicated;
        }

    }

}
