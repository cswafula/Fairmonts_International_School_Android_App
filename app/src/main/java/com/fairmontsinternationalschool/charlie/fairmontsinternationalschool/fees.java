package com.fairmontsinternationalschool.charlie.fairmontsinternationalschool;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class fees extends AppCompatActivity {

    RecyclerView recyclerView;
    feesAdapter adapter;
    List<feesClass> feesClassList;
    TextView balance,total_invoiced, total_paid;

    private static String FETCH_URL;
    public static String BALANCE_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fees);

        balance=findViewById(R.id.Fees_balance);
        total_invoiced=findViewById(R.id.Fees_total_invoiced);
        total_paid=findViewById(R.id.Fees_total_paid);

        Paper.init(this);
        String admission_no;
        admission_no=Paper.book().read("admission_no").toString();
        FETCH_URL="http://fairmontsinternationalschool.co.ke/fairmontsAPI/fetchfeesrecords.php?admission_no="+admission_no;
        BALANCE_URL="http://fairmontsinternationalschool.co.ke/fairmontsAPI/fetchfees.php?admission_no="+admission_no;

        fetchfeebalance();

        feesClassList = new ArrayList<>();
        recyclerView=findViewById(R.id.fees_logs_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Fetching fees records....");
        progressDialog.show();

        StringRequest stringRequest=new StringRequest(Request.Method.GET, FETCH_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray jsonArray=jsonObject.getJSONArray("Transactions");

                            for(int i=0 ; i<jsonArray.length();i++){
                                JSONObject object=jsonArray.getJSONObject(i);
                                feesClassList.add(new feesClass("Date: "+object.getString("date"),"KES "+object.getString("amount")
                                        ,"Receipt#: "+object.getString("receipt_no")));
                            }

                            adapter=new feesAdapter(fees.this,feesClassList);
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_LONG).show();
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

    private void fetchfeebalance() {
        final JsonObjectRequest jsonObjectRequest2= new JsonObjectRequest(Request.Method.GET, BALANCE_URL,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray2= response.getJSONArray("child");
                    String Balance= jsonArray2.get(0).toString();
                    String Total_invoiced= jsonArray2.get(1).toString();
                    String Total_paid= jsonArray2.get(2).toString();
                    balance.setText("KES "+Balance);
                    total_invoiced.setText("KES "+Total_invoiced);
                    total_paid.setText("KES "+Total_paid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
        jsonObjectRequest2.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest2);
    }

}
