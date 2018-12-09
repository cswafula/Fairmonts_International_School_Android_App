package com.example.charlie.fairmontsinternationalschool;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import io.paperdb.Paper;

public class StudentProfile extends AppCompatActivity {

    TextView studentName,system_enrolled,level,fee,gender,studentNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        studentName=findViewById(R.id.Profile_student_Name);
        system_enrolled=findViewById(R.id.Profile_student_system);
        level=findViewById(R.id.Profile_student_level);
        fee=findViewById(R.id.Profile_student_fee_balance);
        gender=findViewById(R.id.Profile_student_gender);
        studentNumber=findViewById(R.id.Profile_student_admission_no);

        Paper.init(this);
        String names=Paper.book().read("names").toString();
        String Ssystem=Paper.book().read("system").toString();
        String Slevel=Paper.book().read("level").toString();
        String Sgender=Paper.book().read("gender").toString();
        String Sfees=Paper.book().read("balance").toString();
        String admission_no=Paper.book().read("admission_no").toString();

        studentName.setText(names);
        system_enrolled.setText(Ssystem);
        level.setText(Slevel);
        fee.setText(Sfees);
        gender.setText(Sgender);
        studentNumber.setText(admission_no);
    }
}
