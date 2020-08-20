package com.prometteur.saloonuser.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prometteur.saloonuser.Adapter.SalonInfoReviewAdapter;
import com.prometteur.saloonuser.Model.SalonDetailBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.databinding.FragmentSalonReviewBinding;

import java.util.ArrayList;
import java.util.List;

import static com.prometteur.saloonuser.Activities.ActivitySalonProfile.salonDetailBean;
import static com.prometteur.saloonuser.Utils.Utils.setEmptyMsg;

public class FragmentSalonReview extends Fragment {

    FragmentSalonReviewBinding salonReviewBinding;
    Activity nActivity;

    public FragmentSalonReview(Activity nActivity) {
        this.nActivity = nActivity;
    }

    public FragmentSalonReview() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        salonReviewBinding=FragmentSalonReviewBinding.inflate(inflater,container,false);
        View view=salonReviewBinding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setDataListItems();
    }
    private List<SalonDetailBean.Review> mDataList = new ArrayList<>();
    private void setDataListItems() {
        if(salonDetailBean!=null) {
            if(salonDetailBean.getResult().size()!=0) {
                SalonDetailBean.Result result = salonDetailBean.getResult().get(0);
                mDataList = result.getReviews();
                if (mDataList.size() != 0) {
                    salonReviewBinding.tvReviewCount.setText("All Reviews (" + mDataList.size() + ")");
                    salonReviewBinding.tvReviewCount.setVisibility(View.VISIBLE);
                } else {
                    salonReviewBinding.tvReviewCount.setVisibility(View.GONE);
                }
                salonReviewBinding.recycleReviewProfile.setLayoutManager(new LinearLayoutManager(nActivity));
                salonReviewBinding.recycleReviewProfile.setAdapter(new SalonInfoReviewAdapter(nActivity, mDataList));
            }

        }
        salonReviewBinding.layNoData.ivNoData.setImageResource(R.drawable.img_no_reviews_empty);
        setEmptyMsg(mDataList, salonReviewBinding.recycleReviewProfile, salonReviewBinding.layNoData.ivNoData);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        nActivity= (Activity) context;
    }
}
