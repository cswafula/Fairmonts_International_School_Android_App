package com.fairmontsinternationalschool.charlie.fairmontsinternationalschool;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
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

public class splashscreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;
    ProgressBar mProgressbar;
    String Parent_id;

    private static  String LOGIN_URL = "http://www.fairmontsinternationalschool.co.ke/fairmontsAPI/url.php";

    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        prefs = getSharedPreferences("com.example.charlie.fairmontsinternationalschool", MODE_PRIVATE);

        Paper.init(this);

        getmainurl();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (prefs.getBoolean("firstrun", true)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    String empty="";
                    Paper.book().write("Parent_id",empty);
                    startActivity(new Intent(splashscreen.this,Introduction.class));
                    finish();

                }
            },SPLASH_TIME_OUT);
            prefs.edit().putBoolean("firstrun", false).apply();
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkSession();
                }
            },SPLASH_TIME_OUT);
        }
    }

    private void checkSession() {
        Parent_id=Paper.book().read("Parent_id").toString();
        if(true ){ //Parent_id.isEmpty()
            startActivity(new Intent(splashscreen.this,LoginActivity.class));
            finish();
        }else{
            startActivity(new Intent(splashscreen.this,children_profiles.class));
            finish();
        }
    }

    private void getmainurl() {

        final ProgressDialog progressDialog=new ProgressDialog(splashscreen.this);
        progressDialog.setMessage("Logging in...");
        progressDialog.show();

        final JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET, LOGIN_URL,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                try{
                    JSONArray jsonArray=response.getJSONArray("User");
                    String parent_id=jsonArray.get(0).toString();
                    Paper.book().write("Main_url",parent_id);
                    Toast.makeText(getApplicationContext(),parent_id,Toast.LENGTH_LONG).show(); //"Login Successful!"
                    //startActivity(new Intent(LoginActivity.this,children_profiles.class));
                    finish();
                }catch(JSONException e){
                    Toast.makeText(getApplicationContext(),"Login Failed! Try again",Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}
