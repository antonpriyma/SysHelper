package com.example.anton.syshelper;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.budiyev.android.circularprogressbar.CircularProgressBar;

import org.w3c.dom.Text;

import java.util.List;



public class DiskInfoFragment extends AbstractFragment {
    private static final int LAYOUT=R.layout.fragment_disk_info;
    private TextView diskUsageProcent;
    private TextView diskUsage;
    private TextView diskSpace;
    private TextView diskName;
    CircularProgressBar progressBar;
    private final String DELIM ="/";


    public void setContext(Context context) {
        this.context = context;
    }


    public static DiskInfoFragment getInstance(Context context) {
        Bundle args = new Bundle();
        DiskInfoFragment fragment = new DiskInfoFragment();
        fragment.setArguments(args);
        fragment.setContext(context);
        fragment.setTitle("TEST");
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
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        diskUsageProcent=view.findViewById(R.id.disk_usage_procent_text_view);
        diskUsage=view.findViewById(R.id.disk_usage_text_view);
        diskSpace=view.findViewById(R.id.disk_size_text_view);
        diskName=view.findViewById(R.id.disk_name_text_view);
        ServerInfoActivity activity=(ServerInfoActivity)getActivity();
        View view=getView();
        activity.getDiskInfo();
        progressBar=view.findViewById(R.id.progress_bar);
        progressBar.setProgress(0f);
    }

    protected void setDiskInfo(DiskInfo diskInfo){
        diskUsageProcent.setText(diskInfo.getUsedproc());
        diskUsage.setText(diskInfo.getUsed());
        diskSpace.setText(DELIM+diskInfo.getSize());
        diskName.setText(diskInfo.getName());
        progressBar.setProgress(Integer.parseInt(diskInfo.getUsedproc().replace("%","")));
    }
}