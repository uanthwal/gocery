package com.mobilecomputing.one_sec.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;

import com.mobilecomputing.one_sec.model.LoginInfo;

public class AppDbHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "one-sec.db";
    public static final String TABLE_LOGIN = "login";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USER_NAME = "name";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_DOB = "dob";
    public static final String COLUMN_MOB_NUM = "mob_num";
    public static final String COLUMN_EMAIL = "email";


    public AppDbHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_LOGIN + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_NAME + " TEXT ," +
                COLUMN_PASSWORD + " TEXT ," +
                COLUMN_DOB + " TEXT ," +
                COLUMN_EMAIL + " TEXT ," +
                COLUMN_MOB_NUM + " TEXT " +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        onCreate(db);
    }

    public void addNewUser(LoginInfo userLogin) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, userLogin.getName());
        values.put(COLUMN_PASSWORD, userLogin.getPassword());
        values.put(COLUMN_MOB_NUM, userLogin.getMobNum());
        values.put(COLUMN_EMAIL, userLogin.getEmail());
        values.put(COLUMN_DOB, userLogin.getDob());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_LOGIN, null, values);
        db.close();
    }

    public LoginInfo authenticateUser(String email, String password) {
        LoginInfo loginInfo = new LoginInfo();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_LOGIN + " WHERE " + COLUMN_EMAIL + " = '" + email + "' AND " + COLUMN_PASSWORD + " = '" + password + "'";
        Cursor recordSet = db.rawQuery(query, null);
        recordSet.moveToFirst();
        if (recordSet.getCount() == 1) {
            loginInfo.setMobNum(recordSet.getString(recordSet.getColumnIndex("mob_num")));
            loginInfo.setName(recordSet.getString(recordSet.getColumnIndex("name")));
            loginInfo.setDob(recordSet.getString(recordSet.getColumnIndex("dob")));
            loginInfo.setEmail(recordSet.getString(recordSet.getColumnIndex("email")));
        }
        db.close();
        return loginInfo;
    }

    public int checkIfUserExists(String email) {
        LoginInfo loginInfo = new LoginInfo();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_LOGIN + " WHERE " + COLUMN_EMAIL + " = '" + email+ "'";
        Cursor recordSet = db.rawQuery(query, null);
        recordSet.moveToFirst();
        int flag = -1;
        if (recordSet.getCount() > 0) {
            flag = 1;
        }
        db.close();
        return flag;
    }

    public void addDummyUser() {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, "upendra");
        values.put(COLUMN_PASSWORD, "pass");
        values.put(COLUMN_EMAIL, "uanthwal@gmail.com");
        values.put(COLUMN_MOB_NUM, "9024031170");
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_LOGIN, null, values);
        db.close();
    }

}
