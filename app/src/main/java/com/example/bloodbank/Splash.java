package com.example.bloodbank;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.bloodbank.profile.ProfileValueModel;
import com.example.bloodbank.userManagement.UserManagement;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class Splash extends AppCompatActivity {
    FirebaseUser user;
    DatabaseReference databaseReference;
    String TAG="Splash";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("/users");
            databaseReference = databaseReference.child(user.getUid());
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    String currentTopic = dataSnapshot.child("bloodGroup").getValue(String.class);
                    ProfileValueModel profileValueModel= dataSnapshot.getValue(ProfileValueModel.class);
                    String currentBloodType=profileValueModel.getBloodGroup();
                    Log.d(TAG, "onDataChange: bloodGroup: "+currentBloodType);
                    setNotification(profileValueModel.getBloodGroup());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Helper.showToast(Splash.this, databaseError.getMessage());
                }
            });
        } else {
            userManagement();
        }
    }


    void setNotification(String currentTopic) {
        String previousTopic = Helper.getNotificationTopic(this);
        //if topic has changed
        if (!currentTopic.equals(previousTopic)) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(previousTopic);
            //setting new topic to device
            Helper.setNotificationTopic(currentTopic, Splash.this);
            String newTopic = Helper.getNotificationTopic(Splash.this);
            //subscribe to new topic
            FirebaseMessaging.getInstance().subscribeToTopic(newTopic)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                startApp();
                            } else {
                                Helper.showToast(Splash.this, "error!!" + task.getException().getMessage());
                                finish();
                            }
                        }
                    });
        } else {
            startApp();
        }
    }

    void startApp() {
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    void userManagement() {
        finish();
        startActivity(new Intent(this, UserManagement.class));
    }
}
