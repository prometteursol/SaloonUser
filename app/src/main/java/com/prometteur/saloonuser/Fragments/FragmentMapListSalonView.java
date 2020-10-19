package com.prometteur.saloonuser.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.maps.android.clustering.ClusterManager;
import com.prometteur.saloonuser.Activities.ActivityAccountsSettings;
import com.prometteur.saloonuser.Activities.ActivityNotifications;
import com.prometteur.saloonuser.Activities.ActivitySalonProfile;
import com.prometteur.saloonuser.Activities.ActivitySearchLocation;
import com.prometteur.saloonuser.Activities.ActivitySearchSalons;
import com.prometteur.saloonuser.Activities.CartActivity;
import com.prometteur.saloonuser.Activities.SuccessDialogActivity;
import com.prometteur.saloonuser.Adapter.SalonListAdapter;
import com.prometteur.saloonuser.Adapter.ViewPagerSliderAdapter;
import com.prometteur.saloonuser.Model.HomeBean;
import com.prometteur.saloonuser.Model.LoginBean;
import com.prometteur.saloonuser.Model.MapClusterItem;
import com.prometteur.saloonuser.Model.UpdateLocationBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.LinePagerIndicatorDecoration;
import com.prometteur.saloonuser.Utils.MyClusterRenderer;
import com.prometteur.saloonuser.Utils.MyCustomAdapterForItems;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.prometteur.saloonuser.Utils.Preferences;
import com.prometteur.saloonuser.databinding.FragmentMapListSalonViewBinding;
import com.prometteur.saloonuser.retrofit.ApiInterface;
import com.prometteur.saloonuser.retrofit.RetrofitInstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.branchId;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.homeBean;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.strLat;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.strLong;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERCITY;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERFNAME;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERIDVAL;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERLNAME;
import static com.prometteur.saloonuser.Utils.Utils.isNetworkAvailable;
import static com.prometteur.saloonuser.Utils.Utils.logout;
import static com.prometteur.saloonuser.Utils.Utils.showFailToast;
import static com.prometteur.saloonuser.Utils.Utils.showNoInternetDialog;
import static com.prometteur.saloonuser.Utils.Utils.showProgress;
import static com.prometteur.saloonuser.Utils.Utils.showSuccessToast;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMapListSalonView extends Fragment implements OnMapReadyCallback, View.OnClickListener {

    Activity nActivity;
    private static final String TAG = "MapListSalonView";

    FragmentMapListSalonViewBinding mapViewBinding;

    //map initialization and other variables
    private static final int Error_dialog_request = 9001;
    public static GoogleMap mMap;
    private static final String Fine_Location = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String Coarse_Location = Manifest.permission.ACCESS_COARSE_LOCATION;
    private boolean Loc_permissiongranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final float Default_Zoom = 15f;
    public static LatLng deviceLocation;
    private static final int loc_request_code = 1234;
    List<HomeBean.Result> homeResultList=new ArrayList<>();
    int size =3;
    int i=1;
    String lat="",lon="";
    View view;
    public FragmentMapListSalonView() {
        // Required empty public constructor
    }

    public FragmentMapListSalonView(Activity nActivity) {
        this.nActivity = nActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
      /*  if(view!=null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);

        }*/
        mapViewBinding = FragmentMapListSalonViewBinding.inflate(inflater, container, false);
        view = mapViewBinding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null) {
            initalizeMap();
        }
        if (isServiceOk()) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(nActivity);
            layoutManager.setOrientation(RecyclerView.HORIZONTAL);
            if (savedInstanceState == null) {
                initalizeMap();
            }
            if(homeBean!=null) {
                homeResultList = homeBean.getResult();
                mapViewBinding.tvUserName.setText("Hello, "+USERFNAME+" "+USERLNAME);
                if(!USERCITY.isEmpty() || !USERCITY.equalsIgnoreCase("NA")) {
                    mapViewBinding.tvUserAddress.setText("" + USERCITY+" >");
                }else
                {
                    mapViewBinding.tvUserAddress.setText("Select Location >");
                }
                mapViewBinding.ivSearchimg.setOnClickListener(FragmentMapListSalonView.this);
                mapViewBinding.tvUserName.setOnClickListener(FragmentMapListSalonView.this);
                mapViewBinding.tvUserAddress.setOnClickListener(FragmentMapListSalonView.this);
                mapViewBinding.tvUserAddressTemp.setOnClickListener(FragmentMapListSalonView.this);
                mapViewBinding.ivNotification.setOnClickListener(FragmentMapListSalonView.this);
                mapViewBinding.frameCart.setOnClickListener(this);
                if(homeBean.getCart()!=0) {
                    mapViewBinding.tvUnreadCart.setText("" + homeBean.getCart());
                }else
                {
                    mapViewBinding.tvUnreadCart.setText("0");
                    mapViewBinding.tvUnreadCart.setVisibility(View.GONE);
                }
                if(homeBean.getNotifications()!=null) {
                    mapViewBinding.tvUnreadNots.setText("" + homeBean.getNotifications());
                }else
                {
                    mapViewBinding.tvUnreadNots.setText("0");
                    mapViewBinding.tvUnreadNots.setVisibility(View.GONE);
                    mapViewBinding.framebell.removeView(mapViewBinding.tvUnreadNots);
                }

                try {
                    mapViewBinding.slider.setAdapter(new ViewPagerSliderAdapter((AppCompatActivity) nActivity, false, homeBean.getAdvertisement()));
                }catch (Exception e)
                {e.printStackTrace();}
                mapViewBinding.slider.setPageMargin(-(int) getResources().getDimension(R.dimen._35sdp));
                setPageNuber(homeBean.getAdvertisement().size(),i);
            }

        }
        getLocationPermission();
    }
    /*@Override
    public void onDestroyView() {
        super.onDestroyView();
        SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            getFragmentManager().beginTransaction().remove(mapFragment).commit();
        }
    }*/
    public void setPageNuber(final int insize, int i) {
        if (insize == i) {
            i = 0;
        }

        mapViewBinding.slider.setCurrentItem(i, true);
//    slider.setCurrentItem(i);
        //slider.setAnimation(new ());
        i++;
        final int finalI = i;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setPageNuber(insize, finalI);
            }
        }, 5000);

    }



    public boolean isServiceOk() {
        try {

            Log.d(TAG, "isServiceOk: checking google service version");

            int available = GoogleApiAvailability.getInstance()
                    .isGooglePlayServicesAvailable(context);
            if (available == ConnectionResult.SUCCESS) {
                Log.d(TAG, "isServiceOk: Google play service is working");
                return true;
            } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
                //error occured but can be resolved
                Log.d(TAG, "isServiceOk: an error occured but we can resolve it");
                Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(nActivity, available, Error_dialog_request);
                dialog.show();

            } else {
                Toast.makeText(context, "you can't make map request", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    private void initalizeMap() {
        Log.d(TAG, "initalizeMap: Initializing map");

        SupportMapFragment mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
        mapFragment.getMapAsync(this);


    }
    private ClusterManager<MapClusterItem> mClusterManager;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                /*mMap.setOnMapClickListener(this);
                mMap.setOnMapLongClickListener(this);*/
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(false);

        if (Loc_permissiongranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(nActivity, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                    (nActivity, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                return;
            }

            // googleMap.setOnMapLoadedCallback(() -> googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(LOCATION_BOUND, 0)));
            mClusterManager = new ClusterManager<>(nActivity, googleMap);
            mClusterManager.setRenderer(new MyClusterRenderer(nActivity, googleMap,
                    mClusterManager));
            googleMap.setOnCameraIdleListener(mClusterManager);
            googleMap.setOnMarkerClickListener(mClusterManager);
            googleMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
            googleMap.setOnInfoWindowClickListener(mClusterManager);
            mClusterManager.setOnClusterItemInfoWindowClickListener(new ClusterManager.OnClusterItemInfoWindowClickListener<MapClusterItem>() {
                @Override
                public void onClusterItemInfoWindowClick(MapClusterItem mapClusterItem) {

                    //Cluster item InfoWindow clicked, set title as action
                   /* Intent hotelDetailsActivity = new Intent(nActivity, HotelDetailsActivity.class);
                    hotelDetailsActivity.putExtra(BundleConstants.HOTEL_ID, mapClusterItem.getHotel_id());
                    hotelDetailsActivity.setAction(mapClusterItem.getTitle());
                    startActivity(hotelDetailsActivity);*/

                    nActivity.startActivity(new Intent(nActivity, ActivitySalonProfile.class).putExtra("branchId", mapClusterItem.getHotel_id()));
                }
            }); //added
            mClusterManager
                    .setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MapClusterItem>() {
                        @Override
                        public boolean onClusterItemClick(MapClusterItem item) {
                            mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(
                                    new MyCustomAdapterForItems(getActivity(),item));
                            return false;
                        }
                    });



            List<MapClusterItem> clusterItems = new ArrayList<>();
            double latitude=0.0;
            double longitude=0.0;
            if (homeResultList.size() > 0) {

int pos=0;
                for (int i = 0; i < homeResultList.size(); i++) {
//
                    try {
                         latitude = Double.parseDouble(homeResultList.get(i).getBranLat());
                         longitude = Double.parseDouble(homeResultList.get(i).getBranLon());
                    } catch (NumberFormatException ex) { // handle your exception
                        ex.printStackTrace();
                    }

                    clusterItems.add(new MapClusterItem(new LatLng(latitude, longitude),homeResultList.get(i).getBranName(), homeResultList.get(i).getBranId(),
                            homeResultList.get(i).getDistance().replace("KM"," KM"), (String) homeResultList.get(i).getSalonRating(), homeResultList.get(i).getBranImg()));
if(branchId!=null) {
    if (!branchId.equalsIgnoreCase("0")) {
        try {
            if (branchId.equalsIgnoreCase(homeResultList.get(i).getBranId())) {
                pos = i;
            }
        } catch (NumberFormatException ex) { // handle your exception
            ex.printStackTrace();
        }
    }
}
                }
                mClusterManager.clearItems();
                mClusterManager.addItems(clusterItems);
//                                mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(
//                                        new MyCustomAdapterForItems());
//

                LatLngBounds LOCATION_BOUND = new LatLngBounds(
                        new LatLng(clusterItems.get(0).getLatitude(), clusterItems.get(0).getLongitude()), new LatLng(clusterItems.get(0).getLatitude(), clusterItems.get(0).getLongitude()));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom( new LatLng(clusterItems.get(pos).getLatitude(), clusterItems.get(pos).getLongitude()), Default_Zoom));
               // mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(LOCATION_BOUND, 0));



            }else
            {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom( new LatLng(Double.parseDouble(strLat), Double.parseDouble(strLong)), Default_Zoom));
            }

        }
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
               // initalizeMap();
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
                                    lat=""+location.getLatitude();
                                    lon=""+location.getLongitude();
                                    Log.d(TAG, "onSuccess: location data");
                                    Log.d(TAG, "onSuccess: " + location.getLatitude() + "    " + location.getLongitude());
                                    deviceLocation = new LatLng(location.getLatitude(),
                                            location.getLongitude());
                                    moveCamera( deviceLocation, Default_Zoom);
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
    public void moveCamera( LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: moving camera to lat:" + latLng.latitude + " , long:" + latLng.longitude);
       // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.ivSearchimg:
                startActivity(new Intent(nActivity, ActivitySearchSalons.class));
                break;

            case R.id.tvUserAddress:
            case R.id.tvUserAddressTemp:
            case R.id.tvUserName:
                getStartPlaceLocation();
                //startActivity(new Intent(nActivity, ActivitySearchLocation.class));
                break;

            case R.id.ivNotification:
                startActivity(new Intent(nActivity, ActivityNotifications.class));
                break;

                case R.id.frameCart:
                    startActivity(new Intent(nActivity, CartActivity.class).putExtra("branchId", Preferences.getPreferenceValue(nActivity, "oneTimeSalonId")).putExtra("mainCat", Preferences.getPreferenceValue(nActivity, "mainCat")));
                break;

        }

    }


    int AUTOCOMPLETE_REQUEST_CODE = 1;
    private long mLastClickTime = 0;
    // Set the fields to specify which types of place data to
// return after the user has made a selection.
    private void getStartPlaceLocation() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.ADDRESS,Place.Field.LAT_LNG);

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
                FragmentListSalonView.lat= ""+place.getLatLng().latitude;
                FragmentListSalonView.lon=""+place.getLatLng().longitude;
                Preferences.setPreferenceValue(nActivity, "lat", FragmentListSalonView.lat);
                Preferences.setPreferenceValue(nActivity, "lon", FragmentListSalonView.lon);
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
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
        this.nActivity= (Activity) context;
    }

    UpdateLocationBean updateLocationBean;
    private void getUpdateLocation(String address) {

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
                      //  showFailToast(nActivity, getResources().getString(R.string.went_wrong));
                    }

                    @Override
                    public void onComplete() {
                        // Updates UI with data
                        progressDialog.dismiss();
                        if (updateLocationBean.getStatus() == 1) {
                           // startActivity(new Intent(nActivity, SuccessDialogActivity.class));
                            showSuccessToast(nActivity,"Update Location successfully");
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


}
