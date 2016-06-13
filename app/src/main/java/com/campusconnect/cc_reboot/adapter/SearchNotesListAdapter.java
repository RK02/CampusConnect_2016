package com.campusconnect.cc_reboot.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.campusconnect.cc_reboot.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by RK on 05/06/2016.
 */
public class SearchNotesListAdapter extends
        RecyclerView.Adapter<SearchNotesListAdapter.SearchCourseListViewHolder> {

    Context context;

    public SearchNotesListAdapter(Context context) {
        this.context = context;

    }

    @Override
    public int getItemCount() {
        return 8;
    }

    @Override
    public void onBindViewHolder(SearchCourseListViewHolder SearchCourseListViewHolder, int i) {
    }

    @Override
    public SearchCourseListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.card_layout_search_note, viewGroup, false);

        return new SearchCourseListViewHolder(itemView);
    }

    public class SearchCourseListViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.search_notes_card)
        CardView search_notes_card;

        public SearchCourseListViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            search_notes_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Intent intent_temp = new Intent(v.getContext(), CoursePageActivity.class);
                    //context.startActivity(intent_temp);
                }
            });

        }
    }
}