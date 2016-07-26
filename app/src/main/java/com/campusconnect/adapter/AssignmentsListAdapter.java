package com.campusconnect.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.campusconnect.AssignmentPageActivity;
import com.campusconnect.POJO.AssList;
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
public class AssignmentsListAdapter extends
        RecyclerView.Adapter<AssignmentsListAdapter.AssignmentsListViewHolder> {

    Context context;
    ArrayList<AssList> mAssignments;
    int courseColor;

    public AssignmentsListAdapter(Context context,ArrayList<AssList> mAssignments, int mCourseColor) {
        this.context = context;
        this.mAssignments = mAssignments;
        this.courseColor = mCourseColor;
    }

    @Override
    public int getItemCount() {
        return mAssignments.size();
    }

    public void add(AssList a)
    {
        mAssignments.add(a);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(AssignmentsListViewHolder assignmentsListViewHolder,int i) {
        final AssList assList = mAssignments.get(i);
        assignmentsListViewHolder.assignment_name.setText(assList.getCourseName());
        assignmentsListViewHolder.assignment_description.setText(assList.getAssignmentDesc());
        assignmentsListViewHolder.assignment_due_date.setText(assList.getDueDate());
        assignmentsListViewHolder.assignment_posted_on.setText(assList.getLastUpdated().substring(0,10));
        assignmentsListViewHolder.assignment_views.setText(assList.getViews());
        assignmentsListViewHolder.assignment_uploader.setText(assList.getUploaderName());
        String time = assList.getLastUpdated();
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
        if(days==0) {if(hours==0) {if(minutes==0) {if(seconds==0) {assignmentsListViewHolder.assignment_posted_on.setText("Just now");}
        else {if(seconds==1) assignmentsListViewHolder.assignment_posted_on.setText(seconds + " second ago");
        else assignmentsListViewHolder.assignment_posted_on.setText(seconds + " seconds ago");}}
        else {if(minutes==1) assignmentsListViewHolder.assignment_posted_on.setText(minutes + " minute ago");
        else assignmentsListViewHolder.assignment_posted_on.setText(minutes + " minutes ago");}}
        else {if(hours==1)assignmentsListViewHolder.assignment_posted_on.setText(hours + " hour ago");
        else assignmentsListViewHolder.assignment_posted_on.setText(hours + " hours ago");}}
        else {if(days==1)assignmentsListViewHolder.assignment_posted_on.setText(days + " day ago");
        else assignmentsListViewHolder.assignment_posted_on.setText(days + " days ago");}
        assignmentsListViewHolder.assignment_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_temp = new Intent(v.getContext(), AssignmentPageActivity.class);
                intent_temp.putExtra("assignmentId",assList.getAssignmentId());
                context.startActivity(intent_temp);
            }
        });
    }
    //public void getAssignmentId(String )

    @Override
    public AssignmentsListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.card_layout_assignment, viewGroup, false);

        return new AssignmentsListViewHolder(itemView);
    }

    public class AssignmentsListViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.assignment_card)
        CardView assignment_card;

        @Bind(R.id.tv_assignment_name)
        TextView assignment_name;
        @Bind(R.id.tv_uploader)
        TextView assignment_uploader;
        @Bind(R.id.tv_date_posted)
        TextView assignment_posted_on;
        @Bind(R.id.tv_description)
        TextView assignment_description;
        @Bind(R.id.tv_views_count)
        TextView assignment_views;
        @Bind(R.id.tv_due_date)
        TextView assignment_due_date; //Format in which it is to be set: "Due by 25 Sept, 2016"

        public AssignmentsListViewHolder(View v) {
            super(v);
            ButterKnife.bind(this,v);




        }
    }
}