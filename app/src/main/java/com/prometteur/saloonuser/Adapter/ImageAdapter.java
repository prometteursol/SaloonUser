package com.prometteur.saloonuser.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.prometteur.saloonuser.R;
import com.prometteur.saloonuser.Utils.TouchImageView;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by prometteur-3 on 13/2/18.
 */

public class ImageAdapter extends PagerAdapter {

    ArrayList<String> ImagesArray;
    String[] myImageList;
    ArrayList<String> bitmapArray;
    TouchImageView imageView;
    ProgressBar pb;
    String img;
    //  private Drawable[] ImagesArray;
    private LayoutInflater inflater;
    private Context context;
    List<String> galaryPhotos;
    int VP_position = 0;
    TextView TV_current_pic;

    public ImageAdapter(Context context, List<String> galaryPhotos, int VP_position) {

        this.context = context;
        inflater = LayoutInflater.from(context);
        this.galaryPhotos = galaryPhotos;
        this.VP_position = VP_position;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return galaryPhotos.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {


        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);


        imageView = imageLayout.findViewById(R.id.image);

        pb = imageLayout.findViewById(R.id.progress_bar);

        img = galaryPhotos.get(position);

        loadPicture(view, img, true);

        view.addView(imageLayout, 0);

        return imageLayout;
    }


    private void loadPicture(final View holder, String photoUrl, final Boolean shouldLoadAgain) {

        pb.setVisibility(View.VISIBLE);
        try {

            Glide.with(context)
                    .load(img)
                    .transition(DrawableTransitionOptions.withCrossFade()) //Optional
                    .skipMemoryCache(true)  //No memory cache
                    .diskCacheStrategy(DiskCacheStrategy.NONE)   //No disk cache
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            pb.setVisibility(View.GONE);
                            if (shouldLoadAgain)
                                loadPicture(holder, img, false);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            pb.setVisibility(View.GONE);

                            return false;
                        }
                    })
                    .into(imageView);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}