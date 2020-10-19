package com.prometteur.saloonuser.Adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
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
import com.prometteur.saloonuser.Utils.Preferences;

import java.util.ArrayList;
import java.util.List;

import static com.prometteur.saloonuser.Activities.ActivityHomepage.brands;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.category;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.discount;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.gender;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.highToLow;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.isFilter;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.lowToHigh;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.rating;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.selectedCats;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.selectedCatsText;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.sortBy;
import static com.prometteur.saloonuser.Activities.ActivitySalonServices.salonServicesBinding;
import static com.prometteur.saloonuser.Constants.ConstantMethods.getResizedDrawable;
import static com.prometteur.saloonuser.Constants.ConstantVariables.colorList;
import static com.prometteur.saloonuser.Fragments.FragmentListSalonView.listSalonBinding;

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
                String catId="";
                switch (mainServices.get(position)) {
                    case "All Services":
                        catId = "0";
                        break;
                    case "Hair":
                        catId = "1";
                        break;
                    case "Skin":
                        catId = "2";
                        break;
                    case "Nails":
                        catId = "3";
                        break;
                    case "Spa":
                        catId = "4";
                        break;
                    case "Makeup":
                        catId = "5";
                        break;
                }

                if(position==0){
                    if(!category.equalsIgnoreCase("") || !brands.equalsIgnoreCase("") || !rating.equalsIgnoreCase("") || !discount.equalsIgnoreCase("") ||!sortBy.equalsIgnoreCase("") || !gender.equalsIgnoreCase("") ||!lowToHigh.equalsIgnoreCase("")||!highToLow.equalsIgnoreCase("")||!gender.equalsIgnoreCase("")||!rating.equalsIgnoreCase(""))
                    {
                        isFilter=true;
                    }else
                    {
                        isFilter=false;
                    }
                    if(isFilter)
                    {
                        salonServicesBinding.ivFilterimg.setImageResource(R.drawable.ic_filter_filled);
                    }else
                    {
                        salonServicesBinding.ivFilterimg.setImageResource(R.drawable.ic_filter_icon);
                    }
                    selectedCatsText=new ArrayList<>();
                    selectedCats=new ArrayList<>();
                    selectedCats.add(""+catId);
                    selectedCatsText.add(holder.btnCatogorieItem.getText().toString().trim());
                    notifyDataSetChanged();
                }else {
                    isFilter=true;
                    salonServicesBinding.ivFilterimg.setImageResource(R.drawable.ic_filter_filled);
                    if(selectedCats.contains(""+0))
                    {
                        selectedCats.remove(""+0);
                        selectedCatsText=new ArrayList<>();
                        //holder.btnCatogorieItem.setCompoundDrawables(null, null, null, null);
                        //notifyDataSetChanged();
                    }
                    if (selectedCatsText.contains(""+mainServices.get(position))) {
                        selectedCats.remove(""+catId);
                        selectedCatsText.remove(mainServices.get(position));
                        holder.btnCatogorieItem.setCompoundDrawables(null, null, null, null);
                    } else {
                        selectedCats.add(""+catId);
                        selectedCatsText.add(mainServices.get(position));
                        Drawable img = nActivity.getResources().getDrawable(R.drawable.ic_circle_tick_white);
                        img.setBounds(0, 0, img.getIntrinsicWidth() * holder.btnCatogorieItem.getMeasuredHeight() / img.getIntrinsicHeight(), holder.btnCatogorieItem.getMeasuredHeight());
                        holder.btnCatogorieItem.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                        getResizedDrawable(nActivity, R.drawable.ic_circle_tick_white, null, holder.btnCatogorieItem, null, R.dimen._11sdp);
                    }
                    notifyDataSetChanged();
                }
                Preferences.setPreferenceValue(nActivity, "categoryFilter", TextUtils.join(",",selectedCats));
                listener.onItemClick(mainServices.get(position));
            }
        });

if(selectedAllCat)
{
    selectedCats=new ArrayList<>();
    selectedCatsText=new ArrayList<>();
    selectedCats.add("0");
    selectedAllCat=false;
}

        if(selectedCatsText.contains(""+mainServices.get(position)))
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
