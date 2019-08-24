package com.example.bloodbank;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;


import com.example.bloodbank.bloodBank.BloodBank;
import com.example.bloodbank.donner.Donner;
import com.example.bloodbank.home.Home;
import com.example.bloodbank.notification.Notification;
import com.example.bloodbank.profile.Profile;
import com.example.bloodbank.searchBlood.SearchBloodActivity;
import com.example.bloodbank.searchBlood.SearchLinkClickListener;
import com.example.bloodbank.userManagement.UserManagement;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity implements SearchLinkClickListener, FirebaseAuth.AuthStateListener, BottomNavigationView.OnNavigationItemSelectedListener {
    FirebaseUser user;
    BottomNavigationView bottomNavigationView;
    FragmentTransaction fragmentTransaction;
    //fragments
    Home home;
    Profile profile;
    Notification notification;
    BloodBank bloodBank;
    Donner donner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseAuth.getInstance().addAuthStateListener(this);

        bottomNavigationView = findViewById(R.id.nav_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        home = new Home();
        profile = new Profile();
        notification = new Notification();
        bloodBank = new BloodBank();
        donner =new Donner();

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.parent_frame, home, "home");
        fragmentTransaction.commit();
    }

    @Override
    public void onSearchLinkClick() {
//        changeFragment(search,"search");
        Intent searchIntent = new Intent(this, SearchBloodActivity.class);
        startActivity(searchIntent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.home_fragment_nav:
                changeFragment(home, "home");
                return true;
            case R.id.profile_fragment_nav:
                changeFragment(profile, "profile");
                return true;
            case R.id.notification_fragment_nav:
                changeFragment(notification, "notification");
                return true;
            case R.id.blood_bank_fragment_nav:
                changeFragment(bloodBank, "bloodBank");
                return true;
            case R.id.donner_fragment_nav:
                changeFragment(donner,"donner");
                return true;
        }
        return false;
    }

    void changeFragment(Fragment fragment, String tag) {
//        if (fragment.isAdded()) {
//            return;
//        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        Fragment frStack = getSupportFragmentManager().findFragmentByTag(tag);

        if (frStack == null) {
            fragmentTransaction.replace(R.id.parent_frame, fragment, tag);
        } else {
            fragmentTransaction.replace(R.id.parent_frame, frStack);
        }

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() == null) {
//            logout();
            finish();
            Intent intent=new Intent(this, UserManagement.class);
            startActivity(intent);
        }
    }

}
