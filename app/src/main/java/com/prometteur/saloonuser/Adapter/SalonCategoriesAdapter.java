package com.prometteur.saloonuser.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.prometteur.saloonuser.Fragments.FragmentListSalonView;
import com.prometteur.saloonuser.R;

import java.util.ArrayList;

import static com.prometteur.saloonuser.Activities.ActivityHomepage.selectedCats;
import static com.prometteur.saloonuser.Constants.ConstantMethods.getResizedDrawable;

public class SalonCategoriesAdapter extends RecyclerView.Adapter<SalonCategoriesAdapter.ViewHolder> {
    private static final String TAG = "Predicted_Address";

    Context nContext;
    ArrayList<String> CategorieName;
    ///Activity nActivity;
 int[] colorList;
    boolean selectedAllCat=false;
    FragmentListSalonView.OnItemClickListener listener;

    public SalonCategoriesAdapter(Context nContext, ArrayList<String> CategorieName, int[] colorList, FragmentListSalonView.OnItemClickListener listener,boolean selectedAllCat) {
        this.nContext = nContext;
        this.CategorieName = CategorieName;
        this.colorList=colorList;
        this.listener=listener;
        this.selectedAllCat = selectedAllCat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(nContext).inflate(R.layout.recycle_salon_categories,
                parent, false);
        return new SalonCategoriesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        String categorie=CategorieName.get(position);
        holder.btnCatogorieItem.setText(categorie);
        holder.cvCard.setCardBackgroundColor(nContext.getResources().getColor(colorList[position]));
        holder.btnCatogorieItem.setBackgroundColor(nContext.getResources().getColor(R.color.transparent));
        holder.btnCatogorieItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                        Drawable img = nContext.getResources().getDrawable(R.drawable.ic_circle_tick_white);
                        img.setBounds(0, 0, img.getIntrinsicWidth() * holder.btnCatogorieItem.getMeasuredHeight() / img.getIntrinsicHeight(), holder.btnCatogorieItem.getMeasuredHeight());
                        holder.btnCatogorieItem.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                        getResizedDrawable(nContext, R.drawable.ic_circle_tick_white, null, holder.btnCatogorieItem, null, R.dimen._11sdp);
                    }
                    notifyDataSetChanged();
                }
                listener.onItemClick(position);
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
            Drawable img = nContext.getResources().getDrawable(R.drawable.ic_circle_tick_white);
            img.setBounds(0, 0, img.getIntrinsicWidth() * holder.btnCatogorieItem.getMeasuredHeight() / img.getIntrinsicHeight(), holder.btnCatogorieItem.getMeasuredHeight());
            holder.btnCatogorieItem.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
            getResizedDrawable(nContext,R.drawable.ic_circle_tick_white,null,holder.btnCatogorieItem,null,R.dimen._11sdp);
        }else
        {
            holder.btnCatogorieItem.setCompoundDrawables(null, null, null, null);
        }

    }

    @Override
    public int getItemCount() {
        return CategorieName.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        Button btnCatogorieItem;
CardView cvCard;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnCatogorieItem=itemView.findViewById(R.id.btnCatogorieItem);
            cvCard=itemView.findViewById(R.id.cvCard);
        }
    }

}