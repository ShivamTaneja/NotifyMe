package com.example.shivam.notifyme.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.shivam.notifyme.R;

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
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase msqLiteDatabase = databaseHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor cursor;
        switch (match)
        {
            case task:
                cursor = msqLiteDatabase.query(TaskContract.TaskEntry.TABLE_NAME, projection, selection,
                        null,null, null, sortOrder );
                break;
            case task_id:
                selection = TaskContract.TaskEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = msqLiteDatabase.query(TaskContract.TaskEntry.TABLE_NAME, projection,
                        selection, selectionArgs ,null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query illegal Uri " + uri );
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case task:
                return TaskContract.TaskEntry.CONTENT_LIST_TYPE;
            case task_id:
                return TaskContract.TaskEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch(match)
        {
            case task:
                return insert_data(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported with uri " + uri);
        }
    }

    private Uri insert_data(Uri uri, ContentValues contentValues) {

        String taskName = contentValues.getAsString(TaskContract.TaskEntry.COLUMN_TASK_NAME);
        if( TextUtils.isEmpty(taskName))
        {
            Toast.makeText(getContext(), R.string.Task_Name_is_missing, Toast.LENGTH_SHORT).show();
            return null;
        }

        String taskType = contentValues.getAsString(TaskContract.TaskEntry.COLUMN_TASK_TYPE);
        if( TextUtils.isEmpty(taskType))
        {
            Toast.makeText(getContext(), R.string.Task_Type_is_missing, Toast.LENGTH_SHORT).show();
            return null;
        }

        SQLiteDatabase msqLiteDatabase = databaseHelper.getWritableDatabase();
        long id = msqLiteDatabase.insert(TaskContract.TaskEntry.TABLE_NAME,null,contentValues);
        if (id == -1) {
            Log.e("twitter", "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int del_count=0;
        switch(match)
        {
            case task:
                del_count = sqLiteDatabase.delete(TaskContract.TaskEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case task_id:
                selection = TaskContract.TaskEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                del_count = sqLiteDatabase.delete(TaskContract.TaskEntry.TABLE_NAME, selection,selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported with uri " + uri );
        }
        if(del_count > 0)
            getContext().getContentResolver().notifyChange(uri , null);
        return del_count;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case task:
                return update_data(uri, contentValues, selection, selectionArgs);
            case task_id:
                selection = TaskContract.TaskEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return update_data(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Updation is not supported for uri " + uri);
        }
    }

    private int update_data(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        if(contentValues.size() == 0)
            return 0;
        if(contentValues.containsKey(TaskContract.TaskEntry.COLUMN_TASK_NAME))
        {
            String taskName = contentValues.getAsString(TaskContract.TaskEntry.COLUMN_TASK_NAME);
            if(TextUtils.isEmpty(taskName))
            {
                Toast.makeText(getContext(), R.string.Task_Name_is_missing, Toast.LENGTH_SHORT).show();
                return -1;
            }
        }
        if(contentValues.containsKey(TaskContract.TaskEntry.COLUMN_TASK_TYPE))
        {
            String taskType = contentValues.getAsString(TaskContract.TaskEntry.COLUMN_TASK_TYPE);
            if(TextUtils.isEmpty(taskType))
            {
                Toast.makeText(getContext(), R.string.Task_Type_is_missing, Toast.LENGTH_SHORT).show();
                return -1;
            }
        }

        SQLiteDatabase msqLiteDatabase = databaseHelper.getWritableDatabase();
        int id = msqLiteDatabase.update(TaskContract.TaskEntry.TABLE_NAME, contentValues,
                selection, selectionArgs);
        if (id == -1) {
            Log.e("twitter", "Failed to update row for " + uri);
            return 0;
        }
        if(id>0)
            getContext().getContentResolver().notifyChange(uri, null);
        return id;
    }
}
