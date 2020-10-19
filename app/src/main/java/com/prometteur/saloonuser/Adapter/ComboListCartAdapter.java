package com.prometteur.saloonuser.Adapter;

import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.prometteur.saloonuser.Activities.CartActivity;
import com.prometteur.saloonuser.Model.CartDetailBean;
import com.prometteur.saloonuser.Model.RemoveCartBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.Preferences;
import com.prometteur.saloonuser.Utils.TextViewCustomFont;
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
import static com.prometteur.saloonuser.Activities.ActivityHomepage.dateTimeOneTime;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.globalCartCount;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.strAppDate;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.strDate;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.strTime;
import static com.prometteur.saloonuser.Constants.ConstantMethods.getStrikeString;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERIDVAL;
import static com.prometteur.saloonuser.Utils.Utils.isNetworkAvailable;
import static com.prometteur.saloonuser.Utils.Utils.logout;
import static com.prometteur.saloonuser.Utils.Utils.showFailToast;
import static com.prometteur.saloonuser.Utils.Utils.showNoInternetDialog;
import static com.prometteur.saloonuser.Utils.Utils.showProgress;
import static com.prometteur.saloonuser.Utils.Utils.showSuccessToast;

public class ComboListCartAdapter extends RecyclerView.Adapter<ComboListCartAdapter.ViewHolder> {

    AppCompatActivity nActivity;

    boolean bottomlist;
    List<CartDetailBean.Result> results;

    public ComboListCartAdapter(AppCompatActivity nActivity, List<CartDetailBean.Result> results, boolean bottomlist) {
        this.nActivity = nActivity;
        this.results=results;
        this.bottomlist=bottomlist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(nActivity).inflate(R.layout.recycle_cart_combo_list,
                parent, false);

        return new ComboListCartAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        if(results.get(position).getCartType().equalsIgnoreCase("combo_offer")) {
            holder.tvComboName.setText(results.get(position).getDetails().get(0).getOfferName());
            holder.tvComboOfferPrice.setText("₹ " + results.get(position).getDetails().get(0).getDiscountPrice());
            holder.tvComboprice.setText("₹ " + results.get(position).getDetails().get(0).getOfferPrice());

            holder.recycleComboServicesOffers.setLayoutManager(new LinearLayoutManager(nActivity));
            holder.recycleComboServicesOffers.setAdapter(new ComboCartAdapter(nActivity, results.get(position).getDetails().get(0).getServices(), false));


holder.ivRemoveCombo.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if (isNetworkAvailable(nActivity)) {
            getRemoveCart(position,results.get(position).getCartId());   //remove from cart
        } else {
            showNoInternetDialog(nActivity);
        }

    }
});

            getStrikeString(holder.tvComboprice);
        }
        else
        {
            holder.itemView.setVisibility(View.GONE);
        }

      //  getResizedDrawable(nActivity,R.drawable.ic_clock_blue,holder.tvTime,null,null,R.dimen._12sdp);


    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextViewCustomFont tvComboName;
        TextViewCustomFont tvComboOfferPrice;
        TextViewCustomFont tvComboprice;
        ImageView ivRemoveCombo;


        RecyclerView recycleComboServicesOffers;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvComboName = itemView.findViewById(R.id.tvComboName);
            tvComboOfferPrice = itemView.findViewById(R.id.tvComboOfferPrice);
            ivRemoveCombo = itemView.findViewById(R.id.ivRemoveCombo);

            tvComboprice = itemView.findViewById(R.id.tvComboprice);
            recycleComboServicesOffers = itemView.findViewById(R.id.recycleComboServicesOffers);
            recycleComboServicesOffers = itemView.findViewById(R.id.recycleComboServicesOffers);
        }
    }


    RemoveCartBean cartDetailBean;
    private void getRemoveCart(final int pos, String cartId) {

        final ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        final Dialog progressDialog = showProgress(nActivity, 0);
        progressDialog.show();
        service.getRemoveCart(cartId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RemoveCartBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(RemoveCartBean loginBeanObj) {
                        progressDialog.dismiss();
                        cartDetailBean = loginBeanObj;

                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                       // showFailToast(nActivity, nActivity.getResources().getString(R.string.went_wrong));
                    }

                    @Override
                    public void onComplete() {
                        // Updates UI with data
                        progressDialog.dismiss();

                        if (cartDetailBean.getStatus() == 1) {
                            Preferences.setPreferenceValue(nActivity, "couponId", "0");
                            Preferences.setPreferenceValue(nActivity, "couponCode", "");
                            Preferences.setPreferenceValue(nActivity, "couponDesc", "");
                            Preferences.setPreferenceValue(nActivity, "couponOffPrice", "0");
                            if(globalCartCount==1){
                                dateTimeOneTime=false;
                                Preferences.setPreferenceValue(nActivity, "dateTimeOneTime","false");
                                Preferences.setPreferenceValue(nActivity, "oneTimeSalonId","0");
                                strTime="";
                                strDate="";strAppDate="";
                                Preferences.setPreferenceValue(nActivity, "dateTime","");
                                Preferences.setPreferenceValue(nActivity,"couponCode","");
                                Preferences.setPreferenceValue(nActivity,"couponDesc","");
                                Preferences.setPreferenceValue(nActivity,"couponOffPrice","0");
                            }
                            globalCartCount--;
/*if(globalCartCount==0)
{*/
    nActivity.startActivity(new Intent(nActivity, CartActivity.class).putExtra("branchId", branchId).putExtra("mainCat",mainCat));
    nActivity.finish();
//}
                            results.remove(pos);
                            notifyDataSetChanged();
                            showSuccessToast(nActivity, "" + cartDetailBean.getMsg());
                        } else if (cartDetailBean.getStatus() == 3) {
                            showFailToast(nActivity, "" + cartDetailBean.getMsg());
                              logout(nActivity);
                        }
                        //setEmptyMsg(mDataList, comboPageBinding.recycleCombolist, ivNoData);
                    }
                });
    }

}
