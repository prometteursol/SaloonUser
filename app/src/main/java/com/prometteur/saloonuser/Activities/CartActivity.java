package com.prometteur.saloonuser.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.prometteur.saloonuser.Adapter.BookingTimeSlotAdapter;
import com.prometteur.saloonuser.Adapter.ComboListCartAdapter;
import com.prometteur.saloonuser.Adapter.OfferListAdapter;
import com.prometteur.saloonuser.Adapter.SelectOperatorBottomAdapter;
import com.prometteur.saloonuser.Adapter.ServiceCartAdapter;
import com.prometteur.saloonuser.Model.BookAppointBean;
import com.prometteur.saloonuser.Model.CartDetailBean;
import com.prometteur.saloonuser.Model.OrderIdBean;
import com.prometteur.saloonuser.Model.SalonDetailBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.Preferences;
import com.prometteur.saloonuser.Utils.TextViewCustomFont;
import com.prometteur.saloonuser.databinding.ActivityCartBinding;
import com.prometteur.saloonuser.databinding.DialogAccountCreatedBinding;
import com.prometteur.saloonuser.databinding.DialogPaymentTypeCartBinding;
import com.prometteur.saloonuser.listener.OnTimeslotItemClickListener;
import com.prometteur.saloonuser.retrofit.ApiInterface;
import com.prometteur.saloonuser.retrofit.RetrofitInstance;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.prometteur.saloonuser.Activities.ActivityHomepage.dateTimeOneTime;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.strAppDate;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.strDate;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.strTime;
import static com.prometteur.saloonuser.Constants.ConstantMethods.getResizedDrawable;
import static com.prometteur.saloonuser.Constants.ConstantMethods.getStrikeString;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERIDVAL;
import static com.prometteur.saloonuser.Utils.Utils.getTimeShow12to24HR;
import static com.prometteur.saloonuser.Utils.Utils.getTimeShow24to12HR;
import static com.prometteur.saloonuser.Utils.Utils.getTimeSlots;
import static com.prometteur.saloonuser.Utils.Utils.isNetworkAvailable;
import static com.prometteur.saloonuser.Utils.Utils.logout;
import static com.prometteur.saloonuser.Utils.Utils.showFailToast;
import static com.prometteur.saloonuser.Utils.Utils.showFailToastForTimeCheck;
import static com.prometteur.saloonuser.Utils.Utils.showFailToastLongTime;
import static com.prometteur.saloonuser.Utils.Utils.showNoInternetDialog;
import static com.prometteur.saloonuser.Utils.Utils.showProgress;
import static com.prometteur.saloonuser.Utils.Utils.showSuccessToast;


public class CartActivity extends AppCompatActivity implements View.OnClickListener {

    static ActivityCartBinding salonServicesBinding;
    private static List<String> timeSlots = new ArrayList<>();
    AppCompatActivity nActivity;
    String branchId = "",mainCat="0";
    String strPaymentType = "0";
    DialogPaymentTypeCartBinding cancellationBinding;
    DialogAccountCreatedBinding requestSendBinding;
    String couponCode = "";
    CartDetailBean cartDetailBean;
    double subTotal = 0;
    String strGst = "0",strServiceCharge = "0";
    OrderIdBean orderIdBean;
    String aptServices = "";
   static BookAppointBean bookAppointBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        salonServicesBinding = ActivityCartBinding.inflate(getLayoutInflater());
        View view = salonServicesBinding.getRoot();
        setContentView(view);
        nActivity = this;
        salonServicesBinding.conlaybottom.setVisibility(View.GONE);
        salonServicesBinding.nsvNestedScroll.setVisibility(View.GONE);
        if (isNetworkAvailable(nActivity)) {
            getCartDetails();
        } else {
            showNoInternetDialog(nActivity);
        }


        salonServicesBinding.tvLocation.setOnClickListener(this);
        salonServicesBinding.ivBackArrowimg.setOnClickListener(this);
        salonServicesBinding.btnBookNow.setOnClickListener(this);
        salonServicesBinding.tvApplyCoupon.setOnClickListener(this);
        salonServicesBinding.tvDateTime.setOnClickListener(this);
        salonServicesBinding.tvAddMoreService.setOnClickListener(this);
       // getResizedDrawable(nActivity, R.drawable.percentage, salonServicesBinding.tvApplyCoupon, null, null, R.dimen._12sdp);
        getResizedDrawable(nActivity, R.drawable.ic_location_icon, salonServicesBinding.tvLocation, null, null, R.dimen._11sdp);
        getStrikeString(salonServicesBinding.tvPricebeforeOffer);

        branchId = getIntent().getStringExtra("branchId");
        mainCat = getIntent().getStringExtra("mainCat");
        if(mainCat==null)
        {
            mainCat="0";
        }
        Checkout.preload(getApplicationContext());
        checkDateTimeIsExpired();

        salonServicesBinding.tvDateTime.setText(Preferences.getPreferenceValue(nActivity, "dateTime"));

        //Preferences.setPreferenceValue(nActivity,"dateTime",strDate+strTime);
        strAppDate= Preferences.getPreferenceValue(nActivity,"appDate");
        strTime=Preferences.getPreferenceValue(nActivity,"appTime");

        couponCode=Preferences.getPreferenceValue(nActivity,"couponCode");
        couponDesc=Preferences.getPreferenceValue(nActivity,"couponDesc");
        couponPrice=Preferences.getPreferenceValue(nActivity,"couponOffPrice");
        if(couponPrice.equalsIgnoreCase("NA"))
        {
            couponPrice="0";
        }
        salonServicesBinding.tvDesc.setVisibility(View.GONE);

    }

    private boolean checkDateTimeIsExpired() {
        String time=Preferences.getPreferenceValue(nActivity, "dateTime");
        if(time!=null && !time.equalsIgnoreCase("NA")){
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("E, dd MMM yyyy, hh:mm a");
            try {
                Date date=simpleDateFormat2.parse(time);
                if(date.getTime()<new Date().getTime())
                {
                    showFailToastLongTime(nActivity,"Selected date & time of appointment is not valid, please select another one & proceed");
                    ActivityBookAppointment filterBottomSheet = new ActivityBookAppointment(CartActivity.this);
                    filterBottomSheet.show(nActivity.getSupportFragmentManager(), filterBottomSheet.getTag());
                    return true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }else
        {
            showFailToastLongTime(nActivity,"Selected date & time of appointment is not valid, please select another one & proceed");
            ActivityBookAppointment filterBottomSheet = new ActivityBookAppointment(CartActivity.this);
            filterBottomSheet.show(nActivity.getSupportFragmentManager(), filterBottomSheet.getTag());
            return true;
        }
        return false;
    }

    long mLastClickTimeMoreServices=0;
   long mLastClickTimeCoupon=0,mLastClickTimeBookNow=0;
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBackArrowimg:
                finish();
                break;

            case R.id.btnBookNow:
                if (SystemClock.elapsedRealtime() - mLastClickTimeBookNow < 2000) {
                    return;
                }
                mLastClickTimeBookNow = SystemClock.elapsedRealtime();
                if(!checkDateTimeIsExpired()) {
                    if (isNetworkAvailable(nActivity)) {
                        getBranchWorkingDetails();
                    } else {
                        showNoInternetDialog(nActivity);
                    }
                }

                break;
            case R.id.tvApplyCoupon:
                if (SystemClock.elapsedRealtime() - mLastClickTimeCoupon < 2000) {
                    return;
                }
                mLastClickTimeCoupon = SystemClock.elapsedRealtime();
                startActivityForResult(new Intent(nActivity, CouponCodeListActivity.class).putExtra("aptAmount",new DecimalFormat("##.##").format(subTotal-pointUse)), 122);
                break;
            case R.id.tvDateTime:
                ActivityBookAppointment filterBottomSheet = new ActivityBookAppointment(CartActivity.this);
                filterBottomSheet.show(nActivity.getSupportFragmentManager(), filterBottomSheet.getTag());
                // filterBottomSheet.show(getSupportFragmentManager(), filterBottomSheet.getTag());
                break;
            case R.id.tvAddMoreService:
                if (SystemClock.elapsedRealtime() - mLastClickTimeMoreServices < 2000) {
                    return;
                }
                mLastClickTimeMoreServices = SystemClock.elapsedRealtime();
                startActivity(new Intent(nActivity, ActivitySalonServices.class).putExtra("branchId", branchId).putExtra("mainCat", mainCat));
                finish();
                break;

        }
    }

    public void showPaymentTypeDialog(final Activity inActivity) {
        final Dialog dialogCancelAppointment = new Dialog(inActivity, R.style.CustomAlertDialog);
        cancellationBinding = DialogPaymentTypeCartBinding.inflate(LayoutInflater.from(inActivity));
        dialogCancelAppointment.setContentView(cancellationBinding.getRoot());
        Window window = dialogCancelAppointment.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);
        dialogCancelAppointment.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        cancellationBinding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioButton radioButton = dialogCancelAppointment.findViewById(cancellationBinding.rgPaymentType.getCheckedRadioButtonId());
                if(radioButton!=null) {
                    if (radioButton.getText().toString().equalsIgnoreCase("Online Payment")) {
                        strPaymentType = "1";
                        //checkOut(view);
                    } else {
                        strPaymentType = "2";


                    }
                    if (isNetworkAvailable(nActivity)) {
                        getBookAppoint();
                    } else {
                        showNoInternetDialog(nActivity);
                    }


                }else {
                    showFailToast(nActivity,"Please select Payment type");
                }

                dialogCancelAppointment.dismiss();

            }
        });
        dialogCancelAppointment.show();

    }

    private void showRequestSendDialog(final Activity inActivity) {
        final Dialog dialogRequestSend = new Dialog(inActivity, R.style.CustomAlertDialog);
        requestSendBinding = DialogAccountCreatedBinding.inflate(LayoutInflater.from(inActivity));
        dialogRequestSend.setContentView(requestSendBinding.getRoot());
        Window window = dialogRequestSend.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);
        dialogRequestSend.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        requestSendBinding.ivCongoimg.setImageDrawable(inActivity.getResources().getDrawable(R.drawable.ic_tick_inside_circle_blue));
        requestSendBinding.tvAccountCreated.setText(nActivity.getResources().getString(R.string.Your_appointment_booking_request));
        requestSendBinding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogRequestSend.dismiss();
                startActivity(new Intent(inActivity, ActivityAppointmentDetails.class).putExtra("appId",""+bookAppointBean.getResult().get(0)));
                finish();
            }
        });
        //if(isFinishing()) {
            dialogRequestSend.show();
       // }
    }
