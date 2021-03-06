package com.prometteur.saloonuser.Adapter;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.prometteur.saloonuser.Model.HomeBean;
import com.prometteur.saloonuser.R;
import com.github.siyamed.shapeimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import static com.prometteur.saloonuser.Utils.Utils.showFailToast;

public class ViewPagerSliderAdapter extends PagerAdapter {
    private static final String TAG = "ViewPagerSliderAdapter";

    AppCompatActivity nContext;
    ArrayList<String> SalonList;
    Activity nActivity;
    boolean isMapFragment;
    LayoutInflater nLayoutInflater;
    List<HomeBean.Advertisement> advertisementList;

    public ViewPagerSliderAdapter(AppCompatActivity nContext, boolean isMapfargment, List<HomeBean.Advertisement> advertisementList) {
        this.nContext = nContext;
        this.isMapFragment = isMapfargment;
        this.advertisementList = advertisementList;

        nLayoutInflater = (LayoutInflater) nContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        if(advertisementList!=null) {
            return advertisementList.size();
        }else
        {
            return 0;
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==((ConstraintLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = nLayoutInflater.inflate(R.layout.pager_slider_item, container, false);

        RoundedImageView imageView = itemView.findViewById(R.id.rimSlideImage);
         Glide.with(nContext).load(advertisementList.get(position).getAdvImg()).error(R.drawable.img_mooi_logo_bg).into(imageView);
itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        String url =advertisementList.get(position).getAdvUrl();
        try {
            if(!url.isEmpty()) {
                openWebPage(url);
            }
        } catch (ActivityNotFoundException e) {
            // Chrome is probably not installed
        }
    }
});
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ConstraintLayout) object);
    }

    public void openWebPage(String url) {
        try {
            Uri webpage = Uri.parse(url);
            Intent myIntent = new Intent(Intent.ACTION_VIEW, webpage);
            nContext.startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            showFailToast(nContext, "No application can handle this request. Please install a web browser or check your URL.");
            e.printStackTrace();
        }
    }
}
