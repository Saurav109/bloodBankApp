package com.example.bloodbank.userManagement;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bloodbank.Helper;
import com.example.bloodbank.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends Fragment implements View.OnClickListener {
    Context context;
    EditText email;
    Button resetButton;
    TextView goLogin;
    ProgressDialog progressDialog;
    UserMngInterface userMngInterface;
    String TAG="Reset Password";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.reset_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        email = view.findViewById(R.id.email_reset_password);
        resetButton = view.findViewById(R.id.reset_button);
        resetButton.setOnClickListener(this);
        goLogin = view.findViewById(R.id.go_login_from_reset);
        goLogin.setOnClickListener(this);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading, Please wait");
        progressDialog.setCancelable(false);
        userMngInterface = (UserManagement) context;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.request_button:
                String email = this.email.getText().toString().trim();
                Log.d(TAG, "onClick: email:"+email);
                if (!email.isEmpty()) {
                    reset(email);
                }else {
                    Helper.showToast(context,"Please Enter email address");
                }
                break;
            case R.id.go_login_from_reset:
                userMngInterface.selectLogin();
                break;
        }
    }

    void reset(String email) {
        progressDialog.show();
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Helper.showToast(context, "Email sent!");
                        } else {
                            progressDialog.dismiss();
                            Helper.showToast(context, task.getException().getMessage());
                        }
                    }
                });
    }
}
