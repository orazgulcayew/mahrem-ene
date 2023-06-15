package com.gocreative.tm.lukman.Models;

import java.util.Date;

public class Message {
    private String message, sent_by, type;
    private Date sent_at;

    public Message(){}

    public Message(String message, String sent_by, String type, Date sent_at) {
        this.message = message;
        this.sent_by = sent_by;
        this.type = type;
        this.sent_at = sent_at;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSent_by() {
        return sent_by;
    }

    public void setSent_by(String sent_by) {
        this.sent_by = sent_by;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getSent_at() {
        return sent_at;
    }

    public void setSent_at(Date sent_at) {
        this.sent_at = sent_at;
    }
}
