package com.prometteur.saloonuser.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.prometteur.saloonuser.Adapter.AppointmentStatusAdapter;
import com.prometteur.saloonuser.Adapter.ComboListAppDetailsAdapter;
import com.prometteur.saloonuser.Adapter.ComboListCartAdapter;
import com.prometteur.saloonuser.Adapter.PromoOffListAppDetailsAdapter;
import com.prometteur.saloonuser.Adapter.ServiceSelectedAdapter;
import com.prometteur.saloonuser.Constants.ConstantMethods;
import com.prometteur.saloonuser.Constants.ConstantVariables;
import com.prometteur.saloonuser.Fragments.FragmentListSalonView;
import com.prometteur.saloonuser.Model.AcceptAppBean;
import com.prometteur.saloonuser.Model.AppointDetailBean;
import com.prometteur.saloonuser.Model.AppointmentBean;
import com.prometteur.saloonuser.Model.CheckPenaltyBean;
import com.prometteur.saloonuser.Model.OrderIdBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.NetworkChangeReceiver;
import com.prometteur.saloonuser.Utils.Preferences;
import com.prometteur.saloonuser.databinding.ActivityAppointmentDetailsBinding;
import com.prometteur.saloonuser.databinding.DialogAppointmentCancellationBinding;
import com.prometteur.saloonuser.databinding.DialogPaymentTypeBinding;
import com.prometteur.saloonuser.retrofit.ApiInterface;
import com.prometteur.saloonuser.retrofit.RetrofitInstance;
import com.prometteur.saloonuser.retrofit.RetrofitInstanceForPay;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

import java.text.DecimalFormat;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.prometteur.saloonuser.Activities.ActivityHomepage.menuPos;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.strHistory;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.strLat;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.strLong;
import static com.prometteur.saloonuser.Activities.ActivitySalonServices.getServices;
import static com.prometteur.saloonuser.Constants.ConstantMethods.getResizedDrawable;
import static com.prometteur.saloonuser.Constants.ConstantVariables.fromAppointmentCompleted;
import static com.prometteur.saloonuser.Constants.ConstantVariables.fromPayment;
import static com.prometteur.saloonuser.Utils.Utils.getDateShowDDMMMYYYY;
import static com.prometteur.saloonuser.Utils.Utils.getDateShowDayDDMMMYYYY;
import static com.prometteur.saloonuser.Utils.Utils.getTimeShow24to12HR;
import static com.prometteur.saloonuser.Utils.Utils.isNetworkAvailable;
import static com.prometteur.saloonuser.Utils.Utils.logout;
import static com.prometteur.saloonuser.Utils.Utils.showFailToast;
import static com.prometteur.saloonuser.Utils.Utils.showNoInternetDialog;
import static com.prometteur.saloonuser.Utils.Utils.showProgress;
import static com.prometteur.saloonuser.Utils.Utils.showSuccessToast;

public class ActivityAppointmentDetails extends AppCompatActivity implements View.OnClickListener, PaymentResultWithDataListener {

    ActivityAppointmentDetailsBinding appointmentDetailsBinding;
    AppCompatActivity nActivity;
    Bundle nBundle;
    DialogAppointmentCancellationBinding cancellationBinding;
    Dialog dialogCancelAppointment;
NetworkChangeReceiver networkChangeReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appointmentDetailsBinding = ActivityAppointmentDetailsBinding.inflate(getLayoutInflater());
        View view = appointmentDetailsBinding.getRoot();
        setContentView(view);
        nActivity=this;
        nBundle=getIntent().getExtras();
        networkChangeReceiver = new NetworkChangeReceiver();
        appointmentDetailsBinding.conlaybottom.setVisibility(View.GONE);
        appointmentDetailsBinding.conMainLayout.setVisibility(View.GONE);
        if (nBundle!=null){
            if (nBundle.containsKey(fromPayment)){
                appointmentDetailsBinding.btnPayNow.setVisibility(View.VISIBLE);
                appointmentDetailsBinding.view3.setVisibility(View.GONE);
                appointmentDetailsBinding.tvStartotp.setVisibility(View.GONE);
                appointmentDetailsBinding.tvStartOtpCount.setVisibility(View.GONE);
                appointmentDetailsBinding.btnCall.setVisibility(View.GONE);
                appointmentDetailsBinding.btnCancel.setVisibility(View.GONE);
            }
            if (nBundle.containsKey(fromAppointmentCompleted)){
                appointmentDetailsBinding.btnPayNow.setVisibility(View.GONE);
                appointmentDetailsBinding.view3.setVisibility(View.GONE);
                appointmentDetailsBinding.tvStartotp.setVisibility(View.GONE);
                appointmentDetailsBinding.tvStartOtpCount.setVisibility(View.GONE);
                appointmentDetailsBinding.btnCall.setVisibility(View.GONE);
                appointmentDetailsBinding.btnCancel.setVisibility(View.GONE);
                appointmentDetailsBinding.viewbggrey.setVisibility(View.VISIBLE);
                appointmentDetailsBinding.tvPaymentStatusTitle.setVisibility(View.VISIBLE);
                appointmentDetailsBinding.tvPaymentStatus.setVisibility(View.VISIBLE);
                appointmentDetailsBinding.relPayType.setVisibility(View.VISIBLE);
                appointmentDetailsBinding.relPayStatus.setVisibility(View.VISIBLE);
                appointmentDetailsBinding.tvPaymentTypeTitle.setVisibility(View.VISIBLE);
                appointmentDetailsBinding.tvPaymentType.setVisibility(View.VISIBLE);

            }
        }


