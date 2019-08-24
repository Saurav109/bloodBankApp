package com.example.bloodbank.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bloodbank.Helper;
import com.example.bloodbank.MainActivity;
import com.example.bloodbank.map.MapsActivity;
import com.example.bloodbank.R;
import com.example.bloodbank.adapter.Adapter;
import com.example.bloodbank.feed.Feed;
import com.example.bloodbank.requestBlood.RequestBlood;
import com.example.bloodbank.searchBlood.SearchLinkClickListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Home extends Fragment {

    RequestBlood requestBlood;
    Feed feed;
    View view;
    Context context;
    TextView searchLinkTextView;
    SearchLinkClickListener searchLinkClickListener;
    boolean isLoaded;
    TextView showMap;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (!isLoaded) {
            view = inflater.inflate(R.layout.home, container, false);
            requestBlood = new RequestBlood();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Helper.FEED);
            feed = Feed.newInstance(databaseReference, Adapter.BLOOD_REQUEST_ITEM_VIEW);
            searchLinkClickListener = (MainActivity) context;
            isLoaded = true;

        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchLinkTextView = view.findViewById(R.id.search_link);
        showMap = view.findViewById(R.id.showMap);
        showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, MapsActivity.class));
            }
        });
        searchLinkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchLinkClickListener.onSearchLinkClick();
            }
        });
        changeFragment(R.id.feed_parent_frame, feed, "feed");
        changeFragment(R.id.request_parent_frame, requestBlood, "req");

    }


    void changeFragment(int frameId, Fragment fragment, String tag) {
        if (fragment.isAdded()) {
            return;
        }

        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
//        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        Fragment frStack = getChildFragmentManager().findFragmentByTag(tag);

        if (frStack == null) {
            fragmentTransaction.add(frameId, fragment, tag);
        } else {
            fragmentTransaction.add(frameId, frStack);
        }

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isLoaded = false;
    }
}
