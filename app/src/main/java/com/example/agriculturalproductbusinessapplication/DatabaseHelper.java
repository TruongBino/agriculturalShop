package com.example.agriculturalproductbusinessapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "vegetable store";
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_ROLE = "role";
    private static final String COLUMN_AVATAR = "avatar";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_PHONE + " INT,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_ROLE + " INTEGER,"
                + COLUMN_AVATAR + " BLOB"
                + ")";
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public void addUser(String username,String phone, String email, String password, int role, byte[] avatar) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_ROLE, role);
        values.put(COLUMN_AVATAR, avatar);
        db.insert(TABLE_USERS, null, values);
    }

    public boolean checkUser(String email, String password, int role) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_USERS +
                " WHERE " + COLUMN_EMAIL + " = ?" +
                " AND " + COLUMN_PASSWORD + " = ?" +
                " AND " + COLUMN_ROLE + " = ?";
        String[] selectionArgs = {email, password, String.valueOf(role)};
        Cursor cursor = db.rawQuery(query, selectionArgs);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count > 0;
    }

    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_USERS +
                " WHERE " + COLUMN_EMAIL + " = ?";
        String[] selectionArgs = {email};
        Cursor cursor = db.rawQuery(query, selectionArgs);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count > 0;
    }
}