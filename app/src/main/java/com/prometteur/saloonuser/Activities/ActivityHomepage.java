package com.prometteur.saloonuser.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.prometteur.saloonuser.Fragments.FragmentAppointments;
import com.prometteur.saloonuser.Fragments.FragmentListSalonView;
import com.prometteur.saloonuser.Fragments.FragmentMapListSalonView;
import com.prometteur.saloonuser.Fragments.FragmentMyAccounts;
import com.prometteur.saloonuser.Model.HomeBean;
import com.prometteur.saloonuser.Model.UpdateLocationBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.NetworkChangeReceiver;
import com.prometteur.saloonuser.Utils.Preferences;
import com.prometteur.saloonuser.databinding.ActivityHomepageBinding;
import com.prometteur.saloonuser.retrofit.ApiInterface;
import com.prometteur.saloonuser.retrofit.RetrofitInstance;
import com.prometteur.saloonuser.services.ClosingService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.prometteur.saloonuser.Constants.ConstantVariables.REDEEMPOINTSKEY;
import static com.prometteur.saloonuser.Constants.ConstantVariables.REFERERPOINTKEY;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERCITY;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERCITYKEY;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USEREMAIL;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USEREMAILKEY;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERFNAME;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERFNAMEKEY;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERGENDER;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERGENDERKEY;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERID;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERIDVAL;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERLNAME;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERLNAMEKEY;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERMOB;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERMOBKEY;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERPROFILE;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERPROFILEKEY;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERSESSION;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERSESSIONVAL;
import static com.prometteur.saloonuser.Constants.ConstantVariables.apiKey;
import static com.prometteur.saloonuser.Fragments.FragmentListSalonView.lat;
import static com.prometteur.saloonuser.Fragments.FragmentListSalonView.lon;
import static com.prometteur.saloonuser.Utils.Utils.enableLocation;
import static com.prometteur.saloonuser.Utils.Utils.isNetworkAvailable;
import static com.prometteur.saloonuser.Utils.Utils.logout;
import static com.prometteur.saloonuser.Utils.Utils.showFailToast;
import static com.prometteur.saloonuser.Utils.Utils.showNoInternetDialog;
import static com.prometteur.saloonuser.Utils.Utils.showProgress;
import static com.prometteur.saloonuser.Utils.Utils.showSuccessToast;

public class ActivityHomepage extends AppCompatActivity implements View.OnClickListener, LocationListener {
    private static final String TAG = "ActivityHomepage";

