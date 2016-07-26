package com.campusconnect.cc_reboot.viewpager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.campusconnect.cc_reboot.auxiliary.ViewPagerDisable;
import com.campusconnect.cc_reboot.fragment.NotesSliderPageFragment;

import java.util.ArrayList;

/**
 * Created by RK on 13/06/2016.
 */
public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<String> Titles;
    ArrayList<ArrayList<String>> urls;
    int NumPages;
    private Context mContext;
    ViewPagerDisable parent_pager;

    public ScreenSlidePagerAdapter(FragmentManager fm, ArrayList<String> mTitles,ArrayList<ArrayList<String>> urls, int mNumPages, Context context, ViewPagerDisable parent) {
        super(fm);
        this.Titles = mTitles;
        this.urls = urls;
        this.NumPages = mNumPages;
        this.mContext = context;
        this.parent_pager = parent;
    }

    @Override
    public Fragment getItem(int position) {
        NotesSliderPageFragment page_fragment = new NotesSliderPageFragment();
        Bundle fragBundle = new Bundle();
        fragBundle.putString("PageTitle",Titles.get(position).toString());
        fragBundle.putInt("PagePos",position);
        page_fragment.setArguments(fragBundle);
        return page_fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles.get(position);
    }

    @Override
    public int getCount() {
        return NumPages;
    }
}

