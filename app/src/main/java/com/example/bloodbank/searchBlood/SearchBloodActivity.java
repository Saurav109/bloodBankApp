package com.example.bloodbank.searchBlood;

import android.graphics.Color;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bloodbank.Helper;
import com.example.bloodbank.R;
import com.example.bloodbank.adapter.Adapter;
import com.example.bloodbank.feed.Feed;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class SearchBloodActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener {

    Feed feed;
    EditText searchEditText;
    String TAG = "SEARCH";

    FragmentTransaction fragmentTransaction;
    TextView blood, patient, bloodBank, profile;
    String searchTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Helper.FEED);
        searchTag="name";
        feed = Feed.newInstance(databaseReference, Adapter.BLOOD_REQUEST_ITEM_VIEW);

        //
        blood = findViewById(R.id.blood_search);
        blood.setOnClickListener(this);
        patient = findViewById(R.id.patient_search);
        patient.setOnClickListener(this);
        profile = findViewById(R.id.profile_search);
        profile.setOnClickListener(this);
        bloodBank = findViewById(R.id.blood_bank_search);
        bloodBank.setOnClickListener(this);
        changeSelectedTag(blood);
        searchTag = "bloodGroup";
        //
        searchEditText = findViewById(R.id.search_edit_text);
        searchEditText.addTextChangedListener(this);


        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.search_feed, feed,"home");
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_search:
                changeSelectedTag(v);
                searchTag = "profile";
                //
                getAllSearchResults(searchEditText.getText().toString());
                break;
            case R.id.patient_search:
                changeSelectedTag(v);
                searchTag = "name";
                //
                getAllSearchResults(searchEditText.getText().toString());
                break;
            case R.id.blood_bank_search:
                changeSelectedTag(v);
                searchTag = "bloodBank";
                //
                getAllSearchResults(searchEditText.getText().toString());
                break;
            case R.id.blood_search:
                changeSelectedTag(v);
                searchTag = "bloodGroup";
                //
                getAllSearchResults(searchEditText.getText().toString());
                break;
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String queryText = searchEditText.getText().toString().trim();
        getAllSearchResults(queryText);
    }

    void getAllSearchResults(String queryText) {
        Log.e(TAG, "getAllSearchResults: " + queryText);
        DatabaseReference databaseReference;
        Query query;
        switch (searchTag){
            case "profile":
                feed.setITEM_VIEW(Adapter.DONNER_ITEM_VIEW);
                databaseReference = FirebaseDatabase.getInstance().getReference(Helper.USERS);
                query = databaseReference.orderByChild("name").startAt(queryText).endAt(queryText + "\uf8ff");
                feed.clearSearch(query);
                break;
            case "name":
                feed.setITEM_VIEW(Adapter.BLOOD_REQUEST_ITEM_VIEW);
                databaseReference = FirebaseDatabase.getInstance().getReference(Helper.FEED);
                query = databaseReference.orderByChild("name").startAt(queryText).endAt(queryText + "\uf8ff");
                feed.clearSearch(query);
                break;
            case "bloodBank":
                feed.setITEM_VIEW(Adapter.BLOOD_BANK_ITEM_VIEW);
                databaseReference = FirebaseDatabase.getInstance().getReference(Helper.BLOOD_BANK);
                query = databaseReference.orderByChild("name").startAt(queryText).endAt(queryText + "\uf8ff");
                feed.clearSearch(query);
                break;
            case "bloodGroup":
                feed.setITEM_VIEW(Adapter.BLOOD_REQUEST_ITEM_VIEW);
                databaseReference = FirebaseDatabase.getInstance().getReference(Helper.USERS);
                query = databaseReference.orderByChild("bloodGroup").startAt(queryText).endAt(queryText + "\uf8ff");
                feed.clearSearch(query);
                break;
        }

    }

    void changeSelectedTag(View view) {
        bloodBank.setBackground(getResources().getDrawable(R.drawable.empty_blood_style));
        bloodBank.setTextColor(Color.BLACK);
        profile.setBackground(getResources().getDrawable(R.drawable.empty_blood_style));
        profile.setTextColor(Color.BLACK);
        patient.setBackground(getResources().getDrawable(R.drawable.empty_blood_style));
        patient.setTextColor(Color.BLACK);
        blood.setBackground(getResources().getDrawable(R.drawable.empty_blood_style));
        blood.setTextColor(Color.BLACK);
        //
        TextView textView=(TextView) view;

        textView.setBackground(getResources().getDrawable(R.drawable.blood_style));
        textView.setTextColor(Color.WHITE);
    }
}