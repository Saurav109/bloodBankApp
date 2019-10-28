package com.example.bloodbank.notification;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bloodbank.Helper;
import com.example.bloodbank.R;
import com.example.bloodbank.adapter.Adapter;
import com.example.bloodbank.feed.Feed;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Notification extends Fragment {
    boolean isLoaded;
    View view;
    Feed feed;
    Context context;
    String TAG="Notification";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(!isLoaded){
            view=inflater.inflate(R.layout.notification, container, false);
            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(Helper.FEED);
            String userBloodGroupName=Helper.getBloodGroup(context);

            Log.d(TAG, "onCreateView: "+"blood group:"+userBloodGroupName);

            Query query=databaseReference.orderByChild(Helper.BLOOD_GROUP).startAt(userBloodGroupName).endAt(userBloodGroupName+"\uf8ff");
            feed=Feed.newInstance(query, Adapter.BLOOD_REQUEST_ITEM_VIEW);
            isLoaded=true;
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        changeFragment(R.id.notification_feed,feed,"notification_feed");
    }
    void changeFragment(int frameId,Fragment fragment,String tag){
        FragmentTransaction fragmentTransaction=getChildFragmentManager().beginTransaction();
        Fragment frStack = getChildFragmentManager().findFragmentByTag(tag);

        if(frStack == null){
            fragmentTransaction.replace(frameId,fragment,tag);
        }else {
            fragmentTransaction.replace(frameId,frStack);
        }

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        isLoaded=false;
    }
}
