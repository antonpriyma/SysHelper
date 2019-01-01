package com.example.anton.syshelper;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;


public class LogInActivity extends AppCompatActivity {
    FragmentTransaction fragmentTransaction;
    LogInFragment logInFragment;
    RegisterFragment registerFragment;
    private int fragment_no;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        openLogInFragment();

    }

    public void openLogInFragment(){
        fragmentTransaction=getSupportFragmentManager().beginTransaction();
        logInFragment=new LogInFragment();
        fragment_no=1;
        fragmentTransaction.replace(R.id.fragment_container,logInFragment);
        fragmentTransaction.commit();
    }

    public void openLogInFragmentFromRegiser(){
        logInFragment=new LogInFragment();
        fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragment_no=1;
        fragmentTransaction.setCustomAnimations(R.animator.slide_in_left_open,R.animator.slide_in_right_close);
        fragmentTransaction.replace(R.id.fragment_container,logInFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (fragment_no==1){
            super.onBackPressed();
        }else {
            openLogInFragmentFromRegiser();
        }
    }

    public void startServersActivity(){
        Intent intent=new Intent(this,ServerListActivity.class);
        startActivity(intent);
        overridePendingTransition(R.animator.slide_down_open,R.animator.slide_down_close);
    }


    protected void setRegisterNo(){
        fragment_no=2;
    }


}

