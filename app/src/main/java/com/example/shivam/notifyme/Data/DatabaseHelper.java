package com.example.shivam.notifyme.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME="Task.db";
    private final static Integer DATABASE_VERSION = 2;

    String SQL_CREATE_ENTRIES = "CREATE TABLE " + TaskContract.TaskEntry.TABLE_NAME +  " ( "
            + TaskContract.TaskEntry.COLUMN_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TaskContract.TaskEntry.COLUMN_TASK_NAME + " TEXT NOT NULL, "
            + TaskContract.TaskEntry.COLUMN_TASK_TYPE + " TEXT NOT NULL, "
            + TaskContract.TaskEntry.COLUMN_TASk_STARTING_DATE + " TEXT, "
            + TaskContract.TaskEntry.COLUMN_TASk_ENDING_DATE + " TEXT, "
            + TaskContract.TaskEntry.COLUMN_TASk_NOTIFICATION_TIME_HOUR + " INTEGER NOT NULL, "
            + TaskContract.TaskEntry.COLUMN_TASk_NOTIFICATION_TIME_MINUTE + " INTEGER NOT NULL, "
            + TaskContract.TaskEntry.COLUMN_TASk_MAKE_IT_A_HABIT + " BOOLEAN, "
            + TaskContract.TaskEntry.COLUMN_TASk_NUMBER_OF_DAYS_PERFORMED + " INTEGER NOT NULL, "
            + TaskContract.TaskEntry.COLUMN_TASk_TOTAL_NUMBER_OF_DAYS + " INTEGER NOT NULL );" ;

    String SQL_DELETE_ENTRIES = "DROP TABLE " + TaskContract.TaskEntry.TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
