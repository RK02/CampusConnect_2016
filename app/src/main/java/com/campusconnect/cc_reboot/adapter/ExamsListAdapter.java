package com.campusconnect.cc_reboot.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.campusconnect.cc_reboot.CoursePageActivity;
import com.campusconnect.cc_reboot.ExamPageActivity;
import com.campusconnect.cc_reboot.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by RK on 05/06/2016.
 */
public class ExamsListAdapter extends
        RecyclerView.Adapter<ExamsListAdapter.ExamsListViewHolder> {

    Context context;

    public ExamsListAdapter(Context context) {
        this.context = context;

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public void onBindViewHolder(ExamsListViewHolder courseListViewHolder, int i) {
    }

    @Override
    public ExamsListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.card_layout_exam, viewGroup, false);

        return new ExamsListViewHolder(itemView);
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

            exam_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_temp = new Intent(v.getContext(), ExamPageActivity.class);
                    context.startActivity(intent_temp);
                }
            });

        }
    }
}