package com.prometteur.saloonuser.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prometteur.saloonuser.Activities.ActivityBookAppointment;
import com.prometteur.saloonuser.Activities.CartActivity;
import com.prometteur.saloonuser.Model.CartDetailBean;
import com.prometteur.saloonuser.Model.RemoveCartBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.Preferences;
import com.prometteur.saloonuser.Utils.TextViewCustomFont;
import com.prometteur.saloonuser.retrofit.ApiInterface;
import com.prometteur.saloonuser.retrofit.RetrofitInstance;

import java.text.DecimalFormat;
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
import static com.prometteur.saloonuser.Activities.ActivitySalonServices.filterBottomSheet;
import static com.prometteur.saloonuser.Constants.ConstantMethods.getResizedDrawable;
import static com.prometteur.saloonuser.Constants.ConstantMethods.getStrikeString;
import static com.prometteur.saloonuser.Utils.Utils.isNetworkAvailable;
import static com.prometteur.saloonuser.Utils.Utils.logout;
import static com.prometteur.saloonuser.Utils.Utils.showFailToast;
import static com.prometteur.saloonuser.Utils.Utils.showNoInternetDialog;
import static com.prometteur.saloonuser.Utils.Utils.showProgress;
import static com.prometteur.saloonuser.Utils.Utils.showSuccessToast;

public class ServiceCartAdapter extends RecyclerView.Adapter<ServiceCartAdapter.ViewHolder> {

    AppCompatActivity nActivity;
    List<CartDetailBean.Service> mDataList;

    //bottom dialog widgets
    RecyclerView recycleOperatorsList;
    TextViewCustomFont tvOperatortitle;
    ImageView ivCloseCrossimg;
    Button btnDone;

    String selectOperator;
    String removeOperator;
    String operatorName;
    boolean bottomlist;
    boolean isbottom;
    RemoveCartBean cartDetailBean;

