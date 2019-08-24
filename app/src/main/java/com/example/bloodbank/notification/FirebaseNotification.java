package com.example.bloodbank.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.bloodbank.R;
import com.example.bloodbank.searchBlood.SearchBloodActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseNotification extends FirebaseMessagingService {
    String TAG = "FirebaseNotification";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String bloodGroup=remoteMessage.getData().get("bloodGroup");
        String comment=remoteMessage.getData().get("comment");
        String location=remoteMessage.getData().get("location");
        String mobileNo=remoteMessage.getData().get("mobileNo");
        String time=remoteMessage.getData().get("time");
        String uid=remoteMessage.getData().get("uid");
        String name=remoteMessage.getData().get("name");
        String pushId=remoteMessage.getData().get("pushId");

        Log.d(TAG, "onMessageReceived");
        Intent notificationDialog=new Intent(this, NotificationDialog.class);
        notificationDialog.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        notificationDialog.putExtra("name",name);
        notificationDialog.putExtra("location",location);
        notificationDialog.putExtra("time",time);
        notificationDialog.putExtra("uid",uid);
        notificationDialog.putExtra("mobileNo",mobileNo);
        notificationDialog.putExtra("bloodGroup",bloodGroup);
        notificationDialog.putExtra("comment",comment);
        notificationDialog.putExtra("pushId",pushId);


        startActivity(notificationDialog);
        noti(location,comment);
    }


    void noti(String title,String msg){
        NotificationCompat.Builder mBuilder =   new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle(title) // title for notification
                .setContentText(msg) // message for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(getApplicationContext(), Notification.class);
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(),0,intent,0);
        mBuilder.setContentIntent(pi);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }

}
