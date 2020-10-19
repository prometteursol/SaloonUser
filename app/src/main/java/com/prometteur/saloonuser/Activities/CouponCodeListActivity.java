package com.prometteur.saloonuser.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.prometteur.saloonuser.Adapter.CouponCodeListAdapter;
import com.prometteur.saloonuser.Model.CouponBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.NetworkChangeReceiver;
import com.prometteur.saloonuser.databinding.ActivityCouponListBinding;
import com.prometteur.saloonuser.listener.OnItemClickListener;
import com.prometteur.saloonuser.retrofit.ApiInterface;
import com.prometteur.saloonuser.retrofit.RetrofitInstance;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.prometteur.saloonuser.Constants.ConstantVariables.USERIDVAL;
import static com.prometteur.saloonuser.Utils.Utils.isNetworkAvailable;
import static com.prometteur.saloonuser.Utils.Utils.logout;
import static com.prometteur.saloonuser.Utils.Utils.setEmptyMsg;
import static com.prometteur.saloonuser.Utils.Utils.showFailToast;
import static com.prometteur.saloonuser.Utils.Utils.showNoInternetDialog;
import static com.prometteur.saloonuser.Utils.Utils.showProgress;
import static com.prometteur.saloonuser.Utils.Utils.showSuccessToast;


public class CouponCodeListActivity extends AppCompatActivity implements View.OnClickListener, OnItemClickListener {

    public static BottomSheetDialogFragment filterBottomSheet;
    ActivityCouponListBinding salonServicesBinding;
    AppCompatActivity nActivity;
    CouponBean couponBean;
    List<CouponBean.Result> mDataList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        salonServicesBinding = ActivityCouponListBinding.inflate(getLayoutInflater());
        View view = salonServicesBinding.getRoot();
        setContentView(view);
        nActivity = this;
        salonServicesBinding.ivBackArrowimg.setOnClickListener(this);
        salonServicesBinding.ivsendMessage.setOnClickListener(this);
        if (isNetworkAvailable(nActivity)) {
            getCoupons();
        } else {
            showNoInternetDialog(nActivity);
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivsendMessage:
                if (!salonServicesBinding.edtTypeMessage.getText().toString().isEmpty()) {
                    if (isNetworkAvailable(nActivity)) {
                        getValidateCoupon();
                    } else {
                        showNoInternetDialog(nActivity);
                    }

                } else {
                    salonServicesBinding.edtTypeMessage.setError("Please enter coupon code");
                }
                break;
            case R.id.ivBackArrowimg:
                finish();
                break;
            case R.id.btnBookNow:

                break;

        }
    }

    private void getCoupons() {

        final ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        final Dialog progressDialog = showProgress(nActivity, 0);
        progressDialog.show();
        service.getCoupons(USERIDVAL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CouponBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CouponBean loginBeanObj) {
                        progressDialog.dismiss();
                        couponBean = loginBeanObj;
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

                        if (couponBean.getStatus() == 1) {
                            mDataList = couponBean.getResult();
                            /*offerPageBinding.recycleOfferlist.setLayoutManager(new LinearLayoutManager(nActivity));
                            offerPageBinding.recycleOfferlist.setAdapter(new ComboListItemAdapter(nActivity,mDataList));*/
                            initAdapter();
                        } else if (couponBean.getStatus() == 3) {
                            showFailToast(nActivity, "" + couponBean.getMsg());
                              logout(nActivity);
                        }
                        setEmptyMsg(mDataList,  salonServicesBinding.recycleChatting, salonServicesBinding.layNoData.ivNoData);
                    }
                });
    }
CouponBean couponBean1;
    private void getValidateCoupon() {

        final ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        final Dialog progressDialog = showProgress(nActivity, 0);
        progressDialog.show();
        service.getValidateCoupon(USERIDVAL,salonServicesBinding.edtTypeMessage.getText().toString(),getIntent().getStringExtra("aptAmount"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CouponBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CouponBean loginBeanObj) {
                        progressDialog.dismiss();
                        couponBean1 = loginBeanObj;
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

                        if (couponBean1.getStatus() == 1) {
                            couponBean1.getResult();
                            showSuccessToast(nActivity,"Your Cashback coupon "+salonServicesBinding.edtTypeMessage.getText()+" is successfully applied");
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("couponId", couponBean1.getResult().get(0).getCouponId());
                            resultIntent.putExtra("couponCode", salonServicesBinding.edtTypeMessage.getText().toString());
                            resultIntent.putExtra("couponOffPrice", couponBean1.getResult().get(0).getCouponDiscountPrice());
                            resultIntent.putExtra("couponDesc", couponBean1.getResult().get(0).getCouponDescription());
                            setResult(122, resultIntent);
                            finish();
                        } else if (couponBean1.getStatus() == 3) {
                            showFailToast(nActivity, "" + couponBean1.getMsg());
                            logout(nActivity);
                        }else
                        {
                            showFailToast(nActivity, "" + couponBean1.getMsg());
                        }
                        //setEmptyMsg(mDataList, comboPageBinding.recycleCombolist, ivNoData);
                    }
                });
    }


    private void initAdapter() {
        salonServicesBinding.recycleChatting.setLayoutManager(new LinearLayoutManager(nActivity));
        salonServicesBinding.recycleChatting.setAdapter(new CouponCodeListAdapter(nActivity, mDataList, false, this,getIntent().getStringExtra("aptAmount")));
    }

    @Override
    public void onItemClick(CouponBean.Result code) {
        Intent resultIntent = new Intent();
// TODO Add extras or a data URI to this intent as appropriate.
        resultIntent.putExtra("couponId", code.getCouponId());
        resultIntent.putExtra("couponCode", code.getCouponCode());
        resultIntent.putExtra("couponOffPrice", code.getCouponDiscountPrice());
        resultIntent.putExtra("couponDesc", code.getCouponDescription());
        setResult(122, resultIntent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkInternet();
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
