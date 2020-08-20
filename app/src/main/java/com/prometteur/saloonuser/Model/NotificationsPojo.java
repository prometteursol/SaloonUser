package com.prometteur.saloonuser.Model;

public class NotificationsPojo {

    String notificationTitle;
    String notificationDesc;
    String notificationPayment;
    String notificationTimeSpan;

    public NotificationsPojo(String notificationTitle, String notificationDesc, String notificationPayment, String notificationTimeSpan) {
        this.notificationTitle = notificationTitle;
        this.notificationDesc = notificationDesc;
        this.notificationPayment = notificationPayment;
        this.notificationTimeSpan = notificationTimeSpan;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public String getNotificationDesc() {
        return notificationDesc;
    }

    public void setNotificationDesc(String notificationDesc) {
        this.notificationDesc = notificationDesc;
    }

    public String getNotificationPayment() {
        return notificationPayment;
    }

    public void setNotificationPayment(String notificationPayment) {
        this.notificationPayment = notificationPayment;
    }

    public String getNotificationTimeSpan() {
        return notificationTimeSpan;
    }

    public void setNotificationTimeSpan(String notificationTimeSpan) {
        this.notificationTimeSpan = notificationTimeSpan;
    }
}
