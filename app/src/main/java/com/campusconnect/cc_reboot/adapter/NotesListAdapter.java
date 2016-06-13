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
import com.campusconnect.cc_reboot.NotePageActivity;
import com.campusconnect.cc_reboot.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by RK on 05/06/2016.
 */
public class NotesListAdapter extends
        RecyclerView.Adapter<NotesListAdapter.NotesListViewHolder> {

    Context context;

    public NotesListAdapter(Context context) {
        this.context = context;

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    @Override
    public void onBindViewHolder(NotesListViewHolder courseListViewHolder, int i) {
    }

    @Override
    public NotesListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.card_layout_notes, viewGroup, false);

        return new NotesListViewHolder(itemView);
    }

    public class NotesListViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.notes_card)
        CardView notes_card;

        @Bind(R.id.tv_note_name)
        TextView note_name;
        @Bind(R.id.tv_uploader)
        TextView note_uploader;
        @Bind(R.id.tv_date_posted)
        TextView note_posted_on;
        @Bind(R.id.tv_description)
        TextView note_description;
        @Bind(R.id.tv_views_count)
        TextView note_views;
        @Bind(R.id.tv_rating)
        TextView note_rating;
        @Bind(R.id.tv_pages_count)
        TextView note_pages_count;

        public NotesListViewHolder(View v) {
            super(v);
            ButterKnife.bind(this,v);

            notes_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_temp = new Intent(v.getContext(), NotePageActivity.class);
                    context.startActivity(intent_temp);
                }
            });

        }
    }
}