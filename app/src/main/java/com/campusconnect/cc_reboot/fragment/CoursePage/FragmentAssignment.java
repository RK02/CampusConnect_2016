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

import com.campusconnect.cc_reboot.CoursePageActivity;
import com.campusconnect.cc_reboot.R;
import com.campusconnect.cc_reboot.adapter.AssignmentsListAdapter;

/**
 * Created by RK on 05/06/2016.
 */
public class FragmentAssignment extends Fragment {
    RecyclerView assignments_list;
    AssignmentsListAdapter mAssignmentsAdapter;
    LinearLayoutManager mLayoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_assignment, container, false);

        assignments_list = (RecyclerView) v.findViewById (R.id.rv_assignments);

        //Setting the recyclerView
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAssignmentsAdapter = new AssignmentsListAdapter(v.getContext());
        assignments_list.setLayoutManager(mLayoutManager);
        assignments_list.setItemAnimator(new DefaultItemAnimator());
        assignments_list.setAdapter(mAssignmentsAdapter);

        return v;
    }
}
