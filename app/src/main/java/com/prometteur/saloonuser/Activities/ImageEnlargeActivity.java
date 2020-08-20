package com.prometteur.saloonuser.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.prometteur.saloonuser.Adapter.ImageAdapter;
import com.prometteur.saloonuser.R;

import java.util.List;


public class ImageEnlargeActivity extends AppCompatActivity {
    int VP_position = 0,isGal=0;

    public static List<String> GalaryPhotos = null;
    TextView TV_current_pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_enlarge);
        ViewPager vp_enlarge = findViewById(R.id.vp_enlarge);
        //  DotsIndicator dotsIndicator = findViewById(R.id.dots_indicator);
        ImageView IV_back = findViewById(R.id.ivBackArrowimg);
        TV_current_pic = findViewById(R.id.TV_current_pic);
        //Extract the data…
        Bundle bundle = getIntent().getExtras();
        try {
            if (bundle != null) {
                VP_position = bundle.getInt("pager_position", 0);
                isGal = bundle.getInt("isGal", 0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        ImageAdapter adapter = new ImageAdapter(this, GalaryPhotos, VP_position);

        vp_enlarge.setAdapter(adapter);
        vp_enlarge.setCurrentItem(VP_position);
        TV_current_pic.setText(VP_position+1 + "/" + GalaryPhotos.size());

        vp_enlarge.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                TV_current_pic.setText(i + 1 + "/" + GalaryPhotos.size());


            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        //  dotsIndicator.setViewPager(vp_enlarge);


        IV_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
