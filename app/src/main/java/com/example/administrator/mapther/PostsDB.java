package com.example.administrator.mapther;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zhtian on 2016/11/16.
 */

public class PostsDB extends SQLiteOpenHelper {

    private static final String DB_NAME = "big_db.db";
    private static final String TABLE_NAME = "posts_table";
    private static final int DB_VERSION = 1;
    private static int count = 0;

    public PostsDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "create table if not exists " + TABLE_NAME +
                " (_id integer primary key autoincrement," +
                "user_name text," +
                "content text," +
                "phone text)";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insert2DB(String user_name, String content, String phone) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_name", user_name);
        contentValues.put("content", content);
        contentValues.put("phone", phone);
        db.insert(TABLE_NAME, null, contentValues);
        db.close();
        count++;
    }

    public void updateById(int _id, String content) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("content", content);
        db.update(TABLE_NAME, contentValues, "_id=?", new String[]{_id+""});
        db.close();
    }

    public void deleteById(int _id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, "_id=?", new String[]{_id+""});
        count--;
        db.close();
    }

    public Cursor getAll() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{"_id", "user_name", "content", "phone"}, null, null, null, null, null);
        return cursor;
    }

    public boolean queryByName(String name) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, new String[]{"_id", "user_name", "content", "phone"}, "user_name='" + name + "'", null, null, null, null);
        if (cursor == null || cursor.getCount() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public static int getCount() {
        return count;
    }
}
