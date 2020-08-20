package com.prometteur.saloonuser.Model;

public class HistoryDataModel {

      String message;
     String date;

    public HistoryDataModel(String message, String date) {
        this.message = message;
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



}
