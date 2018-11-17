package com.example.shivam.notifyme.Data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class TaskContract {

      static final String CONTENT_AUTHORITY = "com.example.shivam.notifyme";
      private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
      static final String PATH_PRODUCT_INFORMATION = "TaskInformation";

    private TaskContract() {
    }

    public final static class TaskEntry implements BaseColumns {

        public static final String TABLE_NAME = "TaskInformation";
        public static final String COLUMN_TASK_ID = BaseColumns._ID;
        public static final String COLUMN_TASK_NAME = "TaskName";
        public static final String COLUMN_TASK_TYPE = "TaskType";
        public static final String COLUMN_TASk_STARTING_DATE = "FromDate";
        public static final String COLUMN_TASk_ENDING_DATE = "ToDate";
        public static final String COLUMN_TASk_NOTIFICATION_TIME_HOUR = "NotifyTimeHour";
        public static final String COLUMN_TASk_NOTIFICATION_TIME_MINUTE = "NotifyTimeMinute";
        public static final String COLUMN_TASk_MAKE_IT_A_HABIT = "MakeItAHabit";
        public static final String COLUMN_TASk_NUMBER_OF_DAYS_PERFORMED = "NumberOfDaysTaskPerformed";
        public static final String COLUMN_TASk_TOTAL_NUMBER_OF_DAYS = "TotalNumberOfDays";


        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCT_INFORMATION);
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCT_INFORMATION;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCT_INFORMATION;

        }
}