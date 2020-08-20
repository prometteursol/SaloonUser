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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.prometteur.saloonuser.Activities.RejectionActivity;
import com.prometteur.saloonuser.Model.AcceptAppBean;
import com.prometteur.saloonuser.Model.AppointDetailBean;
import com.prometteur.saloonuser.Model.AppointmentBean;
import com.prometteur.saloonuser.Model.CheckPenaltyBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.TextViewCustomFont;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.prometteur.saloonuser.databinding.DialogAppointmentCancellationBinding;
import com.prometteur.saloonuser.retrofit.ApiInterface;
import com.prometteur.saloonuser.retrofit.RetrofitInstance;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.prometteur.saloonuser.Constants.ConstantMethods.getResizedDrawable;
import static com.prometteur.saloonuser.Constants.ConstantMethods.makePhoneCall;
import static com.prometteur.saloonuser.Utils.Utils.getDateShowDDMMMYYYY;
import static com.prometteur.saloonuser.Utils.Utils.getDateShowDayDDMMMYYYY;
import static com.prometteur.saloonuser.Utils.Utils.getTimeShow24to12HR;
import static com.prometteur.saloonuser.Utils.Utils.isNetworkAvailable;
import static com.prometteur.saloonuser.Utils.Utils.logout;
import static com.prometteur.saloonuser.Utils.Utils.showFailToast;
import static com.prometteur.saloonuser.Utils.Utils.showNoInternetDialog;
import static com.prometteur.saloonuser.Utils.Utils.showProgress;
import static com.prometteur.saloonuser.Utils.Utils.showSuccessToast;

public class FixedAppointmentsAdapter extends RecyclerView.Adapter<FixedAppointmentsAdapter.ViewHolder> {

