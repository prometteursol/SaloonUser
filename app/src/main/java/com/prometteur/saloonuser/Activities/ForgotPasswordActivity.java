package com.prometteur.saloonuser.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.prometteur.saloonuser.Model.ForgotPassBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.databinding.ActivityForgotPasswordBinding;
import com.prometteur.saloonuser.retrofit.ApiInterface;
import com.prometteur.saloonuser.retrofit.RetrofitInstance;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.prometteur.saloonuser.Utils.Utils.isNetworkAvailable;
import static com.prometteur.saloonuser.Utils.Utils.showFailToast;
import static com.prometteur.saloonuser.Utils.Utils.showNoInternetDialog;
import static com.prometteur.saloonuser.Utils.Utils.showProgress;
import static com.prometteur.saloonuser.Utils.Utils.showSuccessToast;


public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

   ActivityForgotPasswordBinding activityForgotPasswordBinding;
    String userEmail="",userMob="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activityForgotPasswordBinding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        View view = activityForgotPasswordBinding.getRoot();
        setContentView(view);
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion>=Build.VERSION_CODES.P) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        }
        setListeners();

    }

    private void setListeners() {
        activityForgotPasswordBinding.btnRequestOTP.setOnClickListener(this);
        activityForgotPasswordBinding.ivBackArrowimg.setOnClickListener(this);
    }

String inputType="";
    final long[] mLastClickTime = {0};
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRequestOTP:

                  if (SystemClock.elapsedRealtime() - mLastClickTime[0] < 5000) {
                      return;
                  }
                  mLastClickTime[0] = SystemClock.elapsedRealtime();
                userEmail="";
                userMob="";
                if(!activityForgotPasswordBinding.edtEmail.getText().toString().isEmpty() || !activityForgotPasswordBinding.edtMobileNo.getText().toString().isEmpty()) {
                    if (!activityForgotPasswordBinding.edtEmail.getText().toString().isEmpty()) {
                        if (activityForgotPasswordBinding.edtEmail.getText().toString().length() < 9) {
                            activityForgotPasswordBinding.edtEmail.setError(getResources().getString(R.string.please_enter_registered_email_id));
                            activityForgotPasswordBinding.edtEmail.requestFocus();
                        } else if (!Patterns.EMAIL_ADDRESS.matcher(activityForgotPasswordBinding.edtEmail.getText().toString()).matches()) {
                            activityForgotPasswordBinding.edtEmail.setError(getResources().getString(R.string.please_enter_valid_registered_email_id));
                            activityForgotPasswordBinding.edtEmail.requestFocus();
                        } else {
                            userEmail = activityForgotPasswordBinding.edtEmail.getText().toString();
                            if (isNetworkAvailable(ForgotPasswordActivity.this)) {
                                inputType = "1";
                                getForgotPass();
                            } else {
                                // Toast.makeText(ForgotPasswordActivity.this, getResources().getString(R.string.please_check_newtork_connection), Toast.LENGTH_SHORT).show();
                                showNoInternetDialog(ForgotPasswordActivity.this);
                            }
                        }
                    }else if (!activityForgotPasswordBinding.edtMobileNo.getText().toString().isEmpty()) {
                        if (activityForgotPasswordBinding.edtMobileNo.getText().toString().length() < 10) {
                            activityForgotPasswordBinding.edtMobileNo.setError(getResources().getString(R.string.please_enter_registered_mobile));
                            activityForgotPasswordBinding.edtMobileNo.requestFocus();
                        } else {
                            userMob = activityForgotPasswordBinding.edtMobileNo.getText().toString();
                            if (isNetworkAvailable(ForgotPasswordActivity.this)) {
                                inputType = "2";
                                getForgotPass();
                            } else {
                                showNoInternetDialog(ForgotPasswordActivity.this);
                                //Toast.makeText(ForgotPasswordActivity.this, getResources().getString(R.string.please_check_newtork_connection), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }else {
                    showFailToast(ForgotPasswordActivity.this, "Please enter Registered Email or Mobile number");
                }
                /*startActivity(new Intent(ForgotPasswordActivity.this, OtpVerificationActivity.class).putExtra("otp","1234").putExtra("mobNumber",userMob).putExtra("email",userEmail));
                finish();*/

                break;
            case R.id.ivBackArrowimg:
                finish();
                break;
        }
    }

    ForgotPassBean loginBean;
    private void getForgotPass() {

        ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        final Dialog progressDialog=showProgress(ForgotPasswordActivity.this,0);
        progressDialog.show();
        service.getForgotPass(userEmail,userMob)
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
                        //showFailToast(ForgotPasswordActivity.this,  getResources().getString(R.string.went_wrong));
                    }

                    @Override
                    public void onComplete() {
                        // Updates UI with data
                        progressDialog.dismiss();
                        if (loginBean.getStatus() == 1) {
                            if(!userEmail.isEmpty())
                            {
                                showSuccessToast(ForgotPasswordActivity.this,"OTP sent on Email");
                            }else if(!userMob.isEmpty())
                            {
                                showSuccessToast(ForgotPasswordActivity.this,"OTP sent on Mobile number");
                            }
                            //showSuccessToast(ForgotPasswordActivity.this,""+loginBean.getMsg());
                            startActivity(new Intent(ForgotPasswordActivity.this, ActivityOtpVerification.class).putExtra("otp",""+loginBean.getResult().get(0).getOtp()).putExtra("mobNumber",userMob).putExtra("email",userEmail).putExtra("from","forPass"));
                            //finish();
                        }else
                        {
                            if(inputType.equalsIgnoreCase("1")){
                                if(loginBean.getMsg().contains("Email Not"))
                                {
                                    activityForgotPasswordBinding.edtEmail.setError(getResources().getString(R.string.please_enter_registered_email_id));
                                    activityForgotPasswordBinding.edtEmail.requestFocus();
                                }else{
                                    showFailToast(ForgotPasswordActivity.this,  "" + loginBean.getMsg());
                                }
                            }else if(inputType.equalsIgnoreCase("2"))
                            {
                                if(loginBean.getMsg().contains("Mobile No Not"))
                                {
                                    activityForgotPasswordBinding.edtMobileNo.setError(getResources().getString(R.string.please_enter_registered_mobile));
                                    activityForgotPasswordBinding.edtMobileNo.requestFocus();
                                } else if(loginBean.getMsg().contains("Email Not"))
                                {
                                    activityForgotPasswordBinding.edtEmail.setError(getResources().getString(R.string.please_enter_registered_email_id));
                                    activityForgotPasswordBinding.edtEmail.requestFocus();
                                }else{
                                    showFailToast(ForgotPasswordActivity.this,  "" + loginBean.getMsg());
                                }
                            }

                        }
                    }
                });
    }
}
