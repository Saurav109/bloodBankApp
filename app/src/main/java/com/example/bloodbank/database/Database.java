package com.example.bloodbank.database;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.bloodbank.Helper;
import com.example.bloodbank.adapter.Adapter;
import com.example.bloodbank.adapter.CustomHashMap;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Database {
    private DatabaseReference databaseReference;
    private CustomHashMap<String, Object> customHashMap;
    private Adapter adapter;
    private Context context;

    public Database(DatabaseReference databaseReference, CustomHashMap<String, Object> customHashMap, Adapter adapter, Context context) {
        this.databaseReference = databaseReference;
        this.customHashMap = customHashMap;
        this.adapter = adapter;
        this.context = context;
    }

    void startFetching() {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                customHashMap.add(dataSnapshot.getKey(), dataSnapshot.getValue());
                adapter.notifyItemInserted(customHashMap.getIndexByKey(dataSnapshot.getKey()));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                customHashMap.change(dataSnapshot.getKey(), dataSnapshot.getValue());
                adapter.notifyItemChanged(customHashMap.getIndexByKey(dataSnapshot.getKey()));
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                customHashMap.remove(dataSnapshot.getKey());
                adapter.notifyItemRemoved(customHashMap.getIndexByKey(dataSnapshot.getKey()));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //TODO: don't know what to do
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Helper.showToast(context, databaseError.getMessage());
            }
        });
    }
}
