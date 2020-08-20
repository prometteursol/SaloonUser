package com.prometteur.saloonuser.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.prometteur.saloonuser.Adapter.ServicesTabAdapter;
import com.prometteur.saloonuser.Adapter.TopServicesAdapter;
import com.prometteur.saloonuser.Fragments.FragmentAboutSalon;
import com.prometteur.saloonuser.Fragments.FragmentSalonGallery;
import com.prometteur.saloonuser.Fragments.FragmentSalonReview;
import com.prometteur.saloonuser.Fragments.ServicesProfileFragment;
import com.prometteur.saloonuser.Model.SalonDetailBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.HeaderView;
import com.prometteur.saloonuser.Utils.Preferences;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.prometteur.saloonuser.databinding.ActivitySalonProfileBinding;
import com.prometteur.saloonuser.retrofit.ApiInterface;
import com.prometteur.saloonuser.retrofit.RetrofitInstance;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.prometteur.saloonuser.Utils.Utils.getDateShowDayDDMMMHHMM;
import static com.prometteur.saloonuser.Utils.Utils.getTimeShow24to12HR;
import static com.prometteur.saloonuser.Utils.Utils.isNetworkAvailable;
import static com.prometteur.saloonuser.Utils.Utils.logout;
import static com.prometteur.saloonuser.Utils.Utils.showFailToast;
import static com.prometteur.saloonuser.Utils.Utils.showNoInternetDialog;
import static com.prometteur.saloonuser.Utils.Utils.showProgress;

public class ActivitySalonProfile extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener, View.OnClickListener {

    ActivitySalonProfileBinding salonProfileBinding;
    AppCompatActivity nActivity;
    AppBarLayout appBarLayout;

    CollapsingToolbarLayout collapsingToolbarLayout;


    //Toolbar toolbar;
    HeaderView toolbarHeaderView;
    HeaderView floatHeaderView;
    boolean isHideToolbarView = false;
    LinearLayoutManager mLayoutManager;
    TopServicesAdapter nAdapter;
    String branchId="0",mainCat="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        salonProfileBinding = ActivitySalonProfileBinding.inflate(getLayoutInflater());
        View view = salonProfileBinding.getRoot();
        setContentView(view);
        setToolbar();
        salonProfileBinding.coLayout.setVisibility(View.GONE);
        salonProfileBinding.linBtnSec.setVisibility(View.GONE);
        salonProfileBinding.collapsingToolbar.setTitle(" ");
        toolbarHeaderView=findViewById(R.id.toolbar_header_view);
        floatHeaderView=findViewById(R.id.float_header_view);

        toolbarHeaderView.bindTo("Loading...", "Loading...", 0.0f, "0", "OPEN","0");
        floatHeaderView.bindTo("Loading...", "Loading...", 0.0f, "0", "OPEN","0");
        appBarLayout=findViewById(R.id.app_bar_layout);
        //toolbar=findViewById(R.id.toolbar);
        appBarLayout.addOnOffsetChangedListener(this);
        nActivity = this;
        branchId=getIntent().getStringExtra("branchId");
        mainCat=getIntent().getStringExtra("branchId");

        initTabs();
        int currentapiVersion = Build.VERSION.SDK_INT;
        if(Build.VERSION_CODES.P==currentapiVersion)
        {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        }

        salonProfileBinding.tvComboOffers.setOnClickListener(this);
        salonProfileBinding.btnBookNow.setOnClickListener(this);
        floatHeaderView.setOnClickListener(this);
        if (isNetworkAvailable(nActivity)) {
            getSalonDetail();
        } else {
            showNoInternetDialog(nActivity);
        }

    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;


        if (percentage >= 1f && isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.VISIBLE);

            isHideToolbarView = !isHideToolbarView;
            floatHeaderView.hideShowRating(true);
            toolbarHeaderView.hideShowBadge(true);

