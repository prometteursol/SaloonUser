package com.prometteur.saloonuser.Model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HomeBean implements Serializable
{

@SerializedName("status")
@Expose
private Integer status;
@SerializedName("msg")
@Expose
private String msg;
@SerializedName("referrer_point")
@Expose
private String referrerPoint;
@SerializedName("result")
@Expose
private List<Result> result = null;
@SerializedName("cart")
@Expose
private Integer cart;
@SerializedName("main_category")
@Expose
private MainCategory mainCategory;
@SerializedName("notifications")
@Expose
private Integer notifications;
@SerializedName("brands")
@Expose
private List<Brand> brands = null;
    @SerializedName("redeem_points")
    @Expose
    private RedeemPoints redeemPoints;
private final static long serialVersionUID = 4158305476632849727L;
    @SerializedName("advertisement")
    @Expose
    private List<Advertisement> advertisement = null;

    @SerializedName("user_rescheduled_apt")
    @Expose
    private List<UserRescheduledApt> userRescheduledApt = null;
    public List<UserRescheduledApt> getUserRescheduledApt() {
        return userRescheduledApt;
    }

    public void setUserRescheduledApt(List<UserRescheduledApt> userRescheduledApt) {
        this.userRescheduledApt = userRescheduledApt;
    }

    public List<Advertisement> getAdvertisement() {
        return advertisement;
    }

    public void setAdvertisement(List<Advertisement> advertisement) {
        this.advertisement = advertisement;
    }

    public HomeBean withAdvertisement(List<Advertisement> advertisement) {
        this.advertisement = advertisement;
        return this;
    }


    public RedeemPoints getRedeemPoints() {
        return redeemPoints;
    }

    public void setRedeemPoints(RedeemPoints redeemPoints) {
        this.redeemPoints = redeemPoints;
    }
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

public Integer getCart() {
return cart;
}

public void setCart(Integer cart) {
this.cart = cart;
}

public MainCategory getMainCategory() {
return mainCategory;
}

public void setMainCategory(MainCategory mainCategory) {
this.mainCategory = mainCategory;
}

public Integer getNotifications() {
return notifications;
}

public void setNotifications(Integer notifications) {
this.notifications = notifications;
}

public List<Brand> getBrands() {
return brands;
}

public void setBrands(List<Brand> brands) {
this.brands = brands;
}

    public String getReferrerPoint() {
        return referrerPoint;
    }

    public void setReferrerPoint(String referrerPoint) {
        this.referrerPoint = referrerPoint;
    }

    public class Brand implements Serializable
    {

        @SerializedName("brnd_id")
        @Expose
        private String brndId;
        @SerializedName("brnd_name")
        @Expose
        private String brndName;
        @SerializedName("brnd_category")
        @Expose
        private String brndCategory;
        @SerializedName("brnd_img")
        @Expose
        private String brndImg;
        @SerializedName("brnd_url")
        @Expose
        private String brndUrl;
        @SerializedName("brnd_create_date")
        @Expose
        private String brndCreateDate;
        @SerializedName("brnd_update_date")
        @Expose
        private String brndUpdateDate;
        @SerializedName("brnd_create_by")
        @Expose
        private String brndCreateBy;
        @SerializedName("brnd_update_by")
        @Expose
        private String brndUpdateBy;
        @SerializedName("brnd_status")
        @Expose
        private String brndStatus;
        private final static long serialVersionUID = -5068265339783089853L;

        public String getBrndId() {
            return brndId;
        }

        public void setBrndId(String brndId) {
            this.brndId = brndId;
        }

        public String getBrndName() {
            return brndName;
        }

        public void setBrndName(String brndName) {
            this.brndName = brndName;
        }

        public String getBrndCategory() {
            return brndCategory;
        }

        public void setBrndCategory(String brndCategory) {
            this.brndCategory = brndCategory;
        }

        public String getBrndImg() {
            return brndImg;
        }

        public void setBrndImg(String brndImg) {
            this.brndImg = brndImg;
        }

        public String getBrndUrl() {
            return brndUrl;
        }

        public void setBrndUrl(String brndUrl) {
            this.brndUrl = brndUrl;
        }

        public String getBrndCreateDate() {
            return brndCreateDate;
        }

        public void setBrndCreateDate(String brndCreateDate) {
            this.brndCreateDate = brndCreateDate;
        }

        public String getBrndUpdateDate() {
            return brndUpdateDate;
        }

        public void setBrndUpdateDate(String brndUpdateDate) {
            this.brndUpdateDate = brndUpdateDate;
        }

        public String getBrndCreateBy() {
            return brndCreateBy;
        }

        public void setBrndCreateBy(String brndCreateBy) {
            this.brndCreateBy = brndCreateBy;
        }

        public String getBrndUpdateBy() {
            return brndUpdateBy;
        }

        public void setBrndUpdateBy(String brndUpdateBy) {
            this.brndUpdateBy = brndUpdateBy;
        }

        public String getBrndStatus() {
            return brndStatus;
        }

        public void setBrndStatus(String brndStatus) {
            this.brndStatus = brndStatus;
        }

    }

    public class MainCategory implements Serializable
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
        private final static long serialVersionUID = -6990763297808951660L;

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


    public class Result implements Serializable
    {

        @SerializedName("bran_id")
        @Expose
        private String branId;
        @SerializedName("bran_name")
        @Expose
        private String branName;
        @SerializedName("bran_img")
        @Expose
        private String branImg;
        @SerializedName("bran_open_status")
        @Expose
        private String branOpenStatus;

        @SerializedName("bran_lat")
        @Expose
        private String branLat;
        @SerializedName("bran_lon")
        @Expose
        private String branLon;
        @SerializedName("distance")
        @Expose
        private String distance;
        @SerializedName("bran_addr")
        @Expose
        private String branAddr;
        @SerializedName("salon_rating")
        @Expose
        private Object salonRating;
        @SerializedName("discount")
        @Expose
        private String discount;
        @SerializedName("combo_discount")
        @Expose
        private String comboDiscount;
        @SerializedName("offer_discount")
        @Expose
        private String offerDiscount;
        private final static long serialVersionUID = -4158311341946606274L;

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

        public String getBranImg() {
            return branImg;
        }

        public void setBranImg(String branImg) {
            this.branImg = branImg;
        }

        public String getBranOpenStatus() {
            return branOpenStatus;
        }

        public void setBranOpenStatus(String branOpenStatus) {
            this.branOpenStatus = branOpenStatus;
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

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public Object getSalonRating() {
            return salonRating;
        }

        public void setSalonRating(Object salonRating) {
            this.salonRating = salonRating;
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

        public String getBranAddr() {
            return branAddr;
        }

        public void setBranAddr(String branAddr) {
            this.branAddr = branAddr;
        }

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

        public String getBranWorkingHrs() {
            return branWorkingHrs;
        }

        public void setBranWorkingHrs(String branWorkingHrs) {
            this.branWorkingHrs = branWorkingHrs;
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
        @SerializedName("bran_opened_on")
        @Expose
        private String branOpenedOn;
        @SerializedName("bran_closed")
        @Expose
        private String branClosed;
        public String getBranOpenedOn() {
            return branOpenedOn;
        }

        public void setBranOpenedOn(String branOpenedOn) {
            this.branOpenedOn = branOpenedOn;
        }

        public String getBranClosed() {
            return branClosed;
        }

        public void setBranClosed(String branClosed) {
            this.branClosed = branClosed;
        }
    }

    public class Advertisement implements Serializable
    {

        @SerializedName("adv_id")
        @Expose
        private String advId;
        @SerializedName("adv_img")
        @Expose
        private String advImg;
        @SerializedName("adv_url")
        @Expose
        private String advUrl;
        @SerializedName("adv_for")
        @Expose
        private String advFor;
        @SerializedName("adv_pincode")
        @Expose
        private String advPincode;
        @SerializedName("adv_create_date")
        @Expose
        private String advCreateDate;
        @SerializedName("adv_create_by")
        @Expose
        private String advCreateBy;
        @SerializedName("adv_update_date")
        @Expose
        private String advUpdateDate;
        @SerializedName("adv_update_by")
        @Expose
        private String advUpdateBy;
        @SerializedName("adv_status")
        @Expose
        private String advStatus;
        private final static long serialVersionUID = -2103491025325668621L;

        public String getAdvId() {
            return advId;
        }

        public void setAdvId(String advId) {
            this.advId = advId;
        }

        public Advertisement withAdvId(String advId) {
            this.advId = advId;
            return this;
        }

        public String getAdvImg() {
            return advImg;
        }

        public void setAdvImg(String advImg) {
            this.advImg = advImg;
        }

        public Advertisement withAdvImg(String advImg) {
            this.advImg = advImg;
            return this;
        }

        public String getAdvUrl() {
            return advUrl;
        }

        public void setAdvUrl(String advUrl) {
            this.advUrl = advUrl;
        }

        public Advertisement withAdvUrl(String advUrl) {
            this.advUrl = advUrl;
            return this;
        }

        public String getAdvFor() {
            return advFor;
        }

        public void setAdvFor(String advFor) {
            this.advFor = advFor;
        }

        public Advertisement withAdvFor(String advFor) {
            this.advFor = advFor;
            return this;
        }

        public String getAdvPincode() {
            return advPincode;
        }

        public void setAdvPincode(String advPincode) {
            this.advPincode = advPincode;
        }

        public Advertisement withAdvPincode(String advPincode) {
            this.advPincode = advPincode;
            return this;
        }

        public String getAdvCreateDate() {
            return advCreateDate;
        }

        public void setAdvCreateDate(String advCreateDate) {
            this.advCreateDate = advCreateDate;
        }

        public Advertisement withAdvCreateDate(String advCreateDate) {
            this.advCreateDate = advCreateDate;
            return this;
        }

        public String getAdvCreateBy() {
            return advCreateBy;
        }

        public void setAdvCreateBy(String advCreateBy) {
            this.advCreateBy = advCreateBy;
        }

        public Advertisement withAdvCreateBy(String advCreateBy) {
            this.advCreateBy = advCreateBy;
            return this;
        }

        public String getAdvUpdateDate() {
            return advUpdateDate;
        }

        public void setAdvUpdateDate(String advUpdateDate) {
            this.advUpdateDate = advUpdateDate;
        }

        public Advertisement withAdvUpdateDate(String advUpdateDate) {
            this.advUpdateDate = advUpdateDate;
            return this;
        }

        public String getAdvUpdateBy() {
            return advUpdateBy;
        }

        public void setAdvUpdateBy(String advUpdateBy) {
            this.advUpdateBy = advUpdateBy;
        }

        public Advertisement withAdvUpdateBy(String advUpdateBy) {
            this.advUpdateBy = advUpdateBy;
            return this;
        }

        public String getAdvStatus() {
            return advStatus;
        }

        public void setAdvStatus(String advStatus) {
            this.advStatus = advStatus;
        }

        public Advertisement withAdvStatus(String advStatus) {
            this.advStatus = advStatus;
            return this;
        }

    }


    public class RedeemPoints implements Serializable
    {

        @SerializedName("points")
        @Expose
        private String points;
        @SerializedName("rs")
        @Expose
        private String rs;
        private final static long serialVersionUID = -8693847926748799443L;

        public String getPoints() {
            return points;
        }

        public void setPoints(String points) {
            this.points = points;
        }

        public RedeemPoints withPoints(String points) {
            this.points = points;
            return this;
        }

        public String getRs() {
            return rs;
        }

        public void setRs(String rs) {
            this.rs = rs;
        }

        public RedeemPoints withRs(String rs) {
            this.rs = rs;
            return this;
        }

    }



    public class UserRescheduledApt implements Serializable
    {

        @SerializedName("apt_id")
        @Expose
        private String aptId;
        @SerializedName("apt_accept_time")
        @Expose
        private String aptAcceptTime;
        private final static long serialVersionUID = -619286571025996725L;

        public String getAptId() {
            return aptId;
        }

        public void setAptId(String aptId) {
            this.aptId = aptId;
        }

        public UserRescheduledApt withAptId(String aptId) {
            this.aptId = aptId;
            return this;
        }

        public String getAptAcceptTime() {
            return aptAcceptTime;
        }

        public void setAptAcceptTime(String aptAcceptTime) {
            this.aptAcceptTime = aptAcceptTime;
        }

        public UserRescheduledApt withAptAcceptTime(String aptAcceptTime) {
            this.aptAcceptTime = aptAcceptTime;
            return this;
        }

    }
}