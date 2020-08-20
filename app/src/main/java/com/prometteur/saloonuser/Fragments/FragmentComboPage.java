package com.prometteur.saloonuser.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.SystemClock;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prometteur.saloonuser.Activities.ActivityHomepage;
import com.prometteur.saloonuser.Activities.CartActivity;
import com.prometteur.saloonuser.Adapter.ComboListItemAdapter;
import com.prometteur.saloonuser.Model.AddCartBean;
import com.prometteur.saloonuser.Model.ComboOffBean;
import com.prometteur.saloonuser.Model.HomeBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.databinding.FragmentComboPageBinding;
import com.prometteur.saloonuser.retrofit.ApiInterface;
import com.prometteur.saloonuser.retrofit.RetrofitInstance;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.prometteur.saloonuser.Activities.ActivityComboAndOffers.branchId;
import static com.prometteur.saloonuser.Activities.ActivityComboAndOffers.mainCat;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.brands;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.brandsArr;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.category;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.highToLow;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.lowToHigh;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.selectedCats;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERIDVAL;
import static com.prometteur.saloonuser.Fragments.FragmentListSalonView.getHomeData;
import static com.prometteur.saloonuser.Utils.Utils.isNetworkAvailable;
import static com.prometteur.saloonuser.Utils.Utils.logout;
import static com.prometteur.saloonuser.Utils.Utils.setEmptyMsg;
import static com.prometteur.saloonuser.Utils.Utils.showFailToast;
import static com.prometteur.saloonuser.Utils.Utils.showNoInternetDialog;
import static com.prometteur.saloonuser.Utils.Utils.showProgress;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentComboPage extends Fragment {
    static FragmentComboPageBinding comboPageBinding;
   static AppCompatActivity nActivity;

    public FragmentComboPage() {
        // Required empty public constructor
    }

    public FragmentComboPage(AppCompatActivity nActivity) {
        this.nActivity = nActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        comboPageBinding=FragmentComboPageBinding.inflate(nActivity.getLayoutInflater());
        View view=comboPageBinding.getRoot();

        return view;
    }
    long mLastClickTimeGoToCart=0;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isNetworkAvailable(nActivity)) {
            getComboListDetails();
        } else {
            showNoInternetDialog(nActivity);
        }

       comboPageBinding.btnBookNow.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (SystemClock.elapsedRealtime() - mLastClickTimeGoToCart < 2000) {
                   return;
               }
               mLastClickTimeGoToCart = SystemClock.elapsedRealtime();
               startActivity(new Intent(nActivity, CartActivity.class).putExtra("branchId", branchId).putExtra("mainCat",mainCat));
               nActivity.finish();
           }
       });
       /* mDataList=new ArrayList<>();
        comboPageBinding.recycleCombolist.setLayoutManager(new LinearLayoutManager(nActivity));
        comboPageBinding.recycleCombolist.setAdapter(new ComboListItemAdapter(nActivity,mDataList));*/
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        nActivity= (AppCompatActivity) context;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    static ComboOffBean comboListBean;
    public static List<ComboOffBean.Result> mDataList;
    public static void getComboListDetails() {
        mDataList=new ArrayList<>();
        final ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        final Dialog progressDialog = showProgress(nActivity, 0);
        progressDialog.show();
        if(selectedCats.contains("0"))
        {
            category="";
        }else
        {
            category= TextUtils.join(",",selectedCats);
        }


        brands=TextUtils.join(",",brandsArr);
        service.getComboListDetails(branchId, category,lowToHigh,highToLow,brands)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ComboOffBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ComboOffBean loginBeanObj) {
                        progressDialog.dismiss();
                        comboListBean = loginBeanObj;
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        showFailToast(nActivity, nActivity.getResources().getString(R.string.went_wrong));
                    }

                    @Override
                    public void onComplete() {
                        // Updates UI with data
                        progressDialog.dismiss();

                        if (comboListBean.getStatus() == 1) {
                            mDataList = comboListBean.getResult();
                            comboPageBinding.recycleCombolist.setLayoutManager(new LinearLayoutManager(nActivity));
                            comboPageBinding.recycleCombolist.setAdapter(new ComboListItemAdapter(nActivity,mDataList));
                        } else if (comboListBean.getStatus() == 3) {
                            showFailToast(nActivity,  "" + comboListBean.getMsg());
                            logout(nActivity);
                        }
                        comboPageBinding.includeEmpty.ivNoData.setImageResource(R.drawable.img_combos_empty);
                        setEmptyMsg(mDataList, comboPageBinding.recycleCombolist, comboPageBinding.includeEmpty.ivNoData);
                    }
                });
    }


}
