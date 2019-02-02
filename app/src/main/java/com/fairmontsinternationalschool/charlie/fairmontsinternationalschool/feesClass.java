package com.fairmontsinternationalschool.charlie.fairmontsinternationalschool;

public class feesClass {
    private String date,amount,reciept_no;

    public feesClass(String date, String amount, String reciept_no) {
        this.date = date;
        this.amount = amount;
        this.reciept_no = reciept_no;
    }

    public String getDate() {
        return date;
    }

    public String getAmount() {
        return amount;
    }

    public String getReciept_no() {
        return reciept_no;
    }
}
