package com.gocreative.tm.mahrem_ene.Models;

import com.google.firebase.Timestamp;

public class Note {
    private String note;
    private Timestamp date_created;
    private String date_in_calendar;
    private String id;

    // Empty constructor for firebase
    public Note(){}

    public Note(String note, Timestamp date_created, String date_in_calendar, String id) {
        this.note = note;
        this.date_created = date_created;
        this.date_in_calendar = date_in_calendar;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Timestamp getDate_created() {
        return date_created;
    }

    public void setDate_created(Timestamp date_created) {
        this.date_created = date_created;
    }

    public String getDate_in_calendar() {
        return date_in_calendar;
    }

    public void setDate_in_calendar(String date_in_calendar) {
        this.date_in_calendar = date_in_calendar;
    }
}
