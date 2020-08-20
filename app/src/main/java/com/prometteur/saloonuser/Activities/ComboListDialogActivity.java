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


import com.prometteur.saloonuser.Adapter.ComboServiceListAdapter;
import com.prometteur.saloonuser.Model.ComboOffBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.databinding.DialogComboListBinding;
import com.prometteur.saloonuser.databinding.DialogPromoOfferListBinding;

import java.util.ArrayList;
import java.util.List;

import static com.prometteur.saloonuser.Constants.ConstantMethods.getStrikeString;


public class ComboListDialogActivity extends AppCompatActivity {
    ComboOffBean.Result result;
    DialogComboListBinding dialogPromoOfferListBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogPromoOfferListBinding = DialogComboListBinding.inflate(getLayoutInflater());
        View view = dialogPromoOfferListBinding.getRoot();
        setContentView(view);

       // ivNoData=findViewById(R.id.ivNoData);
        result= (ComboOffBean.Result) getIntent().getSerializableExtra("objCombo");
        dialogPromoOfferListBinding.tvComboName.setText(result.getOfferName());
        dialogPromoOfferListBinding.tvDiscountAmount.setText("₹ " +result.getOfferDiscountPrice());
        if(!result.getOfferPrice().isEmpty()) {
            dialogPromoOfferListBinding.tvAmount.setText("₹ " +result.getOfferPrice());
        }else
        {
            dialogPromoOfferListBinding.tvAmount.setVisibility(View.GONE);
        }
        getStrikeString(dialogPromoOfferListBinding.tvAmount);
        initAdapter(result.getServices());
//        getResizedDrawable(mContext, R.drawable.ic_time, holder.tvTime, null, null, R.dimen._11sdp);
        /*List<String> mDataList=new ArrayList<>();
        mDataList.add("");
        mDataList.add("");
        mDataList.add("");
        mDataList.add("");
        initAdapter(mDataList);*/
    }
    ComboServiceListAdapter adapter;
    ImageView ivNoData;
    private void initAdapter(List dataList) {

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(ComboListDialogActivity.this, RecyclerView.VERTICAL, false);
        dialogPromoOfferListBinding.recyclerView.setLayoutManager(mLayoutManager);

        adapter = new ComboServiceListAdapter(ComboListDialogActivity.this, dataList);
        dialogPromoOfferListBinding.recyclerView.setAdapter(adapter);
        //setEmptyMsg(dataList, recyclerView, ivNoData);
    }
    public void closeDialog(View view) {
        finish();
    }
}
