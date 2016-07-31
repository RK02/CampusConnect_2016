package com.campusconnect.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.campusconnect.ExamPageActivity;
import com.campusconnect.POJO.ModelTest;
import com.campusconnect.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by RK on 05/06/2016.
 */
public class ExamsListAdapter extends
        RecyclerView.Adapter<ExamsListAdapter.ExamsListViewHolder> {

    Context context;
    ArrayList<ModelTest> mModelTests;
    int courseColor;

    public ExamsListAdapter(Context context, ArrayList<ModelTest> mModelTests, int mCourseColor) {
        this.context = context;
        this.mModelTests = mModelTests;
        this.courseColor = mCourseColor;
    }

    @Override
    public int getItemCount() {
        return mModelTests.size();
    }

    @Override
    public void onBindViewHolder(ExamsListViewHolder examsListViewHolder, final int i) {

        final ModelTest temp = mModelTests.get(i);
        examsListViewHolder.exam_date.setText(temp.getDueDate());
        examsListViewHolder.exam_description.setText(temp.getExamDesc());
        examsListViewHolder.exam_uploader.setText(temp.getUploaderName());
        examsListViewHolder.exam_views.setText(temp.getViews());
        examsListViewHolder.exam_name.setText(temp.getExamTitle());
        String time = temp.getLastUpdated();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        int days = 0,hours=0,minutes=0,seconds=0;
        try {
            Calendar a = Calendar.getInstance();
            Calendar b = Calendar.getInstance();
            b.setTime(df.parse(time));
            long difference = a.getTimeInMillis() - b.getTimeInMillis();
            days = (int) (difference/ (1000*60*60*24));
            hours = (int) (difference/ (1000*60*60));
            minutes = (int) (difference/ (1000*60));
            seconds = (int) (difference/1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        days = Math.abs(days);
        hours = Math.abs(hours);
        minutes = Math.abs(minutes);
        seconds = Math.abs(seconds);
        if(days==0) {if(hours==0) {if(minutes==0) {if(seconds==0) {examsListViewHolder.exam_posted_on.setText("Just now");}
        else {if(seconds==1) examsListViewHolder.exam_posted_on.setText(seconds + " second ago");
        else examsListViewHolder.exam_posted_on.setText(seconds + " seconds ago");}}
        else {if(minutes==1) examsListViewHolder.exam_posted_on.setText(minutes + " minute ago");
        else examsListViewHolder.exam_posted_on.setText(minutes + " minutes ago");}}
        else {if(hours==1)examsListViewHolder.exam_posted_on.setText(hours + " hour ago");
        else examsListViewHolder.exam_posted_on.setText(hours + " hours ago");}}
        else {if(days==1)examsListViewHolder.exam_posted_on.setText(days + " day ago");
        else examsListViewHolder.exam_posted_on.setText(days + " days ago");}
        examsListViewHolder.exam_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_temp = new Intent(v.getContext(), ExamPageActivity.class);
                intent_temp.putExtra("testId",mModelTests.get(i).getExamId());
                intent_temp.putExtra("CourseColor",courseColor);
                context.startActivity(intent_temp);
            }
        });
    }

    @Override
    public ExamsListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.card_layout_exam, viewGroup, false);

        return new ExamsListViewHolder(itemView);
    }
    public void add(ModelTest t){
        mModelTests.add(t);
        notifyDataSetChanged();
    }

    public class ExamsListViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.exam_card)
        CardView exam_card;

        @Bind(R.id.tv_exam_name)
        TextView exam_name;
        @Bind(R.id.tv_uploader)
        TextView exam_uploader;
        @Bind(R.id.tv_date_posted)
        TextView exam_posted_on;
        @Bind(R.id.tv_description)
        TextView exam_description;
        @Bind(R.id.tv_views_count)
        TextView exam_views;
        @Bind(R.id.tv_exam_date)
        TextView exam_date; //Format in which it is to be set: "Date : 14 Sept, 2016"

        public ExamsListViewHolder(View v) {
            super(v);
            ButterKnife.bind(this,v);



        }
    }
}