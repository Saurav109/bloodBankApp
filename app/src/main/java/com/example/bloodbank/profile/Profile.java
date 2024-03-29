package com.example.bloodbank.profile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bloodbank.Helper;
import com.example.bloodbank.R;
import com.example.bloodbank.adapter.Adapter;
import com.example.bloodbank.feed.Feed;
import com.example.bloodbank.map.MapsActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Profile extends Fragment implements View.OnClickListener {
    ImageView profilePicture;
    TextView bloodGroup, name, email, occupation, comment, mobileNo, location, age, isUserActive, donetText,lastDonation;
    TextView drug, aids, jaundice, otherDiseases,cancer;
    Button logout, editProfile, entryBlood, callButton, messageButton;
    Context context;
    Feed feed;
    View view;
    String uid = "";
    boolean isLoaded;
    boolean userActivity;

    public static Profile newInstance(String uid) {
        Profile profile = new Profile();
        profile.setUid(uid);
        return profile;
    }

    void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (!isLoaded) {
            view = inflater.inflate(R.layout.profile, container, false);
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Helper.FEED);
            String profileUid = uid;
            if (profileUid.isEmpty()) {
                profileUid = FirebaseAuth.getInstance().getUid();
            } else {
                profileUid = uid;
            }

            Query query = databaseReference.orderByChild(Helper.UID).startAt(profileUid).endAt(profileUid + "\uf8ff");
            feed = Feed.newInstance(query, Adapter.BLOOD_REQUEST_ITEM_VIEW);
            isLoaded = true;
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profilePicture = view.findViewById(R.id.image_profile);
        bloodGroup = view.findViewById(R.id.blood_group_profile);
        donetText = view.findViewById(R.id.donet_text);
        lastDonation=view.findViewById(R.id.last_donetion);
        name = view.findViewById(R.id.name_profile);
        email = view.findViewById(R.id.email_profile);
        occupation = view.findViewById(R.id.occupation_profile);
        comment = view.findViewById(R.id.comment_profile);
        mobileNo = view.findViewById(R.id.mobile_no_profile);
        location = view.findViewById(R.id.location_profile);
        isUserActive = view.findViewById(R.id.is_active_profile);
        age = view.findViewById(R.id.age_profile);
        entryBlood = view.findViewById(R.id.entry_blood_button_profile);
        entryBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entryBloodDialog();
            }
        });

        drug=view.findViewById(R.id.drug);
        jaundice=view.findViewById(R.id.jaun);
        otherDiseases=view.findViewById(R.id.other);
        aids=view.findViewById(R.id.aids);
        cancer=view.findViewById(R.id.cancer);


        logout = view.findViewById(R.id.logout_profile);
        logout.setOnClickListener(this);
        callButton = view.findViewById(R.id.call_button_profile);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.call(context, mobileNo.getText().toString());
            }
        });

        messageButton = view.findViewById(R.id.message_button_profile);
        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.message(context, mobileNo.getText().toString());
            }
        });

        editProfile = view.findViewById(R.id.edit_profile_button);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, EditProfile.class));
            }
        });

        //other's profile
        if (!uid.isEmpty()) {
            donetText.setVisibility(View.GONE);
            logout.setVisibility(View.GONE);
            editProfile.setVisibility(View.GONE);
            entryBlood.setVisibility(View.GONE);
        } else
        //owner's profile
        {
            callButton.setVisibility(View.GONE);
            messageButton.setVisibility(View.GONE);
        }

        changeFragment(R.id.profile_activity_frame, feed, "profileFeed");
    }

    @Override
    public void onStart() {
        super.onStart();
        getProfileValue();
    }

    void getProfileValue() {
        String currentUid;
        if (uid.isEmpty()) {
            currentUid = FirebaseAuth.getInstance().getUid();
        } else {

            currentUid = this.uid;
        }
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Helper.USERS);
        databaseReference = databaseReference.child(currentUid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ProfileValueModel profileValueModel = dataSnapshot.getValue(ProfileValueModel.class);
                setAllData(profileValueModel, dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Helper.showToast(context, databaseError.getMessage());
            }
        });
    }

    void setAllData(ProfileValueModel profileValueModel, String uid) {

        name.setText(profileValueModel.getName().isEmpty() ? "unknown" : profileValueModel.getName());
        age.setText(profileValueModel.getBirthday().isEmpty() ? "unknown" : profileValueModel.getBirthday());
        occupation.setText(profileValueModel.getOccupation().isEmpty() ? "unknown" : profileValueModel.getOccupation());
        location.setText(profileValueModel.getLocation().isEmpty() ? "unknown" : profileValueModel.getLocation());
        email.setText(profileValueModel.getEmail().isEmpty() ? "unknown" : profileValueModel.getEmail());
        mobileNo.setText(profileValueModel.getEmail().isEmpty() ? "unknown" : profileValueModel.getMobileNo());
        comment.setText(profileValueModel.getComment().isEmpty() ? "no comment" : profileValueModel.getComment());
        bloodGroup.setText(profileValueModel.getBloodGroup().isEmpty() ? "unknown" : profileValueModel.getBloodGroup());

        drug.setText(profileValueModel.getDrug());
        cancer.setText(profileValueModel.getCancer());
        otherDiseases.setText(profileValueModel.getOtherDiseases());
        aids.setText(profileValueModel.getAids());
        jaundice.setText(profileValueModel.getJaundice());

        getImage(uid);

        long userTimeEntry = profileValueModel.getBloodEntryTimeSnapshot();
        long deviceTimeStamp = System.currentTimeMillis();


        if (deviceTimeStamp - userTimeEntry > Helper.THREE_MOUNTH) {
            isUserActive.setText("Active");
            isUserActive.setTextColor(Color.RED);
            userActivity = true;
            entryBlood.setText("Set Profile Inactive");
            lastDonation.setVisibility(View.GONE);

        }
        if (deviceTimeStamp - userTimeEntry < Helper.THREE_MOUNTH) {
            long daysPast = (deviceTimeStamp -userTimeEntry )/ (1000 * 60 * 60 * 24);
            lastDonation.setText("Last blood donated "+daysPast+" days ago");
            isUserActive.setText("Inactive");
            isUserActive.setTextColor(Color.BLACK);
            userActivity = false;
            entryBlood.setText("Set Profile active");
        }
    }


    void getImage(String uid) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("images");
        storageReference.child(uid).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri.toString()).fit().centerCrop().into(profilePicture);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isLoaded = false;
    }

    @Override
    public void onClick(View v) {


        new AlertDialog.Builder(context)
                .setTitle("Logout")
                .setMessage("Do you really want to logout?")
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        FirebaseAuth.getInstance().signOut();
                        Helper.showToast(context, "Logout!");
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    void changeFragment(int frameId, Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        Fragment frStack = getChildFragmentManager().findFragmentByTag(tag);

        if (frStack == null) {
            fragmentTransaction.replace(frameId, fragment, tag);
        } else {
            fragmentTransaction.replace(frameId, frStack);
        }

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    void entryBloodDialog() {
        String title = "Are you  really want to inactive your profile?";
        String message = "People will see your profile as inactive , and you won't receive any notification";
        if (!userActivity) {
            title = "Are you  really want to active your profile?";
            message = "People will see your profile as active , and you will receive notification";
        }

        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        entryBlood();

                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    void entryBlood() {

        String currentUid;
        if (uid.isEmpty()) {
            currentUid = FirebaseAuth.getInstance().getUid();
        } else {

            currentUid = this.uid;
        }
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Helper.USERS);
        databaseReference = databaseReference.child(currentUid);

        if (userActivity) {

            databaseReference.child("bloodEntryTimeSnapshot").setValue(ServerValue.TIMESTAMP)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Helper.showToast(context, "Successful!");
                        }
                    });
        } else {
            databaseReference.child("bloodEntryTimeSnapshot").setValue(0)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Helper.showToast(context, "Successful!");
                        }
                    });
        }
    }
}
