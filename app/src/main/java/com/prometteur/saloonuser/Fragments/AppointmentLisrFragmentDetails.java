package com.prometteur.saloonuser.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.prometteur.saloonuser.Activities.ActivityAppointmentDetails;
import com.prometteur.saloonuser.Activities.RatingActivity;
import com.prometteur.saloonuser.Activities.RejectionActivity;
import com.prometteur.saloonuser.Model.AcceptAppBean;
import com.prometteur.saloonuser.Model.AppointDetailBean;
import com.prometteur.saloonuser.Model.AppointmentBean;
import com.prometteur.saloonuser.Model.CheckPenaltyBean;
import com.prometteur.saloonuser.Model.OrderIdBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.TextViewCustomFont;
import com.prometteur.saloonuser.databinding.DialogAppointmentCancellationBinding;
import com.prometteur.saloonuser.databinding.DialogPaymentTypeBinding;
import com.prometteur.saloonuser.databinding.DialogPaymentTypeCartBinding;
import com.prometteur.saloonuser.retrofit.ApiInterface;
import com.prometteur.saloonuser.retrofit.RetrofitInstance;
import com.prometteur.saloonuser.retrofit.RetrofitInstanceForPay;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.prometteur.saloonuser.Constants.ConstantMethods.getResizedDrawable;
import static com.prometteur.saloonuser.Constants.ConstantMethods.makePhoneCall;
import static com.prometteur.saloonuser.Fragments.FragmentAppointments.appointmentTabAdapter;
import static com.prometteur.saloonuser.Utils.Utils.getDateShowDayDDMMMYYYY;
import static com.prometteur.saloonuser.Utils.Utils.getTimeShow24to12HR;
import static com.prometteur.saloonuser.Utils.Utils.isNetworkAvailable;
import static com.prometteur.saloonuser.Utils.Utils.logout;
import static com.prometteur.saloonuser.Utils.Utils.showFailToast;
import static com.prometteur.saloonuser.Utils.Utils.showNoInternetDialog;
import static com.prometteur.saloonuser.Utils.Utils.showProgress;
import static com.prometteur.saloonuser.Utils.Utils.showSuccessToast;


public class AppointmentLisrFragmentDetails extends Fragment implements Serializable, PaymentResultWithDataListener {