    public static ActivityHomepageBinding homeBinding;
    public static HomeBean homeBean;
    Context nContext;
    static AppCompatActivity nActivity;
    static boolean mapLoaded = false;
    public static boolean dateTimeOneTime = false;
    public static String oneTimeSalonId = "0";
    public static String strTime = "";
    public static String strDate = "", strAppDate = "";
    public static int menuPos = 0, globalCartCount = 0;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    public static boolean loadHomeFirstTime = false;
    LocationProviderChangedReceiver gpsReceiver;
    public static String category="",brands="",rating="",discount="",sortBy="",gender="",lowToHigh="",highToLow="";
    public static List<String> brandsArr=new ArrayList<>();
    public static List<String> selectedCats=new ArrayList<>();
    public static List<String> selectedCatsText=new ArrayList<>();
    public static String strHistory="0";
    public NetworkChangeReceiver receiver;
    public static boolean isFilter=false;
    public static AlertDialog.Builder alertDialog;
    public static int comboSkip=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeBinding = ActivityHomepageBinding.inflate(getLayoutInflater());
        View view = homeBinding.getRoot();
        setContentView(view);
        nContext = this;
        nActivity = this;
         alertDialog= new AlertDialog.Builder(nActivity);
        gpsReceiver = new LocationProviderChangedReceiver();
        Intent serviceIntent = new Intent(this, ClosingService.class);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            startService(serviceIntent);
        }else
        {
            startForegroundService(serviceIntent);
        }
        USERIDVAL = Preferences.getPreferenceValue(ActivityHomepage.this, USERID);
        USERSESSIONVAL = Preferences.getPreferenceValue(ActivityHomepage.this, USERSESSION);
        USERFNAME = Preferences.getPreferenceValue(ActivityHomepage.this, USERFNAMEKEY);
        USERLNAME = Preferences.getPreferenceValue(ActivityHomepage.this, USERLNAMEKEY);
        USEREMAIL = Preferences.getPreferenceValue(ActivityHomepage.this, USEREMAILKEY);
        USERMOB = Preferences.getPreferenceValue(ActivityHomepage.this, USERMOBKEY);
        USERPROFILE = Preferences.getPreferenceValue(ActivityHomepage.this, USERPROFILEKEY);
        USERGENDER = Preferences.getPreferenceValue(ActivityHomepage.this, USERGENDERKEY);
        USERCITY = Preferences.getPreferenceValue(ActivityHomepage.this, USERCITYKEY);

       gender= Preferences.getPreferenceValue(nActivity, "genderFilter" );
       category= Preferences.getPreferenceValue(nActivity, "categoryFilter");
       brands= Preferences.getPreferenceValue(nActivity, "brandsFilter");
       rating= Preferences.getPreferenceValue(nActivity, "ratingFilter" );
       discount= Preferences.getPreferenceValue(nActivity, "discountFilter");
       sortBy= Preferences.getPreferenceValue(nActivity, "sortByFilter");
        lowToHigh= Preferences.getPreferenceValue(nActivity, "sortByLowToHigh");
        highToLow= Preferences.getPreferenceValue(nActivity, "sortByHighToLow");

       if(!category.isEmpty() && !category.equalsIgnoreCase("NA")&& !category.equalsIgnoreCase("0") )
       {
           selectedCats=new ArrayList<>();
           selectedCats.addAll(Arrays.asList(category.split(",")));
           isFilter=true;
       }else
       {
           category="";
           selectedCats=new ArrayList<>();
           selectedCatsText=new ArrayList<>();
       }
       if(!brands.isEmpty() && !brands.equalsIgnoreCase("NA"))
       {
           brandsArr=new ArrayList<>();
           brandsArr.addAll(Arrays.asList(brands.split(",")));
           isFilter=true;
       }else
       {
           brands="";
           brandsArr=new ArrayList<>();
       }

       if(gender.equalsIgnoreCase("NA")|| gender.equalsIgnoreCase(""))
       {
           gender="";
       }else
       {
           isFilter=true;
       }

       if(rating.equalsIgnoreCase("NA") || rating.equalsIgnoreCase("0.0") || rating.isEmpty())
       {
           rating="";
       }else
       {
           isFilter=true;
       }if(discount.equalsIgnoreCase("NA") || discount.equalsIgnoreCase(""))
       {
           discount="";
       }else
        {
            isFilter=true;
        }
       if(sortBy.equalsIgnoreCase("NA") || sortBy.equalsIgnoreCase(""))
       {
           sortBy="";
       }else
       {
           isFilter=true;
       }if(lowToHigh.equalsIgnoreCase("NA") || lowToHigh.equalsIgnoreCase(""))
       {
           lowToHigh="";
       }else
        {
            isFilter=true;
        }if(highToLow.equalsIgnoreCase("NA") || highToLow.equalsIgnoreCase(""))
       {
           highToLow="";
       }else
        {
            isFilter=true;
        }
       String appId=getIntent().getStringExtra("appId");
       if(appId==null)
       {
           appId="null";
       }
        if (!appId.equalsIgnoreCase("null")) {
            //showFailToast(ActivityHomepage.this,"In Home "+ getIntent().getStringExtra("appId")+"  "+getIntent().getStringExtra("notiId"));
            Intent intent1 = new Intent(ActivityHomepage.this, ActivityAppointmentDetails.class);
            intent1.putExtra("appId", "" + getIntent().getStringExtra("appId"));
            intent1.putExtra("notiId", "" + getIntent().getStringExtra("notiId"));
            /*intent1.addFlags(FLAG_ACTIVITY_NEW_TASK);
            intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);*/
            Log.i("push notification","ActivityHomePage");
            startActivity(intent1);
        }



        if (!Preferences.getPreferenceValue(ActivityHomepage.this, "dateTimeOneTime").equalsIgnoreCase("NA")) {
            dateTimeOneTime = Boolean.parseBoolean(Preferences.getPreferenceValue(ActivityHomepage.this, "dateTimeOneTime"));
        } else {
            dateTimeOneTime = false;
        }
        if (!Preferences.getPreferenceValue(ActivityHomepage.this, "oneTimeSalonId").equalsIgnoreCase("NA")) {
            oneTimeSalonId = Preferences.getPreferenceValue(ActivityHomepage.this, "oneTimeSalonId");
        } else {
            oneTimeSalonId = "0";
        }

        //Preferences.setPreferenceValue(nActivity,"dateTime",strDate+strTime);
        strAppDate = Preferences.getPreferenceValue(nActivity, "appDate");
        strTime = Preferences.getPreferenceValue(nActivity, "appTime");

        homeBinding.fabFloatingButton.setImageDrawable
                (nActivity.getResources().getDrawable(R.drawable.ic_show_map_icon));
     //   menuPos = 0;
        loadFragment(new FragmentListSalonView(ActivityHomepage.this));
        homeBinding.fabFloatingButton.setOnClickListener(this);
        /*homeBinding.ivSearchimg.setOnClickListener(this);
        homeBinding.tvUserAddress.setOnClickListener(this);*/
        homeBinding.navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        //  getBottomNavigationCount(ActivityHomepage.this, homeBinding.navigation);
        if (menuPos == 1) {
            homeBinding.navigation.setSelectedItemId(R.id.navigation_Appointments);
        }
        homeBinding.navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                switch (menuItem.getItemId()) {

                    case R.id.navigation_home:
                        displayLocationSettingsRequest(nActivity);
                        loadHomeFirstTime = false;
                       // getLocationPermission();
                        homeBinding.fabFloatingButton.setVisibility(View.VISIBLE);
                        homeBinding.fabFloatingButton.setImageDrawable
                                (nActivity.getResources().getDrawable(R.drawable.ic_show_map_icon));
                        getIntent().putExtra("branchId", "");
                        mapLoaded = false;
                        fragment = new FragmentListSalonView(nActivity);
                        menuPos = 0;
//                        startActivity(new Intent(nActivity,CouponCodeListActivity.class));
                        break;
                    case R.id.navigation_Appointments:
                        mapLoaded = false;
                        homeBinding.fabFloatingButton.setVisibility(View.GONE);
                        fragment = new FragmentAppointments(nActivity);
                        menuPos = 1;
                        strHistory="0";
                        break;
                    case R.id.navigation_Account:
                        mapLoaded = false;
                        homeBinding.fabFloatingButton.setVisibility(View.GONE);
                        fragment = new FragmentMyAccounts(nActivity);
                        menuPos = 2;
                }
                return loadFragment(fragment);
            }
        });
        // Initialize the SDK
        Places.initialize(getApplicationContext(), apiKey);

