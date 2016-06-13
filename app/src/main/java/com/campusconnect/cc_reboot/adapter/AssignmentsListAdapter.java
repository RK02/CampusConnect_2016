package com.campusconnect.cc_reboot.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.campusconnect.cc_reboot.AssignmentPageActivity;
import com.campusconnect.cc_reboot.CoursePageActivity;
import com.campusconnect.cc_reboot.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by RK on 05/06/2016.
 */
public class AssignmentsListAdapter extends
        RecyclerView.Adapter<AssignmentsListAdapter.AssignmentsListViewHolder> {

    Context context;

    public AssignmentsListAdapter(Context context) {
        this.context = context;

    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public void onBindViewHolder(AssignmentsListViewHolder courseListViewHolder, int i) {
    }

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


            assignment_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_temp = new Intent(v.getContext(), AssignmentPageActivity.class);
                    context.startActivity(intent_temp);
                }
            });

        }
    }
}