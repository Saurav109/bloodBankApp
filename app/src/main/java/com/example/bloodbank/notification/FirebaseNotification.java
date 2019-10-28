package com.example.bloodbank.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.bloodbank.Helper;
import com.example.bloodbank.R;
import com.example.bloodbank.profile.ProfileValueModel;
import com.example.bloodbank.searchBlood.SearchBloodActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseNotification extends FirebaseMessagingService {
    String TAG = "FirebaseNotification";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        final String bloodGroup=remoteMessage.getData().get("bloodGroup");
        final String comment=remoteMessage.getData().get("comment");
        final String location=remoteMessage.getData().get("location");
        final String mobileNo=remoteMessage.getData().get("mobileNo");
        final String time=remoteMessage.getData().get("time");
        final String uid=remoteMessage.getData().get("uid");
        final String name=remoteMessage.getData().get("name");
        final String pushId=remoteMessage.getData().get("pushId");

        Log.d(TAG, "onMessageReceived");

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(Helper.USERS)
                .child(FirebaseAuth.getInstance().getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ProfileValueModel profileValueModel=dataSnapshot.getValue(ProfileValueModel.class);

                long userTimeEntry = profileValueModel.getBloodEntryTimeSnapshot();
                long deviceTimeStamp = System.currentTimeMillis();

                if (deviceTimeStamp - userTimeEntry > Helper.THREE_MOUNTH) {
                    Intent notificationDialog=new Intent(getApplicationContext(), NotificationDialog.class);
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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }




    void noti(String title,String msg){
        NotificationCompat.Builder mBuilder =   new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle(title) // title for notification
                .setContentText(msg) // message for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(),0,intent,0);
        mBuilder.setContentIntent(pi);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }

}
