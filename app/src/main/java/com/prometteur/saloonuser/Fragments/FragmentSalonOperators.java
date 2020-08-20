package com.prometteur.saloonuser.Fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prometteur.saloonuser.Adapter.SalonInfoOperatorsAdapter;
import com.prometteur.saloonuser.databinding.FragmentSalonOperatorsBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSalonOperators extends Fragment {

    FragmentSalonOperatorsBinding salonOperatorsBinding;
    Activity nActivity;

    public FragmentSalonOperators(Activity nActivity) {
        this.nActivity = nActivity;
    }

    public FragmentSalonOperators() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        salonOperatorsBinding=FragmentSalonOperatorsBinding.inflate(inflater,container,false);
        View view=salonOperatorsBinding.getRoot();

        salonOperatorsBinding.recycleOperatorInfo.setLayoutManager(new LinearLayoutManager(nActivity));
        salonOperatorsBinding.recycleOperatorInfo.setAdapter(new SalonInfoOperatorsAdapter(nActivity));
        return view;
    }
}
