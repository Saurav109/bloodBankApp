package com.example.bloodbank.profile;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.bloodbank.Helper;
import com.example.bloodbank.LatLon;
import com.example.bloodbank.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class EditProfile extends AppCompatActivity {
    final int PICK_IMAGE_REQ = 13290;
    EditText name, email, occupation, comment, mobileNo, age;
    Spinner bloodGroup, location,drug,aids,jaundice,otherDiseases,cancer;
    ImageView profilePicture;
    private Uri filePath=null;
    long entryTime=0;
    Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        bloodGroup = findViewById(R.id.blood_group_edit);
        name = findViewById(R.id.name_edit);
        email = findViewById(R.id.email_edit);
        occupation = findViewById(R.id.occupation_edit);
        comment = findViewById(R.id.comment_edit);
        mobileNo = findViewById(R.id.mobile_no_edit);
        location = findViewById(R.id.location_edit);
        drug=findViewById(R.id.drug_addicted);
        jaundice=findViewById(R.id.jaundice_patient);
        otherDiseases=findViewById(R.id.other_Blood_effected_diseases);
        aids=findViewById(R.id.aids_patient);
        cancer=findViewById(R.id.cancer_patient);

        age = findViewById(R.id.birthday_edit);
        profilePicture = findViewById(R.id.profile_picture_edit);


        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        age.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(EditProfile.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
    private void updateLabel() {
        String myFormat = Helper.DATE_FORMAT;//In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        age.setText(sdf.format(myCalendar.getTime()));
    }
    @Override
    protected void onStart() {
        super.onStart();
        getProfileValue();
//        getImage();
    }

    void getImage(){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("images");
        storageReference.child(FirebaseAuth.getInstance().getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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

    void getProfileValue() {
        String currentUid = FirebaseAuth.getInstance().getUid();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Helper.USERS);
        databaseReference = databaseReference.child(currentUid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ProfileValueModel profileValueModel = dataSnapshot.getValue(ProfileValueModel.class);
                setAllData(profileValueModel);
                entryTime=profileValueModel.getBloodEntryTimeSnapshot();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Helper.showToast(EditProfile.this, databaseError.getMessage());
            }
        });
    }

    void setAllData(ProfileValueModel profileValueModel) {

        name.setText(profileValueModel.getName().isEmpty() ? "unknown" : profileValueModel.getName());
        age.setText(profileValueModel.getBirthday().isEmpty() ? "unknown" : profileValueModel.getBirthday());
        occupation.setText(profileValueModel.getOccupation().isEmpty() ? "unknown" : profileValueModel.getOccupation());
        email.setText(profileValueModel.getEmail().isEmpty() ? "unknown" : profileValueModel.getEmail());
        mobileNo.setText(profileValueModel.getEmail().isEmpty() ? "unknown" : profileValueModel.getMobileNo());
        comment.setText(profileValueModel.getComment().isEmpty() ? "no comment" : profileValueModel.getComment());
    }

    public void saveProfile(View view) {
        afterSignUp(name.getText().toString(),
                email.getText().toString(),
                age.getText().toString(),
                occupation.getText().toString(),
                comment.getText().toString(),
                mobileNo.getText().toString(),
                location.getSelectedItem().toString(),
                bloodGroup.getSelectedItem().toString());
    }

    void afterSignUp(String name, String email, String birthday, String occupation, String comment, String mobileNo, String location, String bloodGroup) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("/users");
        LatLon l = new LatLon();
        LatLon.Location latLon = l.getLatLon(location);
        HashMap<String,Double> la=new HashMap<>();
        la.put("lat",latLon.getLat());
        la.put("lon",latLon.getLon());
//        ProfileValueModel profileValueModel = new ProfileValueModel("", bloodGroup, name, email, occupation, comment, mobileNo, location, birthday, la,entryTime);


        ProfileValueModel profileValueModel=new ProfileValueModel("",bloodGroup,name,email,occupation,comment,mobileNo,location,drug.getSelectedItem().toString(),aids.getSelectedItem().toString(),jaundice.getSelectedItem().toString(),otherDiseases.getSelectedItem().toString(),cancer.getSelectedItem().toString(),birthday,la,entryTime);
        assert user != null;
        userRef.child(user.getUid()).setValue(profileValueModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if(filePath!=null){
                        uploadImage(FirebaseAuth.getInstance().getUid());
                    }else {
                        Helper.showToast(EditProfile.this, "Edit Successful");
                        finish();
                    }

                } else {

                    Helper.showToast(EditProfile.this, Objects.requireNonNull(task.getException()).getMessage());
                }
            }
        });
    }

    public void chooseImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQ);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQ && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Log.d("Edit", "onActivityResult: success");
            filePath = data.getData();
            if(filePath!=null){
                Log.d("Edit", "onActivityResult: not null");
            }else {
                Log.d("Edit", "onActivityResult: null");
            }
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profilePicture.setImageBitmap(bitmap);
            } catch (Exception e) {
            }
        }
    }

    void uploadImage(String uuid){
        if(filePath!=null){
            final ProgressDialog progressDialog =new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.setCancelable(false);
            progressDialog.show();

            StorageReference reference= FirebaseStorage.getInstance().getReference().child("images/"+uuid);

            reference.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Helper.showToast(EditProfile.this, "Edit Successful");
                            finish();
                            filePath=null;
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Upload "+(int)progress+"%");
                        }
                    });

        }
    }

}
