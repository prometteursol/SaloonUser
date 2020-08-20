package com.prometteur.saloonuser.Model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginBean implements Serializable
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

private final static long serialVersionUID = -5584638056136503349L;

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

        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("user_name")
        @Expose
        private String userName;
        @SerializedName("user_email")
        @Expose
        private String userEmail;
        @SerializedName("user_mb_no")
        @Expose
        private String userMbNo;
        @SerializedName("user_password")
        @Expose
        private String userPassword;
        @SerializedName("user_f_name")
        @Expose
        private String userFName;
        @SerializedName("user_m_name")
        @Expose
        private String userMName;
        @SerializedName("user_l_name")
        @Expose
        private String userLName;
        @SerializedName("user_dob")
        @Expose
        private String userDob;
        @SerializedName("user_gender")
        @Expose
        private String userGender;
        @SerializedName("user_language")
        @Expose
        private String userLanguage;
        @SerializedName("user_lat")
        @Expose
        private String userLat;
        @SerializedName("user_lon")
        @Expose
        private String userLon;
        @SerializedName("user_img")
        @Expose
        private String userImg;
        @SerializedName("user_create_date")
        @Expose
        private String userCreateDate;
        @SerializedName("user_update_date")
        @Expose
        private String userUpdateDate;
        @SerializedName("user_create_by")
        @Expose
        private String userCreateBy;
        @SerializedName("user_update_by")
        @Expose
        private String userUpdateBy;
        @SerializedName("user_status")
        @Expose
        private String userStatus;
        @SerializedName("user_fcm_key")
        @Expose
        private String userFcmKey;
        @SerializedName("user_session")
        @Expose
        private String userSession;
        @SerializedName("user_media_id")
        @Expose
        private String userMediaId;
        @SerializedName("user_address")
        @Expose
        private String userAddress;
        @SerializedName("user_city")
        @Expose
        private String userCity;
        @SerializedName("user_state")
        @Expose
        private String userState;
        @SerializedName("user_pin_code")
        @Expose
        private String userPinCode;
        @SerializedName("user_referral_code")
        @Expose
        private String userReferralCode;
        @SerializedName("user_refer_code")
        @Expose
        private String userReferCode;
        @SerializedName("user_redeem")
        @Expose
        private String userRedeem;
        private final static long serialVersionUID = 4076123788693215843L;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserEmail() {
            return userEmail;
        }

        public void setUserEmail(String userEmail) {
            this.userEmail = userEmail;
        }

        public String getUserMbNo() {
            return userMbNo;
        }

        public void setUserMbNo(String userMbNo) {
            this.userMbNo = userMbNo;
        }

        public String getUserPassword() {
            return userPassword;
        }

        public void setUserPassword(String userPassword) {
            this.userPassword = userPassword;
        }

        public String getUserFName() {
            return userFName;
        }

        public void setUserFName(String userFName) {
            this.userFName = userFName;
        }

        public String getUserMName() {
            return userMName;
        }

        public void setUserMName(String userMName) {
            this.userMName = userMName;
        }

        public String getUserLName() {
            return userLName;
        }

        public void setUserLName(String userLName) {
            this.userLName = userLName;
        }

        public String getUserDob() {
            return userDob;
        }

        public void setUserDob(String userDob) {
            this.userDob = userDob;
        }

        public String getUserGender() {
            return userGender;
        }

        public void setUserGender(String userGender) {
            this.userGender = userGender;
        }

        public String getUserLanguage() {
            return userLanguage;
        }

        public void setUserLanguage(String userLanguage) {
            this.userLanguage = userLanguage;
        }

        public String getUserLat() {
            return userLat;
        }

        public void setUserLat(String userLat) {
            this.userLat = userLat;
        }

        public String getUserLon() {
            return userLon;
        }

        public void setUserLon(String userLon) {
            this.userLon = userLon;
        }

        public String getUserImg() {
            return userImg;
        }

        public void setUserImg(String userImg) {
            this.userImg = userImg;
        }

        public String getUserCreateDate() {
            return userCreateDate;
        }

        public void setUserCreateDate(String userCreateDate) {
            this.userCreateDate = userCreateDate;
        }

        public String getUserUpdateDate() {
            return userUpdateDate;
        }

        public void setUserUpdateDate(String userUpdateDate) {
            this.userUpdateDate = userUpdateDate;
        }

        public String getUserCreateBy() {
            return userCreateBy;
        }

        public void setUserCreateBy(String userCreateBy) {
            this.userCreateBy = userCreateBy;
        }

        public String getUserUpdateBy() {
            return userUpdateBy;
        }

        public void setUserUpdateBy(String userUpdateBy) {
            this.userUpdateBy = userUpdateBy;
        }

        public String getUserStatus() {
            return userStatus;
        }

        public void setUserStatus(String userStatus) {
            this.userStatus = userStatus;
        }

        public String getUserFcmKey() {
            return userFcmKey;
        }

        public void setUserFcmKey(String userFcmKey) {
            this.userFcmKey = userFcmKey;
        }

        public String getUserSession() {
            return userSession;
        }

        public void setUserSession(String userSession) {
            this.userSession = userSession;
        }

        public String getUserMediaId() {
            return userMediaId;
        }

        public void setUserMediaId(String userMediaId) {
            this.userMediaId = userMediaId;
        }

        public String getUserAddress() {
            return userAddress;
        }

        public void setUserAddress(String userAddress) {
            this.userAddress = userAddress;
        }

        public String getUserCity() {
            return userCity;
        }

        public void setUserCity(String userCity) {
            this.userCity = userCity;
        }

        public String getUserState() {
            return userState;
        }

        public void setUserState(String userState) {
            this.userState = userState;
        }

        public String getUserPinCode() {
            return userPinCode;
        }

        public void setUserPinCode(String userPinCode) {
            this.userPinCode = userPinCode;
        }

        public String getUserReferralCode() {
            return userReferralCode;
        }

        public void setUserReferralCode(String userReferralCode) {
            this.userReferralCode = userReferralCode;
        }

        public String getUserReferCode() {
            return userReferCode;
        }

        public void setUserReferCode(String userReferCode) {
            this.userReferCode = userReferCode;
        }

        public String getUserRedeem() {
            return userRedeem;
        }

        public void setUserRedeem(String userRedeem) {
            this.userRedeem = userRedeem;
        }
    }
}