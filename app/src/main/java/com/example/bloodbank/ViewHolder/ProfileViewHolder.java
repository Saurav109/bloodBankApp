package com.example.bloodbank.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.bloodbank.R;
import com.example.bloodbank.profile.ProfileValueModel;
import com.google.firebase.database.DataSnapshot;

public class ProfileViewHolder extends RecyclerView.ViewHolder{

    TextView bloodGroup, name, mobileNo, location;

    public ProfileViewHolder(@NonNull View itemView) {
        super(itemView);
        bloodGroup=itemView.findViewById(R.id.profile_blood_group);
        name=itemView.findViewById(R.id.profile_name);
        mobileNo=itemView.findViewById(R.id.profile_mobile_no);
        location=itemView.findViewById(R.id.profile_location);
    }
    public void setView(DataSnapshot dataSnapshot){
        ProfileValueModel profileValueModel=dataSnapshot.getValue(ProfileValueModel.class);

        bloodGroup.setText(profileValueModel.getBloodGroup());
        name.setText(profileValueModel.getName());
        mobileNo.setText(profileValueModel.getMobileNo());
        location.setText(profileValueModel.getLocation());
    }
}
