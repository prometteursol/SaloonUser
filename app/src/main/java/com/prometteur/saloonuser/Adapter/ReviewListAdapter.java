package com.prometteur.saloonuser.Adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prometteur.saloonuser.Activities.RatingActivity;
import com.prometteur.saloonuser.Model.AppointDetailBean;
import com.prometteur.saloonuser.Model.ReviewDetailsBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.TextViewCustomFont;

import java.util.List;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ViewHolder> {

    Activity nActivity;
    List<ReviewDetailsBean.Operator> operators;
    String branchName;
    public ReviewListAdapter(Activity nActivity, List<ReviewDetailsBean.Operator> operators) {
        this.nActivity = nActivity;
        this.operators = operators;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(nActivity).inflate(R.layout.recycle_review_operator,parent, false);
        return new ReviewListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.tvServiceOperator.setText(operators.get(position).getUserFName()+" "+operators.get(position).getUserLName());
        holder.tvSalonName.setText("("+operators.get(position).getSrvcName()+")");
        holder.ratingBarOptName1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                RatingActivity.operators.get(position).setRating(v);
            }
        });

    }

    @Override
    public int getItemCount() {
        return operators.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextViewCustomFont tvSalonName;
        TextViewCustomFont tvServiceOperator;
RatingBar ratingBarOptName1;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            tvSalonName = itemView.findViewById(R.id.tvSalonName);
            tvServiceOperator = itemView.findViewById(R.id.tvServiceOperator);
            ratingBarOptName1 = itemView.findViewById(R.id.ratingBarOptName1);

        }
    }
}
