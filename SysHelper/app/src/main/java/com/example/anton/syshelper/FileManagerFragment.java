package com.example.anton.syshelper;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.circularprogressbar.CircularProgressBar;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileManagerFragment extends AbstractFragment {
    private static final int LAYOUT=R.layout.fragment_file_manager;
    private StringBuilder currPath;
    private ServerInfoActivity serverInfoActivity;
    private String deletingFileName;
    private int deletingFileNumber;
    private ListView lv;
    private ArrayList<String> files;
    private ArrayAdapter adapter;
    private final String DELIM ="/";

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            TextView textView = (TextView)view;
            String pathName = textView.getText().toString();
            currPath.append(DELIM+pathName);
            getFiles();

        }
    };


    public void setContext(Context context) {
        this.context = context;
    }


    public static FileManagerFragment getInstance(Context context) {
        Bundle args = new Bundle();
        FileManagerFragment fragment = new FileManagerFragment();
        fragment.setArguments(args);
        fragment.setContext(context);
        fragment.setTitle("Files");
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(LAYOUT, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        view=getView();
        super.onActivityCreated(savedInstanceState);
        serverInfoActivity=(ServerInfoActivity)getActivity();
        currPath=new StringBuilder("/home/"+serverInfoActivity.getServerInfo().getName());

    }

    private void getFiles() {
        serverInfoActivity.execFilesCommand("cd "+currPath+" && " + "ls");
    }
    private void deleteFile() {
        serverInfoActivity.execFilesCommand("cd "+currPath+" && " + "rmdir "+deletingFileName+" && "+"ls");
        files.remove(deletingFileNumber);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();

        lv=view.findViewById(R.id.list_view);
        files=new ArrayList<>();
        adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,files);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(onItemClickListener);
        registerForContextMenu(lv);
        lv.setLongClickable(true);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView)view;
                deletingFileName= textView.getText().toString();
                deletingFileNumber=i;
                return false;
            }
        });

        getFiles();

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.list_view) {

            MenuInflater inflater = serverInfoActivity.getMenuInflater();
            inflater.inflate(R.menu.files_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_file_button:
                Toast toast= Toast.makeText(getContext(),"delete",Toast.LENGTH_LONG);
                toast.show();
                deleteFile();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    protected void setFiles(String log){
        while (files==null){
            ;
        }
        files.clear();
        files.addAll(Arrays.asList(log.replace("$", "\n").split("\n")));
        adapter.notifyDataSetChanged();
    }


    public boolean onBackPressed() {
        if (currPath.lastIndexOf("/")!=5) {
            currPath = new StringBuilder(currPath.substring(0, currPath.lastIndexOf("/")));
            getFiles();
            return true;
        }
        return false;
    }
}