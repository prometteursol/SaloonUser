package com.prometteur.saloonuser.Adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class ServicesTabAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private ArrayList<String> tabStringArrayList = new ArrayList<>();

    public ServicesTabAdapter(FragmentManager fragmentManager)
    {
        super(fragmentManager);
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
    }
}
