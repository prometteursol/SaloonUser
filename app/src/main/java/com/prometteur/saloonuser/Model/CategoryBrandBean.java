package com.prometteur.saloonuser.Model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryBrandBean implements Serializable
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
private final static long serialVersionUID = 8537636104854262585L;

public Integer getStatus() {
return status;
}

public void setStatus(Integer status) {
this.status = status;
}

public CategoryBrandBean withStatus(Integer status) {
this.status = status;
return this;
}

public String getMsg() {
return msg;
}

public void setMsg(String msg) {
this.msg = msg;
}

public CategoryBrandBean withMsg(String msg) {
this.msg = msg;
return this;
}

public List<Result> getResult() {
return result;
}

public void setResult(List<Result> result) {
this.result = result;
}

public CategoryBrandBean withResult(List<Result> result) {
this.result = result;
return this;
}

    public class Result implements Serializable
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
        private final static long serialVersionUID = 3033924924282997990L;

        public String getBrndId() {
            return brndId;
        }

        public void setBrndId(String brndId) {
            this.brndId = brndId;
        }

        public Result withBrndId(String brndId) {
            this.brndId = brndId;
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

        public String getBrndCategory() {
            return brndCategory;
        }

        public void setBrndCategory(String brndCategory) {
            this.brndCategory = brndCategory;
        }

        public Result withBrndCategory(String brndCategory) {
            this.brndCategory = brndCategory;
            return this;
        }

        public String getBrndImg() {
            return brndImg;
        }

        public void setBrndImg(String brndImg) {
            this.brndImg = brndImg;
        }

        public Result withBrndImg(String brndImg) {
            this.brndImg = brndImg;
            return this;
        }

        public String getBrndUrl() {
            return brndUrl;
        }

        public void setBrndUrl(String brndUrl) {
            this.brndUrl = brndUrl;
        }

        public Result withBrndUrl(String brndUrl) {
            this.brndUrl = brndUrl;
            return this;
        }

        public String getBrndCreateDate() {
            return brndCreateDate;
        }

        public void setBrndCreateDate(String brndCreateDate) {
            this.brndCreateDate = brndCreateDate;
        }

        public Result withBrndCreateDate(String brndCreateDate) {
            this.brndCreateDate = brndCreateDate;
            return this;
        }

        public String getBrndUpdateDate() {
            return brndUpdateDate;
        }

        public void setBrndUpdateDate(String brndUpdateDate) {
            this.brndUpdateDate = brndUpdateDate;
        }

        public Result withBrndUpdateDate(String brndUpdateDate) {
            this.brndUpdateDate = brndUpdateDate;
            return this;
        }

        public String getBrndCreateBy() {
            return brndCreateBy;
        }

        public void setBrndCreateBy(String brndCreateBy) {
            this.brndCreateBy = brndCreateBy;
        }

        public Result withBrndCreateBy(String brndCreateBy) {
            this.brndCreateBy = brndCreateBy;
            return this;
        }

        public String getBrndUpdateBy() {
            return brndUpdateBy;
        }

        public void setBrndUpdateBy(String brndUpdateBy) {
            this.brndUpdateBy = brndUpdateBy;
        }

        public Result withBrndUpdateBy(String brndUpdateBy) {
            this.brndUpdateBy = brndUpdateBy;
            return this;
        }

        public String getBrndStatus() {
            return brndStatus;
        }

        public void setBrndStatus(String brndStatus) {
            this.brndStatus = brndStatus;
        }

        public Result withBrndStatus(String brndStatus) {
            this.brndStatus = brndStatus;
            return this;
        }

    }

}