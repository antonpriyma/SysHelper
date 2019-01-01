package com.example.anton.syshelper;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.widget.Toolbar;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ServerInfoActivity extends AppCompatActivity {

    private ViewPager viewPager;
    DiskInfoFragment diskInfoFragment;
    ConsoleFragment consoleFragment;
    FileManagerFragment fileManagerFragment;
    private Socket client;
    InfoClient chatClient;
    private  TabsAdapter adapter;
    ServerInfo serverInfo;

    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_server_info);
        android.support.v7.widget.Toolbar toolbar=findViewById(R.id.server_info_toolbar);
        setSupportActionBar(toolbar);
       // getSupportActionBar().setWindowTitle("test");

        setServerInfo();
        getSupportActionBar().setTitle(serverInfo.getName());
        chatClient=new InfoClient();
        new AsyncRequest().execute();
        initTabs();

    }

    private void initTabs() {//Инициализация верхнего меню
        diskInfoFragment=new DiskInfoFragment();
        consoleFragment=new ConsoleFragment();
        fileManagerFragment=new FileManagerFragment();
        viewPager = (ViewPager)findViewById(R.id.view_pager);//Разметка
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab_layout);//Разметка Бара выбора
        adapter = new TabsAdapter(this,getSupportFragmentManager());//Обработчик выбора
        adapter.addFragment(diskInfoFragment,"Disk");
        adapter.addFragment(consoleFragment,"CONSOLE");
        adapter.addFragment(fileManagerFragment,"FILES");
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
    }

    protected void setDiskInfoText(DiskInfo diskInfo){
        diskInfoFragment.setDiskInfo(diskInfo);
    }

    protected void setConsoleLog(String log){
        consoleFragment.addLog(log);
    }
    protected void setFiles(String log){
        fileManagerFragment.setFiles(log);
    }

    private void setServerInfo(){
        Intent intent=getIntent();
        serverInfo=new ServerInfo(intent.getStringExtra("NAME"),
                intent.getStringExtra("HOST"),
                intent.getStringExtra("PORT"),
                intent.getStringExtra("PASSWORD"),
                "","");
    }

    private void service(){
        Log.d("TAG", "point 2");

        chatClient.setClient(client, serverInfo, getInstance());


        Runnable watch = new Runnable() {
            @Override
            public void run() {
                try {
                    chatClient.watchForConnectionInput();
                } catch (Exception e) {
                    Toast toast=Toast.makeText(getApplicationContext(),"No connection",Toast.LENGTH_LONG);
                    toast.show();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent=new Intent(getApplicationContext(),ServerListActivity.class);
                            startActivity(intent);
                        }
                    });

                }
            }
        };
        new Thread(watch).start();

    }

    private ServerInfoActivity getInstance(){
        return this;
    }

    class AsyncRequest extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {


            try {
                try {

                    client = new Socket("192.168.43.67", 9000);  //connect to server

                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (client.isConnected()) {
                    service();
                }

//
            } catch (Exception e){
                Log.d("TAG", "doInBackground: "+e.toString());
            }
            return null;
        }
    }
    //TODO сделать нормально через константы
    protected String getDiskInfo(){
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                chatClient.watchForConsoleInput("/getDiskInfo");
            }
        });
        thread.start();

        return "";
    }

    protected String execCommand(final String command){
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                chatClient.watchForConsoleInput("/exe "+command);
            }
        });
        thread.start();

        return "";
    }
    protected String execFilesCommand(final String command){
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                chatClient.watchForConsoleInput("/filesExe "+command);
            }
        });
        thread.start();

        return "";
    }

    public ServerInfo getServerInfo() {
        return serverInfo;
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                chatClient.closeConnection();
            }
        });
        thread.start();
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem()==2){
            boolean result = ((FileManagerFragment)adapter.getItem(2)).onBackPressed();
            if (!result){
                super.onBackPressed();
            }
            return;
        }
        super.onBackPressed();

    }
}