// Create a new Places client instance
        PlacesClient placesClient = Places.createClient(this);
        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i("call home api","onCreate");
            getLocationPermission();
            return;
        }
*/


    }

    @Override
    protected void onResume() {
        super.onResume();
        displayLocationSettingsRequest(nActivity);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i("call home api","resume");
            getLocationPermission();
            return;
        }
        checkInternet();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ActivityHomepage.this);
        //showSuccessToast(nActivity,"Request Location Update");
        //registerReceiver(gpsReceiver, new IntentFilter("android.location.PROVIDERS_CHANGED"));
        getUpdateLocation("");
        if(loadHomeFirstTime) {
            Log.i("call home api","resumeHome");
            getHomeData();
        }

        if(menuPos==1)
        {
            loadFragment(new FragmentAppointments(nActivity));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("ActivityHome","Destroy");
      //  unregisterReceiver(gpsReceiver);
    }

    private static boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            try {
                nActivity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFrame_container, fragment)
                        //.addToBackStack(null)
                        .commit();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

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
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.fabFloatingButton:
                if (mapLoaded) {
                    mapLoaded = false;
                    homeBinding.fabFloatingButton.setImageDrawable(nActivity.getResources().getDrawable(R.drawable.ic_show_map_icon));
                    getIntent().putExtra("branchId","");
                    loadFragment(new FragmentListSalonView(ActivityHomepage.this));
                } else {
                    mapLoaded = true;
                    homeBinding.fabFloatingButton.setImageDrawable(nActivity.getResources().getDrawable(R.drawable.ic_show_salon_list_icon));
                    loadFragment(new FragmentMapListSalonView(ActivityHomepage.this));
                }
                break;


        }
    }
