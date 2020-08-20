package com.prometteur.saloonuser.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.listener.OnTimeslotItemClickListener;

import java.util.List;

public class BookingTimeSlotAdapter extends RecyclerView.Adapter<BookingTimeSlotAdapter.ViewHolder> {
    Activity nActivity;
    int index=-1;
    List<String> timeSlots;
    OnTimeslotItemClickListener listener;
    public BookingTimeSlotAdapter(Activity nActivity, List<String> timeSlots, OnTimeslotItemClickListener listener) {
        this.nActivity = nActivity;
        this.timeSlots = timeSlots;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(nActivity).inflate(R.layout.recycle_time_slot_row,
                parent,
                false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tvSlotTime.setText(timeSlots.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index=position;
                listener.onItemClick(timeSlots.get(position));
                notifyDataSetChanged();
            }
        });
        if(index==position){
            holder.tvSlotTime.setBackground(nActivity.getResources().getDrawable(R.drawable.row_rounded_time_slot_background_light_blue));
            holder.tvSlotTime.setTextColor(nActivity.getResources().getColor(R.color.skyBlueLight));
        }else{
            holder.tvSlotTime.setBackground(nActivity.getResources().getDrawable(R.drawable.row_rounded_time_slot_background_white));
            holder.tvSlotTime.setTextColor(nActivity.getResources().getColor(R.color.grey));

        }
    }

    @Override
    public int getItemCount() {
        return timeSlots.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSlotTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSlotTime = itemView.findViewById(R.id.tvSlotTime);

        }
    }
}
