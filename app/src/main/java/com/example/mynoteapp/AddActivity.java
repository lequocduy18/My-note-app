package com.example.mynoteapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity {
    EditText title_input, content_input;
    Button add_button,selectTime_button, setAlarm_button, cancelAlarm_button;
    TextView selectedTime;

    private NotificationManagerCompat notificationManagerCompat;
    private MaterialTimePicker picker;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        title_input = findViewById(R.id.title_input);
        content_input = findViewById(R.id.content_input);
        add_button = findViewById(R.id.add_button);
        selectTime_button = findViewById(R.id.selectedTimeBtn);
        setAlarm_button = findViewById(R.id.setAlarmBtn);
        cancelAlarm_button = findViewById(R.id.cancelAlarmBtn);
        selectedTime = findViewById(R.id.selectedTime);
        notificationManagerCompat = NotificationManagerCompat.from(this);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(AddActivity.this);
                myDB.addNote(title_input.getText().toString().trim(),
                        content_input.getText().toString().trim());
                sendOnChannel1(view);
            }
        });

        selectTime_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        setAlarm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarm();
            }
        });

        cancelAlarm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm();
            }
        });
    }

    public void sendOnChannel1(View v){
        String title = title_input.getText().toString();
        android.app.Notification notification = new NotificationCompat.Builder(this, MyNotification.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentTitle("Notification title: " + title)
                .setContentText("Add success")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        notificationManagerCompat.notify(1, notification);
    }

    public void showTimePicker() {
        picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select alarm time")
                .build();

        picker.show(getSupportFragmentManager(), "");
        picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(picker.getHour() > 12){
                    selectedTime.setText(
                            String.format("%02d",(picker.getHour()-12))+" : "+String.format("%02d",picker.getMinute())+" PM"
                    );
                } else {
                    selectedTime.setText(picker.getHour() + " : " + picker.getMinute() + " AM");
                }

                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, picker.getHour());
                calendar.set(Calendar.MINUTE, picker.getMinute());
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
            }
        });


    }

    public void setAlarm() {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0 ,intent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                pendingIntent);
        Toast.makeText(this, "Alarm set Successfully", Toast.LENGTH_SHORT).show();
    }

    public void cancelAlarm() {
        Intent intent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0 ,intent, 0);
        if(alarmManager == null){
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        }
        alarmManager.cancel(pendingIntent);
        Toast.makeText(this, "Alarm cancel", Toast.LENGTH_SHORT).show();
    }
}
