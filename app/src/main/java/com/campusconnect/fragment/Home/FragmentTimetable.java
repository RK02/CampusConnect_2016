package com.campusconnect.fragment.Home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.campusconnect.POJO.SubscribedCourseList;
import com.campusconnect.R;
import com.campusconnect.adapter.TimetableAdapter;
import com.campusconnect.auxiliary.ObservableScrollView;

import java.util.ArrayList;

/**
 * Created by RK on 05/06/2016.
 */
public class FragmentTimetable extends Fragment{

    //@Bind(R.id.rv_timetable)
    static RecyclerView timetable;

    //@Bind(R.id.scroll_horizontal_header)
    static ObservableScrollView header_scroll_horizontal;

    public static TimetableAdapter mTimetableAdapter;
    static LinearLayoutManager mLayoutManager;

    String courseCode[] = {"ECO"};
    String date[] = {"1","2"};
    String startTime[]={"13:00","14:00"};
    String endTime[]={"14:00","15:00"};

    static ArrayList<SubscribedCourseList> subscribedCourseListList;

    public static View v;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_timetable_final, container, false);
        invalidate();
        return v;
    }

    public static void invalidate()
    {
        timetable = (RecyclerView) v.findViewById(R.id.rv_timetable);
        header_scroll_horizontal = (ObservableScrollView) v.findViewById(R.id.scroll_horizontal_header);
        mLayoutManager = new LinearLayoutManager(v.getContext());
        mTimetableAdapter = new TimetableAdapter(v.getContext(),subscribedCourseListList,header_scroll_horizontal);
        timetable.setLayoutManager(mLayoutManager);
        timetable.setItemAnimator(new DefaultItemAnimator());
        timetable.setAdapter(mTimetableAdapter);
    }
}
