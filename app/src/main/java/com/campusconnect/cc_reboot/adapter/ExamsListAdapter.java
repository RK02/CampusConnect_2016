package com.campusconnect.cc_reboot.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.campusconnect.cc_reboot.CoursePageActivity;
import com.campusconnect.cc_reboot.ExamPageActivity;
import com.campusconnect.cc_reboot.R;

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

        CardView exam_card;

        public ExamsListViewHolder(View v) {
            super(v);

            exam_card = (CardView) v.findViewById(R.id.exam_card);

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