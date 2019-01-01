package com.example.anton.syshelper;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AddServerFragment extends Fragment {
    private final String USERS_TABLE="users";

    private EditText hostEditText;
    private EditText portEditText;
    private EditText nameEditText;
    private EditText titleEditText;
    private EditText passwordEditText;
    private android.support.v7.widget.Toolbar toolbar;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference myRef = database.getReference(USERS_TABLE);
    private FragmentTransaction fragmentTransaction;

//    private View.OnClickListener onClickListener=new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            switch (view.getId()){
//                case R.id.fab:
////                    Executor executor=Executors.newCachedThreadPool();
////                    ((ExecutorService) executor).submit(new Runnable() {
////                        @Override
////                        public void run() {
////                            AddTask(nameEditText.getText().toString(),
////                                    hostEditText.getText().toString(),
////                                    portEditText.getText().toString(),
////                                    passwordEditText.getText().toString(),
////                                    "Test");
////
////                        }
////
////
////                    });
//
//            }
//        }
//    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    private void showServerListFragment(){
        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
        ServerListFragment serverListFragment=new ServerListFragment();
     //
        fragmentTransaction.replace(R.id.servers_fragment_container,serverListFragment);
        fragmentTransaction.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_server, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        toolbar=view.findViewById(R.id.servers_toolbar1);
        toolbar.setTitle("Servers");
        ((ServerListActivity)getActivity()).setSupportActionBar(toolbar);
        ((ServerListActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorSecondary),PorterDuff.Mode.SRC_ATOP);
        hostEditText=view.findViewById(R.id.input_host);
        passwordEditText=view.findViewById(R.id.input_password);
        nameEditText=view.findViewById(R.id.input_username);
        portEditText=view.findViewById(R.id.input_port);
        titleEditText=view.findViewById(R.id.input_title);
    }


    private void AddTask(String name, String host, String port, String password, String title){
        String key= myRef.child(firebaseUser.getUid()).push().getKey();
        Log.d("ADD", "AddTask: "+key);
        //myRef.child(firebaseUser.getUid()).push().setValue(new ServerInfo(name,host,port,password,title));
        myRef.child(firebaseUser.getUid()).child(key).setValue(new ServerInfo(name,host,port,password,title,key));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
        ServerListFragment serverListFragment=new ServerListFragment();
        switch (item.getItemId()){
            case R.id.plus_action:
                Executor executor=Executors.newCachedThreadPool();
                ((ExecutorService) executor).submit(new Runnable() {
                    @Override
                    public void run() {
                            AddTask(nameEditText.getText().toString(),
                                hostEditText.getText().toString(),
                                portEditText.getText().toString(),
                                passwordEditText.getText().toString(),
                                titleEditText.getText().toString());
                    }


                });
                fragmentTransaction.setCustomAnimations(R.animator.slide_in_left_open, R.animator.slide_in_right_close);
                fragmentTransaction.replace(R.id.servers_fragment_container, serverListFragment);
                fragmentTransaction.commit();


//        getSupportActionBar().hide();

                break;
            case android.R.id.home:
//        getSupportActionBar().hide();
                fragmentTransaction.setCustomAnimations(R.animator.slide_in_left_open, R.animator.slide_in_right_close);
                fragmentTransaction.replace(R.id.servers_fragment_container, serverListFragment);
                fragmentTransaction.commit();
        }

        return true;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.server_list_toolbar_menu,menu);
    }
}
