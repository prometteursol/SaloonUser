package com.prometteur.saloonuser.Model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppointDetailBean implements Serializable
{

@SerializedName("status")
@Expose
private Integer status;
@SerializedName("msg")
@Expose
private String msg;

@SerializedName("online_payment_redeem_point")
@Expose
private String onlinePaymentRedeemPoint;
@SerializedName("result")
@Expose
private List<Result> result = null;
private final static long serialVersionUID = -7361885083842313361L;

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

    public String getOnlinePaymentRedeemPoint() {
        return onlinePaymentRedeemPoint;
    }

    public void setOnlinePaymentRedeemPoint(String onlinePaymentRedeemPoint) {
        this.onlinePaymentRedeemPoint = onlinePaymentRedeemPoint;
    }

    public class ComboService implements Serializable
    {

        @SerializedName("aptser_id")
        @Expose
        private String aptserId;
        @SerializedName("offer_id")
        @Expose
        private String offerId;
        @SerializedName("offer_name")
        @Expose
        private String offerName;
        @SerializedName("offer_price")
        @Expose
        private String offerPrice;
        @SerializedName("offer_discount")
        @Expose
        private String offerDiscount;
        @SerializedName("offer_discount_price")
        @Expose
        private String offerDiscountPrice;
        @SerializedName("services")
        @Expose
        private List<Service> services = null;
        private final static long serialVersionUID = -7781788578320961428L;

        public String getAptserId() {
            return aptserId;
        }

        public void setAptserId(String aptserId) {
            this.aptserId = aptserId;
        }

        public String getOfferId() {
            return offerId;
        }

        public void setOfferId(String offerId) {
            this.offerId = offerId;
        }

        public String getOfferName() {
            return offerName;
        }

        public void setOfferName(String offerName) {
            this.offerName = offerName;
        }

        public String getOfferPrice() {
            return offerPrice;
        }

        public void setOfferPrice(String offerPrice) {
            this.offerPrice = offerPrice;
        }

        public String getOfferDiscount() {
            return offerDiscount;
        }

        public void setOfferDiscount(String offerDiscount) {
            this.offerDiscount = offerDiscount;
        }

        public String getOfferDiscountPrice() {
            return offerDiscountPrice;
        }

        public void setOfferDiscountPrice(String offerDiscountPrice) {
            this.offerDiscountPrice = offerDiscountPrice;
        }

        public List<Service> getServices() {
            return services;
        }

        public void setServices(List<Service> services) {
            this.services = services;
        }

    }

    public class PromotionalService implements Serializable
    {

        @SerializedName("aptser_id")
        @Expose
        private String aptserId;
        @SerializedName("prooffer_id")
        @Expose
        private String proofferId;
        @SerializedName("prooffer_name")
        @Expose
        private String proofferName;
        @SerializedName("prooffer_price")
        @Expose
        private String proofferPrice;
        @SerializedName("prooffer_discount")
        @Expose
        private String proofferDiscount;
        @SerializedName("prooffer_discount_price")
        @Expose
        private String proofferDiscountPrice;
        @SerializedName("services")
        @Expose
        private List<Service> services = null;
        private final static long serialVersionUID = -1638243272819239115L;

        public String getAptserId() {
            return aptserId;
        }

        public void setAptserId(String aptserId) {
            this.aptserId = aptserId;
        }

        public String getProofferId() {
            return proofferId;
        }

        public void setProofferId(String proofferId) {
            this.proofferId = proofferId;
        }

        public String getProofferName() {
            return proofferName;
        }

        public void setProofferName(String proofferName) {
            this.proofferName = proofferName;
        }

        public String getProofferPrice() {
            return proofferPrice;
        }

        public void setProofferPrice(String proofferPrice) {
            this.proofferPrice = proofferPrice;
        }

        public String getProofferDiscount() {
            return proofferDiscount;
        }

        public void setProofferDiscount(String proofferDiscount) {
            this.proofferDiscount = proofferDiscount;
        }

        public String getProofferDiscountPrice() {
            return proofferDiscountPrice;
        }

        public void setProofferDiscountPrice(String proofferDiscountPrice) {
            this.proofferDiscountPrice = proofferDiscountPrice;
        }

        public List<Service> getServices() {
            return services;
        }

        public void setServices(List<Service> services) {
            this.services = services;
        }

    }

    public class Result implements Serializable
    {

        @SerializedName("apt_id")
        @Expose
        private String aptId;
        @SerializedName("apt_date")
        @Expose
        private String aptDate;
        @SerializedName("apt_time")
        @Expose
        private String aptTime;
        @SerializedName("apt_payment_type")
        @Expose
        private String aptPaymentType;
        @SerializedName("apt_payment_status")
        @Expose
        private String aptPaymentStatus;
        @SerializedName("apt_amount")
        @Expose
        private String aptAmount;
        @SerializedName("apt_status")
        @Expose
        private String aptStatus;
        @SerializedName("apt_start_otp")
        @Expose
        private String aptStartOtp;
        @SerializedName("apt_end_otp")
        @Expose
        private String aptEndOtp;
        @SerializedName("bran_id")
        @Expose
        private String branId;
        @SerializedName("bran_name")
        @Expose
        private String branName;
        @SerializedName("bran_addr")
        @Expose
        private String branAddr;
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
        @SerializedName("apt_coupon_id")
        @Expose
        private String aptCouponId;
        @SerializedName("coupon_title")
        @Expose
        private String couponTitle;
        @SerializedName("coupon_discount_price")
        @Expose
        private String couponDiscountPrice;
        @SerializedName("distance")
        @Expose
        private String distance;
        @SerializedName("salon_rating")
        @Expose
        private String salonRating;
        @SerializedName("user_penalty")
        @Expose
        private String userPenalty;
        @SerializedName("apt_coupon_price")
        @Expose
        private String aptCouponPrice;
        @SerializedName("apt_mooi_discount")
        @Expose
        private String aptMooiDiscount;
        @SerializedName("apt_discount")
        @Expose
        private String aptDiscount;
        @SerializedName("apt_penality")
        @Expose
        private String aptPenality;
        @SerializedName("apt_redeem_point")
        @Expose
        private String aptRedeemPoint;
        @SerializedName("apt_service_charges")
        @Expose
        private String aptServiceCharges;
        @SerializedName("bran_rate_of_gst")
        @Expose
        private String branRateOfGst;
        @SerializedName("is_review_given")
        @Expose
        private String isReviewGiven;
        @SerializedName("services")
        @Expose
        private List<Service> services = null;
        @SerializedName("combo_services")
        @Expose
        private List<ComboService> comboServices = null;
        @SerializedName("promotional_services")
        @Expose
        private List<PromotionalService> promotionalServices = null;
        private final static long serialVersionUID = 4373715206135334373L;

        public String getAptId() {
            return aptId;
        }

        public void setAptId(String aptId) {
            this.aptId = aptId;
        }

        public String getAptDate() {
            return aptDate;
        }

        public void setAptDate(String aptDate) {
            this.aptDate = aptDate;
        }

        public String getAptTime() {
            return aptTime;
        }

        public void setAptTime(String aptTime) {
            this.aptTime = aptTime;
        }

        public String getAptPaymentType() {
            return aptPaymentType;
        }

        public void setAptPaymentType(String aptPaymentType) {
            this.aptPaymentType = aptPaymentType;
        }

        public String getAptPaymentStatus() {
            return aptPaymentStatus;
        }

        public void setAptPaymentStatus(String aptPaymentStatus) {
            this.aptPaymentStatus = aptPaymentStatus;
        }

        public String getAptMooiDiscount() {
            return aptMooiDiscount;
        }

        public void setAptMooiDiscount(String aptMooiDiscount) {
            this.aptMooiDiscount = aptMooiDiscount;
        }

        public String getAptDiscount() {
            return aptDiscount;
        }

        public void setAptDiscount(String aptDiscount) {
            this.aptDiscount = aptDiscount;
        }

        public String getAptAmount() {
            return aptAmount;
        }

        public void setAptAmount(String aptAmount) {
            this.aptAmount = aptAmount;
        }

        public String getAptStatus() {
            return aptStatus;
        }

        public void setAptStatus(String aptStatus) {
            this.aptStatus = aptStatus;
        }

        public String getAptStartOtp() {
            return aptStartOtp;
        }

        public void setAptStartOtp(String aptStartOtp) {
            this.aptStartOtp = aptStartOtp;
        }

        public String getAptEndOtp() {
            return aptEndOtp;
        }

        public void setAptEndOtp(String aptEndOtp) {
            this.aptEndOtp = aptEndOtp;
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

        public String getAptCouponId() {
            return aptCouponId;
        }

        public void setAptCouponId(String aptCouponId) {
            this.aptCouponId = aptCouponId;
        }

        public String getCouponTitle() {
            return couponTitle;
        }

        public void setCouponTitle(String couponTitle) {
            this.couponTitle = couponTitle;
        }

        public String getCouponDiscountPrice() {
            return couponDiscountPrice;
        }

        public void setCouponDiscountPrice(String couponDiscountPrice) {
            this.couponDiscountPrice = couponDiscountPrice;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getSalonRating() {
            return salonRating;
        }

        public void setSalonRating(String salonRating) {
            this.salonRating = salonRating;
        }

        public List<Service> getServices() {
            return services;
        }

        public void setServices(List<Service> services) {
            this.services = services;
        }

        public List<ComboService> getComboServices() {
            return comboServices;
        }

        public void setComboServices(List<ComboService> comboServices) {
            this.comboServices = comboServices;
        }

        public List<PromotionalService> getPromotionalServices() {
            return promotionalServices;
        }

        public void setPromotionalServices(List<PromotionalService> promotionalServices) {
            this.promotionalServices = promotionalServices;
        }

        public String getBranRateOfGst() {
            return branRateOfGst;
        }

        public void setBranRateOfGst(String branRateOfGst) {
            this.branRateOfGst = branRateOfGst;
        }

        public String getUserPenalty() {
            return userPenalty;
        }

        public void setUserPenalty(String userPenalty) {
            this.userPenalty = userPenalty;
        }

        public String getIsReviewGiven() {
            return isReviewGiven;
        }

        public void setIsReviewGiven(String isReviewGiven) {
            this.isReviewGiven = isReviewGiven;
        }

        public String getAptPenality() {
            return aptPenality;
        }

        public void setAptPenality(String aptPenality) {
            this.aptPenality = aptPenality;
        }

        public String getAptRedeemPoint() {
            return aptRedeemPoint;
        }

        public void setAptRedeemPoint(String aptRedeemPoint) {
            this.aptRedeemPoint = aptRedeemPoint;
        }

        public String getAptServiceCharges() {
            return aptServiceCharges;
        }

        public void setAptServiceCharges(String aptServiceCharges) {
            this.aptServiceCharges = aptServiceCharges;
        }

        public String getAptCouponPrice() {
            return aptCouponPrice;
        }

        public void setAptCouponPrice(String aptCouponPrice) {
            this.aptCouponPrice = aptCouponPrice;
        }
    }


    public class Service implements Serializable
    {

        @SerializedName("srvc_name")
        @Expose
        private String srvcName;
        @SerializedName("brnd_name")
        @Expose
        private String brndName;
        @SerializedName("srvc_price")
        @Expose
        private String srvcPrice;
        @SerializedName("user_f_name")
        @Expose
        private String userFName;
        @SerializedName("user_l_name")
        @Expose
        private String userLName;
        @SerializedName("srvc_discount_price")
        @Expose
        private String srvcDiscountPrice;
        private final static long serialVersionUID = 7662479106818600039L;

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

        public String getSrvcPrice() {
            return srvcPrice;
        }

        public void setSrvcPrice(String srvcPrice) {
            this.srvcPrice = srvcPrice;
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

        public String getSrvcDiscountPrice() {
            return srvcDiscountPrice;
        }

        public void setSrvcDiscountPrice(String srvcDiscountPrice) {
            this.srvcDiscountPrice = srvcDiscountPrice;
        }

    }


}