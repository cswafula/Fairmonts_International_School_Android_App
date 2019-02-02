package com.fairmontsinternationalschool.charlie.fairmontsinternationalschool;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MarksAdapter extends RecyclerView.Adapter<MarksAdapter.MarksViewHolder>{

    private Context mCtx;
    private List<Marks> marksList;

    public MarksAdapter(Context mCtx, List<Marks> marksList) {
        this.mCtx = mCtx;
        this.marksList = marksList;
    }

    @NonNull
    @Override
    public MarksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(mCtx);
        View view=layoutInflater.inflate(R.layout.exam_marks_list,null);
        return new MarksAdapter.MarksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MarksViewHolder holder, int position) {
        final Marks marks=marksList.get(position);
        holder.Subject.setText(marks.getSubject());
        holder.TextMarks.setText(marks.getMarks());
    }

    @Override
    public int getItemCount() {
        return marksList.size();
    }

    class MarksViewHolder extends RecyclerView.ViewHolder{

        TextView Subject,TextMarks;

        public MarksViewHolder(View itemView) {
            super(itemView);
            Subject=itemView.findViewById(R.id.Exam_marks_subject);
            TextMarks=itemView.findViewById(R.id.Exam_marks_marks);
        }
    }
}
