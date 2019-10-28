package com.example.bloodbank.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bloodbank.Helper;
import com.example.bloodbank.MainActivity;
import com.example.bloodbank.R;
import com.example.bloodbank.profile.ProfileActivity;
import com.example.bloodbank.profile.ProfileValueModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class DonnerViewHolder extends RecyclerView.ViewHolder {
    TextView name, blood, location, mobile, isUserActive;
    ImageView callIcon;
    RelativeLayout relativeLayout;
    Context context;
    ImageView profilePicture;

    public DonnerViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.name_donner_view_model);
        blood = itemView.findViewById(R.id.blood_group_donner_view_model);
        location = itemView.findViewById(R.id.location_donner_view_model);
        mobile = itemView.findViewById(R.id.mobile_no_donner_view_model);
        callIcon = itemView.findViewById(R.id.call_icon_donner_view_model);
        relativeLayout = itemView.findViewById(R.id.relative_layout_donner_view_model);
        profilePicture = itemView.findViewById(R.id.profile_picture_donner);
        isUserActive = itemView.findViewById(R.id.is_user_active);
    }

    public void setView(final DataSnapshot dataSnapshot, final Context context) {
        this.context = context;
        final ProfileValueModel profileValueModel = dataSnapshot.getValue(ProfileValueModel.class);
        name.setText(profileValueModel.getName());
        blood.setText(profileValueModel.getBloodGroup());
        location.setText(profileValueModel.getLocation());
        mobile.setText(profileValueModel.getMobileNo());

        long userTimeEntry = profileValueModel.getBloodEntryTimeSnapshot();
        long deviceTimeStamp = System.currentTimeMillis();
        isUserActive.setText(String.valueOf(deviceTimeStamp));

        if (deviceTimeStamp - userTimeEntry > Helper.THREE_MOUNTH) {
            isUserActive.setText("Active donors");
            isUserActive.setTextColor(Color.RED);

        }
        if (deviceTimeStamp - userTimeEntry < Helper.THREE_MOUNTH) {
            isUserActive.setText("Inactive");
            isUserActive.setTextColor(Color.BLACK);

        }


        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("uid", dataSnapshot.getKey());
                context.startActivity(intent);
            }
        });

        callIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.call(context, profileValueModel.getMobileNo());
            }
        });

        getImage(dataSnapshot.getKey());
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

}
