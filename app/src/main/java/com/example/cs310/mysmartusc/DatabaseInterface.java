package com.example.cs310.mysmartusc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseInterface extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseInterface";
    private static final String DATABASE_NAME = "MySmartUSC";

//    In the names of the columns (e.g., COL1_0), the first number represents the table number
//    (1 for Users, 2 for Emails) and the second number represents the column number within the table.

    private static final String TABLE_1_NAME = "Users";
    public static final String COL1_0 = "ID";
    public static final String COL1_1 = "EMAIL";

    private static final String TABLE_2_NAME = "Emails";
    public static final String COL2_0 = "ID";
    public static final String COL2_1 = "SENDER";
    public static final String COL2_2 = "SUBJECT";
    public static final String COL2_3 = "BODY";
    public static final String COL2_4 = "TYPE";
    public static final String COL2_5 = "USERID";

    private static final String TABLE_3_NAME = "Keywords";
    public static final String COL3_0 = "ID";
    public static final String COL3_1 = "TEXT";
    public static final String COL3_2 = "TYPE";
    public static final String COL3_3 = "USERID";
    public static final String COL3_4 = "CATEGORY";


    public DatabaseInterface(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql1 = "CREATE TABLE " +
                TABLE_1_NAME + " ( " +
                COL1_0 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL1_1 + " TEXT )";

        String sql2 = "CREATE TABLE " +
                TABLE_2_NAME + " ( " +
                COL2_0 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2_1 + " TEXT, " +
                COL2_2 + " TEXT, " +
                COL2_3 + " TEXT, " +
                COL2_4 + " TEXT, " +
                COL2_5 + " INTEGER, " +
                "FOREIGN KEY(" + COL2_5 + ") REFERENCES " + TABLE_1_NAME + "(" + COL1_0 + " ))";


        String sql3 = "CREATE TABLE " +
                TABLE_3_NAME + " ( " +
                COL3_0 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL3_1 + " TEXT, " +
                COL3_2 + " TEXT, " +
                COL3_3 + " INTEGER, " +
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

    // User table functions:
    // addUser(), getAllUsers(), updateUser(), getUserID(), deleteUser()
    public boolean addUser(String user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();


        if(cv.get(user) == null){
            cv.put(COL1_1, user);
            Log.e("Database Activity!", "Added user: " + user);
            long result = db.insert(TABLE_1_NAME, null, cv);

            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }

        return true;
    }


    public Cursor getAllUsers(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_1_NAME, null);
    }

    public boolean updateUser(String user, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL1_1, user);

        int update = db.update(TABLE_1_NAME, cv, COL1_0 + " = ? ", new String[] {String.valueOf(id)} );

        if(update != 1) {
            return false;
        }
        else{
            return true;
        }
    }

    public Cursor getUserID(String user){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_1_NAME  +
                " WHERE " + COL1_1 + " = '" + user + "'";
        return db.rawQuery(sql, null);
    }

    public Integer deleteUser(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_1_NAME, "ID = ?", new String[] {String.valueOf(id)});
    }



    // Email table functions:
    // addEmail(), getAllEmails(), updateEmail(), getEmailID(), deleteEmail()

    public boolean addEmail(Email email, String user, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        Cursor c = this.getUserID(user);
        int userID = c.getInt(0);
        cv.put(COL2_1, email.getSender());
        cv.put(COL2_2, email.getSubject());
        cv.put(COL2_3, email.getBody());
        cv.put(COL2_4, type);
        cv.put(COL2_5, userID);

        long result = db.insert(TABLE_2_NAME, null, cv);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public Cursor getAllEmails(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_2_NAME, null);
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

    public Cursor getEmailID(Email email){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_2_NAME  +
                " WHERE " + COL2_1 + " = '" + email.getSender() + "'" +
                " AND " + COL2_2 + " = '" + email.getSubject() + "'";
        return db.rawQuery(sql, null);
    }

    public Integer deleteEmail(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_2_NAME, "ID = ?", new String[] {String.valueOf(id)});
    }




    // Keyword table functions:
    // addKeyword(), getAllKeywords(), updateKeyword(), getKeywordID(), deleteKeyword()

    public boolean addKeyword(String keyword, String type, String user) {
        Log.e("Database Activity!", "");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        Cursor c = this.getUserID(user);
        int userID = c.getInt(0);
        cv.put(COL3_1, keyword);
        cv.put(COL3_2, type);
        cv.put(COL3_3, userID);

        long result = db.insert(TABLE_3_NAME, null, cv);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getKeywordsByType(String type, String category)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_3_NAME + " WHERE " + COL3_2
        + " = " + type + " AND " + COL3_4 + " = " + category, null);
    }

    public Cursor getAllKeywords(){
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

    public Cursor getKeywordID(String user, String keyword){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = this.getUserID(user);
        int userID = c.getInt(0);

        String sql = "SELECT * FROM " + TABLE_3_NAME  +
                " WHERE " + COL3_1 + " = '" + keyword + "'" +
                " AND " + COL3_3 + " = '" + userID + "'";
        return db.rawQuery(sql, null);
    }

    public Integer deleteKeyword(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_3_NAME, "ID = ?", new String[] {String.valueOf(id)});
    }




}
