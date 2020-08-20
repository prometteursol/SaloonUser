package com.prometteur.saloonuser.Activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.prometteur.saloonuser.Adapter.PromoOfferServiceListAdapter;
import com.prometteur.saloonuser.Model.PromoOfferBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.databinding.ActivitySearchLocationBinding;
import com.prometteur.saloonuser.databinding.DialogPromoOfferListBinding;

import java.util.ArrayList;
import java.util.List;

import static com.prometteur.saloonuser.Constants.ConstantMethods.convertDateToString;


public class PromoOfferListDialogActivity extends AppCompatActivity {
    DialogPromoOfferListBinding dialogPromoOfferListBinding;
    PromoOfferBean.Result result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogPromoOfferListBinding = DialogPromoOfferListBinding.inflate(getLayoutInflater());
        View view = dialogPromoOfferListBinding.getRoot();
        setContentView(view);


        result= (PromoOfferBean.Result) getIntent().getSerializableExtra("objOffer");
        dialogPromoOfferListBinding.tvOfferName.setText(result.getProofferName());
        dialogPromoOfferListBinding.tvStartDate.setText(convertDateToString(result.getProofferStartDate()));
        dialogPromoOfferListBinding.tvEndDate.setText(convertDateToString(result.getProofferEndDate()));
        dialogPromoOfferListBinding.tvPercentage.setText(result.getProofferDiscount()+"%");
        initAdapter( result.getServices());
      /*  List<String> mDataList=new ArrayList<>();
       mDataList.add("");
        mDataList.add("");
        mDataList.add("");
        mDataList.add("");
        initAdapter(mDataList);*/
    }
    PromoOfferServiceListAdapter adapter;
    private void initAdapter(List dataList) {

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(PromoOfferListDialogActivity.this, RecyclerView.VERTICAL, false);
        dialogPromoOfferListBinding.recyclerView.setLayoutManager(mLayoutManager);

        adapter = new PromoOfferServiceListAdapter(PromoOfferListDialogActivity.this, dataList);
        dialogPromoOfferListBinding.recyclerView.setAdapter(adapter);
        //setEmptyMsg(dataList, recyclerView, ivNoData);
    }

    public void closeDialog(View view) {
        finish();
    }
}
