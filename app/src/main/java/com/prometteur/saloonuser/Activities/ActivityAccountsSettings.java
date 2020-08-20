package com.prometteur.saloonuser.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.prometteur.saloonuser.Fragments.FragmentListSalonView;
import com.prometteur.saloonuser.Model.LoginBean;
import com.prometteur.saloonuser.Model.UpdateLocationBean;
import com.prometteur.saloonuser.Model.UpodatePassBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.Preferences;
import com.prometteur.saloonuser.databinding.ActivityAccountsSettingsBinding;
import com.prometteur.saloonuser.databinding.DialogPasswordChangeBinding;
import com.prometteur.saloonuser.retrofit.ApiInterface;
import com.prometteur.saloonuser.retrofit.RetrofitInstance;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.prometteur.saloonuser.Constants.ConstantVariables.USERCITY;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERCITYKEY;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERIDVAL;
import static com.prometteur.saloonuser.Utils.Utils.enableLocation;
import static com.prometteur.saloonuser.Utils.Utils.isNetworkAvailable;
import static com.prometteur.saloonuser.Utils.Utils.isValidPassword;
import static com.prometteur.saloonuser.Utils.Utils.logout;
import static com.prometteur.saloonuser.Utils.Utils.showFailToast;
import static com.prometteur.saloonuser.Utils.Utils.showNoInternetDialog;
import static com.prometteur.saloonuser.Utils.Utils.showProgress;
import static com.prometteur.saloonuser.Utils.Utils.showSuccessToast;

public class ActivityAccountsSettings extends AppCompatActivity implements View.OnClickListener {

    ActivityAccountsSettingsBinding accountsSettingsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountsSettingsBinding = ActivityAccountsSettingsBinding.inflate(getLayoutInflater());
        View view = accountsSettingsBinding.getRoot();
        setContentView(view);
        accountsSettingsBinding.ivBackArrowimg.setOnClickListener(this);
        accountsSettingsBinding.tvLogOut.setOnClickListener(this);
        accountsSettingsBinding.linChangePass.setOnClickListener(this);
        accountsSettingsBinding.tvMyLocation.setOnClickListener(this);
        accountsSettingsBinding.tvLocationText.setOnClickListener(this);
        accountsSettingsBinding.ivLocArrow.setOnClickListener(this);

        accountsSettingsBinding.tvLocationText.setText(""+USERCITY+" >");
        enableLocation(ActivityAccountsSettings.this);

        if (Loc_permissiongranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(ActivityAccountsSettings.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                    (ActivityAccountsSettings.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                return;
            }
        }

    }
    
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBackArrowimg:
                finish();
                break;
            case R.id.linChangePass:
                showCancelRequestDialog(this);
                break;
                case R.id.tvMyLocation:case R.id.tvLocationText:case R.id.ivLocArrow:
                getStartPlaceLocation();
                break;
            case R.id.tvLogOut:

                startActivity(new Intent(ActivityAccountsSettings.this,ConfirmDialogActivity.class).putExtra("confirmMsg","Are you sure you want to logout from the application?"));

