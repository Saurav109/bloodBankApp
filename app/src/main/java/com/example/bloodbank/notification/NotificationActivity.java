package com.example.bloodbank.notification;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.bloodbank.R;

public class NotificationActivity extends AppCompatActivity {
    Notification notification;
    FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_activity);
        notification=new Notification();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.notification_frame, notification, "notification");
        fragmentTransaction.commit();
    }
}
