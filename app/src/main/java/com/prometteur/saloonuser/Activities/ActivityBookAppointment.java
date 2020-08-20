package com.prometteur.saloonuser.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CalendarView;
import android.widget.Toast;


import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.prometteur.saloonuser.Adapter.BookingTimeSlotAdapter;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.Preferences;
import com.prometteur.saloonuser.databinding.ActivityBookAppointmentBinding;
import com.prometteur.saloonuser.databinding.DialogAccountCreatedBinding;
import com.prometteur.saloonuser.listener.OnTimeslotItemClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.prometteur.saloonuser.Activities.ActivityHomepage.strAppDate;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.strDate;
import static com.prometteur.saloonuser.Utils.Utils.showFailToast;

public class ActivityBookAppointment extends BottomSheetDialogFragment implements View.OnClickListener, OnTimeslotItemClickListener
{


    ActivityBookAppointmentBinding bookAppointmentBinding;
    AppCompatActivity nActivity;
    Dialog dialogRequestSend;
    DialogAccountCreatedBinding requestSendBinding;

    String strTime="";
    String strDate="";
    List<String> timeSlots=new ArrayList<>();
    public ActivityBookAppointment(AppCompatActivity nActivity) {
        this.nActivity =nActivity ;
    }


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
        bookAppointmentBinding.recycleTimeSlot.setAdapter(new BookingTimeSlotAdapter(nActivity,timeSlots,ActivityBookAppointment.this));
        bookAppointmentBinding.btnBookNow.setOnClickListener(this);
        bookAppointmentBinding.ivCloseCrossimg.setOnClickListener(this);
        final String strHolidayFrom= Preferences.getPreferenceValue(nActivity,"holidayFrom");
        final String strHolidayTo= Preferences.getPreferenceValue(nActivity,"holidayTo");
        String strOffDay=Preferences.getPreferenceValue(nActivity,"offDay");
        bookAppointmentBinding.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                long date= calendarView.getDate();
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("E, dd MMM YYYY,");
                strDate=simpleDateFormat.format(date);

                SimpleDateFormat simpleDateFormat1=new SimpleDateFormat("yyyy-MM-dd");

                try {

                    //Date date1=simpleDateFormat.parse(newDate);
                    Date fromDate=simpleDateFormat.parse(strHolidayFrom);
                    Date toDate=simpleDateFormat.parse(strHolidayTo);
                    Log.i("fromDate",fromDate.toString()+" toDate "+toDate.toString());
                    long lonHolFrom=fromDate.getTime();
                    long lonHolTo=toDate.getTime();
//                    if(!(date1.getTime()>=lonHolFrom && date1.getTime()<=lonHolTo))
//                    {
//                        strAppDate=simpleDateFormat.format(date);
//                        SimpleDateFormat simpleDateFormat2=new SimpleDateFormat("E, dd MMM yyyy, ");
//                        strDate=simpleDateFormat2.format(date);
//                        bookAppointmentBinding.tvSelectDateTitle.setVisibility(View.GONE);
//                        bookAppointmentBinding.calendarView.setVisibility(View.GONE);
//                        bookAppointmentBinding.tvSelectTimeSlotTitle.setVisibility(View.VISIBLE);
//                        bookAppointmentBinding.recycleTimeSlot.setVisibility(View.VISIBLE);
//                    }else
//                    {
//                        showFailToast(nActivity,getString(R.string.salon_off));
//                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
        bookAppointmentBinding.calendarView.setMinDate(System.currentTimeMillis() - 1000);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btnBookNow:
               // salonServicesBinding.tvDateTime.setText(""+strDate+strTime);
                showRequestSendDialog(nActivity);
                break;
            case R.id.ivCloseCrossimg:
                //nActivity.finish();
                dismiss();
                break;

        }
    }

    private void showRequestSendDialog(final Activity inActivity) {
        dialogRequestSend=new Dialog(inActivity,R.style.CustomAlertDialog);
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
                startActivity(new Intent(inActivity,ActivityAppointmentDetails.class));

            }
        });
        dialogRequestSend.show();

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
/*public class ActivityBookAppointment extends BottomSheetDialogFragment implements View.OnClickListener {

    ActivityBookAppointmentBinding bookAppointmentBinding;
    AppCompatActivity nActivity;
    Dialog dialogRequestSend;
    DialogAccountCreatedBinding requestSendBinding;
    public ActivityBookAppointment(AppCompatActivity nActivity) {
        this.nActivity = nActivity;
    }


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
      //  bookAppointmentBinding.recycleTimeSlot.setAdapter(new BookingTimeSlotAdapter(nActivity, timeSlots,));
        bookAppointmentBinding.btnBookNow.setOnClickListener(this);
        bookAppointmentBinding.ivCloseCrossimg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btnBookNow:
                showRequestSendDialog(nActivity);
                break;
            case R.id.ivCloseCrossimg:
                //nActivity.finish();
                dismiss();
                break;

        }
    }

    private void showRequestSendDialog(final Activity inActivity) {
        dialogRequestSend=new Dialog(inActivity,R.style.CustomAlertDialog);
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
                startActivity(new Intent(inActivity,ActivityAppointmentDetails.class));

            }
        });
        dialogRequestSend.show();

    }
}*/