    Activity nActivity;
    private final OnItemClickListener listener;
    DialogAppointmentCancellationBinding cancellationBinding;
    Dialog dialogCancelAppointment;
    List<AppointmentBean.OngoingAppointment> result;
String appId="0";
    public FixedAppointmentsAdapter(Activity nActivity, List<AppointmentBean.OngoingAppointment> result, OnItemClickListener listener) {
        this.nActivity = nActivity;
        this.listener = listener;
        this.result = result;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(nActivity).inflate(R.layout.recycle_fixed_appointments,
                parent, false);
        return new FixedAppointmentsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.bind(position, listener);
        getResizedDrawable(nActivity,R.drawable.ic_location_icon,holder.tvSalonLocation,null,null,R.dimen._11sdp);
        getResizedDrawable(nActivity,R.drawable.ic_location_distance_icon,holder.tvSalonDistance,null,null,R.dimen._11sdp);
        final AppointmentBean.OngoingAppointment appointmentData=result.get(position);
        appId=appointmentData.getAptId();
        holder.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.btnCall.getText().toString().equalsIgnoreCase("Accept"))
                {
                    if (isNetworkAvailable(nActivity)) {
                        getAcceptAppoint();
                    } else {
                        showNoInternetDialog(nActivity);
                    }
                }else {
                    makePhoneCall(nActivity, 1, "");
                }
            }
        });
        holder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(appointmentData.getAptStatus().equalsIgnoreCase("1")) {
                    if (isNetworkAvailable(nActivity)) {
                        getCheckPenalty();
                    } else {
                        showNoInternetDialog(nActivity);
                    }
                }else
                {
                    nActivity.startActivity(new Intent(nActivity, RejectionActivity.class).putExtra("aptId",appId));
                    notifyDataSetChanged();
                }
            }
        });
        holder.tvSalonName.setText(appointmentData.getBranName());
        holder.tvAppointmentTime.setText(getTimeShow24to12HR(appointmentData.getAptTime()));
        holder.tvAppointmentDate.setText(getDateShowDayDDMMMYYYY(appointmentData.getAptDate()));
        holder.tvSalonLocation.setText(appointmentData.getBranAddr());
        holder.tvSalonDistance.setText(appointmentData.getDistance()+" KM");
        holder.tvAppliedServices.setText(appointmentData.getServices());
        if(appointmentData.getSalonRating()!=null) {
            holder.rbSalonRating.setRating(Float.parseFloat(appointmentData.getSalonRating()));
        }

        if(appointmentData.getBranImg()!=null) {
            Glide.with(nActivity).load(appointmentData.getBranImg()).into(holder.rivSaloonImage);
        }

        if(appointmentData.getAptStatus().equalsIgnoreCase("0"))
        {
            holder.tvAppointmentStatus.setText("Pending");
            holder.linOtpSec.setVisibility(View.GONE);
            holder.view.setVisibility(View.GONE);
        }else if(appointmentData.getAptStatus().equalsIgnoreCase("1"))
        {
            holder.tvAppointmentStatus.setText("Accepted");
            holder.tvStartotp.setText("Start OTP");
            holder.tvStartOtpCount.setText(appointmentData.getAptStartOtp());
        }else if(appointmentData.getAptStatus().equalsIgnoreCase("5"))
        {
            holder.tvAppointmentStatus.setText("Ongoing");
            holder.tvStartotp.setText("End OTP");
            holder.tvStartOtpCount.setText(""+appointmentData.getAptEndOtp());
            holder.linBtnSec.setVisibility(View.GONE);
        }else if(appointmentData.getAptStatus().equalsIgnoreCase("4"))
        {
            holder.tvAppointmentStatus.setText("Finished");
            if(appointmentData.getAptPaymentType().equalsIgnoreCase("1")) {
                holder.btnPayNow.setVisibility(View.VISIBLE);
            }
            holder.btnCall.setVisibility(View.GONE);
            holder.btnCancel.setVisibility(View.GONE);
        }else if(appointmentData.getAptStatus().equalsIgnoreCase("3"))
        {
            holder.tvAppointmentStatus.setText("Rescheduled");
            holder.btnCall.setText("Accept");
            holder.btnCall.setVisibility(View.VISIBLE);
            holder.btnCancel.setVisibility(View.VISIBLE);
            holder.btnPayNow.setVisibility(View.GONE);
            holder.linOtpSec.setVisibility(View.GONE);
            holder.view.setVisibility(View.GONE);
        }
        //holder.tvAppliedServices.setText(appointmentData.gets());
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextViewCustomFont tvAppointmentDate;
        TextViewCustomFont tvAppointmentTime;
        TextViewCustomFont tvAppointmentStatus;
        TextViewCustomFont tvStartotp;
        RoundedImageView rivSaloonImage;
        TextViewCustomFont tvSalonName;
        RatingBar rbSalonRating;
        TextViewCustomFont tvSalonLocation;
        TextViewCustomFont tvSalonDistance;
        TextViewCustomFont tvStartOtpCount;
        TextViewCustomFont tvAppliedServices;
        Button btnCall;
        Button btnCancel;
        Button btnPayNow;
        LinearLayout linOtpSec,linBtnSec;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAppointmentDate = itemView.findViewById(R.id.tvAppointmentDate);
            tvAppointmentTime = itemView.findViewById(R.id.tvAppointmentTime);
            tvAppointmentStatus = itemView.findViewById(R.id.tvAppointmentStatus);
            rivSaloonImage = itemView.findViewById(R.id.rivSaloonImage);
            tvSalonName = itemView.findViewById(R.id.tvSalonName);
            rbSalonRating = itemView.findViewById(R.id.rbSalonRating);
            tvSalonLocation = itemView.findViewById(R.id.tvSalonLocation);
            tvSalonDistance = itemView.findViewById(R.id.tvSalonDistance);
            tvStartOtpCount = itemView.findViewById(R.id.tvStartOtpCount);
            tvAppliedServices = itemView.findViewById(R.id.tvAppliedServices);
            btnCall = itemView.findViewById(R.id.btnCall);
            btnCancel = itemView.findViewById(R.id.btnCancel);
            linOtpSec = itemView.findViewById(R.id.linOtpSec);
            view = itemView.findViewById(R.id.view);
            tvStartotp = itemView.findViewById(R.id.tvStartotp);
            linBtnSec = itemView.findViewById(R.id.linBtnSec);
            btnPayNow = itemView.findViewById(R.id.btnPayNow);

        }
        public void bind(final int position, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(position);
                }
            });
        }

    }
    public void showCancelRequestDialog(Activity inActivity) {
        dialogCancelAppointment=new Dialog(inActivity,R.style.CustomAlertDialog);
        cancellationBinding = DialogAppointmentCancellationBinding.inflate(LayoutInflater.from(inActivity));
        dialogCancelAppointment.setContentView(cancellationBinding.getRoot());
        Window window = dialogCancelAppointment.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);
        dialogCancelAppointment.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        cancellationBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nActivity.startActivity(new Intent(nActivity, RejectionActivity.class));
                notifyDataSetChanged();
                dialogCancelAppointment.dismiss();
            }
        });
        dialogCancelAppointment.show();

    }


    AcceptAppBean appintAcceptBean;
    private void getAcceptAppoint() {

        final ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        final Dialog progressDialog = showProgress(nActivity, 0);
        progressDialog.show();
        service.getAcceptAppointment(""+appId,"1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AcceptAppBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(AcceptAppBean loginBeanObj) {
                        progressDialog.dismiss();
                        appintAcceptBean = loginBeanObj;
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        showFailToast(nActivity, nActivity.getResources().getString(R.string.went_wrong));
                    }

                    @Override
                    public void onComplete() {
                        // Updates UI with data
                        progressDialog.dismiss();

                        if (appintAcceptBean.getStatus() == 1) {
                            showSuccessToast(nActivity,"Appointment accepted successfully");
                        } else if (appintAcceptBean.getStatus() == 3) {
                            showFailToast(nActivity, "" + appintAcceptBean.getMsg());
                            logout(nActivity);
                        }
                        //setEmptyMsg(mDataList, comboPageBinding.recycleCombolist, ivNoData);
                    }
                });
    }



    CheckPenaltyBean checkPenaltyBean;
    String penaltyAmt="0";
    private void getCheckPenalty() {

        final ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        final Dialog progressDialog = showProgress(nActivity, 0);
        progressDialog.show();
        service.getCheckPenalty(appId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CheckPenaltyBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CheckPenaltyBean loginBeanObj) {
                        progressDialog.dismiss();
                        checkPenaltyBean = loginBeanObj;
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        showFailToast(nActivity, nActivity.getResources().getString(R.string.went_wrong));
                    }

                    @Override
                    public void onComplete() {
                        // Updates UI with data
                        progressDialog.dismiss();

                        if (checkPenaltyBean.getStatus() == 1) {
                            penaltyAmt=checkPenaltyBean.getResult().get(0);
                            if(!penaltyAmt.equalsIgnoreCase("0")) {
                                showCancelRequestDialog(nActivity);
                            }else
                            {
                                nActivity.startActivity(new Intent(nActivity, RejectionActivity.class).putExtra("aptId",appId));
                                notifyDataSetChanged();
                                dialogCancelAppointment.dismiss();
                            }
                        } else if (checkPenaltyBean.getStatus() == 3) {
                            showFailToast(nActivity, "" + checkPenaltyBean.getMsg());
                            logout(nActivity);
                        }
                        //setEmptyMsg(mDataList, comboPageBinding.recycleCombolist, ivNoData);
                    }
                });
    }
}
