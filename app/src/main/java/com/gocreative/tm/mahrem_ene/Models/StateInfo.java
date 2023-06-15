package com.gocreative.tm.mahrem_ene.Models;

public class StateInfo {
    private String child_birthdate, pregnancy_date;
    private boolean has_child, is_child_age_greater_than_one, is_pregnant;


    // Empty constructor for firestore
    public StateInfo(){}

    public String getChild_birthdate() {
        return child_birthdate;
    }

    public String getPregnancy_date() {
        return pregnancy_date;
    }

    public boolean isHas_child() {
        return has_child;
    }

    public boolean isIs_child_age_greater_than_one() {
        return is_child_age_greater_than_one;
    }

    public boolean isIs_pregnant() {
        return is_pregnant;
    }
}
