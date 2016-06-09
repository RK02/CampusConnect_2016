package com.campusconnect.cc_reboot.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.campusconnect.cc_reboot.CoursePageActivity;
import com.campusconnect.cc_reboot.R;

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

        CardView assignment_card;

        public AssignmentsListViewHolder(View v) {
            super(v);

            assignment_card = (CardView) v.findViewById(R.id.assignment_card);

            assignment_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_temp = new Intent(v.getContext(), CoursePageActivity.class);
                    context.startActivity(intent_temp);
                }
            });

        }
    }
}