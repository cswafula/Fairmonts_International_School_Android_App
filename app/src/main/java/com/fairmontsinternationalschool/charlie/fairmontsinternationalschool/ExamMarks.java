package com.fairmontsinternationalschool.charlie.fairmontsinternationalschool;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import java.util.List;

import io.paperdb.Paper;

public class ExamMarks extends AppCompatActivity {

    RecyclerView recyclerView;
    private static String FETCH_URL;
    MarksAdapter adapter;
    List<Marks> marksList;
    TextView no_records;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_marks);

        Intent intent=getIntent();
        String exam_name=intent.getStringExtra("ExamName");
        this.setTitle(exam_name);

        Paper.init(this);
        String admission_no=Paper.book().read("admission_no").toString();

        FETCH_URL="http://fairmontsinternationalschool.co.ke/fairmontsAPI/fetchexammarks.php?admission_no="+admission_no+"&exam_name="+exam_name;

        no_records=findViewById(R.id.Exam_marks_none_found);
        recyclerView=findViewById(R.id.Exam_marks_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        marksList=new ArrayList<>();

        final ProgressDialog progressDialog=new ProgressDialog(ExamMarks.this);
        progressDialog.setMessage("Fetching results.");
        progressDialog.show();

        StringRequest stringRequest=new StringRequest(Request.Method.GET, FETCH_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        try {

                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray jsonArray=jsonObject.getJSONArray("Results");

                            for(int i=0 ; i<jsonArray.length();i++){
                                JSONObject object=jsonArray.getJSONObject(i);
                                marksList.add(new Marks(object.getString("subject"),object.getString("marks")));
                            }

                            adapter=new MarksAdapter(ExamMarks.this,marksList);
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            no_records.setVisibility(View.VISIBLE);
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
}
