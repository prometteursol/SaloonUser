package com.prometteur.saloonuser.Model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PromoOfferBean implements Serializable
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
private final static long serialVersionUID = 8610240840303232808L;

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

        @SerializedName("prooffer_id")
        @Expose
        private String proofferId;
        @SerializedName("prooffer_branch_id")
        @Expose
        private String proofferBranchId;
        @SerializedName("prooffer_name")
        @Expose
        private String proofferName;
        @SerializedName("prooffer_services")
        @Expose
        private String proofferServices;
        @SerializedName("prooffer_price")
        @Expose
        private String proofferPrice;
        @SerializedName("prooffer_discount")
        @Expose
        private String proofferDiscount;
        @SerializedName("prooffer_discount_price")
        @Expose
        private String proofferDiscountPrice;
        @SerializedName("prooffer_start_date")
        @Expose
        private String proofferStartDate;
        @SerializedName("prooffer_end_date")
        @Expose
        private String proofferEndDate;
        @SerializedName("services")
        @Expose
        private List<Service> services = null;
        @SerializedName("in_cart")
        @Expose
        private Integer inCart;
        private final static long serialVersionUID = -7580123677682341291L;
        @SerializedName("cart_id")
        @Expose
        private String cartId;

        public String getCartId() {
            return cartId;
        }

        public void setCartId(String cartId) {
            this.cartId = cartId;
        }

        public String getProofferId() {
            return proofferId;
        }

        public void setProofferId(String proofferId) {
            this.proofferId = proofferId;
        }

        public String getProofferBranchId() {
            return proofferBranchId;
        }

        public void setProofferBranchId(String proofferBranchId) {
            this.proofferBranchId = proofferBranchId;
        }

        public String getProofferName() {
            return proofferName;
        }

        public void setProofferName(String proofferName) {
            this.proofferName = proofferName;
        }

        public String getProofferServices() {
            return proofferServices;
        }

        public void setProofferServices(String proofferServices) {
            this.proofferServices = proofferServices;
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

        public String getProofferStartDate() {
            return proofferStartDate;
        }

        public void setProofferStartDate(String proofferStartDate) {
            this.proofferStartDate = proofferStartDate;
        }

        public String getProofferEndDate() {
            return proofferEndDate;
        }

        public void setProofferEndDate(String proofferEndDate) {
            this.proofferEndDate = proofferEndDate;
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
        private final static long serialVersionUID = 1208346393366442483L;
        @SerializedName("operators")
        @Expose
        private List<Operator> operators = null;
        public void setOperators(List<Operator> operators) {
            this.operators = operators;
        }

        public Service withOperators(List<Operator> operators) {
            this.operators = operators;
            return this;
        }


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
        private final static long serialVersionUID = 852994042278101268L;

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