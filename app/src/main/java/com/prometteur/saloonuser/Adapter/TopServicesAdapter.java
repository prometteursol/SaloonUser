package com.prometteur.saloonuser.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prometteur.saloonuser.Constants.ConstantVariables;
import com.prometteur.saloonuser.Model.SalonDetailBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.TextViewCustomFont;

import java.util.List;

public class TopServicesAdapter extends RecyclerView.Adapter<TopServicesAdapter.ViewHolder> {

    Activity nActivity;
    private final OnItemClickListener listener;
    List<SalonDetailBean.Topservice> topservices;
    public TopServicesAdapter(Activity nActivity, List<SalonDetailBean.Topservice> topservices, OnItemClickListener listener) {
        this.nActivity = nActivity;
        this.listener=listener;
        this.topservices=topservices;
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(nActivity).inflate(R.layout.recycle_top_services_row,
                parent, false);
        return new TopServicesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position,listener);
        holder.linlayItem.setBackgroundColor(nActivity.getResources().getColor(ConstantVariables.colorList[position]));
        holder.tvServices.setText(topservices.get(position).getSrvcCategory());
        if(topservices.get(position).getTypes().equalsIgnoreCase("1")) {
            holder.tvTypes.setText(topservices.get(position).getTypes() + " Type");
        }else {
            holder.tvTypes.setText(topservices.get(position).getTypes() + " Types");
        }
    }

    @Override
    public int getItemCount() {
        return topservices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linlayItem;
        TextViewCustomFont tvServices;
        TextViewCustomFont tvTypes;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linlayItem=itemView.findViewById(R.id.linlayItem);
            tvServices=itemView.findViewById(R.id.tvServices);
            tvTypes=itemView.findViewById(R.id.tvTypes);
        }
        public void bind(final int position, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(position);
                }
            });
        }
    }
}
