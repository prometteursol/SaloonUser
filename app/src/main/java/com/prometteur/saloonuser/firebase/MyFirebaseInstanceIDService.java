package com.prometteur.saloonuser.firebase;

import android.content.Intent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.common.internal.Constants;
import com.google.firebase.iid.FirebaseInstanceId;

import static com.prometteur.saloonuser.Constants.ConstantVariables.REGISTRATION_COMPLETE;


public class MyFirebaseInstanceIDService/* extends FirebaseInstanceIdService*/ {

   /* private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        //super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();



        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

*/


}

