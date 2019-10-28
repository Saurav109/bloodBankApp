package com.example.bloodbank.ViewHolder;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bloodbank.Helper;
import com.example.bloodbank.R;
import com.example.bloodbank.requestBlood.BloodRequestValueModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BloodRequestViewHolder extends RecyclerView.ViewHolder {
    private TextView name, location, comment, mobileNo, bloodGroup, time,hospital;
    private String uid;
    private SimpleDateFormat formatter;
    private Button bloodFound,call;
    private ImageView doneIcon;
    private ImageView deleteIcon;
    private Context context;

    public BloodRequestViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.patient_name_view_model);
        location = itemView.findViewById(R.id.location_view_model);
        comment = itemView.findViewById(R.id.comment_view_model);
        mobileNo = itemView.findViewById(R.id.mobile_view_model);
        bloodGroup = itemView.findViewById(R.id.blood_group_view_model);
        time = itemView.findViewById(R.id.time_view_model);
        bloodFound = itemView.findViewById(R.id.blood_found_button_view_model);
        call=itemView.findViewById(R.id.call_button_view_model);
        doneIcon = itemView.findViewById(R.id.done_icon_view_model);
        deleteIcon=itemView.findViewById(R.id.delete_post_icon_view_model);
        hospital=itemView.findViewById(R.id.hospital_view_model);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        formatter = new SimpleDateFormat("HH:mm  dd/MM/yyyy");
    }


    public void setView(final DataSnapshot dataSnapshot, final Context context) {
        this.context=context;
        final BloodRequestValueModel bloodRequestValueModel = dataSnapshot.getValue(BloodRequestValueModel.class);

        name.setText(bloodRequestValueModel.getName());
        location.setText(bloodRequestValueModel.getLocation());
        comment.setText(bloodRequestValueModel.getComment());
        mobileNo.setText(bloodRequestValueModel.getMobileNo());
        bloodGroup.setText(bloodRequestValueModel.getBloodGroup());
        hospital.setText(bloodRequestValueModel.getHospitalName());

        String dateString = formatter.format(new Date(bloodRequestValueModel.time));
        time.setText(dateString);
        String postOwnerUid=bloodRequestValueModel.getUid();

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.call(context,bloodRequestValueModel.getMobileNo());
            }
        });

        Log.d("BLOODREQ", "setView: "+"current uid:"+uid+" post Uid: "+bloodRequestValueModel.getUid());
        //if this is user post or not
        //hide delete button and blood found button
        if(!uid.equals(postOwnerUid)){
            deleteIcon.setVisibility(View.GONE);
            bloodFound.setVisibility(View.GONE);
        }
        else {
            bloodFound.setVisibility(View.VISIBLE);
            deleteIcon.setVisibility(View.VISIBLE);

            //blood found button  function
            deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deletePost(dataSnapshot.getKey());
                }
            });
            bloodFound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bloodFoundButton(dataSnapshot.getKey());
                }
            });
        }

        //if Blood found
        if (!bloodRequestValueModel.isBloodFound()) {
            doneIcon.setVisibility(View.GONE);
            bloodFound.setText("Blood found");
        } else {
            bloodFound.setText("Blood not found");
        }

    }

    void bloodFoundButton(final String key) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Helper.FEED);
        databaseReference = databaseReference.child(key).child("bloodFound");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Helper.FEED);
                databaseReference = databaseReference.child(key).child("bloodFound");
                if (!dataSnapshot.exists()) {
                    databaseReference.setValue(true);
                } else {

                    boolean isBloodFound = dataSnapshot.getValue(Boolean.class);

                    if (isBloodFound) {
                        databaseReference.setValue(false);
                    } else {
                        databaseReference.setValue(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void deletePost(String key){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Helper.FEED);
        databaseReference = databaseReference.child(key);
        final DatabaseReference finalDatabaseReference = databaseReference;
        new AlertDialog.Builder(context)
                .setTitle("Delete post")
                .setMessage("Do you really want to delete this post?")
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                       //
                        finalDatabaseReference.removeValue();
                        Helper.showToast(context,"Deleted!");

                    }})
                .setNegativeButton(android.R.string.no, null).show();

    }

}
