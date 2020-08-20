package com.prometteur.saloonuser.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.prometteur.saloonuser.Model.LoginBean;
import com.prometteur.saloonuser.Model.UpdateLocationBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.ImagePickerActivity;
import com.prometteur.saloonuser.Utils.Preferences;
import com.prometteur.saloonuser.databinding.ActivityUpdateProfileBinding;
import com.prometteur.saloonuser.retrofit.ApiInterface;
import com.prometteur.saloonuser.retrofit.RetrofitInstance;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;

import static com.prometteur.saloonuser.Constants.ConstantVariables.ISLOGIN;
import static com.prometteur.saloonuser.Constants.ConstantVariables.MOBNO;
import static com.prometteur.saloonuser.Constants.ConstantVariables.REDEEMPOINTSKEY;
import static com.prometteur.saloonuser.Constants.ConstantVariables.REFERCODEKEY;
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
import static com.prometteur.saloonuser.Constants.ConstantVariables.apiKey;
import static com.prometteur.saloonuser.Fragments.FragmentListSalonView.lat;
import static com.prometteur.saloonuser.Fragments.FragmentListSalonView.lon;
import static com.prometteur.saloonuser.Utils.Utils.getImageBody;
import static com.prometteur.saloonuser.Utils.Utils.getOtherFields;
import static com.prometteur.saloonuser.Utils.Utils.isNetworkAvailable;
import static com.prometteur.saloonuser.Utils.Utils.logout;
import static com.prometteur.saloonuser.Utils.Utils.showFailToast;
import static com.prometteur.saloonuser.Utils.Utils.showNoInternetDialog;
import static com.prometteur.saloonuser.Utils.Utils.showProgress;
import static com.prometteur.saloonuser.Utils.Utils.showSuccessToast;

public class ActivityUpdateProfile extends AppCompatActivity implements View.OnClickListener {

    public static int REQUEST_IMAGE = 121;
    ActivityUpdateProfileBinding updateProfileBinding;
    String strImagePath = "";
    LoginBean regBean;
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    UpdateLocationBean updateLocationBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateProfileBinding = ActivityUpdateProfileBinding.inflate(getLayoutInflater());
        View view = updateProfileBinding.getRoot();
        setContentView(view);

        updateProfileBinding.btnDone.setOnClickListener(this);
        updateProfileBinding.civProfileimg.setOnClickListener(this);

        updateProfileBinding.edtFirstName.setText("" + USERFNAME);
        updateProfileBinding.edtLastName.setText("" + USERLNAME);
        updateProfileBinding.edtEmail.setText("" + USEREMAIL);
        updateProfileBinding.edtMobileNum.setText("" + USERMOB);
        if (USERGENDER.equalsIgnoreCase("1")) {
            updateProfileBinding.rbMale.setChecked(true);
        } else if (USERGENDER.equalsIgnoreCase("2")){
            updateProfileBinding.rbFemale.setChecked(true);
        }else
        {
            updateProfileBinding.rbOther.setChecked(true);
        }
        if (!USERPROFILE.isEmpty()) {
            Glide.with(ActivityUpdateProfile.this).load(USERPROFILE).error(R.drawable.img_profile).into(updateProfileBinding.civProfileimg);
        }

        if (getIntent().getStringExtra("fromScreen").equalsIgnoreCase("reg")) {
            updateProfileBinding.ivBackArrowimg.setVisibility(View.GONE);
            try {
                // Initialize the SDK
                Places.initialize(getApplicationContext(), apiKey);
// Create a new Places client instance
                PlacesClient placesClient = Places.createClient(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            updateProfileBinding.ivBackArrowimg.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnDone:
                USERFNAME = updateProfileBinding.edtFirstName.getText().toString();
                USERLNAME = updateProfileBinding.edtLastName.getText().toString();
                USEREMAIL = updateProfileBinding.edtEmail.getText().toString();
                USERMOB = updateProfileBinding.edtMobileNum.getText().toString();
                if (USERFNAME.length() == 0) {

                    updateProfileBinding.edtFirstName.setError(getString(R.string.enter_first_name));
                    updateProfileBinding.edtFirstName.requestFocus();

                } else if (USERLNAME.length() == 0) {

                    updateProfileBinding.edtLastName.setError(getString(R.string.enter_last_name));
                    updateProfileBinding.edtLastName.requestFocus();

                } else {
                    if (updateProfileBinding.rgGender != null) {
                        RadioButton radioButton = findViewById(updateProfileBinding.rgGender.getCheckedRadioButtonId());
                        if (radioButton.getText().toString().equalsIgnoreCase("Male")) {
                            USERGENDER = "1";
                        } else if (radioButton.getText().toString().equalsIgnoreCase("Female")) {
                            USERGENDER = "2";
                        } else {
                            USERGENDER = "3";
                        }
                    }
                    if (isNetworkAvailable(ActivityUpdateProfile.this)) {
                        updateUser();
                    } else {
                        showNoInternetDialog(ActivityUpdateProfile.this);
                    }

                }
                break;
            case R.id.ivBackArrowimg:
                finish();
                break;
            case R.id.civProfileimg:
                onProfileImageClick();
                break;
        }
    }

    void onProfileImageClick() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions();
                        } else {
                            // TODO - handle permission denied case
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }

