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
import com.campusconnect.cc_reboot.adapter.AssignmentsListAdapter;
import com.campusconnect.cc_reboot.adapter.ExamsListAdapter;

/**
 * Created by RK on 05/06/2016.
 */
public class FragmentExam extends Fragment {

    RecyclerView exams_list;
    ExamsListAdapter mExamsAdapter;
    LinearLayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_exam, container, false);

        exams_list = (RecyclerView) v.findViewById (R.id.rv_exams);

        //Setting the recyclerView
        mLayoutManager = new LinearLayoutManager(getActivity());
        mExamsAdapter = new ExamsListAdapter(v.getContext());
        exams_list.setLayoutManager(mLayoutManager);
        exams_list.setItemAnimator(new DefaultItemAnimator());
        exams_list.setAdapter(mExamsAdapter);


        return v;
    }
}
