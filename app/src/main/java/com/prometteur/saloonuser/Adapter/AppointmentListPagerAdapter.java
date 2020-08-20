package com.prometteur.saloonuser.Adapter;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.prometteur.saloonuser.Fragments.FragmentAppointmantCancelled;
import com.prometteur.saloonuser.Fragments.FragmentAppointmentCompleted;
import com.prometteur.saloonuser.Utils.Preferences;
import com.prometteur.saloonuser.listener.OnTabRemoveListener;

import java.util.ArrayList;

public class AppointmentListPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private ArrayList<String> tabStringArrayList = new ArrayList<>();
    OnTabRemoveListener onTabRemoveListener;
    public AppointmentListPagerAdapter(FragmentManager fragmentManager, OnTabRemoveListener onTabRemoveListener)
    {
        super(fragmentManager);
        this.onTabRemoveListener=onTabRemoveListener;
    }

    @Override
    public Fragment getItem(int position) {
        //return null;
        return fragmentArrayList.get(position);
    }

    @Override
    public int getCount() {
        //return 0;
        return fragmentArrayList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        //return super.getPageTitle(position);
        return tabStringArrayList.get(position);
    }

    public void addCompetetorTabFragments(Fragment tabFragment, String tabTitle)
    {
        fragmentArrayList.add(tabFragment);
        tabStringArrayList.add(tabTitle);
        notifyDataSetChanged();
    }
    public void removeCompetetorTabFragments(Fragment tabFragment)
    {
        fragmentArrayList.remove(tabFragment);

        Intent intent1=new Intent("SendToService");
        tabFragment.getContext().sendBroadcast(intent1);
        //getBottomNavigationCount(tabFragment.getContext(), bottomNavigationView);
        notifyDataSetChanged();
        onTabRemoveListener.onTabRemoved(fragmentArrayList.size());
        //tabStringArrayList.remove(tabTitle);
    }


    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }
}
