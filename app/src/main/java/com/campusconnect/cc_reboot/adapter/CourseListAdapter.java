package com.campusconnect.cc_reboot.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.campusconnect.cc_reboot.CoursePageActivity;
import com.campusconnect.cc_reboot.POJO.SubscribedCourseList;
import com.campusconnect.cc_reboot.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by RK on 05/06/2016.
 */
public class CourseListAdapter extends
        RecyclerView.Adapter<CourseListAdapter.CourseListViewHolder> {

    Context context;
    int card_bk_color[] = {
            Color.rgb(156,204,101),
            Color.rgb(229,115,115)
    };
    private ArrayList<SubscribedCourseList> mCourses;


    public CourseListAdapter(Context context, ArrayList<SubscribedCourseList> courses) {
        this.context = context;
        this.mCourses = courses;
    }

    public void add(SubscribedCourseList v)
    {
        mCourses.add(v);
        notifyDataSetChanged();
    }
    public String getCourseId(String courseName)
    {
        for(SubscribedCourseList s : mCourses)
        {
            if(s.getCourseName().equalsIgnoreCase(courseName))
                return s.getCourseId();
        }
        return null;
    }
    public SubscribedCourseList getItem(int position)
    {
        return mCourses.get(position);
    }
    public SubscribedCourseList getItem(SubscribedCourseList subscribedCourseList)
    {
        return mCourses.get(mCourses.indexOf(subscribedCourseList));
    }

    @Override
    public int getItemCount() {
        return mCourses.size();
    }

    @Override
    public void onBindViewHolder(CourseListViewHolder courseListViewHolder, int i) {
        courseListViewHolder.notes_unseen_count.setText(mCourses.get(i).getRecentNotes());
        courseListViewHolder.exams_count.setText(mCourses.get(i).getDueTests());
        courseListViewHolder.assignments_count.setText(mCourses.get(i).getDueAssignments());
        courseListViewHolder.course_title.setText(mCourses.get(i).getCourseName());

        //If more than two cards are sent from the server, please change this ASAP.
        courseListViewHolder.course_card.setBackgroundColor(card_bk_color[i]);

    }



    @Override
    public CourseListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
              R.layout.card_layout_course, viewGroup, false);
        return new CourseListViewHolder(itemView);
    }

    public class CourseListViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.course_card)
        CardView course_card;
        @Bind(R.id.container_notes_count)
        RelativeLayout notes_count_container;
        @Bind(R.id.container_assignments_count)
        RelativeLayout assignments_count_container;
        @Bind(R.id.container_exams_count)
        RelativeLayout exams_count_container;
        @Bind(R.id.tv_course_title)
        TextView course_title;
        @Bind(R.id.tv_unseen_notes_count)
        TextView notes_unseen_count;
        @Bind(R.id.tv_assignments_count)
        TextView assignments_count;
        @Bind(R.id.tv_exams_count)
        TextView exams_count;

        public CourseListViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            course_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_temp = new Intent(v.getContext(), CoursePageActivity.class);
                    intent_temp.putExtra("courseId",getCourseId(((TextView)v.findViewById(R.id.tv_course_title)).getText().toString()));
                    intent_temp.putExtra("courseColor",card_bk_color[getAdapterPosition()]);
                    ViewGroup viewGroup = (ViewGroup) v.getParent();
                    viewGroup.indexOfChild(v);

                    context.startActivity(intent_temp);
                }
            });
            notes_count_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_temp = new Intent(v.getContext(), CoursePageActivity.class);
                    intent_temp.putExtra("TAB",0);
                    ViewGroup viewGroup = (ViewGroup) course_card.getParent();
                    String id = mCourses.get(viewGroup.indexOfChild(course_card)).getCourseId();
                    intent_temp.putExtra("courseId",id);
                    intent_temp.putExtra("courseColor",card_bk_color[getAdapterPosition()]);

                    context.startActivity(intent_temp);
                }
            });
            assignments_count_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_temp = new Intent(v.getContext(), CoursePageActivity.class);
                    intent_temp.putExtra("TAB",1);
                    ViewGroup viewGroup = (ViewGroup) course_card.getParent();
                    String id = mCourses.get(viewGroup.indexOfChild(course_card)).getCourseId();
                    intent_temp.putExtra("courseId",id);
                    intent_temp.putExtra("courseColor",card_bk_color[getAdapterPosition()]);

                    context.startActivity(intent_temp);
                }
            });
            exams_count_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_temp = new Intent(v.getContext(), CoursePageActivity.class);
                    intent_temp.putExtra("TAB",2);
                    ViewGroup viewGroup = (ViewGroup) course_card.getParent();
                    String id = mCourses.get(viewGroup.indexOfChild(course_card)).getCourseId();
                    intent_temp.putExtra("courseId",id);
                    intent_temp.putExtra("courseColor",card_bk_color[getAdapterPosition()]);

                    context.startActivity(intent_temp);
                }
            });

        }
    }
}