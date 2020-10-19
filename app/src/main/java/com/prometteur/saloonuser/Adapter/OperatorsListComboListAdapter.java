package com.prometteur.saloonuser.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.prometteur.saloonuser.Model.ComboOffBean;
import com.prometteur.saloonuser.Model.ServicesBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.TextViewCustomFont;

import java.util.List;

public class OperatorsListComboListAdapter extends RecyclerView.Adapter<OperatorsListComboListAdapter.ViewHolder> {

    Activity nActivity;
    private final OnItemClickListener listener;
    int selectedPosition = -1;
    List<ComboOffBean.Operator> operators;
    public OperatorsListComboListAdapter(Activity nActivity, List<ComboOffBean.Operator> operators, OnItemClickListener listener) {
        this.nActivity = nActivity;
        this.operators = operators;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, String OperatorName);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(nActivity).inflate(R.layout.recycle_operators_list,
                parent, false);
        return new OperatorsListComboListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

       // holder.bind(position, listener, holder.tvOperatorName.getText().toString(), holder.conlayMainOperatorList);
        holder.tvOperatorName.setText(operators.get(position).getUserFName()+" "+operators.get(position).getUserLName());
        Glide.with(nActivity).load(operators.get(position).getUserImg()).placeholder(R.drawable.placeholder_gray_circle).error(R.drawable.placeholder_gray_circle).into(holder.civ_OperatorImage);
        holder.conlayMainOperatorList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // holder.bind(position, listener, holder.tvOperatorName.getText().toString(), holder.conlayMainOperatorList);
                if(operators.get(position).getSelected().equalsIgnoreCase("selected"))
                {
                    //operators.get(position).setSelected("");
                    selectedPosition=-1;
                }else
                {
                    for(int i=0;i<operators.size();i++){
                        operators.get(i).setSelected("");
                    }
                    selectedPosition = position;
                }
                listener.onItemClick(position,holder.tvOperatorName.getText().toString());
                notifyDataSetChanged();
            }
        });
        if(operators.get(position).getSelected().equalsIgnoreCase("selected"))
        {
            selectedPosition = position;
        }
        if (selectedPosition == position) {
            selectedPosition = position;
            holder.civ_OperatorImage.setBorderColor(nActivity.getResources().getColor(R.color.skyBluelilDark));
            holder.tvOperatorName.setTextColor(nActivity.getResources().getColor(R.color.skyBluelilDark));
        } else {
            holder.civ_OperatorImage.setBorderColor(nActivity.getResources().getColor(R.color.grey));
            holder.tvOperatorName.setTextColor(nActivity.getResources().getColor(R.color.grey));

        }
        if(operators.get(position).getOperatorRating()!=null) {
            holder.rbOptRating.setRating(Float.parseFloat(operators.get(position).getOperatorRating()));
        }else
        {
            holder.rbOptRating.setRating(0);
        }

    }

    @Override
    public int getItemCount() {
        return operators.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout conlayMainOperatorList;
        CircularImageView civ_OperatorImage;
        TextViewCustomFont tvOperatorName;
        RatingBar rbOptRating;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            conlayMainOperatorList = itemView.findViewById(R.id.conlayMainOperatorList);
            civ_OperatorImage = itemView.findViewById(R.id.civ_OperatorImage);
            tvOperatorName = itemView.findViewById(R.id.tvOperatorName);
            rbOptRating = itemView.findViewById(R.id.rbOptRating);
        }

        /*public void bind(final int position, final OnItemClickListener listener, final String operatorName, final ConstraintLayout conlayhighlighting) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position, operatorName);

                    notifyDataSetChanged();
                }
            });
        }*/
    }

}
