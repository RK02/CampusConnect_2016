package com.campusconnect.cc_reboot.fragment.Drawer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.campusconnect.cc_reboot.R;
import com.campusconnect.cc_reboot.slidingtab.SlidingTabLayout_home;
import com.campusconnect.cc_reboot.viewpager.ViewPagerAdapter_home;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by RK on 15/06/2016.
 */
public class FragmentHome extends Fragment{

    public static ViewPager home_pager;

    @Bind(R.id.tabs_home)
    SlidingTabLayout_home home_tabs;

    public static ViewPagerAdapter_home home_adapter;
    CharSequence Titles[] = {"Courses", "Timetable"};
    int Numboftabs = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_home,container,false);

        ButterKnife.bind(this,v);
        home_pager = (ViewPager) v.findViewById(R.id.pager_home);
        //Setting the ViewPager adapter
        home_adapter = new ViewPagerAdapter_home(getChildFragmentManager(), Titles, Numboftabs, v.getContext());
        //Binding the ViewPager and the Adapter
        home_pager.setAdapter(home_adapter);

        //Custom Pager and Tab settings
        home_pager.setCurrentItem(0);
        home_tabs.setDistributeEvenly(true);
        home_tabs.setViewPager(home_pager);

        return v;
    }


}

