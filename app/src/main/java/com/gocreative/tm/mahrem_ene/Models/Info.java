package com.gocreative.tm.mahrem_ene.Models;

import java.io.Serializable;

public class Info implements Serializable {
    private String title, text, imageUrl;
    private int week;

    public Info(){}

    public Info(String title, String text, String imageUrl, int week) {
        this.title = title;
        this.text = text;
        this.imageUrl = imageUrl;
        this.week = week;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageLink() {
        return imageUrl;
    }

    public void setImageLink(String imageLink) {
        this.imageUrl = imageLink;
    }
}
