package com.example.bloodbank.bloodBank;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

public class BloodBank extends Fragment {
    boolean isLoaded;
    View view;
    Feed feed;
    Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(!isLoaded){
            view=inflater.inflate(R.layout.blood_bank, container, false);
            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(Helper.BLOOD_BANK);
            feed=Feed.newInstance(databaseReference, Adapter.BLOOD_BANK_ITEM_VIEW);
            isLoaded=true;
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        changeFragment(R.id.blood_bank_feed,feed,"blood_bank_feed");
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
    public void onDestroyView() {
        super.onDestroyView();
        isLoaded=false;
    }
}
