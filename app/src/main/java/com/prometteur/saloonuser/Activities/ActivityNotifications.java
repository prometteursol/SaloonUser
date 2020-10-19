package com.prometteur.saloonuser.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.Dialog;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.prometteur.saloonuser.Adapter.NotificationsListAdapter;
import com.prometteur.saloonuser.Model.NotificationBean;
import com.prometteur.saloonuser.Model.NotificationsPojo;
import com.prometteur.saloonuser.Model.UpdateLocationBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.NetworkChangeReceiver;
import com.prometteur.saloonuser.databinding.ActivityNotificationsBinding;
import com.prometteur.saloonuser.retrofit.ApiInterface;
import com.prometteur.saloonuser.retrofit.RetrofitInstance;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.prometteur.saloonuser.Activities.ActivityHomepage.homeBean;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERIDVAL;
import static com.prometteur.saloonuser.Fragments.FragmentListSalonView.listSalonBinding;
import static com.prometteur.saloonuser.Utils.Utils.isNetworkAvailable;
import static com.prometteur.saloonuser.Utils.Utils.logout;
import static com.prometteur.saloonuser.Utils.Utils.setEmptyMsg;
import static com.prometteur.saloonuser.Utils.Utils.showFailToast;
import static com.prometteur.saloonuser.Utils.Utils.showNoInternetDialog;
import static com.prometteur.saloonuser.Utils.Utils.showProgress;
import static com.prometteur.saloonuser.Utils.Utils.showSuccessToast;

public class ActivityNotifications extends AppCompatActivity implements View.OnClickListener {

    ActivityNotificationsBinding notificationsBinding;
    Activity nActivity;
List<NotificationBean.Result> notificationsArrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notificationsBinding = ActivityNotificationsBinding.inflate(getLayoutInflater());
        View view = notificationsBinding.getRoot();
        setContentView(view);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        nActivity=this;


        notificationsBinding.ivBackArrowimg.setOnClickListener(ActivityNotifications.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivBackArrowimg:
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkInternet();
        if (isNetworkAvailable(nActivity)) {
            getNotifications();
        } else {
            showNoInternetDialog(nActivity);
        }
    }

    NotificationBean updateLocationBean;
    private void getNotifications() {

        ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        final Dialog progressDialog=showProgress(nActivity,0);
        progressDialog.show();
        service.getNotifications(USERIDVAL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NotificationBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(NotificationBean loginBeanObj) {
                        progressDialog.dismiss();
                        updateLocationBean = loginBeanObj;
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                      //  showFailToast(nActivity, getResources().getString(R.string.went_wrong));
                    }

                    @Override
                    public void onComplete() {
                        // Updates UI with data
                        progressDialog.dismiss();
                        if (updateLocationBean.getStatus() == 1) {
                            // startActivity(new Intent(nActivity, SuccessDialogActivity.class));
                            notificationsArrayList=new ArrayList<>();
                           /* notificationsArrayList.add(new NotificationsPojo("Your appointment request is confirmed.",
                                    "Your start otp is 0000",
                                    "","2 min ago"
                            ));
                            notificationsArrayList.add(new NotificationsPojo("Your service is completed. Please select",
                                    "payment type-",
                                    "Salon or online payment","2 hour ago"
                            ));*/
                            notificationsArrayList=updateLocationBean.getResult();
                            if(notificationsArrayList.size()!=0) {
                                listSalonBinding.tvUnreadNots.setText("" + notificationsArrayList.size());
                            }else
                            {
                                listSalonBinding.tvUnreadNots.setText("0");
                                listSalonBinding.tvUnreadNots.setVisibility(View.GONE);
                            }
                            notificationsBinding.recycleNotifications.setLayoutManager(new LinearLayoutManager(nActivity));
                            notificationsBinding.recycleNotifications.setAdapter(new NotificationsListAdapter(nActivity,notificationsArrayList));

                        }else if(updateLocationBean.getStatus() == 3){
                            logout(ActivityNotifications.this);
                        }else
                        {
                            listSalonBinding.tvUnreadNots.setText("0");
                            listSalonBinding.tvUnreadNots.setVisibility(View.GONE);
                           // showFailToast(nActivity,  "" + updateLocationBean.getMsg());
                        }
                        setEmptyMsg(notificationsArrayList, notificationsBinding.recycleNotifications, notificationsBinding.emptyMsg.ivNoData);
//                        cPresenter.updateCoinList(allCurrencyList);
                    }
                });


    }


    NetworkChangeReceiver receiver;
    public void checkInternet() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver(this);
        registerReceiver(receiver, filter);
    }
    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(receiver);
        } catch (Exception e) {

        }
    }
}
