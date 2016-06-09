package com.campusconnect.cc_reboot.viewpager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.campusconnect.cc_reboot.fragment.CoursePage.FragmentAssignment;
import com.campusconnect.cc_reboot.fragment.CoursePage.FragmentExam;
import com.campusconnect.cc_reboot.fragment.CoursePage.FragmentNotes;

/**
 * Created by RK on 05/06/2016.
 */
public class ViewPagerAdapter_course extends FragmentPagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter_home is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter_home is created

    private Context mContext;
    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter_course(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb, Context context) {
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
            FragmentNotes frag_notes = new FragmentNotes();
            return frag_notes;
        }
        else if(position == 1)
        {
            FragmentAssignment frag_assignment = new FragmentAssignment();
            return frag_assignment;
        }
        else{
            FragmentExam frag_exam = new FragmentExam();
            return frag_exam;
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

