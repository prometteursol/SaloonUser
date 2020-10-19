package com.prometteur.saloonuser.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;

import com.prometteur.saloonuser.R;

import static com.prometteur.saloonuser.Activities.ActivityHomepage.getLocationPermission;
import static com.prometteur.saloonuser.Fragments.FragmentListSalonView.getHomeData;
import static com.prometteur.saloonuser.Utils.Utils.dialog;

public class NetworkChangeReceiver extends BroadcastReceiver {

    public boolean isConnected = true;
    String status;
    Activity activity;
    Activity parent;

public NetworkChangeReceiver()
{

}

    public NetworkChangeReceiver(Activity a) {
        // TODO Auto-generated constructor stub
        parent = a;
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {

        activity = (Activity) context;
        status = Utils.getConnectivityStatusString(context);
        if (status.equals("Not connected to Internet")) {
            //Toast.makeText(context, "Internet connection required", Toast.LENGTH_LONG).show();
        }
        ReturnStatus(status, context);
    }
public static boolean aptReschedule=false;
    public void ReturnStatus(String s, final Context cnt) {
        if (s.equals("Mobile data enabled")) {
            isConnected = true;

            try {
                if(dialog!=null) {
                    dialog.dismiss();
                    dialog=null;
                }
                if(!aptReschedule) {
                 //   getHomeData();
                    getLocationPermission();
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            //Intent intent = new Intent(activity,activity.getClass());
            //activity.startActivity(intent);

        } else if (s.equals("Wifi enabled")) {
            isConnected = true;

            try {
                if(dialog!=null) {
                    if(dialog.isShowing()) {
                        dialog.dismiss();
                        dialog=null;
                    }
                }
                if(!aptReschedule) {
                  //  getHomeData();
                    getLocationPermission();
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        } else {
            isConnected = false;


            if(dialog==null) {
                dialog = new Dialog(cnt);
            }
            dialog.setContentView(R.layout.dialog_no_internet);
            dialog.setCancelable(false);
            dialog.show();

        }
    }


}