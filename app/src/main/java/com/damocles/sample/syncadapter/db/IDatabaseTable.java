package com.damocles.sample.syncadapter.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by zhanglong02 on 16/3/10.
 */
interface IDatabaseTable {

    void onCreate(SQLiteDatabase db);

    void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);

    void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion);
}
