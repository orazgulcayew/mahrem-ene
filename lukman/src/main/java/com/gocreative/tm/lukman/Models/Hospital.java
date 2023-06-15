package com.gocreative.tm.lukman.Models;

import java.util.HashMap;
import java.util.List;

public class Hospital {
    private String name;
    private List<HashMap<String, String>> staff;

    public Hospital(){}

    public Hospital(String name, List<HashMap<String, String>> staff) {
        this.name = name;
        this.staff = staff;
    }

    public List<HashMap<String, String>> getStaff() {
        return staff;
    }

    public void setStaff(List<HashMap<String, String>> staff) {
        this.staff = staff;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
