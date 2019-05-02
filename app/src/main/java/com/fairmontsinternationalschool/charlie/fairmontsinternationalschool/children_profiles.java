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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class children_profiles extends AppCompatActivity {
    RecyclerView recyclerView;
    profileAdapter adapter;
    List<profiles> profilesList;

    private static String FETCH_URL;

    TextView records;
    Button refresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children_profiles);

        records=findViewById(R.id.Profiles_no_records_found);
        refresh=findViewById(R.id.Profiles_refresh_list);

        records.setVisibility(View.GONE);
        refresh.setVisibility(View.GONE);

        Paper.init(this);
        String Parent_id= Paper.book().read("Parent_id").toString();
        String baseurl = Paper.book().read("Main_url").toString();
        FETCH_URL=BaseUrl.returnBase()+"api/fetchprofiles/"+Parent_id;
                //"http://fairmontsinternationalschool.co.ke/fairmontsAPI/fetchprofiles.php?parent_id="+Parent_id;




        profilesList= new ArrayList<>();
        recyclerView=findViewById(R.id.profiles_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadProfiles();

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                records.setVisibility(View.GONE);
                refresh.setVisibility(View.GONE);
                loadProfiles();
            }
        });



    }

    private void loadProfiles() {

        final ProgressDialog progressDialog=new ProgressDialog(children_profiles.this);
        progressDialog.setMessage("Loading Children Profiles....");
        progressDialog.show();

        StringRequest stringRequest=new StringRequest(Request.Method.GET, FETCH_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try{
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray jsonArray=jsonObject.getJSONArray("Student");

                            for(int i=0 ; i<jsonArray.length();i++){
                                JSONObject object=jsonArray.getJSONObject(i);
                                profilesList.add(new profiles(object.getString("Name"),object.getString("Admno")
                                        ,object.getString("CName"),object.getString("CName")));

                                //[names ,admission_no, level ,system] order of elements added
                            }

                            adapter=new profileAdapter(children_profiles.this,profilesList);
                            recyclerView.setAdapter(adapter);


                        }catch (JSONException e){
                            Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                records.setVisibility(View.VISIBLE);
                refresh.setVisibility(View.VISIBLE);

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
