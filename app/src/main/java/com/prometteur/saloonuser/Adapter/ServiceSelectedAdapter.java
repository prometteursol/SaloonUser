package com.prometteur.saloonuser.Adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prometteur.saloonuser.Model.AppointDetailBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.TextViewCustomFont;

import java.util.List;

import static com.prometteur.saloonuser.Constants.ConstantMethods.getStrikeString;

public class ServiceSelectedAdapter extends RecyclerView.Adapter<ServiceSelectedAdapter.ViewHolder> {

    Activity nActivity;
    List<AppointDetailBean.Service> services;
    public ServiceSelectedAdapter(Activity nActivity, List<AppointDetailBean.Service> services) {
        this.nActivity = nActivity;
        this.services = services;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(nActivity).inflate(R.layout.recycle_services_selected,
                parent, false);
        return new ServiceSelectedAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvSerialNo.setText("0"+(position+1)+".");
        holder.tvService.setText(services.get(position).getSrvcName());
        if(services.get(position).getBrndName()!=null) {
            holder.tvServiceBrand.setText(services.get(position).getBrndName());
        }else
        {
            holder.tvServiceBrand.setVisibility(View.GONE);
            holder.tvServiceBrandtitle.setVisibility(View.GONE);
        }
        if(services.get(position).getUserFName()!=null && services.get(position).getUserLName()!=null) {
            holder.tvServiceOperator.setText(services.get(position).getUserFName() + " " + services.get(position).getUserLName());
        }else
        {
            holder.tvServiceOperator.setVisibility(View.GONE);
            holder.tvServiceOperatorTitle.setVisibility(View.GONE);
        }
        holder.tvServiceCost.setText("₹ " +services.get(position).getSrvcPrice());
        holder.tvServiceCost.setTextColor(nActivity.getResources().getColor(R.color.black));
        if(services.get(position).getSrvcDiscountPrice()!=null && !services.get(position).getSrvcDiscountPrice().isEmpty() && !services.get(position).getSrvcDiscountPrice().equalsIgnoreCase("0.00")) {
            holder.tvServiceDiscountCost.setText("₹ " + services.get(position).getSrvcPrice());
            holder.tvServiceCost.setText("₹ " +services.get(position).getSrvcDiscountPrice());
            holder.tvServiceCost.setTextColor(nActivity.getResources().getColor(R.color.skyBlueLight));
            holder.tvServiceDiscountCost.setVisibility(View.VISIBLE);
            getStrikeString(holder.tvServiceDiscountCost);
        }else
        {
            holder.tvServiceDiscountCost.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextViewCustomFont tvSerialNo;
        TextViewCustomFont tvService;
        TextViewCustomFont tvServiceCost;
        TextViewCustomFont tvServiceDiscountCost;
        TextViewCustomFont tvServiceBrandtitle;
        TextViewCustomFont tvServiceOperatorTitle;
        TextViewCustomFont tvServiceBrand;
        TextViewCustomFont tvServiceOperator;


        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            tvSerialNo = itemView.findViewById(R.id.tvSerialNo);
            tvService = itemView.findViewById(R.id.tvService);
            tvServiceCost = itemView.findViewById(R.id.tvServiceCost);
            tvServiceBrand = itemView.findViewById(R.id.tvServiceBrand);
            tvServiceBrandtitle = itemView.findViewById(R.id.tvServiceBrandtitle);
            tvServiceOperator = itemView.findViewById(R.id.tvServiceOperator);
            tvServiceOperatorTitle = itemView.findViewById(R.id.tvServiceOperatorTitle);
            tvServiceDiscountCost = itemView.findViewById(R.id.tvServiceDiscountCost);

        }
    }
}
