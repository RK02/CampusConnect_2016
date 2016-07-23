package com.campusconnect.fragment.Drawer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.campusconnect.R;
import com.campusconnect.slidingtab.SlidingTabLayout_home;
import com.campusconnect.viewpager.ViewPagerAdapter_home;

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

