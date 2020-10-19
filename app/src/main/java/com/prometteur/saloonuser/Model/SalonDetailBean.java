package com.prometteur.saloonuser.Model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SalonDetailBean implements Serializable
{

@SerializedName("status")
@Expose
private Integer status;
@SerializedName("msg")
@Expose
private String msg;
@SerializedName("result")
@Expose
private List<Result> result = null;
private final static long serialVersionUID = 4914286311015681418L;

public Integer getStatus() {
return status;
}

public void setStatus(Integer status) {
this.status = status;
}

public String getMsg() {
return msg;
}

public void setMsg(String msg) {
this.msg = msg;
}

public List<Result> getResult() {
return result;
}

public void setResult(List<Result> result) {
this.result = result;
}


    public class Result implements Serializable
    {

        @SerializedName("bran_id")
        @Expose
        private String branId;
        @SerializedName("bran_name")
        @Expose
        private String branName;
        @SerializedName("bran_addr")
        @Expose
        private String branAddr;
        @SerializedName("bran_estblish_year")
        @Expose
        private String branEstblishYear;
        @SerializedName("bran_working_hrs")
        @Expose
        private String branWorkingHrs;
        @SerializedName("bran_off_day")
        @Expose
        private String branOffDay;
        @SerializedName("bran_holiday_from")
        @Expose
        private String branHolidayFrom;
        @SerializedName("bran_holiday_to")
        @Expose
        private String branHolidayTo;
        @SerializedName("bran_discription")
        @Expose
        private String branDiscription;
        @SerializedName("bran_img")
        @Expose
        private String branImg;
        @SerializedName("bran_gallary_img")
        @Expose
        private String branGallaryImg;
        @SerializedName("bran_open_status")
        @Expose
        private String branOpenStatus;
        @SerializedName("bran_city")
        @Expose
        private String branCity;
        @SerializedName("bran_lat")
        @Expose
        private String branLat;
        @SerializedName("bran_lon")
        @Expose
        private String branLon;
        @SerializedName("bran_state")
        @Expose
        private String branState;
        @SerializedName("bran_pin_code")
        @Expose
        private String branPinCode;
        @SerializedName("bran_res_parking")
        @Expose
        private String branResParking;
        @SerializedName("bran_tv")
        @Expose
        private String branTv;
        @SerializedName("bran_free_wifi")
        @Expose
        private String branFreeWifi;
        @SerializedName("bran_ac")
        @Expose
        private String branAc;
        @SerializedName("discount")
        @Expose
        private String discount;
        @SerializedName("combo_discount")
        @Expose
        private String comboDiscount;
        @SerializedName("offer_discount")
        @Expose
        private String offerDiscount;
        @SerializedName("closed")
        @Expose
        private String closed;
        @SerializedName("open_hrs")
        @Expose
        private String openHrs;
        @SerializedName("branch_rating")
        @Expose
        private Object branchRating;
        @SerializedName("review_count")
        @Expose
        private String reviewCount;
        @SerializedName("topservices")
        @Expose
        private List<Topservice> topservices = null;
        @SerializedName("reviews")
        @Expose
        private List<Review> reviews = null;
        @SerializedName("services_name")
        @Expose
        private ServicesName servicesName;
        private final static long serialVersionUID = -4150199646959200218L;
        @SerializedName("bran_cat_main")
        @Expose
        private String branCatMain;
        public String getBranCatMain() {
            return branCatMain;
        }

        public void setBranCatMain(String branCatMain) {
            this.branCatMain = branCatMain;
        }
        public String getBranId() {
            return branId;
        }

        public void setBranId(String branId) {
            this.branId = branId;
        }

        public String getBranName() {
            return branName;
        }

        public void setBranName(String branName) {
            this.branName = branName;
        }

        public String getBranAddr() {
            return branAddr;
        }

        public void setBranAddr(String branAddr) {
            this.branAddr = branAddr;
        }

        public String getBranLat() {
            return branLat;
        }

        public void setBranLat(String branLat) {
            this.branLat = branLat;
        }

        public String getBranLon() {
            return branLon;
        }

        public void setBranLon(String branLon) {
            this.branLon = branLon;
        }

        public String getBranEstblishYear() {
            return branEstblishYear;
        }

        public void setBranEstblishYear(String branEstblishYear) {
            this.branEstblishYear = branEstblishYear;
        }

        public String getBranWorkingHrs() {
            return branWorkingHrs;
        }

        public void setBranWorkingHrs(String branWorkingHrs) {
            this.branWorkingHrs = branWorkingHrs;
        }

        public String getClosed() {
            return closed;
        }

        public void setClosed(String closed) {
            this.closed = closed;
        }

        public String getOpenHrs() {
            return openHrs;
        }

        public void setOpenHrs(String openHrs) {
            this.openHrs = openHrs;
        }

        public String getBranOffDay() {
            return branOffDay;
        }

        public void setBranOffDay(String branOffDay) {
            this.branOffDay = branOffDay;
        }

        public String getBranHolidayFrom() {
            return branHolidayFrom;
        }

        public void setBranHolidayFrom(String branHolidayFrom) {
            this.branHolidayFrom = branHolidayFrom;
        }

        public String getBranHolidayTo() {
            return branHolidayTo;
        }

        public void setBranHolidayTo(String branHolidayTo) {
            this.branHolidayTo = branHolidayTo;
        }

        public String getBranDiscription() {
            return branDiscription;
        }

        public void setBranDiscription(String branDiscription) {
            this.branDiscription = branDiscription;
        }

        public String getBranImg() {
            return branImg;
        }

        public void setBranImg(String branImg) {
            this.branImg = branImg;
        }

        public String getBranGallaryImg() {
            return branGallaryImg;
        }

        public void setBranGallaryImg(String branGallaryImg) {
            this.branGallaryImg = branGallaryImg;
        }

        public String getBranOpenStatus() {
            return branOpenStatus;
        }

        public void setBranOpenStatus(String branOpenStatus) {
            this.branOpenStatus = branOpenStatus;
        }

        public String getBranCity() {
            return branCity;
        }

        public void setBranCity(String branCity) {
            this.branCity = branCity;
        }

        public String getBranState() {
            return branState;
        }

        public void setBranState(String branState) {
            this.branState = branState;
        }

        public String getBranPinCode() {
            return branPinCode;
        }

        public void setBranPinCode(String branPinCode) {
            this.branPinCode = branPinCode;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getComboDiscount() {
            return comboDiscount;
        }

        public void setComboDiscount(String comboDiscount) {
            this.comboDiscount = comboDiscount;
        }

        public String getOfferDiscount() {
            return offerDiscount;
        }

        public void setOfferDiscount(String offerDiscount) {
            this.offerDiscount = offerDiscount;
        }

        public Object getBranchRating() {
            return branchRating;
        }

        public void setBranchRating(Object branchRating) {
            this.branchRating = branchRating;
        }

        public String getReviewCount() {
            return reviewCount;
        }

        public void setReviewCount(String reviewCount) {
            this.reviewCount = reviewCount;
        }

        public List<Topservice> getTopservices() {
            return topservices;
        }

        public void setTopservices(List<Topservice> topservices) {
            this.topservices = topservices;
        }

        public List<Review> getReviews() {
            return reviews;
        }

        public void setReviews(List<Review> reviews) {
            this.reviews = reviews;
        }

        public ServicesName getServicesName() {
            return servicesName;
        }

        public void setServicesName(ServicesName servicesName) {
            this.servicesName = servicesName;
        }

        @SerializedName("bran_opened_on")
        @Expose
        private String branOpenedOn;
        public String getBranOpenedOn() {
            return branOpenedOn;
        }

        public void setBranOpenedOn(String branOpenedOn) {
            this.branOpenedOn = branOpenedOn;
        }

        public String getBranResParking() {
            return branResParking;
        }

        public void setBranResParking(String branResParking) {
            this.branResParking = branResParking;
        }

        public String getBranTv() {
            return branTv;
        }

        public void setBranTv(String branTv) {
            this.branTv = branTv;
        }

        public String getBranFreeWifi() {
            return branFreeWifi;
        }

        public void setBranFreeWifi(String branFreeWifi) {
            this.branFreeWifi = branFreeWifi;
        }

        public String getBranAc() {
            return branAc;
        }

        public void setBranAc(String branAc) {
            this.branAc = branAc;
        }
    }

    public class Service implements Serializable
    {

        @SerializedName("srvc_id")
        @Expose
        private String srvcId;
        @SerializedName("srvc_name")
        @Expose
        private String srvcName;
        @SerializedName("brnd_name")
        @Expose
        private String brndName;
        @SerializedName("srvc_estimate_time")
        @Expose
        private String srvcEstimateTime;
        @SerializedName("srvc_price")
        @Expose
        private String srvcPrice;
        @SerializedName("srvc_discount_price")
        @Expose
        private String srvcDiscountPrice;
        private final static long serialVersionUID = -6092730675593290711L;

        public String getSrvcId() {
            return srvcId;
        }

        public void setSrvcId(String srvcId) {
            this.srvcId = srvcId;
        }

        public String getSrvcName() {
            return srvcName;
        }

        public void setSrvcName(String srvcName) {
            this.srvcName = srvcName;
        }

        public String getBrndName() {
            return brndName;
        }

        public void setBrndName(String brndName) {
            this.brndName = brndName;
        }

        public String getSrvcEstimateTime() {
            return srvcEstimateTime;
        }

        public void setSrvcEstimateTime(String srvcEstimateTime) {
            this.srvcEstimateTime = srvcEstimateTime;
        }

        public String getSrvcPrice() {
            return srvcPrice;
        }

        public void setSrvcPrice(String srvcPrice) {
            this.srvcPrice = srvcPrice;
        }

        public String getSrvcDiscountPrice() {
            return srvcDiscountPrice;
        }

        public void setSrvcDiscountPrice(String srvcDiscountPrice) {
            this.srvcDiscountPrice = srvcDiscountPrice;
        }

    }

    public class ServicesName implements Serializable
    {

        @SerializedName("1")
        @Expose
        private String _1;
        @SerializedName("2")
        @Expose
        private String _2;
        @SerializedName("3")
        @Expose
        private String _3;
        @SerializedName("4")
        @Expose
        private String _4;
        @SerializedName("5")
        @Expose
        private String _5;
        private final static long serialVersionUID = -4952808921081076064L;

        public String get1() {
            return _1;
        }

        public void set1(String _1) {
            this._1 = _1;
        }

        public String get2() {
            return _2;
        }

        public void set2(String _2) {
            this._2 = _2;
        }

        public String get3() {
            return _3;
        }

        public void set3(String _3) {
            this._3 = _3;
        }

        public String get4() {
            return _4;
        }

        public void set4(String _4) {
            this._4 = _4;
        }

        public String get5() {
            return _5;
        }

        public void set5(String _5) {
            this._5 = _5;
        }

    }

    public class Topservice implements Serializable
    {

        @SerializedName("srvc_category")
        @Expose
        private String srvcCategory;
        @SerializedName("types")
        @Expose
        private String types;
        @SerializedName("services")
        @Expose
        private List<Service> services = null;
        private final static long serialVersionUID = 8609488235230864475L;

        public String getSrvcCategory() {
            return srvcCategory;
        }

        public void setSrvcCategory(String srvcCategory) {
            this.srvcCategory = srvcCategory;
        }

        public String getTypes() {
            return types;
        }

        public void setTypes(String types) {
            this.types = types;
        }

        public List<Service> getServices() {
            return services;
        }

        public void setServices(List<Service> services) {
            this.services = services;
        }

    }

    public class Review implements Serializable
    {

        @SerializedName("rev_id")
        @Expose
        private String revId;
        @SerializedName("user_f_name")
        @Expose
        private String userFName;
        @SerializedName("user_l_name")
        @Expose
        private String userLName;
        @SerializedName("user_img")
        @Expose
        private String userImg;
        @SerializedName("user_gender")
        @Expose
        private String userGender;
        @SerializedName("rev_review")
        @Expose
        private String revReview;
        @SerializedName("rev_rating")
        @Expose
        private String revRating;
        @SerializedName("rev_create_date")
        @Expose
        private String revCreateDate;
        private final static long serialVersionUID = 7025740488350187220L;

        public String getRevId() {
            return revId;
        }

        public void setRevId(String revId) {
            this.revId = revId;
        }

        public String getUserFName() {
            return userFName;
        }

        public void setUserFName(String userFName) {
            this.userFName = userFName;
        }

        public String getUserLName() {
            return userLName;
        }

        public void setUserLName(String userLName) {
            this.userLName = userLName;
        }

        public String getUserImg() {
            return userImg;
        }

        public void setUserImg(String userImg) {
            this.userImg = userImg;
        }

        public String getUserGender() {
            return userGender;
        }

        public void setUserGender(String userGender) {
            this.userGender = userGender;
        }

        public String getRevReview() {
            return revReview;
        }

        public void setRevReview(String revReview) {
            this.revReview = revReview;
        }

        public String getRevRating() {
            return revRating;
        }

        public void setRevRating(String revRating) {
            this.revRating = revRating;
        }

        public String getRevCreateDate() {
            return revCreateDate;
        }

        public void setRevCreateDate(String revCreateDate) {
            this.revCreateDate = revCreateDate;
        }

    }

}