            Log.i("per >=1",""+percentage);
        } else if (percentage < 1f && !isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.GONE);

            isHideToolbarView = !isHideToolbarView;
            floatHeaderView.hideShowRating(false);
            toolbarHeaderView.hideShowBadge(false);

            Log.i("per <1",""+percentage);
        }
    }
    public void setToolbar()
    {
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("");


        int currentapiVersion = Build.VERSION.SDK_INT;
        if(Build.VERSION_CODES.P==currentapiVersion)
        {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        }

    }

    private void initTabs() {

        String[] tabTitles=new  String[]{"About","Gallery","Review"};
        ServicesTabAdapter servicesTabAdapter = new ServicesTabAdapter(getSupportFragmentManager());
        FragmentAboutSalon aboutFragment=new FragmentAboutSalon();
        FragmentSalonGallery galleryFragment=new FragmentSalonGallery(nActivity);
        FragmentSalonReview reviewFragment=new FragmentSalonReview(nActivity);


        servicesTabAdapter.addCompetetorTabFragments(aboutFragment,tabTitles[0]);
        servicesTabAdapter.addCompetetorTabFragments(galleryFragment,tabTitles[1]);
        servicesTabAdapter.addCompetetorTabFragments(reviewFragment,tabTitles[2]);

        salonProfileBinding.vpSalonInfo.setOffscreenPageLimit(3);
        salonProfileBinding.vpSalonInfo.setAdapter(servicesTabAdapter);
        salonProfileBinding.tabLayout.setupWithViewPager(salonProfileBinding.vpSalonInfo);
        salonProfileBinding.tabLayout.setTabMode(TabLayout.MODE_FIXED);


    }
