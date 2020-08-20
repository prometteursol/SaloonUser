package com.prometteur.saloonuser.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.prometteur.saloonuser.Model.CartDetailBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.TextViewCustomFont;

import java.util.List;

public class ComboCartAdapter extends RecyclerView.Adapter<ComboCartAdapter.ViewHolder> {

    AppCompatActivity nActivity;

    boolean bottomlist;
    List<List<CartDetailBean.Service>> results;

    public ComboCartAdapter(AppCompatActivity nActivity, List<List<CartDetailBean.Service>> results, boolean bottomlist) {
        this.nActivity = nActivity;
        this.results=results;
        this.bottomlist=bottomlist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(nActivity).inflate(R.layout.recycle_service_combo,
                parent, false);

        return new ComboCartAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.tvServiceName.setText(results.get(position).get(0).getSrvcName());
        if(results.get(position).get(0).getBrndName()!=null) {
            holder.tvDesc.setText("Brand : " + results.get(position).get(0).getBrndName());
        }else
        {
            holder.tvDesc.setVisibility(View.INVISIBLE);
            holder.tvDesc.setText("Brand : NA");
        }
        holder.tvOfferPrice.setText("₹ " +results.get(position).get(0).getSrvcPrice());
        boolean isOperator=false;
        for(int i=0;i<results.get(position).get(0).getOperators().size();i++) {
            if(results.get(position).get(0).getOperators().get(i).getSelected().equalsIgnoreCase("selected")) {
                holder.tvAddRemoveOperator.setText("Op : " + results.get(position).get(0).getOperators().get(i).getUserFName() + " " + results.get(position).get(0).getOperators().get(i).getUserLName());
                isOperator=true;
            }
        }
        if(!isOperator)
        {
            holder.tvAddRemoveOperator.setVisibility(View.GONE);
        }
        //getStrikeString(holder.tvMainPrice);

      //  getResizedDrawable(nActivity,R.drawable.ic_clock_blue,holder.tvTime,null,null,R.dimen._12sdp);


    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextViewCustomFont tvServiceName;
        TextViewCustomFont tvDesc;
        TextViewCustomFont tvAddRemoveOperator;
        TextViewCustomFont tvAddedOperatorName;
        TextViewCustomFont tvOfferPrice;
        TextViewCustomFont tvMainPrice;
        TextViewCustomFont tvTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvServiceName = itemView.findViewById(R.id.tvServiceName);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            tvAddRemoveOperator = itemView.findViewById(R.id.tvAddRemoveOperator);
            tvAddedOperatorName = itemView.findViewById(R.id.tvAddedOperatorName);
            tvOfferPrice = itemView.findViewById(R.id.tvOfferPrice);
            tvMainPrice = itemView.findViewById(R.id.tvMainPrice);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }


}
