package com.campusconnect.cc_reboot.viewpager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.campusconnect.cc_reboot.fragment.CoursePage.FragmentAssignment;
import com.campusconnect.cc_reboot.fragment.CoursePage.FragmentExam;
import com.campusconnect.cc_reboot.fragment.CoursePage.FragmentNotes;
import com.campusconnect.cc_reboot.fragment.Profile.FragmentBookmarkedNotes;
import com.campusconnect.cc_reboot.fragment.Profile.FragmentUploadedNotes;

/**
 * Created by RK on 05/06/2016.
 */
public class ViewPagerAdapter_profile extends FragmentPagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter_home is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter_home is created

    private Context mContext;
    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter_profile(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb, Context context) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
        this.mContext = context;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        if(position == 0)
        {
            FragmentBookmarkedNotes frag_bookmarked = new FragmentBookmarkedNotes();
            return frag_bookmarked;
        }
        else if(position == 1)
        {
            FragmentUploadedNotes frag_uploads = new FragmentUploadedNotes();
            return frag_uploads;
        }
        else
            return null;

    }



    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }


    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}

