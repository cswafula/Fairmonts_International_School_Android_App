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
                AlertDialog.Builder builder= new AlertDialog.Builder(mCtx);
                builder.setTitle("Student Report");
                builder.setIcon(R.drawable.fairmontslogo);
                builder.setMessage(reportsClass.getReport());
                AlertDialog alertDialog= builder.create();
                alertDialog.show();
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
