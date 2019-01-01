package com.example.anton.syshelper;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Patterns;
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


public class RegisterFragment extends Fragment {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Handler h;
    private Button signUpButton;
    private LogInActivity logInActivity;
    FragmentTransaction fragmentTransaction;
    LogInFragment logInFragment;
    private FirebaseAuth mAuth;
    private final String AUTH = "AUTH";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        logInActivity=(LogInActivity)getActivity();
        emailEditText = view.findViewById(R.id.input_email);
        passwordEditText = view.findViewById(R.id.input_password);
        signUpButton = view.findViewById(R.id.btn_login);
        mAuth = FirebaseAuth.getInstance();

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onClick(View view) {
                boolean valid =true;
                final ProgressDialog progressDialog = new ProgressDialog(getContext(),
                       R.style.MyProgressDialog);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMax(100);

                if (view.getId() == signUpButton.getId()) {
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

                        progressDialog.setMessage("Creating Account...");
                        progressDialog.show();
                        h = new Handler() {
                            public void handleMessage(Message msg) {
                                // выключаем анимацию ожидания
                                    progressDialog.dismiss();
                            }
                        };


                        signUp(emailEditText.getText().toString(), passwordEditText.getText().toString());
                    }
                }
            }
        };
        signUpButton.setOnClickListener(onClickListener);

    }


    private void signUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getContext(), "Register completed.",
                                    Toast.LENGTH_SHORT).show();
                            h.sendEmptyMessage(0);
                            logInActivity.openLogInFragmentFromRegiser();

                        } else {
                            String error=task.getException().toString();
                            error=error.substring(error.indexOf(":")+2);
                            h.sendEmptyMessage(0);
                            Toast.makeText(getContext(), error,
                                    Toast.LENGTH_LONG).show();
                        }

                    }

                });
    }
}




