package com.prometteur.saloonuser.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.prometteur.saloonuser.Adapter.ReviewListAdapter;
import com.prometteur.saloonuser.Adapter.ServiceSelectedAdapter;
import com.prometteur.saloonuser.Model.AppointDetailBean;
import com.prometteur.saloonuser.Model.ReviewBean;
import com.prometteur.saloonuser.Model.ReviewDetailsBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.NetworkChangeReceiver;
import com.prometteur.saloonuser.Utils.TextViewCustomFont;
import com.prometteur.saloonuser.retrofit.ApiInterface;
import com.prometteur.saloonuser.retrofit.RetrofitInstance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.prometteur.saloonuser.Constants.ConstantVariables.USERIDVAL;
import static com.prometteur.saloonuser.Utils.Utils.isNetworkAvailable;
import static com.prometteur.saloonuser.Utils.Utils.logout;
import static com.prometteur.saloonuser.Utils.Utils.showFailToast;
import static com.prometteur.saloonuser.Utils.Utils.showNoInternetDialog;
import static com.prometteur.saloonuser.Utils.Utils.showProgress;
import static com.prometteur.saloonuser.Utils.Utils.showSuccessToast;


public class RatingActivity extends AppCompatActivity {

    RatingBar ratingBar;
    EditText edtReview;
    Button btnSave;
    RecyclerView rvOperator;
    TextViewCustomFont tvSalonName;
    String aptId="0";
    String branchId="0";
    String operatorReview="";
    public static JSONArray jsonArray;
    public static List<ReviewDetailsBean.Operator> operators;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        setToolbar();
        ratingBar = findViewById(R.id.ratingBarSalon);
        edtReview = findViewById(R.id.edtReview);
        btnSave = findViewById(R.id.btnSave);
        rvOperator = findViewById(R.id.rvOperator);
        tvSalonName = findViewById(R.id.tvSalonName);
       // result = (OngoingBean.Result) getIntent().getSerializableExtra("aptObj");
        aptId=getIntent().getStringExtra("aptId");
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operatorReview=edtReview.getText().toString();
                if (ratingBar.getRating() == 0.0) {
                    showFailToast(RatingActivity.this, "Write salon reviews / ratings to proceed.");
                } /*else if (operatorReview.isEmpty()) {
                    showFailToast(RatingActivity.this, "Write operator reviews / ratings to proceed.");
                }*/ else {
                    if (isNetworkAvailable(RatingActivity.this)) {
                        setRatings();
                    } else {
                        showNoInternetDialog(RatingActivity.this);
                    }

                }
            }
        });

        edtReview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.edtReview) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });
        if (isNetworkAvailable(RatingActivity.this)) {
            getReviewDetails();
        } else {
            showNoInternetDialog(RatingActivity.this);
        }

    }

    public void setToolbar() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Review & Rating");
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (Build.VERSION_CODES.P == currentapiVersion) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        }

    }
    @Override
    public boolean onSupportNavigateUp() {
        super.onSupportNavigateUp();
        /*startActivity(new Intent(RatingActivity.this, DashboardMainActivity.class).putExtra("objNoti", (Bundle) null));
        ((Activity)RatingActivity.this).finishAffinity();*/
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       /* startActivity(new Intent(RatingActivity.this, DashboardMainActivity.class).putExtra("objNoti", (Bundle) null));
        ((Activity)RatingActivity.this).finishAffinity();*/
       finish();
    }

    ReviewBean reviewBean;
    private void setRatings() {
        jsonArray=new JSONArray();
        if(operators!=null) {
            for (int i = 0; i < operators.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                //{"rev_branch_id":"2","rev_operator_id":"4","rev_user_id":"6","rev_service_id":"6","rev_rating":"3.5"}
                try {
                    jsonObject.put("rev_branch_id", operators.get(i).getRating());
                    jsonObject.put("rev_operator_id", "" + operators.get(i).getUserId());
                    jsonObject.put("rev_user_id", USERIDVAL);
                    jsonObject.put("rev_service_id", operators.get(i).getSrvcId());
                    jsonObject.put("rev_rating", operators.get(i).getRating());
                    jsonObject.put("rev_apt_id", aptId);
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        operatorReview=jsonArray.toString();
        ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        final Dialog progressDialog = showProgress(RatingActivity.this, 0);
        progressDialog.show();
        service.setReviews(branchId, String.valueOf(ratingBar.getRating()), edtReview.getText().toString(), USERIDVAL,aptId,operatorReview)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ReviewBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ReviewBean loginBeanObj) {
                        progressDialog.dismiss();
                        reviewBean = loginBeanObj;
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                      //  showFailToast(RatingActivity.this,  getResources().getString(R.string.went_wrong));
                    }

                    @Override
                    public void onComplete() {
                        // Updates UI with data
                        progressDialog.dismiss();
                        if (reviewBean.getStatus() == 1) {
                            showSuccessToast(RatingActivity.this,  "Review submitted" );
                            startActivity(new Intent(RatingActivity.this, ActivityHomepage.class).putExtra("objNoti", (Bundle) null));
                            ((Activity)RatingActivity.this).finishAffinity();

                        } else if (reviewBean.getStatus() == 3) {
                            showFailToast(RatingActivity.this,  "" + reviewBean.getMsg());
                            logout(RatingActivity.this);
                        }

                    }
                });
    }


    ReviewDetailsBean reviewDetailsBean;
    private void getReviewDetails() {

        final ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        final Dialog progressDialog = showProgress(RatingActivity.this, 0);
        progressDialog.show();

        service.getReviewDetails(aptId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ReviewDetailsBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ReviewDetailsBean loginBeanObj) {
                        progressDialog.dismiss();
                        reviewDetailsBean = loginBeanObj;
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        //showFailToast(RatingActivity.this, getResources().getString(R.string.went_wrong));
                    }

                    @Override
                    public void onComplete() {
                        // Updates UI with data
                        progressDialog.dismiss();

                        if (reviewDetailsBean.getStatus() == 1) {
                           ReviewDetailsBean.Result result=reviewDetailsBean.getResult().get(0);
                            branchId=result.getBranId();
                            operators=result.getOperators();
                            tvSalonName.setText(result.getBranName());
                            rvOperator.setLayoutManager(new LinearLayoutManager(RatingActivity.this));
                            rvOperator.setAdapter(new ReviewListAdapter(RatingActivity.this,result.getOperators()));
                        } else if (reviewDetailsBean.getStatus() == 3) {
                            showFailToast(RatingActivity.this, "" + reviewDetailsBean.getMsg());
                              logout(RatingActivity.this);
                        }
                        //setEmptyMsg(mDataList, comboPageBinding.recycleCombolist, ivNoData);
                    }
                });
    }
    @Override
    protected void onResume() {
        super.onResume();
        checkInternet();
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
