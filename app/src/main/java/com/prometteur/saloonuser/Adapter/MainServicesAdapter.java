package com.prometteur.saloonuser.Adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.prometteur.saloonuser.Constants.ConstantVariables;
import com.prometteur.saloonuser.R;

import java.util.ArrayList;
import java.util.List;

import static com.prometteur.saloonuser.Activities.ActivityHomepage.selectedCats;
import static com.prometteur.saloonuser.Constants.ConstantMethods.getResizedDrawable;
import static com.prometteur.saloonuser.Constants.ConstantVariables.colorList;

public class MainServicesAdapter extends RecyclerView.Adapter<MainServicesAdapter.ViewHolder> {

    private final OnItemClickListener listener;
    Activity nActivity;
    List<String> mainServices;
int selectedPosition=-1;
    boolean selectedAllCat=false;
    public MainServicesAdapter(Activity nActivity, OnItemClickListener listener, List<String> mainServices,boolean selectedAllCat) {
        this.nActivity = nActivity;
        this.listener = listener;
        this.mainServices = mainServices;
        this.selectedAllCat = selectedAllCat;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(nActivity).inflate(R.layout.recycle_salon_categories,
                parent, false);
        return new MainServicesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.cvCard.setCardBackgroundColor(nActivity.getResources().getColor(colorList[position]));
        holder.btnCatogorieItem.setBackgroundColor(nActivity.getResources().getColor(R.color.transparent));
        holder.btnCatogorieItem.setText(mainServices.get(position));
        holder.btnCatogorieItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*listener.onItemClick(mainServices.get(position));
                selectedPosition = position;
                notifyDataSetChanged();*/
                if(position==0){
                    selectedCats=new ArrayList<>();
                    selectedCats.add(""+position);
                    notifyDataSetChanged();
                }else {
                    if(selectedCats.contains(""+0))
                    {
                        selectedCats.remove(""+0);
                        //holder.btnCatogorieItem.setCompoundDrawables(null, null, null, null);
                        //notifyDataSetChanged();
                    }
                    if (selectedCats.contains(""+position)) {
                        selectedCats.remove(""+position);
                        holder.btnCatogorieItem.setCompoundDrawables(null, null, null, null);
                    } else {
                        selectedCats.add(""+position);
                        Drawable img = nActivity.getResources().getDrawable(R.drawable.ic_circle_tick_white);
                        img.setBounds(0, 0, img.getIntrinsicWidth() * holder.btnCatogorieItem.getMeasuredHeight() / img.getIntrinsicHeight(), holder.btnCatogorieItem.getMeasuredHeight());
                        holder.btnCatogorieItem.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                        getResizedDrawable(nActivity, R.drawable.ic_circle_tick_white, null, holder.btnCatogorieItem, null, R.dimen._11sdp);
                    }
                    notifyDataSetChanged();
                }
                listener.onItemClick(mainServices.get(position));
            }
        });

if(selectedAllCat)
{
    selectedCats=new ArrayList<>();
    selectedCats.add("0");
    selectedAllCat=false;
}

        if(selectedCats.contains(""+position))
        {
            Drawable img = nActivity.getResources().getDrawable(R.drawable.ic_circle_tick_white);
            img.setBounds(0, 0, img.getIntrinsicWidth() * holder.btnCatogorieItem.getMeasuredHeight() / img.getIntrinsicHeight(), holder.btnCatogorieItem.getMeasuredHeight());
            holder.btnCatogorieItem.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
            getResizedDrawable(nActivity,R.drawable.ic_circle_tick_white,null,holder.btnCatogorieItem,null,R.dimen._11sdp);
        } else {
            holder.btnCatogorieItem.setCompoundDrawables(null, null, null, null);
        }

    }

    @Override
    public int getItemCount() {
        return mainServices.size();
    }

    public interface OnItemClickListener {
        void onItemClick(String cats);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linlayItem;
        Button btnCatogorieItem;
CardView cvCard;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linlayItem = itemView.findViewById(R.id.linlayItem);
            btnCatogorieItem = itemView.findViewById(R.id.btnCatogorieItem);
            cvCard=itemView.findViewById(R.id.cvCard);
        }

    }
}
