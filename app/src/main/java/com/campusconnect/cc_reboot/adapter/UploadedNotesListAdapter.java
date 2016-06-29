package com.campusconnect.cc_reboot.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.campusconnect.cc_reboot.NotePageActivity;
import com.campusconnect.cc_reboot.POJO.NoteBookList;
import com.campusconnect.cc_reboot.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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
        notesListViewHolder.note_name.setText(mNotes.get(i).getCourseName());
        notesListViewHolder.note_pages_count.setText(mNotes.get(i).getPages());
        notesListViewHolder.note_views.setText(mNotes.get(i).getViews());
      //  notesListViewHolder.note_description.setText(mNotes.get(i).());
        notesListViewHolder.note_uploader.setText(mNotes.get(i).getUploaderName());
        notesListViewHolder.note_rating.setText(mNotes.get(i).getTotalRating());
        String time = mNotes.get(i).getLastUpdated();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        int days = 0,hours=0,minutes=0,seconds=0;
        try {
            Calendar a = Calendar.getInstance();
            Calendar b = Calendar.getInstance();
            b.setTime(df.parse(time));
            long difference = a.getTimeInMillis() - b.getTimeInMillis();
            days = (int) (difference/ (1000*60*60*24));
            hours = (int) (difference/ (1000*60*60));
            minutes = (int) (difference/ (1000*60));
            seconds = (int) (difference/1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(days==0) {if(hours==0) {if(minutes==0) {if(seconds==0) {notesListViewHolder.last_updated.setText("Just now");}
        else {if(seconds==1) notesListViewHolder.last_updated.setText(seconds + " second ago");
        else notesListViewHolder.last_updated.setText(seconds + " seconds ago");}}
        else {if(minutes==1) notesListViewHolder.last_updated.setText(minutes + " minute ago");
            notesListViewHolder.last_updated.setText(minutes + " minutes ago");}}
        else {if(hours==1)notesListViewHolder.last_updated.setText(hours + " hour ago");
        else notesListViewHolder.last_updated.setText(hours + " hours ago");}}
        else {if(days==1)notesListViewHolder.last_updated.setText(days + " day ago");
        else notesListViewHolder.last_updated.setText(days + " days ago");}
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

        @Nullable
        @Bind(R.id.notes_card)
        CardView notes_card;
        @Nullable
        @Bind(R.id.tv_note_name)
        TextView note_name;
        @Nullable
        @Bind(R.id.tv_uploader)
        TextView note_uploader;
        @Nullable
        @Bind(R.id.tv_date_posted)
        TextView note_posted_on;
        @Nullable
        @Bind(R.id.tv_description)
        TextView note_description;
        @Nullable
        @Bind(R.id.tv_views_count)
        TextView note_views;
        @Bind(R.id.tv_last_updated)
        TextView last_updated;
        @Nullable
        @Bind(R.id.tv_rating)
        TextView note_rating;
        @Nullable
        @Bind(R.id.tv_pages_count)
        TextView note_pages_count;

        public UploadedNotesListViewHolder(View v) {
            super(v);
            ButterKnife.bind(this,v);

            notes_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_temp = new Intent(v.getContext(), NotePageActivity.class);
                    ViewGroup group = (ViewGroup) v.getParent();
                    int index = group.indexOfChild(v);
                    intent_temp.putExtra("noteBookId",mNotes.get(index).getNoteBookId());
                    context.startActivity(intent_temp);
                }
            });

        }
    }
}