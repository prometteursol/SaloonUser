package com.prometteur.saloonuser.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;

import com.prometteur.saloonuser.Adapter.ComboOfferPagerAdapter;
import com.prometteur.saloonuser.Fragments.FragmentFilterBottomSheetComboNOffers;
import com.prometteur.saloonuser.Fragments.FragmentFilterBottomSheetServices;
import com.prometteur.saloonuser.R;
import com.google.android.material.tabs.TabLayout;
import com.prometteur.saloonuser.databinding.ActivityComboAndOffersBinding;

import static com.prometteur.saloonuser.Activities.ActivitySalonServices.filterBottomSheetServices;

public class ActivityComboAndOffers extends AppCompatActivity implements View.OnClickListener {
    ActivityComboAndOffersBinding comboAndOffersBinding;
    AppCompatActivity nActivity;
    public static String branchId="0";
    public static String mainCat="0";
    public static FragmentFilterBottomSheetComboNOffers filterBottomSheetComboNOffers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        comboAndOffersBinding = ActivityComboAndOffersBinding.inflate(getLayoutInflater());
        View view = comboAndOffersBinding.getRoot();
        setContentView(view);
        nActivity=this;
        comboAndOffersBinding.tabComboOffer.addTab(comboAndOffersBinding.tabComboOffer.newTab().setText(nActivity.getResources().getString(R.string.Combo)));
        comboAndOffersBinding.tabComboOffer.addTab(comboAndOffersBinding.tabComboOffer.newTab().setText(nActivity.getResources().getString(R.string.Offers)));
        branchId=getIntent().getStringExtra("branchId");
        mainCat=getIntent().getStringExtra("mainCat");

        final ComboOfferPagerAdapter pagerAdapter = new ComboOfferPagerAdapter(getSupportFragmentManager(), comboAndOffersBinding.tabComboOffer.getTabCount(),nActivity);
        comboAndOffersBinding.vpComboOffer.setAdapter(pagerAdapter);
        comboAndOffersBinding.vpComboOffer.setOffscreenPageLimit(2);
        comboAndOffersBinding.vpComboOffer.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(comboAndOffersBinding.tabComboOffer));

        comboAndOffersBinding.tabComboOffer.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                comboAndOffersBinding.vpComboOffer.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }


        });

        comboAndOffersBinding.ivBackArrowimg.setOnClickListener(this);
        comboAndOffersBinding.ivFilterimg.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    long mLastClickTimeFilter=0;
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.ivBackArrowimg:
                finish();
                break;
            case R.id.ivFilterimg:
                if (SystemClock.elapsedRealtime() - mLastClickTimeFilter < 2000) {
                    return;
                }
                mLastClickTimeFilter = SystemClock.elapsedRealtime();
                filterBottomSheetComboNOffers = new FragmentFilterBottomSheetComboNOffers(nActivity);
                filterBottomSheetComboNOffers.show(getSupportFragmentManager(), filterBottomSheetComboNOffers.getTag());
                break;
        }
    }
}
