package com.prometteur.saloonuser.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prometteur.saloonuser.Activities.ActivityAppointmentDetails;
import com.prometteur.saloonuser.Model.NotificationBean;
import com.prometteur.saloonuser.Model.NotificationsPojo;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.TextViewCustomFont;
import com.prometteur.saloonuser.databinding.DialogSelectPaymentTypeBinding;


import java.util.ArrayList;
import java.util.List;

import static com.prometteur.saloonuser.Constants.ConstantVariables.fromPayment;
import static com.prometteur.saloonuser.Utils.Utils.getReviewDate;

public class NotificationsListAdapter extends RecyclerView.Adapter<NotificationsListAdapter.ViewHolder> {

    Activity nActivity;

    List<NotificationBean.Result> notificationsArrayList;
    Dialog dialogSelectPayment;
    DialogSelectPaymentTypeBinding paymentTypeBinding;


    public NotificationsListAdapter(Activity nActivity, List<NotificationBean.Result> notificationsArrayList) {
        this.nActivity = nActivity;
        this.notificationsArrayList=notificationsArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(nActivity).inflate(R.layout.recycle_notification_list_item, parent, false);
        return new NotificationsListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final NotificationBean.Result notificationsPojo=notificationsArrayList.get(position);
        if (notificationsPojo.getNotiTitle().equals("Your service is completed. Please select")){
            holder.tvPaymentNotification.setVisibility(View.VISIBLE);
            holder.tvPaymentNotification.setText(notificationsPojo.getAptAmount());
            holder.tvNotificationTitle.setText(notificationsPojo.getNotiTitle());
            holder.tvNotificationDesc.setText(notificationsPojo.getNotiMessage());
            holder.tvTimeSpan.setText(getReviewDate(nActivity,notificationsPojo.getNotiCreateDate()));
            holder.ivNotifyTypeimg.setImageDrawable(nActivity.getResources().getDrawable(R.drawable.ic_payment_transparent_bg));
            holder.ivNotifyTypeimg.setPadding(20,20,20,20);
        }
        else {
            holder.tvPaymentNotification.setVisibility(View.GONE);
            holder.tvPaymentNotification.setText(notificationsPojo.getAptAmount());
            holder.tvNotificationTitle.setText(notificationsPojo.getNotiTitle());
            holder.tvNotificationDesc.setText(notificationsPojo.getNotiMessage());
            holder.tvTimeSpan.setText(getReviewDate(nActivity,notificationsPojo.getNotiCreateDate()));
        }

        if(notificationsPojo.getNotiReadStatus().equalsIgnoreCase("0"))
        {
            holder.itemView.setBackgroundColor(nActivity.getResources().getColor(R.color.grayNoti));
        }else
        {
            holder.itemView.setBackgroundColor(nActivity.getResources().getColor(R.color.white));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(nActivity,ActivityAppointmentDetails.class);
                //intent.putExtra("From Payment",fromPayment);
                intent.putExtra("appId",notificationsPojo.getNotiAppointmentId());
                intent.putExtra("notiId",notificationsPojo.getNotiId());
                nActivity.startActivity(intent);
                //showSelectPaymentDialog(nActivity);
            }
        });

    }



    @Override
    public int getItemCount() {
        return notificationsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivNotifyTypeimg;
        TextViewCustomFont tvNotificationTitle;
        TextViewCustomFont tvNotificationDesc;
        TextViewCustomFont tvPaymentNotification;
        TextViewCustomFont tvTimeSpan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivNotifyTypeimg=itemView.findViewById(R.id.ivNotifyTypeimg);
            tvNotificationTitle=itemView.findViewById(R.id.tvNotificationTitle);
            tvNotificationDesc=itemView.findViewById(R.id.tvNotificationDesc);
            tvPaymentNotification=itemView.findViewById(R.id.tvPaymentNotification);
            tvTimeSpan=itemView.findViewById(R.id.tvTimeSpan);
        }
    }
    private void showSelectPaymentDialog(final Activity inActivity) {

        dialogSelectPayment=new Dialog(inActivity,R.style.CustomAlertDialog);
        paymentTypeBinding = DialogSelectPaymentTypeBinding.inflate(LayoutInflater.from(inActivity));
        dialogSelectPayment.setContentView(paymentTypeBinding.getRoot());
        Window window = dialogSelectPayment.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);
        dialogSelectPayment.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        paymentTypeBinding.btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Intent intent=new Intent(inActivity,ActivityAppointmentDetails.class);
                intent.putExtra("From Payment",fromPayment);
                intent.putExtra("aptId",appointmentData.getAptId())
                inActivity.startActivity(intent);*/
                dialogSelectPayment.dismiss();
            }
        });
        dialogSelectPayment.show();
    }
}
