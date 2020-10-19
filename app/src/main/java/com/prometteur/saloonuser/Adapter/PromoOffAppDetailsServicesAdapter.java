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

import static com.prometteur.saloonuser.Constants.ConstantMethods.getStrikeString;

public class PromoOffAppDetailsServicesAdapter extends RecyclerView.Adapter<PromoOffAppDetailsServicesAdapter.ViewHolder> {

    AppCompatActivity nActivity;

    boolean bottomlist;
    List<AppointDetailBean.Service> results;
    AppointDetailBean.PromotionalService promotionalServices;
    public PromoOffAppDetailsServicesAdapter(AppCompatActivity nActivity, AppointDetailBean.PromotionalService promotionalServices, List<AppointDetailBean.Service> results, boolean bottomlist) {
        this.nActivity = nActivity;
        this.promotionalServices=promotionalServices;
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
        if(promotionalServices.getProofferDiscountPrice()!=null && !promotionalServices.getProofferDiscountPrice().isEmpty() && !promotionalServices.getProofferDiscountPrice().equalsIgnoreCase("0.00")) {
            holder.tvServiceDiscountCost.setText("₹ " + promotionalServices.getProofferPrice());
            holder.tvServiceCost.setText("₹ " +promotionalServices.getProofferDiscountPrice());
            holder.tvServiceCost.setTextColor(nActivity.getResources().getColor(R.color.skyBlueLight));
            holder.tvServiceDiscountCost.setVisibility(View.VISIBLE);
            getStrikeString(holder.tvServiceDiscountCost);
        }else
        {
            holder.tvServiceDiscountCost.setVisibility(View.GONE);
        }

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
        TextViewCustomFont tvServiceDiscountCost;
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
            tvServiceDiscountCost = itemView.findViewById(R.id.tvServiceDiscountCost);
            tvServiceBrandtitle = itemView.findViewById(R.id.tvServiceBrandtitle);
            tvServiceOperatorTitle = itemView.findViewById(R.id.tvServiceOperatorTitle);
        }
    }


}
