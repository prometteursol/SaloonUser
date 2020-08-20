package com.prometteur.saloonuser.Adapter;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.prometteur.saloonuser.Fragments.FragmentComboPage;
import com.prometteur.saloonuser.Fragments.FragmentOfferPage;

public class ComboOfferPagerAdapter extends FragmentStatePagerAdapter {

    int noOfTabs;
    AppCompatActivity nActivity;

    public ComboOfferPagerAdapter(FragmentManager fm, int noOfTabs, AppCompatActivity nActivity) {
        super(fm);
        this.noOfTabs=noOfTabs;
        this.nActivity=nActivity;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                FragmentComboPage fragmentComboPage=new FragmentComboPage(nActivity);
                return fragmentComboPage;

            case 1:
                FragmentOfferPage fragmentOfferPage=new FragmentOfferPage(nActivity);
                return fragmentOfferPage;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return noOfTabs;
    }
}
