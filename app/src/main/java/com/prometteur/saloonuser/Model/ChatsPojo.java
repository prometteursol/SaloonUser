package com.prometteur.saloonuser.Model;

public class ChatsPojo {

    long Time;
    String Message;
    String Senderid;
    String Receiverid;

    public ChatsPojo() {
    }

    public ChatsPojo(long time, String message, String senderid, String receiverid) {
        Time = time;
        Message = message;
        Senderid = senderid;
        Receiverid = receiverid;
    }

    public long getTime() {
        return Time;
    }

    public void setTime(long time) {
        Time = time;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    /*public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }*/

    public String getSenderid() {
        return Senderid;
    }

    public void setSenderid(String senderid) {
        Senderid = senderid;
    }

    public String getReceiverid() {
        return Receiverid;
    }

    public void setReceiverid(String receiverid) {
        Receiverid = receiverid;
    }
}
