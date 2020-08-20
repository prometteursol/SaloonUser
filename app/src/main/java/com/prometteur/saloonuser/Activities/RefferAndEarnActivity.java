package com.prometteur.saloonuser.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prometteur.saloonuser.Adapter.ReviewListAdapter;
import com.prometteur.saloonuser.BuildConfig;
import com.prometteur.saloonuser.Model.ReviewBean;
import com.prometteur.saloonuser.Model.ReviewDetailsBean;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.Preferences;
import com.prometteur.saloonuser.Utils.TextViewCustomFont;
import com.prometteur.saloonuser.databinding.ActivityNotificationsBinding;
import com.prometteur.saloonuser.databinding.ActivityReferAndEarnBinding;
import com.prometteur.saloonuser.retrofit.ApiInterface;
import com.prometteur.saloonuser.retrofit.RetrofitInstance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.prometteur.saloonuser.Constants.ConstantVariables.REFERCODEKEY;
import static com.prometteur.saloonuser.Constants.ConstantVariables.REFERERPOINTKEY;
import static com.prometteur.saloonuser.Constants.ConstantVariables.USERIDVAL;
import static com.prometteur.saloonuser.Utils.Utils.logout;
import static com.prometteur.saloonuser.Utils.Utils.showFailToast;
import static com.prometteur.saloonuser.Utils.Utils.showProgress;
import static com.prometteur.saloonuser.Utils.Utils.showSuccessToast;


public class RefferAndEarnActivity extends AppCompatActivity {

    ActivityReferAndEarnBinding activityReferAndEarnBinding;

    String aptId="0";
    String branchId="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityReferAndEarnBinding = ActivityReferAndEarnBinding.inflate(getLayoutInflater());
        View view = activityReferAndEarnBinding.getRoot();
        setContentView(view);
        setToolbar();

        activityReferAndEarnBinding.tvCode.setText(Preferences.getPreferenceValue(RefferAndEarnActivity.this, REFERCODEKEY));
        activityReferAndEarnBinding.inputSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("earnCode", Preferences.getPreferenceValue(RefferAndEarnActivity.this, REFERCODEKEY));
                clipboard.setPrimaryClip(clip);
                showSuccessToast(RefferAndEarnActivity.this,"Code copied");
            }
        });

        activityReferAndEarnBinding.btnRefer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share();
            }
        });

        activityReferAndEarnBinding.ivBackArrowimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        activityReferAndEarnBinding.tvTitle.setText("Refer your friends and get "+Preferences.getPreferenceValue(RefferAndEarnActivity.this,REFERERPOINTKEY)+" points each");
    }

    public void setToolbar() {
        /*setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Refer & Earn");*/
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (Build.VERSION_CODES.P == currentapiVersion) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        }

    }
    @Override
    public boolean onSupportNavigateUp() {
        super.onSupportNavigateUp();
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       finish();
    }


    public void share()
    {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
            String shareMessage= "Join me on Mooi, use referral code - "+Preferences.getPreferenceValue(RefferAndEarnActivity.this, REFERCODEKEY)+" when you sign up. Download Mooi now: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "Choose one"));
        } catch(Exception e) {
            //e.toString();
        }
    }


}