                   /* @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }*/  // TODO for design
                }).check();
    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(ActivityUpdateProfile.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 10); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 8);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 800);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 800);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(ActivityUpdateProfile.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 10); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 10);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    //Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    File file = new File(uri.getPath());
                    strImagePath = uri.getPath();
                    String newPath = Environment.getExternalStorageDirectory() + "/omarimg/" + file.getName();
                    Log.i("imagepath", "img path " + file.getAbsolutePath() + " \n" + file.getPath());
                    Bitmap bitmap = getBitmap(uri.getPath());
                    if (bitmap != null) {
                        updateProfileBinding.civProfileimg.setImageBitmap(bitmap);
                    }

                    /*if (imgArr.size() < 5) {
                     *//*try {
                            copyFile(file, new File(newPath));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }*//*
                        imgArr.add(uri.getPath());
                        LinearLayout insertPoint = (LinearLayout) findViewById(R.id.imageContainer);
                        insertPoint.removeAllViews();
                        for (int i = 0; i < imgArr.size(); i++) {
                            LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View v = vi.inflate(R.layout.image_picker_layout, null);
                            insertPoint = (LinearLayout) findViewById(R.id.imageContainer);
                            ImageView salonImg = (ImageView) v.findViewById(R.id.salonImg);
                            insertPoint.addView(v);
                            Bitmap bitmap = getBitmap(imgArr.get(i));
                            if (bitmap != null) {
                                salonImg.setImageBitmap(bitmap);
                            }
                        }
                        addView();
                    } else {
                        showFailToast(RegistrationActivity.this, getResources().getString(R.string.select_five_images_msg));
                    }*/
                    // loading profile image from local cache
                    //loadProfile(uri.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i("TAG", "Place: " + place.getName() + ", " + place.getId());
                lat = "" + place.getLatLng().latitude;
                lon = "" + place.getLatLng().longitude;
                if (isNetworkAvailable(ActivityUpdateProfile.this)) {
                    getUpdateLocation(place.getAddress());
                } else {
                    showNoInternetDialog(ActivityUpdateProfile.this);
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

    public Bitmap getBitmap(String filePath) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File image = new File(filePath);
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
            bitmap = Bitmap.createScaledBitmap(bitmap, 150, 120, true);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    private void updateUser() {

        ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        final Dialog progressDialog = showProgress(ActivityUpdateProfile.this, 0);
        progressDialog.show();
        MultipartBody.Part[] multiPartImg = getImageBody(strImagePath);

        service.updateUser(getOtherFields(USERIDVAL),
                getOtherFields(USERFNAME),
                getOtherFields(USERLNAME),
                getOtherFields(USERMOB),
                getOtherFields(USEREMAIL),
                getOtherFields(USERGENDER),
                multiPartImg
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LoginBean loginBeanObj) {
                        regBean = loginBeanObj;
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        showFailToast(ActivityUpdateProfile.this, getResources().getString(R.string.went_wrong));
                    }

                    @Override
                    public void onComplete() {
                        progressDialog.dismiss();
                        // Updates UI with data
                        if (regBean.getStatus() == 1) {
                            LoginBean.Result result = regBean.getResult().get(0);
                            Preferences.setPreferenceValue(ActivityUpdateProfile.this, USERID, result.getUserId());
                            Preferences.setPreferenceValue(ActivityUpdateProfile.this, MOBNO, result.getUserMbNo());
                            Preferences.setPreferenceValue(ActivityUpdateProfile.this, ISLOGIN, 1);
                            Preferences.setPreferenceValue(ActivityUpdateProfile.this, USERSESSION, result.getUserSession());
                            Preferences.setPreferenceValue(ActivityUpdateProfile.this, USERPROFILEKEY, result.getUserImg());
                            Preferences.setPreferenceValue(ActivityUpdateProfile.this, USERFNAMEKEY, result.getUserFName());
                            Preferences.setPreferenceValue(ActivityUpdateProfile.this, USERLNAMEKEY, result.getUserLName());
                            Preferences.setPreferenceValue(ActivityUpdateProfile.this, USERMOBKEY, result.getUserMbNo());
                            Preferences.setPreferenceValue(ActivityUpdateProfile.this, USEREMAILKEY, result.getUserEmail());
                            Preferences.setPreferenceValue(ActivityUpdateProfile.this, USERGENDERKEY, result.getUserGender());
                            Preferences.setPreferenceValue(ActivityUpdateProfile.this, REFERCODEKEY, result.getUserReferCode());
                            Preferences.setPreferenceValue(ActivityUpdateProfile.this, REDEEMPOINTSKEY, result.getUserRedeem());
                            //startActivity(new Intent(ActivityUpdateProfile.this, ActivityHomepage.class));

                            USERIDVAL = Preferences.getPreferenceValue(ActivityUpdateProfile.this, USERID);
                            USERFNAME = Preferences.getPreferenceValue(ActivityUpdateProfile.this, USERFNAMEKEY);
                            USERLNAME = Preferences.getPreferenceValue(ActivityUpdateProfile.this, USERLNAMEKEY);
                            USEREMAIL = Preferences.getPreferenceValue(ActivityUpdateProfile.this, USEREMAILKEY);
                            USERMOB = Preferences.getPreferenceValue(ActivityUpdateProfile.this, USERMOBKEY);
                            USERPROFILE = Preferences.getPreferenceValue(ActivityUpdateProfile.this, USERPROFILEKEY);
                            USERGENDER = Preferences.getPreferenceValue(ActivityUpdateProfile.this, USERGENDERKEY);

                            updateProfileBinding.edtFirstName.setText("" + USERFNAME);
                            updateProfileBinding.edtLastName.setText("" + USERLNAME);
                            updateProfileBinding.edtEmail.setText("" + USEREMAIL);
                            updateProfileBinding.edtMobileNum.setText("" + USERMOB);
                            if(USERGENDER.equalsIgnoreCase("1")) {
                                updateProfileBinding.rbMale.setChecked(true);
                            } else if(USERGENDER.equalsIgnoreCase("2")) {
                                updateProfileBinding.rbFemale.setChecked(true);
                            } else {
                                updateProfileBinding.rbOther.setChecked(true);
                            }
                            if (!USERPROFILE.isEmpty()) {
                                Glide.with(ActivityUpdateProfile.this).load(USERPROFILE).error(R.drawable.img_profile).into(updateProfileBinding.civProfileimg);
                            }
                            showSuccessToast(ActivityUpdateProfile.this, "Profile updated successfully");
                            //startActivityForResult(new Intent(ActivityUpdateProfile.this, SuccessDialogActivity.class).putExtra("msg","Your request has been submitted successfully, system admin will get back to you shortly"),resultCodeChangePass);
//finish();
                            if (getIntent().getStringExtra("fromScreen").equalsIgnoreCase("reg")) {
                                getStartPlaceLocation();
                            } else {
                                finish();
                            }
                        } else {
                            showFailToast(ActivityUpdateProfile.this, "" + regBean.getMsg());
                        }
//                        cPresenter.updateCoinList(allCurrencyList);
                    }
                });


    }

    private void getStartPlaceLocation() {

        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);

        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .build(ActivityUpdateProfile.this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    private void getUpdateLocation(final String address) {

        ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        final Dialog progressDialog = showProgress(ActivityUpdateProfile.this, 0);
        progressDialog.show();
        service.getUpdateLocation(USERIDVAL, lat, lon, address)
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
                        showFailToast(ActivityUpdateProfile.this, getResources().getString(R.string.went_wrong));
                    }

                    @Override
                    public void onComplete() {
                        // Updates UI with data
                        progressDialog.dismiss();
                        if (updateLocationBean.getStatus() == 1) {
                            Preferences.setPreferenceValue(ActivityUpdateProfile.this, USERCITYKEY, address);
                            USERCITY = address;
                            showSuccessToast(ActivityUpdateProfile.this, "Update Location successfully");
                            startActivity(new Intent(ActivityUpdateProfile.this, ActivityHomepage.class).putExtra("objNoti", "result"));
                            finishAffinity();
                        } else if (updateLocationBean.getStatus() == 3) {
                            logout(ActivityUpdateProfile.this);
                        } else {
                            showFailToast(ActivityUpdateProfile.this, "" + updateLocationBean.getMsg());
                        }
//                        cPresenter.updateCoinList(allCurrencyList);
                    }
                });


    }

    @Override
    public void onBackPressed() {
        if (getIntent().getStringExtra("fromScreen").equalsIgnoreCase("reg")) {
        } else {
            super.onBackPressed();
        }
        //
    }
}
