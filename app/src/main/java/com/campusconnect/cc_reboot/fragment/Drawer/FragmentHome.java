package com.campusconnect.cc_reboot.fragment.Drawer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.campusconnect.cc_reboot.R;
import com.campusconnect.cc_reboot.slidingtab.SlidingTabLayout_home;
import com.campusconnect.cc_reboot.viewpager.ViewPagerAdapter_home;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by RK on 15/06/2016.
 */
public class FragmentHome extends Fragment implements FloatingActionsMenu.OnFloatingActionsMenuUpdateListener{

    @Bind(R.id.pager_home)
    ViewPager home_pager;

    @Bind(R.id.tabs_home)
    SlidingTabLayout_home home_tabs;

    @Bind(R.id.container_fab)
    FrameLayout fab_menu_container;

    @Bind(R.id.fab_menu)
    FloatingActionsMenu fabMenu;


    ViewPagerAdapter_home home_adapter;
    CharSequence Titles[] = {"Courses", "Timetable"};
    int Numboftabs = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_home,container,false);
        ButterKnife.bind(this,v);

        fab_menu_container.getBackground().setAlpha(0);

        home_adapter = new ViewPagerAdapter_home(getChildFragmentManager(), Titles, Numboftabs, v.getContext());
        home_pager.setAdapter(home_adapter);
        home_pager.setCurrentItem(0);
        home_tabs.setDistributeEvenly(true);
        home_tabs.setViewPager(home_pager);

        //Listener to define layouts for FAB expanded and collapsed
        fabMenu.setOnFloatingActionsMenuUpdateListener(this);

        return v;
    }

    //Layout definition when FAB is expanded
    @Override
    public void onMenuExpanded() {
        fab_menu_container.getBackground().setAlpha(230);
        fab_menu_container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                fabMenu.collapse();
                return true;
            }
        });
    }

    //Layout definition when FAB is collapsed
    @Override
    public void onMenuCollapsed() {
        fab_menu_container.getBackground().setAlpha(0);
        fab_menu_container.setOnTouchListener(null);
    }

}

