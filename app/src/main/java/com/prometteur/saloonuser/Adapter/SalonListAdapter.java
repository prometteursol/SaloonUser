package com.prometteur.saloonuser.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.location.Address;
import android.location.Geocoder;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.prometteur.saloonuser.Activities.ActivityHomepage;
import com.prometteur.saloonuser.Activities.ActivitySalonProfile;
import com.prometteur.saloonuser.Activities.ActivitySalonServices;
import com.prometteur.saloonuser.Activities.ActivityUpdateProfile;
import com.prometteur.saloonuser.Model.HomeBean;
import com.prometteur.saloonuser.R;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.prometteur.saloonuser.Utils.Preferences;
import com.prometteur.saloonuser.databinding.DialogClearCartBinding;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.prometteur.saloonuser.Activities.ActivityHomepage.dateTimeOneTime;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.strAppDate;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.strDate;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.strTime;
import static com.prometteur.saloonuser.Constants.ConstantMethods.getResizedDrawable;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERPROFILE;
import static com.prometteur.saloonuser.Utils.Utils.getDateShowDayDDMMMHHMM;

public class SalonListAdapter extends RecyclerView.Adapter<SalonListAdapter.ViewHolder> {
    private static final String TAG = "Predicted Address";

    Context nContext;
    List<HomeBean.Result> homeResultList;
    Activity nActivity;
    boolean isMapFragment;
    /*private final SalonListAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }*/


    public SalonListAdapter(Activity nActivity, boolean isMapfargment, List<HomeBean.Result> homeResultList){/*,OnItemClickListener listener) {*/
        this.nContext = nActivity;
        this.nActivity = nActivity;
        this.isMapFragment = isMapfargment;
        this.homeResultList = homeResultList;
        //this.listener = listener;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       /* if (isMapFragment) {
            View view = LayoutInflater.from(nActivity).inflate(R.layout.recycle_mapsaloon_list,
                    parent, false);
            return new SalonListAdapter.ViewHolder(view);
        } else {*/
            View view = LayoutInflater.from(nActivity).inflate(R.layout.recycle_listsaloon_view,
                    parent, false);
            return new SalonListAdapter.ViewHolder(view);
        //}
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.tvSalonName.setText(homeResultList.get(position).getBranName());
        holder.tvSalonLocation.setText(homeResultList.get(position).getBranAddr());
        holder.tvSalonDistance.setText(homeResultList.get(position).getDistance()+" KM");
        if(homeResultList.get(position).getSalonRating()!=null) {
            if(!homeResultList.get(position).getSalonRating().toString().isEmpty()) {
                holder.rbSalonRating.setRating(Float.parseFloat( homeResultList.get(position).getSalonRating().toString()));
            }else
            {
                holder.rbSalonRating.setRating(0);
            }
        }else
        {
            holder.rbSalonRating.setRating(0);
        }
        if(homeResultList.get(position).getDiscount()!=null) {
        if(!homeResultList.get(position).getDiscount().isEmpty()) {
            if (holder.tvDiscount != null) {
                holder.tvDiscount.setText(homeResultList.get(position).getDiscount() + "% *");
            }
        }else {
            if(holder.linBadge!=null) {
                holder.linBadge.setVisibility(View.GONE);
            }
        }
        }else
        {
            if(holder.linBadge!=null) {
                holder.linBadge.setVisibility(View.GONE);
            }
            homeResultList.get(position).setDiscount("");
        }
        if(homeResultList.get(position).getBranImg()!=null) {
            Glide.with(nActivity).load(homeResultList.get(position).getBranImg()).error(R.drawable.img_profile).into(holder.rivSaloonImage);
        }
        getResizedDrawable(nActivity, R.drawable.ic_location_icon, holder.tvSalonLocation, null, null, R.dimen._11sdp);
        getResizedDrawable(nActivity, R.drawable.ic_location_distance_icon, holder.tvSalonDistance, null, null, R.dimen._11sdp);
        getResizedDrawable(nActivity, android.R.drawable.ic_dialog_alert, holder.tvSalonOffline, null, null, R.dimen._11sdp);
        if(homeResultList.get(position).getBranOpenStatus().equalsIgnoreCase("0") || homeResultList.get(position).getBranClosed().equalsIgnoreCase("1"))
        {
            //holder.ivSaloonOpenClose.setImageResource(R.drawable.bg_red_circle_white_stroke);
            holder.rivSaloonImage.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(nActivity, R.color.darkGray)));
            holder.tvSalonName.setTextColor(nActivity.getResources().getColor(R.color.darkGray));
            holder.tvSalonLocation.setTextColor(nActivity.getResources().getColor(R.color.darkGray));
            holder.tvSalonDistance.setTextColor(nActivity.getResources().getColor(R.color.darkGray));
            holder.btnBook.setBackground(nActivity.getResources().getDrawable(R.drawable.graybtn));
            holder.linBadge.setBackground(nActivity.getResources().getDrawable(R.drawable.ic_badge_gray));
            holder.rbSalonRating.setProgressTintList(ColorStateList.valueOf(ContextCompat.getColor(nActivity, R.color.darkGray)));
            getResizedDrawable(nActivity, R.drawable.ic_location_gray, holder.tvSalonLocation, null, null, R.dimen._11sdp);
            getResizedDrawable(nActivity, R.drawable.ic_distance_gray, holder.tvSalonDistance, null, null, R.dimen._11sdp);
            //holder.tvgreybg.setVisibility(View.VISIBLE);
            holder.tvSalonOffline.setVisibility(View.VISIBLE);
            holder.rivImageLayer.setVisibility(View.VISIBLE);
            if(homeResultList.get(position).getBranClosed().equalsIgnoreCase("1")){
                holder.tvSalonOffline.setVisibility(View.GONE);
            }else {
                holder.tvSalonOffline.setText("Salon is offline till " + getDateShowDayDDMMMHHMM(homeResultList.get(position).getBranOpenedOn()) + " but can accept appointment for other time slots.");
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.tvDivider.getLayoutParams();
                params.topMargin = 5;
                holder.tvDivider.setLayoutParams(params);
            }
        }
        final long[] mLastClickTime = {0};
        final long[] mLastClickTimeSalonItem = {0};
            holder.btnBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime[0] < 1000) {
                        return;
                    }
                    mLastClickTime[0] = SystemClock.elapsedRealtime();
                    Preferences.setPreferenceValue(nActivity,"workingHour",homeResultList.get(position).getBranWorkingHrs());
                    Preferences.setPreferenceValue(nActivity,"holidayFrom",homeResultList.get(position).getBranHolidayFrom());
                    Preferences.setPreferenceValue(nActivity,"holidayTo",homeResultList.get(position).getBranHolidayTo());
                    Preferences.setPreferenceValue(nActivity,"offDay",homeResultList.get(position).getBranOffDay());
                    Preferences.setPreferenceValue(nActivity,"offlineEndTime",homeResultList.get(position).getBranOpenedOn());
                    Preferences.setPreferenceValue(nActivity, "mainCat",homeResultList.get(position).getBranCatMain());
                    nActivity.startActivity(new Intent(nActivity, ActivitySalonServices.class).putExtra("branchId", homeResultList.get(position).getBranId()).putExtra("mainCat",homeResultList.get(position).getBranCatMain()));
                }
            });
          //  holder.tvgreybg.setVisibility(View.GONE);

holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if (SystemClock.elapsedRealtime() - mLastClickTimeSalonItem[0] < 1000) {
            return;
        }
        mLastClickTimeSalonItem[0] = SystemClock.elapsedRealtime();
        nActivity.startActivity(new Intent(nActivity, ActivitySalonProfile.class).putExtra("branchId", homeResultList.get(position).getBranId()).putExtra("mainCat",homeResultList.get(position).getBranCatMain()));
    }
});


    }

    @Override
    public int getItemCount() {
        return homeResultList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView rivSaloonImage,rivImageLayer;
        ImageView ivSaloonOpenClose;
        TextView tvSalonName;
        RatingBar rbSalonRating;
        TextView tvSalonLocation;
        TextView tvSalonDistance;
        TextView tvDiscount;
        TextView tvSalonOffline;
        TextView tvDivider;
       // TextView tvgreybg;
        Button btnBook;
        LinearLayout linBadge;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rivSaloonImage=itemView.findViewById(R.id.rivSaloonImage);
            rivImageLayer=itemView.findViewById(R.id.rivImageLayer);
             ivSaloonOpenClose=itemView.findViewById(R.id.ivSaloonOpenClose);
             tvSalonName=itemView.findViewById(R.id.tvSalonName);
             rbSalonRating=itemView.findViewById(R.id.rbSalonRating);
             tvSalonLocation=itemView.findViewById(R.id.tvSalonLocation);
             tvSalonDistance=itemView.findViewById(R.id.tvSalonDistance);
            tvDiscount=itemView.findViewById(R.id.tvDiscount);
            btnBook=itemView.findViewById(R.id.btnBook);
            linBadge=itemView.findViewById(R.id.linBadge);
            tvSalonOffline=itemView.findViewById(R.id.tvSalonOffline);
            tvDivider=itemView.findViewById(R.id.tvDivider);
           // tvgreybg=itemView.findViewById(R.id.tvgreybg);
            tvSalonName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            tvSalonName.setMarqueeRepeatLimit(-1);
            tvSalonName.setSingleLine(true);
            tvSalonName.setSelected(true);
        }

        /*public void bind(final int position, final SalonListAdapter.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position);
                }
            });
        }*/

    }




}
