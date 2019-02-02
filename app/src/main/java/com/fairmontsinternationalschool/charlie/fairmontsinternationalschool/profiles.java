package com.fairmontsinternationalschool.charlie.fairmontsinternationalschool;

public class profiles {

    private String childname, admissionNo,level,systems;

    public profiles(String childname, String admissionNo, String level, String systems) {
        this.childname = childname;
        this.admissionNo = admissionNo;
        this.level = level;
        this.systems = systems;
    }

    public String getChildname() {
        return childname;
    }

    public String getAdmissionNo() {
        return admissionNo;
    }

    public String getLevel() {
        return level;
    }

    public String getSystems() {
        return systems;
    }
}
