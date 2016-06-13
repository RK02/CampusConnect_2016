package com.campusconnect.cc_reboot;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.campusconnect.cc_reboot.slidingtab.SlidingTabLayout_home;
import com.campusconnect.cc_reboot.viewpager.ViewPagerAdapter_home;
import com.campusconnect.cc_reboot.viewpager.ViewPagerAdapter_search;

/**
 * Created by RK on 04/06/2016.
 */
public class SearchActivity extends AppCompatActivity {

    ViewPagerAdapter_search search_adapter;
    ViewPager search_pager;
    public static SlidingTabLayout_home search_tabs;
    CharSequence Titles[] = {"Courses", "Notes"};
    int Numboftabs = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search_pager = (ViewPager) findViewById(R.id.pager_home);
        search_tabs = (SlidingTabLayout_home) findViewById(R.id.tabs_home);
        search_adapter = new ViewPagerAdapter_search(getSupportFragmentManager(), Titles, Numboftabs, this);
        search_pager.setAdapter(search_adapter);
        search_pager.setCurrentItem(0);
        search_tabs.setDistributeEvenly(true);
        search_tabs.setViewPager(search_pager);

    }
}

