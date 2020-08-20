package com.prometteur.saloonuser.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prometteur.saloonuser.Model.ComboOffBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.TextViewCustomFont;

import java.util.List;

import static com.prometteur.saloonuser.Constants.ConstantMethods.getResizedDrawable;

public class ComboServiceListAdapter extends RecyclerView.Adapter<ComboServiceListAdapter.TimeLineViewHolder> {
    Context mContext;
    List<ComboOffBean.Service> mDataList;
   // OnItemClickListener listener;
    public ComboServiceListAdapter(Context mContext, List<ComboOffBean.Service> mDataList)
    {
        this.mContext=mContext;
        this.mDataList=mDataList;
        //this.listener=listener;

    }
    @NonNull
    @Override
    public TimeLineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.service_rate_card_row,
                parent,
                false);
        return new TimeLineViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeLineViewHolder holder, final int position) {
        getResizedDrawable(mContext,R.drawable.ic_tick_service,holder.tvServiceName,null,null,R.dimen._11sdp);
        holder.tvServiceName.setText(mDataList.get(position).getSrvcName());
        if(mDataList.get(position).getBrndName()!=null) {
            holder.tvBrand.setText(mDataList.get(position).getBrndName().toString());
        }else
        {
            holder.tvBrand.setText("-");
            holder.tvBrand.setVisibility(View.GONE);
            holder.tvBrandTitle.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class TimeLineViewHolder extends RecyclerView.ViewHolder {

TextViewCustomFont tvServiceName;
TextViewCustomFont tvBrand;
TextViewCustomFont tvBrandTitle;

        public TimeLineViewHolder(@NonNull View itemView) {
            super(itemView);
            tvServiceName=itemView.findViewById(R.id.tvServiceName);
            tvBrand=itemView.findViewById(R.id.tvBrand);
            tvBrandTitle=itemView.findViewById(R.id.tvBrandTitle);
        }
    }


}
