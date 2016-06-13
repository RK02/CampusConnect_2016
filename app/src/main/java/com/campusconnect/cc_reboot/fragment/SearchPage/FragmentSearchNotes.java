package com.campusconnect.cc_reboot.fragment.SearchPage;

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
import com.campusconnect.cc_reboot.adapter.SearchCourseListAdapter;
import com.campusconnect.cc_reboot.adapter.SearchNotesListAdapter;

/**
 * Created by RK on 05/06/2016.
 */
public class FragmentSearchNotes extends Fragment {

    RecyclerView course_list;
    SearchNotesListAdapter mSearchNotesAdapter;
    LinearLayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_courses, container, false);

        course_list = (RecyclerView) v.findViewById (R.id.rv_courses);

        //Setting the recyclerView
        mLayoutManager = new LinearLayoutManager(v.getContext());
        mSearchNotesAdapter = new SearchNotesListAdapter(v.getContext());
        course_list.setLayoutManager(mLayoutManager);
        course_list.setItemAnimator(new DefaultItemAnimator());
        course_list.setAdapter(mSearchNotesAdapter);

        return v;
    }
}