        appointmentDetailsBinding.btnCall.setOnClickListener(this);
        appointmentDetailsBinding.btnCancel.setOnClickListener(this);
        appointmentDetailsBinding.btnPayNow.setOnClickListener(this);
        appointmentDetailsBinding.ivBackArrowimg.setOnClickListener(this);

        getResizedDrawable(nActivity,R.drawable.ic_location_icon,appointmentDetailsBinding.tvSalonLocation,null,null,R.dimen._11sdp);
        getResizedDrawable(nActivity,R.drawable.ic_location_distance_icon,appointmentDetailsBinding.tvSalonDistance,null,null,R.dimen._11sdp);



    }
long mLastClickTimeCall=0;
long mLastClickTimeCancel=0;
long mLastClickTimePayNow=0;
    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btnCall:
                if (SystemClock.elapsedRealtime() - mLastClickTimeCall < 2000) {
                    return;
                }
                mLastClickTimeCall = SystemClock.elapsedRealtime();
                if(appointmentDetailsBinding.btnCall.getText().toString().equalsIgnoreCase("Accept"))
                {
                    if (isNetworkAvailable(nActivity)) {
                        getAcceptAppoint();
                    } else {
                        showNoInternetDialog(nActivity);
                    }

                }else {
                    ConstantMethods.makePhoneCall(this, 1, "");
                }
                break;
            case R.id.btnCancel:
                if (SystemClock.elapsedRealtime() - mLastClickTimeCancel < 2000) {
                    return;
                }
                mLastClickTimeCancel = SystemClock.elapsedRealtime();
                if(appointmentData!=null) {
                    if (appointmentData.getAptStatus().equalsIgnoreCase("1")) {
                        if (isNetworkAvailable(nActivity)) {
                            getCheckPenalty();
                        } else {
                            showNoInternetDialog(nActivity);
                        }

                    } else
                    {
                        startActivity(new Intent(ActivityAppointmentDetails.this, RejectionActivity.class).putExtra("aptId", appointmentData.getAptId()).putExtra("penaltyAmt", penaltyAmt).putExtra("aptStatus", appointmentData.getAptStatus()));
                    }
                }
                break;
            case R.id.btnPayNow:
                if (SystemClock.elapsedRealtime() - mLastClickTimePayNow < 2000) {
                    return;
                }
                mLastClickTimePayNow = SystemClock.elapsedRealtime();
                if(appointmentDetailsBinding.btnPayNow.getText().toString().equalsIgnoreCase("Pay Now"))
                {
                    showPaymentTypeDialog(nActivity);
                }else if(appointmentDetailsBinding.btnPayNow.getText().toString().equalsIgnoreCase("Write A Review"))
                {
                    startActivity(new Intent(nActivity, RatingActivity.class).putExtra("aptId", appointmentData.getAptId()));
                    finish();
                }else
                {
                    //TODO Add or Update functionality
                }
                break;
            case R.id.ivBackArrowimg:
                if(getIntent().getStringExtra("notiId")!=null || strHistory.equalsIgnoreCase("1")){
                    finish();
                }else {
                    menuPos = 1;
                    startActivity(new Intent(ActivityAppointmentDetails.this, ActivityHomepage.class).putExtra("objNoti", (Bundle) null));
                    finishAffinity();
                }
                break;

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(networkChangeReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        if (isNetworkAvailable(nActivity)) {
            getAppointDetails();
        } else {
            showNoInternetDialog(nActivity);
        }

    }

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
                    startActivity(new Intent(ActivityAppointmentDetails.this, RejectionActivity.class).putExtra("aptId", appointmentData.getAptId()).putExtra("penaltyAmt",penaltyAmt).putExtra("aptStatus",appointmentData.getAptStatus()));
                }else
                {
                    showFailToast(nActivity,"Accept terms & conditions to proceed");
                }
                //finish();
            }
        });
        dialogCancelAppointment.show();

    }

    @Override
    public void onBackPressed() {
        if(getIntent().getStringExtra("rescheduleApt")==null) {
            super.onBackPressed();
            if(getIntent().getStringExtra("notiId")!=null || strHistory.equalsIgnoreCase("1")){
                finish();
            }else {
                menuPos = 1;
                startActivity(new Intent(ActivityAppointmentDetails.this, ActivityHomepage.class).putExtra("objNoti", (Bundle) null));
                finishAffinity();
            }
        }
    }

    AppointDetailBean appointmentBean;
    AppointDetailBean.Result appointmentData;

    double subTotal = 0;
    double totalDiscountPrice = 0.0, discount = 0.0;

    double total=0;
    String strTotal="0";
    private void getAppointDetails() {
        subTotal = 0;
        total=0;
        strTotal="0";
        discount = 0.0;
        totalDiscountPrice = 0.0;
        RetrofitInstance retrofitInstance=new RetrofitInstance(ActivityAppointmentDetails.this);
        final ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        final Dialog progressDialog = showProgress(nActivity, 0);
        progressDialog.show();
        String notiId="";
        if(getIntent().getStringExtra("notiId")!=null)
        {
            notiId=getIntent().getStringExtra("notiId");
        }
        if(strLat.equalsIgnoreCase("0"))
        {
            strLat= Preferences.getPreferenceValue(nActivity,"lat");
            strLong=Preferences.getPreferenceValue(nActivity,"lon");
            if(strLat.equalsIgnoreCase("NA"))
            {
                strLat="0";
                strLong="0";
            }
        }
        String appId="";
        if(getIntent().getStringExtra("appId")!=null)
        {
            appId=getIntent().getStringExtra("appId");
        }
        Log.i("push notification","AppointmentDetails"+appId);
        service.getAppointmentDetail(appId, strLat,strLong,notiId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AppointDetailBean>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(AppointDetailBean loginBeanObj) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        appointmentBean = loginBeanObj;
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        showFailToast(nActivity, nActivity.getResources().getString(R.string.went_wrong));
                    }

                    @Override
                    public void onComplete() {
                        // Updates UI with data
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        if (appointmentBean.getStatus() == 1) {
                            appointmentDetailsBinding.conlaybottom.setVisibility(View.VISIBLE);
                            appointmentDetailsBinding.conMainLayout.setVisibility(View.VISIBLE);
                            appointmentData=appointmentBean.getResult().get(0);
                            if(getIntent().getStringExtra("rescheduleApt")!=null)
                            {
                                appointmentDetailsBinding.ivBackArrowimg.setVisibility(View.GONE);
                            }
                            appointmentDetailsBinding.tvAppoId.setText("Appo ID : "+appointmentData.getAptId());
                            appointmentDetailsBinding.tvSalonName.setText(appointmentData.getBranName());
                            appointmentDetailsBinding.tvAppointmentTime.setText(getTimeShow24to12HR(appointmentData.getAptTime()));
                            appointmentDetailsBinding.tvAppointmentDate.setText(getDateShowDayDDMMMYYYY(appointmentData.getAptDate()));
                            appointmentDetailsBinding.tvSalonLocation.setText(appointmentData.getBranAddr());
                            appointmentDetailsBinding.tvSalonDistance.setText(appointmentData.getDistance()+" KM");



                            if(appointmentData.getSalonRating()!=null) {
                                appointmentDetailsBinding.rbSalonRating.setRating(Float.parseFloat(appointmentData.getSalonRating()));
                            }else
                            {
                                appointmentDetailsBinding.rbSalonRating.setRating(0);
                            }
                            if(appointmentData.getAptPaymentStatus().equalsIgnoreCase("0")) {
                                appointmentDetailsBinding.tvPaymentStatus.setText("Unpaid");
                            }else
                            {
                                appointmentDetailsBinding.tvPaymentStatus.setText("Paid");
                            }

                            if(appointmentData.getAptPaymentType().equalsIgnoreCase("2")) {
                                appointmentDetailsBinding.tvPaymentType.setText("Salon");
                            }else
                            {
                                appointmentDetailsBinding.tvPaymentType.setText("Online");
                            }

                            if(appointmentData.getBranImg()!=null) {
                                try {
                                    Glide.with(nActivity).load(appointmentData.getBranImg()).into(appointmentDetailsBinding.rivSaloonImage);
                                }catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }

                            //combo list
                            appointmentDetailsBinding.recycleComboSelected.setLayoutManager(new LinearLayoutManager(nActivity));
                            appointmentDetailsBinding.recycleComboSelected.setAdapter(new ComboListAppDetailsAdapter(nActivity,appointmentData.getComboServices(),false));
                            //promo offer list
                            appointmentDetailsBinding.recyclePromoOffSelected.setLayoutManager(new LinearLayoutManager(nActivity));
                            appointmentDetailsBinding.recyclePromoOffSelected.setAdapter(new PromoOffListAppDetailsAdapter(nActivity,appointmentData.getPromotionalServices(),false));
                            //single services list
                            appointmentDetailsBinding.recycleServicesSelected.setLayoutManager(new LinearLayoutManager(nActivity));
                            appointmentDetailsBinding.recycleServicesSelected.setAdapter(new ServiceSelectedAdapter(nActivity,appointmentData.getServices()));
                            if(appointmentData.getServices().size()==0)
                            {
                                appointmentDetailsBinding.tvMoreService.setVisibility(View.GONE);
                            }else
                            {
                                appointmentDetailsBinding.tvMoreService.setVisibility(View.VISIBLE);
                            }
                            int offerAppliedCount = 0;
                            for (int i = 0; i < appointmentData.getComboServices().size(); i++) {
                                subTotal = subTotal + Double.parseDouble(appointmentData.getComboServices().get(i).getOfferPrice());
                                totalDiscountPrice = totalDiscountPrice + Double.parseDouble(appointmentData.getComboServices().get(i).getOfferDiscountPrice());
                                discount=discount+(Double.parseDouble(appointmentData.getComboServices().get(i).getOfferPrice())-Double.parseDouble(appointmentData.getComboServices().get(i).getOfferDiscountPrice()));
                                offerAppliedCount++;
                            }
                            for (int i = 0; i < appointmentData.getPromotionalServices().size(); i++) {
                                subTotal = subTotal + Double.parseDouble(appointmentData.getPromotionalServices().get(i).getProofferPrice());
                                totalDiscountPrice = totalDiscountPrice + Double.parseDouble(appointmentData.getPromotionalServices().get(i).getProofferDiscountPrice());
                                discount=discount+(Double.parseDouble(appointmentData.getPromotionalServices().get(i).getProofferPrice())-Double.parseDouble(appointmentData.getPromotionalServices().get(i).getProofferDiscountPrice()));
                                offerAppliedCount++;
                            }
                            for (int i = 0; i < appointmentData.getServices().size(); i++) {
                                subTotal = subTotal + Double.parseDouble(appointmentData.getServices().get(i).getSrvcPrice());
                            }

//                            double gstAmt=(subTotal*Double.parseDouble(appointmentData.getAptServiceCharges().replace("%","")))/100;
                            double gstAmt=0;
                            if(!appointmentData.getAptServiceCharges().isEmpty()) {
                                 gstAmt = Double.parseDouble(appointmentData.getAptServiceCharges().replace("%", ""));
                            }
                            if(appointmentData.getAptCouponPrice()!=null && !appointmentData.getAptCouponPrice().isEmpty() && !(appointmentData.getAptCouponPrice().equalsIgnoreCase("0.0") || appointmentData.getAptCouponPrice().equalsIgnoreCase("0.00") || appointmentData.getAptCouponPrice().equalsIgnoreCase("0"))) {
                                total = gstAmt + subTotal - discount - Double.parseDouble(appointmentData.getAptCouponPrice());
                                appointmentDetailsBinding.tvDiscountPrice.setText("- ₹ "+new DecimalFormat("##.##").format(Double.parseDouble(appointmentData.getAptCouponPrice()))+"/-");
                                appointmentDetailsBinding.tvTotalSavings.setText("- ₹ "+new DecimalFormat("##.##").format(discount+Double.parseDouble(appointmentData.getAptCouponPrice()))+"/-");
                            }else {
                                total = gstAmt + subTotal - discount;
                                appointmentDetailsBinding.linDiscount.setVisibility(View.GONE);
                                appointmentDetailsBinding.greyviewDiscount.setVisibility(View.GONE);
                                appointmentDetailsBinding.tvTotalSavings.setText("- ₹ "+new DecimalFormat("##.##").format(discount)+"/-");
                            }
                            if(appointmentData.getAptPenality()!=null && !appointmentData.getAptPenality().isEmpty() && !(appointmentData.getAptPenality().equalsIgnoreCase("0")||appointmentData.getAptPenality().equalsIgnoreCase("0.00")||appointmentData.getAptPenality().equalsIgnoreCase("0.0")))
                            {
                                appointmentDetailsBinding.tvPenaltyPrice.setText("₹ "+new DecimalFormat("##.##").format(Double.parseDouble(appointmentData.getAptPenality()))+"/-");
                                total=total+Double.parseDouble(appointmentData.getAptPenality());
                            }else
                            {
                                appointmentDetailsBinding.linPenalty.setVisibility(View.GONE);
                                appointmentDetailsBinding.greyview11.setVisibility(View.GONE);
                            }

                            if(!(appointmentData.getAptRedeemPoint().isEmpty() || appointmentData.getAptRedeemPoint().equalsIgnoreCase("0.00")||appointmentData.getAptRedeemPoint().equalsIgnoreCase("0.0")))
                            {
                                if(appointmentData.getAptPenality().isEmpty())
                                {
                                    appointmentData.setAptPenality("0");
                                }
                                if(appointmentData.getAptRedeemPoint().isEmpty())
                                {
                                    appointmentData.setAptRedeemPoint("0");
                                }
                                total=total+Double.parseDouble(appointmentData.getAptPenality())-Double.parseDouble(appointmentData.getAptRedeemPoint());
                                appointmentDetailsBinding.tvRedeemPrice.setText("- ₹ "+new DecimalFormat("##.##").format(Double.parseDouble(appointmentData.getAptRedeemPoint()))+"/-");
                            }else
                            {
                                appointmentDetailsBinding.linRedeemPoint.setVisibility(View.GONE);
                                appointmentDetailsBinding.greyviewRedeemPoint.setVisibility(View.GONE);
                            }
                             strTotal =""+Math.round(total);
                            appointmentDetailsBinding.tvSubTotalPrice.setText("₹ "+new DecimalFormat("##.##").format(subTotal)+"/-");
                            appointmentDetailsBinding.tvTotalPrice.setText("₹ "+strTotal+"/-");
                            appointmentDetailsBinding.tvGstPrice.setText("₹ "+new DecimalFormat("##.##").format(gstAmt)+"/-");
                            appointmentDetailsBinding.tvSavingAmt.setText("- ₹ "+new DecimalFormat("##.##").format(discount)+"/-");

                            if(offerAppliedCount!=0) {
                                appointmentDetailsBinding.tvOfferApplied.setText("" + offerAppliedCount + " Offer Applied");
                            }else
                            {
                                appointmentDetailsBinding.relOfferApp.setVisibility(View.GONE);
                            }


                            if(appointmentData.getAptStatus().equalsIgnoreCase("0")) {
                                appointmentDetailsBinding.tvAppointmentStatus.setText("Pending");
                                appointmentDetailsBinding.tvStartotp.setVisibility(View.GONE);
                                appointmentDetailsBinding.tvStartOtpCount.setVisibility(View.GONE);
                            }else
                            if(appointmentData.getAptStatus().equalsIgnoreCase("1")) {
                                appointmentDetailsBinding.tvStartOtpCount.setText(appointmentData.getAptStartOtp());
                                appointmentDetailsBinding.tvStartotp.setText(getString(R.string.start_otp));
                                appointmentDetailsBinding.tvAppointmentStatus.setText("Accepted");
                                appointmentDetailsBinding.btnCall.setText("Call");
                                appointmentDetailsBinding.btnCancel.setText(" Cancel Appointment ");
                                appointmentDetailsBinding.tvStartotp.setVisibility(View.VISIBLE);
                                appointmentDetailsBinding.tvStartOtpCount.setVisibility(View.VISIBLE);
                                if(getIntent().getStringExtra("rescheduleApt")!=null)
                                {

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            startActivity(new Intent(nActivity, ActivityHomepage.class).putExtra("objNoti", (Bundle) null));
                                            finishAffinity();
                                        }
                                    },5000);

                                }

                            }else if(appointmentData.getAptStatus().equalsIgnoreCase("5")) {
                                appointmentDetailsBinding.tvStartOtpCount.setText(appointmentData.getAptEndOtp());
                                appointmentDetailsBinding.tvStartotp.setText("End OTP : ");
                                appointmentDetailsBinding.tvAppointmentStatus.setText("Ongoing");
                                appointmentDetailsBinding.btnCall.setVisibility(View.GONE);
                                appointmentDetailsBinding.btnCancel.setVisibility(View.GONE);
                               /* appointmentDetailsBinding.btnPayNow.setVisibility(View.VISIBLE);
                                appointmentDetailsBinding.btnPayNow.setText("Add / Update");*/
                            }else if(appointmentData.getAptStatus().equalsIgnoreCase("6")) {
                                appointmentDetailsBinding.tvAppointmentStatus.setText("Finished");
                                appointmentDetailsBinding.btnCall.setVisibility(View.GONE);
                                appointmentDetailsBinding.btnCancel.setVisibility(View.GONE);
                                appointmentDetailsBinding.tvStartotp.setVisibility(View.GONE);
                                appointmentDetailsBinding.tvStartOtpCount.setVisibility(View.GONE);
                                appointmentDetailsBinding.tvPaymentType.setVisibility(View.VISIBLE);
                                appointmentDetailsBinding.tvPaymentTypeTitle.setVisibility(View.VISIBLE);
                                appointmentDetailsBinding.tvPaymentStatus.setVisibility(View.VISIBLE);
                                appointmentDetailsBinding.tvPaymentStatusTitle.setVisibility(View.VISIBLE);
                                appointmentDetailsBinding.relPayType.setVisibility(View.VISIBLE);
                                appointmentDetailsBinding.relPayStatus.setVisibility(View.VISIBLE);
                                if(appointmentData.getAptPaymentType().equalsIgnoreCase("1")) {
                                    appointmentDetailsBinding.btnPayNow.setVisibility(View.VISIBLE);
                                }else
                                {
                                    appointmentDetailsBinding.btnPayNow.setVisibility(View.VISIBLE);

                                }
                                if(appointmentData.getAptPaymentStatus().equalsIgnoreCase("1")) {
                                    appointmentDetailsBinding.btnPayNow.setVisibility(View.GONE);
                                }

                                if(appointmentData.getAptPaymentStatus().equalsIgnoreCase("0")) {
                                    appointmentDetailsBinding.tvPaymentStatus.setText("Unpaid");
                                }else
                                {
                                    if(appointmentData.getAptPaymentType().equalsIgnoreCase("1")){
                                        appointmentDetailsBinding.tvPaymentStatus.setText("Paid");
                                    }else {
                                        appointmentDetailsBinding.tvPaymentStatus.setText("Pending for approval");
                                    }
                                }


                            }else if(appointmentData.getAptStatus().equalsIgnoreCase("4")) {
                                appointmentDetailsBinding.tvAppointmentStatus.setText("Completed");
                                appointmentDetailsBinding.btnCall.setVisibility(View.GONE);
                                appointmentDetailsBinding.btnCancel.setVisibility(View.GONE);
                                appointmentDetailsBinding.tvStartotp.setVisibility(View.GONE);
                                appointmentDetailsBinding.tvStartOtpCount.setVisibility(View.GONE);
                                appointmentDetailsBinding.tvPaymentType.setVisibility(View.VISIBLE);
                                appointmentDetailsBinding.tvPaymentTypeTitle.setVisibility(View.VISIBLE);
                                appointmentDetailsBinding.relPayStatus.setVisibility(View.VISIBLE);
                                appointmentDetailsBinding.relPayType.setVisibility(View.VISIBLE);
                                appointmentDetailsBinding.btnPayNow.setVisibility(View.GONE);
                                appointmentDetailsBinding.tvPaymentStatus.setText("Paid");
                                if(appointmentData.getIsReviewGiven().equalsIgnoreCase("0"))
                                {
                                    appointmentDetailsBinding.btnPayNow.setVisibility(View.VISIBLE);
                                    appointmentDetailsBinding.btnPayNow.setText("Write A Review");
                                }else
                                {
                                    appointmentDetailsBinding.btnPayNow.setVisibility(View.GONE);
                                }
                            }else if(appointmentData.getAptStatus().equalsIgnoreCase("2") || appointmentData.getAptStatus().equalsIgnoreCase("7")|| appointmentData.getAptStatus().equalsIgnoreCase("8")) {
                                appointmentDetailsBinding.tvAppointmentStatus.setTextColor(getResources().getColor(R.color.red));
                                if(appointmentData.getAptStatus().equalsIgnoreCase("2")) {
                                    appointmentDetailsBinding.tvAppointmentStatus.setText("Declined");
                                }else if(appointmentData.getAptStatus().equalsIgnoreCase("7")) {
                                    appointmentDetailsBinding.tvAppointmentStatus.setText("Cancelled");
                                }else if(appointmentData.getAptStatus().equalsIgnoreCase("8")) {
                                    appointmentDetailsBinding.tvAppointmentStatus.setText("Unattended");
                                }
                                appointmentDetailsBinding.btnCall.setVisibility(View.GONE);
                                appointmentDetailsBinding.btnCancel.setVisibility(View.GONE);
                                appointmentDetailsBinding.tvStartotp.setVisibility(View.GONE);
                                appointmentDetailsBinding.tvStartOtpCount.setVisibility(View.GONE);
                                appointmentDetailsBinding.relPayStatus.setVisibility(View.GONE);
                                appointmentDetailsBinding.relPayType.setVisibility(View.GONE);
                                appointmentDetailsBinding.relOfferApp.setVisibility(View.GONE);
                                if(getIntent().getStringExtra("rescheduleApt")!=null)
                                {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            startActivity(new Intent(nActivity, ActivityHomepage.class).putExtra("objNoti", (Bundle) null));
                                            finishAffinity();
                                        }
                                    },5000);

                                }

                            }else if(appointmentData.getAptStatus().equalsIgnoreCase("3"))
                            {
                                appointmentDetailsBinding.ivBackArrowimg.setVisibility(View.GONE);
                                getIntent().putExtra("rescheduleApt","rescheduleApt");
                                appointmentDetailsBinding.tvAppointmentStatus.setText("Rescheduled");
                                appointmentDetailsBinding.btnCall.setText("Accept");
                                appointmentDetailsBinding.btnCancel.setText("Reject");
                                appointmentDetailsBinding.btnCall.setVisibility(View.VISIBLE);
                                appointmentDetailsBinding.btnCancel.setVisibility(View.VISIBLE);
                                appointmentDetailsBinding.btnPayNow.setVisibility(View.GONE);
                                appointmentDetailsBinding.tvStartotp.setVisibility(View.GONE);
                                appointmentDetailsBinding.tvStartOtpCount.setVisibility(View.GONE);
                            }

                        } else if (appointmentBean.getStatus() == 3) {
                            showFailToast(nActivity, "" + appointmentBean.getMsg());
                              logout(nActivity);
                        }
                        //setEmptyMsg(mDataList, comboPageBinding.recycleCombolist, ivNoData);
                    }
                });
    }

    DialogPaymentTypeBinding dialogPaymentTypeCartBinding;
    String strPaymentType="",redeemPointOnlinePay="";
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
                if (radioButton.getText().toString().equalsIgnoreCase("Online Payment")) {
                    strPaymentType = "1";
                    redeemPointOnlinePay=""+Math.round(total*Integer.parseInt(appointmentBean.getOnlinePaymentRedeemPoint())/100);
                    checkOut(view);

                } else {
                    strPaymentType = "2";
                    redeemPointOnlinePay="";
                    showPaymentTypeSalonDialog(nActivity);

                }
                dialogCancelAppointment.dismiss();

            }
        });
        dialogCancelAppointment.show();

    }

    //popup when payment type is salon
 com.prometteur.saloonuser.databinding.DialogPaymentTypeSalonBinding dialogPaymentTypeSalonBinding;
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
        dialogPaymentTypeSalonBinding.tvCashMsg.setText("Pay ₹ "+strTotal+" in Cash to Salon");
        dialogPaymentTypeSalonBinding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogCancelAppointment.dismiss();
                if (isNetworkAvailable(nActivity)) {
                    getChangeUnpaidToPaid();
                } else {
                    showNoInternetDialog(nActivity);
                }

                //startActivity(new Intent(nActivity, RatingActivity.class).putExtra("aptId", appointmentData.getAptId()));
            }
        });
        dialogCancelAppointment.show();

    }
