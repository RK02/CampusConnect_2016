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
import android.widget.ImageView;

import com.campusconnect.cc_reboot.POJO.CourseList;
import com.campusconnect.cc_reboot.POJO.SubscribedCourseList;
import com.campusconnect.cc_reboot.R;
import com.campusconnect.cc_reboot.SearchActivity;
import com.campusconnect.cc_reboot.adapter.CourseListAdapter;
import com.campusconnect.cc_reboot.adapter.SearchCourseListAdapter;

import java.util.ArrayList;

/**
 * Created by RK on 05/06/2016.
 */
public class FragmentSearchCourse extends Fragment {

    ImageView no_course;
    RecyclerView course_list;
    public static SearchCourseListAdapter mCourseAdapter;
    LinearLayoutManager mLayoutManager;
    public static ArrayList<String> courseNames;
    public static ArrayList<String> courseIds;
    public static ArrayList<CourseList> courses = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_courses, container, false);
        no_course = (ImageView) v.findViewById (R.id.iv_no_course);
        course_list = (RecyclerView) v.findViewById (R.id.rv_courses);
        no_course.setVisibility(View.GONE);
        //Setting the recyclerView
        courseNames = new ArrayList<>();
        courseIds = new ArrayList<>();
        courses = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(v.getContext());
        mCourseAdapter = new SearchCourseListAdapter(v.getContext(),courses);
        course_list.setLayoutManager(mLayoutManager);
        course_list.setItemAnimator(new DefaultItemAnimator());
        course_list.setAdapter(mCourseAdapter);
        return v;
    }

}
