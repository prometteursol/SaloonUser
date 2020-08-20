package com.prometteur.saloonuser.Model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpodatePassBean implements Serializable
{

@SerializedName("status")
@Expose
private Integer status;
@SerializedName("msg")
@Expose
private String msg;
@SerializedName("result")
@Expose
private List<Boolean> result = null;
private final static long serialVersionUID = -1619555447913135233L;

public Integer getStatus() {
return status;
}

public void setStatus(Integer status) {
this.status = status;
}

public UpodatePassBean withStatus(Integer status) {
this.status = status;
return this;
}

public String getMsg() {
return msg;
}

public void setMsg(String msg) {
this.msg = msg;
}

public UpodatePassBean withMsg(String msg) {
this.msg = msg;
return this;
}

public List<Boolean> getResult() {
return result;
}

public void setResult(List<Boolean> result) {
this.result = result;
}

public UpodatePassBean withResult(List<Boolean> result) {
this.result = result;
return this;
}

}