package com.example.anton.syshelper;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ServerListFragment extends Fragment {

    private final String USERS_TABLE="users";

    private ArrayList<ServerInfo> data;
    RecyclerView rv;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference myRef = database.getReference(USERS_TABLE);
    private android.support.v7.widget.Toolbar toolbar;
    private FragmentTransaction fragmentTransaction;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_server_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view=getView();
//        ((ServerListActivity)getActivity()).showToolbar();
        toolbar=view.findViewById(R.id.servers_toolbar);

        ((ServerListActivity)getActivity()).setSupportActionBar(toolbar);
        ((ServerListActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((ServerListActivity)getActivity()).setTitle("Servers");
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorSecondary),PorterDuff.Mode.SRC_ATOP);
        rv=view.findViewById(R.id.servers_list);
        data=new ArrayList<>();
        ServerListAdapter adapter=new ServerListAdapter(data);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback(adapter));
        itemTouchHelper.attachToRecyclerView(rv);


        rv.setAdapter(adapter);
        getMyRef().child(firebaseUser.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ServerInfo info =dataSnapshot.getValue(ServerInfo.class);
                data.add(info);
                rv.getAdapter().notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                ServerInfo info =dataSnapshot.getValue(ServerInfo.class);
                int counter=0;
                for (ServerInfo i:data){
                    counter++;
                    if (i.compareTo(info) == 0){
                        data.remove(i);
                        break;
                    }
                }
                rv.getAdapter().notifyItemRemoved(counter);

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


//
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Executor executor=Executors.newCachedThreadPool();
        ((ExecutorService) executor).submit(new Runnable() {
            @Override
            public void run() {
                //AddTask("test","test","test","test","test");

            }
        });
        fragmentTransaction=getFragmentManager().beginTransaction();

        switch (item.getItemId()){
            case R.id.plus_action:


//        getSupportActionBar().hide();
                    AddServerFragment addServerFragment=new AddServerFragment();
                    fragmentTransaction.addSharedElement(toolbar, toolbar.getTransitionName());
                    fragmentTransaction.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_in_right);
                    fragmentTransaction.replace(R.id.servers_fragment_container, addServerFragment);
                    fragmentTransaction.commit();

                break;
            case android.R.id.home:
//                ServerListFragment serverListFragment=new ServerListFragment();
//                fragmentTransaction.setCustomAnimations(R.animator.slide_in_left_open,R.animator.slide_in_right_close);
//                fragmentTransaction.replace(R.id.servers_fragment_container,serverListFragment);
//                fragmentTransaction.commit();
//                Intent intent=new Intent(getContext(),ServerListActivity.class);
//                startActivity(intent);
                ((ServerListActivity)getActivity()).startLogInActivity();

        }


        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.server_list_toolbar_menu,menu);
    }

    public DatabaseReference getMyRef() {
        return myRef;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        ((ServerListActivity)getActivity()).showToolbar();
//
//    }
//    @Override
//    public void onStop() {
//        super.onStop();
//        ((ServerListActivity)getActivity()).hideToolbar();
//    }


}