    Context mContext;
    RecyclerView recyclerView;
    private List<AppointmentBean.OngoingAppointment> mDataList = new ArrayList<>();
    private LinearLayoutManager mLayoutManager;
private int fragResult=101;
private int finishFrag=103;
private int startOtpResult=102;
int strCancelRed=0;

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
    String appId;
    ConstraintLayout conlay;
     AppointmentBean.OngoingAppointment appointmentData;
     AppointmentBean appointmentBean;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.recycle_fixed_appointments, null);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View itemView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(itemView, savedInstanceState);

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
        conlay = itemView.findViewById(R.id.conlay);


        getResizedDrawable(mContext,R.drawable.ic_location_icon,tvSalonLocation,null,null,R.dimen._11sdp);
        getResizedDrawable(mContext,R.drawable.ic_location_distance_icon,tvSalonDistance,null,null,R.dimen._11sdp);
         appointmentData=(AppointmentBean.OngoingAppointment) getArguments().getSerializable("appointDetails");
         appointmentBean=(AppointmentBean) getArguments().getSerializable("appointBeans");
        appId=appointmentData.getAptId();
        total=Double.parseDouble(appointmentData.getAptAmount());
        final long[] mLastClickTimeAppoint = {0};
        conlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTimeAppoint[0] < 2000) {
                    return;
                }
                mLastClickTimeAppoint[0] = SystemClock.elapsedRealtime();
                Intent intent=new Intent(mContext, ActivityAppointmentDetails.class);
                intent.putExtra("appId",appointmentData.getAptId());
                //intent.putExtra(fromAppointmentCompleted,fromAppointmentCompleted);
                startActivity(intent);
            }
        });
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnCall.getText().toString().equalsIgnoreCase("Accept"))
                {
                    if (isNetworkAvailable(mContext)) {
                        getAcceptAppoint();
                    } else {
                        showNoInternetDialog(mContext);
                    }
                }else {
                    makePhoneCall((Activity) mContext, 1, "");
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(appointmentData.getAptStatus().equalsIgnoreCase("1")) {
                    if (isNetworkAvailable(mContext)) {
                        getCheckPenalty();
                    } else {
                        showNoInternetDialog(mContext);
                    }
                }else
                {
                    mContext.startActivity(new Intent(mContext, RejectionActivity.class).putExtra("aptId",appId).putExtra("aptStatus",appointmentData.getAptStatus()));
                    //notifyDataSetChanged();
                }
            }
        });
        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPaymentTypeDialog((Activity) mContext);
            }
        });

        tvSalonName.setText(appointmentData.getBranName());
        tvAppointmentTime.setText(getTimeShow24to12HR(appointmentData.getAptTime()));
        tvAppointmentDate.setText(getDateShowDayDDMMMYYYY(appointmentData.getAptDate()));
        tvSalonLocation.setText(appointmentData.getBranAddr());
        tvSalonDistance.setText(appointmentData.getDistance()+" KM");
        tvAppliedServices.setText(appointmentData.getServices());
        if(appointmentData.getSalonRating()!=null) {
            rbSalonRating.setRating(Float.parseFloat(appointmentData.getSalonRating()));
        }
        if(appointmentData.getBranImg()!=null) {
            Glide.with(mContext).load(appointmentData.getBranImg()).into(rivSaloonImage);
        }

        if(appointmentData.getAptStatus().equalsIgnoreCase("0"))
        {
            tvAppointmentStatus.setText("Pending");
            linOtpSec.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
        }else if(appointmentData.getAptStatus().equalsIgnoreCase("1"))
        {
            tvAppointmentStatus.setText("Accepted");
            tvStartotp.setText("Start OTP");
            tvStartOtpCount.setText(appointmentData.getAptStartOtp());
        }else if(appointmentData.getAptStatus().equalsIgnoreCase("5"))
        {
            tvAppointmentStatus.setText("Ongoing");
            tvStartotp.setText("End OTP");
            tvStartOtpCount.setText(""+appointmentData.getAptEndOtp());
            linBtnSec.setVisibility(View.GONE);
        }else if(appointmentData.getAptStatus().equalsIgnoreCase("6"))
        {
            tvAppointmentStatus.setText("Finished");
            if(appointmentData.getAptPaymentType().equalsIgnoreCase("1")) {
                btnPayNow.setVisibility(View.VISIBLE);
            }else {
                btnPayNow.setVisibility(View.VISIBLE);
            }
            if(appointmentData.getAptPaymentStatus().equalsIgnoreCase("1")) {
                btnPayNow.setVisibility(View.GONE);
            }

            btnCall.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
            linOtpSec.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
        }else if(appointmentData.getAptStatus().equalsIgnoreCase("4"))
        {
            tvAppointmentStatus.setText("Completed");
            if(appointmentData.getAptPaymentType().equalsIgnoreCase("1")) {

            }
            btnCall.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
            linOtpSec.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
            btnPayNow.setVisibility(View.GONE);
        }else if(appointmentData.getAptStatus().equalsIgnoreCase("3"))
        {
            tvAppointmentStatus.setText("Rescheduled");
            btnCall.setText("Accept");
            btnCancel.setText("Reject");
            btnCall.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.VISIBLE);
            btnPayNow.setVisibility(View.GONE);
            linOtpSec.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
        }

    }

   /* @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==fragResult)
        {
            if(strCancelRed==1){
                startActivity(new Intent(mContext, DashboardMaimContext.class).putExtra("objNoti", (Bundle) null));
                ((Activity)mContext).finish();
            }else {
                appointmentTabAdapter.removeCompetetorTabFragments(AppointmentBottomFragmentDetails.this);
            }
        }else if(resultCode==finishFrag)
        {
            if(strCancelRed==1){
                startActivity(new Intent(mContext, DashboardMaimContext.class).putExtra("objNoti", (Bundle) null));
                ((Activity)mContext).finish();
            }else {
                showSuccessToast(mContext,  "Appointment rescheduled");
                appointmentTabAdapter.removeCompetetorTabFragments(AppointmentBottomFragmentDetails.this);
            }
        }else if(resultCode==startOtpResult)
        {
            updateAcceptRejectStatus("5");  //for start service
        }
    }*/

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }




    AcceptAppBean appintAcceptBean;
    private void getAcceptAppoint() {

        final ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        final Dialog progressDialog = showProgress(mContext, 0);
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
                        showFailToast(mContext, mContext.getResources().getString(R.string.went_wrong));
                    }

                    @Override
                    public void onComplete() {
                        // Updates UI with data
                        progressDialog.dismiss();

                        if (appintAcceptBean.getStatus() == 1) {
                            showSuccessToast(mContext,"Appointment accepted successfully");
                            appointmentData.setAptStatus("1");
                            appointmentTabAdapter.notifyDataSetChanged();
                        } else if (appintAcceptBean.getStatus() == 3) {
                            showFailToast(mContext, "" + appintAcceptBean.getMsg());
                            logout(mContext);
                        }
                        //setEmptyMsg(mDataList, comboPageBinding.recycleCombolist, ivNoData);
                    }
                });
    }



    CheckPenaltyBean checkPenaltyBean;
    String penaltyAmt="0",penaltyPer="0";
    private void getCheckPenalty() {

        final ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        final Dialog progressDialog = showProgress(mContext, 0);
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
                        if(progressDialog!=null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        checkPenaltyBean = loginBeanObj;
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(progressDialog!=null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        showFailToast(mContext, mContext.getResources().getString(R.string.went_wrong));
                    }

                    @Override
                    public void onComplete() {
                        // Updates UI with data
                        if(progressDialog!=null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        if (checkPenaltyBean.getStatus() == 1) {
                            if(checkPenaltyBean.getPenaltyPercentage().size()!=0) {
                                penaltyPer = checkPenaltyBean.getPenaltyPercentage().get(0);
                                penaltyAmt = checkPenaltyBean.getResult().get(0);
                                if (!penaltyPer.equalsIgnoreCase("0")) {
                                    showCancelRequestDialog((Activity) mContext,penaltyPer);
                                } else {
                                    startActivity(new Intent(mContext, RejectionActivity.class).putExtra("aptId", appointmentData.getAptId()).putExtra("penaltyAmt",penaltyAmt).putExtra("aptStatus",appointmentData.getAptStatus()));
                                    //finish();
                                    // dialogCancelAppointment.dismiss();
                                }
                            }else {
                                startActivity(new Intent(mContext, RejectionActivity.class).putExtra("aptId", appointmentData.getAptId()).putExtra("penaltyAmt",penaltyAmt).putExtra("aptStatus",appointmentData.getAptStatus()));
                                //finish();
                                // dialogCancelAppointment.dismiss();
                            }
                        } else if (checkPenaltyBean.getStatus() == 3) {
                            showFailToast(mContext, "" + checkPenaltyBean.getMsg());
                            logout(mContext);
                        }
                        //setEmptyMsg(mDataList, comboPageBinding.recycleCombolist, ivNoData);
                    }
                });
    }
    DialogAppointmentCancellationBinding cancellationBinding;
    Dialog dialogCancelAppointment;
    public void showCancelRequestDialog(Activity inActivity,String penaltyPer) {
        dialogCancelAppointment=new Dialog(inActivity,R.style.CustomAlertDialog);
        cancellationBinding = DialogAppointmentCancellationBinding.inflate(LayoutInflater.from(inActivity));
        dialogCancelAppointment.setContentView(cancellationBinding.getRoot());
        Window window = dialogCancelAppointment.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);
        dialogCancelAppointment.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        if(penaltyPer.equalsIgnoreCase("50"))
        {
            cancellationBinding.tvPenaltydesc.setText("Cancellation Time > 1-4 Hr > 50%");
        }else if(penaltyPer.equalsIgnoreCase("25"))
        {
            cancellationBinding.tvPenaltydesc.setText("Cancellation Time > 4-8 Hr > 25%");
        }else if(penaltyPer.equalsIgnoreCase("10"))
        {
            cancellationBinding.tvPenaltydesc.setText("Cancellation Time > 8-16 Hr > 10%");
        }

        cancellationBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(cancellationBinding.cbTermsConditions.isChecked()) {
                    dialogCancelAppointment.dismiss();
                    startActivity(new Intent(mContext, RejectionActivity.class).putExtra("aptId", appointmentData.getAptId()).putExtra("penaltyAmt",penaltyAmt).putExtra("aptStatus",appointmentData.getAptStatus()));
                }else
                {
                    showFailToast(mContext,"Accept terms & conditions to proceed");
                }
                //finish();
            }
        });
        dialogCancelAppointment.show();

    }


    DialogPaymentTypeBinding dialogPaymentTypeCartBinding;
    String strPaymentType="";
    public void showPaymentTypeDialog(final Activity inActivity) {
        final Dialog dialogCancelAppointment = new Dialog(inActivity, R.style.CustomAlertDialog);
        dialogPaymentTypeCartBinding = DialogPaymentTypeBinding.inflate(LayoutInflater.from(inActivity));
        dialogCancelAppointment.setContentView(dialogPaymentTypeCartBinding.getRoot());
        Window window = dialogCancelAppointment.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);
        dialogCancelAppointment.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        if(appointmentData.getAptPaymentType().equalsIgnoreCase("1"))
        {
            dialogPaymentTypeCartBinding.rbOnlinePay.setChecked(true);
        }else if(appointmentData.getAptPaymentType().equalsIgnoreCase("2"))
        {
            dialogPaymentTypeCartBinding.rbSalon.setChecked(true);
        }
        dialogPaymentTypeCartBinding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioButton radioButton = dialogCancelAppointment.findViewById(dialogPaymentTypeCartBinding.rgPaymentType.getCheckedRadioButtonId());
                if(radioButton!=null) {
                    if (radioButton.getText().toString().equalsIgnoreCase("Online Payment")) {
                        strPaymentType = "1";
                        redeemPointOnlinePay=""+Math.round(total*Integer.parseInt(appointmentBean.getOnlinePaymentRedeemPoint())/100);
                        checkOut(view);
                        //getChangeUnpaidToPaid();
                    } else {
                        strPaymentType = "2";
                        redeemPointOnlinePay="";
                        showPaymentTypeSalonDialog((Activity) mContext);
                    }
                }else
                {
                    showFailToast(mContext,"Please select Payment Type");
                }
                dialogCancelAppointment.dismiss();

            }
        });
        dialogCancelAppointment.show();

    }



    //popup when payment type is salon
    com.prometteur.saloonuser.databinding.DialogPaymentTypeSalonBinding dialogPaymentTypeSalonBinding;
    String redeemPointOnlinePay="";
    public void showPaymentTypeSalonDialog(final Activity inActivity) {
        final Dialog dialogCancelAppointment = new Dialog(inActivity, R.style.CustomAlertDialog);
        dialogPaymentTypeSalonBinding = com.prometteur.saloonuser.databinding.DialogPaymentTypeSalonBinding.inflate(LayoutInflater.from(inActivity));
        dialogCancelAppointment.setContentView(dialogPaymentTypeSalonBinding.getRoot());
        Window window = dialogCancelAppointment.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);
        dialogCancelAppointment.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        dialogPaymentTypeSalonBinding.tvCashMsg.setText("Pay ₹ "+appointmentData.getAptAmount()+" in Cash to Salon");
        dialogPaymentTypeSalonBinding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getChangeUnpaidToPaid();
                dialogCancelAppointment.dismiss();
               // startActivity(new Intent(mContext, RatingActivity.class).putExtra("aptId", appointmentData.getAptId()));
            }
        });
        dialogCancelAppointment.show();

    }


    AcceptAppBean appintPaidStatus;
    private void getChangeUnpaidToPaid() {

        final ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        final Dialog progressDialog = showProgress(mContext, 0);
        //progressDialog.show();
        service.getUnpaidToPaid(""+appointmentData.getAptId(),"1",transactionId,orderId,redeemPointOnlinePay,strPaymentType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AcceptAppBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(AcceptAppBean loginBeanObj) {
                        progressDialog.dismiss();
                        appintPaidStatus = loginBeanObj;
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        showFailToast(mContext, mContext.getResources().getString(R.string.went_wrong));
                    }

                    @Override
                    public void onComplete() {
                        // Updates UI with data
                        progressDialog.dismiss();

                        if (appintPaidStatus.getStatus() == 1) {
                            showSuccessToast(mContext,"Payment completed successfully");
                            //getAppointDetails();
                            btnPayNow.setVisibility(View.GONE);
                        } else if (appintPaidStatus.getStatus() == 3) {
                            showFailToast(mContext, "" + appintPaidStatus.getMsg());
                            logout(mContext);
                        }
                        //setEmptyMsg(mDataList, comboPageBinding.recycleCombolist, ivNoData);
                    }
                });
    }

    String orderId="",transactionId="";
    @Override
    public void onPaymentSuccess(String razorpayPaymentID, PaymentData paymentData) {
        Log.i("RazorPay", razorpayPaymentID + " " + paymentData.getData().toString());
        orderId="";
        transactionId=razorpayPaymentID;
        getChangeUnpaidToPaid();
    }

    @Override
    public void onPaymentError(int code, String response, PaymentData paymentData) {
        Log.i("RazorPay Error", code + " " + response + " " + paymentData.getData().toString());
    }

    public void checkOut(View view) {
        getOrderId();
    }


    public void startPayment(String orderId) {    /**   * Instantiate Checkout   */
        Checkout checkout = new Checkout();  /**   * Set your logo here   */
        checkout.setImage(R.drawable.ic_launcher_background);  /**   * Reference to current activity   */
        final Activity activity = (Activity) mContext;  /**   * Pass your payment options to the Razorpay Checkout as a JSONObject   */

        try {
            JSONObject options = new JSONObject();      /**      * Merchant Name      * eg: ACME Corp || HasGeek etc.      */
            options.put("name", "Merchant Name");      /**      * Description can be anything      * eg: Reference No. #123123 - This order number is passed by you for your internal reference. This is not the `razorpay_order_id`.      *     Invoice Payment      *     etc.      */
            options.put("description", appointmentData.getAptId());
            options.put("order_id", orderId);
            options.put("currency", "INR");      /**      * Amount is always passed in currency subunits      * Eg: "500" = INR 5.00      */
            options.put("amount", Math.round(total * 100));
            checkout.open(activity, options);
        } catch (Exception e) {
            Log.e("RazorPay", "Error in starting Razorpay Checkout", e);
        }
    }
    OrderIdBean orderIdBean;
    Double total=0.0;
    private void getOrderId() {
        final Activity nActivity = (Activity) mContext;
        ApiInterface service = RetrofitInstanceForPay.getClient().create(ApiInterface.class);
        final Dialog progressDialog = showProgress(nActivity, 0);
        progressDialog.show();
        service.getOrderId("INR",
                Integer.parseInt(""+Math.round(total * 100)), 1, "acc_DyJaXXVFAHEFOe",
                "INR", Integer.parseInt(""+Math.round(total * 100)) /*/ 2), "acc_DxFPhTscOkKe37",
                "INR", Integer.parseInt(""+Math.round(total * 100) / 2)*/
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OrderIdBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(OrderIdBean loginBeanObj) {
                        progressDialog.dismiss();
                        orderIdBean = loginBeanObj;
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        Toast.makeText(nActivity, getResources().getString(R.string.went_wrong), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        // Updates UI with data
                        progressDialog.dismiss();
                        if (orderIdBean.getId() != null) {
                            String orderId = orderIdBean.getId();
                            startPayment(orderId);
                        } else {
                            Toast.makeText(nActivity, "" + orderIdBean.toString(), Toast.LENGTH_SHORT).show();
                        }
//                        cPresenter.updateCoinList(allCurrencyList);
                    }
                });
    }
}
