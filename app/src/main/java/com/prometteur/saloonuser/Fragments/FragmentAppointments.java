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
import androidx.viewpager.widget.ViewPager;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.prometteur.saloonuser.Activities.ActivityAppointmentDetails;
import com.prometteur.saloonuser.Activities.ActivityHomepage;
import com.prometteur.saloonuser.Activities.RejectionActivity;
import com.prometteur.saloonuser.Adapter.AppointmentListPagerAdapter;
import com.prometteur.saloonuser.Adapter.AppointmentStatusPagerAdapter;
import com.prometteur.saloonuser.Adapter.FixedAppointmentsAdapter;
import com.prometteur.saloonuser.Model.AppointmentBean;
import com.prometteur.saloonuser.Model.CouponBean;
import com.prometteur.saloonuser.Model.OrderIdBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.LinePagerIndicatorDecoration;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;
import com.prometteur.saloonuser.databinding.FragmentAppointmentsBinding;
import com.prometteur.saloonuser.listener.OnTabRemoveListener;
import com.prometteur.saloonuser.retrofit.ApiInterface;
import com.prometteur.saloonuser.retrofit.RetrofitInstance;
import com.prometteur.saloonuser.retrofit.RetrofitInstanceForPay;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;

import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.prometteur.saloonuser.Activities.ActivityHomepage.strLat;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.strLong;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERIDVAL;
import static com.prometteur.saloonuser.Fragments.FragmentListSalonView.lat;
import static com.prometteur.saloonuser.Fragments.FragmentListSalonView.lon;
import static com.prometteur.saloonuser.Utils.Utils.isNetworkAvailable;
import static com.prometteur.saloonuser.Utils.Utils.logout;
import static com.prometteur.saloonuser.Utils.Utils.showFailToast;
import static com.prometteur.saloonuser.Utils.Utils.showNoInternetDialog;
import static com.prometteur.saloonuser.Utils.Utils.showProgress;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAppointments extends Fragment implements OnTabRemoveListener {

    FragmentAppointmentsBinding appointmentsBinding;
    BottomSheetBehavior bottomBehaviour;
    Activity nActivity;

    public FragmentAppointments() {
        // Required empty public constructor
    }

    public FragmentAppointments(Activity nActivity) {
        this.nActivity = nActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        appointmentsBinding=FragmentAppointmentsBinding.inflate(inflater,container,false);
        View view=appointmentsBinding.getRoot();
        appointmentsBinding.vpAppointments.setVisibility(View.GONE);
        appointmentsBinding.layoutDot.setVisibility(View.GONE);
        appointmentsBinding.linTab.setPadding(0,20,0,0);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isNetworkAvailable(nActivity)) {
            getAppointments();
        } else {
            showNoInternetDialog(nActivity);
        }


       /* bottomBehaviour=BottomSheetBehavior.from(appointmentsBinding.nsvBottomsheet);
j
        bottomBehaviour.setPeekHeight(465);
        bottomBehaviour.setHideable(false);*/

        appointmentsBinding.tabAppointmentStatus.addTab(
                appointmentsBinding.tabAppointmentStatus.newTab().setText(nActivity.getResources().getString(
                        R.string.Completed
                )));
        appointmentsBinding.tabAppointmentStatus.addTab(
                appointmentsBinding.tabAppointmentStatus.newTab().setText(nActivity.getResources().getString(
                        R.string.Canceled)));

        final AppointmentStatusPagerAdapter pagerAdapter = new AppointmentStatusPagerAdapter(
                getActivity().getSupportFragmentManager(), appointmentsBinding.tabAppointmentStatus.getTabCount(),nActivity);

        appointmentsBinding.vpAppointmentsList.setAdapter(pagerAdapter);
        //appointmentsBinding.vpAppointmentsList.measure(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
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
       // appointmentsBinding.vpAppointmentsList.setOffscreenPageLimit(2);


        appointmentsBinding.vpAppointments.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
Context context;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.nActivity= (Activity) context;
        this.context=context;
    }

    AppointmentBean appointmentBean;
    public static AppointmentListPagerAdapter appointmentTabAdapter;
    private void getAppointments() {

        final ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        final Dialog progressDialog = showProgress(context, 0);
        if(ActivityHomepage.menuPos==1) {
            progressDialog.show();
        }
        service.getAppointments(USERIDVAL,strLat,strLong)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AppointmentBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(AppointmentBean loginBeanObj) {
                        progressDialog.dismiss();
                        appointmentBean = loginBeanObj;
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        showFailToast(context, nActivity.getResources().getString(R.string.went_wrong));
                    }

                    @Override
                    public void onComplete() {
                        // Updates UI with data
                        progressDialog.dismiss();

                        if (appointmentBean.getStatus() == 1) {

                            if(appointmentBean.getOngoingAppointments().size()!=0) {
                                 appointmentTabAdapter = new AppointmentListPagerAdapter(((AppCompatActivity)context).getSupportFragmentManager(), FragmentAppointments.this);
                                for (int i = 0; i < appointmentBean.getOngoingAppointments().size(); i++) {
                                    AppointmentLisrFragmentDetails appointmentBottomFragmentDetails = new AppointmentLisrFragmentDetails();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("dynamicData", "" + (i + 1));
                                    bundle.putSerializable("appointDetails", appointmentBean.getOngoingAppointments().get(i));
                                    bundle.putSerializable("appointBeans", appointmentBean);
                                    appointmentBottomFragmentDetails.setArguments(bundle);
                                    appointmentTabAdapter.addCompetetorTabFragments(appointmentBottomFragmentDetails, "Dashboard");
                                }
                                //appointmentTabAdapter.notifyDataSetChanged();
                                appointmentsBinding.vpAppointments.setClipToPadding(false);
                                appointmentsBinding.vpAppointments.setOffscreenPageLimit(appointmentBean.getOngoingAppointments().size());
                                appointmentsBinding.vpAppointments.setAdapter(appointmentTabAdapter);
                                addDot(0);
                                appointmentsBinding.vpAppointments.setVisibility(View.VISIBLE);
                                appointmentsBinding.layoutDot.setVisibility(View.VISIBLE);
                                appointmentsBinding.linTab.setPadding(0,0,0,0);
                            }else
                            {
                                appointmentsBinding.vpAppointments.setVisibility(View.GONE);
                                appointmentsBinding.layoutDot.setVisibility(View.GONE);
                                appointmentsBinding.linTab.setPadding(0,20,0,0);
                            }
                           // appointmentsBinding.vpAppointments.setPageMargin(-24);
                            //appointmentsBinding.vpAppointments.setPadding(48, 8, 48, 8);

                            /*LinearLayoutManager layoutManager=new LinearLayoutManager(nActivity);
                            layoutManager.setOrientation(RecyclerView.HORIZONTAL);
                            appointmentsBinding.recycleFixedAppointments.setLayoutManager(layoutManager);
                            appointmentsBinding.recycleFixedAppointments.setAdapter(new FixedAppointmentsAdapter(nActivity,appointmentBean.getOngoingAppointments(),
                                    new FixedAppointmentsAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(int position) {
                                            Intent intent=new Intent(nActivity, ActivityAppointmentDetails.class);
                                            intent.putExtra("appId",appointmentBean.getOngoingAppointments().get(position).getAptId());
                                            //intent.putExtra(fromAppointmentCompleted,fromAppointmentCompleted);
                                            startActivity(intent);
                                        }
                                    }));
                            appointmentsBinding.recycleFixedAppointments.addItemDecoration(new LinePagerIndicatorDecoration());*/
                        } else if (appointmentBean.getStatus() == 3) {
                            showFailToast(context, "" + appointmentBean.getMsg());
                              logout(context);
                        }
                        //setEmptyMsg(mDataList, comboPageBinding.recycleCombolist, ivNoData);
                    }
                });
    }

    @Override
    public void onTabRemoved(int size) {

    }


    public void addDot(int page_position) {
       TextView[] dot = new TextView[appointmentBean.getOngoingAppointments().size()];
        appointmentsBinding.layoutDot.removeAllViews();

        for (int i = 0; i < dot.length; i++) {;
            dot[i] = new TextView(nActivity);
            dot[i].setText(Html.fromHtml("&#9673;"));
            dot[i].setTextSize(15);
            dot[i].setTextColor(context.getResources().getColor(R.color.darkGray));
            dot[i].setPadding(2,0,2,0);
            appointmentsBinding.layoutDot.addView(dot[i]);
        }
        //active dot
        dot[page_position].setTextColor(context.getResources().getColor(R.color.skyBlueDark));
    }


}
