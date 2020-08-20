package com.prometteur.saloonuser.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static com.prometteur.saloonuser.Activities.ActivitySalonServices.nActivity;
import static com.prometteur.saloonuser.Fragments.FragmentListSalonView.getHomeData;
import static com.prometteur.saloonuser.Utils.Utils.dialog;
import static com.prometteur.saloonuser.Utils.Utils.isNetworkAvailable;
import static com.prometteur.saloonuser.Utils.Utils.showNoInternetDialogReceiver;

public class NetworkChangeReceiver extends BroadcastReceiver {
public NetworkChangeReceiver(){
            super();
        }
        @Override
        public void onReceive(final Context context, final Intent intent) {
            if (isNetworkAvailable(context)) {
                if(dialog!=null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                        dialog=null;
                    }
                    getHomeData();
                }
            } else {
                showNoInternetDialogReceiver(context);
            }
        }
    }