String couponPrice="0",couponDesc="",couponId="0";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 122) {
            couponId = data.getStringExtra("couponId");
            couponCode = data.getStringExtra("couponCode");
            couponPrice = data.getStringExtra("couponOffPrice");
            couponDesc = data.getStringExtra("couponDesc");
            Preferences.setPreferenceValue(nActivity,"couponId",couponId);
            Preferences.setPreferenceValue(nActivity,"couponCode",couponCode);
            Preferences.setPreferenceValue(nActivity,"couponDesc",couponDesc);
            Preferences.setPreferenceValue(nActivity,"couponOffPrice",couponPrice);
            salonServicesBinding.tvDesc.setVisibility(View.VISIBLE);
            salonServicesBinding.tvCloseCoupon.setVisibility(View.VISIBLE);
            salonServicesBinding.ivRightArrow.setVisibility(View.GONE);
            salonServicesBinding.tvApplyCoupon.setText(couponCode);
           // salonServicesBinding.tvDesc.setText(couponDesc);
           // redeemPoints[0] = Integer.parseInt(cartDetailBean.getRedeemPoints().getRs());
            salonServicesBinding.tvDiscount.setText("- ₹ " + new DecimalFormat("##.##").format((Double.parseDouble(couponPrice))));

            finalPrice=(totalDiscountPrice+gstAmt+serviceCharge-(Double.parseDouble(couponPrice)+ redeemPoints));
            salonServicesBinding.tvPriceOffered.setText("₹ " + Math.round(finalPrice));
            if(discount+Double.parseDouble(couponPrice)!=0) {
                salonServicesBinding.linTotSavings.setVisibility(View.VISIBLE);
                salonServicesBinding.tvTotalSavings.setText("- ₹ " + new DecimalFormat("##.##").format(discount + Double.parseDouble(couponPrice)));
            }else
            {
                salonServicesBinding.linTotSavings.setVisibility(View.GONE);
            }
            if((subTotal+gstAmt+serviceCharge)<=finalPrice)
            {
                salonServicesBinding.tvPricebeforeOffer.setVisibility(View.GONE);
            }else
            {
                salonServicesBinding.tvPricebeforeOffer.setVisibility(View.VISIBLE);
            }
            salonServicesBinding.tvCloseCoupon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Preferences.setPreferenceValue(nActivity,"couponId","0");
                    Preferences.setPreferenceValue(nActivity,"couponCode","");
                    Preferences.setPreferenceValue(nActivity,"couponDesc","");
                    Preferences.setPreferenceValue(nActivity,"couponOffPrice","0");
                    salonServicesBinding.tvCloseCoupon.setVisibility(View.GONE);
                    salonServicesBinding.ivRightArrow.setVisibility(View.VISIBLE);
                    salonServicesBinding.tvApplyCoupon.setText("Apply Coupon");
                    salonServicesBinding.tvDesc.setVisibility(View.GONE);
                    couponCode=Preferences.getPreferenceValue(nActivity,"couponCode");
                    couponDesc=Preferences.getPreferenceValue(nActivity,"couponDesc");
                    couponPrice=Preferences.getPreferenceValue(nActivity,"couponOffPrice");

                   // redeemPoints[0] = Double.parseDouble(cartDetailBean.getRedeemPoints().getRs());
                    salonServicesBinding.tvDiscount.setText("- ₹ " + new DecimalFormat("##.##").format((Double.parseDouble(couponPrice))));
                    finalPrice=(totalDiscountPrice+gstAmt+serviceCharge-(Double.parseDouble(couponPrice)+ redeemPoints));
                    salonServicesBinding.tvPriceOffered.setText("₹ " + Math.round(finalPrice));
                    if(discount+Double.parseDouble(couponPrice)!=0) {
                        salonServicesBinding.linTotSavings.setVisibility(View.VISIBLE);
                        salonServicesBinding.tvTotalSavings.setText("- ₹ " + new DecimalFormat("##.##").format(discount + Double.parseDouble(couponPrice)));
                    }else
                    {
                        salonServicesBinding.linTotSavings.setVisibility(View.GONE);
                    }
                    if((subTotal+gstAmt+serviceCharge)<=finalPrice)
                    {
                        salonServicesBinding.tvPricebeforeOffer.setVisibility(View.GONE);
                    }else
                    {
                        salonServicesBinding.tvPricebeforeOffer.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }
    JSONArray jsonArray;
    double finalPrice=0;
     int redeemPoints = 0;
    double totalDiscountPrice = 0, discount = 0;
     double gstAmt=0,userPenalty=0,serviceCharge = 0;
    int pointUse=0;
    private void getCartDetails() {

        final ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        final Dialog progressDialog = showProgress(nActivity, 0);
        progressDialog.show();
        service.getCartDetails(USERIDVAL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CartDetailBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CartDetailBean loginBeanObj) {
                        progressDialog.dismiss();
                        cartDetailBean = loginBeanObj;

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

                        if (cartDetailBean.getStatus() == 1) {
                            salonServicesBinding.conlaybottom.setVisibility(View.VISIBLE);
                            salonServicesBinding.nsvNestedScroll.setVisibility(View.VISIBLE);
                            salonServicesBinding.tvSalonName.setText(cartDetailBean.getSalonDetails().get(0).getBranName());
                            salonServicesBinding.tvLocation.setText(cartDetailBean.getSalonDetails().get(0).getBranAddr());
                            branchId=cartDetailBean.getSalonDetails().get(0).getBranId();
                            // salonServicesBinding.tvDateTime.setText(cartDetailBean.getSalonDetails().get(0).());
                            strGst = cartDetailBean.getSalonDetails().get(0).getBranRateOfGst();
                            String strTimeSlots = cartDetailBean.getSalonDetails().get(0).getBranWorkingHrs();

                            if (!strTimeSlots.isEmpty()) {
                                timeSlots = getTimeSlots(strTimeSlots);
                            }

                            if (cartDetailBean.getSalonDetails().get(0).getBranImg() != null) {
                                Glide.with(nActivity).load(cartDetailBean.getSalonDetails().get(0).getBranImg()).into(salonServicesBinding.ivSalonImage);
                            }
                            if (cartDetailBean.getSalonDetails().get(0).getSalonRating() != null) {
                                salonServicesBinding.rbSalonRating.setRating(Float.parseFloat(cartDetailBean.getSalonDetails().get(0).getSalonRating()));
                            }else
                            {
                                salonServicesBinding.rbSalonRating.setRating(0);
                            }
                            List<CartDetailBean.Result> mDataList = cartDetailBean.getResult();
                            List<CartDetailBean.Result> comboList = new ArrayList<>();
                            List<CartDetailBean.Service> offerServiceList = new ArrayList<>();
                            List<CartDetailBean.Service> serviceList = new ArrayList<>();
                            int offerAppliedCount = 0, comboCount = 0, serviceCount = 0;

                             jsonArray =new JSONArray(); //{"aptser_service_id":"6","aptser_operator_id":"4","aptser_type" : "promotional_offer","aptser_offer_id":"4"},
                            for (int i = 0; i < mDataList.size(); i++) {

                                if (mDataList.get(i).getCartType().equalsIgnoreCase("combo_offer")) {
                                    comboCount++;
                                    comboList.add(mDataList.get(i));
                                    offerAppliedCount++;
                                    if(mDataList.get(i).getDetails().get(0).getOfferPrice()!=null) {
                                        subTotal = subTotal + Double.parseDouble(mDataList.get(i).getDetails().get(0).getOfferPrice().replace(",",""));
                                    }
                                    if(mDataList.get(i).getDetails().get(0).getDiscountPrice()!=null) {
                                        totalDiscountPrice = totalDiscountPrice + Double.parseDouble(mDataList.get(i).getDetails().get(0).getDiscountPrice().replace(",",""));
                                    }
                                    if(mDataList.get(i).getDetails().get(0).getOfferPrice()!=null) {
                                        discount = discount + (Double.parseDouble(mDataList.get(i).getDetails().get(0).getOfferPrice().replace(",","")) - Double.parseDouble(mDataList.get(i).getDetails().get(0).getDiscountPrice().replace(",","")));
                                    }
                                    if(mDataList.get(i).getDetails().get(0).getServices()!=null) {
                                        for (int j = 0; j < mDataList.get(i).getDetails().get(0).getServices().size(); j++) {
                                            JSONObject jsonObject=new JSONObject();
                                            try {
                                                jsonObject.put("aptser_service_id", mDataList.get(i).getDetails().get(0).getServices().get(j).get(0).getSrvcId());
                                                for (int k = 0; k < mDataList.get(i).getDetails().get(0).getServices().get(j).get(0).getOperators().size(); k++) {
                                                    if (mDataList.get(i).getDetails().get(0).getServices().get(j).get(0).getOperators().get(k).getSelected().equalsIgnoreCase("selected")) {
                                                        jsonObject.put("aptser_operator_id",mDataList.get(i).getDetails().get(0).getServices().get(j).get(0).getOperators().get(k).getUserId());
                                                    }
                                                }
                                                if(jsonObject.length()==1)
                                                {
                                                    jsonObject.put("aptser_operator_id","");
                                                }
                                                jsonObject.put("aptser_type", "combo_offer");
                                                jsonObject.put("aptser_offer_id", mDataList.get(i).getDetails().get(0).getOfferId());
                                                jsonArray.put(jsonObject);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                } else {
                                    serviceCount++;
                                    if (mDataList.get(i).getServices() != null) {

                                        for (int j = 0; j < mDataList.get(i).getServices().size(); j++) {
                                            mDataList.get(i).getServices().get(j).setCartId(mDataList.get(i).getCartId());
                                            if(mDataList.get(i).getServices().get(j).getSrvcPrice()!=null) {
                                                subTotal = subTotal + Double.parseDouble(mDataList.get(i).getServices().get(j).getSrvcPrice());
                                            }
                                            if (mDataList.get(i).getServices().get(j).getSrvcDiscountPrice() != null && !mDataList.get(i).getServices().get(j).getSrvcDiscountPrice().isEmpty()) {
                                                totalDiscountPrice = totalDiscountPrice + Double.parseDouble(mDataList.get(i).getServices().get(j).getSrvcPrice());
                                                //discount=discount+(Double.parseDouble(mDataList.get(i).getServices().get(j).getSrvcPrice())-Double.parseDouble(mDataList.get(i).getServices().get(j).getSrvcDiscountPrice()));
                                            }else{
                                                totalDiscountPrice=totalDiscountPrice +Double.parseDouble(mDataList.get(i).getServices().get(j).getSrvcPrice());
                                            }

                                            try {
                                                JSONObject jsonObject=new JSONObject();
                                                jsonObject.put("aptser_service_id", mDataList.get(i).getServices().get(j).getSrvcId());
                                                for(int k=0;k< mDataList.get(i).getServices().get(j).getOperators().size();k++){
                                                    if (mDataList.get(i).getServices().get(j).getOperators().get(k).getSelected().equalsIgnoreCase("selected")) {
                                                        jsonObject.put("aptser_operator_id", mDataList.get(i).getServices().get(j).getOperators().get(k).getUserId());
                                                    }
                                                }
                                                if(jsonObject.length()==1)
                                                {
                                                    jsonObject.put("aptser_operator_id","");
                                                }
                                                jsonObject.put("aptser_type", mDataList.get(i).getCartType());
                                                if(mDataList.get(i).getDetails()!=null) {
                                                    jsonObject.put("aptser_offer_id", mDataList.get(i).getDetails().get(0).getServices().get(0).get(0).getSrvcId());
                                                }else
                                                {
                                                    jsonObject.put("aptser_offer_id", "");
                                                }


                                                jsonArray.put(jsonObject);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        serviceList.addAll(mDataList.get(i).getServices());  //service and promo offer
                                    } else {
                                        mDataList.get(i).getDetails().get(0).getServices().get(0).get(0).setCartId(mDataList.get(i).getCartId());
                                        mDataList.get(i).getDetails().get(0).getServices().get(0).get(0).setSrvcDiscount(mDataList.get(i).getDetails().get(0).getDiscount());
                                        mDataList.get(i).getDetails().get(0).getServices().get(0).get(0).setSrvcDiscountPrice(mDataList.get(i).getDetails().get(0).getDiscountPrice());
                                        mDataList.get(i).getDetails().get(0).getServices().get(0).get(0).setSrvcPrice(mDataList.get(i).getDetails().get(0).getOfferPrice());

                                        offerAppliedCount++;
                                        if (mDataList.get(i).getDetails().get(0).getOfferPrice() != null) {
                                            subTotal = subTotal + Double.parseDouble(mDataList.get(i).getDetails().get(0).getOfferPrice());
                                            totalDiscountPrice=totalDiscountPrice+Double.parseDouble(mDataList.get(i).getDetails().get(0).getDiscountPrice());
                                            discount=discount+(Double.parseDouble(mDataList.get(i).getDetails().get(0).getOfferPrice())-Double.parseDouble(mDataList.get(i).getDetails().get(0).getDiscountPrice()));
                                        }

                                        if(mDataList.get(i).getDetails().get(0).getServices().get(0)!=null) {
                                            for (int j = 0; j < mDataList.get(i).getDetails().get(0).getServices().get(0).size(); j++) {
                                                try {
                                                    JSONObject jsonObject=new JSONObject();
                                                    jsonObject.put("aptser_service_id", mDataList.get(i).getDetails().get(0).getServices().get(0).get(j).getSrvcId());
                                                    for (int k = 0; k < mDataList.get(i).getDetails().get(0).getServices().get(0).get(j).getOperators().size(); k++) {
                                                        if (mDataList.get(i).getDetails().get(0).getServices().get(0).get(j).getOperators().get(k).getSelected().equalsIgnoreCase("selected")) {
                                                            jsonObject.put("aptser_operator_id",mDataList.get(i).getDetails().get(0).getServices().get(0).get(j).getOperators().get(k).getUserId());
                                                        }
                                                    }
                                                    if(jsonObject.length()==1)
                                                    {
                                                        jsonObject.put("aptser_operator_id","");
                                                    }
                                                    jsonObject.put("aptser_type", "promotional_offer");
                                                    jsonObject.put("aptser_offer_id", mDataList.get(i).getDetails().get(0).getOfferId());
                                                    jsonArray.put(jsonObject);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }

                                        offerServiceList.addAll(mDataList.get(i).getDetails().get(0).getServices().get(0));
                                    }
                                }

                            }
                            offerServiceList.addAll(serviceList);
                            strGst=cartDetailBean.getSalonDetails().get(0).getBranRateOfGst().replace("%","");
                            strServiceCharge=cartDetailBean.getServiceCharges().replace("%","");
                            //gstAmt=(subTotal*Integer.parseInt(strGst))/100;
                            serviceCharge=((subTotal-discount)*Double.parseDouble(strServiceCharge))/100;
                           // discount = subTotal-totalDiscountPrice;
                            if(cartDetailBean.getUserPenalty()!=null && !cartDetailBean.getUserPenalty().isEmpty() && !(cartDetailBean.getUserPenalty().equalsIgnoreCase("0")||cartDetailBean.getUserPenalty().equalsIgnoreCase("0.0")||cartDetailBean.getUserPenalty().equalsIgnoreCase("0.00")))
                            {
                                userPenalty=Double.parseDouble(cartDetailBean.getUserPenalty());
                            }else {
                                salonServicesBinding.linPenalty.setVisibility(View.GONE);
                            }
                            final double finalTotalDiscountPrice = totalDiscountPrice;
                            if(redeemPoints!=0)
                            {
                                salonServicesBinding.cbRedeemPoints.setChecked(true);
                            }else
                            {
                                salonServicesBinding.cbRedeemPoints.setChecked(false);
                            }



                            if(comboCount==0 && (mDataList.size() - comboCount)!=0){
                                salonServicesBinding.tvServiceComboCount.setText((mDataList.size() - comboCount) + " Services added");
                            }else if(comboCount!=0 && (mDataList.size() - comboCount)==0){
                                salonServicesBinding.tvServiceComboCount.setText(comboCount + " Combo added");
                            }else if(comboCount!=0 && (mDataList.size() - comboCount)!=0){
                                salonServicesBinding.tvServiceComboCount.setText((mDataList.size() - comboCount) + " Services + " + comboCount + " Combo added");
                            }else
                            {
                                salonServicesBinding.tvServiceComboCount.setVisibility(View.INVISIBLE);
                            }


                            salonServicesBinding.tvSubTotal.setText("₹ " + new DecimalFormat("##.##").format(subTotal));
                            salonServicesBinding.tvYourSavings.setText("- ₹ " + new DecimalFormat("##.##").format(discount));
                            if(discount+Double.parseDouble(couponPrice)!=0) {
                                salonServicesBinding.linTotSavings.setVisibility(View.VISIBLE);
                                salonServicesBinding.tvTotalSavings.setText("- ₹ " + new DecimalFormat("##.##").format(discount + Double.parseDouble(couponPrice)));
                            }else
                            {
                                salonServicesBinding.linTotSavings.setVisibility(View.GONE);
                            }
                            salonServicesBinding.tvDiscount.setText("- ₹ " + new DecimalFormat("##.##").format((Double.parseDouble(couponPrice))));
                            salonServicesBinding.tvGST.setText("₹ " + new DecimalFormat("##.##").format(serviceCharge));

                            salonServicesBinding.tvSavingAmt.setText("₹ " + new DecimalFormat("##.##").format(discount));
                            salonServicesBinding.tvOfferApplied.setText("" + offerAppliedCount + " Offer Applied");
                            salonServicesBinding.tvPenalty.setText("₹ "+new DecimalFormat("##.##").format(userPenalty));

                            finalPrice=(totalDiscountPrice+gstAmt+serviceCharge-(Double.parseDouble(couponPrice)+ redeemPoints)+userPenalty);

//                            salonServicesBinding.tvPriceOffered.setText("₹ " + new DecimalFormat("##.##").format(finalPrice));
                            salonServicesBinding.tvPriceOffered.setText("₹ " + Math.round(finalPrice));
                            salonServicesBinding.tvPricebeforeOffer.setText("₹ " + new DecimalFormat("##.##").format(subTotal+serviceCharge));
if((subTotal+gstAmt+serviceCharge)<=finalPrice)
{
    salonServicesBinding.tvPricebeforeOffer.setVisibility(View.GONE);
}else
{
    salonServicesBinding.tvPricebeforeOffer.setVisibility(View.VISIBLE);
}

                            if(!cartDetailBean.getRedeemPoints().getRs().isEmpty() && !cartDetailBean.getRedeemPoints().getRs().equalsIgnoreCase("0") && Double.parseDouble(cartDetailBean.getRedeemPoints().getRs())>=Double.parseDouble(cartDetailBean.getMinRedeemPointRequired())) {
                                String pointUsePercent="0";
                                if(!cartDetailBean.getRedeemPointUse().equalsIgnoreCase("0"))
                                {
                                    pointUsePercent=cartDetailBean.getRedeemPointUse();
                                }
                                int pointPercent= (int) (Double.parseDouble(cartDetailBean.getRedeemPoints().getRs())*Integer.parseInt(pointUsePercent)/100);

                                if(finalPrice<=pointPercent)
                                {
                                    pointUse= (int) finalPrice;
                                }else {
                                    pointUse = pointPercent;
                                }
                                salonServicesBinding.tvPoints.setText(pointUse + " Pt.");
                                salonServicesBinding.tvPointsAmt.setText("- ₹ " + new DecimalFormat("##.##").format(Double.parseDouble(""+pointUse)));
                                final int finalPointUse = pointUse;
                                salonServicesBinding.cbRedeemPoints.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                        if (b) {
                                            redeemPoints = finalPointUse;
                                            //salonServicesBinding.tvDiscount.setText("- ₹ " + new DecimalFormat("##.##").format((Double.parseDouble(couponPrice)+ redeemPoints[0])));
                                            finalPrice = (finalTotalDiscountPrice + gstAmt+serviceCharge - (Double.parseDouble(couponPrice) + redeemPoints) + userPenalty);
                                            salonServicesBinding.tvPriceOffered.setText("₹ " + Math.round(finalPrice));
                                        } else {
                                            redeemPoints = 0;
                                            // salonServicesBinding.tvDiscount.setText("- ₹ " + new DecimalFormat("##.##").format((Double.parseDouble(couponPrice)+ redeemPoints[0])));
                                            finalPrice = (finalTotalDiscountPrice + gstAmt+serviceCharge - (Double.parseDouble(couponPrice) + redeemPoints) + userPenalty);
                                            salonServicesBinding.tvPriceOffered.setText("₹ " + Math.round(finalPrice));
                                        }

                                        if((subTotal+gstAmt+serviceCharge)<=finalPrice)
                                        {
                                            salonServicesBinding.tvPricebeforeOffer.setVisibility(View.GONE);
                                        }else
                                        {
                                            salonServicesBinding.tvPricebeforeOffer.setVisibility(View.VISIBLE);
                                        }
                                    }
                                });
                            }else
                            {
                                salonServicesBinding.cbRedeemPoints.setOnCheckedChangeListener(null);
                                salonServicesBinding.cbRedeemPoints.setEnabled(false);
                                salonServicesBinding.relRedeemPoint.setVisibility(View.GONE);
                            }

                            if(!couponPrice.equalsIgnoreCase("NA") && !couponCode.isEmpty()) {
                                if (Double.parseDouble(couponPrice) <= subTotal - pointUse) {
                                    salonServicesBinding.tvCloseCoupon.setVisibility(View.VISIBLE);
                                    salonServicesBinding.ivRightArrow.setVisibility(View.GONE);
                                    salonServicesBinding.tvApplyCoupon.setText(couponCode);
                                    //salonServicesBinding.tvDesc.setText(couponDesc);
                                    salonServicesBinding.tvDesc.setVisibility(View.VISIBLE);

                                    salonServicesBinding.tvCloseCoupon.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Preferences.setPreferenceValue(nActivity, "couponId", "0");
                                            Preferences.setPreferenceValue(nActivity, "couponCode", "");
                                            Preferences.setPreferenceValue(nActivity, "couponDesc", "");
                                            Preferences.setPreferenceValue(nActivity, "couponOffPrice", "0");
                                            salonServicesBinding.tvCloseCoupon.setVisibility(View.GONE);
                                            salonServicesBinding.ivRightArrow.setVisibility(View.VISIBLE);
                                            salonServicesBinding.tvApplyCoupon.setText("Apply Coupon");
                                            salonServicesBinding.tvDesc.setVisibility(View.GONE);
                                            couponCode = Preferences.getPreferenceValue(nActivity, "couponCode");
                                            couponDesc = Preferences.getPreferenceValue(nActivity, "couponDesc");
                                            couponPrice = Preferences.getPreferenceValue(nActivity, "couponOffPrice");

                                            // redeemPoints[0] = Double.parseDouble(cartDetailBean.getRedeemPoints().getRs());
                                            salonServicesBinding.tvDiscount.setText("- ₹ " + new DecimalFormat("##.##").format((Double.parseDouble(couponPrice))));
                                            finalPrice = (totalDiscountPrice + gstAmt + serviceCharge - (Double.parseDouble(couponPrice) + redeemPoints));
                                            salonServicesBinding.tvPriceOffered.setText("₹ " + Math.round(finalPrice));
                                            if (discount + Double.parseDouble(couponPrice) != 0) {
                                                salonServicesBinding.linTotSavings.setVisibility(View.VISIBLE);
                                                salonServicesBinding.tvTotalSavings.setText("- ₹ " + new DecimalFormat("##.##").format(discount + Double.parseDouble(couponPrice)));
                                            } else {
                                                salonServicesBinding.linTotSavings.setVisibility(View.GONE);
                                            }
                                            if ((subTotal + gstAmt + serviceCharge) <= finalPrice) {
                                                salonServicesBinding.tvPricebeforeOffer.setVisibility(View.GONE);
                                            } else {
                                                salonServicesBinding.tvPricebeforeOffer.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    });
                                }else
                                {
                                    Preferences.setPreferenceValue(nActivity, "couponId", "0");
                                    Preferences.setPreferenceValue(nActivity, "couponCode", "");
                                    Preferences.setPreferenceValue(nActivity, "couponDesc", "");
                                    Preferences.setPreferenceValue(nActivity, "couponOffPrice", "0");
                                    salonServicesBinding.tvCloseCoupon.setVisibility(View.GONE);
                                    salonServicesBinding.ivRightArrow.setVisibility(View.VISIBLE);
                                    salonServicesBinding.tvApplyCoupon.setText("Apply Coupon");
                                    salonServicesBinding.tvDesc.setVisibility(View.GONE);
                                    couponCode = Preferences.getPreferenceValue(nActivity, "couponCode");
                                    couponDesc = Preferences.getPreferenceValue(nActivity, "couponDesc");
                                    couponPrice = Preferences.getPreferenceValue(nActivity, "couponOffPrice");

                                    // redeemPoints[0] = Double.parseDouble(cartDetailBean.getRedeemPoints().getRs());
                                    salonServicesBinding.tvDiscount.setText("- ₹ " + new DecimalFormat("##.##").format((Double.parseDouble(couponPrice))));
                                    finalPrice = (totalDiscountPrice + gstAmt + serviceCharge - (Double.parseDouble(couponPrice) + redeemPoints));
                                    salonServicesBinding.tvPriceOffered.setText("₹ " + Math.round(finalPrice));
                                    if (discount + Double.parseDouble(couponPrice) != 0) {
                                        salonServicesBinding.linTotSavings.setVisibility(View.VISIBLE);
                                        salonServicesBinding.tvTotalSavings.setText("- ₹ " + new DecimalFormat("##.##").format(discount + Double.parseDouble(couponPrice)));
                                    } else {
                                        salonServicesBinding.linTotSavings.setVisibility(View.GONE);
                                    }
                                    if ((subTotal + gstAmt + serviceCharge) <= finalPrice) {
                                        salonServicesBinding.tvPricebeforeOffer.setVisibility(View.GONE);
                                    } else {
                                        salonServicesBinding.tvPricebeforeOffer.setVisibility(View.VISIBLE);
                                    }
                                }
                            }else
                            {
                                couponPrice="0";
                                couponDesc="";
                            }
                            if(comboList.size()!=0) {
                                salonServicesBinding.recycleComboListBottom.setLayoutManager(new LinearLayoutManager(nActivity));
                                salonServicesBinding.recycleComboListBottom.setAdapter(new ComboListCartAdapter(nActivity, comboList, false));
                            }else
                            {
                                salonServicesBinding.recycleComboListBottom.setVisibility(View.GONE);
                            }
                            if(offerServiceList.size()!=0) {
                                salonServicesBinding.recycleServiceBottom.setLayoutManager(new LinearLayoutManager(nActivity));
                                salonServicesBinding.recycleServiceBottom.setAdapter(new ServiceCartAdapter(nActivity, offerServiceList, true));
                            }else
                            {
                                salonServicesBinding.recycleServiceBottom.setVisibility(View.GONE);
                            }
                            Preferences.setPreferenceValue(nActivity,"workingHour",cartDetailBean.getSalonDetails().get(0).getBranWorkingHrs());
                            Preferences.setPreferenceValue(nActivity,"holidayFrom",cartDetailBean.getSalonDetails().get(0).getBranHolidayFrom());
                            Preferences.setPreferenceValue(nActivity,"holidayTo",cartDetailBean.getSalonDetails().get(0).getBranHolidayTo());
                            Preferences.setPreferenceValue(nActivity,"offDay",cartDetailBean.getSalonDetails().get(0).getBranOffDay());
                            Preferences.setPreferenceValue(nActivity,"offlineEndTime",cartDetailBean.getSalonDetails().get(0).getBranOpenedOn());

                        } else if (cartDetailBean.getStatus() == 0) {
                            salonServicesBinding.nsvNestedScroll.setVisibility(View.GONE);
                            salonServicesBinding.conlaybottom.setVisibility(View.GONE);
                            salonServicesBinding.cbRedeemPoints.setVisibility(View.GONE);
                            salonServicesBinding.conlayEmpty.setVisibility(View.VISIBLE);
                            //  logout(mContext);
                        } else if (cartDetailBean.getStatus() == 3) {
                            showFailToast(nActivity, "" + cartDetailBean.getMsg());
                            salonServicesBinding.cbRedeemPoints.setVisibility(View.GONE);
                            salonServicesBinding.nsvNestedScroll.setVisibility(View.GONE);
                            salonServicesBinding.conlaybottom.setVisibility(View.GONE);
                            salonServicesBinding.conlayEmpty.setVisibility(View.VISIBLE);
                              logout(nActivity);
                        }
                        //setEmptyMsg(mDataList, comboPageBinding.recycleCombolist, ivNoData);
                    }
                });
    }



    private void getBookAppoint() {

        final ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        final Dialog progressDialog = showProgress(nActivity, 0);
        progressDialog.show();
        aptServices=jsonArray.toString();
        SimpleDateFormat slotTimeRes = new SimpleDateFormat("hh:mm a");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date changeFTime=null;
        try {
             changeFTime=slotTimeRes.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String timeIn24Hr=sdf.format(changeFTime.getTime());
        String strRedeemPoints="",strUserPenaulty="";
        if(redeemPoints==0)
        {
            strRedeemPoints="";
        }else
        {
            strRedeemPoints=""+redeemPoints;
        }
        if(userPenalty==0.0 || userPenalty==0)
        {
            strUserPenaulty="";
        }else
        {
            strUserPenaulty=""+userPenalty;
        }
        service.getBookAppoint(branchId, USERIDVAL, strAppDate, timeIn24Hr, new DecimalFormat("##.##").format(gstAmt), new DecimalFormat("##.##").format(subTotal),"" +Math.round(finalPrice), strPaymentType, couponId, aptServices,strUserPenaulty,strRedeemPoints,""+new DecimalFormat("##.##").format(serviceCharge),couponPrice)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BookAppointBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BookAppointBean loginBeanObj) {
                        progressDialog.dismiss();
                        bookAppointBean = loginBeanObj;

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
                        //showRequestSendDialog(nActivity);
                        if (bookAppointBean.getStatus() == 1) {
                            dateTimeOneTime=false;
                            Preferences.setPreferenceValue(nActivity, "dateTimeOneTime","false");
                            Preferences.setPreferenceValue(nActivity, "oneTimeSalonId","0");
                            strTime="";
                            strDate="";strAppDate="";
                            Preferences.setPreferenceValue(nActivity, "dateTime","");
                            //coupon code clear
                            Preferences.setPreferenceValue(nActivity,"couponCode","");
                            Preferences.setPreferenceValue(nActivity,"couponDesc","");
                            Preferences.setPreferenceValue(nActivity,"couponOffPrice","0");
                            showRequestSendDialog(nActivity);
                        } else if (bookAppointBean.getStatus() == 3) {
                            showFailToast(nActivity, "" + bookAppointBean.getMsg());
                              logout(nActivity);
                        }
                        //setEmptyMsg(mDataList, comboPageBinding.recycleCombolist, ivNoData);
                    }
                });
    }
    SalonDetailBean workingDetails;
    private void getBranchWorkingDetails() {

        final ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        final Dialog progressDialog = showProgress(nActivity, 0);
        progressDialog.show();
        service.getBranchWorkingDetails(branchId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SalonDetailBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(SalonDetailBean loginBeanObj) {
                        progressDialog.dismiss();
                        workingDetails = loginBeanObj;

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
                        //showRequestSendDialog(nActivity);
                        if (workingDetails.getStatus() == 1) {
                            Preferences.setPreferenceValue(nActivity,"workingHour",workingDetails.getResult().get(0).getBranWorkingHrs());
                            Preferences.setPreferenceValue(nActivity,"holidayFrom",workingDetails.getResult().get(0).getBranHolidayFrom());
                            Preferences.setPreferenceValue(nActivity,"holidayTo",workingDetails.getResult().get(0).getBranHolidayTo());
                            Preferences.setPreferenceValue(nActivity,"offDay",workingDetails.getResult().get(0).getBranOffDay());
                            Preferences.setPreferenceValue(nActivity,"offlineEndTime",workingDetails.getResult().get(0).getBranOpenedOn());
                            if(getCheckDateAndTime()) {
                                showPaymentTypeDialog(nActivity);
                            }
                        } else if (workingDetails.getStatus() == 3) {
                            showFailToast(nActivity, "" + workingDetails.getMsg());
                              logout(nActivity);
                        }
                        //setEmptyMsg(mDataList, comboPageBinding.recycleCombolist, ivNoData);
                    }
                });
    }

    public static class ActivityBookAppointment extends BottomSheetDialogFragment implements View.OnClickListener, OnTimeslotItemClickListener {


       // ActivityBookAppointmentBinding bookAppointmentBinding;
        AppCompatActivity nActivity;
        Dialog dialogRequestSend;
        DialogAccountCreatedBinding requestSendBinding;

        public ActivityBookAppointment(AppCompatActivity nActivity) {
            this.nActivity = nActivity;
        }


     /*   @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment

        }*/

        /*@Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            GridLayoutManager gLayoutManager = new GridLayoutManager(nActivity, 4);
            bookAppointmentBinding.recycleTimeSlot.setLayoutManager(gLayoutManager);
            final String[] strTimeSlots = {Preferences.getPreferenceValue(nActivity, "workingHour")};
            final String strHolidayFrom= Preferences.getPreferenceValue(nActivity,"holidayFrom");
            final String strHolidayTo= Preferences.getPreferenceValue(nActivity,"holidayTo");
            final String strOffDay=Preferences.getPreferenceValue(nActivity,"offDay");
            final String strOfflineEndTime=Preferences.getPreferenceValue(nActivity,"offlineEndTime");
            *//*if (!strTimeSlots.isEmpty()) {
                timeSlots = getTimeSlots(strTimeSlots);
            }
            bookAppointmentBinding.recycleTimeSlot.setAdapter(new BookingTimeSlotAdapter(nActivity, timeSlots, ActivityBookAppointment.this));*//*
            bookAppointmentBinding.btnBookNow.setOnClickListener(this);
            bookAppointmentBinding.ivCloseCrossimg.setOnClickListener(this);
            bookAppointmentBinding.calendarView.setMinDate(System.currentTimeMillis() - 1000);

            bookAppointmentBinding.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(CalendarView view, int year, int month, int day){

                    // add one because month starts at 0
                    //month = month + 1;
                    // output to log cat **not sure how to format year to two places here**
                    String newDate = year+"-"+month+"-"+day;
                    Calendar calendar=Calendar.getInstance();
                    calendar.set( year,  month, day);
                    Log.d("NEW_DATE", newDate);
                    long date = calendar.getTimeInMillis();

                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat simpleDateFormatEndDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


                    try {


                        Date date1=simpleDateFormat.parse(newDate);
                        Date offlineEndDate=simpleDateFormatEndDate.parse(strOfflineEndTime);
                        Date fromDate=simpleDateFormat.parse(strHolidayFrom);
                        Date toDate=simpleDateFormat.parse(strHolidayTo);
                        Log.i("fromDate",fromDate.toString()+" toDate "+toDate.toString());
                        long lonHolFrom=fromDate.getTime();
                        long lonHolTo=toDate.getTime();
                        long todayDateTime=date1.getTime();
                        long todayDateTimeEndTime=offlineEndDate.getTime();
                        date1.setMonth((date1.getMonth()+1));

                        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                        String dayOfTheWeek = sdf.format(date1);

                        if(!dayOfTheWeek.equalsIgnoreCase(strOffDay)) {

                            if (!(date1.getTime() >= lonHolFrom && date1.getTime() <= lonHolTo)) {

                                strAppDate = simpleDateFormat.format(date);
                                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("E, dd MMM yyyy, ");
                                strDate = simpleDateFormat2.format(date);
                                bookAppointmentBinding.tvSelectDateTitle.setVisibility(View.GONE);
                                bookAppointmentBinding.calendarView.setVisibility(View.GONE);
                                bookAppointmentBinding.tvSelectTimeSlotTitle.setVisibility(View.VISIBLE);
                                bookAppointmentBinding.recycleTimeSlot.setVisibility(View.VISIBLE);
                                bookAppointmentBinding.btnBookNow.setVisibility(View.VISIBLE);
                                if (!strTimeSlots[0].isEmpty()) {
                                    Date curDate = new Date();
                                    date1.setHours(0);//1591883765659 1591813800000
                                    date1.setMinutes(0);
                                    date1.setSeconds(0);
                                    curDate.setHours(0);
                                    curDate.setMinutes(0);
                                    curDate.setSeconds(0);


                                    if (date1.getDate() == curDate.getDate() && date1.getMonth() == curDate.getMonth() && date1.getYear() == curDate.getYear()) {
                                        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
                                        String changeFTime;
                                        if(todayDateTime>todayDateTimeEndTime) {
                                            changeFTime = sdf1.format(new Date().getTime());
                                        }else
                                        {
                                            Date date2=new Date();
                                            date2.setTime(todayDateTimeEndTime);
                                            changeFTime = sdf1.format(date2.getTime());
                                        }
                                        strTimeSlots[0] = changeFTime + "-" + strTimeSlots[0].split("-")[1];
                                    }*//*if(date1.compareTo(curDate)==0)
                            {
                                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                                String changeFTime=sdf.format(curDate.getTime());
                                strTimeSlots[0] =changeFTime+"-"+ strTimeSlots[0].split("-")[1];
                            }*//*


                                    timeSlots = getTimeSlots(strTimeSlots[0]);
                                }
                                bookAppointmentBinding.recycleTimeSlot.setAdapter(new BookingTimeSlotAdapter(nActivity, timeSlots, ActivityBookAppointment.this));

                            } else {
                                showFailToast(nActivity, getString(R.string.salon_off));
                            }

                        }else {
                            showFailToast(nActivity, getString(R.string.salon_off));
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            });

            bookAppointmentBinding.calendarView.setMinDate(System.currentTimeMillis() - 1000);
            bookAppointmentBinding.calendarView.setSystemUiVisibility(nActivity.getResources().getColor(R.color.skyBluelilDark));
        }*/
        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.btnBookNow:
                    if(!strTime.isEmpty()) {
                    salonServicesBinding.tvDateTime.setText("" + strDate + strTime);
                    Preferences.setPreferenceValue(nActivity,"dateTime",strDate+strTime);
                    Preferences.setPreferenceValue(nActivity,"appDate",strAppDate);
                    Preferences.setPreferenceValue(nActivity,"appTime",strTime);

                    dismiss();
                    }else
                    {
                        showFailToast(nActivity,"Select time slot");
                    }
                    break;
                case R.id.ivCloseCrossimg:
                    //nActivity.finish();
                    dismiss();
                    break;

            }
        }

        private void showRequestSendDialog(final Activity inActivity) {
            dialogRequestSend = new Dialog(inActivity, R.style.CustomAlertDialog);
            requestSendBinding = DialogAccountCreatedBinding.inflate(LayoutInflater.from(inActivity));
            dialogRequestSend.setContentView(requestSendBinding.getRoot());
            Window window = dialogRequestSend.getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.CENTER;
            window.setAttributes(wlp);
            dialogRequestSend.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            requestSendBinding.ivCongoimg.setImageDrawable(inActivity.getResources().getDrawable(R.drawable.ic_tick_inside_circle_blue));
            requestSendBinding.tvAccountCreated.setText(nActivity.getResources().getString(R.string.Your_appointment_booking_request));
            requestSendBinding.btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogRequestSend.dismiss();
                    startActivity(new Intent(inActivity, ActivityAppointmentDetails.class).putExtra("appId",""+bookAppointBean.getResult().get(0)));


                }
            });
            dialogRequestSend.show();

        }

        @Override
        public void onCancel(DialogInterface dialog) {
            super.onCancel(dialog);
            handleUserExit();
        }


        private void handleUserExit() {
            //Toast.makeText(nActivity, "TODO - SAVE data or similar", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onItemClick(String item) {
            strTime = item;
            Preferences.setPreferenceValue(nActivity, "time", item);
        }

        @Override
        public void onOperatorClick(String opId) {

        }

        private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    dismiss();
                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        };

        RecyclerView recycleTimeSlot;
        Button btnBookNow;
        ImageView ivCloseCrossimg;
        CalendarView calendarView;
        TextViewCustomFont tvSelectDateTitle,tvSelectTimeSlotTitle;
        @SuppressLint("RestrictedApi")
        @Override
        public void setupDialog(@NonNull Dialog dialog, int style) {
            super.setupDialog(dialog, style);

           // bookAppointmentBinding = ActivityBookAppointmentBinding.inflate(getLayoutInflater());

            View contentView=View.inflate(getContext(), R.layout.activity_book_appointment, null);
//            View contentView=bookAppointmentBinding.getRoot();
            dialog.setContentView(contentView);

            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
            CoordinatorLayout.Behavior behavior = params.getBehavior();

            if( behavior != null && behavior instanceof BottomSheetBehavior ) {
                ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
            }
            getActivity().getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

            recycleTimeSlot=contentView.findViewById(R.id.recycleTimeSlot);
            btnBookNow=contentView.findViewById(R.id.btnBookNow);
            ivCloseCrossimg=contentView.findViewById(R.id.ivCloseCrossimg);
            calendarView=contentView.findViewById(R.id.calendarView);
            tvSelectDateTitle=contentView.findViewById(R.id.tvSelectDateTitle);
            tvSelectTimeSlotTitle=contentView.findViewById(R.id.tvSelectTimeSlotTitle);

            GridLayoutManager gLayoutManager = new GridLayoutManager(nActivity, 4);
            recycleTimeSlot.setLayoutManager(gLayoutManager);
            final String[] strTimeSlots = {Preferences.getPreferenceValue(nActivity, "workingHour")};
            final String strHolidayFrom= Preferences.getPreferenceValue(nActivity,"holidayFrom");
            final String strHolidayTo= Preferences.getPreferenceValue(nActivity,"holidayTo");
            final String strOffDay=Preferences.getPreferenceValue(nActivity,"offDay");
            final String strOfflineEndTime=Preferences.getPreferenceValue(nActivity,"offlineEndTime");
            if (!strTimeSlots[0].isEmpty()) {
                timeSlots = getTimeSlots(strTimeSlots[0]);
            }
            recycleTimeSlot.setAdapter(new BookingTimeSlotAdapter(nActivity, timeSlots, ActivityBookAppointment.this));
            btnBookNow.setOnClickListener(this);
            ivCloseCrossimg.setOnClickListener(this);
            calendarView.setMinDate(System.currentTimeMillis() - 1000);
            btnBookNow.setVisibility(View.GONE);
            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(CalendarView view, int year, int month, int day){

                    // add one because month starts at 0
                    //month = month + 1;
                    // output to log cat **not sure how to format year to two places here**
                    String newDate = year+"-"+month+"-"+day;
                    Calendar calendar=Calendar.getInstance();
                    calendar.set( year,  month, day);
                    Log.d("NEW_DATE", newDate);
                    long date = calendar.getTimeInMillis();

                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat simpleDateFormatEndDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


                    try {


                        Date date1=simpleDateFormat.parse(newDate);
                        Date offlineEndDate=simpleDateFormatEndDate.parse(strOfflineEndTime);
                        Date fromDate=simpleDateFormat.parse(strHolidayFrom);
                        Date toDate=simpleDateFormat.parse(strHolidayTo);
                        Log.i("fromDate",fromDate.toString()+" toDate "+toDate.toString());
                        long lonHolFrom=fromDate.getTime();
                        long lonHolTo=toDate.getTime();
                        long todayDateTime=date1.getTime();
                        long todayDateTimeEndTime=offlineEndDate.getTime();
                        date1.setMonth((date1.getMonth()+1));

                        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                        String dayOfTheWeek = sdf.format(date1);

                        if(!dayOfTheWeek.equalsIgnoreCase(strOffDay)) {

                            if (!(date1.getTime() >= lonHolFrom && date1.getTime() <= lonHolTo)) {

                                strAppDate = simpleDateFormat.format(date);
                                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("E, dd MMM yyyy, ");
                                strDate = simpleDateFormat2.format(date);
                                strTimeSlots[0]=Preferences.getPreferenceValue(nActivity, "workingHour");
                                strTime="";
                                if (!strTimeSlots[0].isEmpty()) {
                                    Date curDate = new Date();
                                    Date nextDate = new Date();
                                    date1.setHours(0);//1591883765659 1591813800000
                                    date1.setMinutes(0);
                                    date1.setSeconds(0);
                                    curDate.setHours(0);
                                    curDate.setMinutes(0);
                                    curDate.setSeconds(0);
                                    nextDate.setHours(0);
                                    nextDate.setMinutes(0);
                                    nextDate.setSeconds(0);

                                    SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
                                    String changeFTime;

                                    if (date1.getDate() == curDate.getDate() && date1.getMonth() == curDate.getMonth() && date1.getYear() == curDate.getYear()) {

                                        if (todayDateTime > todayDateTimeEndTime) {
                                            changeFTime = sdf1.format(new Date().getTime());
                                        } else {
                                            Date date2 = new Date();
                                            date2.setTime(todayDateTimeEndTime);
                                            changeFTime = sdf1.format(date2.getTime());
                                        }
                                        strTimeSlots[0] = changeFTime + "-" + strTimeSlots[0].split("-")[1];


                                        timeSlots = getTimeSlots(strTimeSlots[0]);
                                        Date dateEndWithDate = new Date();
                                        Date dateEnd = null;
                                        try {
                                            dateEnd = sdf1.parse(strTimeSlots[0].split("-")[1]);
                                            dateEndWithDate.setHours(dateEnd.getHours());
                                            dateEndWithDate.setMinutes(dateEnd.getMinutes());
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                        if (new Date().getTime() < dateEndWithDate.getTime()) {
                                            tvSelectDateTitle.setVisibility(View.GONE);
                                            calendarView.setVisibility(View.GONE);
                                            tvSelectTimeSlotTitle.setVisibility(View.VISIBLE);
                                            recycleTimeSlot.setVisibility(View.VISIBLE);
                                            btnBookNow.setVisibility(View.VISIBLE);
                                            timeSlots = getTimeSlots(strTimeSlots[0]);
                                            recycleTimeSlot.setAdapter(new BookingTimeSlotAdapter(nActivity, timeSlots, ActivityBookAppointment.this));
                                        } else {
                                            showFailToast(nActivity, getString(R.string.salon_off_time));
                                        }
                                    } /*else if (date1.getDate() == nextDate.getDate() && date1.getMonth() == nextDate.getMonth() && date1.getYear() == nextDate.getYear()) //next day
                                    {
                                        tvSelectDateTitle.setVisibility(View.GONE);
                                        calendarView.setVisibility(View.GONE);
                                        tvSelectTimeSlotTitle.setVisibility(View.VISIBLE);
                                        recycleTimeSlot.setVisibility(View.VISIBLE);
                                        btnBookNow.setVisibility(View.VISIBLE);

                                        Date date2=sdf1.parse(strTimeSlots[0].split("-")[0]);
                                        Date date3=new Date();
                                        date3.setTime(date2.getTime()+3600000);  //add 1 hour in next day
                                        changeFTime = sdf1.format(date3.getTime());

                                        strTimeSlots[0] = changeFTime + "-" + strTimeSlots[0].split("-")[1];

                                        timeSlots = getTimeSlots(strTimeSlots[0]);
                                        recycleTimeSlot.setAdapter(new BookingTimeSlotAdapter(nActivity, timeSlots, ActivityBookAppointment.this));
                                    }*/else  {
                                        String strWeekDay="";
                                        switch (strOffDay)
                                        {
                                            case "Monday":
                                                strWeekDay="Tuesday";
                                                break;
                                            case "Tuesday":
                                                strWeekDay="Wednesday";
                                                break;
                                            case "Wednesday":
                                                strWeekDay="Thursday";
                                                break;
                                            case "Thursday":
                                                strWeekDay="Friday";
                                                break;
                                            case "Friday":
                                                strWeekDay="Saturday";
                                                break;
                                            case "Saturday":
                                                strWeekDay="Sunday";
                                                break;
                                            case "Sunday":
                                                strWeekDay="Monday";
                                                break;
                                        }
                                        if (date1.getTime() == (lonHolTo+86400000)) {
                                            nextDate.setTime(lonHolTo);
                                        }else if(dayOfTheWeek.equalsIgnoreCase(strWeekDay)) {
                                            nextDate.setTime(date1.getTime());
                                        }else {
                                            nextDate = new Date();
                                            nextDate.setTime(nextDate.getTime()+86400000); //added 24 hour in current date
                                        }



                                        nextDate.setHours(0);
                                        nextDate.setMinutes(0);
                                        nextDate.setSeconds(0);
                                        if (date1.getDate() == nextDate.getDate() && date1.getMonth() == nextDate.getMonth() && date1.getYear() == nextDate.getYear()) //next day
                                        {

                                            Date dateEndWithDate= new Date();
                                            Date dateEnd= null;
                                            try {
                                                dateEnd = sdf1.parse(strTimeSlots[0].split("-")[1]);
                                                dateEndWithDate.setHours(dateEnd.getHours());
                                                dateEndWithDate.setMinutes(dateEnd.getMinutes());
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            tvSelectDateTitle.setVisibility(View.GONE);
                                            calendarView.setVisibility(View.GONE);
                                            tvSelectTimeSlotTitle.setVisibility(View.VISIBLE);
                                            recycleTimeSlot.setVisibility(View.VISIBLE);
                                            btnBookNow.setVisibility(View.VISIBLE);
                                            if(!(new Date().getTime()<dateEndWithDate.getTime())) {//when todays time is closed


                                                Date date2 = null;
                                                try {
                                                    date2 = sdf1.parse(strTimeSlots[0].split("-")[0]);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                                Date date3 = new Date();
                                                date3.setTime(date2.getTime() + 3600000);  //add 1 hour in next day
                                                changeFTime = sdf1.format(date3.getTime());

                                                strTimeSlots[0] = changeFTime + "-" + strTimeSlots[0].split("-")[1];
                                            }
                                            timeSlots = getTimeSlots(strTimeSlots[0]);
                                            recycleTimeSlot.setAdapter(new BookingTimeSlotAdapter(nActivity, timeSlots, ActivityBookAppointment.this));
                                        } else {
                                            tvSelectDateTitle.setVisibility(View.GONE);
                                            calendarView.setVisibility(View.GONE);
                                            tvSelectTimeSlotTitle.setVisibility(View.VISIBLE);
                                            recycleTimeSlot.setVisibility(View.VISIBLE);
                                            btnBookNow.setVisibility(View.VISIBLE);
                                            timeSlots = getTimeSlots(strTimeSlots[0]);
                                            recycleTimeSlot.setAdapter(new BookingTimeSlotAdapter(nActivity, timeSlots, ActivityBookAppointment.this));
                                        }
                                    }
                                }
                            } else {
                                showFailToast(nActivity, getString(R.string.salon_off));
                            }

                        }else {
                            showFailToast(nActivity, getString(R.string.salon_off));
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            });

            calendarView.setMinDate(System.currentTimeMillis() - 1000);
            calendarView.setSystemUiVisibility(nActivity.getResources().getColor(R.color.skyBluelilDark));
        }
    }


    public boolean getCheckDateAndTime()
    {
        final String[] strTimeSlots = {Preferences.getPreferenceValue(nActivity, "workingHour")};
        final String strHolidayFrom= Preferences.getPreferenceValue(nActivity,"holidayFrom");
        final String strHolidayTo= Preferences.getPreferenceValue(nActivity,"holidayTo");
        final String strOffDay=Preferences.getPreferenceValue(nActivity,"offDay");
        final String strOfflineEndTime=Preferences.getPreferenceValue(nActivity,"offlineEndTime");

        if(strTime==null){
            strTime="00:00 AM";
        }
        String newDate = strAppDate+" "+getTimeShow12to24HR(strTime)+":00";


        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormatEndDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        try {


            Date date1=simpleDateFormat.parse(strAppDate);
            Date date2=simpleDateFormatEndDate.parse(newDate);
            Date offlineEndDate=simpleDateFormatEndDate.parse(strOfflineEndTime);
            Date fromDate=simpleDateFormat.parse(strHolidayFrom);
            Date toDate=simpleDateFormat.parse(strHolidayTo);
            Log.i("fromDate",fromDate.toString()+" toDate "+toDate.toString());
            long lonHolFrom=fromDate.getTime();
            long lonHolTo=toDate.getTime();
            long selectedDateTime=date2.getTime();
            long todayDateTimeEndTime=offlineEndDate.getTime();
            //date1.setMonth((date1.getMonth()+1));

            SimpleDateFormat sdff = new SimpleDateFormat("EEEE");
            String dayOfTheWeek = sdff.format(date1);

            if(!dayOfTheWeek.equalsIgnoreCase(strOffDay)) {

                if (!(date1.getTime() >= lonHolFrom && date1.getTime() <= lonHolTo)) {



                    if (!strTimeSlots[0].isEmpty()) {
                        Date curDate = new Date();
                        date1.setHours(0);//1591883765659 1591813800000
                        date1.setMinutes(0);
                        date1.setSeconds(0);
                        curDate.setHours(0);
                        curDate.setMinutes(0);
                        curDate.setSeconds(0);


                        if (date1.getDate() == curDate.getDate() && date1.getMonth() == curDate.getMonth() && date1.getYear() == curDate.getYear()) {
                            SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
                            String changeFTime;
                            if(selectedDateTime>todayDateTimeEndTime) {
                                return true;
                            }else
                            {
                                showFailToastForTimeCheck(nActivity, "Unable to book an appointment for selected date & time as salon might be offline or closed for the day, choose another appointment date & time to proceed");
                               return false;

                            }

                        }else
                        {
                            return true;
                        }
                    }

                } else {
                    showFailToast(nActivity, getString(R.string.salon_off));
                    return false;
                }

            }else {
                showFailToast(nActivity, getString(R.string.salon_off));
                return false;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
return false;
    }

}
