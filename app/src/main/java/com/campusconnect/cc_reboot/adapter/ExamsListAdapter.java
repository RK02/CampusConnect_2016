package com.campusconnect.cc_reboot.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.campusconnect.cc_reboot.ExamPageActivity;
import com.campusconnect.cc_reboot.POJO.ModelTest;
import com.campusconnect.cc_reboot.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by RK on 05/06/2016.
 */
public class ExamsListAdapter extends
        RecyclerView.Adapter<ExamsListAdapter.ExamsListViewHolder> {

    Context context;
    ArrayList<ModelTest> mModelTests;

    public ExamsListAdapter(Context context, ArrayList<ModelTest> mModelTests) {
        this.context = context;
        this.mModelTests = mModelTests;

    }

    @Override
    public int getItemCount() {
        return mModelTests.size();
    }

    @Override
    public void onBindViewHolder(ExamsListViewHolder examsListViewHolder, int i) {

        ModelTest temp = mModelTests.get(i);
        examsListViewHolder.exam_date.setText(temp.getDueDate());
        examsListViewHolder.exam_description.setText(temp.getTestDesc());
        examsListViewHolder.exam_posted_on.setText(temp.getLastUpdated());
        examsListViewHolder.exam_uploader.setText(temp.getUploaderName());
        examsListViewHolder.exam_views.setText(temp.getViews());
        examsListViewHolder.exam_name.setText(temp.getCourseName());
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

            exam_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_temp = new Intent(v.getContext(), ExamPageActivity.class);
                    ViewGroup temp = (ViewGroup) v.getParent();
                    String testId = mModelTests.get(temp.indexOfChild(v)).getTestId();
                    intent_temp.putExtra("testId",testId);
                    context.startActivity(intent_temp);
                }
            });

        }
    }
}