package com.prometteur.saloonuser.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.prometteur.saloonuser.Model.LoginBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.Preferences;
import com.prometteur.saloonuser.databinding.ActivityLoginBinding;
import com.prometteur.saloonuser.firebase.MyFirebaseMessagingService;
import com.prometteur.saloonuser.retrofit.ApiInterface;
import com.prometteur.saloonuser.retrofit.RetrofitInstance;


import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.menuPos;
import static com.prometteur.saloonuser.Constants.ConstantVariables.ISLOGIN;
import static com.prometteur.saloonuser.Constants.ConstantVariables.MOBNO;
import static com.prometteur.saloonuser.Constants.ConstantVariables.REDEEMPOINTSKEY;
import static com.prometteur.saloonuser.Constants.ConstantVariables.REFERCODEKEY;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERCITYKEY;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USEREMAILKEY;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERFNAMEKEY;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERGENDERKEY;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERID;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERLNAMEKEY;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERMOBKEY;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERPROFILE;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERPROFILEKEY;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERSESSION;
import static com.prometteur.saloonuser.Utils.Utils.isNetworkAvailable;
import static com.prometteur.saloonuser.Utils.Utils.showFailToast;
import static com.prometteur.saloonuser.Utils.Utils.showNoInternetDialog;
import static com.prometteur.saloonuser.Utils.Utils.showProgress;
import static com.prometteur.saloonuser.Utils.Utils.showSuccessToast;

public class ActivityLogin extends AppCompatActivity implements View.OnClickListener {

    private ActivityLoginBinding activityLoginBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = activityLoginBinding.getRoot();
        setContentView(view);
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion>= Build.VERSION_CODES.P) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        }
        activityLoginBinding.btnLogin.setOnClickListener(this);
        activityLoginBinding.tvForgotPassword.setOnClickListener(this);
        activityLoginBinding.tvExpressInterest.setOnClickListener(this);
String appId="null",notiId="null";
        if (Preferences.getPreferenceValueInt(ActivityLogin.this, ISLOGIN) == 1) {
            Bundle bundle=getIntent().getExtras();
            if(bundle!=null) { //for out of app notification
                Log.i("Notification", ""+bundle.get("message"));
                appId=""+bundle.get("noti_appointment_id");
                notiId=""+bundle.get("noti_id");

            }
                menuPos=0;
            if(appId.equalsIgnoreCase("null"))
            {
                appId=""+getIntent().getStringExtra("noti_appointment_id");
            }
            if(notiId.equalsIgnoreCase("null"))
            {
                notiId="";
            }
                startActivity(new Intent(ActivityLogin.this, ActivityHomepage.class).putExtra("objNoti", "result").putExtra("appId", appId).putExtra("notiId", notiId));
                finishAffinity();

        }
        FirebaseApp.initializeApp(this);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnLogin:
                userName = activityLoginBinding.edtUsername.getText().toString();
                userPassword = activityLoginBinding.edtPassword.getText().toString();
                if (isValidData()) {

                    if (isNetworkAvailable(ActivityLogin.this)) {
                        getLogIn();
                    } else {
                        // Toast.makeText(ActivityLogin.this, getResources().getString(R.string.please_check_newtork_connection), Toast.LENGTH_SHORT).show();

                        showNoInternetDialog(ActivityLogin.this);
                    }

                }

                break;
            case R.id.tvForgotPassword:
                startActivity(new Intent(this, ForgotPasswordActivity.class));
                break;
            case R.id.tvExpressInterest:
                startActivity(new Intent(this, ActivitySignUp.class));
        }
    }

    LoginBean loginBean;
    private String userName;
    private String userPassword;
    private boolean isValidData() {

        boolean isValidData = true;

        try {


            if (userName.length() == 0) {

                activityLoginBinding.edtUsername.setError(getResources().getString(R.string.enter_username));
                activityLoginBinding.edtUsername.requestFocus();
                isValidData = false;

            } else if (!(Patterns.EMAIL_ADDRESS.matcher(userName).matches())) {

                activityLoginBinding.edtUsername.setError(getResources().getString(R.string.enter_valid_username));
                activityLoginBinding.edtUsername.requestFocus();
                isValidData = false;

            } else if (userPassword.length() == 0) {

                activityLoginBinding.edtPassword.setError(getResources().getString(R.string.enter_password));
                activityLoginBinding.edtPassword.requestFocus();

                isValidData = false;

            } /*else if (!isValidPassword(userPassword)) {

                edtPassword.setError(getResources().getString(R.string.enter_password_with_special_characters));
                edtPassword.requestFocus();

                isValidData = false;
            }*/


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isValidData;
    }

    private void getLogIn() {

        ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        final Dialog progressDialog = showProgress(ActivityLogin.this, 0);
        progressDialog.show();
        String userFcmKey= FirebaseInstanceId.getInstance().getToken();
        if(userFcmKey!=null) {
            Log.i("FCM Key", userFcmKey);
        }else
        {
            userFcmKey="";
        }
        service.getLogin(userName, userPassword,userFcmKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LoginBean loginBeanObj) {
                        progressDialog.dismiss();
                        loginBean = loginBeanObj;
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                       // showFailToast(ActivityLogin.this, getResources().getString(R.string.went_wrong));
                    }

                    @Override
                    public void onComplete() {
                        // Updates UI with data
                        progressDialog.dismiss();
                        if (loginBean.getStatus() == 1) {
                            LoginBean.Result result = loginBean.getResult().get(0);
                            if (result.getUserStatus().equalsIgnoreCase("0")) {
                                showFailToast(ActivityLogin.this, "Your account has been disabled by admin");
                            } else {
                                Preferences.setPreferenceValue(ActivityLogin.this, USERID,          result.getUserId());
                                Preferences.setPreferenceValue(ActivityLogin.this, MOBNO,           result.getUserMbNo());
                                Preferences.setPreferenceValue(ActivityLogin.this, ISLOGIN,         1);
                                Preferences.setPreferenceValue(ActivityLogin.this, USERSESSION,     result.getUserSession());
                                Preferences.setPreferenceValue(ActivityLogin.this, USERPROFILEKEY,  result.getUserImg());
                                Preferences.setPreferenceValue(ActivityLogin.this, USERFNAMEKEY,    result.getUserFName());
                                Preferences.setPreferenceValue(ActivityLogin.this, USERLNAMEKEY,    result.getUserLName());
                                Preferences.setPreferenceValue(ActivityLogin.this, USERMOBKEY,      result.getUserMbNo());
                                Preferences.setPreferenceValue(ActivityLogin.this, USEREMAILKEY,    result.getUserEmail());
                                Preferences.setPreferenceValue(ActivityLogin.this, USERGENDERKEY,   result.getUserGender());
                                Preferences.setPreferenceValue(ActivityLogin.this, USERCITYKEY,   ""+result.getUserAddress());
                                Preferences.setPreferenceValue(ActivityLogin.this, REFERCODEKEY,   ""+result.getUserReferCode());
                                Preferences.setPreferenceValue(ActivityLogin.this, REDEEMPOINTSKEY,result.getUserRedeem());
                                startActivity(new Intent(ActivityLogin.this, ActivityHomepage.class).putExtra("objNoti", (Bundle) null));
                                finish();
                            }
                        } else {
                            showFailToast(ActivityLogin.this,  "" + loginBean.getMsg());
                        }
//                        cPresenter.updateCoinList(allCurrencyList);
                    }
                });


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
}
