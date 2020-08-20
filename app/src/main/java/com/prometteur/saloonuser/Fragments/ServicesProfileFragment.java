package com.prometteur.saloonuser.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.prometteur.saloonuser.Adapter.TopServiceDetailsAdapter;
import com.prometteur.saloonuser.Model.SalonDetailBean;
import com.prometteur.saloonuser.R;


import java.util.ArrayList;
import java.util.List;

import static com.prometteur.saloonuser.Activities.ActivitySalonProfile.topItem;


public class ServicesProfileFragment extends BottomSheetDialogFragment {

    Context mContext;
    RecyclerView recyclerView;
    TopServiceDetailsAdapter adapter;
    TextView tvServiceName, tvTypeBadge;
    ImageView ivClose;
    private List<SalonDetailBean.Service> mDataList = new ArrayList<>();
    private LinearLayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard
        return inflater.inflate(R.layout.fragment_profile_services, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        tvServiceName = view.findViewById(R.id.tvServiceName);
        tvTypeBadge = view.findViewById(R.id.tvTypeBadge);
        ivClose = view.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setDataListItems();

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }


    private void setDataListItems() {
        tvServiceName.setText(topItem.getSrvcCategory());
        tvTypeBadge.setText(topItem.getTypes());
        mDataList = topItem.getServices();
        initRecyclerView();
    }

    private void initRecyclerView() {
        initAdapter();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //if(recyclerView.getChildAt(0).top < 0) dropshadow.setVisible(); else dropshadow.setGone();
            }
        });
    }

    private void initAdapter() {

        mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);

        adapter = new TopServiceDetailsAdapter(mContext, mDataList);
        recyclerView.setAdapter(adapter);
    }


}
