package com.example.administrator.mapther;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenjg on 2016/12/9.
 */

public class UserDB  extends SQLiteOpenHelper{
    private static final String DB_NAME = "USER_DB";
    private static final String TABLE_NAME = "USER_Table";
    private static final int DB_VERSION = 1;
    private SQLiteDatabase sqLiteDatabase;
    private final String CREATE_DATABASE = "create table " + TABLE_NAME + "(" +
            "_id integer primary key autoincrement," +
            "name text,"+
            "password text,"+
            "phone text)";

    public UserDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabases) {
        sqLiteDatabases.execSQL(CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int il) {

    }

    //注册
    public Boolean insertToDB(String name, String password, String phone) {
        if (sqLiteDatabase == null) {
            sqLiteDatabase = getWritableDatabase();
        }
        //判断用户名是否重复， 若重复，则返回false,代表注册失败
        if (query(name)) {
            return false;
        }
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("phone", phone);
        cv.put("password", password);
        sqLiteDatabase.insert(TABLE_NAME, null, cv);
        return true;
    }

    //更新
    public void updateToDB(UserInfo userInfo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("phone", userInfo.getPhone());
        cv.put("name", userInfo.getName());
        cv.put("password", userInfo.getPassword());
        String whereClause = "_id = " + userInfo.getId();
        db.update(TABLE_NAME, cv, "_id = ?", new String[] {userInfo.getId() + ""});
    }

    //判断用户是否唯一
    public Boolean query(String name) {
        if (sqLiteDatabase == null) {
            sqLiteDatabase = getWritableDatabase();
        }
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, null, "name = ?", new String[] {name}, null, null, null, null);
        if (cursor.getCount() > 0) {
            return true;
        }
        else {
            return false;
        }
    }
    //根据用户名获取用户信息
    public UserInfo getByName(String name) {
        if (sqLiteDatabase == null) {
            sqLiteDatabase = getWritableDatabase();
        }

        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, null, "name = ?", new String[] {name}, null, null, null, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                UserInfo userInfo = new UserInfo(cursor.getInt(cursor.getColumnIndex("_id")),
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("password")),
                        cursor.getString(cursor.getColumnIndex("phone")));
                return userInfo;
            }
            else {
                return null;
            }
        }
        else {
            return null;
        }
    }
    //根据用户名和密码来查找用户，登录时候要用
    public UserInfo query(String name, String password) {
        if (sqLiteDatabase == null) {
            sqLiteDatabase = getWritableDatabase();
        }

        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, null, "name = ? and password = ?", new String[] {name, password}, null, null, null, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                UserInfo userInfo = new UserInfo(cursor.getInt(cursor.getColumnIndex("_id")),
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("password")),
                        cursor.getString(cursor.getColumnIndex("phone")));
                return userInfo;
            }
            else {
                return null;
            }
        }
        else {
            return null;
        }
    }

    //获取用户列表
    public List<UserInfo> getData() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, new String[]{"name", "_id", "birth", "gift"}, null, null, null, null, null);
        List<UserInfo> list = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                UserInfo birthInfo = new UserInfo(cursor.getInt(cursor.getColumnIndex("_id")),
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("password")),
                        cursor.getString(cursor.getColumnIndex("phone")));
                list.add(birthInfo);
            } while(cursor.moveToNext());
        }
        return list;
    }

    //删除用户
    public void delete(int id) {
        if (sqLiteDatabase == null) {
            sqLiteDatabase = getWritableDatabase();
        }
        sqLiteDatabase.delete(TABLE_NAME, "_id = ?", new String[] {id+""});
    }
}
