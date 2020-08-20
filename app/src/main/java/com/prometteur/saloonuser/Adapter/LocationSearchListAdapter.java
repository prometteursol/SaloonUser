package com.prometteur.saloonuser.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prometteur.saloonuser.R;

import java.util.ArrayList;

public class LocationSearchListAdapter extends RecyclerView.Adapter<LocationSearchListAdapter.ViewHolder> {
    private static final String TAG = "Predicted_Address";

    //Context nContext;
    ArrayList<String> SalonList;
    Activity nActivity;
    boolean isMapFragment;

    public LocationSearchListAdapter(Activity nActivity) {
        this.nActivity = nActivity;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(nActivity).inflate(R.layout.recycle_address_results,
                parent, false);
        return new LocationSearchListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 8;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

