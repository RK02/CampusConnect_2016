package com.campusconnect;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.campusconnect.slidingtab.SlidingTabLayout_home;
import com.campusconnect.viewpager.ViewPagerAdapter_home;

/**
 * Created by RK on 04/06/2016.
 */
public class HomeActivity extends HomeActivity2 {

    ViewPagerAdapter_home home_adapter;
    public static ViewPager home_pager;
    public static SlidingTabLayout_home home_tabs;
    CharSequence Titles[] = {"Courses", "Timetable"};
    int Numboftabs = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_true);
        home_pager = (ViewPager) findViewById(R.id.pager_home);
        home_tabs = (SlidingTabLayout_home) findViewById(R.id.tabs_home);
        home_adapter = new ViewPagerAdapter_home(getSupportFragmentManager(), Titles, Numboftabs, this);
        home_pager.setAdapter(home_adapter);
        home_pager.setCurrentItem(0);
        home_tabs.setDistributeEvenly(true);
        home_tabs.setViewPager(home_pager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("sw32HOME","resumeactivity");
    }
}

