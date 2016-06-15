package com.campusconnect.cc_reboot.fragment.Profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.campusconnect.cc_reboot.POJO.NoteBookList;
import com.campusconnect.cc_reboot.R;
import com.campusconnect.cc_reboot.adapter.BookmarkedNotesListAdapter;

import java.util.ArrayList;

/**
 * Created by RK on 05/06/2016.
 */
public class FragmentBookmarkedNotes extends Fragment {

    RecyclerView boomarked_notes_list;
    BookmarkedNotesListAdapter mBookmarkedNotesAdapter;
    LinearLayoutManager mLayoutManager;
    ArrayList<NoteBookList> mNotes;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notes, container, false);

        boomarked_notes_list = (RecyclerView) v.findViewById (R.id.rv_notes);
        mNotes = new ArrayList<>();

        //Setting the recyclerView
        mLayoutManager = new LinearLayoutManager(getActivity());

        mBookmarkedNotesAdapter = new BookmarkedNotesListAdapter(v.getContext(),mNotes);
        boomarked_notes_list.setLayoutManager(mLayoutManager);
        boomarked_notes_list.setItemAnimator(new DefaultItemAnimator());
        boomarked_notes_list.setAdapter(mBookmarkedNotesAdapter);



        return v;
    }
}
