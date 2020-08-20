package com.prometteur.saloonuser.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.prometteur.saloonuser.Activities.ActivityAppointmentDetails;
import com.prometteur.saloonuser.Activities.ActivityHomepage;
import com.prometteur.saloonuser.Activities.ActivityLogin;
import com.prometteur.saloonuser.Model.NotificationBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.Preferences;


import java.util.Random;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERID;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERIDVAL;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERSESSION;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERSESSIONVAL;


/**
 * Created by prometteur-3 on 24/4/18.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final String ADMIN_CHANNEL_ID = "admin_channel";

    public MyFirebaseMessagingService() {
        super();
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if(remoteMessage.getData()!=null) {
            Log.i("remotedata", remoteMessage.getData().toString());
        }
        USERIDVAL = com.prometteur.saloonuser.Utils.Preferences.getPreferenceValue(MyFirebaseMessagingService.this, USERID);
        USERSESSIONVAL = Preferences.getPreferenceValue(MyFirebaseMessagingService.this, USERSESSION);
        Intent intent = new Intent(this, ActivityAppointmentDetails.class);
        if(remoteMessage.getData().get("noti_appointment_id")!=null) {

            intent.putExtra("appId", "" + remoteMessage.getData().get("noti_appointment_id"));
            intent.putExtra("notiId", "" + remoteMessage.getData().get("noti_id"));

            Intent intent1 = new Intent(MyFirebaseMessagingService.this, ActivityAppointmentDetails.class);
            intent1.putExtra("appId", "" + remoteMessage.getData().get("noti_appointment_id"));
            intent1.putExtra("notiId", "" + remoteMessage.getData().get("noti_id"));
            intent1.addFlags(FLAG_ACTIVITY_NEW_TASK);
            intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            Log.i("push notification","MyFirebaseMessagingService");
            startActivity(intent1);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationID = new Random().nextInt(3000);

      /*
        Apps targeting SDK 26 or above (Android O) must implement notification channels and add its notifications
        to at least one of them. Therefore, confirm if version is Oreo or higher, then setup notification channel
      */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels(notificationManager);
        }
        try {

         //   String userId = Preferences.getPreferenceValue(MyFirebaseMessagingService.this,"userId");


            if (null != remoteMessage) {
                /* if (remoteMessage.getData().get("messageids").contains(userId)) {*/
                Log.i("remoteData",remoteMessage.getData().toString());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                        PendingIntent.FLAG_ONE_SHOT);

                Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
                        R.drawable.salon_logo_tr);

                Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                        .setSmallIcon(R.drawable.salon_logo_tr)
                        .setLargeIcon(largeIcon)
                        .setContentTitle(remoteMessage.getData().get("title"))
                        .setContentText(remoteMessage.getData().get("message"))
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(remoteMessage.getData().get("message")))
                        .setAutoCancel(true)
                        .setSound(notificationSoundUri)
                        .setContentIntent(pendingIntent);

                //Set notification color to match your app color template
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    notificationBuilder.setSmallIcon(R.drawable.salon_logo_tr);
                    notificationBuilder.setColor(getResources().getColor(R.color.skyBlueDark));
                } else {
                    notificationBuilder.setSmallIcon(R.drawable.salon_logo_tr);
                }
                notificationManager.notify(notificationID, notificationBuilder.build());

                Intent intent2=new Intent("SendToService");
                sendBroadcast(intent2);
               /* } else {
{noti_title=Appointment Request, noti_create_date=2020-03-13 15:04:11, noti_cp_user_id=1, title=Appointment Request, noti_end_user_id=1, noti_appointment_id=78, noti_message=You have new appointment request, message=You have new appointment request, noti_from=2, noti_type=1, noti_branch_id=1, noti_to=1}
                }*/
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(NotificationManager notificationManager) {
        CharSequence adminChannelName = "My Event";
        String adminChannelDescription = "";

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_HIGH);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }

    @Override
    public void onNewToken(String token) {
        Log.i("Notification", "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
    }
}

