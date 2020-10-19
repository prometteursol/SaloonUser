package com.prometteur.saloonuser.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.prometteur.saloonuser.Activities.ActivityBookAppointment;
import com.prometteur.saloonuser.Activities.PromoOfferListDialogActivity;
import com.prometteur.saloonuser.Model.AddCartBean;
import com.prometteur.saloonuser.Model.PromoOfferBean;
import com.prometteur.saloonuser.Model.RemoveCartBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.Preferences;
import com.prometteur.saloonuser.Utils.TextViewCustomFont;
import com.prometteur.saloonuser.databinding.ActivityBookAppointmentBinding;
import com.prometteur.saloonuser.databinding.DialogClearCartBinding;
import com.prometteur.saloonuser.databinding.RecycleOfferListItemBinding;
import com.prometteur.saloonuser.listener.OnTimeslotItemClickListener;
import com.prometteur.saloonuser.retrofit.ApiInterface;
import com.prometteur.saloonuser.retrofit.RetrofitInstance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

import static com.prometteur.saloonuser.Activities.ActivityComboAndOffers.branchId;
import static com.prometteur.saloonuser.Activities.ActivityComboAndOffers.mainCat;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.dateTimeOneTime;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.globalCartCount;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.homeBean;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.strAppDate;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.strDate;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.strTime;
import static com.prometteur.saloonuser.Constants.ConstantMethods.convertDateToString;
import static com.prometteur.saloonuser.Constants.ConstantMethods.getStrikeString;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERIDVAL;
import static com.prometteur.saloonuser.Utils.Utils.getTimeSlots;
import static com.prometteur.saloonuser.Utils.Utils.getValidDate;
import static com.prometteur.saloonuser.Utils.Utils.isNetworkAvailable;
import static com.prometteur.saloonuser.Utils.Utils.logout;
import static com.prometteur.saloonuser.Utils.Utils.showFailToast;
import static com.prometteur.saloonuser.Utils.Utils.showNoInternetDialog;
import static com.prometteur.saloonuser.Utils.Utils.showProgress;
import static com.prometteur.saloonuser.Utils.Utils.showSuccessToast;

