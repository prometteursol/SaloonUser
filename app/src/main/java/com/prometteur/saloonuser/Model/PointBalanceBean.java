package com.prometteur.saloonuser.Model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PointBalanceBean implements Serializable
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
    @SerializedName("balance")
    @Expose
    private String balance;
    private final static long serialVersionUID = 2123776522554598577L;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public PointBalanceBean withStatus(Integer status) {
        this.status = status;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public PointBalanceBean withMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public PointBalanceBean withResult(List<Result> result) {
        this.result = result;
        return this;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public PointBalanceBean withBalance(String balance) {
        this.balance = balance;
        return this;
    }


    public class Result implements Serializable
    {

        @SerializedName("redm_id")
        @Expose
        private String redmId;
        @SerializedName("redm_user_id")
        @Expose
        private String redmUserId;
        @SerializedName("redm_points")
        @Expose
        private String redmPoints;
        @SerializedName("redm_status")
        @Expose
        private String redmStatus;
        @SerializedName("redm_create_date")
        @Expose
        private String redmCreateDate;
        private final static long serialVersionUID = 670194661027655261L;

        public String getRedmId() {
            return redmId;
        }

        public void setRedmId(String redmId) {
            this.redmId = redmId;
        }

        public Result withRedmId(String redmId) {
            this.redmId = redmId;
            return this;
        }

        public String getRedmUserId() {
            return redmUserId;
        }

        public void setRedmUserId(String redmUserId) {
            this.redmUserId = redmUserId;
        }

        public Result withRedmUserId(String redmUserId) {
            this.redmUserId = redmUserId;
            return this;
        }

        public String getRedmPoints() {
            return redmPoints;
        }

        public void setRedmPoints(String redmPoints) {
            this.redmPoints = redmPoints;
        }

        public Result withRedmPoints(String redmPoints) {
            this.redmPoints = redmPoints;
            return this;
        }

        public String getRedmStatus() {
            return redmStatus;
        }

        public void setRedmStatus(String redmStatus) {
            this.redmStatus = redmStatus;
        }

        public Result withRedmStatus(String redmStatus) {
            this.redmStatus = redmStatus;
            return this;
        }

        public String getRedmCreateDate() {
            return redmCreateDate;
        }

        public void setRedmCreateDate(String redmCreateDate) {
            this.redmCreateDate = redmCreateDate;
        }

        public Result withRedmCreateDate(String redmCreateDate) {
            this.redmCreateDate = redmCreateDate;
            return this;
        }

    }
}