package com.prometteur.saloonuser.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.prometteur.saloonuser.Model.AppointDetailBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.TextViewCustomFont;

import java.util.List;

public class PromoOffAppDetailsServicesAdapter extends RecyclerView.Adapter<PromoOffAppDetailsServicesAdapter.ViewHolder> {

    AppCompatActivity nActivity;

    boolean bottomlist;
    List<AppointDetailBean.Service> results;

    public PromoOffAppDetailsServicesAdapter(AppCompatActivity nActivity, List<AppointDetailBean.Service> results, boolean bottomlist) {
        this.nActivity = nActivity;
        this.results=results;
        this.bottomlist=bottomlist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(nActivity).inflate(R.layout.recycle_services_selected,
                parent, false);

        return new PromoOffAppDetailsServicesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {


        holder.tvSerialNo.setText("0"+(position+1)+".");
        holder.tvService.setText(results.get(position).getSrvcName());
        if(results.get(position).getBrndName()!=null) {
            holder.tvServiceBrand.setText("" + results.get(position).getBrndName());
        }else
        {
            holder.tvServiceBrandtitle.setVisibility(View.GONE);
            holder.tvServiceBrand.setVisibility(View.GONE);
        }
        holder.tvServiceCost.setText("₹ " +results.get(position).getSrvcPrice());

        if(results.get(position).getUserFName()!=null && results.get(position).getUserLName()!=null) {
            holder.tvServiceOperator.setText("" + results.get(position).getUserFName() + " " + results.get(position).getUserLName());
        }else
        {
            holder.tvServiceOperator.setVisibility(View.GONE);
            holder.tvServiceOperatorTitle.setVisibility(View.GONE);
        }
        //getStrikeString(holder.tvMainPrice);

      //  getResizedDrawable(nActivity,R.drawable.ic_clock_blue,holder.tvTime,null,null,R.dimen._12sdp);


    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextViewCustomFont tvSerialNo;
        TextViewCustomFont tvService;
        TextViewCustomFont tvServiceBrand;
        TextViewCustomFont tvServiceOperator;
        TextViewCustomFont tvAddedOperatorName;
        TextViewCustomFont tvServiceCost;
        TextViewCustomFont tvServiceBrandtitle;
        TextViewCustomFont tvServiceOperatorTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSerialNo = itemView.findViewById(R.id.tvSerialNo);
            tvService = itemView.findViewById(R.id.tvService);
            tvServiceBrand = itemView.findViewById(R.id.tvServiceBrand);
            tvServiceOperator = itemView.findViewById(R.id.tvServiceOperator);
            tvAddedOperatorName = itemView.findViewById(R.id.tvAddedOperatorName);
            tvServiceCost = itemView.findViewById(R.id.tvServiceCost);
            tvServiceBrandtitle = itemView.findViewById(R.id.tvServiceBrandtitle);
            tvServiceOperatorTitle = itemView.findViewById(R.id.tvServiceOperatorTitle);
        }
    }


}
