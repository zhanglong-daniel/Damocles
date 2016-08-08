package com.damocles.sample.syncadapter.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zhanglong02 on 16/3/10.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "damocles_syncadapter.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        List<IDatabaseTable> tables = new ArrayList<IDatabaseTable>();
        initTables(tables);
        for (IDatabaseTable table : tables) {
            table.onCreate(db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    private void initTables(List<IDatabaseTable> tables) {
        tables.add(new TodoTable());
    }
}
