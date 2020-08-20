package com.prometteur.saloonuser.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.prometteur.saloonuser.Adapter.ComboListItemAdapter;
import com.prometteur.saloonuser.Adapter.MainServicesAdapter;
import com.prometteur.saloonuser.Adapter.SelectOperatorBottomAdapter;
import com.prometteur.saloonuser.Adapter.TopServicesAdapter;
import com.prometteur.saloonuser.Fragments.FragmentFilterBottomSheet;
import com.prometteur.saloonuser.Fragments.FragmentFilterBottomSheetServices;
import com.prometteur.saloonuser.Model.ComboOffBean;
import com.prometteur.saloonuser.Model.SalonDetailBean;
import com.prometteur.saloonuser.Model.ServicesBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.databinding.ActivitySalonServicesBinding;
import com.prometteur.saloonuser.databinding.DialogAccountCreatedBinding;
import com.prometteur.saloonuser.retrofit.ApiInterface;
import com.prometteur.saloonuser.retrofit.RetrofitInstance;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.prometteur.saloonuser.Activities.ActivityComboAndOffers.branchId;
import static com.prometteur.saloonuser.Activities.ActivityComboAndOffers.mainCat;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.brands;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.brandsArr;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.category;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.highToLow;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.lowToHigh;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.selectedCats;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.sortBy;
import static com.prometteur.saloonuser.Utils.Utils.isNetworkAvailable;
import static com.prometteur.saloonuser.Utils.Utils.logout;
import static com.prometteur.saloonuser.Utils.Utils.setEmptyMsg;
import static com.prometteur.saloonuser.Utils.Utils.showFailToast;
import static com.prometteur.saloonuser.Utils.Utils.showNoInternetDialog;
import static com.prometteur.saloonuser.Utils.Utils.showProgress;

public class ActivitySalonServices extends AppCompatActivity implements View.OnClickListener {

    public static BottomSheetDialogFragment filterBottomSheet;
    public static ActivitySalonServicesBinding salonServicesBinding;
    public static AppCompatActivity nActivity;
    String catId = "";
    public static MainServicesAdapter mainServicesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        salonServicesBinding = ActivitySalonServicesBinding.inflate(getLayoutInflater());
        View view = salonServicesBinding.getRoot();
        setContentView(view);
        nActivity = this;
        salonServicesBinding.ivBackArrowimg.setOnClickListener(this);
        branchId = getIntent().getStringExtra("branchId");
        mainCat = getIntent().getStringExtra("mainCat");
        LinearLayoutManager layoutManager = new LinearLayoutManager(nActivity);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);

        if (isNetworkAvailable(nActivity)) {
            getServices();
        } else {
            showNoInternetDialog(nActivity);
        }

        List<String> mainServices = new ArrayList<>();
        mainServices.add("All Services");
        if(mainCat.contains("1"))
        {
            mainServices.add("Hair");
        }
        if(mainCat.contains("2"))
        {
            mainServices.add("Skin");
        }if(mainCat.contains("3"))
        {
            mainServices.add("Nails");
        }if(mainCat.contains("4"))
        {
            mainServices.add("Spa");
        }if(mainCat.contains("5"))
        {
            mainServices.add("Makeup");
        }
        boolean selectAll=false;
        if(selectedCats.size()==0)
        {
            selectAll=true;
        }
        salonServicesBinding.recycleServices.setLayoutManager(layoutManager);
        mainServicesAdapter=new MainServicesAdapter(nActivity, new MainServicesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String cats) {
                switch (cats) {
                    case "All Services":
                        catId = "";
                        break;
                    case "Hair":
                        catId = "1";
                        break;
                    case "Skin":
                        catId = "2";
                        break;
                    case "Nails":
                        catId = "3";
                        break;
                    case "Spa":
                        catId = "4";
                        break;
                    case "Makeup":
                        catId = "5";
                        break;
                }
                if (isNetworkAvailable(nActivity)) {
                    getServices();
                } else {
                    showNoInternetDialog(nActivity);
                }
            }
        }, mainServices,selectAll);
        salonServicesBinding.recycleServices.setAdapter(mainServicesAdapter);



        salonServicesBinding.tvComboOffers.setOnClickListener(this);
        salonServicesBinding.ivBackArrowimg.setOnClickListener(this);
        salonServicesBinding.btnBookNow.setOnClickListener(this);
        salonServicesBinding.ivFilterimg.setOnClickListener(this);
    }
    long mLastClickTimeComboOffers=0;
    long mLastClickTimeGoToCart=0;
    long mLastClickTimeFilter=0;
    public static FragmentFilterBottomSheetServices filterBottomSheetServices;
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBackArrowimg:
                finish();
                break;
            case R.id.ivFilterimg:
                if (SystemClock.elapsedRealtime() - mLastClickTimeFilter < 2000) {
                    return;
                }
                mLastClickTimeFilter = SystemClock.elapsedRealtime();
                filterBottomSheetServices = new FragmentFilterBottomSheetServices(nActivity);
                filterBottomSheetServices.show(getSupportFragmentManager(), filterBottomSheetServices.getTag());
                break;
            case R.id.tvComboOffers:
                if (SystemClock.elapsedRealtime() - mLastClickTimeComboOffers < 2000) {
                    return;
                }
                mLastClickTimeComboOffers = SystemClock.elapsedRealtime();
                startActivity(new Intent(nActivity, ActivityComboAndOffers.class).putExtra("branchId", branchId).putExtra("mainCat",mainCat));
                break;
            case R.id.btnBookNow:
                if (SystemClock.elapsedRealtime() - mLastClickTimeGoToCart < 2000) {
                    return;
                }
                mLastClickTimeGoToCart = SystemClock.elapsedRealtime();
                startActivity(new Intent(nActivity, CartActivity.class).putExtra("branchId", branchId).putExtra("mainCat",mainCat));
                nActivity.finish();
                break;
        }
    }

    public static ServicesBean servicesBean;
    public static List<ServicesBean.Result> mDataList;
    public static void getServices() {
        mDataList=new ArrayList<>();
        final ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        final Dialog progressDialog = showProgress(nActivity, 0);
        progressDialog.show();

        if(selectedCats.contains("0"))
        {
            category="";
        }else
        {
            category= TextUtils.join(",",selectedCats);
        }


        brands=TextUtils.join(",",brandsArr);
        service.getServices(branchId, category,lowToHigh,highToLow,brands)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ServicesBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ServicesBean loginBeanObj) {
                        progressDialog.dismiss();
                        servicesBean = loginBeanObj;
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

                        if (servicesBean.getStatus() == 1) {
                            mDataList = servicesBean.getResult();
                            salonServicesBinding.recycleServiceBottom.setLayoutManager(new LinearLayoutManager(nActivity));
                            salonServicesBinding.recycleServiceBottom.setAdapter(new SelectOperatorBottomAdapter(nActivity, mDataList, true));

                        } else if (servicesBean.getStatus() == 3) {
                            showFailToast(nActivity, "" + servicesBean.getMsg());
                              logout(nActivity);
                        }
                        salonServicesBinding.includeEmpty.ivNoData.setImageResource(R.drawable.img_no_services_empty);
                        setEmptyMsg(mDataList, salonServicesBinding.recycleServiceBottom, salonServicesBinding.includeEmpty.ivNoData);
                    }
                });
    }

  /*  Dialog dialogRequestSend;
    DialogAccountCreatedBinding requestSendBinding;
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
                startActivity(new Intent(inActivity, ActivityAppointmentDetails.class));

            }
        });
        dialogRequestSend.show();

    }*/
}
