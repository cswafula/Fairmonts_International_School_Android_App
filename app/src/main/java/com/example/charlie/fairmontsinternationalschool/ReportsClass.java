package com.example.charlie.fairmontsinternationalschool;

public class ReportsClass {

    private String report_date,report;

    public ReportsClass(String report_date, String report) {
        this.report_date = report_date;
        this.report = report;
    }

    public String getReport_date() {
        return report_date;
    }

    public String getReport() {
        return report;
    }
}
