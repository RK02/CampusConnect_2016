package com.campusconnect.viewpager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.campusconnect.fragment.SearchPage.FragmentSearchCourse;
import com.campusconnect.fragment.SearchPage.FragmentSearchNotes;

/**
 * Created by hp1 on 21-01-2015.
 */
public class ViewPagerAdapter_search extends FragmentPagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter_home is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter_home is created

    private Context mContext;
    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter_search(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb, Context context) {
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
           FragmentSearchCourse frag_search_courses = new FragmentSearchCourse();
           return frag_search_courses;
        }
        else
        {
            FragmentSearchNotes frag_search_notes = new FragmentSearchNotes();
            return frag_search_notes;
        }

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
