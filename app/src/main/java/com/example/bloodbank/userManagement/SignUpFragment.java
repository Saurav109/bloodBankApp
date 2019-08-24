package com.example.bloodbank.userManagement;

import android.app.ProgressDialog;
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
import android.widget.TextView;

import com.example.bloodbank.Helper;
import com.example.bloodbank.LatLon;
import com.example.bloodbank.MainActivity;
import com.example.bloodbank.R;
import com.example.bloodbank.profile.ProfileValueModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class SignUpFragment extends Fragment implements View.OnClickListener {
    EditText name, email, password, password2, birthday, occupation, comment, mobileNo;
    Spinner location;
    TextView goLogin;
    Spinner bloodGroup;
    Button signUpButton;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    UserMngInterface userMngInterface;
    Context context;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        name = view.findViewById(R.id.name_sign_up);
        email = view.findViewById(R.id.email_sign_up);
        password = view.findViewById(R.id.password_sign_up);
        password2 = view.findViewById(R.id.password_two_sign_up);
        birthday = view.findViewById(R.id.birthday_sign_up);
        occupation = view.findViewById(R.id.occupation_sign_up);
        comment = view.findViewById(R.id.comment_sign_up);
        mobileNo = view.findViewById(R.id.mobile_no_sign_up);
        location = view.findViewById(R.id.location_sign_up);
        bloodGroup = view.findViewById(R.id.blood_group_sign_up);
        signUpButton = view.findViewById(R.id.sign_up_button);
        goLogin = view.findViewById(R.id.go_login);
        goLogin.setOnClickListener(this);
        signUpButton.setOnClickListener(this);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading, Please wait");
        progressDialog.setCancelable(false);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        userMngInterface = (UserManagement) context;
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_up_button:
                check();
                break;
            case R.id.go_login:
                userMngInterface.selectLogin();
                break;
        }
    }

    void check() {
        String name = this.name.getText().toString().trim();
        String email = this.email.getText().toString().trim();
        String password = this.password.getText().toString().trim();
        String password2 = this.password2.getText().toString().trim();
        String occupation = this.occupation.getText().toString().trim();
        String mobileNo = this.mobileNo.getText().toString().trim();
        String comment = this.comment.getText().toString().trim();
        String birthday = this.birthday.getText().toString().trim();
        String bloodGroup = this.bloodGroup.getSelectedItem().toString();

        String location = this.location.getSelectedItem().toString();


        if (!name.isEmpty()
                && !password.isEmpty()
                && !password2.isEmpty()
                && !location.isEmpty()
                && !occupation.isEmpty()
                && !mobileNo.isEmpty()
                && !bloodGroup.isEmpty()
                && !birthday.isEmpty()
//                && !comment.isEmpty()
                && !email.isEmpty()) {
            //if password don't match
            if (!password.equals(password2)) {
                Helper.showToast(context, "Password didn't match");
            } else {
                //if password match
                signUp(name, email, password, birthday, occupation, comment, mobileNo, location, bloodGroup);
            }

        }
    }

    public void signUp(final String name, final String email, String password, final String birthday, final String occupation, final String comment, final String mobileNo, final String location, final String bloodGroup) {
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            afterSignUp(name, email, birthday, occupation, comment, mobileNo, location, bloodGroup);
                        } else {
                            progressDialog.dismiss();
                            Helper.showToast(context, Objects.requireNonNull(task.getException()).getMessage());
                        }
                    }
                });
    }

    void afterSignUp(String name, String email, String birthday, String occupation, String comment, String mobileNo, String location, String bloodGroup) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("/users");
        LatLon l=new LatLon();
        LatLon.Location latLon=l.getLatLon(location);
        HashMap<String,Double> la=new HashMap<>();
        la.put("lat",latLon.getLat());
        la.put("lon",latLon.getLon());
        ProfileValueModel profileValueModel = new ProfileValueModel("", bloodGroup, name, email, occupation, comment, mobileNo, location, birthday,la);
        assert user != null;
        userRef.child(user.getUid()).setValue(profileValueModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Helper.showToast(context, "Sign up successful");
                    Intent home = new Intent(context, MainActivity.class);
                    startActivity(home);
                } else {
                    progressDialog.dismiss();
                    Helper.showToast(context, Objects.requireNonNull(task.getException()).getMessage());
                }
            }
        });
    }

}
