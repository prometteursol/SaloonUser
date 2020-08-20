package com.prometteur.saloonuser.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.bumptech.glide.Glide;
import com.prometteur.saloonuser.Adapter.SalonAddressResultsAdapter;
import com.prometteur.saloonuser.Adapter.TopServicesAdapter;
import com.prometteur.saloonuser.Model.SalonDetailBean;
import com.prometteur.saloonuser.Model.SearchBean;
import com.prometteur.saloonuser.R;

import com.prometteur.saloonuser.databinding.ActivitySearchSalonsBinding;
import com.prometteur.saloonuser.retrofit.ApiInterface;
import com.prometteur.saloonuser.retrofit.RetrofitInstance;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.prometteur.saloonuser.Utils.Utils.isNetworkAvailable;
import static com.prometteur.saloonuser.Utils.Utils.logout;
import static com.prometteur.saloonuser.Utils.Utils.setEmptyMsg;
import static com.prometteur.saloonuser.Utils.Utils.showFailToast;
import static com.prometteur.saloonuser.Utils.Utils.showNoInternetDialog;
import static com.prometteur.saloonuser.Utils.Utils.showProgress;

public class ActivitySearchSalons extends AppCompatActivity implements View.OnClickListener {

    ActivitySearchSalonsBinding searchSalonsBinding;
    Activity nActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchSalonsBinding = ActivitySearchSalonsBinding.inflate(getLayoutInflater());
        View view = searchSalonsBinding.getRoot();
        setContentView(view);
        nActivity=this;
        searchSalonsBinding.ivBackArrowimg.setOnClickListener(this);
        searchSalonsBinding.edtSearchSalon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                keyword=charSequence.toString();
                if (isNetworkAvailable(nActivity)) {
                    getSearchData();
                } else {
                    showNoInternetDialog(nActivity);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });




    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.ivBackArrowimg:
                finish();
                break;
        }
    }


    public SearchBean searchBean;
    String keyword="";
    List<SearchBean.Result> mDataList;
    private void getSearchData() {
        mDataList=new ArrayList<>();
        final ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        //final Dialog progressDialog = showProgress(nActivity, 0);
        //progressDialog.show();
        service.getSearchResult(keyword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SearchBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(SearchBean loginBeanObj) {
                      //  progressDialog.dismiss();
                        searchBean = loginBeanObj;
                    }

                    @Override
                    public void onError(Throwable e) {
                       // progressDialog.dismiss();
                        showFailToast(nActivity, nActivity.getResources().getString(R.string.went_wrong));
                    }

                    @Override
                    public void onComplete() {
                        // Updates UI with data
                      //  progressDialog.dismiss();

                        if (searchBean.getStatus() == 1) {
                            mDataList=searchBean.getResult();
                            searchSalonsBinding.recycleSalonAddressResults.setLayoutManager(new LinearLayoutManager(nActivity));
                            searchSalonsBinding.recycleSalonAddressResults.setAdapter(new SalonAddressResultsAdapter(nActivity,  mDataList));



                        } else if (searchBean.getStatus() == 3) {
                            showFailToast(nActivity, "" + searchBean.getMsg());
                              logout(nActivity);
                        }
                        searchSalonsBinding.includeEmpty.ivNoData.setImageResource(R.drawable.img_search_empty);
                        setEmptyMsg(mDataList, searchSalonsBinding.recycleSalonAddressResults, searchSalonsBinding.includeEmpty.ivNoData);
                    }
                });
    }
}