String transactionId="",orderId="";
    @Override //pay_FF4Ip30jnYdEo4 {"razorpay_payment_id":"pay_FF4Ip30jnYdEo4","razorpay_order_id":"order_FF4IgkkQbz8g4a","razorpay_signature":"afec283dcbe16ff62b4ce629baa1b5d4e9ce1e07e324aa0f940b2b5f676160b4"}
    public void onPaymentSuccess(String razorpayPaymentID, PaymentData paymentData) {
        Log.i("RazorPay", razorpayPaymentID + " " + paymentData.getData().toString());
        orderId=paymentData.getOrderId();
        transactionId=paymentData.getPaymentId();
        if (isNetworkAvailable(nActivity)) {
            getChangeUnpaidToPaid();
        } else {
            showNoInternetDialog(nActivity);
        }
    }

    @Override
    public void onPaymentError(int code, String response, PaymentData paymentData) {
        Log.i("RazorPay Error", code + " " + response + " " + paymentData.getData().toString());
    }

    public void checkOut(View view) {
        if (isNetworkAvailable(nActivity)) {
            getOrderId();
        } else {
            showNoInternetDialog(nActivity);
        }
    }


    public void startPayment(String orderId) {    /**   * Instantiate Checkout   */
        Checkout checkout = new Checkout();  /**   * Set your logo here   */
        checkout.setImage(R.drawable.ic_launcher_background);  /**   * Reference to current activity   */
        final Activity activity = this;  /**   * Pass your payment options to the Razorpay Checkout as a JSONObject   */

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
    private void getOrderId() {
        ApiInterface service = RetrofitInstanceForPay.getClient().create(ApiInterface.class);
        final Dialog progressDialog = showProgress(nActivity, 0);
        progressDialog.show();
        total=Double.parseDouble(strTotal);
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
                        Toast.makeText(ActivityAppointmentDetails.this, getResources().getString(R.string.went_wrong), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        // Updates UI with data
                        progressDialog.dismiss();
                        if (orderIdBean.getId() != null) {
                            String orderId = orderIdBean.getId();
                            startPayment(orderId);
                        } else {
                            Toast.makeText(ActivityAppointmentDetails.this, "" + orderIdBean.toString(), Toast.LENGTH_SHORT).show();
                        }
//                        cPresenter.updateCoinList(allCurrencyList);
                    }
                });
    }

    AcceptAppBean appintAcceptBean;
    private void getAcceptAppoint() {

        final ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        final Dialog progressDialog = showProgress(nActivity, 0);
        progressDialog.show();
        service.getAcceptAppointment(""+getIntent().getStringExtra("appId"),"1")
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
                            if (isNetworkAvailable(nActivity)) {
                                getAppointDetails();
                            } else {
                                showNoInternetDialog(nActivity);
                            }
                        } else if (appintAcceptBean.getStatus() == 3) {
                            showFailToast(nActivity, "" + appintAcceptBean.getMsg());
                            logout(nActivity);
                        }
                        //setEmptyMsg(mDataList, comboPageBinding.recycleCombolist, ivNoData);
                    }
                });
    }


    CheckPenaltyBean checkPenaltyBean;
    String penaltyAmt="0",penaltyPer="0";
    private void getCheckPenalty() {

        final ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        final Dialog progressDialog = showProgress(nActivity, 0);
        progressDialog.show();
        service.getCheckPenalty(appointmentData.getAptId())
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
                            if(checkPenaltyBean.getPenaltyPercentage().size()!=0) {
                                penaltyPer = checkPenaltyBean.getPenaltyPercentage().get(0);
                                penaltyAmt = checkPenaltyBean.getResult().get(0);
                                if (!penaltyPer.equalsIgnoreCase("0")) {
                                    showCancelRequestDialog(nActivity,penaltyPer);
                                } else {
                                    startActivity(new Intent(ActivityAppointmentDetails.this, RejectionActivity.class).putExtra("aptId", appointmentData.getAptId()).putExtra("penaltyAmt",penaltyAmt).putExtra("aptStatus",appointmentData.getAptStatus()));
                                    //finish();
                                   // dialogCancelAppointment.dismiss();
                                }
                            }else {
                                startActivity(new Intent(ActivityAppointmentDetails.this, RejectionActivity.class).putExtra("aptId", appointmentData.getAptId()).putExtra("penaltyAmt",penaltyAmt).putExtra("aptStatus",appointmentData.getAptStatus()));
                                //finish();
                               // dialogCancelAppointment.dismiss();
                            }
                        } else if (checkPenaltyBean.getStatus() == 3) {
                            showFailToast(nActivity, "" + checkPenaltyBean.getMsg());
                            logout(nActivity);
                        }
                        //setEmptyMsg(mDataList, comboPageBinding.recycleCombolist, ivNoData);
                    }
                });
    }


    AcceptAppBean appintPaidStatus;
    private void getChangeUnpaidToPaid() {

        final ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        final Dialog progressDialog = showProgress(nActivity, 0);
        //progressDialog.show();
        service.getUnpaidToPaid(""+getIntent().getStringExtra("appId"),"1",transactionId,orderId,redeemPointOnlinePay,strPaymentType)
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
                        showFailToast(nActivity, nActivity.getResources().getString(R.string.went_wrong));
                    }

                    @Override
                    public void onComplete() {
                        // Updates UI with data
                        progressDialog.dismiss();

                        if (appintPaidStatus.getStatus() == 1) {
                            showSuccessToast(nActivity,"Payment completed successfully");
                            getAppointDetails();
                        } else if (appintPaidStatus.getStatus() == 3) {
                            showFailToast(nActivity, "" + appintPaidStatus.getMsg());
                            logout(nActivity);
                        }
                        //setEmptyMsg(mDataList, comboPageBinding.recycleCombolist, ivNoData);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
    }
}
