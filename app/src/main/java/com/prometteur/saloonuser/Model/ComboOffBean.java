package com.prometteur.saloonuser.Model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ComboOffBean implements Serializable
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
private final static long serialVersionUID = -7751779123436560824L;

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

        @SerializedName("offer_id")
        @Expose
        private String offerId;
        @SerializedName("offer_branch_id")
        @Expose
        private String offerBranchId;
        @SerializedName("offer_name")
        @Expose
        private String offerName;
        @SerializedName("offer_services")
        @Expose
        private String offerServices;
        @SerializedName("offer_price")
        @Expose
        private String offerPrice;
        @SerializedName("offer_discount_price")
        @Expose
        private String offerDiscountPrice;
        @SerializedName("services")
        @Expose
        private List<Service> services = null;
        @SerializedName("in_cart")
        @Expose
        private Integer inCart;
        private final static long serialVersionUID = -4467921714772726361L;
        @SerializedName("cart_id")
        @Expose
        private String cartId;

        public String getCartId() {
            return cartId;
        }

        public void setCartId(String cartId) {
            this.cartId = cartId;
        }
        public String getOfferId() {
            return offerId;
        }

        public void setOfferId(String offerId) {
            this.offerId = offerId;
        }

        public String getOfferBranchId() {
            return offerBranchId;
        }

        public void setOfferBranchId(String offerBranchId) {
            this.offerBranchId = offerBranchId;
        }

        public String getOfferName() {
            return offerName;
        }

        public void setOfferName(String offerName) {
            this.offerName = offerName;
        }

        public String getOfferServices() {
            return offerServices;
        }

        public void setOfferServices(String offerServices) {
            this.offerServices = offerServices;
        }

        public String getOfferPrice() {
            return offerPrice;
        }

        public void setOfferPrice(String offerPrice) {
            this.offerPrice = offerPrice;
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

        public Integer getInCart() {
            return inCart;
        }

        public void setInCart(Integer inCart) {
            this.inCart = inCart;
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
        @SerializedName("operators")
        @Expose
        private List<Operator> operators = null;
        private final static long serialVersionUID = 1208346393366442483L;

        public String getSrvcId() {
            return srvcId;
        }

        public void setSrvcId(String srvcId) {
            this.srvcId = srvcId;
        }

        public Service withSrvcId(String srvcId) {
            this.srvcId = srvcId;
            return this;
        }

        public String getSrvcName() {
            return srvcName;
        }

        public void setSrvcName(String srvcName) {
            this.srvcName = srvcName;
        }

        public Service withSrvcName(String srvcName) {
            this.srvcName = srvcName;
            return this;
        }

        public String getBrndName() {
            return brndName;
        }

        public void setBrndName(String brndName) {
            this.brndName = brndName;
        }

        public Service withBrndName(String brndName) {
            this.brndName = brndName;
            return this;
        }

        public String getSrvcOperators() {
            return srvcOperators;
        }

        public void setSrvcOperators(String srvcOperators) {
            this.srvcOperators = srvcOperators;
        }

        public Service withSrvcOperators(String srvcOperators) {
            this.srvcOperators = srvcOperators;
            return this;
        }

        public List<Operator> getOperators() {
            return operators;
        }

        public void setOperators(List<Operator> operators) {
            this.operators = operators;
        }

        public Service withOperators(List<Operator> operators) {
            this.operators = operators;
            return this;
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
        private final static long serialVersionUID = 852994042278101268L;
        @SerializedName("selected")
        @Expose
        private String selected;
        @SerializedName("operator_rating")
        @Expose
        private String operatorRating;
        public String getOperatorRating() {
            return operatorRating;
        }

        public void setOperatorRating(String operatorRating) {
            this.operatorRating = operatorRating;
        }
        public String getSelected() {
            return selected;
        }

        public void setSelected(String selected) {
            this.selected = selected;
        }
        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public Operator withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public String getUserFName() {
            return userFName;
        }

        public void setUserFName(String userFName) {
            this.userFName = userFName;
        }

        public Operator withUserFName(String userFName) {
            this.userFName = userFName;
            return this;
        }

        public String getUserLName() {
            return userLName;
        }

        public void setUserLName(String userLName) {
            this.userLName = userLName;
        }

        public Operator withUserLName(String userLName) {
            this.userLName = userLName;
            return this;
        }

        public String getUserImg() {
            return userImg;
        }

        public void setUserImg(String userImg) {
            this.userImg = userImg;
        }

        public Operator withUserImg(String userImg) {
            this.userImg = userImg;
            return this;
        }

    }
}