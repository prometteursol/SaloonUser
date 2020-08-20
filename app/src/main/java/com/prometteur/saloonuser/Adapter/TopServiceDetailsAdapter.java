package com.prometteur.saloonuser.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.prometteur.saloonuser.Model.SalonDetailBean;
import com.prometteur.saloonuser.R;

import java.util.List;

import static com.prometteur.saloonuser.Constants.ConstantMethods.getResizedDrawable;
import static com.prometteur.saloonuser.Constants.ConstantMethods.getStrikeString;

public class TopServiceDetailsAdapter extends RecyclerView.Adapter<TopServiceDetailsAdapter.TimeLineViewHolder> {
    Context mContext;
    List<SalonDetailBean.Service> mDataList;

    public TopServiceDetailsAdapter(Context mContext, List mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
    }

    @NonNull
    @Override
    public TimeLineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.rate_card_row,
                parent,
                false);
        return new TimeLineViewHolder(v, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeLineViewHolder holder, int position) {

            holder.tvName.setText(mDataList.get(position).getSrvcName());

        if(!mDataList.get(position).getSrvcDiscountPrice().isEmpty()) {
            holder.tvMainPrice.setText("₹ " + mDataList.get(position).getSrvcPrice());
            holder.tvOfferPrice.setText("₹ " + mDataList.get(position).getSrvcDiscountPrice());
        }else
        {
            holder.tvOfferPrice.setText("₹ " + mDataList.get(position).getSrvcPrice());
            holder.tvMainPrice.setVisibility(View.GONE);
        }
            holder.tvTime.setText( mDataList.get(position).getSrvcEstimateTime()+" Min");
            if(mDataList.get(position).getBrndName()!=null) {
                holder.tvBrand.setText(mDataList.get(position).getBrndName().toString());
            }else {
                holder.tvBrand.setText("-");
            }



        getStrikeString(holder.tvMainPrice);
        getResizedDrawable(mContext, R.drawable.ic_clock_blue, holder.tvTime, null, null, R.dimen._11sdp);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class TimeLineViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvBrand;
        TextView tvMainPrice;
        TextView tvOfferPrice;
        TextView tvTime;

        public TimeLineViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvServiceName);
            tvBrand = itemView.findViewById(R.id.tvBrand);
            tvMainPrice = itemView.findViewById(R.id.tvMainPrice);
            tvOfferPrice = itemView.findViewById(R.id.tvOfferPrice);
            tvTime = itemView.findViewById(R.id.tvTime);


        }
    }


}
