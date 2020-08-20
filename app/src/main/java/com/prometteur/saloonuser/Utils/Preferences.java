package com.prometteur.saloonuser.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class Preferences {
    public static final String PREFERENCES_KEY="SalonCP";

    public static void setPreferenceValue(Context context, String key,
                                          String value) {

        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_KEY,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }
public static void setPreferenceValueSet(Context context, String key,
                                         Set<String> value) {

        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_KEY,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet(key, value);
        editor.commit();
    }


    public static String getPreferenceValue(Context context, String key) {

        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_KEY,
                Context.MODE_PRIVATE);

        return prefs.getString(key, "NA");

    }
    public static Set<String> getPreferenceValueSet(Context context, String key) {

        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_KEY,
                Context.MODE_PRIVATE);

        return prefs.getStringSet(key,null);

    }

    public static void setPreferenceValue(Context context, String key, int value) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_KEY,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getPreferenceValueInt(Context context, String key) {

        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_KEY,
                Context.MODE_PRIVATE);

        return prefs.getInt(key, 0);

    }

    public static void getClearPrefs(Context context) {

        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_KEY,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();

    }
}