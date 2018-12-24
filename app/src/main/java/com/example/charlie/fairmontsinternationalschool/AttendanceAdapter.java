package com.example.charlie.fairmontsinternationalschool;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder>{

    private Context mCtx;
    private List<AttendanceClass> attendanceClassList;

    public AttendanceAdapter(Context mCtx, List<AttendanceClass> attendanceClassList) {
        this.mCtx = mCtx;
        this.attendanceClassList = attendanceClassList;
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(mCtx);
        View view=layoutInflater.inflate(R.layout.attendance_list,null);
        return new AttendanceAdapter.AttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, int position) {
        final AttendanceClass attendanceClass=attendanceClassList.get(position);
        holder.date.setText(attendanceClass.getSdate());
        holder.time.setText(attendanceClass.getStime());
        holder.select_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(mCtx);
                builder.setTitle("Attendance details");
                builder.setIcon(R.drawable.fairmontslogo);
                builder.setMessage("Teacher: "+attendanceClass.getTeacher()+
                        "\n\n"+attendanceClass.getSdate()
                        +"\n\n"+attendanceClass.getStime()
                        +"\n\nBrought by: \n"+attendanceClass.getBrought_by()
                        +"\n\nUniform/ Sanitation status: \n"+attendanceClass.getUniform_status()
                        +"\n\nPersonal Items status: \n"+attendanceClass.getItems_status()
                        +"\n\nComments: \n"+attendanceClass.getComments());
                AlertDialog alertDialog= builder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return attendanceClassList.size();
    }


    class AttendanceViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout select_student;
        TextView date,time;

        public AttendanceViewHolder(View itemView) {
            super(itemView);

            select_student=itemView.findViewById(R.id.select_student_attendence);
            date=itemView.findViewById(R.id.Student_attendance_date);
            time=itemView.findViewById(R.id.Student_attendance_time);
        }
    }

}
