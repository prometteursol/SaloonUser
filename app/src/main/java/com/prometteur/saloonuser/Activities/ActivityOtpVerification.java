package com.prometteur.saloonuser.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.prometteur.saloonuser.Model.CheckBean;
import com.prometteur.saloonuser.Model.ForgotPassBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.databinding.ActivityOtpVerificationBinding;
import com.prometteur.saloonuser.databinding.ActivitySignUpBinding;
import com.prometteur.saloonuser.databinding.DialogAccountCreatedBinding;
import com.prometteur.saloonuser.retrofit.ApiInterface;
import com.prometteur.saloonuser.retrofit.RetrofitInstance;

import in.aabhasjindal.otptextview.OTPListener;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.prometteur.saloonuser.Activities.ActivitySignUp.SIGNUPREQUEST;
import static com.prometteur.saloonuser.Utils.Utils.isNetworkAvailable;
import static com.prometteur.saloonuser.Utils.Utils.showFailToast;
import static com.prometteur.saloonuser.Utils.Utils.showNoInternetDialog;
import static com.prometteur.saloonuser.Utils.Utils.showProgress;
import static com.prometteur.saloonuser.Utils.Utils.showSuccessToast;

public class ActivityOtpVerification extends AppCompatActivity {

    ActivityOtpVerificationBinding otpBinding;

    Context mContext;

    String apiOTP="0";
    String strOtp,strEmailOrMobNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        otpBinding = ActivityOtpVerificationBinding.inflate(getLayoutInflater());
        View view = otpBinding.getRoot();
        setContentView(view);
        mContext=this;



        if(getIntent().getStringExtra("from").equalsIgnoreCase("signUp")){
            otpBinding.tvEmailOrMobNoText.setText("Please verify your mobile number to continue, OTP sent on Mobile Number*");
            strEmailOrMobNo=getIntent().getStringExtra("mobNumber");
            String strFirst=strEmailOrMobNo.substring(0,2);
            String strSecond=strEmailOrMobNo.substring(8,10);
            otpBinding.tvEmailOrMobNo.setText(strFirst+"XXXXXX"+strSecond);
        }else
        {
            if(getIntent().getStringExtra("email").contains("@"))
            {
                otpBinding.tvEmailOrMobNoText.setText(getResources().getString(R.string.please_verify_your_email_to_continue_n_otp_sent_on_email));
                strEmailOrMobNo=getIntent().getStringExtra("email");
                otpBinding.tvEmailOrMobNo.setText(strEmailOrMobNo);
            }else
            {
                otpBinding.tvEmailOrMobNoText.setText(getResources().getString(R.string.please_verify_your_mobile_to_continue_n_otp_sent_on_mobile_number));
                strEmailOrMobNo=getIntent().getStringExtra("mobNumber");
                otpBinding.tvEmailOrMobNo.setText(strEmailOrMobNo);
            }
            //getForgotPass();
            apiOTP=getIntent().getStringExtra("otp");
        }



        otpBinding.otpTextView.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                // fired when user types something in the Otpbox
            }
            @Override
            public void onOTPComplete(String otp) {
                // fired when user has entered the OTP fully.
                strOtp=otp;
                //Toast.makeText(OtpVerificationActivity.this, "The OTP is " + otp,  Toast.LENGTH_SHORT).show();

            }
        });

        apiOTP=getIntent().getStringExtra("otp");
        otpBinding.btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(apiOTP.equalsIgnoreCase(strOtp))
                {
                    if(getIntent().getStringExtra("from").equalsIgnoreCase("forPass")){
                        startActivity(new Intent(mContext,ChangePasswordActivity.class).putExtra("email",getIntent().getStringExtra("email")).putExtra("mobNumber",getIntent().getStringExtra("mobNumber")));
                        finish();
                    }
                    else {
                        setResult(SIGNUPREQUEST);
                        finish();
                    }

                }else{
                    showFailToast(mContext, "OTP is incorrect");
                }

