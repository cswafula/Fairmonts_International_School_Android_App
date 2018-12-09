package com.example.charlie.fairmontsinternationalschool;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import io.paperdb.Paper;

public class splashscreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;
    ProgressBar mProgressbar;
    String Parent_id;

    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        prefs = getSharedPreferences("com.example.charlie.fairmontsinternationalschool", MODE_PRIVATE);

        Paper.init(this);

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
                    startActivity(new Intent(splashscreen.this,LoginActivity.class));
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
        if(Parent_id.isEmpty()){
            startActivity(new Intent(splashscreen.this,LoginActivity.class));
            finish();
        }else{
            startActivity(new Intent(splashscreen.this,children_profiles.class));
            finish();
        }
    }
}
