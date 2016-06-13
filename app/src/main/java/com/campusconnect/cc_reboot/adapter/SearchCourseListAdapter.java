package com.campusconnect.cc_reboot.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.campusconnect.cc_reboot.CoursePageActivity;
import com.campusconnect.cc_reboot.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by RK on 05/06/2016.
 */
public class SearchCourseListAdapter extends
        RecyclerView.Adapter<SearchCourseListAdapter.SearchNotesListViewHolder> {

    Context context;

    public SearchCourseListAdapter(Context context) {
        this.context = context;

    }

    @Override
    public int getItemCount() {
        return 8;
    }

    @Override
    public void onBindViewHolder(SearchNotesListViewHolder SearchNotesListViewHolder, int i) {
    }

    @Override
    public SearchNotesListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.card_layout_search_course, viewGroup, false);

        return new SearchNotesListViewHolder(itemView);
    }

    public class SearchNotesListViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.search_course_card)
        CardView search_course_card;

        public SearchNotesListViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            search_course_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Intent intent_temp = new Intent(v.getContext(), CoursePageActivity.class);
                    //context.startActivity(intent_temp);
                }
            });

        }
    }
}