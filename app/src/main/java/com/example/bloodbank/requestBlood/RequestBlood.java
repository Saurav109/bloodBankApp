package com.example.bloodbank.requestBlood;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.bloodbank.Helper;
import com.example.bloodbank.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class RequestBlood extends Fragment implements View.OnClickListener  {
    Context context;
    EditText name, comment, mobileNo;
    Spinner location;
    private final static int PLACE_PICKER_REQUEST = 111;
//    TextView searchLink;
//    SearchLinkClickListener searchLinkClickListener;
    Spinner bloodGroup;
    Button requestButton;
    View view;
    boolean isLoaded;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(!isLoaded){
            view=inflater.inflate(R.layout.request_blood, container, false);
            isLoaded=true;
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        name = view.findViewById(R.id.patient_name_request);
        location = view.findViewById(R.id.location_request);
        comment = view.findViewById(R.id.comment_request);
        mobileNo = view.findViewById(R.id.mobile_request);
        bloodGroup = view.findViewById(R.id.blood_group_request);
        requestButton = view.findViewById(R.id.request_button);
        requestButton.setOnClickListener(this);

//        location.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                openPlacePicker();
//                startActivity(new Intent(context, MapsActivity.class));
////                searchLinkClickListener.onPickLocationClick();
//            }
//        });

//        searchLinkClickListener=(MainActivity)context;
//        searchLink=view.findViewById(R.id.search_link);
//        searchLink.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                searchLinkClickListener.onSearchLinkClick();
//            }
//        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        String name, location, comment, mobileNo, bloodGroup;
        name = this.name.getText().toString().trim();
        location = this.location.getSelectedItem().toString().trim();
        comment = this.comment.getText().toString().trim();
        mobileNo = this.mobileNo.getText().toString().trim();
        bloodGroup = this.bloodGroup.getSelectedItem().toString();

        if (!name.isEmpty() &&
                !location.isEmpty() &&
                !comment.isEmpty() &&
                !mobileNo.isEmpty() &&
                !bloodGroup.isEmpty()) {
            requestBlood(name, location, comment, mobileNo, bloodGroup);
        } else {
            Helper.showToast(context, "Enter all required field for requesting blood");
        }
    }

    void requestBlood(String name, String location, String comment, String mobileNo, String bloodGroup) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("feed");
        String uid = FirebaseAuth.getInstance().getUid();
        BloodRequestValueModel bloodRequestValueModel = new BloodRequestValueModel(name, location, comment, mobileNo, bloodGroup, false,uid);

        databaseReference.push().setValue(bloodRequestValueModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Helper.showToast(context, "Request successful, please wait for response...");
                    emptyAllTextBox();
                } else if (task.isCanceled()) {
                    emptyAllTextBox();
                    Helper.showToast(context, Objects.requireNonNull(task.getException()).getMessage());
                }
            }
        });
    }

    void emptyAllTextBox() {
        name.setText("");

        comment.setText("");
        mobileNo.setText("");
    }




}
