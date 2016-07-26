package com.campusconnect.viewpager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.campusconnect.fragment.CoursePage.FragmentAssignment;
import com.campusconnect.fragment.CoursePage.FragmentExam;
import com.campusconnect.fragment.CoursePage.FragmentNotes;

/**
 * Created by RK on 05/06/2016.
 */
public class ViewPagerAdapter_course extends FragmentPagerAdapter {

    int courseColor;
    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter_home is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter_home is created

    private Context mContext;
    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter_course(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb, int mCourseColor, Context context) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
        this.courseColor = mCourseColor;
        this.mContext = context;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        Bundle bundle = new Bundle();
        bundle.putInt("CourseColor",courseColor);

        if(position == 0)
        {
            FragmentNotes frag_notes = new FragmentNotes();
            frag_notes.setArguments(bundle);
            return frag_notes;
        }
        else if(position == 1)
        {
            FragmentAssignment frag_assignment = new FragmentAssignment();
            frag_assignment.setArguments(bundle);
            return frag_assignment;
        }
        else{
            FragmentExam frag_exam = new FragmentExam();
            frag_exam.setArguments(bundle);
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

