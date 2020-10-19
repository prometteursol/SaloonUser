package com.prometteur.saloonuser.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.prometteur.saloonuser.Activities.ActivityAccountsSettings;
import com.prometteur.saloonuser.Activities.ActivityAppointmentDetails;
import com.prometteur.saloonuser.Activities.ActivityHomepage;
import com.prometteur.saloonuser.Activities.ActivityNotifications;
import com.prometteur.saloonuser.Activities.ActivitySalonProfile;
import com.prometteur.saloonuser.Activities.ActivitySearchLocation;
import com.prometteur.saloonuser.Activities.ActivitySearchSalons;
import com.prometteur.saloonuser.Activities.ActivityUpdateProfile;
import com.prometteur.saloonuser.Activities.CartActivity;
import com.prometteur.saloonuser.Activities.SuccessDialogActivity;
import com.prometteur.saloonuser.Adapter.SalonCategoriesAdapter;
import com.prometteur.saloonuser.Adapter.SalonListAdapter;
import com.prometteur.saloonuser.Adapter.SliderAdapterExample;
import com.prometteur.saloonuser.Adapter.ViewPagerSliderAdapter;
import com.prometteur.saloonuser.Model.CouponBean;
import com.prometteur.saloonuser.Model.HomeBean;
import com.prometteur.saloonuser.Model.SliderItem;
import com.prometteur.saloonuser.Model.UpdateLocationBean;
import com.prometteur.saloonuser.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.prometteur.saloonuser.Utils.NetworkChangeReceiver;
import com.prometteur.saloonuser.Utils.Preferences;
import com.prometteur.saloonuser.databinding.FragmentListsalonViewBinding;
import com.prometteur.saloonuser.listener.OnItemClickListener;
import com.prometteur.saloonuser.retrofit.ApiInterface;
import com.prometteur.saloonuser.retrofit.RetrofitInstance;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.prometteur.saloonuser.Activities.ActivityComboAndOffers.branchId;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.brands;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.brandsArr;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.category;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.dateTimeOneTime;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.discount;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.gender;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.globalCartCount;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.highToLow;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.homeBean;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.isFilter;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.loadHomeFirstTime;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.lowToHigh;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.menuPos;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.rating;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.selectedCats;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.sortBy;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.strAppDate;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.strDate;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.strTime;
import static com.prometteur.saloonuser.Constants.ConstantVariables.REDEEMPOINTSKEY;
import static com.prometteur.saloonuser.Constants.ConstantVariables.REFERERPOINTKEY;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERCITY;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERCITYKEY;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERFNAME;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERIDVAL;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERLNAME;
import static com.prometteur.saloonuser.Constants.ConstantVariables.colorList;
import static com.prometteur.saloonuser.Constants.ConstantVariables.fromPayment;
import static com.prometteur.saloonuser.Utils.Utils.enableLocation;
import static com.prometteur.saloonuser.Utils.Utils.isNetworkAvailable;
import static com.prometteur.saloonuser.Utils.Utils.logout;
import static com.prometteur.saloonuser.Utils.Utils.setEmptyMsg;
import static com.prometteur.saloonuser.Utils.Utils.showFailToast;
import static com.prometteur.saloonuser.Utils.Utils.showNoInternetDialog;
import static com.prometteur.saloonuser.Utils.Utils.showProgress;
import static com.prometteur.saloonuser.Utils.Utils.showSuccessToast;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentListSalonView extends Fragment implements View.OnClickListener {

    static AppCompatActivity nActivity;
    private static final String TAG = "MapfragmentSalonView";

    //widgets


    ArrayList<String> salonCategories;
    public static FragmentListsalonViewBinding listSalonBinding;
    int i=1;
    View view;

    public static SalonCategoriesAdapter catAdapter;


    public static BottomSheetDialogFragment filterBottomSheet;



    public FragmentListSalonView() {
        // Required empty public constructor
    }

    public FragmentListSalonView(AppCompatActivity nActivity) {
        this.nActivity = nActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       /* if(view!=null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);

        }*/
        listSalonBinding = FragmentListsalonViewBinding.inflate(inflater, container, false);
        view = listSalonBinding.getRoot();

        /*listSalonBinding.recycleListsaloonView=view.findViewById(R.id.recycle_listsaloon_view);
        listSalonBinding.recycleSalonCategories=view.findViewById(R.id.recycle_SalonCategories);
        slider=view.findViewById(R.id.slider);*/




        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listSalonBinding.pullToRefresh.setNestedScrollingEnabled(false);
        listSalonBinding.pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to make your refresh action
                getLocationPermission();
                if(listSalonBinding.pullToRefresh.isRefreshing()) {
                    listSalonBinding.pullToRefresh.setRefreshing(false);
                }
            }
        });
        getLocationPermission();
        if (Loc_permissiongranted) {

            if (ActivityCompat.checkSelfPermission(nActivity, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                    (nActivity, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                //return view;
            }
            getDeviceLocation();
        }
        salonCategories = new ArrayList<>();
        salonCategories.add(" All \nCategories ");
        salonCategories.add("Hair");
        salonCategories.add("Skin");
        salonCategories.add("Nail");
        salonCategories.add("Spa");
        salonCategories.add("Makeup");
        LinearLayoutManager layoutManager = new LinearLayoutManager(nActivity);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        if(!USERFNAME.isEmpty() && !USERLNAME.isEmpty()) {
            listSalonBinding.tvUserName.setText("Hello, " + USERFNAME + " " + USERLNAME);
        }
        boolean selectAll=false;
        if(selectedCats.size()==0)
        {
            selectAll=true;
        }
        catAdapter= new SalonCategoriesAdapter(nActivity, salonCategories, colorList, new OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                //brands="";rating="";discount="";sortBy="";brandsArr=new ArrayList<>();gender="";
                if (isNetworkAvailable(nActivity)) {
                    getHomeData();
                } else {
                    showNoInternetDialog(nActivity);
                }
            }
        },selectAll);
        listSalonBinding.recycleSalonCategories.setLayoutManager(layoutManager);
        listSalonBinding.recycleSalonCategories.setAdapter(catAdapter);
        //listSalonBinding.slider.setPageMargin(-(int) getResources().getDimension(R.dimen._35sdp));


        listSalonBinding.ivFilterimg.setOnClickListener(this);
        listSalonBinding.ivNotification.setOnClickListener(this);
        listSalonBinding.ivSearchimg.setOnClickListener(this);
        listSalonBinding.tvUserAddress.setOnClickListener(this);
        listSalonBinding.tvUserAddressTemp.setOnClickListener(this);
        listSalonBinding.tvUserName.setOnClickListener(this);
        listSalonBinding.frameCart.setOnClickListener(this);



    }

    private static class SliderTimer extends TimerTask {

        @Override
        public void run() {
            nActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(homeBean.getAdvertisement()!=null) {
                        /*if (listSalonBinding.slider.getCurrentItem() < homeBean.getAdvertisement().size() - 1) {
                            listSalonBinding.slider.setCurrentItem(listSalonBinding.slider.getCurrentItem() + 1);
                        } else {
                            listSalonBinding.slider.setCurrentItem(0);
                        }*/
                    }
                }
            });
        }
    }
    @Override
    public void onResume() {
        super.onResume();

        if(isFilter)
        {
            listSalonBinding.ivFilterimg.setImageResource(R.drawable.ic_filter_filled);
        }else
        {
            listSalonBinding.ivFilterimg.setImageResource(R.drawable.ic_filter_icon);
        }
        SliderAdapterExample adapter = new SliderAdapterExample(nActivity);
            SliderItem sliderItem=new SliderItem();
            sliderItem.setImageUrl("");
            sliderItem.setDescription("https://mooi.app/");
            adapter.addItem(sliderItem);

        listSalonBinding.sliderView.setSliderAdapter(adapter);
        if(homeBean!=null) {

             homeResultList = homeBean.getResult();
            listSalonBinding.tvPopularSalon.setText("Popular Salon Nearby ("+homeResultList.size()+")");
            listSalonBinding.tvUserName.setText("Hello, "+USERFNAME+" "+USERLNAME);
            if(!USERCITY.isEmpty() || !USERCITY.equalsIgnoreCase("NA")) {
                listSalonBinding.tvUserAddress.setText(""+USERCITY+" >");
            }else
            {
                listSalonBinding.tvUserAddress.setText("Select Location >");
            }

            listSalonBinding.recycleListsaloonView.setLayoutManager(new LinearLayoutManager(nActivity));
            listSalonBinding.recycleListsaloonView.setAdapter(new SalonListAdapter(nActivity, false, homeResultList));/*, new SalonListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                startActivity(new Intent(nActivity, ActivitySalonProfile.class));
            }
        }));*/
            if(homeBean!=null) {
                try {
                    if (homeBean.getCart() != 0) {
                        listSalonBinding.tvUnreadCart.setText("" + homeBean.getCart());
                        globalCartCount = homeBean.getCart();
                    } else {
                        listSalonBinding.tvUnreadCart.setText("0");
                        listSalonBinding.tvUnreadCart.setVisibility(View.GONE);
                    }
                    if (homeBean.getNotifications() != null && homeBean.getNotifications() !=0) {
                        listSalonBinding.tvUnreadNots.setText("" + homeBean.getNotifications());
                    } else {
                        listSalonBinding.tvUnreadNots.setText("0");
                        listSalonBinding.tvUnreadNots.setVisibility(View.GONE);
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            listSalonBinding.layNoData.ivNoData.setImageResource(R.drawable.img_list_is_empty);
            setEmptyMsg(homeResultList, listSalonBinding.recycleListsaloonView, listSalonBinding.layNoData.ivNoData);
            try {


                if(homeBean!=null) {
                    // listSalonBinding.slider.setAdapter(new ViewPagerSliderAdapter(nActivity, false, homeBean.getAdvertisement()));
                    Timer timer = new Timer();
                    timer.scheduleAtFixedRate(new SliderTimer(), 3000, 2000);
                    //    setPageNuber(homeBean.getAdvertisement().size(),i);


                    SliderAdapterExample adapter1 = new SliderAdapterExample(nActivity);

                    for(int i=0;i<homeBean.getAdvertisement().size();i++)
                    {
                        SliderItem sliderItem1=new SliderItem();
                        sliderItem1.setImageUrl(homeBean.getAdvertisement().get(i).getAdvImg());
                        sliderItem1.setDescription(homeBean.getAdvertisement().get(i).getAdvUrl());
                        adapter1.addItem(sliderItem1);
                    }
                    listSalonBinding.sliderView.setSliderAdapter(adapter1);

                    listSalonBinding.sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                    listSalonBinding.sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                    listSalonBinding.sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_LEFT);
                    listSalonBinding.sliderView.setIndicatorSelectedColor(Color.TRANSPARENT);
                    listSalonBinding.sliderView.setIndicatorUnselectedColor(Color.TRANSPARENT);
                    listSalonBinding.sliderView.setIndicatorEnabled(false);
                    listSalonBinding.sliderView.setAutoCycle(true);
                    listSalonBinding.sliderView.setScrollTimeInSec(5); //set scroll delay in seconds :
                    listSalonBinding.sliderView.startAutoCycle();
                }

           /* listSalonBinding.swipeRefresh.setOnRefreshListener(
                    new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            onResume();

                        }
                    }
            );
*/
            }catch (Exception e)
            {e.printStackTrace();}
        }

    }
    SliderHandler sliderHandler=null;
    Handler handler=new Handler();
    public void setPageNuber(final int insize, int i) {
        if (insize == i) {
            i = 0;
        }

       // listSalonBinding.slider.setCurrentItem(i, true);
//    slider.setCurrentItem(i);
        //slider.setAnimation(new ());
        i++;
        final int finalI = i;

        if(!handler.hasCallbacks(sliderHandler)) {

            sliderHandler = new SliderHandler(insize, finalI);

            handler.postDelayed(sliderHandler, 5000);
        }else
        {
            handler.removeCallbacks(sliderHandler);
        }

    }

    private class SliderHandler implements Runnable
    {
        int insize;int finalI;
        public SliderHandler(int insize,int finalI)
        {
            this.insize=insize;
            this.finalI=finalI;
        }
        @Override
        public void run() {
            setPageNuber(insize, finalI);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        handler.removeCallbacks(sliderHandler);
    }
    private long mLastClickTimeFilter = 0;
    long mLastClickTimeNoti = 0;
     long mLastClickTimeSearch = 0;
     long mLastClickTimeCart = 0;
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.ivFilterimg:
                if (SystemClock.elapsedRealtime() - mLastClickTimeFilter < 2000) {
                    return;
                }
                mLastClickTimeFilter = SystemClock.elapsedRealtime();
                filterBottomSheet = new FragmentFilterBottomSheet(nActivity);
                filterBottomSheet.show(getActivity().getSupportFragmentManager(), filterBottomSheet.getTag());
                break;

            case R.id.ivNotification:
                if (SystemClock.elapsedRealtime() - mLastClickTimeNoti < 2000) {
                    return;
                }
                mLastClickTimeNoti = SystemClock.elapsedRealtime();
                startActivity(new Intent(nActivity, ActivityNotifications.class));
                break;

            case R.id.ivSearchimg:
                if (SystemClock.elapsedRealtime() - mLastClickTimeSearch < 2000) {
                    return;
                }
                mLastClickTimeSearch = SystemClock.elapsedRealtime();
                startActivity(new Intent(nActivity, ActivitySearchSalons.class));
                break;

            case R.id.tvUserAddress:
            case R.id.tvUserAddressTemp:
            case R.id.tvUserName:
                getStartPlaceLocation();
               // startActivity(new Intent(nActivity, ActivitySearchLocation.class));
                break;
                case R.id.frameCart:
                    if (SystemClock.elapsedRealtime() - mLastClickTimeSearch < 2000) {
                        return;
                    }
                    mLastClickTimeSearch = SystemClock.elapsedRealtime();
//                    if(!Preferences.getPreferenceValue(nActivity, "oneTimeSalonId").equalsIgnoreCase("0")) {
                        startActivity(new Intent(nActivity, CartActivity.class).putExtra("branchId", Preferences.getPreferenceValue(nActivity, "oneTimeSalonId")).putExtra("mainCat", Preferences.getPreferenceValue(nActivity, "mainCat")));
//                    }
                break;
        }
    }

    int AUTOCOMPLETE_REQUEST_CODE = 1;
    // variable to track event time
    private long mLastClickTime = 0;
    // Set the fields to specify which types of place data to
// return after the user has made a selection.
    private void getStartPlaceLocation() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);

        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .build(context);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i("TAG", "Place: " + place.getName() + ", " + place.getId());
                lat= ""+place.getLatLng().latitude;
                lon=""+place.getLatLng().longitude;
                Preferences.setPreferenceValue(nActivity,"lat",lat);
                Preferences.setPreferenceValue(nActivity,"lon",lon);
                if (isNetworkAvailable(nActivity)) {
                    getUpdateLocation(place.getAddress());
                } else {
                    showNoInternetDialog(nActivity);
                }
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("TAG", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
Context context;
    private static final int loc_request_code = 1234;
    private static final String Fine_Location = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String Coarse_Location = Manifest.permission.ACCESS_COARSE_LOCATION;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }
    public void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permission");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(nActivity.getApplicationContext(), Fine_Location) ==
                PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "getLocationPermission:  fine permissions already granted");
            if (ContextCompat.checkSelfPermission(nActivity.getApplicationContext(), Coarse_Location) ==
                    PackageManager.PERMISSION_GRANTED) {
                Loc_permissiongranted = true;
               // enableLocation(context);
                getDeviceLocation();
                Log.d(TAG, "getLocationPermission:  coarse permissions already granted");

            } else {
                ActivityCompat.requestPermissions(nActivity,
                        permissions, loc_request_code);
            }
        } else {
            ActivityCompat.requestPermissions(nActivity,
                    permissions, loc_request_code);
        }
    }
    private boolean Loc_permissiongranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    public static String lat="0",lon="0";
    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: Getting current Location of device");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(nActivity);
        try {
            if (Loc_permissiongranted) {
                mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(nActivity,
                        new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    Log.d(TAG, "onSuccess: location data");
                                    Log.d(TAG, "onSuccess: " + location.getLatitude() + "    " + location.getLongitude());
                                    lat=""+location.getLatitude();
                                    lon=""+location.getLongitude();
                                } else {
                                    Log.d(TAG, "onfailure: unable to find current device location");
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "getDeviceLocation: " + e.getMessage());
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: " + e.getMessage());
        }
    }

    UpdateLocationBean updateLocationBean;
    public static int updateLocCount1=0;
    private void getUpdateLocation(final String address) {
        loadHomeFirstTime=false;
        ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        final Dialog progressDialog=showProgress(nActivity,0);
        progressDialog.show();
        service.getUpdateLocation(USERIDVAL,lat,lon,address)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UpdateLocationBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(UpdateLocationBean loginBeanObj) {
                        progressDialog.dismiss();
                        updateLocationBean = loginBeanObj;
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        if(e.getMessage().contains("502"))
                        {
                            if(updateLocCount1<2) {
                                updateLocCount1++;
                                getUpdateLocation(address);
                            }else{
                               // showFailToast(nActivity, nActivity.getResources().getString(R.string.went_wrong));
                            }
                        }else
                        {
                           // showFailToast(nActivity, nActivity.getResources().getString(R.string.went_wrong));
                        }
                    }

                    @Override
                    public void onComplete() {
                        // Updates UI with data
                        progressDialog.dismiss();
                        if (updateLocationBean.getStatus() == 1) {
                            Preferences.setPreferenceValue(nActivity, USERCITYKEY,   address);
                            USERCITY=address;
                            listSalonBinding.tvUserAddress.setText(""+USERCITY+" >");
                            showSuccessToast(nActivity,"Update Location successfully");
                            if (isNetworkAvailable(nActivity)) {
                                getHomeData();
                            } else {
                                showNoInternetDialog(nActivity);
                            }
                            //startActivity(new Intent(nActivity, SuccessDialogActivity.class));
                        }else if(updateLocationBean.getStatus() == 3){
                            logout(nActivity);
                        }else
                        {
                            showFailToast(nActivity,  "" + updateLocationBean.getMsg());
                        }
//                        cPresenter.updateCoinList(allCurrencyList);
                    }
                });


    }


    static List<HomeBean.Result> homeResultList;
    public static void getHomeData() {

        ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        //final Dialog progressDialog = showProgress(nActivity, 0);

         final Dialog progressDialog = showProgress(nActivity, 0);
            if (menuPos == 0) {
                if (!nActivity.isFinishing()) {
                    if(!progressDialog.isShowing()) {
                        progressDialog.show();
                    }
                }

            }

        if(selectedCats.contains("0"))
        {
            category="";
        }else
        {
            category=TextUtils.join(",",selectedCats);
        }


        brands=TextUtils.join(",",brandsArr);

        if(rating.equalsIgnoreCase("0.0"))
        {
            rating="";
        }

        homeResultList=new ArrayList<>();
        service.getHomeData(lat, lon, category, brands, rating, discount, sortBy,getPincode(),gender,lowToHigh,highToLow)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HomeBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HomeBean loginBeanObj) {
                        homeBean = loginBeanObj;
                        if(progressDialog!=null) {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(progressDialog!=null) {
                            progressDialog.dismiss();
                        }
                       // showFailToast(nActivity, nActivity.getResources().getString(R.string.went_wrong));
                       // listSalonBinding.conlayFilter.setVisibility(View.GONE);
                        setEmptyMsg(homeResultList, listSalonBinding.recycleListsaloonView, listSalonBinding.layNoData.ivNoData);
                    }

                    @Override
                    public void onComplete() {
                        if(progressDialog!=null) {
                            progressDialog.dismiss();
                        }
                        // Updates UI with data
                        if (homeBean.getStatus() == 1) {
                            //loadFragment(new FragmentListSalonView(nActivity));
                             homeResultList = homeBean.getResult();
                             listSalonBinding.conlayFilter.setVisibility(View.VISIBLE);
                            listSalonBinding.tvPopularSalon.setText("Popular Salon Nearby ("+homeResultList.size()+")");
                            listSalonBinding.tvUserName.setText("Hello, "+USERFNAME+" "+USERLNAME);
                            if(!USERCITY.isEmpty() || !USERCITY.equalsIgnoreCase("NA")) {
                                listSalonBinding.tvUserAddress.setText(""+USERCITY+" >");
                            }else
                            {
                                listSalonBinding.tvUserAddress.setText("Select Location >");
                            };
                            listSalonBinding.recycleListsaloonView.setLayoutManager(new LinearLayoutManager(nActivity));
                            listSalonBinding.recycleListsaloonView.setAdapter(new SalonListAdapter(nActivity, false, homeResultList));/*, new SalonListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                startActivity(new Intent(nActivity, ActivitySalonProfile.class));
            }
        }));*/
                            if(homeBean.getCart()!=0) {
                                listSalonBinding.tvUnreadCart.setText("" + homeBean.getCart());
                                globalCartCount=homeBean.getCart();
                            }else {
                                listSalonBinding.tvUnreadCart.setText("0");
                                listSalonBinding.tvUnreadCart.setVisibility(View.GONE);
                            }
                            if(homeBean.getNotifications()!=null) {
                                listSalonBinding.tvUnreadNots.setText("" + homeBean.getNotifications());
                            }else {
                                listSalonBinding.tvUnreadNots.setText("0");
                                listSalonBinding.tvUnreadNots.setVisibility(View.GONE);
                            }
                            //startActivityForResult(new Intent(ActivityHomepage.this, SuccessDialogActivity.class).putExtra("msg","Your request has been submitted successfully, system admin will get back to you shortly"),resultCodeChangePass);
                           /* try {
                                listSalonBinding.slider.setAdapter(new ViewPagerSliderAdapter(nActivity, false, homeBean.getAdvertisement()));
                            }catch (Exception e)
                            {e.printStackTrace();}*/

                            Preferences.setPreferenceValue(nActivity, REDEEMPOINTSKEY,homeBean.getRedeemPoints().getPoints());

                            if(homeBean.getUserRescheduledApt().size()!=0)
                            {
                                NetworkChangeReceiver.aptReschedule=true;
                                nActivity.startActivity(new Intent(nActivity, ActivityAppointmentDetails.class).putExtra("appId",""+homeBean.getUserRescheduledApt().get(0).getAptId()).putExtra("rescheduleApt","rescheduleApt"));
                            }
                            if(homeBean.getReferrerPoint()!=null && !homeBean.getReferrerPoint().isEmpty())
                            {
                                Preferences.setPreferenceValue(nActivity,REFERERPOINTKEY,homeBean.getReferrerPoint());
                            }else
                            {
                                Preferences.setPreferenceValue(nActivity,REFERERPOINTKEY,"0");
                            }

                            try {


                                if(homeBean!=null) {
                                    // listSalonBinding.slider.setAdapter(new ViewPagerSliderAdapter(nActivity, false, homeBean.getAdvertisement()));
                                    Timer timer = new Timer();
                                    timer.scheduleAtFixedRate(new SliderTimer(), 3000, 2000);
                                    //    setPageNuber(homeBean.getAdvertisement().size(),i);


                                    SliderAdapterExample adapter = new SliderAdapterExample(nActivity);

                                    for(int i=0;i<homeBean.getAdvertisement().size();i++)
                                    {
                                        SliderItem sliderItem=new SliderItem();
                                        sliderItem.setImageUrl(homeBean.getAdvertisement().get(i).getAdvImg());
                                        sliderItem.setDescription(homeBean.getAdvertisement().get(i).getAdvUrl());
                                        adapter.addItem(sliderItem);
                                    }
                                    listSalonBinding.sliderView.setSliderAdapter(adapter);

                                    listSalonBinding.sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                                    listSalonBinding.sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                                    listSalonBinding.sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_LEFT);
                                    listSalonBinding.sliderView.setIndicatorSelectedColor(Color.TRANSPARENT);
                                    listSalonBinding.sliderView.setIndicatorUnselectedColor(Color.TRANSPARENT);
                                    listSalonBinding.sliderView.setIndicatorEnabled(false);
                                    listSalonBinding.sliderView.setAutoCycle(true);
                                    listSalonBinding.sliderView.setScrollTimeInSec(5); //set scroll delay in seconds :
                                    listSalonBinding.sliderView.startAutoCycle();
                                }
                            }catch (Exception e)
                            {e.printStackTrace();}
                        }  else if(homeBean.getStatus()==3){
                            logout(nActivity);
                        }else {
                          //  listSalonBinding.conlayFilter.setVisibility(View.GONE);
                            //showFailToast(nActivity, "" + homeBean.getMsg());
                            listSalonBinding.tvPopularSalon.setText("Popular Salon Nearby ("+homeResultList.size()+")");
                        }
//                        cPresenter.updateCoinList(allCurrencyList);
                        if(homeResultList.size()>0)
                        {
                            listSalonBinding.pullToRefresh.setVisibility(View.VISIBLE);
                        }else
                        {
                            listSalonBinding.pullToRefresh.setVisibility(View.GONE);
                        }
                        setEmptyMsg(homeResultList, listSalonBinding.recycleListsaloonView, listSalonBinding.layNoData.ivNoData);
                    }
                });
    }

    public static String getPincode()
    {
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(nActivity, Locale.getDefault());

        try {
            lat=Preferences.getPreferenceValue(nActivity,"lat");
            lon=Preferences.getPreferenceValue(nActivity,"lon");
            if(lat.equalsIgnoreCase("NA"))
            {
                lat="0";
                lon="0";
            }
            addresses = geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lon), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(addresses!=null) {

            if (addresses.size() != 0) {
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                return postalCode;
            }
        }
return "";
    }

}
