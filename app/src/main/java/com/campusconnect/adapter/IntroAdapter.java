package com.campusconnect.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.campusconnect.fragment.FragmentIntro;

/**
 * Created by RK on 18/07/2016.
 */
public class IntroAdapter extends FragmentPagerAdapter {

    public IntroAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return FragmentIntro.newInstance(position);
            case 1:
                return FragmentIntro.newInstance(position);
            case 2:
                return FragmentIntro.newInstance(position);
            default:
                return FragmentIntro.newInstance(position);
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

}
