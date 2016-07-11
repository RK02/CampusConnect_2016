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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by RK on 05/06/2016.
 */
public class SearchNotesListAdapter extends
        RecyclerView.Adapter<SearchNotesListAdapter.SearchCourseListViewHolder> {

    Context context;
    ArrayList<NoteBookList> mNotes;
    public SearchNotesListAdapter(Context context,ArrayList<NoteBookList> noteBookLists) {
        this.context = context;
        this.mNotes = noteBookLists;

    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    @Override
    public void onBindViewHolder(SearchCourseListViewHolder searchCourseListViewHolder, final int i) {
        NoteBookList aa = mNotes.get(i);
        searchCourseListViewHolder.note_name.setText(aa.getCourseName());
        searchCourseListViewHolder.note_pages_count.setText(aa.getPages());
        searchCourseListViewHolder.note_views.setText(aa.getViews());
        searchCourseListViewHolder.note_uploader.setText(aa.getUploaderName());
        searchCourseListViewHolder.note_rating.setText(aa.getTotalRating());
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
        if(days==0) {if(hours==0) {if(minutes==0) {if(seconds==0) {searchCourseListViewHolder.last_updated.setText("Just now");}
        else {if(seconds==1) searchCourseListViewHolder.last_updated.setText(seconds + " second ago");
        else searchCourseListViewHolder.last_updated.setText(seconds + " seconds ago");}}
        else {if(minutes==1) searchCourseListViewHolder.last_updated.setText(minutes + " minute ago");
            searchCourseListViewHolder.last_updated.setText(minutes + " minutes ago");}}
        else {if(hours==1)searchCourseListViewHolder.last_updated.setText(hours + " hour ago");
        else searchCourseListViewHolder.last_updated.setText(hours + " hours ago");}}
        else {if(days==1)searchCourseListViewHolder.last_updated.setText(days + " day ago");
        else searchCourseListViewHolder.last_updated.setText(days + " days ago");}


        searchCourseListViewHolder.search_notes_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_temp = new Intent(v.getContext(), NotePageActivity.class);
                intent_temp.putExtra("noteBookId",mNotes.get(i).getNoteBookId());
                context.startActivity(intent_temp);
            }
        });


    }

    @Override
    public SearchCourseListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.card_layout_search_note, viewGroup, false);

        return new SearchCourseListViewHolder(itemView);
    }
    public void clear()
    {
        mNotes.clear();
        notifyDataSetChanged();
    }

    public class SearchCourseListViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.search_notes_card)
        CardView search_notes_card;
        @Bind(R.id.tv_note_name)
        TextView note_name;
        @Bind(R.id.tv_date_posted)
        TextView last_updated;
        @Bind(R.id.tv_uploader)
        TextView note_uploader;
        @Bind(R.id.tv_views_count)
        TextView note_views;
        @Bind(R.id.tv_rating)
        TextView note_rating;
        @Bind(R.id.tv_pages_count)
        TextView note_pages_count;


        public SearchCourseListViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);



        }
    }
}