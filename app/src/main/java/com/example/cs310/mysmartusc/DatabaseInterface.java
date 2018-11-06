package com.example.cs310.mysmartusc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;


public class DatabaseInterface extends SQLiteOpenHelper {

    private static DatabaseInterface ourInstance = null;
    private static final String TAG = "DatabaseInterface";
    private static final String DATABASE_NAME = "MySmartUSC";

//    In the names of the columns (e.g., COL1_0), the first number represents the table number
//    (1 for Users, 2 for Emails, 3 for Keywords) and the second number represents the column number within the table.

    private static final String TABLE_1_NAME = "Users";
    public static final String COL1_0 = "ID";
    public static final String COL1_1 = "EMAIL_USER";
    public static final String COL1_2 = "EMAIL_DOMAIN";

    private static final String TABLE_2_NAME = "Emails";
    public static final String COL2_0 = "ID";
    public static final String COL2_1 = "SENDER_USER";
    public static final String COL2_2 = "SUBJECT";
    public static final String COL2_3 = "BODY";
    public static final String COL2_4 = "TYPE";
    public static final String COL2_5 = "USERID";
    public static final String COL2_6 = "SENDER_DOMAIN";
    public static final String COL2_7 = "MESSAGEID";

    private static final String TABLE_3_NAME = "Keywords";
    public static final String COL3_0 = "ID";
    public static final String COL3_1 = "TEXT";
    public static final String COL3_2 = "TYPE";
    public static final String COL3_3 = "USERID";
    public static final String COL3_4 = "CATEGORY";

    public static DatabaseInterface getInstance(Context context) {
        if(ourInstance == null) {
            ourInstance = new DatabaseInterface(context);
        }
        return ourInstance;
    }
    private DatabaseInterface(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql1 = "CREATE TABLE " +
                TABLE_1_NAME + " ( " +
                COL1_0 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL1_1 + " TEXT, " +
                COL1_2 + " TEXT)";

        String sql2 = "CREATE TABLE " +
                TABLE_2_NAME + " ( " +
                COL2_0 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2_1 + " TEXT, " +
                COL2_2 + " TEXT, " +
                COL2_3 + " TEXT, " +
                COL2_4 + " TEXT, " +
                COL2_5 + " INTEGER, " +
                COL2_6 + " INTEGER, " +
                COL2_7 + " TEXT, " +
                "FOREIGN KEY(" + COL2_5 + ") REFERENCES " + TABLE_1_NAME + "(" + COL1_0 + " ))";

        String sql3 = "CREATE TABLE " +
                TABLE_3_NAME + " ( " +
                COL3_0 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL3_1 + " TEXT, " +
                COL3_2 + " TEXT, " +
                COL3_3 + " INTEGER, " +
                COL3_4 + " TEXT, " +
                "FOREIGN KEY(" + COL3_3 + ") REFERENCES " + TABLE_1_NAME + "(" + COL1_0 + " ))";

        db.execSQL(sql1);
        db.execSQL(sql2);
        db.execSQL(sql3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_1_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_2_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_3_NAME);
        onCreate(db);
    }

    public void close(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.close();
    }

    // User table functions:
    // addUser(), getAllUsers(), updateUser(), getUserID(), deleteUser()
    // ---------------------------------------------------------------------------------------------------------

    public boolean addUser(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        String user = email.split("@")[0];
        String domain = email.split("@")[1];

        Cursor getUser = db.rawQuery("SELECT * FROM " + TABLE_1_NAME +
                " WHERE " + COL1_1 + " = '" + user + "'" +
                " AND " + COL1_2 + " = '" + domain + "'", null);

        if (getUser != null && getUser.getCount() == 0) {

            cv.put(COL1_1, user);
            cv.put(COL1_2, domain);
            Log.e("Database Activity!", "Added user: " + user + " with domain: " + domain);
            long result = db.insert(TABLE_1_NAME, null, cv);

            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }

        return false;
    }

    public boolean tableExists(String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + tableName, null);
        if (c != null && c.getCount() >= 0){
            return true;
        } else {
            return false;
        }
    }
    public boolean userExists(String username, String domain){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM USERS WHERE EMAIL_USER = '" + username + "' AND EMAIL_DOMAIN = '" + domain + "'", null);
        if (c != null && c.getCount() >= 0){
            return true;
        } else {
            return false;
        }
    }


    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_1_NAME, null);
    }

    public boolean updateUser(String email, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        String user = email.split("@")[0];
        String domain = email.split("@")[1];

        cv.put(COL1_1, user);
        cv.put(COL1_2, domain);

        int update = db.update(TABLE_1_NAME, cv, COL1_0 + " = ? ", new String[]{String.valueOf(id)});

        if (update != 1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getUserID(String email){
        SQLiteDatabase db = this.getWritableDatabase();

        String user = email.split("@")[0];
        String domain = email.split("@")[1];

        String sql = "SELECT ID FROM " + TABLE_1_NAME +
                " WHERE " + COL1_1 + " = '" + user + "'" +
                " AND " + COL1_2 + " = '" + domain + "'";

        return db.rawQuery(sql, null);
    }

    public Integer deleteUser(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_1_NAME, "ID = ?", new String[]{String.valueOf(id)});
    }


    // Email table functions:
    // addEmail(), getAllEmails(), updateEmail(), getEmailID(), deleteEmail()
    // ---------------------------------------------------------------------------------------------------------

    public boolean addEmail(Email email, String user, String type, String messageID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        Cursor c = this.getUserID(user);
        c.moveToFirst();
        String id = c.getString(0);

        String sender = email.getSender();
        String emailSender = "";

        if(sender.indexOf("<") != -1 || sender.indexOf(">") != -1) {
            System.out.println(sender);
            emailSender = sender.substring(sender.indexOf("<") + 1, sender.indexOf(">"));
        }else{
            emailSender = sender;
        }

        String sender_user = emailSender.split("@")[0];
        String sender_domain = emailSender.split("@")[1];

        cv.put(COL2_1, sender_user);
        cv.put(COL2_2, email.getSubject());
        cv.put(COL2_3, email.getBody());
        cv.put(COL2_4, type);
        cv.put(COL2_5, id);
        cv.put(COL2_6, sender_domain);
        cv.put(COL2_7, messageID);

        long result = db.insert(TABLE_2_NAME, null, cv);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public Cursor getAllEmails() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_2_NAME, null);
    }

    public boolean emailExists(String subject, String sender_user, String sender_domain){
        SQLiteDatabase db = this.getWritableDatabase();

        Log.e("DI", "Checking for d: " + subject + ", " + sender_user + ", " + sender_domain);

        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_2_NAME + " WHERE " + COL2_2 + " = '" + subject + "'" +
                " AND " + COL2_1 + " = '" + sender_user + "'" + " AND " + COL2_6 + " = '" + sender_domain + "'", null);

        if(c.getCount() > 0){
            return true;
        }

        return false;
    }

    public Cursor getEmailByType(String email, String type)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = this.getUserID(email);
        c.moveToFirst();
        String id = c.getString(0);


        return db.rawQuery("SELECT * FROM " + TABLE_2_NAME + " WHERE " + COL2_4 + " = '" + type + "'" +
                " AND " + COL2_5 + " = '" + id + "'", null);
    }

    // Probably won't need a function to update email information once an Email is already
    // in the table.
