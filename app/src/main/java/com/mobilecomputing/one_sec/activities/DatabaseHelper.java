package com.mobilecomputing.one_sec.activities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class DatabaseHelper extends SQLiteOpenHelper implements Serializable {

    public static final String DATABASE_NAME = "onesec.db";
    public static final String TABLE_NAME = "credentials";
    public static final String COL1 = "ID";
    public static final String COL2 = "NAME";
    public static final String COL3 = "USERNAME";
    public static final String COL4 = "PASSWORD";
    public static final String COL5 = "WEBSITE";
    public static final String COL6 = "SECRETKEY";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " NAME TEXT, USERNAME TEXT, PASSWORD TEXT, WEBSITE TEXT, SECRETKEY TEXT)";
        System.out.println("query is " + createTable);
        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String item1, String item2, String item3, String item4, String item5){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, item1);
        contentValues.put(COL3, item2);
        contentValues.put(COL4, item3);
        contentValues.put(COL5, item4);
        contentValues.put(COL6, item5);

        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1){
            return false;
        }
        else {
            return true;
        }
    }

    public Cursor getListContents(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }

    public Cursor getListValue(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlQuery = "SELECT * FROM " + TABLE_NAME + " WHERE NAME=\""+name+ "\"";

        Cursor data = db.rawQuery(sqlQuery, null);
        return data;
    }

    public int getIDFromName(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlQuery = "SELECT * FROM " + TABLE_NAME + " WHERE NAME=\""+name+ "\"";
        Cursor data = db.rawQuery(sqlQuery, null);
        while(data.moveToNext()){
            int id = data.getInt(0);
            return id;
        }
        return -1;
    }

    public void updateCredentials(int id, String name, String username, String password, String website, String secretkey){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL2, name);
        cv.put(COL3, username);
        cv.put(COL4, password);
        cv.put(COL5, website);
        cv.put(COL6, secretkey);
        db.update(TABLE_NAME, cv, COL1 + "="+ id, null);

    }

    public boolean deleteCredential(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,   "NAME = \"" + name + "\"", null) > 0;
    }

    public Cursor getNamesWebsites(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT NAME, WEBSITE FROM " + TABLE_NAME, null);
        return data;
    }
}
