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
import com.prometteur.saloonuser.Model.PointBalanceBean;
import com.prometteur.saloonuser.Model.ReviewDetailsBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.TextViewCustomFont;

import java.util.List;

import static com.prometteur.saloonuser.Utils.Utils.getPointsAddedDate;

public class PointBalanceListAdapter extends RecyclerView.Adapter<PointBalanceListAdapter.ViewHolder> {

    Activity nActivity;
    List<PointBalanceBean.Result> dataList;
    String branchName;
    public PointBalanceListAdapter(Activity nActivity, List<PointBalanceBean.Result> dataList) {
        this.nActivity = nActivity;
        this.dataList = dataList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(nActivity).inflate(R.layout.recycle_point_balance_row,parent, false);
        return new PointBalanceListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

       holder.tvPoints.setText(""+Math.round(Double.parseDouble(dataList.get(position).getRedmPoints())));
       holder.tvDate.setText(getPointsAddedDate(dataList.get(position).getRedmCreateDate()));
       if(dataList.get(position).getRedmStatus().equalsIgnoreCase("0")) {
           holder.tvStatus.setText("Used");
       }else
       {
           holder.tvStatus.setText("Added");
       }


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextViewCustomFont tvStatus;
        TextViewCustomFont tvDate;
        TextViewCustomFont tvPoints;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvPoints = itemView.findViewById(R.id.tvPoints);

        }
    }
}