    public ServiceCartAdapter(AppCompatActivity nActivity, List<CartDetailBean.Service> mDataList, boolean bottomlist) {
        this.nActivity = nActivity;
        this.mDataList = mDataList;
        this.bottomlist = bottomlist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(nActivity).inflate(R.layout.recycle_service_cart,
                parent, false);
        selectOperator = nActivity.getResources().getString(R.string.add_select_operator);
        removeOperator = nActivity.getResources().getString(R.string.minus_Remove_operator);
        return new ServiceCartAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.tvServiceName.setText(mDataList.get(position).getSrvcName());
        if(mDataList.get(position).getBrndName()!=null) {
            holder.tvDesc.setText("Brand : " + mDataList.get(position).getBrndName());
        }else
        {
            holder.tvDesc.setVisibility(View.INVISIBLE);
            holder.tvDesc.setText("Brand : NA");
        }

if(mDataList.get(position).getSrvcDiscount()!=null && !mDataList.get(position).getSrvcDiscount().isEmpty()) {
    holder.tvTime.setText(mDataList.get(position).getSrvcDiscount() + "% OFF");
    if (mDataList.get(position).getSrvcDiscountPrice() == null || mDataList.get(position).getSrvcDiscountPrice().isEmpty()) {
        holder.tvOfferPrice.setText("₹ " + new DecimalFormat("##.##").format(Double.parseDouble(mDataList.get(position).getSrvcPrice())));
        holder.tvMainPrice.setVisibility(View.GONE);
        holder.tvOfferPrice.setPadding(0,20,0,20);
    } else {
        holder.tvOfferPrice.setText("₹ " + new DecimalFormat("##.##").format(Double.parseDouble(mDataList.get(position).getSrvcDiscountPrice())));
        holder.tvMainPrice.setText("₹ " + new DecimalFormat("##.##").format(Double.parseDouble(mDataList.get(position).getSrvcPrice())));
    }
}else {
//    if (mDataList.get(position).getSrvcDiscountPrice() == null || mDataList.get(position).getSrvcDiscountPrice().isEmpty()) {
        holder.tvOfferPrice.setText("₹ " + new DecimalFormat("##.##").format(Double.parseDouble(mDataList.get(position).getSrvcPrice())));
        holder.tvMainPrice.setVisibility(View.GONE);
        holder.tvOfferPrice.setPadding(0,20,0,20);
   /* } else {
        holder.tvOfferPrice.setText("₹ " + new DecimalFormat("##.##").format(Double.parseDouble(mDataList.get(position).getSrvcDiscountPrice())));
        holder.tvMainPrice.setText("₹ " + new DecimalFormat("##.##").format(Double.parseDouble(mDataList.get(position).getSrvcPrice())));
    }*/
    holder.tvTime.setVisibility(View.GONE);
}
boolean isOperator=false;
for(int i=0;i<mDataList.get(position).getOperators().size();i++) {
    if (mDataList.get(position).getOperators().get(i).getSelected().equalsIgnoreCase("selected")){
        holder.tvAddRemoveOperator.setText("Op : "+mDataList.get(position).getOperators().get(i).getUserFName()+" "+mDataList.get(position).getOperators().get(i).getUserLName());
        holder.tvAddRemoveOperator.setTextColor(nActivity.getResources().getColor(R.color.black));
        isOperator=true;
    }
}
if(!isOperator)
{
    holder.tvAddRemoveOperator.setVisibility(View.INVISIBLE);
}
        holder.tvAddRemoveOperator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.tvAddRemoveOperator.getText().toString().equals(selectOperator)) {
                    isbottom = false;
                    holder.tvAddRemoveOperator.setText(removeOperator);
                    holder.tvAddRemoveOperator.setTextColor(nActivity.getResources().getColor(R.color.red));
                    showOperatorSelectBottomDialog(nActivity, mDataList.get(position).getOperators(), holder.tvAddRemoveOperator, holder.tvAddRemoveOperator);
                } else {
                    //holder.tvAddedOperatorName.setText("");
                    holder.tvAddRemoveOperator.setText(selectOperator);
                    holder.tvAddRemoveOperator.setTextColor(nActivity.getResources().getColor(R.color.skyBlueLight));
                }

            }
        });

        holder.tvAddedOperatorName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.tvAddedOperatorName.getText().toString().equals("+ Add to Cart")) {
                    isbottom = true;
                    holder.tvAddedOperatorName.setText(removeOperator);
                    holder.tvAddedOperatorName.setTextColor(nActivity.getResources().getColor(R.color.red));
                    filterBottomSheet = new ActivityBookAppointment(nActivity);
                    filterBottomSheet.show(nActivity.getSupportFragmentManager(), filterBottomSheet.getTag());
                    // showOperatorSelectBottomDialog(nActivity, holder.tvAddedOperatorName,holder.tvAddedOperatorName);
                } else {
                    if (isbottom) {
                        holder.tvAddedOperatorName.setText("+ Add to Cart");
                        holder.tvAddedOperatorName.setTextColor(nActivity.getResources().getColor(R.color.skyBlueLight));
                    } else {
                        holder.tvAddedOperatorName.setText(selectOperator);
                        holder.tvAddedOperatorName.setTextColor(nActivity.getResources().getColor(R.color.skyBlueLight));
                    }
                }

            }
        });
        holder.ivRemoveCombo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable(nActivity)) {
                    getRemoveCart(position, mDataList.get(position).getCartId());   //remove from cart
                } else {
                    showNoInternetDialog(nActivity);
                }
            }
        });
        getStrikeString(holder.tvMainPrice);

        getResizedDrawable(nActivity, R.drawable.ic_clock_blue, holder.tvTime, null, null, R.dimen._12sdp);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void showOperatorSelectBottomDialog(final Activity nActivity, List<CartDetailBean.Operator> operators, final TextViewCustomFont tvOperatorName, final TextViewCustomFont tvAddRemoveOperator) {
        final Dialog operatorSelect = new Dialog(nActivity, R.style.CustomAlertDialog);
        operatorSelect.setContentView(R.layout.dialog_select_operator);
        Window window = operatorSelect.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);
        operatorSelect.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        recycleOperatorsList = operatorSelect.findViewById(R.id.recycleOperatorsList);
        tvOperatortitle = operatorSelect.findViewById(R.id.tvOperatortitle);
        ivCloseCrossimg = operatorSelect.findViewById(R.id.ivCloseCrossimg);
        btnDone = operatorSelect.findViewById(R.id.btnDone);

        LinearLayoutManager layoutManager = new LinearLayoutManager(nActivity);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recycleOperatorsList.setLayoutManager(layoutManager);
        recycleOperatorsList.setAdapter(new OperatorsListCartServiceAdapter(nActivity, operators, new OperatorsListCartServiceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, String OperatorName) {
                operatorName = OperatorName;
                //constraintLayout.setBackground(nActivity.getResources().getDrawable(R.drawable.bg_white_blue_stroke_curved));
            }
        }));
        ivCloseCrossimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isbottom) {
                    tvAddRemoveOperator.setText("+ Add to Cart");
                    tvAddRemoveOperator.setTextColor(nActivity.getResources().getColor(R.color.skyBlueLight));
                } else {
                    tvAddRemoveOperator.setText(selectOperator);
                    tvAddRemoveOperator.setTextColor(nActivity.getResources().getColor(R.color.skyBlueLight));
                }
                operatorSelect.dismiss();
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (operatorName != null) {

                    tvOperatorName.setText("Op:" + operatorName);
                    tvAddRemoveOperator.setTextColor(nActivity.getResources().getColor(R.color.black));
                    notifyDataSetChanged();
                    operatorSelect.dismiss();
                } else {
                    Toast.makeText(nActivity, "Please select operator", Toast.LENGTH_SHORT).show();
                }

            }
        });
        operatorSelect.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                if (operatorName != null) {

                }
                if (isbottom) {
                    tvAddRemoveOperator.setText("+Add");
                    tvAddRemoveOperator.setTextColor(nActivity.getResources().getColor(R.color.skyBlueLight));

                } else {
                    tvAddRemoveOperator.setText(selectOperator);
                    tvAddRemoveOperator.setTextColor(nActivity.getResources().getColor(R.color.skyBlueLight));
                }
                notifyDataSetChanged();
            }

        });
        operatorSelect.show();
    }

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
                        showFailToast(nActivity, nActivity.getResources().getString(R.string.went_wrong));
                    }

                    @Override
                    public void onComplete() {
                        // Updates UI with data
                        progressDialog.dismiss();
                        try {
                            if (cartDetailBean.getStatus() == 1) {

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
//                                }
                                mDataList.remove(pos);
                                //notifyDataSetChanged();
                                showSuccessToast(nActivity, "" + cartDetailBean.getMsg());
                            } else if (cartDetailBean.getStatus() == 3) {
                                showFailToast(nActivity, "" + cartDetailBean.getMsg());
                                  logout(nActivity);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //setEmptyMsg(mDataList, comboPageBinding.recycleCombolist, ivNoData);
                    }
                });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextViewCustomFont tvServiceName;
        TextViewCustomFont tvDesc;
        TextViewCustomFont tvAddRemoveOperator;
        TextViewCustomFont tvAddedOperatorName;
        TextViewCustomFont tvOfferPrice;
        TextViewCustomFont tvMainPrice;
        TextViewCustomFont tvTime;
        ImageView ivRemoveCombo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvServiceName = itemView.findViewById(R.id.tvServiceName);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            tvAddRemoveOperator = itemView.findViewById(R.id.tvAddRemoveOperator);
            tvAddedOperatorName = itemView.findViewById(R.id.tvAddedOperatorName);
            tvOfferPrice = itemView.findViewById(R.id.tvOfferPrice);
            tvMainPrice = itemView.findViewById(R.id.tvMainPrice);
            tvTime = itemView.findViewById(R.id.tvTime);
            ivRemoveCombo = itemView.findViewById(R.id.ivRemoveCombo);
        }
    }
}
