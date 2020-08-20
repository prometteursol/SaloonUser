package com.prometteur.saloonuser.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Transfer implements Serializable
{

    @SerializedName("recipient")
    @Expose
    private String recipient;
    @SerializedName("amount")
    @Expose
    private Integer amount;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("notes")
    @Expose
    private List<Object> notes = null;
    @SerializedName("linked_account_notes")
    @Expose
    private List<Object> linkedAccountNotes = null;
    @SerializedName("on_hold")
    @Expose
    private Boolean onHold;
    @SerializedName("on_hold_until")
    @Expose
    private Object onHoldUntil;
    private final static long serialVersionUID = -6793904136540829073L;

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<Object> getNotes() {
        return notes;
    }

    public void setNotes(List<Object> notes) {
        this.notes = notes;
    }

    public List<Object> getLinkedAccountNotes() {
        return linkedAccountNotes;
    }

    public void setLinkedAccountNotes(List<Object> linkedAccountNotes) {
        this.linkedAccountNotes = linkedAccountNotes;
    }

    public Boolean getOnHold() {
        return onHold;
    }

    public void setOnHold(Boolean onHold) {
        this.onHold = onHold;
    }

    public Object getOnHoldUntil() {
        return onHoldUntil;
    }

    public void setOnHoldUntil(Object onHoldUntil) {
        this.onHoldUntil = onHoldUntil;
    }

}