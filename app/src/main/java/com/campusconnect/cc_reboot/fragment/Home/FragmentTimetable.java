package com.campusconnect.cc_reboot.fragment.Home;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.campusconnect.cc_reboot.POJO.SubscribedCourseList;
import com.campusconnect.cc_reboot.R;
import com.campusconnect.cc_reboot.adapter.CourseListAdapter;
import com.campusconnect.cc_reboot.adapter.TimetableAdapter;
import com.campusconnect.cc_reboot.auxiliary.ObservableScrollView;
import com.campusconnect.cc_reboot.auxiliary.ScrollViewListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

import butterknife.Bind;
import butterknife.ButterKnife;

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
