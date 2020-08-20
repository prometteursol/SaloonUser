package com.prometteur.saloonuser.Model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppointmentBean implements Serializable
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
@SerializedName("ongoing_appointments")
@Expose
private List<OngoingAppointment> ongoingAppointments = null;
    @SerializedName("online_payment_redeem_point")
    @Expose
    private String onlinePaymentRedeemPoint;
private final static long serialVersionUID = -9164482427134203408L;

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

public List<OngoingAppointment> getOngoingAppointments() {
return ongoingAppointments;
}

public void setOngoingAppointments(List<OngoingAppointment> ongoingAppointments) {
this.ongoingAppointments = ongoingAppointments;
}

    public String getOnlinePaymentRedeemPoint() {
        return onlinePaymentRedeemPoint;
    }

    public void setOnlinePaymentRedeemPoint(String onlinePaymentRedeemPoint) {
        this.onlinePaymentRedeemPoint = onlinePaymentRedeemPoint;
    }

    public class OngoingAppointment implements Serializable
    {

        @SerializedName("apt_id")
        @Expose
        private String aptId;
        @SerializedName("apt_date")
        @Expose
        private String aptDate;
        @SerializedName("apt_time")
        @Expose
        private String aptTime;
        @SerializedName("apt_payment_type")
        @Expose
        private String aptPaymentType;
        @SerializedName("apt_payment_status")
        @Expose
        private String aptPaymentStatus;
        @SerializedName("apt_amount")
        @Expose
        private String aptAmount;
        @SerializedName("apt_status")
        @Expose
        private String aptStatus;
        @SerializedName("bran_id")
        @Expose
        private String branId;
        @SerializedName("bran_name")
        @Expose
        private String branName;
        @SerializedName("bran_addr")
        @Expose
        private String branAddr;
        @SerializedName("bran_img")
        @Expose
        private String branImg;
        @SerializedName("bran_open_status")
        @Expose
        private String branOpenStatus;
        @SerializedName("bran_lat")
        @Expose
        private String branLat;
        @SerializedName("bran_lon")
        @Expose
        private String branLon;
        @SerializedName("distance")
        @Expose
        private String distance;
        @SerializedName("salon_rating")
        @Expose
        private String salonRating;
        @SerializedName("services")
        @Expose
        private String services;
        @SerializedName("apt_start_otp")
        @Expose
        private String aptStartOtp;
        @SerializedName("apt_end_otp")
        @Expose
        private String aptEndOtp;
        private final static long serialVersionUID = -1446791079186419948L;

        public String getAptId() {
            return aptId;
        }

        public void setAptId(String aptId) {
            this.aptId = aptId;
        }

        public String getAptDate() {
            return aptDate;
        }

        public void setAptDate(String aptDate) {
            this.aptDate = aptDate;
        }

        public String getAptTime() {
            return aptTime;
        }

        public void setAptTime(String aptTime) {
            this.aptTime = aptTime;
        }

        public String getAptPaymentType() {
            return aptPaymentType;
        }

        public void setAptPaymentType(String aptPaymentType) {
            this.aptPaymentType = aptPaymentType;
        }

        public String getAptPaymentStatus() {
            return aptPaymentStatus;
        }

        public void setAptPaymentStatus(String aptPaymentStatus) {
            this.aptPaymentStatus = aptPaymentStatus;
        }

        public String getAptAmount() {
            return aptAmount;
        }

        public void setAptAmount(String aptAmount) {
            this.aptAmount = aptAmount;
        }

        public String getAptStatus() {
            return aptStatus;
        }

        public void setAptStatus(String aptStatus) {
            this.aptStatus = aptStatus;
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

        public String getBranAddr() {
            return branAddr;
        }

        public void setBranAddr(String branAddr) {
            this.branAddr = branAddr;
        }

        public String getBranImg() {
            return branImg;
        }

        public void setBranImg(String branImg) {
            this.branImg = branImg;
        }

        public String getBranOpenStatus() {
            return branOpenStatus;
        }

        public void setBranOpenStatus(String branOpenStatus) {
            this.branOpenStatus = branOpenStatus;
        }

        public String getBranLat() {
            return branLat;
        }

        public void setBranLat(String branLat) {
            this.branLat = branLat;
        }

        public String getBranLon() {
            return branLon;
        }

        public void setBranLon(String branLon) {
            this.branLon = branLon;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getSalonRating() {
            return salonRating;
        }

        public void setSalonRating(String salonRating) {
            this.salonRating = salonRating;
        }
        public String getServices() {
            return services;
        }

        public void setServices(String services) {
            this.services = services;
        }
        public String getAptStartOtp() {
            return aptStartOtp;
        }

        public void setAptStartOtp(String aptStartOtp) {
            this.aptStartOtp = aptStartOtp;
        }

        public String getAptEndOtp() {
            return aptEndOtp;
        }

        public void setAptEndOtp(String aptEndOtp) {
            this.aptEndOtp = aptEndOtp;
        }
    }

    public class Result implements Serializable
    {

        @SerializedName("apt_id")
        @Expose
        private String aptId;
        @SerializedName("apt_date")
        @Expose
        private String aptDate;
        @SerializedName("apt_time")
        @Expose
        private String aptTime;
        @SerializedName("apt_payment_type")
        @Expose
        private String aptPaymentType;
        @SerializedName("apt_payment_status")
        @Expose
        private String aptPaymentStatus;
        @SerializedName("apt_amount")
        @Expose
        private String aptAmount;
        @SerializedName("apt_status")
        @Expose
        private String aptStatus;
        @SerializedName("bran_id")
        @Expose
        private String branId;
        @SerializedName("bran_name")
        @Expose
        private String branName;
        @SerializedName("bran_addr")
        @Expose
        private String branAddr;
        @SerializedName("bran_img")
        @Expose
        private String branImg;
        @SerializedName("bran_open_status")
        @Expose
        private String branOpenStatus;
        @SerializedName("bran_lat")
        @Expose
        private String branLat;
        @SerializedName("bran_lon")
        @Expose
        private String branLon;
        @SerializedName("distance")
        @Expose
        private String distance;
        @SerializedName("salon_rating")
        @Expose
        private String salonRating;
        @SerializedName("services")
        @Expose
        private String services;
        private final static long serialVersionUID = -2850369189917837891L;

        public String getAptId() {
            return aptId;
        }

        public void setAptId(String aptId) {
            this.aptId = aptId;
        }

        public String getAptDate() {
            return aptDate;
        }

        public void setAptDate(String aptDate) {
            this.aptDate = aptDate;
        }

        public String getAptTime() {
            return aptTime;
        }

        public void setAptTime(String aptTime) {
            this.aptTime = aptTime;
        }

        public String getAptPaymentType() {
            return aptPaymentType;
        }

        public void setAptPaymentType(String aptPaymentType) {
            this.aptPaymentType = aptPaymentType;
        }

        public String getAptPaymentStatus() {
            return aptPaymentStatus;
        }

        public void setAptPaymentStatus(String aptPaymentStatus) {
            this.aptPaymentStatus = aptPaymentStatus;
        }

        public String getAptAmount() {
            return aptAmount;
        }

        public void setAptAmount(String aptAmount) {
            this.aptAmount = aptAmount;
        }

        public String getAptStatus() {
            return aptStatus;
        }

        public void setAptStatus(String aptStatus) {
            this.aptStatus = aptStatus;
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

        public String getBranAddr() {
            return branAddr;
        }

        public void setBranAddr(String branAddr) {
            this.branAddr = branAddr;
        }

        public String getBranImg() {
            return branImg;
        }

        public void setBranImg(String branImg) {
            this.branImg = branImg;
        }

        public String getBranOpenStatus() {
            return branOpenStatus;
        }

        public void setBranOpenStatus(String branOpenStatus) {
            this.branOpenStatus = branOpenStatus;
        }

        public String getBranLat() {
            return branLat;
        }

        public void setBranLat(String branLat) {
            this.branLat = branLat;
        }

        public String getBranLon() {
            return branLon;
        }

        public void setBranLon(String branLon) {
            this.branLon = branLon;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getSalonRating() {
            return salonRating;
        }

        public void setSalonRating(String salonRating) {
            this.salonRating = salonRating;
        }

        public String getServices() {
            return services;
        }

        public void setServices(String services) {
            this.services = services;
        }
    }

}