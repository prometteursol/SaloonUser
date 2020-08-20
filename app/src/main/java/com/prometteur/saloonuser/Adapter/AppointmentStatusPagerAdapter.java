package com.prometteur.saloonuser.Adapter;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.prometteur.saloonuser.Fragments.FragmentAppointmantCancelled;
import com.prometteur.saloonuser.Fragments.FragmentAppointmentCompleted;

public class AppointmentStatusPagerAdapter extends FragmentStatePagerAdapter {

    int noOfTabs;
    Activity nActivity;

    public AppointmentStatusPagerAdapter(FragmentManager fm,int noOfTabs,Activity nActivity) {
        super(fm);
        this.noOfTabs=noOfTabs;
        this.nActivity=nActivity;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                FragmentAppointmentCompleted fragmentAppointmentCompleted=new FragmentAppointmentCompleted(nActivity);
                return fragmentAppointmentCompleted;

            case 1:
                FragmentAppointmantCancelled fragmentAppointmentCancelled=new FragmentAppointmantCancelled(nActivity);
                return fragmentAppointmentCancelled;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return noOfTabs;
    }
}
