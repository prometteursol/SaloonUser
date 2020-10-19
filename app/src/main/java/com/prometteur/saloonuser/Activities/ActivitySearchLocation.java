package com.prometteur.saloonuser.Activities;

import android.app.Activity;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.prometteur.saloonuser.Adapter.LocationSearchListAdapter;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.NetworkChangeReceiver;
import com.prometteur.saloonuser.databinding.ActivitySearchLocationBinding;

public class ActivitySearchLocation extends AppCompatActivity implements View.OnClickListener {

    ActivitySearchLocationBinding searchBinding;
    Activity nActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchBinding = ActivitySearchLocationBinding.inflate(getLayoutInflater());
        View view = searchBinding.getRoot();
        setContentView(view);
        nActivity=this;

        searchBinding.ivBackArrowimg.setOnClickListener(this);
        searchBinding.recycleAddressResults.setLayoutManager(new LinearLayoutManager(this));
        searchBinding.recycleAddressResults.setAdapter(new LocationSearchListAdapter(nActivity));


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivBackArrowimg:
                finish();
                break;
        }

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
