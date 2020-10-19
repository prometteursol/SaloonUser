package com.prometteur.saloonuser.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.prometteur.saloonuser.Adapter.NotificationsListAdapter;
import com.prometteur.saloonuser.Model.NotificationBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.NetworkChangeReceiver;
import com.prometteur.saloonuser.databinding.ActivityAboutusBinding;
import com.prometteur.saloonuser.databinding.ActivityNotificationsBinding;
import com.prometteur.saloonuser.retrofit.ApiInterface;
import com.prometteur.saloonuser.retrofit.RetrofitInstance;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.prometteur.saloonuser.Constants.ConstantVariables.USERIDVAL;
import static com.prometteur.saloonuser.Fragments.FragmentListSalonView.listSalonBinding;
import static com.prometteur.saloonuser.Utils.Utils.isNetworkAvailable;
import static com.prometteur.saloonuser.Utils.Utils.logout;
import static com.prometteur.saloonuser.Utils.Utils.setEmptyMsg;
import static com.prometteur.saloonuser.Utils.Utils.showFailToast;
import static com.prometteur.saloonuser.Utils.Utils.showNoInternetDialog;
import static com.prometteur.saloonuser.Utils.Utils.showProgress;

public class ActivityAboutUs extends AppCompatActivity implements View.OnClickListener {

    ActivityAboutusBinding aboutusBinding;
    Activity nActivity;
List<NotificationBean.Result> notificationsArrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aboutusBinding = ActivityAboutusBinding.inflate(getLayoutInflater());
        View view = aboutusBinding.getRoot();
        setContentView(view);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        nActivity=this;


        String text="Who loves being pampered ? we do ! <br/>" +
                "Nothing can replace the awesome feeling we get after a relaxing SPA or a grooming session at the Salon. Most of us prefer to visit a particular Salon/SPA regularly. But what really prevents us from trying something new ? Is it a lack of trust and information about the new facility ? Do they use my preferred products ? Are the therapists good enough for me ? Are there offers, even at my existing Salon ? <br/>" +
                "Well, As an answer to such questions and much more we present to you "+"<font color='black'><b>mooi</b></font>"+" (<i>pronounced as moy/मॉय</i>)<br/>" +
                "mooi aims to enhance your grooming and relaxation experience through a platform that answers all the above and lets you book your appointments with your preferred Salon and Spa at the click of a button.<br/>" +
                "mooi lets you search Salon/SPA by their locality, give you trusted ratings from people who have completed services, details of brands etc. etc. <br/>" +
                "Wait ! it's not over, you can book services with your preferred therapist, for a particular brand of product at your preferred time. To top it all you pay only after completion of the service at the Salon or on our payment gateway in which case you earn exciting reward points.<br/>" +
                "Let's enjoy this beautiful and relaxed journey together !<br/>" +
                "mooi !";
        //aboutusBinding.tvAbout.setText(Html.fromHtml(getString(R.string.about_us)));
        aboutusBinding.tvAbout.setText(Html.fromHtml(text));
        aboutusBinding.ivBackArrowimg.setOnClickListener(ActivityAboutUs.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivBackArrowimg:
                finish();
                break;
        }
    }




}
