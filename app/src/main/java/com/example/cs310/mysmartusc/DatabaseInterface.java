package com.example.cs310.mysmartusc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseInterface extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseInterface";

    private static final String DATABASE_NAME = "MySmartUSC";
    private static final String TABLE_NAME = "USC";
    public static final String COL0 = "ID";
    public static final String COL1 = "EMAIL";
    public static final String COL2 = "PASSWORD";


    public DatabaseInterface(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " +
                TABLE_NAME + " ( " +
                COL0 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL1 + " TEXT, " +
                COL2 + " TEXT )";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, user.getEmail());
        contentValues.put(COL2, user.getPassword());

        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public Cursor getAllUsers(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public boolean updateUser(User user, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, user.getEmail());
        contentValues.put(COL2, user.getPassword());

        int update = db.update(TABLE_NAME, contentValues, COL0 + " = ? ", new String[] {String.valueOf(id)} );

        if(update != 1) {
            return false;
        }
        else{
            return true;
        }
    }

    public Cursor getUserID(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME  +
                " WHERE " + COL1 + " = '" + user.getEmail() + "'" +
                " AND " + COL2 + " = '" + user.getPassword() + "'";
        return db.rawQuery(sql, null);
    }

    public Integer deleteUser(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[] {String.valueOf(id)});
    }

}
