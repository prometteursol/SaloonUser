package com.prometteur.saloonuser.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.prometteur.saloonuser.Activities.ImageEnlargeActivity;
import com.prometteur.saloonuser.R;

import java.util.List;

public class SalonInfoGalleryAdapter extends RecyclerView.Adapter<SalonInfoGalleryAdapter.TimeLineViewHolder> {

    AppCompatActivity nActivity;
    List<String> mDataList;
    public SalonInfoGalleryAdapter(AppCompatActivity nActivity, List<String> mDataList)
    {
        this.nActivity=nActivity;
        this.mDataList=mDataList;

    }

    @NonNull
    @Override
    public TimeLineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(nActivity).inflate(R.layout.recycle_salon_gallery,
                parent, false);
        return new SalonInfoGalleryAdapter.TimeLineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeLineViewHolder holder, final int position) {
        Glide.with(nActivity).load(mDataList.get(position)).into(holder.profileImage);
        holder.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                Intent largeVP = new Intent(nActivity, ImageEnlargeActivity.class);
                bundle.putInt("pager_position", position);
                ImageEnlargeActivity.GalaryPhotos = mDataList;
                largeVP.putExtras(bundle);
                nActivity.startActivity(largeVP);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }


    public class TimeLineViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView profileImage;

        public TimeLineViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profile_image);


        }
    }
}
