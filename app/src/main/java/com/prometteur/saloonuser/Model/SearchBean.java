package com.prometteur.saloonuser.Model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchBean implements Serializable
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
private final static long serialVersionUID = 5123085022909096357L;

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

        @SerializedName("bran_id")
        @Expose
        private String branId;
        @SerializedName("bran_name")
        @Expose
        private String branName;
        @SerializedName("bran_img")
        @Expose
        private String branImg;
        @SerializedName("bran_addr")
        @Expose
        private String branAddr;
        @SerializedName("bran_city")
        @Expose
        private String branCity;
        private final static long serialVersionUID = -5214747948998079819L;
        @SerializedName("bran_cat_main")
        @Expose
        private String branCatMain;
        public String getBranCatMain() {
            return branCatMain;
        }

        public void setBranCatMain(String branCatMain) {
            this.branCatMain = branCatMain;
        }
        public String getBranId() {
            return branId;
        }

        public void setBranId(String branId) {
            this.branId = branId;
        }

        public String getBranName() {
            return branName;
        }

        public void setBranName(String branName) {
            this.branName = branName;
        }

        public String getBranImg() {
            return branImg;
        }

        public void setBranImg(String branImg) {
            this.branImg = branImg;
        }

        public String getBranAddr() {
            return branAddr;
        }

        public void setBranAddr(String branAddr) {
            this.branAddr = branAddr;
        }

        public String getBranCity() {
            return branCity;
        }

        public void setBranCity(String branCity) {
            this.branCity = branCity;
        }

    }
}