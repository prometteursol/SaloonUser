package com.prometteur.saloonuser.Fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prometteur.saloonuser.Adapter.SalonInfoGalleryAdapter;
import com.prometteur.saloonuser.Model.SalonDetailBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.databinding.FragmentSalonGalleryBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.prometteur.saloonuser.Activities.ActivitySalonProfile.salonDetailBean;
import static com.prometteur.saloonuser.Utils.Utils.setEmptyMsg;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSalonGallery extends Fragment {

    FragmentSalonGalleryBinding salonGalleryBinding;
    AppCompatActivity nActivity;
    List<String> mDataList;
    public FragmentSalonGallery() {
        // Required empty public constructor
    }

    public FragmentSalonGallery(AppCompatActivity nActivity) {
        this.nActivity = nActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        salonGalleryBinding=FragmentSalonGalleryBinding.inflate(inflater,container,false);
        View view=salonGalleryBinding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDataList=new ArrayList<>();
        if(salonDetailBean!=null) {
            if(salonDetailBean.getResult().size()!=0) {
                SalonDetailBean.Result result = salonDetailBean.getResult().get(0);

                if (!result.getBranGallaryImg().isEmpty()) {
                    String[] imgArr = result.getBranGallaryImg().split(",");
                    mDataList = Arrays.asList(imgArr);
                }
                GridLayoutManager gridLayoutManager = new GridLayoutManager(nActivity, 3);

                salonGalleryBinding.recycleSalonGallery.setLayoutManager(gridLayoutManager);
                try {
                    salonGalleryBinding.recycleSalonGallery.setAdapter(new SalonInfoGalleryAdapter(nActivity, mDataList));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            salonGalleryBinding.layNoData.ivNoData.setImageResource(R.drawable.img_no_photos_empty);
            setEmptyMsg(mDataList, salonGalleryBinding.recycleSalonGallery, salonGalleryBinding.layNoData.ivNoData);
        }
    }
}
