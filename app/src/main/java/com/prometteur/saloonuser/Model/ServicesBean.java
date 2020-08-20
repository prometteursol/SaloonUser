package com.prometteur.saloonuser.Model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServicesBean implements Serializable
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
private final static long serialVersionUID = -3037546929665459878L;

public Integer getStatus() {
return status;
}

public void setStatus(Integer status) {
this.status = status;
}

public ServicesBean withStatus(Integer status) {
this.status = status;
return this;
}

public String getMsg() {
return msg;
}

public void setMsg(String msg) {
this.msg = msg;
}

public ServicesBean withMsg(String msg) {
this.msg = msg;
return this;
}

public List<Result> getResult() {
return result;
}

public void setResult(List<Result> result) {
this.result = result;
}

public ServicesBean withResult(List<Result> result) {
this.result = result;
return this;
}

    public class Result implements Serializable
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
        @SerializedName("srvc_operators")
        @Expose
        private String srvcOperators;
        @SerializedName("operators")
        @Expose
        private List<Operator> operators = null;
        private final static long serialVersionUID = -4175731019424484425L;

        public String getSrvcId() {
            return srvcId;
        }

        public void setSrvcId(String srvcId) {
            this.srvcId = srvcId;
        }

        public Result withSrvcId(String srvcId) {
            this.srvcId = srvcId;
            return this;
        }

        public String getSrvcName() {
            return srvcName;
        }

        public void setSrvcName(String srvcName) {
            this.srvcName = srvcName;
        }

        public Result withSrvcName(String srvcName) {
            this.srvcName = srvcName;
            return this;
        }

        public String getBrndName() {
            return brndName;
        }

        public void setBrndName(String brndName) {
            this.brndName = brndName;
        }

        public Result withBrndName(String brndName) {
            this.brndName = brndName;
            return this;
        }

        public String getSrvcEstimateTime() {
            return srvcEstimateTime;
        }

        public void setSrvcEstimateTime(String srvcEstimateTime) {
            this.srvcEstimateTime = srvcEstimateTime;
        }

        public Result withSrvcEstimateTime(String srvcEstimateTime) {
            this.srvcEstimateTime = srvcEstimateTime;
            return this;
        }

        public String getSrvcPrice() {
            return srvcPrice;
        }

        public void setSrvcPrice(String srvcPrice) {
            this.srvcPrice = srvcPrice;
        }

        public Result withSrvcPrice(String srvcPrice) {
            this.srvcPrice = srvcPrice;
            return this;
        }

        public String getSrvcDiscountPrice() {
            return srvcDiscountPrice;
        }

        public void setSrvcDiscountPrice(String srvcDiscountPrice) {
            this.srvcDiscountPrice = srvcDiscountPrice;
        }

        public Result withSrvcDiscountPrice(String srvcDiscountPrice) {
            this.srvcDiscountPrice = srvcDiscountPrice;
            return this;
        }

        public String getSrvcOperators() {
            return srvcOperators;
        }

        public void setSrvcOperators(String srvcOperators) {
            this.srvcOperators = srvcOperators;
        }

        public Result withSrvcOperators(String srvcOperators) {
            this.srvcOperators = srvcOperators;
            return this;
        }

        public List<Operator> getOperators() {
            return operators;
        }

        public void setOperators(List<Operator> operators) {
            this.operators = operators;
        }

        public Result withOperators(List<Operator> operators) {
            this.operators = operators;
            return this;
        }

        @SerializedName("in_cart")
        @Expose
        private Integer inCart;

        public Integer getInCart() {
            return inCart;
        }

        public void setInCart(Integer inCart) {
            this.inCart = inCart;
        }
        @SerializedName("cart_id")
        @Expose
        private String cartId;

        public String getCartId() {
            return cartId;
        }

        public void setCartId(String cartId) {
            this.cartId = cartId;
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