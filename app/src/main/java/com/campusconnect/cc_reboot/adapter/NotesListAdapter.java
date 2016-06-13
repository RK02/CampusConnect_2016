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

        CardView notes_card;

        public NotesListViewHolder(View v) {
            super(v);

            notes_card = (CardView) v.findViewById(R.id.notes_card);

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