package com.prometteur.saloonuser.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CheckBean implements Serializable
{

@SerializedName("status")
@Expose
private Integer status;
@SerializedName("msg")
@Expose
private String msg;
@SerializedName("result")
@Expose
private Result result;
private final static long serialVersionUID = 5721306530745242530L;

public Integer getStatus() {
return status;
}

public void setStatus(Integer status) {
this.status = status;
}

public CheckBean withStatus(Integer status) {
this.status = status;
return this;
}

public String getMsg() {
return msg;
}

public void setMsg(String msg) {
this.msg = msg;
}

public CheckBean withMsg(String msg) {
this.msg = msg;
return this;
}

public Result getResult() {
return result;
}

public void setResult(Result result) {
this.result = result;
}

public CheckBean withResult(Result result) {
this.result = result;
return this;
}
    public class Result implements Serializable
    {

        @SerializedName("otp")
        @Expose
        private Integer otp;
        private final static long serialVersionUID = 8237603327180901837L;

        public Integer getOtp() {
            return otp;
        }

        public void setOtp(Integer otp) {
            this.otp = otp;
        }

        public Result withOtp(Integer otp) {
            this.otp = otp;
            return this;
        }

    }
}