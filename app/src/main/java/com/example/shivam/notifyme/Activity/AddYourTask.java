package com.example.shivam.notifyme.Activity;

import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.example.shivam.notifyme.R;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

public class AddYourTask extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String CHANNEL_ID = "task_notifications";
    private static final int NOTIFICATION_ID = 001;
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
        int hourOfDay = timePickerNotifyAt.getCurrentHour();
        int minute = timePickerNotifyAt.getCurrentMinute();


        //calendar
        Date currentTime = calendar.getInstance().getTime();
        int currentHours = currentTime.getHours();
        int currentMinutes = currentTime.getMinutes();
        
        
        calendarViewFrom = findViewById(R.id.simpleCalendarViewFrom);
        calendarViewTo = findViewById(R.id.simpleCalendarViewTo);

        buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, timePickerNotifyAt.getCurrentHour());
                calendar.set(Calendar.MINUTE, timePickerNotifyAt.getCurrentMinute());
                Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        );

        makeItAHabit = findViewById(R.id.checkboxMakeItAHabit);

        checkConditionForNotification(currentHours, currentMinutes, hourOfDay, minute);
        
}

    private void checkConditionForNotification(int currentHours, int currentMinutes, int hourOfDay, int minute)
    {
      if(currentHours == hourOfDay && currentMinutes == minute)
      {
          Log.d("twitter", currentHours + " " + hourOfDay + " " + currentMinutes + " " + minute + " " + taskNameInput);
          NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                  .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                  .setContentTitle(taskNameInput)
                  .setContentText(taskTypeSelected)
                  .setPriority(NotificationCompat.PRIORITY_DEFAULT);

          NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
          notificationManagerCompat.notify(NOTIFICATION_ID ,mBuilder.build());
      }
    }

/*    
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // Create an explicit intent for an Activity in your app
    Intent intent = new Intent(this, AlertDetails.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
            .setContentTitle("My notification")
            .setContentText("Hello World!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setAutoCancel(true);

    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

    // notificationId is a unique int for each notification that you must define
    notificationManager.notify(notificationId, mBuilder.build());*/




    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
