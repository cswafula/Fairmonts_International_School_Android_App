package com.fairmontsinternationalschool.charlie.fairmontsinternationalschool;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
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
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Coursework extends AppCompatActivity {

    TextView no_records;
    MaterialSpinner year,term;
    String fetch_year="2018";
    String fetch_term="TERM ONE";
    Button filter;
    RecyclerView coursework_recycler;
    private static String FETCH_URL;
    ExamsAdapter adapter;
    List<Exams> examsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coursework);

        year=findViewById(R.id.Coursework_year);
        term=findViewById(R.id.Coursework_term);
        no_records=findViewById(R.id.Coursework_no_exams_found);


        year.setItems("2018","2019","2020");
        term.setItems("TERM ONE","TERM TWO","TERM THREE");

        year.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                fetch_year=item.toString();
            }
        });

        term.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                fetch_term=item.toString();
            }
        });

        filter=findViewById(R.id.Coursework_filter);
        examsList=new ArrayList<>();
        coursework_recycler=findViewById(R.id.Coursework_recyclerview);
        coursework_recycler.setHasFixedSize(true);
        coursework_recycler.setLayoutManager(new LinearLayoutManager(this));

        fetch();


        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                no_records.setVisibility(View.GONE);
                coursework_recycler.setVisibility(View.GONE);
                fetch();
            }
        });


    }

    private void fetch() {
        examsList.clear();
        FETCH_URL="http://fairmontsinternationalschool.co.ke/fairmontsAPI/fetchexams.php?year="+fetch_year+"&term="+fetch_term;

        final ProgressDialog progressDialog=new ProgressDialog(Coursework.this);
        progressDialog.setMessage("Fetching exams.");
        progressDialog.show();

        StringRequest stringRequest=new StringRequest(Request.Method.GET, FETCH_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        no_records.setVisibility(View.GONE);
                        coursework_recycler.setVisibility(View.VISIBLE);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray jsonArray=jsonObject.getJSONArray("Exams");

                            for(int i=0 ; i<jsonArray.length();i++){
                                JSONObject object=jsonArray.getJSONObject(i);
                                examsList.add(new Exams(object.getString("exam_name")));
                            }

                            adapter=new ExamsAdapter(Coursework.this,examsList);
                            coursework_recycler.setAdapter(adapter);


                        } catch (JSONException e) {
                            no_records.setVisibility(View.VISIBLE);
                            coursework_recycler.setVisibility(View.GONE);
                            examsList.clear();
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                coursework_recycler.setVisibility(View.GONE);
                examsList.clear();

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
}
