package com.fairmontsinternationalschool.charlie.fairmontsinternationalschool;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CreatePassword extends AppCompatActivity {

    MaterialEditText phoneNumber;
    TextView next;
    private static String FETCH_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);

        phoneNumber= findViewById(R.id.Create_Phone_number);
        next=findViewById(R.id.Create_next);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phone=phoneNumber.getText().toString().trim();
                if(phone.isEmpty()){
                    phoneNumber.setError("Phone Number is Required!!");
                }else if(phone.length() < 10){
                    phoneNumber.setError("Enter a valid number! (Hint:0712345678)");
                }else if(phone.length() > 10){
                    phoneNumber.setError("Enter a valid number! (Hint:0712345678)");
                }else{
                    FETCH_URL="http://fairmontsinternationalschool.co.ke/fairmontsAPI/firstlogin.php?phone_no="+phone;

                    final ProgressDialog progressDialog=new ProgressDialog(CreatePassword.this);
                    progressDialog.setMessage("Just a moment...");
                    progressDialog.show();

                    final JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, FETCH_URL, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    progressDialog.dismiss();

                                    try {
                                        JSONArray jsonArray=response.getJSONArray("Check");
                                        String Status=jsonArray.get(0).toString();
                                        switch (Status){
                                            case "Already Exists":
                                                Toast.makeText(getApplicationContext(),"This Account has already been setup.",Toast.LENGTH_LONG).show();
                                                break;
                                            case "New User":
                                                Intent intent=new Intent(CreatePassword.this,CreateAccount.class);
                                                intent.putExtra("phone_number",phone);
                                                startActivity(intent);
                                                finish();
                                                break;
                                            case "Failed":
                                                Toast.makeText(getApplicationContext(),"Phone Number entered is not registered. Please contact administration.",Toast.LENGTH_LONG).show();
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
            }
        });
    }
}
