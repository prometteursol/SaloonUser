package com.prometteur.saloonuser.Model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationBean implements Serializable
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
private final static long serialVersionUID = -8329798700789834755L;

public Integer getStatus() {
return status;
}

public void setStatus(Integer status) {
this.status = status;
}

public NotificationBean withStatus(Integer status) {
this.status = status;
return this;
}

public String getMsg() {
return msg;
}

public void setMsg(String msg) {
this.msg = msg;
}

public NotificationBean withMsg(String msg) {
this.msg = msg;
return this;
}

public List<Result> getResult() {
return result;
}

public void setResult(List<Result> result) {
this.result = result;
}

public NotificationBean withResult(List<Result> result) {
this.result = result;
return this;
}

    public class Result implements Serializable
    {

        @SerializedName("noti_id")
        @Expose
        private String notiId;
        @SerializedName("noti_title")
        @Expose
        private String notiTitle;
        @SerializedName("noti_message")
        @Expose
        private String notiMessage;
        @SerializedName("noti_appointment_id")
        @Expose
        private String notiAppointmentId;
        @SerializedName("noti_branch_id")
        @Expose
        private String notiBranchId;
        @SerializedName("noti_end_user_id")
        @Expose
        private String notiEndUserId;
        @SerializedName("noti_cp_user_id")
        @Expose
        private String notiCpUserId;
        @SerializedName("noti_from")
        @Expose
        private String notiFrom;
        @SerializedName("noti_to")
        @Expose
        private String notiTo;
        @SerializedName("noti_type")
        @Expose
        private String notiType;
        @SerializedName("noti_read_status")
        @Expose
        private String notiReadStatus;
        @SerializedName("noti_create_date")
        @Expose
        private String notiCreateDate;
        @SerializedName("apt_id")
        @Expose
        private String aptId;
        @SerializedName("apt_cp_branch")
        @Expose
        private String aptCpBranch;
        @SerializedName("apt_user_id")
        @Expose
        private String aptUserId;
        @SerializedName("apt_date")
        @Expose
        private String aptDate;
        @SerializedName("apt_time")
        @Expose
        private String aptTime;
        @SerializedName("apt_subtotal")
        @Expose
        private String aptSubtotal;
        @SerializedName("apt_gst")
        @Expose
        private String aptGst;
        @SerializedName("apt_amount")
        @Expose
        private String aptAmount;
        @SerializedName("apt_payment_status")
        @Expose
        private String aptPaymentStatus;
        @SerializedName("apt_payment_type")
        @Expose
        private String aptPaymentType;
        @SerializedName("apt_payment_date")
        @Expose
        private String aptPaymentDate;
        @SerializedName("apt_reshedule_by")
        @Expose
        private String aptResheduleBy;
        @SerializedName("apt_reshedule_date")
        @Expose
        private String aptResheduleDate;
        @SerializedName("apt_status")
        @Expose
        private String aptStatus;
        @SerializedName("apt_reject_reason")
        @Expose
        private String aptRejectReason;
        @SerializedName("apt_start_otp")
        @Expose
        private String aptStartOtp;
        @SerializedName("apt_end_otp")
        @Expose
        private String aptEndOtp;
        @SerializedName("apt_coupon_id")
        @Expose
        private String aptCouponId;
        @SerializedName("apt_create_date")
        @Expose
        private String aptCreateDate;
        @SerializedName("apt_create_by")
        @Expose
        private String aptCreateBy;
        @SerializedName("apt_update_date")
        @Expose
        private String aptUpdateDate;
        @SerializedName("apt_update_by")
        @Expose
        private String aptUpdateBy;
        private final static long serialVersionUID = -1919243692835469222L;

        public String getNotiId() {
            return notiId;
        }

        public void setNotiId(String notiId) {
            this.notiId = notiId;
        }

        public Result withNotiId(String notiId) {
            this.notiId = notiId;
            return this;
        }

        public String getNotiTitle() {
            return notiTitle;
        }

        public void setNotiTitle(String notiTitle) {
            this.notiTitle = notiTitle;
        }

        public Result withNotiTitle(String notiTitle) {
            this.notiTitle = notiTitle;
            return this;
        }

        public String getNotiMessage() {
            return notiMessage;
        }

        public void setNotiMessage(String notiMessage) {
            this.notiMessage = notiMessage;
        }

        public Result withNotiMessage(String notiMessage) {
            this.notiMessage = notiMessage;
            return this;
        }

        public String getNotiAppointmentId() {
            return notiAppointmentId;
        }

        public void setNotiAppointmentId(String notiAppointmentId) {
            this.notiAppointmentId = notiAppointmentId;
        }

        public Result withNotiAppointmentId(String notiAppointmentId) {
            this.notiAppointmentId = notiAppointmentId;
            return this;
        }

        public String getNotiBranchId() {
            return notiBranchId;
        }

        public void setNotiBranchId(String notiBranchId) {
            this.notiBranchId = notiBranchId;
        }

        public Result withNotiBranchId(String notiBranchId) {
            this.notiBranchId = notiBranchId;
            return this;
        }

        public String getNotiEndUserId() {
            return notiEndUserId;
        }

        public void setNotiEndUserId(String notiEndUserId) {
            this.notiEndUserId = notiEndUserId;
        }

        public Result withNotiEndUserId(String notiEndUserId) {
            this.notiEndUserId = notiEndUserId;
            return this;
        }

        public String getNotiCpUserId() {
            return notiCpUserId;
        }

        public void setNotiCpUserId(String notiCpUserId) {
            this.notiCpUserId = notiCpUserId;
        }

        public Result withNotiCpUserId(String notiCpUserId) {
            this.notiCpUserId = notiCpUserId;
            return this;
        }

        public String getNotiFrom() {
            return notiFrom;
        }

        public void setNotiFrom(String notiFrom) {
            this.notiFrom = notiFrom;
        }

        public Result withNotiFrom(String notiFrom) {
            this.notiFrom = notiFrom;
            return this;
        }

        public String getNotiTo() {
            return notiTo;
        }

        public void setNotiTo(String notiTo) {
            this.notiTo = notiTo;
        }

        public Result withNotiTo(String notiTo) {
            this.notiTo = notiTo;
            return this;
        }

        public String getNotiType() {
            return notiType;
        }

        public void setNotiType(String notiType) {
            this.notiType = notiType;
        }

        public Result withNotiType(String notiType) {
            this.notiType = notiType;
            return this;
        }

        public String getNotiReadStatus() {
            return notiReadStatus;
        }

        public void setNotiReadStatus(String notiReadStatus) {
            this.notiReadStatus = notiReadStatus;
        }

        public Result withNotiReadStatus(String notiReadStatus) {
            this.notiReadStatus = notiReadStatus;
            return this;
        }

        public String getNotiCreateDate() {
            return notiCreateDate;
        }

        public void setNotiCreateDate(String notiCreateDate) {
            this.notiCreateDate = notiCreateDate;
        }

        public Result withNotiCreateDate(String notiCreateDate) {
            this.notiCreateDate = notiCreateDate;
            return this;
        }

        public String getAptId() {
            return aptId;
        }

        public void setAptId(String aptId) {
            this.aptId = aptId;
        }

        public Result withAptId(String aptId) {
            this.aptId = aptId;
            return this;
        }

        public String getAptCpBranch() {
            return aptCpBranch;
        }

        public void setAptCpBranch(String aptCpBranch) {
            this.aptCpBranch = aptCpBranch;
        }

        public Result withAptCpBranch(String aptCpBranch) {
            this.aptCpBranch = aptCpBranch;
            return this;
        }

        public String getAptUserId() {
            return aptUserId;
        }

        public void setAptUserId(String aptUserId) {
            this.aptUserId = aptUserId;
        }

        public Result withAptUserId(String aptUserId) {
            this.aptUserId = aptUserId;
            return this;
        }

        public String getAptDate() {
            return aptDate;
        }

        public void setAptDate(String aptDate) {
            this.aptDate = aptDate;
        }

        public Result withAptDate(String aptDate) {
            this.aptDate = aptDate;
            return this;
        }

        public String getAptTime() {
            return aptTime;
        }

        public void setAptTime(String aptTime) {
            this.aptTime = aptTime;
        }

        public Result withAptTime(String aptTime) {
            this.aptTime = aptTime;
            return this;
        }

        public String getAptSubtotal() {
            return aptSubtotal;
        }

        public void setAptSubtotal(String aptSubtotal) {
            this.aptSubtotal = aptSubtotal;
        }

        public Result withAptSubtotal(String aptSubtotal) {
            this.aptSubtotal = aptSubtotal;
            return this;
        }

        public String getAptGst() {
            return aptGst;
        }

        public void setAptGst(String aptGst) {
            this.aptGst = aptGst;
        }

        public Result withAptGst(String aptGst) {
            this.aptGst = aptGst;
            return this;
        }

        public String getAptAmount() {
            return aptAmount;
        }

        public void setAptAmount(String aptAmount) {
            this.aptAmount = aptAmount;
        }

        public Result withAptAmount(String aptAmount) {
            this.aptAmount = aptAmount;
            return this;
        }

        public String getAptPaymentStatus() {
            return aptPaymentStatus;
        }

        public void setAptPaymentStatus(String aptPaymentStatus) {
            this.aptPaymentStatus = aptPaymentStatus;
        }

        public Result withAptPaymentStatus(String aptPaymentStatus) {
            this.aptPaymentStatus = aptPaymentStatus;
            return this;
        }

        public String getAptPaymentType() {
            return aptPaymentType;
        }

        public void setAptPaymentType(String aptPaymentType) {
            this.aptPaymentType = aptPaymentType;
        }

        public Result withAptPaymentType(String aptPaymentType) {
            this.aptPaymentType = aptPaymentType;
            return this;
        }

        public String getAptPaymentDate() {
            return aptPaymentDate;
        }

        public void setAptPaymentDate(String aptPaymentDate) {
            this.aptPaymentDate = aptPaymentDate;
        }

        public Result withAptPaymentDate(String aptPaymentDate) {
            this.aptPaymentDate = aptPaymentDate;
            return this;
        }

        public String getAptResheduleBy() {
            return aptResheduleBy;
        }

        public void setAptResheduleBy(String aptResheduleBy) {
            this.aptResheduleBy = aptResheduleBy;
        }

        public Result withAptResheduleBy(String aptResheduleBy) {
            this.aptResheduleBy = aptResheduleBy;
            return this;
        }

        public String getAptResheduleDate() {
            return aptResheduleDate;
        }

        public void setAptResheduleDate(String aptResheduleDate) {
            this.aptResheduleDate = aptResheduleDate;
        }

        public Result withAptResheduleDate(String aptResheduleDate) {
            this.aptResheduleDate = aptResheduleDate;
            return this;
        }

        public String getAptStatus() {
            return aptStatus;
        }

        public void setAptStatus(String aptStatus) {
            this.aptStatus = aptStatus;
        }

        public Result withAptStatus(String aptStatus) {
            this.aptStatus = aptStatus;
            return this;
        }

        public String getAptRejectReason() {
            return aptRejectReason;
        }

        public void setAptRejectReason(String aptRejectReason) {
            this.aptRejectReason = aptRejectReason;
        }

        public Result withAptRejectReason(String aptRejectReason) {
            this.aptRejectReason = aptRejectReason;
            return this;
        }

        public String getAptStartOtp() {
            return aptStartOtp;
        }

        public void setAptStartOtp(String aptStartOtp) {
            this.aptStartOtp = aptStartOtp;
        }

        public Result withAptStartOtp(String aptStartOtp) {
            this.aptStartOtp = aptStartOtp;
            return this;
        }

        public String getAptEndOtp() {
            return aptEndOtp;
        }

        public void setAptEndOtp(String aptEndOtp) {
            this.aptEndOtp = aptEndOtp;
        }

        public Result withAptEndOtp(String aptEndOtp) {
            this.aptEndOtp = aptEndOtp;
            return this;
        }

        public String getAptCouponId() {
            return aptCouponId;
        }

        public void setAptCouponId(String aptCouponId) {
            this.aptCouponId = aptCouponId;
        }

        public Result withAptCouponId(String aptCouponId) {
            this.aptCouponId = aptCouponId;
            return this;
        }

        public String getAptCreateDate() {
            return aptCreateDate;
        }

        public void setAptCreateDate(String aptCreateDate) {
            this.aptCreateDate = aptCreateDate;
        }

        public Result withAptCreateDate(String aptCreateDate) {
            this.aptCreateDate = aptCreateDate;
            return this;
        }

        public String getAptCreateBy() {
            return aptCreateBy;
        }

        public void setAptCreateBy(String aptCreateBy) {
            this.aptCreateBy = aptCreateBy;
        }

        public Result withAptCreateBy(String aptCreateBy) {
            this.aptCreateBy = aptCreateBy;
            return this;
        }

        public String getAptUpdateDate() {
            return aptUpdateDate;
        }

        public void setAptUpdateDate(String aptUpdateDate) {
            this.aptUpdateDate = aptUpdateDate;
        }

        public Result withAptUpdateDate(String aptUpdateDate) {
            this.aptUpdateDate = aptUpdateDate;
            return this;
        }

        public String getAptUpdateBy() {
            return aptUpdateBy;
        }

        public void setAptUpdateBy(String aptUpdateBy) {
            this.aptUpdateBy = aptUpdateBy;
        }

        public Result withAptUpdateBy(String aptUpdateBy) {
            this.aptUpdateBy = aptUpdateBy;
            return this;
        }

    }

}