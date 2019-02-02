package com.fairmontsinternationalschool.charlie.fairmontsinternationalschool;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.util.Calendar;

import io.paperdb.Paper;

public class ParentComment extends AppCompatActivity {

    Button save;
    TextView text_date,notes,teachers_coments;
    EditText comments;
    String admission_no;
    private static String FETCH_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_comment);


        save=findViewById(R.id.Comment_save);
        text_date=findViewById(R.id.Comment_date_text);
        notes=findViewById(R.id.Comment_notes_text);
        teachers_coments=findViewById(R.id.Comment_teacher_text);
        comments=findViewById(R.id.Comment_text);


        Intent intent=getIntent();
        String date=intent.getStringExtra("date");
        String Diary_notes=intent.getStringExtra("notes");
        String Tcomment=intent.getStringExtra("teacher_comment");
        String Pcomment=intent.getStringExtra("parent_comment");

        text_date.setText(date);
        notes.setText(Diary_notes);
        teachers_coments.setText(Tcomment);
        comments.setText(Pcomment);

        comments.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                save.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                save.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                save.setVisibility(View.VISIBLE);
            }
        });

        Paper.init(this);
        admission_no=Paper.book().read("admission_no").toString();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dated=text_date.getText().toString().trim();
                String Comment=comments.getText().toString().trim();
                if(Comment.isEmpty()){
                    Snackbar.make(v,"Write a comment first!!",Snackbar.LENGTH_LONG).show();
                }else{
                    FETCH_URL="http://fairmontsinternationalschool.co.ke/fairmontsAPI/addcomment.php?admission_no="+admission_no
                            +"&date="+dated+"&comment="+Comment;
                    execute();
                    }
            }
        });
    }

    private void execute() {

        final ProgressDialog progressDialog=new ProgressDialog(ParentComment.this);
        progressDialog.setMessage("Saving...");
        progressDialog.show();

        final JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, FETCH_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();

                        try {
                            JSONArray jsonArray=response.getJSONArray("Comment");
                            String Status=jsonArray.get(0).toString();

                            switch (Status){
                                case"Updated":
                                    Toast.makeText(getApplicationContext(),"Comment Saved!",Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(ParentComment.this,Reports.class));
                                    finish();
                                    break;
                                case"Failed":
                                    Toast.makeText(getApplicationContext(),"Operation Failed, make sure the selected date has a report first.",Toast.LENGTH_LONG).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ParentComment.this,Reports.class));
        finish();
    }
}
