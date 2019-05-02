package com.fairmontsinternationalschool.charlie.fairmontsinternationalschool;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.paperdb.Paper;

public class Reports extends AppCompatActivity {

    RecyclerView diaryRecycler,diaryRecycler2;
    ReportsAdapter adapter;
    ReportsAdapter adapter2;
    List<ReportsClass> reportsClassList;
    List<ReportsClass> reportsClassList2;
    private static String FETCH_URL;
    private static String SEARCH_URL;
    LinearLayout searchlayout;
    TextView previous_reports,close,from_date,to_date,no_reports;
    Button from_button,to_button,filter;
    String admission_no;
    Calendar cal;
    DatePickerDialog datedialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        Paper.init(this);
        admission_no=Paper.book().read("admission_no").toString();
        FETCH_URL= BaseUrl.returnBase()+"api/fetchreports/?id="+admission_no;
                //"http://fairmontsinternationalschool.co.ke/fairmontsAPI/fetchreports.php?admission_no="+admission_no;
        searchlayout=findViewById(R.id.Diary_search_layout);
        previous_reports=findViewById(R.id.Diary_previous_reports);
        close=findViewById(R.id.Diary_search_close);
        from_date=findViewById(R.id.Diary_Start_date_text);
        to_date=findViewById(R.id.Diary_End_date_text);
        from_button=findViewById(R.id.Diary_Start_date_button);
        to_button=findViewById(R.id.Diary_End_date_button);
        no_reports=findViewById(R.id.Diary_no_reports);
        diaryRecycler=findViewById(R.id.Diary_Recyclerview);
        filter=findViewById(R.id.Diary_filter_dates);

        reportsClassList= new ArrayList<>();
        reportsClassList2= new ArrayList<>();
        diaryRecycler.setHasFixedSize(true);
        diaryRecycler.setLayoutManager(new LinearLayoutManager(this));

        diaryRecycler2=findViewById(R.id.Diary_Recyclerview_all_search);
        diaryRecycler2.setHasFixedSize(true);
        diaryRecycler2.setLayoutManager(new LinearLayoutManager(this));

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String start_date=from_date.getText().toString().trim();
                String end_date=to_date.getText().toString().trim();
                if(start_date.equals("0000-00-00")){
                    Snackbar.make(v,"Select both dates first!!",Snackbar.LENGTH_LONG).show();
                }else if(end_date.equals("0000-00-00")){
                    Snackbar.make(v,"Select both dates first!!",Snackbar.LENGTH_LONG).show();
                }else{
                    filter();
                }
            }
        });

        from_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal=Calendar.getInstance();
                int day=cal.get(Calendar.DAY_OF_MONTH);
                int month= cal.get(Calendar.MONTH);
                int year=cal.get(Calendar.YEAR);

                datedialog=new DatePickerDialog(Reports.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                from_date.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                            }
                        },day,month,year);
                datedialog.show();
            }
        });
        to_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal=Calendar.getInstance();
                int day=cal.get(Calendar.DAY_OF_MONTH);
                int month= cal.get(Calendar.MONTH);
                int year=cal.get(Calendar.YEAR);

                datedialog=new DatePickerDialog(Reports.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                to_date.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                            }
                        },day,month,year);
                datedialog.show();
            }
        });

        previous_reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchlayout.setVisibility(View.VISIBLE);
                diaryRecycler.setVisibility(View.GONE);
                diaryRecycler2.setVisibility(View.VISIBLE);
                previous_reports.setVisibility(View.GONE);
                no_reports.setVisibility(View.GONE);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchlayout.setVisibility(View.GONE);
                diaryRecycler.setVisibility(View.VISIBLE);
                diaryRecycler2.setVisibility(View.GONE);
                previous_reports.setVisibility(View.VISIBLE);
                no_reports.setVisibility(View.GONE);
            }
        });


        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Fetching Reports");
        progressDialog.show();

        StringRequest stringRequest=new StringRequest(Request.Method.GET, FETCH_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        no_reports.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray jsonArray=jsonObject.getJSONArray("Report");

                            for(int i=0 ; i<jsonArray.length();i++){
                                JSONObject object=jsonArray.getJSONObject(i);
                                reportsClassList.add(new ReportsClass(object.getString("Date")
                                        ,object.getString("DayEntry"),object.getString("TeacherComment"),object.getString("ParenyComment")));
                            }

                            adapter=new ReportsAdapter(Reports.this,reportsClassList);
                            diaryRecycler.setAdapter(adapter);

                        } catch (JSONException e) {
                            no_reports.setVisibility(View.VISIBLE);
                            reportsClassList.clear();
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
                    message = "Parsing error! Please try again after some time!!";
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

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void filter() {
        diaryRecycler.setVisibility(View.GONE);
        diaryRecycler2.setVisibility(View.VISIBLE);

        final ProgressDialog progressDialog=new ProgressDialog(Reports.this);
        progressDialog.setMessage("Fetching Reports");
        progressDialog.show();

        String start_date=from_date.getText().toString().trim();
        String end_date=to_date.getText().toString().trim();

        SEARCH_URL=BaseUrl.searchreports(admission_no,start_date,end_date);

                //BaseUrl.returnBase()+"api/searchreports?admission_no="+admission_no+"&start_date="+start_date+"&end_date="+end_date;

                //"http://fairmontsinternationalschool.co.ke/fairmontsAPI/searchreports.php?admission_no="+admission_no+"&start_date="+start_date+"&end_date="+end_date;

        StringRequest stringRequest2=new StringRequest(Request.Method.GET, SEARCH_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        no_reports.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject2=new JSONObject(response);
                            JSONArray jsonArray2=jsonObject2.getJSONArray("Report");

                            for(int i=0 ; i<jsonArray2.length();i++){
                                JSONObject object=jsonArray2.getJSONObject(i);
                                reportsClassList2.add(new ReportsClass(object.getString("Date"),object.getString("DayEntry")
                                        ,object.getString("TeacherComment"),object.getString("ParenyComment")));
                            }

                            adapter2=new ReportsAdapter(Reports.this,reportsClassList2);
                            diaryRecycler2.setAdapter(adapter2);

                        } catch (JSONException e) {
                            reportsClassList2.clear();
                            no_reports.setVisibility(View.VISIBLE);
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
                    message = "Parsing error! Please try again after some time!!";
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

        RequestQueue requestQueue2= Volley.newRequestQueue(this);
        requestQueue2.add(stringRequest2);

    }
}