//    public boolean updateEmail(User user, int id){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//        cv.put(COL2_1, user.getEmail());
//        cv.put(COL2_2, user.getPassword());
//
//        int update = db.update(TABLE_1_NAME, cv, COL1_0 + " = ? ", new String[] {String.valueOf(id)} );
//
//        if(update != 1) {
//            return false;
//        }
//        else{
//            return true;
//        }
//    }

    public Cursor getEmailID(Email email) {
        SQLiteDatabase db = this.getWritableDatabase();

        String sender_user = email.getSender().split("@")[0];
        String sender_domain = email.getSender().split("@")[1];

        String sql = "SELECT * FROM " + TABLE_2_NAME  +
                " WHERE " + COL2_1 + " = '" + sender_user + "'" +
                " AND " + COL2_6 + " = '" + sender_domain + "'" +
                " AND " + COL2_2 + " = '" + email.getSubject() + "'";
        return db.rawQuery(sql, null);
    }

    public Integer deleteEmail(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_2_NAME, "ID = ?", new String[]{String.valueOf(id)});
    }

    // returns true if an Email is already in the database with that ID
    // returns false if the Email ID is new so the email can be sorted
    public boolean checkMessageID(String id) {

        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "SELECT * FROM " + TABLE_2_NAME  +
                " WHERE " + COL2_7 + " = '" + id + "'";

        Cursor c = db.rawQuery(sql, null);
        int count = c.getCount();
        return (count != 0);
    }


    // Keyword table functions:
    // addKeyword(), getAllKeywords(), updateKeyword(), getKeywordID(), deleteKeyword()
    // ---------------------------------------------------------------------------------------------------------

    public boolean addKeyword(String keyword, String type, String user, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        Cursor c = this.getUserID(user);
        c.moveToFirst();
        String id = c.getString(0);
        Integer intID = Integer.parseInt(id);

        cv.put(COL3_1, keyword);
        cv.put(COL3_2, type);
        cv.put(COL3_3, intID);
        cv.put(COL3_4, category);

        Log.e("DatabaseInterface", "Trying to add keyword " + keyword + ": " + type + ", " + id + ", "  + category);


        long result = db.insert(TABLE_3_NAME, null, cv);

        if (result == -1) {
            Log.e("DataBaseInterface", "Failed to add keyword");
            return false;
        } else {
            Log.e("DatabaseInterface", "Success adding keyword: " + keyword);
            return true;
        }
    }

    public Cursor getKeywordsByType(String type, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        //Log.e("DatabaseInterface", "GetKeywordsByType("+type+", " + category +")");

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_3_NAME + " WHERE " + COL3_2
                + " = " + "'" + type + "'" + " AND " + COL3_4 + " = " + "'" + category + "'" , null);

        Log.e("DatabaseInterface", "GetKeywordsByType() returned " + cursor.getCount() + " rows");
        return cursor;
    }

    public Cursor getAllKeywords() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_3_NAME, null);
    }

    // Again, may not need this function for the Keyword table.
    //
//    public boolean updateKeyword(User user, int id){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//        cv.put(COL1_1, user.getEmail());
//        cv.put(COL1_2, user.getPassword());
//
//        int update = db.update(TABLE_1_NAME, cv, COL1_0 + " = ? ", new String[] {String.valueOf(id)} );
//
//        if(update != 1) {
//            return false;
//        }
//        else{
//            return true;
//        }
//    }

    public Cursor getKeywordID(String user, String keyword) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = this.getUserID(user);
        int userID = c.getInt(0);

        String sql = "SELECT * FROM " + TABLE_3_NAME +
                " WHERE " + COL3_1 + " = '" + keyword + "'" +
                " AND " + COL3_3 + " = '" + userID + "'";
        return db.rawQuery(sql, null);
    }

    public Integer deleteKeyword(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_3_NAME, "ID = ?", new String[]{String.valueOf(id)});
    }

    public void removeKeyword(String user, String keyword){
        int id = getKeywordID(user, keyword);
        deleteKeyword(id);
    }

}

