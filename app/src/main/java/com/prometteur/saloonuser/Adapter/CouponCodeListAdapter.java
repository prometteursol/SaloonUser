package com.prometteur.saloonuser.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prometteur.saloonuser.Activities.CouponCodeListActivity;
import com.prometteur.saloonuser.Model.CouponBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.TextViewCustomFont;
import com.prometteur.saloonuser.Utils.Utils;
import com.prometteur.saloonuser.databinding.RecycleCouponCodesBinding;
import com.prometteur.saloonuser.listener.OnItemClickListener;
import com.prometteur.saloonuser.retrofit.ApiInterface;
import com.prometteur.saloonuser.retrofit.RetrofitInstance;

import java.text.DecimalFormat;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.prometteur.saloonuser.Constants.ConstantMethods.getResizedDrawable;
import static com.prometteur.saloonuser.Constants.ConstantMethods.getStrikeString;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERIDVAL;
import static com.prometteur.saloonuser.Utils.Utils.isNetworkAvailable;
import static com.prometteur.saloonuser.Utils.Utils.logout;
import static com.prometteur.saloonuser.Utils.Utils.showFailToast;
import static com.prometteur.saloonuser.Utils.Utils.showNoInternetDialog;
import static com.prometteur.saloonuser.Utils.Utils.showProgress;
import static com.prometteur.saloonuser.Utils.Utils.showSuccessToast;

public class CouponCodeListAdapter extends RecyclerView.Adapter<CouponCodeListAdapter.CouponViewHolder> {

    Activity nActivity;
    int size;

    //bottom dialog widgets
    RecyclerView recycleOperatorsList;
    TextViewCustomFont tvOperatortitle;
    ImageView ivCloseCrossimg;
    Button btnDone;


    boolean bottomlist;

    List<CouponBean.Result> mDatalist;
    OnItemClickListener listener;
    String aptAmt="";
    public CouponCodeListAdapter(Activity nActivity, List<CouponBean.Result> mDatalist, boolean bottomlist,  OnItemClickListener listener,String aptAmt) {
        this.nActivity = nActivity;
        this.mDatalist = mDatalist;
        this.bottomlist=bottomlist;
        this.listener=listener;
        this.aptAmt=aptAmt;
    }

    @NonNull
    @Override
    public CouponViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       /* View view = LayoutInflater.from(nActivity).inflate(R.layout.recycle_coupon_codes,
                parent, false);
        return new CouponCodeListAdapter.CouponViewHolder(view);*/
        View view = LayoutInflater.from(nActivity).inflate(R.layout.recycle_coupon_codes,
                parent, false);
        return new CouponCodeListAdapter.CouponViewHolder(RecycleCouponCodesBinding.inflate(nActivity.getLayoutInflater()));
    }

    @Override
    public void onBindViewHolder(@NonNull final CouponViewHolder holder, final int position) {


        getResizedDrawable(nActivity,R.drawable.ic_paypal,holder.recycleCouponCodesBinding.tvCouponCode,null,null,R.dimen._12sdp);

        holder.recycleCouponCodesBinding.tvCouponCode.setText(mDatalist.get(position).getCouponCode());
        holder.recycleCouponCodesBinding.tvText.setText(mDatalist.get(position).getCouponText());
        holder.recycleCouponCodesBinding.tvDesc.setText(mDatalist.get(position).getCouponDescription());
        holder.recycleCouponCodesBinding.tvTitle.setText(mDatalist.get(position).getCouponTitle());
        Utils.makeTextViewResizable(holder.recycleCouponCodesBinding.tvDesc, 1, "+ More", true);


       /* if(!mDatalist.get(position).getCouponStatus().equalsIgnoreCase("1"))
        {*/
            holder.recycleCouponCodesBinding.tvApply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(Double.parseDouble(mDatalist.get(position).getCouponMinimumPrice())<Double.parseDouble(aptAmt)) {
                        double couponPrice=Double.parseDouble(aptAmt)*Double.parseDouble(mDatalist.get(position).getCouponDiscount())/100;
                        if(couponPrice>Double.parseDouble(mDatalist.get(position).getCouponMaxDiscount())) {
                            mDatalist.get(position).setCouponDiscountPrice(""+mDatalist.get(position).getCouponMaxDiscount());
                        }else
                        {
                            mDatalist.get(position).setCouponDiscountPrice(""+new DecimalFormat("##.##").format(couponPrice));
                        }
                        if (isNetworkAvailable(nActivity)) {
                            getApplyCoupon(mDatalist.get(position).getCouponId(), mDatalist.get(position).getCouponCode(), position);
                        } else {
                            showNoInternetDialog(nActivity);
                        }

                    }else
                    {
                        showFailToast(nActivity,"The amount is not sufficient to apply this coupon");
                    }
                }
            });
        /*}else
        {
            holder.recycleCouponCodesBinding.tvApply.setText("Applied");
        }*/
    }

    @Override
    public int getItemCount() {
        return mDatalist.size();
    }

    public class CouponViewHolder extends RecyclerView.ViewHolder {

        RecycleCouponCodesBinding recycleCouponCodesBinding;


        public CouponViewHolder(@NonNull RecycleCouponCodesBinding itemView) {
            super(itemView.getRoot());
            recycleCouponCodesBinding=itemView;
        }
    }

    CouponBean couponBean;

    private void getApplyCoupon(final String couponId, final String couponCode, final int position) {

        final ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        final Dialog progressDialog = showProgress(nActivity, 0);
        progressDialog.show();
        service.getApplyCoupon(USERIDVAL,couponId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CouponBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CouponBean loginBeanObj) {
                        progressDialog.dismiss();
                        couponBean = loginBeanObj;
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                      //  showFailToast(nActivity, nActivity.getResources().getString(R.string.went_wrong));
                    }

                    @Override
                    public void onComplete() {
                        // Updates UI with data
                        progressDialog.dismiss();

                        if (couponBean.getStatus() == 1) {
                            showSuccessToast(nActivity,"Your Cashback coupon "+couponCode+" is successfully applied");

                            listener.onItemClick(mDatalist.get(position));
                        } else if (couponBean.getStatus() == 3) {
                            showFailToast(nActivity, "" + couponBean.getMsg());
                              logout(nActivity);
                        }else
                        {
                            showFailToast(nActivity,couponBean.getMsg());
                        }
                        //setEmptyMsg(mDataList, comboPageBinding.recycleCombolist, ivNoData);
                    }
                });
    }


}