long mLastClickTimeComboOffers=0;
long mLastClickTimeBookNow=0;
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvComboOffers:
                if (SystemClock.elapsedRealtime() - mLastClickTimeComboOffers < 2000) {
                    return;
                }
                mLastClickTimeComboOffers = SystemClock.elapsedRealtime();
                startActivity(new Intent(nActivity,ActivityComboAndOffers.class).putExtra("branchId",branchId).putExtra("mainCat",mainCat));
                break;
                case R.id.float_header_view:
                startActivity(new Intent(nActivity,ActivityHomepage.class).putExtra("branchId",branchId));
                break;

            case R.id.btnBookNow:
                if (SystemClock.elapsedRealtime() - mLastClickTimeBookNow < 2000) {
                    return;
                }
                mLastClickTimeBookNow = SystemClock.elapsedRealtime();
                startActivity(new Intent(nActivity,ActivitySalonServices.class).putExtra("branchId",branchId).putExtra("mainCat",mainCat));
                break;

        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        super.onSupportNavigateUp();
        finish();
        return true;
    }

    public static SalonDetailBean salonDetailBean;
    String status="";
    public static SalonDetailBean.Topservice topItem;
    private void getSalonDetail() {

        final ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        final Dialog progressDialog = showProgress(nActivity, 0);
        progressDialog.show();
        service.getSalonDetails(branchId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SalonDetailBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(SalonDetailBean loginBeanObj) {
                        progressDialog.dismiss();
                        salonDetailBean = loginBeanObj;
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
                        salonProfileBinding.coLayout.setVisibility(View.VISIBLE);
                        salonProfileBinding.linBtnSec.setVisibility(View.VISIBLE);
                        if (salonDetailBean.getStatus() == 1) {
                            final SalonDetailBean.Result result = salonDetailBean.getResult().get(0);
                            float rating = 0;
                            String reviews = "0";
                            if (result.getBranchRating() != null && !result.getBranchRating().toString().isEmpty()) {
                                rating = Float.parseFloat(""+result.getBranchRating());
                                reviews = result.getReviewCount();
                            }

                            Preferences.setPreferenceValue(nActivity,"workingHour",result.getBranWorkingHrs());
                            Preferences.setPreferenceValue(nActivity,"holidayFrom",result.getBranHolidayFrom());
                            Preferences.setPreferenceValue(nActivity,"holidayTo",result.getBranHolidayTo());
                            Preferences.setPreferenceValue(nActivity,"offDay",result.getBranOffDay());
                            Preferences.setPreferenceValue(nActivity,"offlineEndTime",result.getBranOpenedOn());
                            if(result.getClosed().equalsIgnoreCase("1")) {
                                status = "CLOSED";
                                salonProfileBinding.headerViewStatus.setBackground(getResources().getDrawable(R.drawable.status_rounded_red_background));
                                    salonProfileBinding.tvSalonOpenTime.setVisibility(View.GONE);
                                    salonProfileBinding.tvInfraDetails.setText("Infrastructure Details");

                            }else if (result.getBranOpenStatus().equalsIgnoreCase("0")) {
                                status = "CLOSED";
                                salonProfileBinding.headerViewStatus.setBackground(getResources().getDrawable(R.drawable.status_rounded_red_background));
                                    salonProfileBinding.tvSalonOpenTime.setText("Offline Till - " + getDateShowDayDDMMMHHMM(result.getBranOpenedOn()));


                            } else {
                                status = "OPEN";
                                salonProfileBinding.headerViewStatus.setBackground(getResources().getDrawable(R.drawable.status_rounded_green_background));
                                salonProfileBinding.cvSalonOpenTime.setVisibility(View.GONE);
                                salonProfileBinding.tvInfraDetails.setText("Infrastructure Details");
                            }
                            salonProfileBinding.headerViewStatus.setText(status);
                            String strDiscount="0";
                            if(result.getDiscount()==null)
                            {
                                strDiscount="0";
                            }else
                            {
                                strDiscount=result.getDiscount();
                            }
                            toolbarHeaderView.bindTo(result.getBranName(), result.getBranCity(), rating, reviews, status,strDiscount);
                            floatHeaderView.bindTo(result.getBranName(), result.getBranCity(), rating, reviews, status,result.getDiscount());
                            try {
                                Glide.with(ActivitySalonProfile.this).load(result.getBranImg()).into(salonProfileBinding.ivHeaderImg);
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                            mainCat=result.getBranCatMain();
                            Preferences.setPreferenceValue(nActivity, "mainCat",result.getBranCatMain());
                           // setColorList();
                            /*mLayoutManager = new LinearLayoutManager(nActivity, RecyclerView.HORIZONTAL, false);
                            salonProfileBinding.recycleServices.setLayoutManager(mLayoutManager);
                            nAdapter =new TopServicesAdapter(nActivity,result.getTopservices(), new TopServicesAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    topItem =  result.getTopservices().get(position);
                                   *//* BottomSheetDialogFragment bottomSheetDialogFragment = new ServicesProfileFragment();
                                    bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());*//*
                                }
                            });
                            salonProfileBinding.recycleServices.setAdapter(nAdapter);*/
                            if(result.getBranAc().equalsIgnoreCase("Yes"))
                            {
                                salonProfileBinding.ivAC.setImageResource(R.drawable.ic_active_ac);
                            }if(result.getBranFreeWifi().equalsIgnoreCase("Yes"))
                            {
                                salonProfileBinding.ivWIFI.setImageResource(R.drawable.ic_active_wifi);
                            }if(result.getBranTv().equalsIgnoreCase("Yes"))
                            {
                                salonProfileBinding.ivTV.setImageResource(R.drawable.ic_active_tv);
                            }if(result.getBranResParking().equalsIgnoreCase("Yes"))
                            {
                                salonProfileBinding.ivParking.setImageResource(R.drawable.ic_active_park);
                            }
                            initTabs();
                        } else if (salonDetailBean.getStatus() == 3) {
                            showFailToast(nActivity, "" + salonDetailBean.getMsg());
                              logout(nActivity);
                        }
                        //setEmptyMsg(mDataList, comboPageBinding.recycleCombolist, ivNoData);
                    }
                });
    }
}