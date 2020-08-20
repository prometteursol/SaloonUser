package com.prometteur.saloonuser.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.prometteur.saloonuser.Activities.ActivitySalonProfile;
import com.prometteur.saloonuser.Model.SearchBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.Preferences;
import com.prometteur.saloonuser.Utils.TextViewCustomFont;

import java.util.ArrayList;
import java.util.List;

public class SalonAddressResultsAdapter extends RecyclerView.Adapter<SalonAddressResultsAdapter.ViewHolder> {

    Context nContext;
    List<SearchBean.Result> result;


    public SalonAddressResultsAdapter(Context nContext, List<SearchBean.Result> result) {
        this.nContext = nContext;
        this.result = result;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(nContext).inflate(R.layout.recycle_salon_address_results,
                parent, false);
        return new SalonAddressResultsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tvSalonName.setText(result.get(position).getBranName());
        holder.tvSalonLocation.setText(result.get(position).getBranAddr()+", "+result.get(position).getBranCity());
        Glide.with(nContext).load(result.get(position).getBranImg()).into(holder.ivSalonProfileImg);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Preferences.setPreferenceValue(nContext, "mainCat",result.get(position).getBranCatMain());
                nContext.startActivity(new Intent(nContext, ActivitySalonProfile.class).putExtra("branchId", result.get(position).getBranId()).putExtra("mainCat",result.get(position).getBranCatMain()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return result.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivSalonProfileImg;
        TextViewCustomFont tvSalonName,tvSalonLocation;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivSalonProfileImg=itemView.findViewById(R.id.ivSalonProfileImg);
            tvSalonName=itemView.findViewById(R.id.tvSalonName);
            tvSalonLocation=itemView.findViewById(R.id.tvSalonLocation);
        }
    }
}
