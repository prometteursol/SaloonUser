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
import androidx.recyclerview.widget.RecyclerView;

import android.os.SystemClock;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prometteur.saloonuser.Activities.CartActivity;
import com.prometteur.saloonuser.Adapter.OfferListAdapter;
import com.prometteur.saloonuser.Model.ComboOffBean;
import com.prometteur.saloonuser.Model.PromoOfferBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.databinding.FragmentOfferPageBinding;
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
import static com.prometteur.saloonuser.Fragments.FragmentComboPage.getComboListDetails;
import static com.prometteur.saloonuser.Utils.Utils.isNetworkAvailable;
import static com.prometteur.saloonuser.Utils.Utils.logout;
import static com.prometteur.saloonuser.Utils.Utils.setEmptyMsg;
import static com.prometteur.saloonuser.Utils.Utils.showFailToast;
import static com.prometteur.saloonuser.Utils.Utils.showNoInternetDialog;
import static com.prometteur.saloonuser.Utils.Utils.showProgress;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOfferPage extends Fragment {

    static FragmentOfferPageBinding offerPageBinding;
    static AppCompatActivity nActivity;

    public FragmentOfferPage() {
        // Required empty public constructor
    }

    public FragmentOfferPage(AppCompatActivity nActivity) {
        this.nActivity = nActivity;
    }
    long mLastClickTimeGoToCart=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        offerPageBinding= FragmentOfferPageBinding.inflate(nActivity.getLayoutInflater());
        View view=offerPageBinding.getRoot();

        /*offerPageBinding.recycleOfferlist.setLayoutManager(new LinearLayoutManager(nActivity));
        offerPageBinding.recycleOfferlist.setAdapter(new OfferListAdapter(nActivity, mDataList));
*/
        offerPageBinding.btnBookNow.setOnClickListener(new View.OnClickListener() {
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
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isNetworkAvailable(nActivity)) {
            getPromoOffListDetails();
        } else {
            showNoInternetDialog(nActivity);
        }

    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        nActivity= (AppCompatActivity) context;
    }
    private static void initAdapter() {

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(nActivity, RecyclerView.VERTICAL, false);
        offerPageBinding.recycleOfferlist.setLayoutManager(mLayoutManager);

        OfferListAdapter adapter =new OfferListAdapter(nActivity,mDataList);
        offerPageBinding.recycleOfferlist.setAdapter(adapter);
    }



    static PromoOfferBean promoOfferBean;
    static List<PromoOfferBean.Result> mDataList;
    public static void getPromoOffListDetails() {
        mDataList=new ArrayList<>();
        final ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        final Dialog progressDialog = showProgress(nActivity, 0);
        //progressDialog.show();
        if(selectedCats.contains("0"))
        {
            category="";
        }else
        {
            category= TextUtils.join(",",selectedCats);
        }


        brands=TextUtils.join(",",brandsArr);
        service.getPromoOfferDetails(branchId, category,lowToHigh,highToLow,brands)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PromoOfferBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(PromoOfferBean loginBeanObj) {
                        progressDialog.dismiss();
                        promoOfferBean = loginBeanObj;
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

                        if (promoOfferBean.getStatus() == 1) {
                            mDataList = promoOfferBean.getResult();
                            /*offerPageBinding.recycleOfferlist.setLayoutManager(new LinearLayoutManager(nActivity));
                            offerPageBinding.recycleOfferlist.setAdapter(new ComboListItemAdapter(nActivity,mDataList));*/
                            initAdapter();
                        } else if (promoOfferBean.getStatus() == 3) {
                            showFailToast(nActivity,  "" + promoOfferBean.getMsg());
                              logout(nActivity);
                        }
                        offerPageBinding.includeEmpty.ivNoData.setImageResource(R.drawable.img_offers_empty);
                        setEmptyMsg(mDataList, offerPageBinding.recycleOfferlist, offerPageBinding.includeEmpty.ivNoData);
                    }
                });
    }
}
