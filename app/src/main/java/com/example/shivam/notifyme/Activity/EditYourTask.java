package com.example.shivam.notifyme.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.shivam.notifyme.Data.DatabaseHelper;
import com.example.shivam.notifyme.Data.TaskContract;
import com.example.shivam.notifyme.Others.NotificationReceiver;
import com.example.shivam.notifyme.R;

import java.util.Calendar;
import java.util.Date;

public class EditYourTask extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemSelectedListener {

    EditText editTask; // task name
    Spinner spin; // spinner for type of task and their values are stored in taskNames;
    CalendarView calendarViewFrom; // to choose dates from
    CalendarView calendarViewTo; // to choose dates upto
    TimePicker timePickerNotifyAt;// time at which notification will appear
    CheckBox makeItAHabit;
    Button buttonSave;
    Calendar calendar;

    String[] taskNames = {"Health & fitness", "Study", "Work", "Meeting", "Shopping", "Entertainment", "Relax", "Travel", "Family Time", "Others"};
    ArrayAdapter arrayAdapter;
    String taskNameInput;
    String taskTypeSelected;
    int hourOfDay, minute;
    boolean makeItAHabitIsChecked = false;
    long fromDate;
    long toDate;

    Uri currentUri;
    private static final int EXISTING_LOADER = 0;
    private boolean dataHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            dataHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_edit_your_task);

            editTask = findViewById(R.id.editTask);
            spin = findViewById(R.id.simple_spinner);
            calendarViewFrom = findViewById(R.id.simpleCalendarViewFrom);
            calendarViewTo = findViewById(R.id.simpleCalendarViewTo);
            timePickerNotifyAt = findViewById(R.id.timepickerNotifyAt);
            makeItAHabit = findViewById(R.id.checkboxMakeItAHabit);
            buttonSave = findViewById(R.id.buttonSave);
            calendar = Calendar.getInstance();


            spin.setOnItemSelectedListener(EditYourTask.this);
            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, taskNames);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin.setAdapter(arrayAdapter);

            timePickerNotifyAt.setIs24HourView(false); // used to display AM/PM mode

            //calendar
            buttonSave = findViewById(R.id.buttonSave);
            buttonSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

            hourOfDay = timePickerNotifyAt.getCurrentHour();
            minute = timePickerNotifyAt.getCurrentMinute();
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND,1);

            Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
            intent.setAction("Task");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
              100, intent, PendingIntent.FLAG_UPDATE_CURRENT );
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
              AlarmManager.INTERVAL_DAY, pendingIntent);
            save_changes();
            }
        }
        );

        Intent intent = getIntent();
        currentUri = intent.getData();

        getSupportLoaderManager().initLoader(EXISTING_LOADER, null, this);

        editTask.setOnTouchListener(mTouchListener);
        spin.setOnTouchListener(mTouchListener);
        calendarViewFrom.setOnTouchListener(mTouchListener);
        calendarViewTo.setOnTouchListener(mTouchListener);
        timePickerNotifyAt.setOnTouchListener(mTouchListener);
        makeItAHabit.setOnTouchListener(mTouchListener);
    }

    @Override
    public void onBackPressed() {
        if (!dataHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };

        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private void save_changes()
    {
        taskNameInput = editTask.getText().toString();
        if(TextUtils.isEmpty(taskNameInput)) {
            Toast.makeText(getApplicationContext(), R.string.Task_Name_is_missing, Toast.LENGTH_SHORT).show();
            return;
        }
        taskTypeSelected = spin.getSelectedItem().toString();
        fromDate = calendarViewFrom.getDate();
        toDate = calendarViewTo.getDate();
        hourOfDay = timePickerNotifyAt.getCurrentHour();
        minute = timePickerNotifyAt.getCurrentMinute();
        makeItAHabitIsChecked =  makeItAHabit.isChecked();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TaskContract.TaskEntry.COLUMN_TASK_NAME, taskNameInput);
        contentValues.put(TaskContract.TaskEntry.COLUMN_TASK_TYPE, taskTypeSelected);
        contentValues.put(TaskContract.TaskEntry.COLUMN_TASk_STARTING_DATE, String.valueOf(calendarViewFrom.getDate()));
        contentValues.put(TaskContract.TaskEntry.COLUMN_TASk_ENDING_DATE, String.valueOf(calendarViewTo.getDate()));
        contentValues.put(TaskContract.TaskEntry.COLUMN_TASk_NOTIFICATION_TIME_HOUR, hourOfDay);
        contentValues.put(TaskContract.TaskEntry.COLUMN_TASk_NOTIFICATION_TIME_MINUTE, minute);
        contentValues.put(TaskContract.TaskEntry.COLUMN_TASk_MAKE_IT_A_HABIT, makeItAHabitIsChecked);
        contentValues.put(TaskContract.TaskEntry.COLUMN_TASk_NUMBER_OF_DAYS_PERFORMED, 0);
        contentValues.put(TaskContract.TaskEntry.COLUMN_TASk_TOTAL_NUMBER_OF_DAYS, 365);

        int id = getContentResolver().update(currentUri, contentValues, null, null);
        if (id == -1)
            Toast.makeText(getApplicationContext(), R.string.data_not_changed, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), R.string.data_changed_successfully, Toast.LENGTH_SHORT).show();

        finish();
    }

    private void delete_record(){
        int id = getContentResolver().delete(currentUri, null, null);
        if (id == -1) {
            Toast.makeText(this, R.string.editor_delete_data_failed, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.editor_delete_data_successful, Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new CursorLoader(this, currentUri, null,null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (cursor.moveToFirst()) {

            int tasknameColumnIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_NAME);
            int tasktypeColumnIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_TYPE);
            int fromDateColumnIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASk_STARTING_DATE);
            int toDateColumnIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASk_ENDING_DATE);
            int notifytimehours = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASk_NOTIFICATION_TIME_HOUR);
            int notifytimeminutes = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASk_NOTIFICATION_TIME_MINUTE);
            int makeItAhabitColumnIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASk_MAKE_IT_A_HABIT);

            taskNameInput = cursor.getString(tasknameColumnIndex);
            taskTypeSelected = cursor.getString(tasktypeColumnIndex);
            fromDate = cursor.getLong(fromDateColumnIndex);
            toDate = cursor.getLong(toDateColumnIndex);
            hourOfDay = cursor.getInt(notifytimehours);
            minute = cursor.getInt(notifytimeminutes);
            makeItAHabitIsChecked = Boolean.parseBoolean(cursor.getString(makeItAhabitColumnIndex));

            editTask.setText(taskNameInput);
            int spinnerPosition =  arrayAdapter.getPosition(taskTypeSelected);
            spin.setSelection(spinnerPosition);
            calendarViewFrom.setDate(fromDate);
            calendarViewTo.setDate(toDate);
            timePickerNotifyAt.setCurrentHour(hourOfDay);
            timePickerNotifyAt.setCurrentMinute(minute);
            makeItAHabit.setChecked(makeItAHabitIsChecked);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        editTask.setText("");
    }

    private void showUnsavedChangesDialog( DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete the data?");
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                delete_record();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_screen_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.saveChanges:
                save_changes();
                break;
            case R.id.discardChanges:
                finish();
                break;
            case R.id.deleteRecord:
                showDeleteConfirmationDialog();
            default:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
