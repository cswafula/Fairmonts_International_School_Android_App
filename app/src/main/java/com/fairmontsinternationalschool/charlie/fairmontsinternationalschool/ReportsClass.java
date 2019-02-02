package com.fairmontsinternationalschool.charlie.fairmontsinternationalschool;

public class ReportsClass {

    private String report_date,report,teacher_commment,parent_comment;

    public ReportsClass(String report_date, String report, String teacher_commment, String parent_comment) {
        this.report_date = report_date;
        this.report = report;
        this.teacher_commment = teacher_commment;
        this.parent_comment = parent_comment;
    }

    public String getReport_date() {
        return report_date;
    }

    public String getReport() {
        return report;
    }

    public String getTeacher_commment() {
        return teacher_commment;
    }

    public String getParent_comment() {
        return parent_comment;
    }
}
