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
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodbank.Helper;
import com.example.bloodbank.MainActivity;
import com.example.bloodbank.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment implements View.OnClickListener {

    EditText email;
    EditText password;
    Button loginButton;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    UserMngInterface userMngInterface;
    Context context;
    TextView goToSignUp;
    TextView goToresetPassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        email = view.findViewById(R.id.email_login);
        password = view.findViewById(R.id.pass_login);
        goToSignUp = view.findViewById(R.id.go_sign_up);
        goToSignUp.setOnClickListener(this);
        loginButton = view.findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);
        goToresetPassword=view.findViewById(R.id.go_to_reset_password);
        goToresetPassword.setOnClickListener(this);

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
            case R.id.login_button:
                check();
                break;
            case R.id.go_sign_up:
                userMngInterface.selectSignUp();
                break;
            case R.id.go_to_reset_password:
                userMngInterface.selectResetPassword();
                break;
        }
    }

    void check() {
        String email = this.email.getText().toString().trim();
        String password = this.password.getText().toString().trim();

        if (!email.isEmpty() && !password.isEmpty()) {
            login(email, password);
        } else {
            Toast.makeText(context, "Please enter email and password", Toast.LENGTH_LONG).show();
        }
    }

    void login(String email, String password) {
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            afterLogin();
                        } else {
                            progressDialog.dismiss();
                            Helper.showToast(context,task.getException().getMessage());
                        }
                    }
                });
    }

    void afterLogin() {
        progressDialog.dismiss();
        Intent home = new Intent(context, MainActivity.class);
        startActivity(home);
    }
}
