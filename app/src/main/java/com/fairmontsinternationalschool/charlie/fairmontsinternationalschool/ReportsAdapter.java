package com.fairmontsinternationalschool.charlie.fairmontsinternationalschool;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.ReportsViewHolder>{

    private Context mCtx;
    private List<ReportsClass> reportsClassList;

    public ReportsAdapter(Context mCtx, List<ReportsClass> reportsClassList) {
        this.mCtx = mCtx;
        this.reportsClassList = reportsClassList;
    }

    @NonNull
    @Override
    public ReportsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(mCtx);
        View view=layoutInflater.inflate(R.layout.reports_list,null);
        return new ReportsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportsViewHolder holder, int position) {
        final ReportsClass reportsClass=reportsClassList.get(position);
        holder.date.setText(reportsClass.getReport_date());

        holder.select_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String date=reportsClass.getReport_date();
                String notes=reportsClass.getReport();
                String teacher_comments=reportsClass.getTeacher_commment();
                String parent_comment=reportsClass.getParent_comment();

                Intent intent= new Intent(mCtx,ParentComment.class);
                intent.putExtra("date",date);
                intent.putExtra("notes",notes);
                intent.putExtra("teacher_comment",teacher_comments);
                intent.putExtra("parent_comment",parent_comment);
                mCtx.startActivity(intent);
                ((Reports)mCtx).finish();

            }
        });
    }

    @Override
    public int getItemCount() {
        return reportsClassList.size();
    }

    class ReportsViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout select_student;
        TextView date;

        public ReportsViewHolder(View itemView) {
            super(itemView);
            select_student=itemView.findViewById(R.id.select_student_report);
            date=itemView.findViewById(R.id.Student_report_date);
        }
    }
}
