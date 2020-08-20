package com.prometteur.saloonuser.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.iid.FirebaseInstanceId;
import com.prometteur.saloonuser.Model.CheckBean;
import com.prometteur.saloonuser.Model.ForgotPassBean;
import com.prometteur.saloonuser.Model.RegBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.databinding.ActivitySignUpBinding;
import com.prometteur.saloonuser.databinding.DialogAccountCreatedBinding;
import com.prometteur.saloonuser.retrofit.ApiInterface;
import com.prometteur.saloonuser.retrofit.RetrofitInstance;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.prometteur.saloonuser.Constants.ConstantVariables.USEREMAIL;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERFNAME;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERGENDER;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERIDVAL;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERLNAME;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERMOB;
import static com.prometteur.saloonuser.Utils.Utils.isNetworkAvailable;
import static com.prometteur.saloonuser.Utils.Utils.isValidPassword;
import static com.prometteur.saloonuser.Utils.Utils.showFailToast;
import static com.prometteur.saloonuser.Utils.Utils.showNoInternetDialog;
import static com.prometteur.saloonuser.Utils.Utils.showProgress;

public class ActivitySignUp extends AppCompatActivity implements View.OnClickListener {

    ActivitySignUpBinding SignupBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        SignupBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = SignupBinding.getRoot();
        setContentView(view);
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion>=Build.VERSION_CODES.P) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        }
        SignupBinding.ivBackImg.setOnClickListener(this);
        SignupBinding.btnSignUp.setOnClickListener(this);
        SignupBinding.tvLogin.setOnClickListener(this);



    }
    final long[] mLastClickTime = {0};
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.ivBackImg:
                finish();
                break;
            case R.id.btnSignUp:
                userFName = SignupBinding.edtFirstname.getText().toString();
                userLName = SignupBinding.edtLastname.getText().toString();
                userMob = SignupBinding.edtMobilenum.getText().toString();
                userEmail = SignupBinding.edtEmail.getText().toString();
                userReferalCode = SignupBinding.edtReferralCode.getText().toString();
                userPassword = SignupBinding.edtPassword.getText().toString();
                userConPassword = SignupBinding.edtConfirmPassword.getText().toString();
                RadioButton radioButtonCustomFont=findViewById(SignupBinding.rgGender.getCheckedRadioButtonId());
                if(radioButtonCustomFont!=null) {
                    if(radioButtonCustomFont.getText().toString().equalsIgnoreCase("Male")) {
                        userGender="1";
                    }else if(radioButtonCustomFont.getText().toString().equalsIgnoreCase("Female")) {
                        userGender="2";
                    }else {
                        userGender="3";
                    }
                }
                if (isValidData()) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime[0] < 3000) {
                        return;
                    }
                    mLastClickTime[0] = SystemClock.elapsedRealtime();
                    if (isNetworkAvailable(ActivitySignUp.this)) {
                        /*startActivity(new Intent(ActivitySignUp.this, ActivityOtpVerification.class).putExtra("otp","123456").putExtra("mobNumber",userMob).putExtra("email",userEmail).putExtra("from","signUp"));
                        finish();*/
                        USERFNAME =userFName;
                        USERLNAME =userLName;
                        USEREMAIL =userMob;
                        USERMOB = userEmail;
                        USERGENDER =userGender;
                        if (isNetworkAvailable(ActivitySignUp.this)) {
                            getVerifyUser();
                        } else {
                            showNoInternetDialog(ActivitySignUp.this);
                        }



                    } else {
                        // Toast.makeText(ActivityLogin.this, getResources().getString(R.string.please_check_newtork_connection), Toast.LENGTH_SHORT).show();

                        showNoInternetDialog(ActivitySignUp.this);
                    }

                }

                break;
            case R.id.tvLogin:
                startActivity(new Intent(this, ActivityLogin.class));
                break;
        }
    }


    RegBean loginBean;
    private String userFName;
    private String userLName;
    private String userMob;
    private String userEmail;
    private String userPassword;
    private String userReferalCode;
    private String userConPassword;
    private String userGender="";
    private boolean isValidData() {

        boolean isValidData = true;

        try {


            if (userFName.length() == 0) {

                SignupBinding.edtFirstname.setError(getString(R.string.enter_first_name));
                SignupBinding.edtFirstname.requestFocus();
                isValidData = false;

            } else if (userLName.length() == 0) {

                SignupBinding.edtLastname.setError(getString(R.string.enter_last_name));
                SignupBinding.edtLastname.requestFocus();
                isValidData = false;

            }else if (userMob.length() == 0) {

                SignupBinding.edtMobilenum.setError(getString(R.string.enter_mobile_number));
                SignupBinding.edtMobilenum.requestFocus();
                isValidData = false;

            }else if (userMob.length() != 10) {

                SignupBinding.edtMobilenum.setError(getString(R.string.enter_valid_mobile_number));
                SignupBinding.edtMobilenum.requestFocus();
                isValidData = false;

            } else if (userEmail.length() == 0) {

                SignupBinding.edtEmail.setError(getResources().getString(R.string.enter_username));
                SignupBinding.edtEmail.requestFocus();
                isValidData = false;

            } else if (!(Patterns.EMAIL_ADDRESS.matcher(userEmail).matches())) {

                SignupBinding.edtEmail.setError(getResources().getString(R.string.enter_valid_username));
                SignupBinding.edtEmail.requestFocus();
                isValidData = false;

            } else
            if(SignupBinding.edtPassword.getText().toString().length()<8)
            {
                SignupBinding.edtPassword.setError("Password should be at least 8 characters long, must include letters, numbers and special characters");
                SignupBinding.edtPassword.requestFocus();
                isValidData = false;
            }
            else if(!isValidPassword(SignupBinding.edtPassword.getText().toString()))
            {
                SignupBinding.edtPassword.setError(getResources().getString(R.string.enter_password_with_special_characters));
                SignupBinding.edtPassword.requestFocus();
                isValidData = false;
            }
            else if(SignupBinding.edtConfirmPassword.getText().toString().isEmpty())
            {
                SignupBinding.edtConfirmPassword.setError("Enter confirm password");
                SignupBinding.edtConfirmPassword.requestFocus();
                isValidData = false;
            }else if(SignupBinding.edtConfirmPassword.getText().toString().length()<8)
            {
                SignupBinding.edtConfirmPassword.setError("Password doesn't match");
                SignupBinding.edtConfirmPassword.requestFocus();
                isValidData = false;
            }else if(!isValidPassword(SignupBinding.edtConfirmPassword.getText().toString()))
            {
                SignupBinding.edtConfirmPassword.setError("Password doesn't match");
                SignupBinding.edtConfirmPassword.requestFocus();
                isValidData = false;
            }
            else if(!SignupBinding.edtPassword.getText().toString().equals(SignupBinding.edtConfirmPassword.getText().toString()))
            {
                SignupBinding.edtConfirmPassword.setError("Password doesn't match");
                SignupBinding.edtConfirmPassword.requestFocus();
                isValidData = false;
            }else if (userGender.length() == 0) {

                showFailToast(ActivitySignUp.this,  "Select Gender");

                isValidData = false;

            }/*else if (!isValidPassword(userPassword)) {

                edtPassword.setError(getResources().getString(R.string.enter_password_with_special_characters));
                edtPassword.requestFocus();

                isValidData = false;
            }*/


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isValidData;
    }

    private void getRegister() {

        ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        final Dialog progressDialog = showProgress(ActivitySignUp.this, 0);
        progressDialog.show();
        String userFcmKey= FirebaseInstanceId.getInstance().getToken();
        Log.i("FCM Key",userFcmKey);
        service.getRegister(userFName,userLName,userMob,userEmail, userPassword,userReferalCode,userGender,userFcmKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RegBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(RegBean loginBeanObj) {
                        progressDialog.dismiss();
                        loginBean = loginBeanObj;
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        showFailToast(ActivitySignUp.this, getResources().getString(R.string.went_wrong));
                    }

                    @Override
                    public void onComplete() {
                        // Updates UI with data
                        progressDialog.dismiss();
                        if (loginBean.getStatus() == 1) {
                            USERIDVAL=""+loginBean.getResult().get(0);
                            USERFNAME =userFName;
                            USERLNAME =userLName;
                            USERMOB =userMob;
                            USEREMAIL = userEmail;
                            USERGENDER =userGender;
                            //getForgotPass();
                            showCongratsDialog(ActivitySignUp.this);
                            //finish();
                        } else {
                            showFailToast(ActivitySignUp.this,  "" + loginBean.getMsg());
                        }
//                        cPresenter.updateCoinList(allCurrencyList);
                    }
                });


    }

    ForgotPassBean verifyUser;
    private void getVerifyUser() {

        ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        final Dialog progressDialog=showProgress(ActivitySignUp.this,0);
        progressDialog.show();
        service.getVerifyUser(userEmail,userMob,"")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ForgotPassBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ForgotPassBean loginBeanObj) {
                        progressDialog.dismiss();
                        verifyUser = loginBeanObj;
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        showFailToast(ActivitySignUp.this,  getResources().getString(R.string.went_wrong));
                    }

                    @Override
                    public void onComplete() {
                        // Updates UI with data
                        progressDialog.dismiss();
                        if (verifyUser.getStatus() == 1) {
                            if (userReferalCode.isEmpty()) {
                                if (isNetworkAvailable(ActivitySignUp.this)) {
                                    getForgotPass();
                                } else {
                                    showNoInternetDialog(ActivitySignUp.this);
                                }
                            } else {
                                if (isNetworkAvailable(ActivitySignUp.this)) {
                                    getVerifyUserReferral();
                                } else {
                                    showNoInternetDialog(ActivitySignUp.this);
                                }

                            }
                            //finish();
                        }else
                        {
                            showFailToast(ActivitySignUp.this,verifyUser.getMsg());
                        }
                    }
                });
    }

    ForgotPassBean verifyUserReferral;
    private void getVerifyUserReferral() {

        ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        final Dialog progressDialog=showProgress(ActivitySignUp.this,0);
        progressDialog.show();
        service.getVerifyUser("","",userReferalCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ForgotPassBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ForgotPassBean loginBeanObj) {
                        progressDialog.dismiss();
                        verifyUserReferral = loginBeanObj;
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        showFailToast(ActivitySignUp.this,  getResources().getString(R.string.went_wrong));
                    }

                    @Override
                    public void onComplete() {
                        // Updates UI with data
                        progressDialog.dismiss();
                        if (verifyUserReferral.getStatus() == 1) {
                            if (isNetworkAvailable(ActivitySignUp.this)) {
                                getForgotPass();
                            } else {
                                showNoInternetDialog(ActivitySignUp.this);
                            }
                            //finish();
                        }else
                        {
                            showFailToast(ActivitySignUp.this,verifyUserReferral.getMsg());
                        }
                    }
                });
    }


    CheckBean forgotPassBean;
    private void getForgotPass() {

        ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        final Dialog progressDialog=showProgress(ActivitySignUp.this,0);
        progressDialog.show();
        service.getSendOtp(userMob)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CheckBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CheckBean loginBeanObj) {
                        progressDialog.dismiss();
                        forgotPassBean = loginBeanObj;
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        showFailToast(ActivitySignUp.this,  getResources().getString(R.string.went_wrong));
                    }

                    @Override
                    public void onComplete() {
                        // Updates UI with data
                        progressDialog.dismiss();
                        if (forgotPassBean.getStatus() == 1) {
                            startActivityForResult(new Intent(ActivitySignUp.this, ActivityOtpVerification.class).putExtra("otp",""+forgotPassBean.getResult().getOtp()).putExtra("mobNumber",userMob).putExtra("email",userEmail).putExtra("from","signUp"),SIGNUPREQUEST);
                            //finish();
                        }
                    }
                });
    }

    public static int SIGNUPREQUEST=1212;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==SIGNUPREQUEST)
        {
            if (isValidData()) {

                if (isNetworkAvailable(ActivitySignUp.this)) {
                        /*startActivity(new Intent(ActivitySignUp.this, ActivityOtpVerification.class).putExtra("otp","123456").putExtra("mobNumber",userMob).putExtra("email",userEmail).putExtra("from","signUp"));
                        finish();*/
                    getRegister();
                } else {
                    // Toast.makeText(ActivityLogin.this, getResources().getString(R.string.please_check_newtork_connection), Toast.LENGTH_SHORT).show();

                    showNoInternetDialog(ActivitySignUp.this);
                }

            }
        }
    }

    Dialog accountCreated;
    DialogAccountCreatedBinding dialogBinding;
    private void showCongratsDialog(final Context nContext) {
        accountCreated = new Dialog(nContext, R.style.CustomAlertDialog);
        dialogBinding = DialogAccountCreatedBinding.inflate(LayoutInflater.from(nContext));
        accountCreated.setContentView(dialogBinding.getRoot());
        Window window = accountCreated.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);
        accountCreated.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        dialogBinding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accountCreated.dismiss();

                startActivity(new Intent(nContext, ActivityUpdateProfile.class).putExtra("fromScreen","reg"));
                finishAffinity();
            }
        });
        accountCreated.show();

    }
}