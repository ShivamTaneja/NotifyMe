package com.example.shivam.notifyme.Data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class TaskProvider extends ContentProvider {

    private DatabaseHelper databaseHelper;
    private static final int task = 100;
    private static final int task_id = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(TaskContract.CONTENT_AUTHORITY, TaskContract.PATH_PRODUCT_INFORMATION, task);
        sUriMatcher.addURI(TaskContract.CONTENT_AUTHORITY, TaskContract.PATH_PRODUCT_INFORMATION + "/#", task_id);
    }

    @Override
    public boolean onCreate() {
        databaseHelper = new DatabaseHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] strings,@Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        return null;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
