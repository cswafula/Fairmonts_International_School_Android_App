package com.fairmontsinternationalschool.charlie.fairmontsinternationalschool;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.ImageView;
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

public class CreateAccount extends AppCompatActivity {

    TextView finish_Account;
    MaterialEditText password;
    private static String FETCH_URL;
    ImageView showPass;
    int setType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        Intent intent=getIntent();
        final String phone_no=intent.getStringExtra("phone_number");

        showPass=findViewById(R.id.AccountShowPassword);
        password=findViewById(R.id.Account_Password);
        finish_Account=findViewById(R.id.Account_finish);

        showPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(setType==1) {
                    setType = 0;
                    password.setTransformationMethod(null);
                    if (password.getText().length() > 0)
                        password.setSelection(password.getText().length());
                }else{
                    setType=1;
                    password.setTransformationMethod(new PasswordTransformationMethod());
                    if(password.getText().length() > 0)
                        password.setSelection(password.getText().length());
                }
            }
        });

        finish_Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass=password.getText().toString().trim();
                if(pass.isEmpty()){
                    password.setError("Enter password first");
                }else if(pass.length() < 6){
                    password.setError("Password is too short");
                }else{
                    FETCH_URL=BaseUrl.addpasswordphp(phone_no,pass);

                            //"https://fairmontsinternationalschool.co.ke/fairmontsAPI/addpassword.php?phone_no="+phone_no+"&password="+pass;
                    execute();
                }
            }
        });
    }

    private void execute() {
        final ProgressDialog progressDialog=new ProgressDialog(CreateAccount.this);
        progressDialog.setMessage("Creating new Password...");
        progressDialog.show();

        final JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, FETCH_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        progressDialog.dismiss();

                        try {
                            JSONArray jsonArray=response.getJSONArray("Update");
                            String Status=jsonArray.get(0).toString();
                            switch (Status){
                                case "Updated":
                                    Toast.makeText(getApplicationContext(),"Password created successfully.",Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(CreateAccount.this,LoginActivity.class));
                                    finish();
                                    break;
                                case "Failed":
                                    Toast.makeText(getApplicationContext(),"Failed to update password. Please contact administrator.",Toast.LENGTH_LONG).show();
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
