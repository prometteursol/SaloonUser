package com.prometteur.saloonuser.Model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgotPassBean implements Serializable
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
private final static long serialVersionUID = 1356941622715741681L;

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

        @SerializedName("otp")
        @Expose
        private Integer otp;
        private final static long serialVersionUID = -2041526709218802566L;

        public Integer getOtp() {
            return otp;
        }

        public void setOtp(Integer otp) {
            this.otp = otp;
        }

    }
}