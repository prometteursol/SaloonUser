package com.prometteur.saloonuser.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prometteur.saloonuser.Model.ComboOffBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.TextViewCustomFont;

import java.util.List;

public class OperatorsListComboAdapter extends RecyclerView.Adapter<OperatorsListComboAdapter.ViewHolder> {

    private final OnItemClickListener listener;
    Activity nActivity;
    int selectedPosition = -1;
    List<ComboOffBean.Service> operators;

    public OperatorsListComboAdapter(Activity nActivity, List<ComboOffBean.Service> operators, OnItemClickListener listener) {
        this.nActivity = nActivity;
        this.operators = operators;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(nActivity).inflate(R.layout.recycle_select_combo_operator,
                parent, false);
        return new OperatorsListComboAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        // holder.bind(position, listener, holder.tvOperatorName.getText().toString(), holder.conlayMainOperatorList);
        holder.tvServiceName.setText(operators.get(position).getSrvcName());

        if(operators.get(position).getOperators().size()==0){
            holder.itemView.setVisibility(View.GONE);
            holder.tvServiceName.setVisibility(View.GONE);
            holder.recycleOperatorsList.setVisibility(View.GONE);
        }else
        {
            //holder.itemView.setVisibility(View.VISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(nActivity);
            layoutManager.setOrientation(RecyclerView.HORIZONTAL);
            holder.recycleOperatorsList.setLayoutManager(layoutManager);
            holder.recycleOperatorsList.setAdapter(new OperatorsListComboListAdapter(nActivity, operators.get(position).getOperators(), new OperatorsListComboListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int pos, String OperatorName) {
                    if(operators.get(position).getOperators().get(pos).getSelected().equalsIgnoreCase("selected"))
                    {
                        operators.get(position).getOperators().get(pos).setSelected("");
                    }else {
                        operators.get(position).getOperators().get(pos).setSelected("selected");
                    }
                }
            }));
        }



    }

    @Override
    public int getItemCount() {
        return operators.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position, String OperatorName);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextViewCustomFont tvServiceName;
        RecyclerView recycleOperatorsList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recycleOperatorsList = itemView.findViewById(R.id.recycleOperatorsList);
            tvServiceName = itemView.findViewById(R.id.tvServiceName);
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
