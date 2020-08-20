package com.prometteur.saloonuser.Activities;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.prometteur.saloonuser.Adapter.PointBalanceListAdapter;
import com.prometteur.saloonuser.Adapter.ReviewListAdapter;
import com.prometteur.saloonuser.Model.PointBalanceBean;
import com.prometteur.saloonuser.Model.ReviewDetailsBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.databinding.ActivityPointBalanceBinding;
import com.prometteur.saloonuser.databinding.ActivityReferAndEarnBinding;
import com.prometteur.saloonuser.retrofit.ApiInterface;
import com.prometteur.saloonuser.retrofit.RetrofitInstance;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.prometteur.saloonuser.Utils.Utils.isNetworkAvailable;
import static com.prometteur.saloonuser.Utils.Utils.logout;
import static com.prometteur.saloonuser.Utils.Utils.setEmptyMsg;
import static com.prometteur.saloonuser.Utils.Utils.showFailToast;
import static com.prometteur.saloonuser.Utils.Utils.showNoInternetDialog;
import static com.prometteur.saloonuser.Utils.Utils.showProgress;


public class PointBalanceActivity extends AppCompatActivity {

    ActivityPointBalanceBinding activityPointBalanceBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPointBalanceBinding = ActivityPointBalanceBinding.inflate(getLayoutInflater());
        View view = activityPointBalanceBinding.getRoot();
        setContentView(view);
        setToolbar();
        activityPointBalanceBinding.ivBackArrowimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (isNetworkAvailable(PointBalanceActivity.this)) {
            getPointBalance();
        } else {
            showNoInternetDialog(PointBalanceActivity.this);
        }

    }

    public void setToolbar() {
       /* setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Your Point Balance");*/
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (Build.VERSION_CODES.P == currentapiVersion) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
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
       finish();
    }




    PointBalanceBean reviewDetailsBean;
    List<PointBalanceBean.Result> mDataList;
    private void getPointBalance() {
mDataList=new ArrayList<>();
        final ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        final Dialog progressDialog = showProgress(PointBalanceActivity.this, 0);
        progressDialog.show();

        service.getPointBalance()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PointBalanceBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(PointBalanceBean loginBeanObj) {
                        progressDialog.dismiss();
                        reviewDetailsBean = loginBeanObj;
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        showFailToast(PointBalanceActivity.this, getResources().getString(R.string.went_wrong));
                    }

                    @Override
                    public void onComplete() {
                        // Updates UI with data
                        progressDialog.dismiss();

                        if (reviewDetailsBean.getStatus() == 1) {
                            mDataList=reviewDetailsBean.getResult();
                            if(!reviewDetailsBean.getBalance().isEmpty() && !reviewDetailsBean.getBalance().equalsIgnoreCase("0")) {
                                activityPointBalanceBinding.tvBalance.setText(reviewDetailsBean.getBalance() + " Points");
                            }else
                            {
                                activityPointBalanceBinding.tvBalance.setText("XXX Points");
                            }

                            activityPointBalanceBinding.rvPointBalance.setLayoutManager(new LinearLayoutManager(PointBalanceActivity.this));
                            activityPointBalanceBinding.rvPointBalance.setAdapter(new PointBalanceListAdapter(PointBalanceActivity.this,mDataList));
                        } else if (reviewDetailsBean.getStatus() == 3) {
                            showFailToast(PointBalanceActivity.this, "" + reviewDetailsBean.getMsg());
                              logout(PointBalanceActivity.this);
                        }
                        activityPointBalanceBinding.layNoData.ivNoData.setImageResource(R.drawable.img_no_point_balance_empty);
                        setEmptyMsg(mDataList, activityPointBalanceBinding.rvPointBalance, activityPointBalanceBinding.layNoData.ivNoData);
                    }
                });
    }

}
