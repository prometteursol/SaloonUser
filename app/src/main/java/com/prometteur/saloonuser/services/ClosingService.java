package com.prometteur.saloonuser.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.prometteur.saloonuser.Utils.Preferences;

import java.util.ArrayList;

import static com.prometteur.saloonuser.Activities.ActivityHomepage.brands;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.brandsArr;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.category;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.discount;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.gender;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.highToLow;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.lowToHigh;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.rating;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.selectedCats;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.selectedCatsText;
import static com.prometteur.saloonuser.Activities.ActivityHomepage.sortBy;

public class ClosingService extends Service {
    private static final String TAG = ClosingService.class.getSimpleName();
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand()");
        return START_NOT_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.i(TAG,"Removed");
        // Handle application closing
        try {
            category = "";
            brands = "";
            rating = "";
            discount = "";
            sortBy = "";
            gender = "";
            lowToHigh = "";
            highToLow = "";
            brandsArr = new ArrayList<>();
            selectedCats = new ArrayList<>();
            selectedCatsText = new ArrayList<>();

            Preferences.setPreferenceValue(this, "genderFilter", "");
            Preferences.setPreferenceValue(this, "categoryFilter", "");
            Preferences.setPreferenceValue(this, "brandsFilter", "");
            Preferences.setPreferenceValue(this, "ratingFilter", "");
            Preferences.setPreferenceValue(this, "discountFilter", "");
            Preferences.setPreferenceValue(this, "sortByFilter", "");
            Preferences.setPreferenceValue(this, "sortByLowToHigh", "");
            Preferences.setPreferenceValue(this, "sortByHighToLow", "");
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        // Destroy the service
        stopSelf();
    }
}