public static String branchId="0";
public static int homeCount=0;
    final static long[] mLastClickTime = {0};
    private static void getHomeData() {
        if (SystemClock.elapsedRealtime() - mLastClickTime[0] < 1000) {
            return;
        }
        mLastClickTime[0] = SystemClock.elapsedRealtime();
        ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);

   final Dialog progressDialog = showProgress(nActivity, 0);
    if (menuPos == 0) {
        if (!nActivity.isFinishing()) {
            try {
                if(!progressDialog.isShowing()) {
                    progressDialog.show();
                }
            }catch (Exception e)
            {}
        }


    }

        if(strLat.equalsIgnoreCase("0") || strLat.equalsIgnoreCase("NA")) {
            strLat = Preferences.getPreferenceValue(nActivity, "lat");
            strLong = Preferences.getPreferenceValue(nActivity, "lon");
            if (strLat.equalsIgnoreCase("NA")) {
                strLat = "0";
                strLong = "0";
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
       // service.getHomeData(strLat, strLong, "", "", "", "", "",getPincode(),"")
        service.getHomeData(strLat, strLong, category, brands, rating, discount, sortBy,getPincode(),gender,lowToHigh,highToLow)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HomeBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HomeBean loginBeanObj) {
                        homeBean = loginBeanObj;
                        if(progressDialog!=null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(progressDialog!=null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        if(e.getMessage().contains("502"))
                        {
                            if(homeCount<2) {
                                homeCount++;
                                getDeviceLocation();
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
                        if(progressDialog!=null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        // Updates UI with data
                        if (homeBean.getStatus() == 1) {
                            // Preferences.setPreferenceValue(ActivityHomepage.this, USERID,          result.getUserId());
                            if(menuPos==0) {

                                if(nActivity.getIntent().getStringExtra("branchId")!=null && !nActivity.getIntent().getStringExtra("branchId").isEmpty()){
                                    branchId=nActivity.getIntent().getStringExtra("branchId");
                                    mapLoaded = true;
                                    homeBinding.fabFloatingButton.setImageDrawable(nActivity.getResources().getDrawable(R.drawable.ic_show_salon_list_icon));
                                    loadFragment(new FragmentMapListSalonView(nActivity));
                                }else if(mapLoaded)
                                {
                                    branchId=nActivity.getIntent().getStringExtra("branchId");
                                    mapLoaded = true;
                                    homeBinding.fabFloatingButton.setImageDrawable(nActivity.getResources().getDrawable(R.drawable.ic_show_salon_list_icon));
                                    loadFragment(new FragmentMapListSalonView(nActivity));
                                }else{
                                    branchId="";
                                    mapLoaded = false;
                                    homeBinding.fabFloatingButton.setImageDrawable(nActivity.getResources().getDrawable(R.drawable.ic_show_map_icon));
                                    loadFragment(new FragmentListSalonView(nActivity));
                                }
                            }else if(menuPos==2)
                            {
                                loadFragment(new FragmentMyAccounts(nActivity));
                            }
                            Preferences.setPreferenceValue(nActivity, REDEEMPOINTSKEY,homeBean.getRedeemPoints().getPoints());
                            //startActivityForResult(new Intent(ActivityHomepage.this, SuccessDialogActivity.class).putExtra("msg","Your request has been submitted successfully, system admin will get back to you shortly"),resultCodeChangePass);
                            if(homeBean.getUserRescheduledApt().size()!=0)
                            {
                                nActivity.startActivity(new Intent(nActivity, ActivityAppointmentDetails.class).putExtra("appId",""+homeBean.getUserRescheduledApt().get(0).getAptId()).putExtra("rescheduleApt","rescheduleApt"));
                            }
                            if(homeBean.getReferrerPoint()!=null && !homeBean.getReferrerPoint().isEmpty())
                            {
                                Preferences.setPreferenceValue(nActivity,REFERERPOINTKEY,homeBean.getReferrerPoint());
                            }else
                            {
                                Preferences.setPreferenceValue(nActivity,REFERERPOINTKEY,"0");
                            }
                        } else if(homeBean.getStatus()==3){
                            logout(nActivity);
                        }else {
                            showFailToast(nActivity, "" + homeBean.getMsg());
                        }
//                        cPresenter.updateCoinList(allCurrencyList);
                    }
                });
    }

    private static final int loc_request_code = 1234;
    private static final String Fine_Location = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String Coarse_Location = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permission");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
if(nActivity!=null) {
    if (ContextCompat.checkSelfPermission(nActivity.getApplicationContext(), Fine_Location) ==
            PackageManager.PERMISSION_GRANTED) {
        Log.d(TAG, "getLocationPermission:  fine permissions already granted");
        if (ContextCompat.checkSelfPermission(nActivity.getApplicationContext(), Coarse_Location) ==
                PackageManager.PERMISSION_GRANTED) {
            Loc_permissiongranted = true;
            //initalizeMap();
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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(loc_request_code==requestCode)
        {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission granted
                Loc_permissiongranted = true;
            }else{
                //show some warning
            }

           // enableLocation(nActivity);
            //getDeviceLocation();
        }
    }

    private static boolean Loc_permissiongranted = false;
    private static FusedLocationProviderClient mFusedLocationProviderClient;
    public static String strLat="0";
    public static String strLong="0";
    private static void getDeviceLocation() {
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
                                    strLat=""+location.getLatitude();
                                    strLong=""+location.getLongitude();
                                   // if(Preferences.getPreferenceValue(nActivity,"lat").equalsIgnoreCase("NA") || Preferences.getPreferenceValue(nActivity,"lat").isEmpty()) {
                                        Preferences.setPreferenceValue(nActivity, "lat", strLat);
                                        Preferences.setPreferenceValue(nActivity, "lon", strLong);
                                   /* }else
                                    {
                                        strLat=Preferences.getPreferenceValue(nActivity,"lat");
                                        strLong=Preferences.getPreferenceValue(nActivity,"lon");
                                    }*/
                                    if (isNetworkAvailable(nActivity)) {
                                        getUpdateLocation("");
                                        if(!loadHomeFirstTime) {
                                            loadHomeFirstTime=true;
                                            getHomeData();
                                        }
                                    } else {
                                        // Toast.makeText(ActivityLogin.this, getResources().getString(R.string.please_check_newtork_connection), Toast.LENGTH_SHORT).show();

                                        showNoInternetDialog(nActivity);
                                    }
                                } else {
                                    Log.d(TAG, "onfailure: unable to find current device location");
                                    /*strLat=Preferences.getPreferenceValue(nActivity,"lat");
                                    strLong=Preferences.getPreferenceValue(nActivity,"lon");*/
                                    if(strLat.equalsIgnoreCase("NA"))
                                    {
                                        strLat="0";
                                        strLong="0";
                                    }

                                    if (isNetworkAvailable(nActivity)) {
                                        getUpdateLocation("");
                                        getHomeData();
                                    } else {
                                        // Toast.makeText(ActivityLogin.this, getResources().getString(R.string.please_check_newtork_connection), Toast.LENGTH_SHORT).show();

                                        showNoInternetDialog(nActivity);
                                    }

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
    boolean doubleBackToExitPressedOnce=false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finishAffinity();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        showSuccessToast(this, "Please click BACK again to exit");

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


   // static final long[] mLastClickTime2 = {0};
    @Override
    public void onLocationChanged(Location location) {
       /* if (SystemClock.elapsedRealtime() - mLastClickTime2[0] < 60000) {
            return;
        }
        mLastClickTime2[0] = SystemClock.elapsedRealtime();*/
        strLat=String.valueOf(location.getLatitude());
        strLong=String.valueOf(location.getLongitude());
        lat=String.valueOf(location.getLatitude());
        lon=String.valueOf(location.getLongitude());

       // if(Preferences.getPreferenceValue(nActivity,"lat").equalsIgnoreCase("NA") || Preferences.getPreferenceValue(nActivity,"lat").isEmpty()) {
            Preferences.setPreferenceValue(nActivity, "lat", strLat);
            Preferences.setPreferenceValue(nActivity, "lon", strLong);

//        getUpdateLocation("");
       // }
       // showSuccessToast(nActivity,"onLocation :"+location.getLatitude()+","+location.getLongitude());
       // getHomeData();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public static String getPincode() {
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(nActivity, Locale.getDefault());

        try {
            if (!Preferences.getPreferenceValue(nActivity, "lat").equalsIgnoreCase("NA")) {
                strLat = Preferences.getPreferenceValue(nActivity, "lat");
                strLong = Preferences.getPreferenceValue(nActivity, "lon");
            }
            addresses = geocoder.getFromLocation(Double.parseDouble(strLat), Double.parseDouble(strLong), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (addresses != null) {
            if (addresses.size() != 0) {
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                if (USERCITY.isEmpty() || USERCITY.equalsIgnoreCase("NA")) {
                    USERCITY = address;// + ", " + city + ", " + state;
                }
                addressUpdatedLoc = address;
                return postalCode;
            }
        }
        return "";
    }
    static final long[] mLastClickTime1 = {0};

    public static class LocationProviderChangedReceiver extends BroadcastReceiver {
        private final static String TAG = "LocationProviderChanged";

        boolean isGpsEnabled;
        boolean isNetworkEnabled;

public LocationProviderChangedReceiver()
{
super();
}
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().matches("android.location.PROVIDERS_CHANGED"))
            {
                Log.i(TAG, "Location Providers changed");

                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                //Start your Activity if location was enabled:
                if (isGpsEnabled || isNetworkEnabled) {
                    Log.i("call home api","GPS Enabled");
                    getLocationPermission();
                }else
                {
                    if (SystemClock.elapsedRealtime() - mLastClickTime1[0] < 3000) {
                        return;
                    }
                    mLastClickTime1[0] = SystemClock.elapsedRealtime();
                    displayLocationSettingsRequest(nActivity);
                }
            }
        }
    }


    static UpdateLocationBean updateLocationBean;
    static String addressUpdatedLoc="";
    public static int updateLocCount=0;
    private static void getUpdateLocation(final String address) {
        getPincode();
        ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        /*final Dialog progressDialog=showProgress(ActivityHomepage.this,0);
        progressDialog.show();*/
        service.getUpdateLocation(USERIDVAL,lat,lon,addressUpdatedLoc)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UpdateLocationBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(UpdateLocationBean loginBeanObj) {
                        //progressDialog.dismiss();
                        updateLocationBean = loginBeanObj;
                    }

                    @Override
                    public void onError(Throwable e) {
                        //progressDialog.dismiss();
                        if(e.getMessage().contains("502"))
                        {
                            if(updateLocCount<2) {
                                updateLocCount++;
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
                        //progressDialog.dismiss();
                        if (updateLocationBean.getStatus() == 1) {
                            Preferences.setPreferenceValue(nActivity, USERCITYKEY,   addressUpdatedLoc);
                            USERCITY=addressUpdatedLoc;
                          //  showSuccessToast(ActivityHomepage.this,"Update Location successfully");
                            //startActivity(new Intent(ActivityAccountsSettings.this, SuccessDialogActivity.class));
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

static int REQUEST_CHECK_SETTINGS=10101;
    private static void displayLocationSettingsRequest(Context context) {
        try {
            GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(LocationServices.API).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(10000);
            locationRequest.setFastestInterval(10000 / 2);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            Log.i(TAG, "All location settings are satisfied.");
                            getLocationPermission();
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                            try {
                                // Show the dialog by calling startResolutionForResult(), and check the result
                                // in onActivityResult().
                                status.startResolutionForResult(nActivity, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                                Log.i(TAG, "PendingIntent unable to execute request.");
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                            break;
                    }
                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}