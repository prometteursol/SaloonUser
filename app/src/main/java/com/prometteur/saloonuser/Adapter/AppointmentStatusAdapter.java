package com.prometteur.saloonuser.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.prometteur.saloonuser.Model.AppointmentBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.TextViewCustomFont;

import java.util.List;

import static com.prometteur.saloonuser.Constants.ConstantMethods.getResizedDrawable;
import static com.prometteur.saloonuser.Constants.ConstantMethods.makePhoneCall;
import static com.prometteur.saloonuser.Utils.Utils.getDateShowDDMMMYYYY;
import static com.prometteur.saloonuser.Utils.Utils.getDateShowDayDDMMMYYYY;
import static com.prometteur.saloonuser.Utils.Utils.getTimeShow24to12HR;

public class AppointmentStatusAdapter extends RecyclerView.Adapter<AppointmentStatusAdapter.ViewHolder> {

    Activity nActivity;
    private final OnItemClickListener listener;
    boolean fromAppointmentCompleted;
    List<AppointmentBean.Result> result;
    public AppointmentStatusAdapter(Activity nActivity, boolean b, List<AppointmentBean.Result> result, OnItemClickListener onItemClickListener) {
        this.nActivity = nActivity;
        this.listener=onItemClickListener;
        this.fromAppointmentCompleted=b;
        this.result=result;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(nActivity).inflate(R.layout.recycle_appointment_status,
                parent, false);
        return new AppointmentStatusAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        getResizedDrawable(nActivity, R.drawable.ic_location_icon, holder.tvSalonLocation, null, null, R.dimen._11sdp);
        getResizedDrawable(nActivity, R.drawable.ic_location_distance_icon, holder.tvSalonDistance, null, null, R.dimen._11sdp);
        AppointmentBean.Result appointmentData=result.get(position);
        if (fromAppointmentCompleted){
            holder.bind(position, listener);
            holder.tvAppointmentStatus.setText("Completed");
            holder.tvPaidAmount.setText("₹ "+appointmentData.getAptAmount());
        }
        else{
            if(appointmentData.getAptStatus().equalsIgnoreCase("2")) {
                holder.tvAppointmentStatus.setText("Declined");
            }else if(appointmentData.getAptStatus().equalsIgnoreCase("7")) {
                holder.tvAppointmentStatus.setText("Cancelled");
            }else if(appointmentData.getAptStatus().equalsIgnoreCase("8")) {
                holder.tvAppointmentStatus.setText("Unattended");
            }else if(appointmentData.getAptStatus().equalsIgnoreCase("9")) {
                holder.tvAppointmentStatus.setText("No Show");
            }
           // holder.tvAppointmentStatus.setText(R.string.cancelled);
            holder.tvPaidAmount.setText("NA");
            holder.tvAppointmentStatus.setTextColor(nActivity.getResources().getColor(R.color.red));
            holder.bind(position, listener);
        }



        holder.tvSalonName.setText(appointmentData.getBranName());
        holder.tvAppointmentTime.setText(getTimeShow24to12HR(appointmentData.getAptTime()));
        holder.tvAppointmentDate.setText(getDateShowDayDDMMMYYYY(appointmentData.getAptDate()));
        holder.tvSalonLocation.setText(appointmentData.getBranAddr());
        holder.tvSalonDistance.setText(appointmentData.getDistance()+" KM");

        holder.tvAppliedServices.setText(appointmentData.getServices());
        if(appointmentData.getSalonRating()!=null) {
            holder.rbSalonRating.setRating(Float.parseFloat(appointmentData.getSalonRating()));
        }else
        {
            holder.rbSalonRating.setRating(0);
        }
        if(appointmentData.getAptPaymentType().equalsIgnoreCase("1")) {
            holder.tvPaymentType.setText("Online");
        }else
        {
            holder.tvPaymentType.setText("Salon");
        }
        if(appointmentData.getBranImg()!=null) {
            Glide.with(nActivity).load(appointmentData.getBranImg()).placeholder(R.drawable.placeholder_gray_corner).error(R.drawable.placeholder_gray_corner).into(holder.rivSaloonImage);
        }

    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        RatingBar rbSalonRating;
        TextViewCustomFont tvAppointmentDate;
        TextViewCustomFont tvAppointmentTime;
        TextViewCustomFont tvAppointmentStatus;
        TextViewCustomFont tvSalonName;
        TextViewCustomFont tvSalonLocation;
        TextViewCustomFont tvSalonDistance;
        TextViewCustomFont tvStartOtpCount;
        TextViewCustomFont tvStartotp;
        TextViewCustomFont tvAppliedServices;
        TextViewCustomFont tvPaidAmount;
        TextViewCustomFont tvPaid;
        TextViewCustomFont tvPaymentType;
        TextViewCustomFont tvPayment;
        RoundedImageView rivSaloonImage;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rbSalonRating = itemView.findViewById(R.id.rbSalonRating);
            tvAppointmentDate = itemView.findViewById(R.id.tvAppointmentDate);
            tvAppointmentTime = itemView.findViewById(R.id.tvAppointmentTime);
            tvAppointmentStatus = itemView.findViewById(R.id.tvAppointmentStatus);
            tvSalonName = itemView.findViewById(R.id.tvSalonName);
            tvSalonLocation = itemView.findViewById(R.id.tvSalonLocation);
            tvSalonDistance = itemView.findViewById(R.id.tvSalonDistance);
            tvStartOtpCount = itemView.findViewById(R.id.tvStartOtpCount);
            tvStartotp = itemView.findViewById(R.id.tvStartotp);
            tvAppliedServices = itemView.findViewById(R.id.tvAppliedServices);
            tvPaidAmount = itemView.findViewById(R.id.tvPaidAmount);
            tvPaid = itemView.findViewById(R.id.tvPaid);
            tvPaymentType = itemView.findViewById(R.id.tvPaymentType);
            tvPayment = itemView.findViewById(R.id.tvPayment);
            rivSaloonImage = itemView.findViewById(R.id.rivSaloonImage);


        }
        public void bind(final int position, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(position);
                }
            });
        }
    }

}
