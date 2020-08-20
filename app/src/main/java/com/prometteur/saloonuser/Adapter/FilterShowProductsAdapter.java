package com.prometteur.saloonuser.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prometteur.saloonuser.Model.CategoryBrandBean;
import com.prometteur.saloonuser.Model.HomeBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.CheckBoxCustomFont;
import com.prometteur.saloonuser.Utils.TextViewCustomFont;

import java.util.ArrayList;
import java.util.List;

import static com.prometteur.saloonuser.Constants.ConstantMethods.getResizedDrawable;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.brandsArr;

public class FilterShowProductsAdapter extends RecyclerView.Adapter<FilterShowProductsAdapter.ViewHolder> {
    private static final String TAG = "Predicted_Address";

    Activity nActivity;
    Context nContext;
    int itemIndex;
    List<CategoryBrandBean.Result> brands;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public FilterShowProductsAdapter(Activity nActivity, List<CategoryBrandBean.Result> brands, OnItemClickListener listener) {
        this.nActivity = nActivity;
        this.listener = listener;
        this.brands = brands;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        nContext = nActivity;
        View view = LayoutInflater.from(nContext).inflate(R.layout.recycle_bs_show_products,
                parent, false);
        return new FilterShowProductsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.bind(position, listener);
        holder.btnProductItem.setText(brands.get(position).getBrndName());

        holder.btnProductItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    if(!brandsArr.contains(brands.get(position).getBrndId())){
                    brandsArr.add(brands.get(position).getBrndId());
                }
                else {
                    brandsArr.remove(brands.get(position).getBrndId());
                }
                notifyDataSetChanged();
            }
        });

        if (brandsArr.contains(brands.get(position).getBrndId())) {
//            holder.btnProductItem.setBackground(nActivity.getResources().getDrawable(R.drawable.btn_bg_blue_small));
            setDrawableTextView(R.drawable.ic_check_black_24dp, holder.btnProductItem);
            holder.btnProductItem.setTextColor(nActivity.getResources().getColor(R.color.skyBluelilDark));

        } else {
//            holder.btnProductItem.setBackground(nActivity.getResources().getDrawable(R.drawable.bg_white_curved_corners));
            removeDrawableCheckbox(holder.btnProductItem);
            holder.btnProductItem.setTextColor(nActivity.getResources().getColor(R.color.grey));
        }

    }

    @Override
    public int getItemCount() {
        if(brands!=null) {
            return brands.size();
        }else {
            return 0;
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        CheckBoxCustomFont btnProductItem;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnProductItem = itemView.findViewById(R.id.btnProductItem);
        }

        public void bind(final int position, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position);
                }
            });
        }
    }


    //remove drawable
    private void removeDrawableCheckbox(CheckBox checkBox) {
        /*Drawable img = nContext.getResources().getDrawable(id);
        img.setBounds(0, 0, 70, 70);*/
        checkBox.setCompoundDrawables(null, null, null, null);
    }
    private void setDrawableTextView(int ic_keyboard_arrow_up_black_24dp, CheckBox tvSalonDetails) {
        Drawable img = nContext.getResources().getDrawable(ic_keyboard_arrow_up_black_24dp);
        img.setBounds(0, 0, 60, 50);
        tvSalonDetails.setCompoundDrawables(null, null, img, null);
    }
}
