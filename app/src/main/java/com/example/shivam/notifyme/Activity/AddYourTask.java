package com.example.shivam.notifyme.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
    Date fromDate, toDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_your_task);

        //task
        editTask = findViewById(R.id.editTask);

        //spinner
        spin = findViewById(R.id.simple_spinner);
        spin.setOnItemSelectedListener(AddYourTask.this);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, taskNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(arrayAdapter);
        taskTypeSelected = spin.getSelectedItem().toString();

        //time picker
        timePickerNotifyAt = findViewById(R.id.timepickerNotifyAt);
        timePickerNotifyAt.setIs24HourView(false); // used to display AM/PM mode
        // perform set on time changed listener event
        timePickerNotifyAt.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) { 
                
            }
        });

        calendarViewFrom = findViewById(R.id.simpleCalendarViewFrom);
        calendarViewTo = findViewById(R.id.simpleCalendarViewTo);

        //calendar
        buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                taskNameInput = editTask.getText().toString();
                if(TextUtils.isEmpty(taskNameInput))
                {
                    Toast.makeText(getApplicationContext(),"Task Name is missing",Toast.LENGTH_SHORT).show();
                    return;
                }
                hourOfDay = timePickerNotifyAt.getCurrentHour();
                minute = timePickerNotifyAt.getCurrentMinute();

                Calendar calendar = Calendar.getInstance();
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

            Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_SHORT).show();
            insertData();
            finish();
            }
        }
        );

        makeItAHabit = findViewById(R.id.checkboxMakeItAHabit);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private void insertData()
    {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        taskNameInput = editTask.getText().toString();
        taskTypeSelected = spin.getSelectedItem().toString();
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

        long rowId = sqLiteDatabase.insert(TaskContract.TaskEntry.TABLE_NAME, null, contentValues);
    }
}
