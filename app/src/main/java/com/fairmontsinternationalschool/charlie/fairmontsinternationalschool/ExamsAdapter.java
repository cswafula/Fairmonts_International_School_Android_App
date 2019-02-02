package com.fairmontsinternationalschool.charlie.fairmontsinternationalschool;

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

public class ExamsAdapter extends RecyclerView.Adapter<ExamsAdapter.ExamsViewHolder> {

    private Context mCtx;
    private List<Exams> examsList;

    public ExamsAdapter(Context mCtx, List<Exams> examsList) {
        this.mCtx = mCtx;
        this.examsList = examsList;
    }

    @NonNull
    @Override
    public ExamsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(mCtx);
        View view=inflater.inflate(R.layout.exams_list,null);
        return new ExamsAdapter.ExamsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamsViewHolder holder, int position) {

        final Exams exams=examsList.get(position);
        holder.exam_name.setText(exams.getExam_name());
        holder.exam_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String examName=exams.getExam_name();
                Intent intent=new Intent(mCtx,ExamMarks.class);
                intent.putExtra("ExamName",examName);
                mCtx.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return examsList.size();
    }

    class ExamsViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout exam_layout;
        TextView exam_name;

        public ExamsViewHolder(View itemView) {
            super(itemView);
            exam_layout=itemView.findViewById(R.id.Exams_List_layout);
            exam_name=itemView.findViewById(R.id.Exams_List_Exam_name);
        }
    }
}
