package com.fairmontsinternationalschool.charlie.fairmontsinternationalschool;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;

import com.jaredrummler.materialspinner.MaterialSpinner;

public class timetables extends AppCompatActivity {

    MaterialSpinner spinner;
    String timetable;
    TableLayout British,Kenyan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetables);

        spinner= findViewById(R.id.SystemSpinner);
        British=findViewById(R.id.BritishTimetable);
        Kenyan=findViewById(R.id.KenyanTimetable);
        spinner.setItems("British System Timetable","Kenyan System Timetable");

        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                timetable=item.toString();
                switch (timetable){
                    case "British System Timetable":
                        Kenyan.setVisibility(View.GONE);
                        British.setVisibility(View.VISIBLE);
                        break;
                    case "Kenyan System Timetable":
                        British.setVisibility(View.GONE);
                        Kenyan.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

    }
}
