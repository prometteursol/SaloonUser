package com.prometteur.saloonuser.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.prometteur.saloonuser.Model.SalonDetailBean;
import com.prometteur.saloonuser.R;

import java.util.List;

import static com.prometteur.saloonuser.Utils.Utils.getReviewDate;

public class SalonInfoReviewAdapter extends RecyclerView.Adapter<SalonInfoReviewAdapter.TimeLineViewHolder> {

    Activity nActivity;
    List<SalonDetailBean.Review> mDataList;
    public SalonInfoReviewAdapter(Activity nActivity, List<SalonDetailBean.Review> mDataList) {
        this.nActivity = nActivity;
        this.mDataList = mDataList;
    }

    @NonNull
    @Override
    public TimeLineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(nActivity).inflate(R.layout.recycle_review_profile,
                parent, false);
        return new SalonInfoReviewAdapter.TimeLineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeLineViewHolder holder, int position) {
        holder.tvName.setText(mDataList.get(position).getUserFName()+" "+mDataList.get(position).getUserLName());
        holder.tvDuration.setText(getReviewDate(nActivity,mDataList.get(position).getRevCreateDate()));
        if(!mDataList.get(position).getRevReview().isEmpty()) {
            holder.tvReview.setText(mDataList.get(position).getRevReview());
        }else
        {
            holder.tvReview.setVisibility(View.GONE);
        }
        holder.rbRatings.setRating(Float.parseFloat(mDataList.get(position).getRevRating()));

        if(mDataList.get(position).getUserImg()!=null && !mDataList.get(position).getUserImg().isEmpty()) {
            Glide.with(nActivity).load(mDataList.get(position).getUserImg()).into(holder.profileImage);
        }else
        {
            Glide.with(nActivity).load(R.drawable.img_profile).into(holder.profileImage);
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class TimeLineViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvDuration,tvReview;
        RatingBar rbRatings;
        CircularImageView profileImage;

        public TimeLineViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            rbRatings = itemView.findViewById(R.id.rbSalonRating);
            tvReview = itemView.findViewById(R.id.tvReview);
            profileImage = itemView.findViewById(R.id.profile_image);
        }
    }
}
