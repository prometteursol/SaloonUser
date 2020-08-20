package com.prometteur.saloonuser.Model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CouponBean implements Serializable
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
private final static long serialVersionUID = 1324160362119478200L;

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

        @SerializedName("coupon_id")
        @Expose
        private String couponId;
        @SerializedName("coupon_title")
        @Expose
        private String couponTitle;
        @SerializedName("coupon_text")
        @Expose
        private String couponText;
        @SerializedName("coupon_description")
        @Expose
        private String couponDescription;
        @SerializedName("coupon_code")
        @Expose
        private String couponCode;
        @SerializedName("coupon_discount_price")
        @Expose
        private String couponDiscountPrice;
        @SerializedName("coupon_mininum_price")
        @Expose
        private String couponMinimumPrice;
        @SerializedName("coupon_start_date")
        @Expose
        private String couponStartDate;
        @SerializedName("coupon_end_date")
        @Expose
        private String couponEndDate;
        @SerializedName("coupon_status")
        @Expose
        private String couponStatus;
        @SerializedName("coupon_create_date")
        @Expose
        private String couponCreateDate;
        @SerializedName("coupon_create_by")
        @Expose
        private String couponCreateBy;
        @SerializedName("coupon_update_date")
        @Expose
        private String couponUpdateDate;
        @SerializedName("coupon_update_by")
        @Expose
        private String couponUpdateBy;
        @SerializedName("coupon_max_discount")
        @Expose
        private String couponMaxDiscount;
        @SerializedName("coupon_discount")
        @Expose
        private String couponDiscount;
        private final static long serialVersionUID = -8394420896912856088L;

        public String getCouponId() {
            return couponId;
        }

        public void setCouponId(String couponId) {
            this.couponId = couponId;
        }

        public String getCouponTitle() {
            return couponTitle;
        }

        public void setCouponTitle(String couponTitle) {
            this.couponTitle = couponTitle;
        }

        public String getCouponText() {
            return couponText;
        }

        public void setCouponText(String couponText) {
            this.couponText = couponText;
        }

        public String getCouponDescription() {
            return couponDescription;
        }

        public void setCouponDescription(String couponDescription) {
            this.couponDescription = couponDescription;
        }

        public String getCouponCode() {
            return couponCode;
        }

        public void setCouponCode(String couponCode) {
            this.couponCode = couponCode;
        }

        public String getCouponDiscountPrice() {
            return couponDiscountPrice;
        }

        public void setCouponDiscountPrice(String couponDiscountPrice) {
            this.couponDiscountPrice = couponDiscountPrice;
        }

        public String getCouponStartDate() {
            return couponStartDate;
        }

        public void setCouponStartDate(String couponStartDate) {
            this.couponStartDate = couponStartDate;
        }

        public String getCouponEndDate() {
            return couponEndDate;
        }

        public void setCouponEndDate(String couponEndDate) {
            this.couponEndDate = couponEndDate;
        }

        public String getCouponStatus() {
            return couponStatus;
        }

        public void setCouponStatus(String couponStatus) {
            this.couponStatus = couponStatus;
        }

        public String getCouponCreateDate() {
            return couponCreateDate;
        }

        public void setCouponCreateDate(String couponCreateDate) {
            this.couponCreateDate = couponCreateDate;
        }

        public String getCouponCreateBy() {
            return couponCreateBy;
        }

        public void setCouponCreateBy(String couponCreateBy) {
            this.couponCreateBy = couponCreateBy;
        }

        public String getCouponUpdateDate() {
            return couponUpdateDate;
        }

        public void setCouponUpdateDate(String couponUpdateDate) {
            this.couponUpdateDate = couponUpdateDate;
        }

        public String getCouponUpdateBy() {
            return couponUpdateBy;
        }

        public void setCouponUpdateBy(String couponUpdateBy) {
            this.couponUpdateBy = couponUpdateBy;
        }

        public String getCouponMinimumPrice() {
            return couponMinimumPrice;
        }

        public void setCouponMinimumPrice(String couponMinimumPrice) {
            this.couponMinimumPrice = couponMinimumPrice;
        }

        public String getCouponMaxDiscount() {
            return couponMaxDiscount;
        }

        public void setCouponMaxDiscount(String couponMaxDiscount) {
            this.couponMaxDiscount = couponMaxDiscount;
        }

        public String getCouponDiscount() {
            return couponDiscount;
        }

        public void setCouponDiscount(String couponDiscount) {
            this.couponDiscount = couponDiscount;
        }
    }
}