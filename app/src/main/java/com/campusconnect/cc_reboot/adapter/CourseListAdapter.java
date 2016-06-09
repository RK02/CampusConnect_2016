package com.campusconnect.cc_reboot.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.campusconnect.cc_reboot.CoursePageActivity;
import com.campusconnect.cc_reboot.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by RK on 05/06/2016.
 */
public class CourseListAdapter extends
        RecyclerView.Adapter<CourseListAdapter.CourseListViewHolder> {

    Context context;

    public CourseListAdapter(Context context) {
        this.context = context;

    }

    @Override
    public int getItemCount() {
        return 6;
    }

    @Override
    public void onBindViewHolder(CourseListViewHolder courseListViewHolder, int i) {
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

        public CourseListViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            course_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_temp = new Intent(v.getContext(), CoursePageActivity.class);
                    context.startActivity(intent_temp);
                }
            });
            notes_count_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_temp = new Intent(v.getContext(), CoursePageActivity.class);
                    intent_temp.putExtra("TAB",0);
                    context.startActivity(intent_temp);
                }
            });
            assignments_count_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_temp = new Intent(v.getContext(), CoursePageActivity.class);
                    intent_temp.putExtra("TAB",1);
                    context.startActivity(intent_temp);
                }
            });
            exams_count_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_temp = new Intent(v.getContext(), CoursePageActivity.class);
                    intent_temp.putExtra("TAB",2);
                    context.startActivity(intent_temp);
                }
            });

        }
    }
}