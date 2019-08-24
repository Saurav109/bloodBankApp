package com.example.bloodbank.notification;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodbank.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationDialog extends Activity {
    TextView  location, comment,  time;
    String uid,pushId;
    private SimpleDateFormat formatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_dialoge);
        setFinishOnTouchOutside(false);

        comment=findViewById(R.id.notification_comment);
        time=findViewById(R.id.notification_time);
        location=findViewById(R.id.notification_location);

        uid=getIntent().getStringExtra("uid");
        pushId=getIntent().getStringExtra("pushId");

        comment.setText(getIntent().getStringExtra("comment"));

        formatter = new SimpleDateFormat(" HH:mm  dd/MM/yyyy");
        String dateString = formatter.format(new Date(Long.valueOf(getIntent().getStringExtra("time"))));
        time.setText( dateString);
        location.setText(getIntent().getStringExtra("location"));
    }


    public void openNotification(View view) {
        Intent intent = new Intent(getApplicationContext(), Notification.class);
        startActivity(intent);
    }

    public void closeDialog(View view) {
        finish();
    }
}
