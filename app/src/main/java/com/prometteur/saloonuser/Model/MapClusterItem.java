package com.prometteur.saloonuser.Model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.clustering.ClusterItem;

public class MapClusterItem implements ClusterItem {

    private final LatLng location;
    private final String hotelTraderName;
    private final static int count = 0;
    private final String hotel_id;


    public String getHotel_title() {
        return hotelTraderName;
    }

    public String getHotel_id() {
        return hotel_id;
    }

    public double getLatitude() {
        return location.latitude;
    }

    public double getLongitude() {
        return location.longitude;
    }

    @Override
    public LatLng getPosition() {
        return location;
    }

    @Nullable
    @Override
    public String getTitle() {
        return null;
    }

    @Nullable
    @Override
    public String getSnippet() {
        return null;
    }


    @SerializedName("hotel_city")
    @Expose
    private String hotelCity;
    @SerializedName("hotel_internet_bookingengine")
    @Expose
    private String hotelInternetBookingengine;
    @SerializedName("hotel_country")
    @Expose
    private String hotelCountry;
    @SerializedName("hotel_address1")
    @Expose
    private String hotelAddress1;
    @SerializedName("hotel_galary_photos")
    @Expose
    private String hotelGalaryPhotos;
    @SerializedName("hotel_lat")
    @Expose
    private String hotelLat;
    @SerializedName("hotel_long")
    @Expose
    private String hotelLong;
    @SerializedName("hotel_avg_price")
    @Expose
    private String hotelAvgPrice;
    @SerializedName("hotel_min_price")
    @Expose
    private String hotelMinPrice;
    @SerializedName("hotel_max_price")
    @Expose
    private String hotelMaxPrice;
    private final static long serialVersionUID = 2216878181690488868L;


    public MapClusterItem(@NonNull LatLng location, String hotelTraderName, String hotel_id, String hotelCity, String hotelCountry, String hotelGalaryPhotos) {
        this.location = location;
        this.hotelTraderName = hotelTraderName;
        this.hotel_id = hotel_id;
        this.hotelCity = hotelCity;
        this.hotelCountry = hotelCountry;
        this.hotelGalaryPhotos=hotelGalaryPhotos;
    }

    public String getHotelTraderName() {
        return hotelTraderName;
    }


    public String getHotelCity() {
        return hotelCity;
    }

    public void setHotelCity(String hotelCity) {
        this.hotelCity = hotelCity;
    }

    public String getHotelInternetBookingengine() {
        return hotelInternetBookingengine;
    }

    public void setHotelInternetBookingengine(String hotelInternetBookingengine) {
        this.hotelInternetBookingengine = hotelInternetBookingengine;
    }

    public String getHotelCountry() {
        return hotelCountry;
    }

    public void setHotelCountry(String hotelCountry) {
        this.hotelCountry = hotelCountry;
    }

    public String getHotelAddress1() {
        return hotelAddress1;
    }

    public void setHotelAddress1(String hotelAddress1) {
        this.hotelAddress1 = hotelAddress1;
    }

    public String getHotelGalaryPhotos() {
        return hotelGalaryPhotos;
    }

    public void setHotelGalaryPhotos(String hotelGalaryPhotos) {
        this.hotelGalaryPhotos = hotelGalaryPhotos;
    }

    public String getHotelLat() {
        return hotelLat;
    }

    public void setHotelLat(String hotelLat) {
        this.hotelLat = hotelLat;
    }

    public String getHotelLong() {
        return hotelLong;
    }

    public void setHotelLong(String hotelLong) {
        this.hotelLong = hotelLong;
    }

    public String getHotelAvgPrice() {
        return hotelAvgPrice;
    }

    public void setHotelAvgPrice(String hotelAvgPrice) {
        this.hotelAvgPrice = hotelAvgPrice;
    }

    public String getHotelMinPrice() {
        return hotelMinPrice;
    }

    public void setHotelMinPrice(String hotelMinPrice) {
        this.hotelMinPrice = hotelMinPrice;
    }

    public String getHotelMaxPrice() {
        return hotelMaxPrice;
    }

    public void setHotelMaxPrice(String hotelMaxPrice) {
        this.hotelMaxPrice = hotelMaxPrice;
    }


}
