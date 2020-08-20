package com.prometteur.saloonuser.Fragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.prometteur.saloonuser.Activities.ActivityAccountsSettings;
import com.prometteur.saloonuser.Activities.ActivityCustomerSupport;
import com.prometteur.saloonuser.Activities.ActivityHomepage;
import com.prometteur.saloonuser.Activities.ActivityUpdateProfile;
import com.prometteur.saloonuser.Activities.AppointmentHistoryActivity;
import com.prometteur.saloonuser.Activities.PointBalanceActivity;
import com.prometteur.saloonuser.Activities.RefferAndEarnActivity;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.Preferences;
import com.prometteur.saloonuser.databinding.FragmentMyAccountsBinding;

import static com.prometteur.saloonuser.Constants.ConstantVariables.REDEEMPOINTSKEY;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERFNAME;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERLNAME;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERPROFILE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMyAccounts extends Fragment implements View.OnClickListener {
    FragmentMyAccountsBinding myAccountsBinding;

    Activity nActivity;

    public FragmentMyAccounts() {
        // Required empty public constructor
    }

    public FragmentMyAccounts(Activity nActivity) {
        this.nActivity = nActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myAccountsBinding = FragmentMyAccountsBinding.inflate(inflater, container, false);
        View view = myAccountsBinding.getRoot();
        myAccountsBinding.ivSettingsGoRight.setOnClickListener(this);
        myAccountsBinding.ivSettings.setOnClickListener(this);
        myAccountsBinding.tvSettings.setOnClickListener(this);
        myAccountsBinding.tvSettingstext.setOnClickListener(this);
        myAccountsBinding.ivCustomerSupportGo.setOnClickListener(this);
        myAccountsBinding.tvCustomerSupportTitle.setOnClickListener(this);
        myAccountsBinding.ivCustomerSupport.setOnClickListener(this);
        myAccountsBinding.tvUpdateProfile.setOnClickListener(this);
        myAccountsBinding.tvMyAppointmentHistory.setOnClickListener(this);
        myAccountsBinding.tvMyAppointmenttext.setOnClickListener(this);
        myAccountsBinding.ivHistoryArrow.setOnClickListener(this);
        myAccountsBinding.tvReferEarnTitle.setOnClickListener(this);
        myAccountsBinding.ivReferEarnGo.setOnClickListener(this);
        myAccountsBinding.ivReferEarn.setOnClickListener(this);
        myAccountsBinding.Conlay0.setOnClickListener(this);

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        nActivity= (Activity) context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myAccountsBinding.tvHiTitle.setText("Hi, " + USERFNAME + " " + USERLNAME + " !");
        if(Preferences.getPreferenceValue(nActivity, REDEEMPOINTSKEY).equalsIgnoreCase("NA"))
        {
            Preferences.setPreferenceValue(nActivity, REDEEMPOINTSKEY,"0");
        }
        myAccountsBinding.tvRedeemPoints.setText(Preferences.getPreferenceValue(nActivity, REDEEMPOINTSKEY)+" Pt.");
        try {
            Glide.with(nActivity).load(USERPROFILE).error(R.drawable.img_profile).into(myAccountsBinding.civUserProfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    long mLastClickTimeUpdateProf=0;
    long mLastClickTimePoint=0;
    long mLastClickTimeRef=0;
    long mLastClickTimeHistory=0;
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.tvMyAppointmentHistory:
            case R.id.tvMyAppointmenttext:
            case R.id.ivHistoryArrow:
                if (SystemClock.elapsedRealtime() - mLastClickTimeHistory < 2000) {
                    return;
                }
                mLastClickTimeHistory = SystemClock.elapsedRealtime();
                ActivityHomepage.strHistory="1";
                startActivity(new Intent(nActivity, AppointmentHistoryActivity.class));
                break;
            case R.id.ivCustomerSupportGo:
            case R.id.tvCustomerSupportTitle:
            case R.id.ivCustomerSupport:
                startActivity(new Intent(nActivity, ActivityCustomerSupport.class));
                break;
            case R.id.ivSettings:
            case R.id.tvSettings:
            case R.id.tvSettingstext:
            case R.id.ivSettingsGoRight:
                startActivity(new Intent(nActivity, ActivityAccountsSettings.class));
                break;
            case R.id.tvUpdateProfile:
                if (SystemClock.elapsedRealtime() - mLastClickTimeUpdateProf < 2000) {
                    return;
                }
                mLastClickTimeUpdateProf = SystemClock.elapsedRealtime();
                startActivity(new Intent(nActivity, ActivityUpdateProfile.class).putExtra("fromScreen", "account"));
                break;
            case R.id.Conlay0:
                if (SystemClock.elapsedRealtime() - mLastClickTimePoint < 2000) {
                    return;
                }
                mLastClickTimePoint = SystemClock.elapsedRealtime();
                startActivity(new Intent(nActivity, PointBalanceActivity.class));
                break;
            case R.id.tvReferEarnTitle:
            case R.id.ivReferEarn:
            case R.id.ivReferEarnGo:
                if (SystemClock.elapsedRealtime() - mLastClickTimeRef < 2000) {
                    return;
                }
                mLastClickTimeRef = SystemClock.elapsedRealtime();
                startActivity(new Intent(nActivity, RefferAndEarnActivity.class));
                break;
            case R.id.tvRateUs:
                Uri uri = Uri.parse("market://details?id=" + nActivity.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + nActivity.getPackageName())));
                }
                break;
        }

    }
}
