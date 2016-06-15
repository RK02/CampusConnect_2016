package com.campusconnect.cc_reboot.fragment.Drawer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.campusconnect.cc_reboot.R;
import com.campusconnect.cc_reboot.slidingtab.SlidingTabLayout_home;
import com.campusconnect.cc_reboot.viewpager.ViewPagerAdapter_home;

/**
 * Created by RK on 15/06/2016.
 */
public class FragmentHome extends Fragment {

    ViewPagerAdapter_home home_adapter;
    ViewPager home_pager;
    public static SlidingTabLayout_home home_tabs;
    CharSequence Titles[] = {"Courses", "Timetable"};
    int Numboftabs = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_home,container,false);

        home_pager = (ViewPager) v.findViewById(R.id.pager_home);
        home_tabs = (SlidingTabLayout_home) v.findViewById(R.id.tabs_home);
        home_adapter = new ViewPagerAdapter_home(getChildFragmentManager(), Titles, Numboftabs, v.getContext());
        home_pager.setAdapter(home_adapter);
        home_pager.setCurrentItem(0);
        home_tabs.setDistributeEvenly(true);
        home_tabs.setViewPager(home_pager);

        return v;
    }
}

