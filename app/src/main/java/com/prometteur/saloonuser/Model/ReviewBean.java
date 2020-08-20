package com.prometteur.saloonuser.Model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewBean implements Serializable
{

@SerializedName("status")
@Expose
private Integer status;
@SerializedName("msg")
@Expose
private String msg;
@SerializedName("result")
@Expose
private List<Integer> result = null;
private final static long serialVersionUID = -3329491587582241503L;

public Integer getStatus() {
return status;
}

public void setStatus(Integer status) {
this.status = status;
}

public ReviewBean withStatus(Integer status) {
this.status = status;
return this;
}

public String getMsg() {
return msg;
}

public void setMsg(String msg) {
this.msg = msg;
}

public ReviewBean withMsg(String msg) {
this.msg = msg;
return this;
}

public List<Integer> getResult() {
return result;
}

public void setResult(List<Integer> result) {
this.result = result;
}

public ReviewBean withResult(List<Integer> result) {
this.result = result;
return this;
}

}