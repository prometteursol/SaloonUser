package com.prometteur.saloonuser.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.prometteur.saloonuser.Adapter.ServiceSelectedAdapter;
import com.prometteur.saloonuser.Model.AppointDetailBean;
import com.prometteur.saloonuser.Model.CancelAppBean;
import com.prometteur.saloonuser.Model.CheckPenaltyBean;
import com.prometteur.saloonuser.Model.HistoryDataModel;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.RadioButtonCustomFont;
import com.prometteur.saloonuser.retrofit.ApiInterface;
import com.prometteur.saloonuser.retrofit.RetrofitInstance;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.prometteur.saloonuser.Utils.Utils.isNetworkAvailable;
import static com.prometteur.saloonuser.Utils.Utils.logout;
import static com.prometteur.saloonuser.Utils.Utils.showFailToast;
import static com.prometteur.saloonuser.Utils.Utils.showNoInternetDialog;
import static com.prometteur.saloonuser.Utils.Utils.showProgress;
import static com.prometteur.saloonuser.Utils.Utils.showSuccessToast;


public class RejectionActivity extends AppCompatActivity {


    RadioGroup rgFilter;
    Button btnSubmit;
    String aptId="0",aptStatus="";
    ImageView ivBackArrowimg;
    String cancelRejStatus="7";
 /*   OfflineStatusBean ongoingBean;
    private ArrayList<HistoryDataModel> mDataList = new ArrayList<HistoryDataModel>();*/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejection);

        setToolbar();
        btnSubmit = findViewById(R.id.btnSubmit);
        rgFilter = findViewById(R.id.rgFilter);
        ivBackArrowimg = findViewById(R.id.ivBackArrowimg);

        final String[] val = {""};
        aptId=getIntent().getStringExtra("aptId");
        aptStatus=getIntent().getStringExtra("aptStatus");
        setDataListItems();
        rgFilter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rbMonth = findViewById(checkedId);
                val[0] = rbMonth.getText().toString();
                Log.i("RadioButton", val[0]);

            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*startActivity(new Intent(RejectionActivity.this, ActivityAppointmentDetails.class));
                finish();*/
                if (!val[0].isEmpty()) {
                    if (isNetworkAvailable(RejectionActivity.this)) {
                        getCancelAppoint(val[0]);
                    } else {
                        showNoInternetDialog(RejectionActivity.this);
                    }

                } else {
                    showSuccessToast(RejectionActivity.this, "Please select reason.");
                }
            }
        });
        ivBackArrowimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void setToolbar() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // getSupportActionBar().setDisplayShowHomeEnabled(true);
        //setTitle("Rejection Reasons");
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (Build.VERSION_CODES.P == currentapiVersion) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        }

    }

    ArrayList<HistoryDataModel> mDataList = new ArrayList<HistoryDataModel>();
    private void setDataListItems() {

        mDataList = new ArrayList<HistoryDataModel>();

if(aptStatus.equalsIgnoreCase("3")) {
    cancelRejStatus="2";
    //In case of reschedule appo
    mDataList.add(new HistoryDataModel("Proposed date/time not suitable", "2017-02-11 08:00:00"));
    mDataList.add(new HistoryDataModel("Proposed operator not suitable", "2017-02-10 15:00:00"));
    mDataList.add(new HistoryDataModel("Other", "2017-02-10 14:30:00"));
}else
{
    cancelRejStatus="7";
    mDataList.add(new HistoryDataModel("Found better deal directly with Salon",               "2017-02-12 08:00:00" ));
    mDataList.add(new HistoryDataModel("Found better deal with another app",     "2017-02-12 08:00:00" ));
    mDataList.add(new HistoryDataModel("Cannot visit due to other priority", "2017-02-11 21:00:00"));
    mDataList.add(new HistoryDataModel("Booked another appointment through mooi",        "2017-02-11 18:00:00" ));
    mDataList.add(new HistoryDataModel("Other",     "2017-02-11 09:30:00"));
}
        rgFilter.removeAllViews();
        for (int i = 0; i < mDataList.size(); i++) {

            RadioButtonCustomFont radioButton = new RadioButtonCustomFont(this);
            radioButton.setText(mDataList.get(i).getMessage());
            radioButton.setId(1000 + i);
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(15, 15, 15, 15);
            radioButton.setLayoutParams(params);
            radioButton.setBackground(getResources().getDrawable(R.drawable.bg_bottom_line_rejection));
            rgFilter.addView(radioButton);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onSupportNavigateUp();
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    CancelAppBean appointmentBean;
    private void getCancelAppoint(String reason) {

        final ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        final Dialog progressDialog = showProgress(RejectionActivity.this, 0);
        progressDialog.show();
        String penalty="";
        if(getIntent().getStringExtra("penaltyAmt")!=null) {
            if (getIntent().getStringExtra("penaltyAmt").equalsIgnoreCase("0")) {
                penalty = "";
            } else {
                penalty = getIntent().getStringExtra("penaltyAmt");
            }
        }
        service.getAppointmentCancel(aptId,cancelRejStatus,reason,penalty)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CancelAppBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CancelAppBean loginBeanObj) {
                        progressDialog.dismiss();
                        appointmentBean = loginBeanObj;
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        showFailToast(RejectionActivity.this, getResources().getString(R.string.went_wrong));
                    }

                    @Override
                    public void onComplete() {
                        // Updates UI with data
                        progressDialog.dismiss();

                        if (appointmentBean.getStatus() == 1) {
showSuccessToast(RejectionActivity.this,"Appointment Cancelled");
finish();
                        } else if (appointmentBean.getStatus() == 3) {
                            showFailToast(RejectionActivity.this, "" + appointmentBean.getMsg());
                              logout(RejectionActivity.this);
                        }
                        //setEmptyMsg(mDataList, comboPageBinding.recycleCombolist, ivNoData);
                    }
                });
    }




}
