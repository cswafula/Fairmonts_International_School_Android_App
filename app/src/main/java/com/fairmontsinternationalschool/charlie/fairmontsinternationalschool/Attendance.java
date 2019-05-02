package com.fairmontsinternationalschool.charlie.fairmontsinternationalschool;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.paperdb.Paper;

public class Attendance extends AppCompatActivity {

    private static String FETCH_URL;
    CalendarView calendar;
    TextView teacher,dates,times,broughtby,uniformstatus,personalitems,comment,attendance_absent,
    close,select_date,sign_out_teacher,sign_out_time,taken_by,sign_out_uniform,sign_out_personalitems,sign_out_comments;
    LinearLayout attendance;
    String admission_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        Paper.init(this);
        admission_no=Paper.book().read("admission_no").toString();
        sign_out_teacher=findViewById(R.id.attendance_teacher_details_sign_out);
        sign_out_time=findViewById(R.id.attendance_sign_out_time);
        taken_by=findViewById(R.id.attendance_brought_by_sign_out);
        sign_out_uniform=findViewById(R.id.attendance_uniform_status_sign_out);
        sign_out_personalitems=findViewById(R.id.attendance_personal_items_status_sign_out);
        sign_out_comments=findViewById(R.id.attendance_comments_sign_out);
        calendar=findViewById(R.id.attendance_calendar);
        teacher=findViewById(R.id.attendance_teacher_details);
        dates=findViewById(R.id.attendance_date_recorded);
        times=findViewById(R.id.attendance_sign_in_time);
        broughtby=findViewById(R.id.attendance_brought_by);
        uniformstatus=findViewById(R.id.attendance_uniform_status);
        personalitems=findViewById(R.id.attendance_personal_items_status);
        comment=findViewById(R.id.attendance_comments);
        attendance=findViewById(R.id.attendance_details_layout);
        attendance_absent=findViewById(R.id.attendance_absent);
        close=findViewById(R.id.Attendance_close);
        select_date=findViewById(R.id.Attendance_select_date_label);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close.setVisibility(View.GONE);
                select_date.setVisibility(View.VISIBLE);
                calendar.setVisibility(View.VISIBLE);
                attendance.setVisibility(View.GONE);
                attendance_absent.setVisibility(View.GONE);
            }
        });



        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                attendance.setVisibility(View.GONE);
                attendance_absent.setVisibility(View.GONE);

                final ProgressDialog progressDialog=new ProgressDialog(Attendance.this);
                progressDialog.setMessage("Fetching records...");
                progressDialog.show();

                final String date= Integer.toString(year)+"-"+Integer.toString(month+1)+"-"+Integer.toString(dayOfMonth);
                FETCH_URL=BaseUrl.returnBase()+"/api/fetchattendance?admission_no="+admission_no+"&date="+date;
                        //"https://fairmontsinternationalschool.co.ke/fairmontsAPI/fetchattendance.php?admission_no="+admission_no+"&date="+date;

                final JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, FETCH_URL, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                progressDialog.dismiss();

                                try {
                                    JSONArray jsonArray=response.getJSONArray("Attendance");

                                    String Status=jsonArray.get(0).toString();
                                    switch (Status){
                                        case "Present":
                                            teacher.setText(jsonArray.get(1).toString());
                                            dates.setText(jsonArray.get(2).toString());
                                            times.setText(jsonArray.get(3).toString());
                                            broughtby.setText(jsonArray.get(4).toString());
                                            uniformstatus.setText(jsonArray.get(5).toString());
                                            personalitems.setText(jsonArray.get(6).toString());
                                            comment.setText(jsonArray.get(7).toString());
                                            sign_out_teacher.setText(jsonArray.get(8).toString());
                                            sign_out_time.setText(jsonArray.get(9).toString());
                                            taken_by.setText(jsonArray.get(10).toString());
                                            sign_out_uniform.setText(jsonArray.get(11).toString());
                                            sign_out_personalitems.setText(jsonArray.get(12).toString());
                                            sign_out_comments.setText(jsonArray.get(13).toString());

                                            close.setVisibility(View.VISIBLE);
                                            select_date.setVisibility(View.GONE);
                                            calendar.setVisibility(View.GONE);
                                            attendance.setVisibility(View.VISIBLE);
                                            attendance_absent.setVisibility(View.GONE);
                                            break;
                                        case "Absent":
                                            attendance.setVisibility(View.GONE);
                                            attendance_absent.setVisibility(View.VISIBLE);
                                            break;
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressDialog.dismiss();

                        String message = null;
                        if (error instanceof NetworkError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof ServerError) {
                            message = "The server could not be found. Please try again after some time!!";
                        } else if (error instanceof AuthFailureError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof ParseError) {
                            message = "Invalid Credentials! Please try again!!";
                        } else if (error instanceof NoConnectionError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof TimeoutError) {
                            message = "Connection TimeOut! Please check your internet connection.";
                        }else{
                            Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                        }
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();

                    }
                });

                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                        5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(jsonObjectRequest);
            }
        });

    }
}
