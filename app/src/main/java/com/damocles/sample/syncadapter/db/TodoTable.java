package com.damocles.sample.syncadapter.db;

import com.damocles.android.base.Constants;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by zhanglong02 on 16/3/10.
 */
public class TodoTable implements IDatabaseTable {

    public static final String TABLE_NAME = "todos";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_SERVER_ID = "server_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_STATUS_FLAG = "status_flag";

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(Constants.LOG_TAG, "create table " + TABLE_NAME);
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_SERVER_ID + " INTEGER,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_STATUS_FLAG + " INTEGER"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(Constants.LOG_TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(Constants.LOG_TAG, "Downgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public static long insert(SQLiteDatabase db, ContentValues values) {
        return db.insert(TABLE_NAME, TodoTable.COLUMN_TITLE, values);
    }

    public static int delete(SQLiteDatabase db, String whereClause, String[] whereArgs) {
        return db.delete(TABLE_NAME, whereClause, whereArgs);
    }

    public static int update(SQLiteDatabase db, ContentValues values, String whereClause, String[] whereArgs) {
        return db.update(TABLE_NAME, values, whereClause, whereArgs);
    }
}
