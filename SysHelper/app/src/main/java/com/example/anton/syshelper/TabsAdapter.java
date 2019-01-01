package com.example.anton.syshelper;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.ArrayList;

public class TabsAdapter extends FragmentPagerAdapter {
    private ArrayList<String> FragmentTitels;
    private ArrayList<Fragment> FragmentList;
    private Context context;

    public TabsAdapter(Context context, FragmentManager fm){
        super(fm);
        this.context=context;
        FragmentTitels=new ArrayList<>();
        FragmentList=new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return FragmentList.get(position);
    }

    @Override
    public int getCount() {
        return   FragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence currient=FragmentTitels.get(position);
        return currient;
    }

    public void addFragment(AbstractFragment fragment, String title) {
        FragmentList.add(fragment);
        FragmentTitels.add(title);
    }






}
