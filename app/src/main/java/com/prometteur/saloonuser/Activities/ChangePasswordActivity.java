package com.prometteur.saloonuser.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.prometteur.saloonuser.Model.LoginBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.NetworkChangeReceiver;
import com.prometteur.saloonuser.databinding.ActivityChangePasswordBinding;
import com.prometteur.saloonuser.databinding.ActivityForgotPasswordBinding;
import com.prometteur.saloonuser.databinding.DialogAccountCreatedBinding;
import com.prometteur.saloonuser.databinding.DialogPasswordChangedBinding;
import com.prometteur.saloonuser.retrofit.ApiInterface;
import com.prometteur.saloonuser.retrofit.RetrofitInstance;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.prometteur.saloonuser.Utils.Utils.isNetworkAvailable;
import static com.prometteur.saloonuser.Utils.Utils.isValidPassword;
import static com.prometteur.saloonuser.Utils.Utils.showFailToast;
import static com.prometteur.saloonuser.Utils.Utils.showNoInternetDialog;
import static com.prometteur.saloonuser.Utils.Utils.showProgress;


public class ChangePasswordActivity extends AppCompatActivity {

   ActivityChangePasswordBinding activityChangePasswordBinding;
    final long[] mLastClickTime = {0};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activityChangePasswordBinding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        View view = activityChangePasswordBinding.getRoot();
        setContentView(view);
        int currentapiVersion = Build.VERSION.SDK_INT;
        if(currentapiVersion>=Build.VERSION_CODES.P)
        {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        }

        activityChangePasswordBinding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(activityChangePasswordBinding.edtPassword.getText().toString().length()<8)
                {
                    activityChangePasswordBinding.edtPassword.setError("Password should be at least 8 characters long, must include letters, numbers and special characters");
                    activityChangePasswordBinding.edtPassword.requestFocus();
                }
                else if(!isValidPassword(activityChangePasswordBinding.edtPassword.getText().toString()))
                {
                    activityChangePasswordBinding.edtPassword.setError(getResources().getString(R.string.enter_password_with_special_characters));
                    activityChangePasswordBinding.edtPassword.requestFocus();
                }
                else if(activityChangePasswordBinding.edtConfPassword.getText().toString().isEmpty())
                {
                    activityChangePasswordBinding.edtConfPassword.setError("Enter confirm password");
                    activityChangePasswordBinding.edtConfPassword.requestFocus();
                }else if(activityChangePasswordBinding.edtConfPassword.getText().toString().length()<8)
                {
                    activityChangePasswordBinding.edtConfPassword.setError("Password doesn't match");
                    activityChangePasswordBinding.edtConfPassword.requestFocus();
                }else if(!isValidPassword(activityChangePasswordBinding.edtConfPassword.getText().toString()))
                {
                    activityChangePasswordBinding.edtConfPassword.setError("Password doesn't match");
                    activityChangePasswordBinding.edtConfPassword.requestFocus();
                }
                else if(!activityChangePasswordBinding.edtPassword.getText().toString().equals(activityChangePasswordBinding.edtConfPassword.getText().toString()))
                {
                    activityChangePasswordBinding.edtConfPassword.setError("Password doesn't match");
                    activityChangePasswordBinding.edtConfPassword.requestFocus();
                }else
                {
                    if (SystemClock.elapsedRealtime() - mLastClickTime[0] < 3000) {
                        return;
                    }
                    mLastClickTime[0] = SystemClock.elapsedRealtime();
                    if (isNetworkAvailable(ChangePasswordActivity.this)) {
                        getChangePass();
                    } else {
                       // Toast.makeText(ChangePasswordActivity.this, getResources().getString(R.string.please_check_newtork_connection), Toast.LENGTH_SHORT).show();
                        showNoInternetDialog(ChangePasswordActivity.this);
                    }
//                    startActivity(new Intent(ChangePasswordActivity.this, SuccessDialogActivity.class));
                }
            }
        });
    }


    LoginBean loginBean;
    private void getChangePass() {

        ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        final Dialog progressDialog=showProgress(ChangePasswordActivity.this,0);
        progressDialog.show();
        service.getChangePass(getIntent().getStringExtra("email"),getIntent().getStringExtra("mobNumber"), activityChangePasswordBinding.edtPassword.getText().toString())
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
                      //  showFailToast(ChangePasswordActivity.this, getResources().getString(R.string.went_wrong));
                    }

                    @Override
                    public void onComplete() {
                        // Updates UI with data
                        progressDialog.dismiss();
                        if (loginBean.getStatus() == 1) {
                            startActivityForResult(new Intent(ChangePasswordActivity.this, SuccessDialogActivity.class),resultCodeChangePass);
                        }else
                        {
                            showFailToast(ChangePasswordActivity.this,  "" + loginBean.getMsg());
                        }
//                        cPresenter.updateCoinList(allCurrencyList);
                    }
                });


    }

    public static int resultCodeChangePass=103;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCodeChangePass==resultCode)
        {
            startActivity(new Intent(ChangePasswordActivity.this,ActivityLogin.class));
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //checkInternet();
    }

    NetworkChangeReceiver receiver;
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
}
