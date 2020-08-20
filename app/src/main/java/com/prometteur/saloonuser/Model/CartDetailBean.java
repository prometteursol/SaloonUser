package com.prometteur.saloonuser.Model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartDetailBean implements Serializable
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
@SerializedName("salon_details")
@Expose
private List<SalonDetail> salonDetails = null;
@SerializedName("redeem_points")
@Expose
private RedeemPoints redeemPoints;
    @SerializedName("user_penalty")
    @Expose
    private String userPenalty;
    @SerializedName("service_charges")
    @Expose
    private String serviceCharges;
    @SerializedName("redeem_point_use")
    @Expose
    private String redeemPointUse;
    @SerializedName("min_redeem_point_required")
    @Expose
    private String minRedeemPointRequired;
private final static long serialVersionUID = 7650736166330598575L;

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

public List<SalonDetail> getSalonDetails() {
return salonDetails;
}

public void setSalonDetails(List<SalonDetail> salonDetails) {
this.salonDetails = salonDetails;
}

public RedeemPoints getRedeemPoints() {
return redeemPoints;
}

public void setRedeemPoints(RedeemPoints redeemPoints) {
this.redeemPoints = redeemPoints;
}
    public String getUserPenalty() {
        return userPenalty;
    }

    public void setUserPenalty(String userPenalty) {
        this.userPenalty = userPenalty;
    }

    public String getServiceCharges() {
        return serviceCharges;
    }

    public void setServiceCharges(String serviceCharges) {
        this.serviceCharges = serviceCharges;
    }

    public String getRedeemPointUse() {
        return redeemPointUse;
    }

    public void setRedeemPointUse(String redeemPointUse) {
        this.redeemPointUse = redeemPointUse;
    }

    public String getMinRedeemPointRequired() {
        return minRedeemPointRequired;
    }

    public void setMinRedeemPointRequired(String minRedeemPointRequired) {
        this.minRedeemPointRequired = minRedeemPointRequired;
    }

    public class Detail implements Serializable
    {

        @SerializedName("offer_id")
        @Expose
        private String offerId;
        @SerializedName("offer_name")
        @Expose
        private String offerName;
        @SerializedName("offer_price")
        @Expose
        private String offerPrice;
        @SerializedName("discount")
        @Expose
        private String discount;
        @SerializedName("discount_price")
        @Expose
        private String discountPrice;
        @SerializedName("offer_services")
        @Expose
        private String offerServices;
        @SerializedName("services")
        @Expose
        private List<List<Service>> services = null;
        private final static long serialVersionUID = 6740750127105101074L;

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

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getDiscountPrice() {
            return discountPrice;
        }

        public void setDiscountPrice(String discountPrice) {
            this.discountPrice = discountPrice;
        }

        public String getOfferServices() {
            return offerServices;
        }

        public void setOfferServices(String offerServices) {
            this.offerServices = offerServices;
        }

        public List<List<Service>> getServices() {
            return services;
        }

        public void setServices(List<List<Service>> services) {
            this.services = services;
        }

    }

    public class Operator implements Serializable
    {

        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("user_f_name")
        @Expose
        private String userFName;
        @SerializedName("user_l_name")
        @Expose
        private String userLName;
        @SerializedName("user_img")
        @Expose
        private String userImg;
        @SerializedName("selected")
        @Expose
        private String selected;
        public String getSelected() {
            return selected;
        }

        public void setSelected(String selected) {
            this.selected = selected;
        }
        @SerializedName("operator_rating")
        @Expose
        private String operatorRating;
        public String getOperatorRating() {
            return operatorRating;
        }

        public void setOperatorRating(String operatorRating) {
            this.operatorRating = operatorRating;
        }
        private final static long serialVersionUID = -3777290535011898363L;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
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



    }


    public class RedeemPoints implements Serializable
    {

        @SerializedName("points")
        @Expose
        private String points;
        @SerializedName("rs")
        @Expose
        private String rs;
        private final static long serialVersionUID = -7034131194663223091L;

        public String getPoints() {
            return points;
        }

        public void setPoints(String points) {
            this.points = points;
        }

        public String getRs() {
            return rs;
        }

        public void setRs(String rs) {
            this.rs = rs;
        }

    }

    public class Result implements Serializable
    {

        @SerializedName("cart_id")
        @Expose
        private String cartId;
        @SerializedName("cart_user_id")
        @Expose
        private String cartUserId;
        @SerializedName("cart_type")
        @Expose
        private String cartType;
        @SerializedName("cart_branch_id")
        @Expose
        private String cartBranchId;
        @SerializedName("cart_service_id")
        @Expose
        private String cartServiceId;
        @SerializedName("cart_services")
        @Expose
        private String cartServices;
        @SerializedName("cart_create_date")
        @Expose
        private String cartCreateDate;
        @SerializedName("cart_update_date")
        @Expose
        private String cartUpdateDate;
        @SerializedName("cart_status")
        @Expose
        private String cartStatus;
        @SerializedName("details")
        @Expose
        private List<Detail> details = null;
        @SerializedName("services")
        @Expose
        private List<Service> services = null;
        private final static long serialVersionUID = -5356710289017049924L;

        public String getCartId() {
            return cartId;
        }

        public void setCartId(String cartId) {
            this.cartId = cartId;
        }

        public String getCartUserId() {
            return cartUserId;
        }

        public void setCartUserId(String cartUserId) {
            this.cartUserId = cartUserId;
        }

        public String getCartType() {
            return cartType;
        }

        public void setCartType(String cartType) {
            this.cartType = cartType;
        }

        public String getCartBranchId() {
            return cartBranchId;
        }

        public void setCartBranchId(String cartBranchId) {
            this.cartBranchId = cartBranchId;
        }

        public String getCartServiceId() {
            return cartServiceId;
        }

        public void setCartServiceId(String cartServiceId) {
            this.cartServiceId = cartServiceId;
        }

        public String getCartServices() {
            return cartServices;
        }

        public void setCartServices(String cartServices) {
            this.cartServices = cartServices;
        }

        public String getCartCreateDate() {
            return cartCreateDate;
        }

        public void setCartCreateDate(String cartCreateDate) {
            this.cartCreateDate = cartCreateDate;
        }

        public String getCartUpdateDate() {
            return cartUpdateDate;
        }

        public void setCartUpdateDate(String cartUpdateDate) {
            this.cartUpdateDate = cartUpdateDate;
        }

        public String getCartStatus() {
            return cartStatus;
        }

        public void setCartStatus(String cartStatus) {
            this.cartStatus = cartStatus;
        }

        public List<Detail> getDetails() {
            return details;
        }

        public void setDetails(List<Detail> details) {
            this.details = details;
        }

        public List<Service> getServices() {
            return services;
        }

        public void setServices(List<Service> services) {
            this.services = services;
        }

    }

    public class SalonDetail implements Serializable
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
        @SerializedName("bran_state")
        @Expose
        private String branState;
        @SerializedName("bran_pin_code")
        @Expose
        private String branPinCode;
        @SerializedName("salon_rating")
        @Expose
        private String salonRating;
        @SerializedName("discount")
        @Expose
        private String discount;
        @SerializedName("bran_rate_of_gst")
        @Expose
        private String branRateOfGst;
        private final static long serialVersionUID = 8073487918635333791L;

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

        public String getSalonRating() {
            return salonRating;
        }

        public void setSalonRating(String salonRating) {
            this.salonRating = salonRating;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getBranRateOfGst() {
            return branRateOfGst;
        }

        public void setBranRateOfGst(String branRateOfGst) {
            this.branRateOfGst = branRateOfGst;
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
        @SerializedName("srvc_operators")
        @Expose
        private String srvcOperators;
        @SerializedName("srvc_price")
        @Expose
        private String srvcPrice;
        @SerializedName("srvc_discount")
        @Expose
        private String srvcDiscount;
        @SerializedName("srvc_discount_price")
        @Expose
        private String srvcDiscountPrice;
        @SerializedName("cart_id")
        @Expose
        private String cartId;
        @SerializedName("operators")
        @Expose
        private List<Operator> operators = null;
        private final static long serialVersionUID = -1130938076855724352L;

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

        public String getSrvcOperators() {
            return srvcOperators;
        }

        public void setSrvcOperators(String srvcOperators) {
            this.srvcOperators = srvcOperators;
        }

        public String getSrvcPrice() {
            return srvcPrice;
        }

        public void setSrvcPrice(String srvcPrice) {
            this.srvcPrice = srvcPrice;
        }

        public String getSrvcDiscount() {
            return srvcDiscount;
        }

        public void setSrvcDiscount(String srvcDiscount) {
            this.srvcDiscount = srvcDiscount;
        }

        public String getSrvcDiscountPrice() {
            return srvcDiscountPrice;
        }

        public void setSrvcDiscountPrice(String srvcDiscountPrice) {
            this.srvcDiscountPrice = srvcDiscountPrice;
        }

        public List<Operator> getOperators() {
            return operators;
        }

        public void setOperators(List<Operator> operators) {
            this.operators = operators;
        }

        public String getCartId() {
            return cartId;
        }

        public void setCartId(String cartId) {
            this.cartId = cartId;
        }
    }
}