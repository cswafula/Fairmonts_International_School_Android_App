package com.example.charlie.fairmontsinternationalschool;

public class profiles {

    private String childname, admissionNo,fees_id,level,systems;

    public profiles(String childname, String admissionNo, String fees_id, String level, String systems) {
        this.childname = childname;
        this.admissionNo = admissionNo;
        this.fees_id = fees_id;
        this.level = level;
        this.systems = systems;
    }

    public String getChildname() {
        return childname;
    }

    public String getAdmissionNo() {
        return admissionNo;
    }

    public String getFees_id() {
        return fees_id;
    }

    public String getLevel() {
        return level;
    }

    public String getSystems() {
        return systems;
    }
}
