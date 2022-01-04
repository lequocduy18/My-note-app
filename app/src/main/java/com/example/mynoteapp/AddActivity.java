package com.example.mynoteapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity {

    EditText title_input, content_input;
    Button add_button;
    private NotificationManagerCompat notificationManagerCompat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        title_input = findViewById(R.id.title_input);
        content_input = findViewById(R.id.content_input);
        add_button = findViewById(R.id.add_button);
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
}
