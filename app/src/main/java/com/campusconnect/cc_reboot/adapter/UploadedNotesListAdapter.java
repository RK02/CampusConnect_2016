package com.campusconnect.cc_reboot.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.campusconnect.cc_reboot.NotePageActivity;
import com.campusconnect.cc_reboot.POJO.NoteBookList;
import com.campusconnect.cc_reboot.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by RK on 05/06/2016.
 */
public class UploadedNotesListAdapter extends
        RecyclerView.Adapter<UploadedNotesListAdapter.UploadedNotesListViewHolder> {

    Context context;
    ArrayList<NoteBookList> mNotes;
    public UploadedNotesListAdapter(Context context, ArrayList<NoteBookList> mNotes) {
        this.context = context;
        this.mNotes = mNotes;

    }
    public void add(NoteBookList noteBookList)
    {
        mNotes.add(noteBookList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    @Override
    public void onBindViewHolder(UploadedNotesListViewHolder notesListViewHolder, int i) {
//        notesListViewHolder.note_name.setText(mNotes.get(i).getCourseName());
//        notesListViewHolder.note_pages_count.setText(mNotes.get(i).getPages());
//        notesListViewHolder.note_views.setText(mNotes.get(i).getViews());
//        notesListViewHolder.note_description.setText(mNotes.get(i).getCourseName());
//        notesListViewHolder.note_uploader.setText(mNotes.get(i).getUploaderName());
//        notesListViewHolder.note_rating.setText(mNotes.get(i).getTotalRating());
//        notesListViewHolder.note_posted_on.setText(mNotes.get(i).getLastUpdated());
    }

    public String getNoteBookId(String noteBookName)
    {
        for(NoteBookList s : mNotes)
        {
            if(s.getCourseName().equalsIgnoreCase(noteBookName))
                return s.getNoteBookId();
        }
        return null;
    }

    @Override
    public UploadedNotesListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.card_layout_notes, viewGroup, false);

        return new UploadedNotesListViewHolder(itemView);
    }

    public class UploadedNotesListViewHolder extends RecyclerView.ViewHolder {

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

        public UploadedNotesListViewHolder(View v) {
            super(v);
            ButterKnife.bind(this,v);

            notes_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_temp = new Intent(v.getContext(), NotePageActivity.class);
                    intent_temp.putExtra("noteBookId",getNoteBookId(note_name.getText().toString()));
                    context.startActivity(intent_temp);
                }
            });

        }
    }
}