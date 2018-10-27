package com.example.shivam.notifyme;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.Calendar;

public class AddYourTask extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText editTask; // task name
    Spinner spin; // spinner for type of task and their values are stored in taskNames;
    CalendarView calendarViewFrom; // to choose dates from
    CalendarView calendarViewTo; // to choose dates upto
    TimePicker timePickerNotifyAt;// time at which notification will appear
    CheckBox makeItAHabit;
    Button buttonSave;

    String[] taskNames = {"Health & fitness", "Study", "Work", "Meeting", "Shopping", "Entertainment", "Relax", "Travel", "Family Time", "Others"};
    String taskNameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_your_task);

        //task
        editTask = findViewById(R.id.editTask);
        taskNameInput = editTask.getText().toString();

        //spinner
        spin = findViewById(R.id.simple_spinner);
        spin.setOnItemSelectedListener(AddYourTask.this);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, taskNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(arrayAdapter);

        //time picker
        timePickerNotifyAt = findViewById(R.id.timepickerNotifyAt);
        timePickerNotifyAt.setIs24HourView(false); // used to display AM/PM mode
        // perform set on time changed listener event
        timePickerNotifyAt.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

            }
        });

        //calendar
        calendarViewFrom = findViewById(R.id.simpleCalendarViewFrom);
        calendarViewTo = findViewById(R.id.simpleCalendarViewTo);

        buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, timePickerNotifyAt.getCurrentHour());
                calendar.set(Calendar.MINUTE, timePickerNotifyAt.getCurrentMinute());
                Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_SHORT).show();
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
}
