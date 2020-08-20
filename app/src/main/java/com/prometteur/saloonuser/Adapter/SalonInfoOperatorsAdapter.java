package com.prometteur.saloonuser.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prometteur.saloonuser.R;

public class SalonInfoOperatorsAdapter extends RecyclerView.Adapter<SalonInfoOperatorsAdapter.ViewHolder> {
    Activity nActivity;

    public SalonInfoOperatorsAdapter(Activity nActivity) {
        this.nActivity = nActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(nActivity).inflate(R.layout.recycle_operator_info,
                parent, false);
        return new SalonInfoOperatorsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
