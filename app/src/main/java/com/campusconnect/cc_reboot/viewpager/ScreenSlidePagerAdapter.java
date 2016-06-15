package com.campusconnect.cc_reboot.viewpager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.campusconnect.cc_reboot.fragment.NotesSliderPageFragment;

/**
 * Created by RK on 13/06/2016.
 */
public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[];
    int NumPages;
    private Context mContext;

    public ScreenSlidePagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumPages, Context context) {
        super(fm);
        this.Titles = mTitles;
        this.NumPages = mNumPages;
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        NotesSliderPageFragment page_fragment = new NotesSliderPageFragment();
        Bundle fragBundle = new Bundle();
        fragBundle.putString("PageTitle",Titles[position].toString());
        fragBundle.putInt("PagePos",position);
        page_fragment.setArguments(fragBundle);
        return page_fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    @Override
    public int getCount() {
        return NumPages;
    }
}

