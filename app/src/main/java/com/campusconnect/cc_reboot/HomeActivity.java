package com.campusconnect.cc_reboot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.campusconnect.cc_reboot.slidingtab.SlidingTabLayout_home;
import com.campusconnect.cc_reboot.viewpager.ViewPagerAdapter_home;

/**
 * Created by RK on 04/06/2016.
 */
public class HomeActivity extends AppCompatActivity {

    ViewPagerAdapter_home home_adapter;
    ViewPager home_pager;
    public static SlidingTabLayout_home home_tabs;
    CharSequence Titles[] = {"Courses", "Timetable"};
    int Numboftabs = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        home_pager = (ViewPager) findViewById(R.id.pager_home);
        home_tabs = (SlidingTabLayout_home) findViewById(R.id.tabs_home);
        home_adapter = new ViewPagerAdapter_home(getSupportFragmentManager(), Titles, Numboftabs, this);
        home_pager.setAdapter(home_adapter);
        home_pager.setCurrentItem(0);
        home_tabs.setDistributeEvenly(true);
        home_tabs.setViewPager(home_pager);
    }
}

