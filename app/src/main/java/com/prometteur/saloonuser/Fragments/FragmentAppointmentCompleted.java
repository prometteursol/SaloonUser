package com.prometteur.saloonuser.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prometteur.saloonuser.Activities.ActivityAppointmentDetails;
import com.prometteur.saloonuser.Activities.RejectionActivity;
import com.prometteur.saloonuser.Adapter.AppointmentStatusAdapter;
import com.prometteur.saloonuser.Adapter.FixedAppointmentsAdapter;
import com.prometteur.saloonuser.Model.AppointmentBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.LinePagerIndicatorDecoration;

import com.prometteur.saloonuser.databinding.FragmentAppointmentCompletedBinding;
import com.prometteur.saloonuser.retrofit.ApiInterface;
import com.prometteur.saloonuser.retrofit.RetrofitInstance;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.prometteur.saloonuser.Activities.ActivityHomepage.menuPos;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.strLat;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.strLong;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERIDVAL;
import static com.prometteur.saloonuser.Constants.ConstantVariables.fromAppointmentCompleted;
import static com.prometteur.saloonuser.Fragments.FragmentListSalonView.lat;
import static com.prometteur.saloonuser.Fragments.FragmentListSalonView.lon;
import static com.prometteur.saloonuser.Utils.Utils.isNetworkAvailable;
import static com.prometteur.saloonuser.Utils.Utils.logout;
import static com.prometteur.saloonuser.Utils.Utils.setEmptyMsg;
import static com.prometteur.saloonuser.Utils.Utils.showFailToast;
import static com.prometteur.saloonuser.Utils.Utils.showNoInternetDialog;
import static com.prometteur.saloonuser.Utils.Utils.showProgress;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAppointmentCompleted extends Fragment {

    FragmentAppointmentCompletedBinding appCompletedBinding;
    Activity nActivity;

    public FragmentAppointmentCompleted() {
        // Required empty public constructor
    }

    public FragmentAppointmentCompleted(Activity nActivity) {
        this.nActivity = nActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        appCompletedBinding = FragmentAppointmentCompletedBinding.inflate(inflater, container, false);
        View view = appCompletedBinding.getRoot();


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appCompletedBinding.pullToRefresh.setNestedScrollingEnabled(false);
        appCompletedBinding.pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to make your refresh action
                if (isNetworkAvailable(nActivity)) {
                    getAppointments();
                } else {
                    showNoInternetDialog(nActivity);
                }
                if(appCompletedBinding.pullToRefresh.isRefreshing()) {
                    appCompletedBinding.pullToRefresh.setRefreshing(false);
                }
            }
        });
        if (isNetworkAvailable(nActivity)) {
            getAppointments();
        } else {
            showNoInternetDialog(nActivity);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        nActivity= (Activity) context;
    }

    AppointmentBean appointmentBean;
    List<AppointmentBean.Result> results;
    long mLastClickTimeAppoint=0;
    private void getAppointments() {
        results=new ArrayList<>();
        final ApiInterface service = RetrofitInstance.getClient().create(ApiInterface.class);
        final Dialog progressDialog = showProgress(nActivity, 0);
        if(menuPos ==2) {
            progressDialog.show();
        }
        service.getAppointments(USERIDVAL,"4",strLat,strLong)
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
                       // showFailToast(nActivity, nActivity.getResources().getString(R.string.went_wrong));
                    }

                    @Override
                    public void onComplete() {
                        // Updates UI with data
                        progressDialog.dismiss();

                        if (appointmentBean.getStatus() == 1) {
                            results=appointmentBean.getResult();
                            LinearLayoutManager layoutManager=new LinearLayoutManager(nActivity);
                            layoutManager.setOrientation(RecyclerView.HORIZONTAL);
                            appCompletedBinding.recycleAppointmentStatus.setLayoutManager(new LinearLayoutManager(nActivity));
                            appCompletedBinding.recycleAppointmentStatus.setAdapter(new AppointmentStatusAdapter(nActivity,true,results, new AppointmentStatusAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    if (SystemClock.elapsedRealtime() - mLastClickTimeAppoint < 2000) {
                                        return;
                                    }
                                    mLastClickTimeAppoint = SystemClock.elapsedRealtime();
                                    Intent intent=new Intent(nActivity, ActivityAppointmentDetails.class);
                                    intent.putExtra("appId",appointmentBean.getResult().get(position).getAptId());
                                    intent.putExtra(fromAppointmentCompleted,fromAppointmentCompleted);
                                    startActivity(intent);
                                }
                            }));
                        } else if (appointmentBean.getStatus() == 3) {
                            showFailToast(nActivity, "" + appointmentBean.getMsg());
                              logout(nActivity);
                        }
                        appCompletedBinding.layNoData.ivNoData.setImageResource(R.drawable.img_completed_list_empty);
                        setEmptyMsg(results, appCompletedBinding.recycleAppointmentStatus, appCompletedBinding.layNoData.ivNoData);
                    }
                });
    }
}
