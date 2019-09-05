package com.example.bloodbank.userManagement;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.bloodbank.R;

public class UserManagement extends AppCompatActivity implements UserMngInterface {
    FrameLayout frameLayout;
    LoginFragment loginFragment;
    SignUpFragment signUpFragment;
    ResetPassword resetPassword;
    String TAG="USER_MANGEMENT";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_management);
        Log.d(TAG, "onCreate: user management");

        frameLayout = findViewById(R.id.userMngFrame);
        loginFragment = new LoginFragment();
        signUpFragment = new SignUpFragment();
        resetPassword = new ResetPassword();

        getSupportFragmentManager().beginTransaction().replace(R.id.userMngFrame, loginFragment).commit();

    }

    @Override
    public void selectLogin() {
        changeFragment(loginFragment,"login");
//        getSupportFragmentManager().beginTransaction().add(R.id.userMngFrame, loginFragment).commit();
        Log.e("USER", "select login ");
    }

    @Override
    public void selectSignUp() {
        changeFragment(signUpFragment,"signUp");
//        getSupportFragmentManager().beginTransaction().add(R.id.userMngFrame, signUpFragment).commit();
        Log.e("USER", "select signUp ");
    }

    @Override
    public void selectResetPassword() {
        changeFragment(resetPassword,"resetPassword");
//        getSupportFragmentManager().beginTransaction().add(R.id.userMngFrame,resetPassword).commit();
        Log.e("USER", "select reset password ");
    }
    void changeFragment(Fragment fragment, String tag) {
        if (fragment.isAdded()) {
            return;
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        Fragment frStack = getSupportFragmentManager().findFragmentByTag(tag);

        if (frStack == null) {
            fragmentTransaction.replace(R.id.userMngFrame, fragment, tag);
        } else {
            fragmentTransaction.replace(R.id.userMngFrame, frStack);
        }

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
