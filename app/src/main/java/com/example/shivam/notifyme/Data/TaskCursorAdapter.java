package com.example.shivam.notifyme.Data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.shivam.notifyme.Data.TaskContract;
import com.example.shivam.notifyme.R;

public class TaskCursorAdapter extends CursorAdapter {

    public TaskCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup,false);
    }

    @Override
    public void bindView(View view,final Context context,final Cursor cursor) {

        TextView taskname = view.findViewById(R.id.taskname);
        TextView tasktype = view.findViewById(R.id.tasktype);
        TextView notifytime = view.findViewById(R.id.notifytime);

        int tasknameColumnIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_NAME);
        int tasktypeColumnIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_TYPE);
        int notifytimehours = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASk_NOTIFICATION_TIME_HOUR);
        int notifytimeminutes = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASk_NOTIFICATION_TIME_MINUTE);

        String taskNameString = cursor.getString(tasknameColumnIndex);
        String taskTypeString = cursor.getString(tasktypeColumnIndex);
        String notifyTimeHoursString = cursor.getString(notifytimehours);
        String notifyTimeMinutesString = cursor.getString(notifytimeminutes);

        String time = notifyTimeHoursString + ":" + notifyTimeMinutesString;
        taskname.setText(taskNameString);
        tasktype.setText(taskTypeString);
        notifytime.setText(time);

    }
}
