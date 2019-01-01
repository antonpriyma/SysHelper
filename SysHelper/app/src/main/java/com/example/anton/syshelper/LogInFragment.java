package com.example.anton.syshelper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatButton;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LogInFragment extends Fragment {
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button logInButton;
    FragmentTransaction fragmentTransaction;
    private FirebaseAuth mAuth;
    private TextView signUpLink;
    private final String AUTH="AUTH";
    private LogInActivity logInActivity;
    private Handler h;
    RegisterFragment registerFragment;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_log_in, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view=getView();
        logInActivity=(LogInActivity)getActivity();
        signUpLink=view.findViewById(R.id.link_signup);
        emailEditText=view.findViewById(R.id.input_email);
        passwordEditText=view.findViewById(R.id.input_password);
        logInButton=view.findViewById(R.id.btn_login);
        mAuth = FirebaseAuth.getInstance();

        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId()==logInButton.getId()){
                    boolean valid =true;
                    final ProgressDialog progressDialog = new ProgressDialog(getContext(),
                            R.style.MyProgressDialog);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setMax(100);

                        String email = emailEditText.getText().toString();
                        String password = passwordEditText.getText().toString();
                        if (email.isEmpty()|| !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            emailEditText.setError("enter a valid email address");
                            valid = false;
                        }

                        if (password.isEmpty() ||password.length()<6){
                            passwordEditText.setError("at least 6 characters");
                            valid=false;
                        }
                        if (valid) {
                            progressDialog.setIndeterminate(true);

                            progressDialog.setMessage("Loading...");
                            progressDialog.show();
                            h = new Handler() {
                                public void handleMessage(Message msg) {
                                    progressDialog.dismiss();
                                }
                            };
                        }

                            logIn(emailEditText.getText().toString(),passwordEditText.getText().toString());
                }else if (view.getId()==signUpLink.getId()){
                    openSignUpFragment();
                }
            }
        };
        logInButton.setOnClickListener(onClickListener);
        signUpLink.setOnClickListener(onClickListener);
    }


    public void openSignUpFragment(){
        fragmentTransaction=getFragmentManager().beginTransaction();
        registerFragment=new RegisterFragment();
        logInActivity.setRegisterNo();
        fragmentTransaction.setCustomAnimations(R.animator.slide_in_left,R.animator.slide_in_right);
        fragmentTransaction.replace(R.id.fragment_container,registerFragment);
        fragmentTransaction.commit();
    }




    private void logIn(String email,String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getContext(), "Authentication completed.",
                                    Toast.LENGTH_LONG).show();
                            h.sendEmptyMessage(0);
                            Log.d(AUTH,"Authentication completed.");
                            ((LogInActivity)getActivity()).startServersActivity();
                        } else {
                            // If sign in fails, display a message to the user.
                            h.sendEmptyMessage(0);
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_LONG).show();
                            Log.d(AUTH,"Authentication failed.");

                        }

                    }
                });
    }

}
