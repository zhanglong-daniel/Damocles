package com.damocles.sample.syncadapter;

import java.util.HashMap;

import com.damocles.sample.syncadapter.db.DatabaseHelper;
import com.damocles.sample.syncadapter.db.TodoTable;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by zhanglong02 on 16/3/10.
 */
public class TodoContentProvider extends ContentProvider {

    private static final int TODO = 1;
    private static final int TODO_ID = 2;

    public static final String DEFAULT_SORT_ORDER = "_id ASC";

    private static final UriMatcher sUriMatcher;

    public static final String AUTHORITY = TodoContentProvider.class
            .getCanonicalName();

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.damocles.todo";
    public static final String CONTENT_TYPE_ID = "vnd.android.cursor.item/vnd.damocles.todo";

    public static final Uri CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + TodoTable.TABLE_NAME);

    private DatabaseHelper databaseHelper;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, TodoTable.TABLE_NAME, TODO);
        sUriMatcher.addURI(AUTHORITY, TodoTable.TABLE_NAME + "/#", TODO_ID);
    }

    @Override
    public boolean onCreate() {
        databaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case TODO:
                return CONTENT_TYPE;
            case TODO_ID:
                return CONTENT_TYPE_ID;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        HashMap<String, String> projectionMap = new HashMap<String, String>();
        projectionMap.put(TodoTable.COLUMN_ID, TodoTable.COLUMN_ID);
        projectionMap.put(TodoTable.COLUMN_SERVER_ID, TodoTable.COLUMN_SERVER_ID);
        projectionMap.put(TodoTable.COLUMN_TITLE, TodoTable.COLUMN_TITLE);
        projectionMap.put(TodoTable.COLUMN_STATUS_FLAG, TodoTable.COLUMN_STATUS_FLAG);

        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        sqLiteQueryBuilder.setTables(TodoTable.TABLE_NAME);
        sqLiteQueryBuilder.setProjectionMap(projectionMap);
        switch (sUriMatcher.match(uri)) {
            case TODO:
                break;
            case TODO_ID:
                sqLiteQueryBuilder.appendWhere(TodoTable.COLUMN_ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new RuntimeException("Unknown URI");
        }
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor c = sqLiteQueryBuilder.query(db, projection, selection, selectionArgs, null,
                null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }
        switch (sUriMatcher.match(uri)) {
            case TODO:
                break;
            default:
                new RuntimeException("Invalid URI for inserting: " + uri);
        }
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long rowId = TodoTable.insert(db, values);
        if (rowId > 0) {
            Uri noteUri = ContentUris.withAppendedId(uri, rowId);
            getContext().getContentResolver().notifyChange(noteUri, null);
            return noteUri;
        }

        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
            case TODO:
                count = TodoTable.delete(db, selection, selectionArgs);
                break;
            case TODO_ID:
                String id = uri.getPathSegments().get(1);
                selection = TodoTable.COLUMN_ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")"
                                   : "");
                count = TodoTable.delete(db, selection, selectionArgs);
                break;
            default:
                throw new RuntimeException("Unkown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
            case TODO:
                count = TodoTable.update(db, values, selection, selectionArgs);
                break;
            case TODO_ID:
                count = TodoTable.update(db, values,
                        TodoTable.COLUMN_ID + "=" + uri.getPathSegments().get(1)
                                + (!TextUtils.isEmpty(selection) ? " AND ("
                                + selection + ")" : ""), selectionArgs);
                break;
            default:
                throw new RuntimeException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

}
