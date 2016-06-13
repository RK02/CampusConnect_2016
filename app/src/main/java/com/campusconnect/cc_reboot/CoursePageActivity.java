package com.campusconnect.cc_reboot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.campusconnect.cc_reboot.slidingtab.SlidingTabLayout_home;
import com.campusconnect.cc_reboot.viewpager.ViewPagerAdapter_course;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by RK on 04/06/2016.
 */
public class CoursePageActivity extends AppCompatActivity {

    @Bind(R.id.ib_sort)
    ImageButton sort_button;
    @Bind(R.id.ib_search)
    ImageButton search_button;
    @Bind(R.id.ib_notification)
    ImageButton notification_button;

    @Bind(R.id.tv_course_title)
    TextView course_title;
    @Bind(R.id.tv_course_details)
    TextView course_details;
    @Bind(R.id.tv_timetable)
    TextView course_timetable;
    @Bind(R.id.tv_prof_name)
    TextView course_prof;
    @Bind(R.id.tv_view_students)
    TextView course_view_students;

    ViewPagerAdapter_course course_adapter;
    ViewPager course_pager;
    public static SlidingTabLayout_home course_tabs;
    CharSequence Titles[] = {"Notes", "Assignment", "Exam"};
    int Numboftabs = 3;

    int defaultTabPosition=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        ButterKnife.bind(this);

        defaultTabPosition = getIntent().getIntExtra("TAB",0);

        course_pager = (ViewPager) findViewById(R.id.pager_course);
        course_tabs = (SlidingTabLayout_home) findViewById(R.id.tabs_course);
        course_adapter = new ViewPagerAdapter_course(getSupportFragmentManager(), Titles, Numboftabs, this);
        course_pager.setAdapter(course_adapter);
        course_pager.setCurrentItem(defaultTabPosition);
        course_tabs.setDistributeEvenly(true);
        course_tabs.setViewPager(course_pager);

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_temp = new Intent(v.getContext(), SearchActivity.class);
                startActivity(intent_temp);
            }
        });

    }
}

