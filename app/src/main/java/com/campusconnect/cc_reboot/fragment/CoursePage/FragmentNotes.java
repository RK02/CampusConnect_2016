package com.campusconnect.cc_reboot.fragment.CoursePage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.campusconnect.cc_reboot.R;
import com.campusconnect.cc_reboot.adapter.CourseListAdapter;
import com.campusconnect.cc_reboot.adapter.NotesListAdapter;

/**
 * Created by RK on 05/06/2016.
 */
public class FragmentNotes extends Fragment {

    RecyclerView notes_list;
    NotesListAdapter mNotesAdapter;
    LinearLayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notes, container, false);

        notes_list = (RecyclerView) v.findViewById (R.id.rv_notes);

        //Setting the recyclerView
        mLayoutManager = new LinearLayoutManager(getActivity());
        mNotesAdapter = new NotesListAdapter(v.getContext());
        notes_list.setLayoutManager(mLayoutManager);
        notes_list.setItemAnimator(new DefaultItemAnimator());
        notes_list.setAdapter(mNotesAdapter);

        return v;
    }
}
