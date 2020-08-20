package com.prometteur.saloonuser.Model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckPenaltyBean implements Serializable
{

@SerializedName("status")
@Expose
private Integer status;
@SerializedName("msg")
@Expose
private String msg;
@SerializedName("result")
@Expose
private List<String> result = null;
@SerializedName("penalty_percentage")
@Expose
private List<String> penaltyPercentage = null;
private final static long serialVersionUID = 51645878410159632L;

public Integer getStatus() {
return status;
}

public void setStatus(Integer status) {
this.status = status;
}

public CheckPenaltyBean withStatus(Integer status) {
this.status = status;
return this;
}

public String getMsg() {
return msg;
}

public void setMsg(String msg) {
this.msg = msg;
}

public CheckPenaltyBean withMsg(String msg) {
this.msg = msg;
return this;
}

public List<String> getResult() {
return result;
}

public void setResult(List<String> result) {
this.result = result;
}

public CheckPenaltyBean withResult(List<String> result) {
this.result = result;
return this;
}

    public List<String> getPenaltyPercentage() {
        return penaltyPercentage;
    }

    public void setPenaltyPercentage(List<String> penaltyPercentage) {
        this.penaltyPercentage = penaltyPercentage;
    }
}