public class OfferListAdapter extends RecyclerView.Adapter<OfferListAdapter.ViewHolder> {
    static AppCompatActivity nActivity;
    public static List<PromoOfferBean.Result> mDataList;
    static List<PromoOfferBean.Service> services;
    public OfferListAdapter(AppCompatActivity nActivity, List<PromoOfferBean.Result> mDataList) {
        this.nActivity = nActivity;
        this.mDataList = mDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       // View view = LayoutInflater.from(nActivity).inflate(R.layout.recycle_offer_list_item, parent, false);
        return new OfferListAdapter.ViewHolder(RecycleOfferListItemBinding.inflate(nActivity.getLayoutInflater()));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.recOff.tvComboNameTitle.setText(mDataList.get(position).getProofferName());
        holder.recOff.tvComboPriceOffered.setText("₹ " + mDataList.get(position).getProofferDiscountPrice());
        if(!mDataList.get(position).getProofferPrice().isEmpty()) {
            holder.recOff.tvOriginalPrice.setText("₹ " + mDataList.get(position).getProofferPrice());
        }else
        {
            holder.recOff.tvOriginalPrice.setVisibility(View.GONE);
        }

        holder.recOff.tvStartDate.setText(convertDateToString(mDataList.get(position).getProofferStartDate()));
        holder.recOff.tvEndDate.setText(convertDateToString(mDataList.get(position).getProofferEndDate()));

         services=mDataList.get(position).getServices();
        String opName="";
        List<String> opList=new ArrayList<>();
        boolean isOperator=false;
        for(int j=0;j<services.size();j++) {
            if(services.get(j).getOperators().size()!=0)
            {
                isOperator=true;
            }
            for (int i = 0; i < services.get(j).getOperators().size();i++){
                if (services.get(j).getOperators().get(i).getSelected().equalsIgnoreCase("selected")) {
                    opList.add( services.get(j).getOperators().get(i).getUserFName() + " " + services.get(j).getOperators().get(i).getUserLName());
                }
            }
        }
        if(opList.size()>0) {
            opName = "Op : "+ TextUtils.join(", ",opList);
            holder.recOff.tvOperator.setText(opName);
            holder.recOff.tvOperator.setTextColor(nActivity.getResources().getColor(R.color.black));
        }
        if(services.size()==1)
        {
            if(services.get(0).getBrndName()!=null)
            {
                holder.recOff.tvServiceOne.setText(services.get(0).getSrvcName()+" : "+services.get(0).getBrndName());
            }else
            {
                holder.recOff.tvServiceOne.setText(services.get(0).getSrvcName());
            }
            holder.recOff.tvServiceTwo.setVisibility(View.GONE);
        }else if(services.size()==2)
        {
            if(services.get(0).getBrndName()!=null)
            {
                holder.recOff.tvServiceOne.setText(services.get(0).getSrvcName()+" : "+services.get(0).getBrndName());
            }else
            {
                holder.recOff.tvServiceOne.setText(services.get(0).getSrvcName());
            }
            if(services.get(1).getBrndName()!=null)
            {
                holder.recOff.tvServiceTwo.setText(services.get(1).getSrvcName()+" : "+services.get(1).getBrndName());
            }else
            {
                holder.recOff.tvServiceTwo.setText(services.get(1).getSrvcName());
            }
        }else if(services.size()>2)
        {
            if(services.get(0).getBrndName()!=null)
            {
                holder.recOff.tvServiceOne.setText(services.get(0).getSrvcName()+" : "+services.get(0).getBrndName());
            }else
            {
                holder.recOff.tvServiceOne.setText(services.get(0).getSrvcName());
            }
            if(services.get(1).getBrndName()!=null)
            {
                holder.recOff.tvServiceTwo.setText(services.get(1).getSrvcName()+" : "+services.get(1).getBrndName()+" +"+(services.size()-2));
            }else
            {
                holder.recOff.tvServiceTwo.setText(services.get(1).getSrvcName()+" +"+(services.size()-2));
            }
        }
        getStrikeString(holder.recOff.tvOriginalPrice);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //nActivity.startActivity(new Intent(nActivity, PromoOfferListDialogActivity.class).putExtra("objOffer",mDataList.get(position)));
            }
        });
        if(mDataList.get(position).getInCart()==1)
        {
            holder.recOff.btnAdd.setText("- Remove");
            holder.recOff.btnAdd.setTextColor(nActivity.getResources().getColor(R.color.red));
        }
        final long[] mLastClickTime = {0};
        holder.recOff.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime[0] < 1000) {
                    return;
                }
                mLastClickTime[0] = SystemClock.elapsedRealtime();

                pos=position;
                if(holder.recOff.btnAdd.getText().toString().equalsIgnoreCase("+ Add to Cart"))
                {
                    if(getValidDate(mDataList.get(position).getProofferStartDate())) {
                        /*if(Preferences.getPreferenceValue(nActivity, "oneTimeSalonId").equalsIgnoreCase("0") || Preferences.getPreferenceValue(nActivity, "oneTimeSalonId").equalsIgnoreCase("NA"))
                        {
                            Preferences.setPreferenceValue(nActivity, "oneTimeSalonId",branchId);
                            Preferences.setPreferenceValue(nActivity, "mainCat",mainCat);
                        }*/
                        if(Preferences.getPreferenceValue(nActivity, "oneTimeSalonId").equalsIgnoreCase(branchId)) {
                        if (dateTimeOneTime) {
                            if (isNetworkAvailable(nActivity)) {
                                getPromoOffAddToCart(mDataList.get(position).getProofferId(), mDataList.get(pos).getServices(),holder.recOff.btnAdd);
                            } else {
                                showNoInternetDialog(nActivity);
                            }

                        } else {
                            tvBtn=holder.recOff.btnAdd;
                            BottomSheetDialogFragment filterBottomSheet = new ActivityBookAppointment();
                            filterBottomSheet.show(nActivity.getSupportFragmentManager(), filterBottomSheet.getTag());
                        }
                    }else
                    {
                        String firstSalonName="",secondSalonName="";
                        for(int i=0;i<homeBean.getResult().size();i++){
                            if(homeBean.getResult().get(i).getBranId().equalsIgnoreCase(Preferences.getPreferenceValue(nActivity, "oneTimeSalonId"))) {
                                firstSalonName = homeBean.getResult().get(i).getBranName();
                            }
                            if(homeBean.getResult().get(i).getBranId().equalsIgnoreCase(branchId)) {
                                secondSalonName = homeBean.getResult().get(i).getBranName();
                            }
                        }
                        tvBtn=holder.recOff.btnAdd;
                        if(Preferences.getPreferenceValue(nActivity, "oneTimeSalonId").equalsIgnoreCase("0") || Preferences.getPreferenceValue(nActivity, "oneTimeSalonId").equalsIgnoreCase("NA"))
                        {
                            BottomSheetDialogFragment filterBottomSheet = new ActivityBookAppointment();
                            filterBottomSheet.show(nActivity.getSupportFragmentManager(), filterBottomSheet.getTag());
                        }else {
                            showCartClearDialog(nActivity, "Your cart contains services from <b>" + firstSalonName + "</b>. Do you want to discard the selection and add services from <b>" + secondSalonName + "</b>?");
                        }
                        //Utils.showSuccessToast(nActivity,"Your cart contains services from "+firstSalonName+". Do you want to discard the selection and add services from "+homeResultList.get(position).getBranName()+"?");
                    }

                       /* holder.recOff.btnAdd.setText("- Remove");
                        holder.recOff.btnAdd.setTextColor(nActivity.getResources().getColor(R.color.red));*/
                    }else
                    {
                        showFailToast(nActivity,"Offer not yet started.");
                    }
                }else
                {
                    if (isNetworkAvailable(nActivity)) {
                        getRemoveCart(position,mDataList.get(position).getCartId(),holder.recOff.tvOperator);   //remove from cart
                    } else {
                        showNoInternetDialog(nActivity);
                    }

                    holder.recOff.btnAdd.setText("+ Add to Cart");
                    holder.recOff.btnAdd.setTextColor(nActivity.getResources().getColor(R.color.skyBlueDark));
                }
            }
        });
        if(isOperator) {
            holder.recOff.tvOperator.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                if (holder.recOff.tvOperator.getText().toString().equals("+ Select Operator")){
                   /* holder.tvAddRemoveOperator.setText(removeOperator);
                    holder.tvAddRemoveOperator.setTextColor(nActivity.getResources().getColor(R.color.red));*/
                    if(mDataList.get(position).getInCart()==1)
                    {
                        showSuccessToast(nActivity,"Can't be selected after adding offer into Cart");
                    }else {

                        if (getValidDate(mDataList.get(position).getProofferStartDate())) {
                            showOperatorSelectBottomDialog(nActivity, mDataList.get(position).getServices().get(0).getOperators(), holder.recOff.tvOperator, holder.recOff.tvOperator, position);
                        } else {
                            showFailToast(nActivity, "Offer not yet started.");
                        }
                    }

              /*  }
                else {

                    //holder.tvAddedOperatorName.setText("");
                    holder.recOff.tvOperator.setText("+ Select Operator");
                    holder.recOff.tvOperator.setTextColor(nActivity.getResources().getColor(R.color.skyBlueLight));

                }*/

                }
            });
        }else
        {
            holder.recOff.tvOperator.setVisibility(View.GONE);
        }
        //getStrikeString(holder.recOff.tvMainPrice);
        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nActivity.startActivity(new Intent(nActivity, PromoOfferListDialogActivity.class));
            }
        });*/


    }
    DialogClearCartBinding dialogClearCartBinding;
    Dialog dialogSelectPayment;
    private void showCartClearDialog(final Activity inActivity, String msg) {

        dialogSelectPayment=new Dialog(inActivity,R.style.CustomAlertDialog);
        dialogClearCartBinding = DialogClearCartBinding.inflate(LayoutInflater.from(inActivity));
        dialogSelectPayment.setContentView(dialogClearCartBinding.getRoot());
        Window window = dialogSelectPayment.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);
        dialogSelectPayment.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        dialogClearCartBinding.tvMsg.setText(Html.fromHtml(msg));
        dialogClearCartBinding.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable(nActivity)) {
                    getRemoveAllCart();
                } else {
                    showNoInternetDialog(nActivity);
                }
                dialogSelectPayment.dismiss();
            }
        });dialogClearCartBinding.btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogSelectPayment.dismiss();
            }
        });
        dialogSelectPayment.show();
    }
    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RecycleOfferListItemBinding recOff;
        public ViewHolder(@NonNull RecycleOfferListItemBinding itemView) {
            super(itemView.getRoot());
            recOff=itemView;
        }
    }

    //bottom dialog widgets
    RecyclerView recycleOperatorsList;
    TextViewCustomFont tvOperatortitle;
    ImageView ivCloseCrossimg;
    Button btnDone;
    String operatorName;
    public void showOperatorSelectBottomDialog(final Activity nActivity, final List<PromoOfferBean.Operator> operators, final TextViewCustomFont tvOperatorName, final TextViewCustomFont tvAddRemoveOperator, final int pos) {
        final Dialog operatorSelect = new Dialog(nActivity, R.style.CustomAlertDialog);
        operatorSelect.setContentView(R.layout.dialog_select_operator);
        Window window = operatorSelect.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);
        operatorSelect.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        recycleOperatorsList = operatorSelect.findViewById(R.id.recycleOperatorsList);
        tvOperatortitle = operatorSelect.findViewById(R.id.tvOperatortitle);
        ivCloseCrossimg = operatorSelect.findViewById(R.id.ivCloseCrossimg);
        btnDone = operatorSelect.findViewById(R.id.btnDone);

        LinearLayoutManager layoutManager = new LinearLayoutManager(nActivity);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recycleOperatorsList.setLayoutManager(layoutManager);
        recycleOperatorsList.setAdapter(new OperatorsListProOffAdapter(nActivity, operators, new OperatorsListProOffAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, String OperatorName) {

                if(mDataList.get(pos).getServices().get(0).getOperators().get(position).getSelected().equalsIgnoreCase("selected"))
                {
                    mDataList.get(pos).getServices().get(0).getOperators().get(position).setSelected("");
                    operatorName=null;
                }else {
                    operatorName=OperatorName;
                    mDataList.get(pos).getServices().get(0).getOperators().get(position).setSelected("selected");
                }
                //constraintLayout.setBackground(nActivity.getResources().getDrawable(R.drawable.bg_white_blue_stroke_curved));
            }
        }));
        ivCloseCrossimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (operatorName != null){
                if(!operatorName.isEmpty()) {
                    tvOperatorName.setText("Op : " + operatorName);
                    tvAddRemoveOperator.setTextColor(nActivity.getResources().getColor(R.color.black));
                }else {
                    tvOperatorName.setText("+ Select Operator");
                    tvAddRemoveOperator.setTextColor(nActivity.getResources().getColor(R.color.skyBlueDark));
                }
                }else{
                    tvAddRemoveOperator.setText("+ Select Operator");
                    tvAddRemoveOperator.setTextColor(nActivity.getResources().getColor(R.color.skyBlueDark));
                }
                operatorSelect.dismiss();
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (operatorName != null){
                    if(!operatorName.isEmpty()) {
                        tvOperatorName.setText("Op : " + operatorName);
                        tvAddRemoveOperator.setTextColor(nActivity.getResources().getColor(R.color.black));
                    }else {
                        tvOperatorName.setText("+ Select Operator");
                        tvAddRemoveOperator.setTextColor(nActivity.getResources().getColor(R.color.skyBlueDark));
                    }
                    notifyDataSetChanged();
                    operatorSelect.dismiss();
                }else{
                    tvAddRemoveOperator.setText("+ Select Operator");
                    tvAddRemoveOperator.setTextColor(nActivity.getResources().getColor(R.color.skyBlueDark));
                    //Toast.makeText(nActivity, "Please select operator", Toast.LENGTH_SHORT).show();
                    operatorSelect.dismiss();
                }

            }
        });
        operatorSelect.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                if (operatorName != null) {
                    if(!operatorName.isEmpty()) {
                        tvOperatorName.setText("Op : " + operatorName);
                        tvAddRemoveOperator.setTextColor(nActivity.getResources().getColor(R.color.black));
                    }else {
                        tvOperatorName.setText("+ Select Operator");
                        tvAddRemoveOperator.setTextColor(nActivity.getResources().getColor(R.color.skyBlueDark));
                    }
                }else {

                    tvAddRemoveOperator.setText("+ Select Operator");
                    tvAddRemoveOperator.setTextColor(nActivity.getResources().getColor(R.color.skyBlueDark));
                }
               // notifyDataSetChanged();
            }

        });
        operatorSelect.show();
    }


    static AddCartBean addCartBean;
    static TextViewCustomFont tvBtn;
    private static void getPromoOffAddToCart(String promoOffId, List<PromoOfferBean.Service> services, final TextViewCustomFont btnAdd) {

        final ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        final Dialog progressDialog = showProgress(nActivity, 0);
        progressDialog.show();
        JSONArray jsonArray=new JSONArray();
        for(int i=0;i<services.size();i++)
        {
            JSONObject jsonObject=new JSONObject();

            try {
                jsonObject.put("service_id",services.get(i).getSrvcId());
                for(int j=0;j<services.get(i).getOperators().size();j++) {
                    if(services.get(i).getOperators().get(j).getSelected()!=null) {
                        if (services.get(i).getOperators().get(j).getSelected().equalsIgnoreCase("selected")) {
                            jsonObject.put("operator_id", services.get(i).getOperators().get(j).getUserId());
                        }
                    }
                }
                if(jsonObject.length()==1) {
                    jsonObject.put("operator_id", "");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
        }

        service.getAddCart(USERIDVAL,"promotional_offer",branchId,promoOffId,jsonArray.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AddCartBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(AddCartBean loginBeanObj) {
                        progressDialog.dismiss();
                        addCartBean = loginBeanObj;
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        //showFailToast(nActivity, nActivity.getResources().getString(R.string.went_wrong));
                    }

                    @Override
                    public void onComplete() {
                        // Updates UI with data
                        progressDialog.dismiss();

                        if (addCartBean.getStatus() == 1) {
                            globalCartCount++;
                            btnAdd.setText("- Remove");
                            btnAdd.setTextColor(nActivity.getResources().getColor(R.color.red));
                            mDataList.get(pos).setCartId(""+addCartBean.getResult().get(0));
                            mDataList.get(pos).setInCart(1);
                        } else if (addCartBean.getStatus() == 3) {
                            showFailToast(nActivity,  "" + addCartBean.getMsg());
                              logout(nActivity);
                        }
                        //setEmptyMsg(mDataList, comboPageBinding.recycleCombolist, ivNoData);
                    }
                });
    }



    private static List<String> timeSlots = new ArrayList<>();
    static int pos=0;
    public static class ActivityBookAppointment extends BottomSheetDialogFragment implements View.OnClickListener, OnTimeslotItemClickListener
    {


        ActivityBookAppointmentBinding bookAppointmentBinding;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            bookAppointmentBinding = ActivityBookAppointmentBinding.inflate(inflater,container,false);

            return bookAppointmentBinding.getRoot();
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            GridLayoutManager gLayoutManager=new GridLayoutManager(nActivity,4);
            bookAppointmentBinding.recycleTimeSlot.setLayoutManager(gLayoutManager);
            final String[] strTimeSlots = {Preferences.getPreferenceValue(nActivity, "workingHour")};
            final String strHolidayFrom= Preferences.getPreferenceValue(nActivity,"holidayFrom");
            final String strHolidayTo= Preferences.getPreferenceValue(nActivity,"holidayTo");
            final String strOffDay=Preferences.getPreferenceValue(nActivity,"offDay");
            final String strOfflineEndTime=Preferences.getPreferenceValue(nActivity,"offlineEndTime");
           /* if (!strTimeSlots.isEmpty()) {
                timeSlots = getTimeSlots(strTimeSlots);
            }
            bookAppointmentBinding.recycleTimeSlot.setAdapter(new BookingTimeSlotAdapter(nActivity,timeSlots, ActivityBookAppointment.this));*/
            getActivity().getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
           bookAppointmentBinding.btnBookNow.setOnClickListener(this);
            bookAppointmentBinding.ivCloseCrossimg.setOnClickListener(this);
            bookAppointmentBinding.btnBookNow.setVisibility(View.GONE);
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
                                strTimeSlots[0]=Preferences.getPreferenceValue(nActivity, "workingHour");
                                strTime="";
                                if (!strTimeSlots[0].isEmpty()) {

                                    //for today
                                    final SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
                                    final Date dateStartWithDate= new Date();
                                    Date startTimeDate = null;
                                    try {
                                        startTimeDate = sdf1.parse(strTimeSlots[0].split("-")[0]);
                                        dateStartWithDate.setHours(startTimeDate.getHours());
                                        dateStartWithDate.setMinutes(startTimeDate.getMinutes());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    //for today end

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

                                    String changeFTime;
                                    if (date1.getDate() == curDate.getDate() && date1.getMonth() == curDate.getMonth() && date1.getYear() == curDate.getYear()) {

                                        final long currentDateTime=new Date().getTime();
                                        if(currentDateTime<dateStartWithDate.getTime())
                                        {
                                            Date date2 = null;
                                            try {
                                                date2 = sdf1.parse(strTimeSlots[0].split("-")[0]);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            Date date3 = new Date();
                                            date3.setTime(date2.getTime());  //add 1 hour in next day
                                            changeFTime = sdf1.format(date3.getTime()+ 3600000);

                                            strTimeSlots[0] = changeFTime + "-" + strTimeSlots[0].split("-")[1];
                                        }else
                                        if(currentDateTime>todayDateTimeEndTime) {
                                            changeFTime = sdf1.format(currentDateTime);
                                        }else
                                        {
                                            Date date2=new Date();
                                            date2.setTime(todayDateTimeEndTime);
                                            changeFTime = sdf1.format(date2.getTime());
                                        }
                                        strTimeSlots[0] = changeFTime + "-" + strTimeSlots[0].split("-")[1];
                                        final Date dateEndWithDate= new Date();
                                        Date dateEnd= null;
                                        try {
                                            dateEnd = sdf1.parse(strTimeSlots[0].split("-")[1]);
                                            dateEndWithDate.setHours(dateEnd.getHours());
                                            dateEndWithDate.setMinutes(dateEnd.getMinutes());
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                       /* final Date dateStartWithDate= new Date();
                                        Date startTimeDate = null;
                                        try {
                                            startTimeDate = sdf1.parse(strTimeSlots[0].split("-")[0]);
                                            dateStartWithDate.setHours(startTimeDate.getHours());
                                            dateStartWithDate.setMinutes(startTimeDate.getMinutes());
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }*/

                                        if(currentDateTime<(dateEndWithDate.getTime()-900000)){
                                            Log.i("todays","in between start and end");
                                            bookAppointmentBinding.tvSelectDateTitle.setVisibility(View.GONE);
                                            bookAppointmentBinding.calendarView.setVisibility(View.GONE);
                                            bookAppointmentBinding.tvSelectTimeSlotTitle.setVisibility(View.VISIBLE);
                                            bookAppointmentBinding.recycleTimeSlot.setVisibility(View.VISIBLE);
                                            bookAppointmentBinding.btnBookNow.setVisibility(View.VISIBLE);

                                            timeSlots = getTimeSlots(strTimeSlots[0]);
                                            bookAppointmentBinding.recycleTimeSlot.setAdapter(new BookingTimeSlotAdapter(nActivity, timeSlots, ActivityBookAppointment.this));
                                        }else
                                        {
                                            Log.i("todays","closed");
                                            showFailToast(nActivity, getString(R.string.salon_off_time));
                                        }

                                    }else  {
                                        String strWeekDay="";
                                        boolean isOneHrNextFlag=false;
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
                                            isOneHrNextFlag=true;
                                        }else if(dayOfTheWeek.equalsIgnoreCase(strWeekDay)) {
                                            nextDate.setTime(date1.getTime());
                                            isOneHrNextFlag=true;
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
                                            bookAppointmentBinding.tvSelectDateTitle.setVisibility(View.GONE);
                                            bookAppointmentBinding.calendarView.setVisibility(View.GONE);
                                            bookAppointmentBinding.tvSelectTimeSlotTitle.setVisibility(View.VISIBLE);
                                            bookAppointmentBinding.recycleTimeSlot.setVisibility(View.VISIBLE);
                                            bookAppointmentBinding.btnBookNow.setVisibility(View.VISIBLE);
                                            if(!(new Date().getTime()<dateEndWithDate.getTime()-900000)) {//when todays time is closed


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
                                            }else
                                            {
                                                if(isOneHrNextFlag)
                                                {
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
                                            }
                                            timeSlots = getTimeSlots(strTimeSlots[0]);
                                            bookAppointmentBinding.recycleTimeSlot.setAdapter(new BookingTimeSlotAdapter(nActivity, timeSlots, ActivityBookAppointment.this));
                                        } else {
                                            bookAppointmentBinding.tvSelectDateTitle.setVisibility(View.GONE);
                                            bookAppointmentBinding.calendarView.setVisibility(View.GONE);
                                            bookAppointmentBinding.tvSelectTimeSlotTitle.setVisibility(View.VISIBLE);
                                            bookAppointmentBinding.recycleTimeSlot.setVisibility(View.VISIBLE);
                                            bookAppointmentBinding.btnBookNow.setVisibility(View.VISIBLE);
                                            timeSlots = getTimeSlots(strTimeSlots[0]);
                                            bookAppointmentBinding.recycleTimeSlot.setAdapter(new BookingTimeSlotAdapter(nActivity, timeSlots, ActivityBookAppointment.this));
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

            bookAppointmentBinding.calendarView.setMinDate(System.currentTimeMillis() - 1000);
            bookAppointmentBinding.calendarView.setSystemUiVisibility(nActivity.getResources().getColor(R.color.skyBluelilDark));
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){

                case R.id.btnBookNow:
                    if(!strTime.isEmpty()) {
                    // salonServicesBinding.tvDateTime.setText(""+strDate+strTime);
                    Preferences.setPreferenceValue(nActivity,"dateTime",strDate+strTime);
                    Preferences.setPreferenceValue(nActivity,"appDate",strAppDate);
                    Preferences.setPreferenceValue(nActivity,"appTime",strTime);

                    dateTimeOneTime=true;
                    Preferences.setPreferenceValue(nActivity, "dateTimeOneTime","true");
                        Preferences.setPreferenceValue(nActivity, "oneTimeSalonId",branchId);
                        Preferences.setPreferenceValue(nActivity, "mainCat",mainCat);
                        if (isNetworkAvailable(nActivity)) {
                            getPromoOffAddToCart(mDataList.get(pos).getProofferId(), mDataList.get(pos).getServices(),tvBtn);
                        } else {
                            showNoInternetDialog(nActivity);
                        }

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


        @Override
        public void onCancel(DialogInterface dialog)
        {
            super.onCancel(dialog);
            handleUserExit();
        }



        private void handleUserExit()
        {
           // Toast.makeText(nActivity, "TODO - SAVE data or similar", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onItemClick(String item) {
            strTime=item;
            Preferences.setPreferenceValue(nActivity,"time",item);
        }

        @Override
        public void onOperatorClick(String opId) {

        }
    }


    RemoveCartBean cartDetailBean;
    private void getRemoveCart(final int pos, String cartId, final TextViewCustomFont tvAddRemoveOperator) {

        final ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        final Dialog progressDialog = showProgress(nActivity, 0);
        progressDialog.show();
        service.getRemoveCart(cartId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RemoveCartBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(RemoveCartBean loginBeanObj) {
                        progressDialog.dismiss();
                        cartDetailBean = loginBeanObj;

                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                       // showFailToast(nActivity, nActivity.getResources().getString(R.string.went_wrong));
                    }

                    @Override
                    public void onComplete() {
                        // Updates UI with data
                        progressDialog.dismiss();

                        if (cartDetailBean.getStatus() == 1) {
                            //results.remove(pos);
                            //notifyDataSetChanged();
                            Preferences.setPreferenceValue(nActivity, "couponId", "0");
                            Preferences.setPreferenceValue(nActivity, "couponCode", "");
                            Preferences.setPreferenceValue(nActivity, "couponDesc", "");
                            Preferences.setPreferenceValue(nActivity, "couponOffPrice", "0");
                            mDataList.get(pos).setInCart(0);
                            if(globalCartCount==1){
                                dateTimeOneTime=false;
                                Preferences.setPreferenceValue(nActivity, "dateTimeOneTime","false");
                                Preferences.setPreferenceValue(nActivity, "oneTimeSalonId","0");
                                strTime="";
                                strDate="";strAppDate="";
                                Preferences.setPreferenceValue(nActivity, "dateTime","");

                            }
                            tvAddRemoveOperator.setText("+ Select Operator");
                            tvAddRemoveOperator.setTextColor(nActivity.getResources().getColor(R.color.skyBlueDark));
                            globalCartCount--;
                            showSuccessToast(nActivity, "" + cartDetailBean.getMsg());
                        } else if (cartDetailBean.getStatus() == 3) {
                            showFailToast(nActivity, "" + cartDetailBean.getMsg());
                            logout(nActivity);
                        }
                        //setEmptyMsg(mDataList, comboPageBinding.recycleCombolist, ivNoData);
                    }
                });
    }

    RemoveCartBean removeAllCart;
    private void getRemoveAllCart() {

        final ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        final Dialog progressDialog = showProgress(nActivity, 0);
        progressDialog.show();
        service.getRemoveAllCart(USERIDVAL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RemoveCartBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(RemoveCartBean loginBeanObj) {
                        progressDialog.dismiss();
                        removeAllCart = loginBeanObj;

                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                      //  showFailToast(nActivity, nActivity.getResources().getString(R.string.went_wrong));
                    }

                    @Override
                    public void onComplete() {
                        // Updates UI with data
                        progressDialog.dismiss();

                        if (removeAllCart.getStatus() == 1) {

                            globalCartCount=0;

                            dateTimeOneTime=false;
                            Preferences.setPreferenceValue(nActivity, "dateTimeOneTime","false");
                            Preferences.setPreferenceValue(nActivity, "oneTimeSalonId","0");
                            strTime="";
                            strDate="";strAppDate="";
                            Preferences.setPreferenceValue(nActivity, "dateTime","");
                            Preferences.setPreferenceValue(nActivity,"couponCode","");
                            Preferences.setPreferenceValue(nActivity,"couponDesc","");
                            Preferences.setPreferenceValue(nActivity,"couponOffPrice","0");
                            showSuccessToast(nActivity, "" + removeAllCart.getMsg());
                           // Preferences.setPreferenceValue(nActivity, "oneTimeSalonId",branchId);
                            BottomSheetDialogFragment filterBottomSheet = new ActivityBookAppointment();
                            filterBottomSheet.show(nActivity.getSupportFragmentManager(), filterBottomSheet.getTag());
                        } else if (removeAllCart.getStatus() == 3) {
                            showFailToast(nActivity, "" + removeAllCart.getMsg());
                            logout(nActivity);
                        }
                        //setEmptyMsg(mDataList, comboPageBinding.recycleCombolist, ivNoData);
                    }
                });
    }
}