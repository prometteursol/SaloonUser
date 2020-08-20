package com.prometteur.saloonuser.Model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewDetailsBean implements Serializable
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
private final static long serialVersionUID = 3652327295519580011L;

public Integer getStatus() {
return status;
}

public void setStatus(Integer status) {
this.status = status;
}

public ReviewDetailsBean withStatus(Integer status) {
this.status = status;
return this;
}

public String getMsg() {
return msg;
}

public void setMsg(String msg) {
this.msg = msg;
}

public ReviewDetailsBean withMsg(String msg) {
this.msg = msg;
return this;
}

public List<Result> getResult() {
return result;
}

public void setResult(List<Result> result) {
this.result = result;
}

public ReviewDetailsBean withResult(List<Result> result) {
this.result = result;
return this;
}

    public class Result implements Serializable
    {

        @SerializedName("bran_id")
        @Expose
        private String branId;
        @SerializedName("bran_name")
        @Expose
        private String branName;
        @SerializedName("operators")
        @Expose
        private List<Operator> operators = null;
        private final static long serialVersionUID = 8168753990342617212L;

        public String getBranId() {
            return branId;
        }

        public void setBranId(String branId) {
            this.branId = branId;
        }

        public Result withBranId(String branId) {
            this.branId = branId;
            return this;
        }

        public String getBranName() {
            return branName;
        }

        public void setBranName(String branName) {
            this.branName = branName;
        }

        public Result withBranName(String branName) {
            this.branName = branName;
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

    }
    public class Operator implements Serializable
    {

        @SerializedName("srvc_id")
        @Expose
        private String srvcId;
        @SerializedName("srvc_name")
        @Expose
        private String srvcName;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("user_f_name")
        @Expose
        private String userFName;
        @SerializedName("user_l_name")
        @Expose
        private String userLName;
        @SerializedName("rating")
        @Expose
        private float rating=0.0f;
        private final static long serialVersionUID = -3356231541599252668L;

        public String getSrvcId() {
            return srvcId;
        }

        public void setSrvcId(String srvcId) {
            this.srvcId = srvcId;
        }

        public Operator withSrvcId(String srvcId) {
            this.srvcId = srvcId;
            return this;
        }

        public String getSrvcName() {
            return srvcName;
        }

        public void setSrvcName(String srvcName) {
            this.srvcName = srvcName;
        }

        public Operator withSrvcName(String srvcName) {
            this.srvcName = srvcName;
            return this;
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

        public float getRating() {
            return rating;
        }

        public void setRating(float rating) {
            this.rating = rating;
        }
    }

}