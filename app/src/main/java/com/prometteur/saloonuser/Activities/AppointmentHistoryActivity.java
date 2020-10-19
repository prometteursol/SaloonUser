package com.prometteur.saloonuser.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;
import com.prometteur.saloonuser.Adapter.AppointmentStatusPagerAdapter;
import com.prometteur.saloonuser.Adapter.FixedAppointmentsAdapter;
import com.prometteur.saloonuser.Model.AppointmentBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.LinePagerIndicatorDecoration;
import com.prometteur.saloonuser.Utils.NetworkChangeReceiver;
import com.prometteur.saloonuser.databinding.FragmentAppointmentsBinding;
import com.prometteur.saloonuser.retrofit.ApiInterface;
import com.prometteur.saloonuser.retrofit.RetrofitInstance;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.prometteur.saloonuser.Utils.Utils.logout;
import static com.prometteur.saloonuser.Utils.Utils.showFailToast;
import static com.prometteur.saloonuser.Utils.Utils.showProgress;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppointmentHistoryActivity extends AppCompatActivity {

    FragmentAppointmentsBinding appointmentsBinding;

    public AppointmentHistoryActivity() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appointmentsBinding=FragmentAppointmentsBinding.inflate(getLayoutInflater());
        View view=appointmentsBinding.getRoot();
        setContentView(view);
     //   getAppointments();

       /* bottomBehaviour=BottomSheetBehavior.from(appointmentsBinding.nsvBottomsheet);
j
        bottomBehaviour.setPeekHeight(465);
        bottomBehaviour.setHideable(false);*/
        appointmentsBinding.ivBackArrowimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        appointmentsBinding.ivBackArrowimg.setVisibility(View.VISIBLE);
        appointmentsBinding.pullToRefresh.setVisibility(View.GONE);
        appointmentsBinding.layoutDot.setVisibility(View.GONE);
        appointmentsBinding.linTab.setPadding(0,20,0,0);
        appointmentsBinding.tvTitle.setText("Appointment History");
        appointmentsBinding.tabAppointmentStatus.addTab(
                appointmentsBinding.tabAppointmentStatus.newTab().setText(getResources().getString(
                        R.string.Completed
                )));
        appointmentsBinding.tabAppointmentStatus.addTab(
                appointmentsBinding.tabAppointmentStatus.newTab().setText(getResources().getString(
                        R.string.Canceled)));

        final AppointmentStatusPagerAdapter pagerAdapter = new AppointmentStatusPagerAdapter(
               getSupportFragmentManager(), appointmentsBinding.tabAppointmentStatus.getTabCount(),AppointmentHistoryActivity.this);

        appointmentsBinding.vpAppointmentsList.setAdapter(pagerAdapter);

        appointmentsBinding.vpAppointmentsList.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(appointmentsBinding.tabAppointmentStatus));

        appointmentsBinding.tabAppointmentStatus.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                appointmentsBinding.vpAppointmentsList.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }


        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkInternet();
    }

    NetworkChangeReceiver receiver;
    public void checkInternet() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver(this);
        registerReceiver(receiver, filter);
    }
    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(receiver);
        } catch (Exception e) {

        }
    }


}
