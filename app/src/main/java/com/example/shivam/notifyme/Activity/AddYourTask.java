package com.example.shivam.notifyme.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddYourTask extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText editTask; // task name
    Spinner spin; // spinner for type of task and their values are stored in taskNames;
    CalendarView calendarViewFrom; // to choose dates from
    CalendarView calendarViewTo; // to choose dates upto
    TimePicker timePickerNotifyAt;// time at which notification will appear
    CheckBox makeItAHabit;
    Button buttonSave;
    Calendar calendar;

    String[] taskNames = {"Health & fitness", "Study", "Work", "Meeting", "Shopping", "Entertainment", "Relax", "Travel", "Family Time", "Others"};
    String taskNameInput;
    String taskTypeSelected;
    int hourOfDay, minute;
    boolean makeItAHabitIsChecked = false;
    String fromDateString, toDateString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_your_task);

        editTask = findViewById(R.id.editTask);
        spin = findViewById(R.id.simple_spinner);
        calendarViewFrom = findViewById(R.id.simpleCalendarViewFrom);
        calendarViewTo = findViewById(R.id.simpleCalendarViewTo);
        timePickerNotifyAt = findViewById(R.id.timepickerNotifyAt);
        makeItAHabit = findViewById(R.id.checkboxMakeItAHabit);
        buttonSave = findViewById(R.id.buttonSave);
        calendar = Calendar.getInstance();

        spin.setOnItemSelectedListener(AddYourTask.this);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, taskNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(arrayAdapter);

        calendarViewFrom.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                int correctMonth = month + 1;
                fromDateString = String.valueOf(year + "-" + correctMonth + "-" + dayOfMonth);
            }
        });
        calendarViewTo.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                int correctMonth = month + 1;
                toDateString = String.valueOf(year + "-" + correctMonth + "-" + dayOfMonth);
            }
        });

        timePickerNotifyAt.setIs24HourView(false); // used to display AM/PM mode

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
                insertData();
            }
        }
        );
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private void insertData()
    {
        taskNameInput = editTask.getText().toString();
        if(TextUtils.isEmpty(taskNameInput)) {
            Toast.makeText(getApplicationContext(), R.string.Task_Name_is_missing, Toast.LENGTH_SHORT).show();
            return;
        }
        taskTypeSelected = spin.getSelectedItem().toString();
        hourOfDay = timePickerNotifyAt.getCurrentHour();
        minute = timePickerNotifyAt.getCurrentMinute();
        makeItAHabitIsChecked =  makeItAHabit.isChecked();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TaskContract.TaskEntry.COLUMN_TASK_NAME, taskNameInput);
        contentValues.put(TaskContract.TaskEntry.COLUMN_TASK_TYPE, taskTypeSelected);
        contentValues.put(TaskContract.TaskEntry.COLUMN_TASk_STARTING_DATE, fromDateString);
        contentValues.put(TaskContract.TaskEntry.COLUMN_TASk_ENDING_DATE, toDateString);
        contentValues.put(TaskContract.TaskEntry.COLUMN_TASk_NOTIFICATION_TIME_HOUR, hourOfDay);
        contentValues.put(TaskContract.TaskEntry.COLUMN_TASk_NOTIFICATION_TIME_MINUTE, minute);
        contentValues.put(TaskContract.TaskEntry.COLUMN_TASk_MAKE_IT_A_HABIT, makeItAHabitIsChecked);
        contentValues.put(TaskContract.TaskEntry.COLUMN_TASk_NUMBER_OF_DAYS_PERFORMED, 0);
        contentValues.put(TaskContract.TaskEntry.COLUMN_TASk_TOTAL_NUMBER_OF_DAYS, 365);

        //long rowId = sqLiteDatabase.insert(TaskContract.TaskEntry.TABLE_NAME, null, contentValues);
        Uri uri = getContentResolver().insert(TaskContract.TaskEntry.CONTENT_URI, contentValues);
        if (uri == null)
            Toast.makeText(getApplicationContext(), "Data did not inserted", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), "Data inserted", Toast.LENGTH_SHORT).show();

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.insert_product_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.insert_data:
                insertData();
                break;
            case R.id.discard_changes:
                finish();
                break;
            default:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
