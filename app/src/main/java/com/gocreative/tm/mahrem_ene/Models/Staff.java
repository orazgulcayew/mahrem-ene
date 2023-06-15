package com.gocreative.tm.mahrem_ene.Models;

public class Staff {
    private String staffName, id;

    public Staff(String staffName, String id) {
        this.staffName = staffName;
        this.id = id;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
