package com.campusconnect.cc_reboot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.campusconnect.cc_reboot.POJO.ModelCoursePage;
import com.campusconnect.cc_reboot.POJO.MyApi;
import com.campusconnect.cc_reboot.fragment.Home.FragmentCourses;
import com.campusconnect.cc_reboot.slidingtab.SlidingTabLayout_home;
import com.campusconnect.cc_reboot.viewpager.ViewPagerAdapter_course;
import com.campusconnect.cc_reboot.viewpager.ViewPagerAdapter_profile;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by RK on 04/06/2016.
 */
public class ProfilePageActivity extends AppCompatActivity {

    ViewPagerAdapter_profile profile_adapter;
    ViewPager profile_pager;
    public static SlidingTabLayout_home profile_tabs;
    CharSequence Titles[] = {"Bookmarked", "Uploads"};
    int Numboftabs = 2;

    int defaultTabPosition=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        profile_pager = (ViewPager) findViewById(R.id.pager_course);
        profile_tabs = (SlidingTabLayout_home) findViewById(R.id.tabs_course);
        profile_adapter = new ViewPagerAdapter_profile(getSupportFragmentManager(), Titles, Numboftabs, this);
        profile_pager.setAdapter(profile_adapter);
        profile_pager.setCurrentItem(defaultTabPosition);
        profile_tabs.setDistributeEvenly(true);
        profile_tabs.setViewPager(profile_pager);

    }
}

