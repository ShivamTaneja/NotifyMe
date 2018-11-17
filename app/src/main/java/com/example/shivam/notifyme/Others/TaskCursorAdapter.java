package com.example.shivam.notifyme.Others;

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
    public void bindView(View view, Context context, Cursor cursor) {

        TextView taskname = view.findViewById(R.id.taskname);
        TextView tasktype = view.findViewById(R.id.tasktype);

        int tasknameColumnIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_NAME);
        int tasktypeColumnIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_TYPE);

        String tasknamestring = cursor.getString(tasknameColumnIndex);
        String tasktypestring = cursor.getString(tasktypeColumnIndex);

        taskname.setText(tasknamestring);
        tasktype.setText(tasktypestring);

    }
}