//                startActivity(new Intent(OtpVerificationActivity.this,ChangePasswordActivity.class).putExtra("email",getIntent().getStringExtra("email")).putExtra("mobNumber",getIntent().getStringExtra("mobNumber")));
//                finish();
            }
        });


        otpBinding.tvResend.setEnabled(false);

        new CountDownTimer(180000, 1000) {

            public void onTick(long millisUntilFinished) {

                otpBinding.tvResend.setText("Resend OTP (" + millisUntilFinished / 1000 + ")");
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                apiOTP="0";
                otpBinding.tvResend.setEnabled(true);
                otpBinding.tvResend.setText("Resend OTP ");
            }

        }.start();

        otpBinding.tvResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otpBinding.tvResend.setEnabled(false);
                otpBinding.otpTextView.setOTP("");
                if(getIntent().getStringExtra("from").equalsIgnoreCase("signUp")){
                    if (isNetworkAvailable(ActivityOtpVerification.this)) {
                    getRegOtp();
                    } else {
                        showNoInternetDialog(ActivityOtpVerification.this);
                    }
                }else {
                    if (isNetworkAvailable(ActivityOtpVerification.this)) {
                        getForgotPass();
                    } else {
                        showNoInternetDialog(ActivityOtpVerification.this);
                    }

                }
                new CountDownTimer(180000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        otpBinding.tvResend.setText("Resend OTP (" + millisUntilFinished / 1000 + ")");

                    }

                    public void onFinish() {
                        otpBinding.tvResend.setEnabled(true);
                        otpBinding.tvResend.setText("Resend OTP ");
                    }

                }.start();

                if(getIntent().getStringExtra("from").equalsIgnoreCase("signUp")) {
                    showSuccessToast(mContext, "OTP is sent on " + getIntent().getStringExtra("mobNumber"));
                }else{
                    if (!getIntent().getStringExtra("email").isEmpty()) {
                        showSuccessToast(mContext, "OTP is sent on " + getIntent().getStringExtra("email"));
                    } else {
                        showSuccessToast(mContext, "OTP is sent on " + getIntent().getStringExtra("mobNumber"));
                    }
                }

            }
        });

    }




    ForgotPassBean loginBean;
    private void getForgotPass() {
        String email=getIntent().getStringExtra("email");
        if(getIntent().getStringExtra("from").equalsIgnoreCase("signUp")){
            email="";
        }
        ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        final Dialog progressDialog=showProgress(mContext,0);
        progressDialog.show();
        service.getForgotPass(email,getIntent().getStringExtra("mobNumber"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ForgotPassBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ForgotPassBean loginBeanObj) {
                        progressDialog.dismiss();
                        loginBean = loginBeanObj;
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        showFailToast(mContext,  getResources().getString(R.string.went_wrong));
                    }

                    @Override
                    public void onComplete() {
                        // Updates UI with data
                        progressDialog.dismiss();
                        if (loginBean.getStatus() == 1) {
                            apiOTP=""+loginBean.getResult().get(0).getOtp();
                        }else
                        {
                            showFailToast(mContext,  "" + loginBean.getMsg());
                        }
                    }
                });
    }


    CheckBean forgotPassBean;
    private void getRegOtp() {

        ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        final Dialog progressDialog=showProgress(ActivityOtpVerification.this,0);
        progressDialog.show();
        service.getSendOtp(getIntent().getStringExtra("mobNumber"))
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
                        showFailToast(ActivityOtpVerification.this,  getResources().getString(R.string.went_wrong));
                    }

                    @Override
                    public void onComplete() {
                        // Updates UI with data
                        progressDialog.dismiss();
                        if (forgotPassBean.getStatus() == 1) {
                            apiOTP=""+""+forgotPassBean.getResult().getOtp();
                            //startActivityForResult(new Intent(ActivityOtpVerification.this, ActivityOtpVerification.class).putExtra("otp",""+forgotPassBean.getResult().getOtp()).putExtra("mobNumber",userMob).putExtra("email",userEmail).putExtra("from","signUp"),SIGNUPREQUEST);
                            //finish();
                        }
                    }
                });
    }
}
