package com.example.bloodbank.feed;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bloodbank.Helper;
import com.example.bloodbank.R;
import com.example.bloodbank.adapter.Adapter;
import com.example.bloodbank.adapter.CustomHashMap;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import pl.droidsonroids.gif.GifImageView;


public class Feed extends Fragment {
    CustomHashMap<String, DataSnapshot> customHashMap;
    Context context;
    RecyclerView feedRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    GifImageView loadingView;
    Adapter adapter;
    View view;
    boolean isLoaded;
    String TAG = "Feed";
    Query query;
    int ITEM_VIEW;

    public static Feed newInstance(Query query,int ITEM_VIEW) {
        Feed fragment = new Feed();
        fragment.setDatabaseQuery(query);
        fragment.setITEM_VIEW(ITEM_VIEW);
        return fragment;
    }


    public void setDatabaseQuery(Query query) {
        this.query = query;
    }
    public void setITEM_VIEW(int ITEM_VIEW){
        this.ITEM_VIEW=ITEM_VIEW;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        Log.e(TAG, "onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (!isLoaded) {

            Log.e(TAG, "onCreateView: " + "not loaded");
            view = inflater.inflate(R.layout.feed, container, false);
            customHashMap = new CustomHashMap<>();
            adapter = new Adapter(context,customHashMap,ITEM_VIEW);


        } else {
            Log.e(TAG, "onCreateView: " + "loaded");
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isLoaded) {
            feedRecyclerView = view.findViewById(R.id.feed_recycler_view);
            loadingView=view.findViewById(R.id.loading_feed);
        }
        Log.e(TAG, "onViewCreated ");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart");

        if (!isLoaded) {

            mLayoutManager = new LinearLayoutManager(context);
            feedRecyclerView.setLayoutManager(mLayoutManager);
            feedRecyclerView.setNestedScrollingEnabled(false);
            feedRecyclerView.setAdapter(adapter);
            getAllData();
        }
        //loaded everything
        isLoaded = true;
    }

    void getAllData() {
        Log.e(TAG, "getAllData ");
        this.query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    if(loadingView.getVisibility() == View.VISIBLE){
                        loadingView.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        this.query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(loadingView.getVisibility() == View.VISIBLE){
                    loadingView.setVisibility(View.GONE);
                }
                customHashMap.add(dataSnapshot.getKey(), dataSnapshot);
                adapter.notifyItemInserted(customHashMap.getIndexByKey(dataSnapshot.getKey()));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                customHashMap.change(dataSnapshot.getKey(), dataSnapshot);
                adapter.notifyItemChanged(customHashMap.getIndexByKey(dataSnapshot.getKey()));
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                customHashMap.remove(dataSnapshot.getKey());
//                adapter.notifyItemRemoved(customHashMap.getIndexByKey(dataSnapshot.getKey()));
                adapter.notifyDataSetChanged();
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

    public void clearSearch(Query query){
        Log.e(TAG, "clearSearch: "+query );
        customHashMap.clear();
        adapter = new Adapter(context,customHashMap,ITEM_VIEW);
        feedRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        setDatabaseQuery(query);
        getAllData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
        isLoaded = false;
    }

}