                break;
        }
    }
    private long mLastClickTime = 0;
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    // Set the fields to specify which types of place data to
// return after the user has made a selection.
    private void getStartPlaceLocation() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS,Place.Field.LAT_LNG);

        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .build(ActivityAccountsSettings.this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i("TAG", "Place: " + place.getName() + ", " + place.getId());
                Preferences.setPreferenceValue(ActivityAccountsSettings.this, "lat", FragmentListSalonView.lat);
                Preferences.setPreferenceValue(ActivityAccountsSettings.this, "lon", FragmentListSalonView.lon);
                if (isNetworkAvailable(ActivityAccountsSettings.this)) {
                    getUpdateLocation(place.getAddress());
                } else {
                    showNoInternetDialog(ActivityAccountsSettings.this);
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
    private boolean Loc_permissiongranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    String lat="",lon="";
    String TAG="TAG";
    
    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: Getting current Location of device");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(ActivityAccountsSettings.this);
        try {
            if (Loc_permissiongranted) {
                mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(ActivityAccountsSettings.this,
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
    public void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permission");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Fine_Location) ==
                PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "getLocationPermission:  fine permissions already granted");
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Coarse_Location) ==
                    PackageManager.PERMISSION_GRANTED) {
                Loc_permissiongranted = true;
                getDeviceLocation();
                Log.d(TAG, "getLocationPermission:  coarse permissions already granted");

            } else {
                ActivityCompat.requestPermissions(ActivityAccountsSettings.this,
                        permissions, loc_request_code);
            }
        } else {
            ActivityCompat.requestPermissions(ActivityAccountsSettings.this,
                    permissions, loc_request_code);
        }
    }
    UpdateLocationBean updateLocationBean;
    Context context;
    private static final int loc_request_code = 1234;
    private static final String Fine_Location = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String Coarse_Location = Manifest.permission.ACCESS_COARSE_LOCATION;
    private void getUpdateLocation(final String address) {

        ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        final Dialog progressDialog=showProgress(ActivityAccountsSettings.this,0);
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
                        showFailToast(ActivityAccountsSettings.this, getResources().getString(R.string.went_wrong));
                    }

                    @Override
                    public void onComplete() {
                        // Updates UI with data
                        progressDialog.dismiss();
                        if (updateLocationBean.getStatus() == 1) {
                            Preferences.setPreferenceValue(ActivityAccountsSettings.this, USERCITYKEY,   address);
                            USERCITY=address;
                            accountsSettingsBinding.tvLocationText.setText(""+USERCITY+" >");
                            showSuccessToast(ActivityAccountsSettings.this,"Update Location successfully");
                            //startActivity(new Intent(ActivityAccountsSettings.this, SuccessDialogActivity.class));
                        }else if(updateLocationBean.getStatus() == 3){
                            logout(ActivityAccountsSettings.this);
                        }else
                        {
                            showFailToast(ActivityAccountsSettings.this,  "" + updateLocationBean.getMsg());
                        }
//                        cPresenter.updateCoinList(allCurrencyList);
                    }
                });


    }


    DialogPasswordChangeBinding cancellationBinding;
    public void showCancelRequestDialog(Activity nActivity) {
      final Dialog  dialogCancelAppointment=new Dialog(nActivity,R.style.CustomAlertDialog);
        cancellationBinding = DialogPasswordChangeBinding.inflate(LayoutInflater.from(nActivity));
        dialogCancelAppointment.setContentView(cancellationBinding.getRoot());
        Window window = dialogCancelAppointment.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);
        dialogCancelAppointment.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        cancellationBinding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cancellationBinding.edtCurrentPassword.getText().toString().isEmpty())
                {
                    cancellationBinding.edtCurrentPassword.setError("Enter current password");
                    cancellationBinding.edtCurrentPassword.requestFocus();
                }else if(cancellationBinding.edtPassword.getText().toString().length()<8)
                {
                    cancellationBinding.edtPassword.setError("Password should be at least 8 characters long, must include letters, numbers and special characters");
                    cancellationBinding.edtPassword.requestFocus();
                }
                else if(!isValidPassword(cancellationBinding.edtPassword.getText().toString()))
                {
                    cancellationBinding.edtPassword.setError(getResources().getString(R.string.enter_password_with_special_characters));
                    cancellationBinding.edtPassword.requestFocus();
                }
                else if(cancellationBinding.edtConfPassword.getText().toString().isEmpty())
                {
                    cancellationBinding.edtConfPassword.setError("Enter confirm password");
                    cancellationBinding.edtConfPassword.requestFocus();
                }else if(cancellationBinding.edtConfPassword.getText().toString().length()<8)
                {
                    cancellationBinding.edtConfPassword.setError("Password doesn't match");
                    cancellationBinding.edtConfPassword.requestFocus();
                }else if(!isValidPassword(cancellationBinding.edtConfPassword.getText().toString()))
                {
                    cancellationBinding.edtConfPassword.setError("Password doesn't match");
                    cancellationBinding.edtConfPassword.requestFocus();
                }
                else if(!cancellationBinding.edtPassword.getText().toString().equals(cancellationBinding.edtConfPassword.getText().toString()))
                {
                    cancellationBinding.edtConfPassword.setError("Password doesn't match");
                    cancellationBinding.edtConfPassword.requestFocus();
                }else
                {
                    if (isNetworkAvailable(ActivityAccountsSettings.this)) {
                        getUpdatePassword();
                    } else {
                        // Toast.makeText(ActivityAccountsSettings.this, getResources().getString(R.string.please_check_newtork_connection), Toast.LENGTH_SHORT).show();
                        showNoInternetDialog(ActivityAccountsSettings.this);
                    }
                    dialogCancelAppointment.dismiss();
//                    startActivity(new Intent(ActivityAccountsSettings.this, SuccessDialogActivity.class));
                }
            }
        });
        dialogCancelAppointment.show();

    }

    UpodatePassBean loginBean;
    private void getUpdatePassword() {

        ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        final Dialog progressDialog=showProgress(ActivityAccountsSettings.this,0);
        progressDialog.show();
        service.getUpdatePassword(USERIDVAL,cancellationBinding.edtCurrentPassword.getText().toString(),cancellationBinding.edtConfPassword.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UpodatePassBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(UpodatePassBean loginBeanObj) {
                        progressDialog.dismiss();
                        loginBean = loginBeanObj;
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        showFailToast(ActivityAccountsSettings.this, getResources().getString(R.string.went_wrong));
                    }

                    @Override
                    public void onComplete() {
                        // Updates UI with data
                        progressDialog.dismiss();
                        if (loginBean.getStatus() == 1) {
                            startActivity(new Intent(ActivityAccountsSettings.this, SuccessDialogActivity.class));
                        }else if (loginBean.getStatus() == 3) {
                            showFailToast(ActivityAccountsSettings.this,  "" + loginBean.getMsg());
                            logout(ActivityAccountsSettings.this);
                        }else
                        {
                            showFailToast(ActivityAccountsSettings.this,  "" + loginBean.getMsg());
                        }
//                        cPresenter.updateCoinList(allCurrencyList);
                    }
                });


    }
}
