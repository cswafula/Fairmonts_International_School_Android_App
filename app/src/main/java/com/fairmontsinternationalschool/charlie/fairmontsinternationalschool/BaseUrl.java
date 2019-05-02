package com.fairmontsinternationalschool.charlie.fairmontsinternationalschool;

import io.paperdb.Paper;

public class BaseUrl {



    public  static  String ss = Paper.book().read("Main_url").toString();

    public static String returnBase()
    {
        String base = Paper.book().read("Main_url").toString();
        return  base;

    }

    public static String firstloginphp(String phone)
    {
        String url = ss+"api/firstlogin?phone_no="+phone;

        return url;
    }

    public static String addpasswordphp(String phone,String password)
    {
        String url = ss+"api/addpassword?phone_no="+phone+"&password="+password;

        return url;
    }

    public static String fetchsingleprofile(String admission_no)
    {
        String url = ss+"api/fetchsingleprofile/?id="+admission_no;
        return url;
    }

    public static String loginactivity(String phone_no,String password)
    {
        String url = ss+"api/login?phone_no="+phone_no+"&password="+password;

        return  url;
    }

    public static String fetchfeesrecords(String admission_no)
    {
        String url = ss+"api/fetchfeesrecords/?id="+admission_no;

        return url;
    }

    public static String fetchfees(String admission_no){
        String url = ss+"api/fetchfees/?id="+admission_no;
        return url;
    }

    public static String searchreports(String admission_no,String start_date,String end_date){
        String url = ss + "api/searchreports?admission_no=" +admission_no+"&start_date="+start_date+"&end_date="+end_date;
        return  url;
    }


}
