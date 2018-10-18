package com.example.cs310.mysmartusc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseInterface extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseInterface";
    private static final String DATABASE_NAME = "MySmartUSC";

//    In the names of the columns (e.g., COL1_0), the first number represents the table number
//    (1 for Users, 2 for Emails) and the second number represents the column number within the table.

    private static final String TABLE_1_NAME = "Users";
    public static final String COL1_0 = "ID";
    public static final String COL1_1 = "EMAIL";
    public static final String COL1_2 = "PASSWORD";

    private static final String TABLE_2_NAME = "Emails";
    public static final String COL2_0 = "ID";
    public static final String COL2_1 = "SENDER";
    public static final String COL2_2 = "SUBJECT";
    public static final String COL2_3 = "BODY";
    public static final String COL2_4 = "USERID";

    private static final String TABLE_3_NAME = "Keywords";
    public static final String COL3_0 = "ID";
    public static final String COL3_1 = "TEXT";
    public static final String COL3_2 = "TYPE";
    public static final String COL3_3 = "USERID";



    public DatabaseInterface(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql1 = "CREATE TABLE " +
                TABLE_1_NAME + " ( " +
                COL1_0 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL1_1 + " TEXT, " +
                COL1_2 + " TEXT )";

        String sql2 = "CREATE TABLE " +
                TABLE_2_NAME + " ( " +
                COL2_0 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2_1 + " TEXT, " +
                COL2_2 + " TEXT, " +
                COL2_3 + " TEXT, " +
                COL2_4 + " INTEGER, " +
                "FOREIGN KEY(" + COL2_4 + ") REFERENCES " + TABLE_1_NAME + "(" + COL1_0 + " ))";


        String sql3 = "CREATE TABLE " +
                TABLE_3_NAME + " ( " +
                COL3_0 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL3_1 + " TEXT, " +
                COL3_2 + " TEXT, " +
                COL3_3 + " INTEGER, " +
                "FOREIGN KEY(" + COL3_3 + ") REFERENCES " + TABLE_1_NAME + "(" + COL1_0 + " ))";

        db.execSQL(sql1);
        db.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_1_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_2_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_3_NAME);
        onCreate(db);
    }

    public boolean addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL1_1, user.getEmail());
        cv.put(COL1_2, user.getPassword());

        long result = db.insert(TABLE_1_NAME, null, cv);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public Cursor getAllUsers(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_1_NAME, null);
    }

    public boolean updateUser(User user, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL1_1, user.getEmail());
        cv.put(COL1_2, user.getPassword());

        int update = db.update(TABLE_1_NAME, cv, COL1_0 + " = ? ", new String[] {String.valueOf(id)} );

        if(update != 1) {
            return false;
        }
        else{
            return true;
        }
    }

    public Cursor getUserID(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_1_NAME  +
                " WHERE " + COL1_1 + " = '" + user.getEmail() + "'" +
                " AND " + COL1_2 + " = '" + user.getPassword() + "'";
        return db.rawQuery(sql, null);
    }

    public Integer deleteUser(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_1_NAME, "ID = ?", new String[] {String.valueOf(id)});
    }

}
