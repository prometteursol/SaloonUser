package com.prometteur.saloonuser.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.prometteur.saloonuser.Model.MapClusterItem;
import com.prometteur.saloonuser.R;

import java.util.HashMap;
import java.util.Map;

import static com.prometteur.saloonuser.Constants.ConstantMethods.getResizedDrawable;

public class MyCustomAdapterForItems implements GoogleMap.InfoWindowAdapter {
    private final Map<Marker, Bitmap> images = new HashMap<>();
    private final Map<Marker, Target<Bitmap>> targets = new HashMap<>();
    private final View myContentsView;
    private Marker marker;
    private MapClusterItem clickedClusterItem;
    private Context mContext;

    public MyCustomAdapterForItems(Context mContext, MapClusterItem clickedClusterItem) {
        this.mContext = mContext;
        this.clickedClusterItem = clickedClusterItem;

        myContentsView = LayoutInflater.from(mContext).inflate(R.layout.cluster_info_window, null);

        myContentsView.setBackgroundColor(Color.TRANSPARENT);
    }


    @Override
    public View getInfoContents(final Marker marker) {
        this.marker = marker;
        TextView tvSalonName = (myContentsView.findViewById(R.id.tvSalonName));
        final ImageView IV_hotel_photo = myContentsView.findViewById(R.id.IV_hotel_photo);
        TextView tvDistance = (myContentsView.findViewById(R.id.tvDistance));
        RatingBar ratingBar=myContentsView.findViewById(R.id.ratingBar);
        getResizedDrawable(tvDistance.getContext(), R.drawable.ic_location_distance_icon, tvDistance, null, null, R.dimen._14sdp);
        if (clickedClusterItem.getHotelGalaryPhotos() != null) {
            if (marker.isInfoWindowShown()) {
                marker.hideInfoWindow();
            }
            Glide.with(mContext).load(clickedClusterItem.getHotelGalaryPhotos())
                    .override(80, 80) // made the difference
                    .thumbnail(0.5f)
                    .placeholder(R.drawable.placeholder_gray_corner)
                    .error(R.drawable.placeholder_gray_corner).into(IV_hotel_photo);
            //  Log.e("img",clickedClusterItem.getHotelGalaryPhotos());
          /*  Glide.with(mContext)
                    .asBitmap()
                    .override(80, 80) // made the difference
                    .thumbnail(0.5f)
                    .load(clickedClusterItem.getHotelGalaryPhotos())
                    .placeholder(R.drawable.img_login)
                    .error(R.drawable.img_login)
                    .into(new Target<Bitmap>() {
                        @Override
                        public void onLoadStarted(@Nullable Drawable placeholder) {

                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            IV_hotel_photo.setImageResource(R.drawable.img_login);

                        }

                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            IV_hotel_photo.setImageBitmap(resource);

                            if (marker.isInfoWindowShown()) {
                                marker.hideInfoWindow();
                                marker.showInfoWindow();

                            }

                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            IV_hotel_photo.setImageResource(R.drawable.img_login);

                        }

                        @Override
                        public void getSize(@NonNull SizeReadyCallback cb) {

                        }

                        @Override
                        public void removeCallback(@NonNull SizeReadyCallback cb) {

                        }

                        @Override
                        public void setRequest(@Nullable Request request) {

                        }

                        @Nullable
                        @Override
                        public Request getRequest() {
                            return null;
                        }

                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onStop() {

                        }

                        @Override
                        public void onDestroy() {

                        }
                    });*/
        } else {
            IV_hotel_photo.setImageResource(R.drawable.img_mooi_logo_bg);
        }


        tvSalonName.setText(clickedClusterItem.getHotel_title());
        tvDistance.setText(clickedClusterItem.getHotelCity() + " KM");
        if(clickedClusterItem.getHotelCountry()!=null) {
            ratingBar.setRating(Float.parseFloat(clickedClusterItem.getHotelCountry()));
        }
        return myContentsView;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        this.marker = marker;
        return null;

    }


}
