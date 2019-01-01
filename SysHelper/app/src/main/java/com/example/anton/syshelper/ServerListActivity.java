package com.example.anton.syshelper;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerListActivity extends AppCompatActivity {
    private final String USERS_TABLE="users";

    private ArrayList<ServerInfo> data;
    RecyclerView rv;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference myRef = database.getReference(USERS_TABLE);
    private android.support.v7.widget.Toolbar toolbar;
    private FragmentTransaction fragmentTransaction;
    private boolean isListFragmentOpen;



    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        overridePendingTransition(R.animator.slide_down_open,R.animator.slide_down_close);

        setContentView(R.layout.activity_server_list);
//        toolbar=findViewById(R.id.servers_toolbar);
//        toolbar.setTitle("Servers");
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorSecondary),PorterDuff.Mode.SRC_ATOP);

        fragmentTransaction=getSupportFragmentManager().beginTransaction();
        ServerListFragment serverListFragment=new ServerListFragment();
        fragmentTransaction.add(R.id.servers_fragment_container,serverListFragment);
        fragmentTransaction.commit();
        isListFragmentOpen=true;


    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.server_list_toolbar_menu,menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        Executor executor=Executors.newCachedThreadPool();
//        ((ExecutorService) executor).submit(new Runnable() {
//            @Override
//            public void run() {
//                //AddTask("test","test","test","test","test");
//
//            }
//        });
//        switch (item.getItemId()){
//            case R.id.plus_action:
//                if (isListFragmentOpen) {
//                    item.getIcon().setVisible(false,false);
//                    AddServerFragment addServerFragment = new AddServerFragment();
////        getSupportActionBar().hide();
//                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_in_right);
//                    fragmentTransaction.replace(R.id.servers_fragment_container, addServerFragment);
//                    fragmentTransaction.commit();
//
//                }else {
//
//                }
//                break;
//            case android.R.id.home:
//                ServerListFragment serverListFragment=new ServerListFragment();
////        getSupportActionBar().hide();
//                fragmentTransaction=getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.setCustomAnimations(R.animator.slide_in_left_open,R.animator.slide_in_right_close);
//                fragmentTransaction.replace(R.id.servers_fragment_container,serverListFragment);
//                fragmentTransaction.commit();
//
//        }
//
//        return true;
//    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void showToolbar(){
        toolbar.setVisibility(View.VISIBLE);
    }

    public void hideToolbar(){
        toolbar.setVisibility(View.GONE);
    }

    public void startLogInActivity(){
        Intent intent=new Intent(this,LogInActivity.class);
        startActivity(intent);
        overridePendingTransition(R.animator.slide_up_open,R.animator.slide_up_close);
    }


}
