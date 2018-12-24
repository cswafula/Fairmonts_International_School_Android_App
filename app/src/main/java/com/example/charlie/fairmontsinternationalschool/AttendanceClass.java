package com.example.charlie.fairmontsinternationalschool;

public class AttendanceClass {

    private String teacher,Sdate,Stime,brought_by,uniform_status,items_status,comments;

    public AttendanceClass(String teacher, String sdate, String stime, String brought_by, String uniform_status, String items_status, String comments) {
        this.teacher = teacher;
        Sdate = sdate;
        Stime = stime;
        this.brought_by = brought_by;
        this.uniform_status = uniform_status;
        this.items_status = items_status;
        this.comments = comments;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getSdate() {
        return Sdate;
    }

    public String getStime() {
        return Stime;
    }

    public String getBrought_by() {
        return brought_by;
    }

    public String getUniform_status() {
        return uniform_status;
    }

    public String getItems_status() {
        return items_status;
    }

    public String getComments() {
        return comments;
    }
}
