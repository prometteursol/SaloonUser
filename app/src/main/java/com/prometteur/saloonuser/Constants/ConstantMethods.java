package com.prometteur.saloonuser.Constants;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.prometteur.saloonuser.R;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConstantMethods {

    public static void getBottomNavigationCount(Context mContext, BottomNavigationView bottomNavigationView)
    {
        BottomNavigationMenuView mbottomNavigationMenuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        View chatBadge = LayoutInflater.from(mContext).inflate(R.layout.layout_appointment,
                mbottomNavigationMenuView, false);
        BottomNavigationItemView itemView = (BottomNavigationItemView) mbottomNavigationMenuView.getChildAt(1);

        TextView tvUnreadChats = chatBadge.findViewById(R.id.tvUnreadChats);
        tvUnreadChats.setTextColor(mContext.getResources().getColor(R.color.white));
        tvUnreadChats.setText("1");//String that you want to show in badge
        itemView.addView(chatBadge);
    }

    public static void makePhoneCall(Activity nContext, int REQUEST_CALL, String MobileNum) {
        String number = "9511219268";
        if (number.trim().length() > 0) {

            if (ContextCompat.checkSelfPermission(nContext,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(nContext,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                String dial = "tel:" + number;
                nContext.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }

        } else {
            Toast.makeText(nContext, "Enter Phone Number", Toast.LENGTH_SHORT).show();
        }
    }

    public static void getStrikeString(TextView someTextView)
    {
        someTextView.setPaintFlags(someTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public static void getResizedDrawable(Context context, int imgId, TextView textView, Button button, EditText editText, int size)
    {
        Drawable img = context.getResources().getDrawable(imgId);
        if(button!=null) {
            img.setBounds(0, 0, (int) button.getContext().getResources().getDimension(size),(int) button.getContext().getResources().getDimension(size));
            button.setCompoundDrawables(img, null, null, null);
        }
        if(textView!=null) {
            img.setBounds(0, 0, (int) textView.getContext().getResources().getDimension(size),(int) textView.getContext().getResources().getDimension(size));
            textView.setCompoundDrawables(img, null, null, null);
        }
        if(editText!=null) {
            img.setBounds(0, 0, img.getIntrinsicWidth() * editText.getMeasuredHeight() / img.getIntrinsicHeight(), editText.getMeasuredHeight());
            editText.setCompoundDrawables(img, null, null, null);
        }

    }


    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static DateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy");

    public static String convertDateToString(String strDate) {
        Date date= null;
        try {
            date = dateFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String strDateFormatted = "";
        strDateFormatted = dateFormat1.format(date);
        return strDateFormatted;
    }
}
