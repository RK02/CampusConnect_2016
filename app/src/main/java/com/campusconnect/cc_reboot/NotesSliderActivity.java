package com.campusconnect.cc_reboot;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.campusconnect.cc_reboot.auxiliary.DepthPageTransformer;
import com.campusconnect.cc_reboot.auxiliary.ZoomOutPageTransformer;
import com.campusconnect.cc_reboot.viewpager.ScreenSlidePagerAdapter;

/**
 * Created by RK on 04/06/2016.
 */
public class NotesSliderActivity extends AppCompatActivity {

    private ViewPager mNotesPager;
    private PagerAdapter mNotesPagerAdapter;
    CharSequence Titles[] = {"CAT1", "CAT2", "CAT3", "CAT4", "CAT5", "CAT6"};
    int NumPages = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_slider);

        // Instantiate a ViewPager and a PagerAdapter.
        mNotesPager = (ViewPager) findViewById(R.id.pager);
        mNotesPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(),Titles,NumPages,this);
        mNotesPager.setAdapter(mNotesPagerAdapter);

        mNotesPager.setPageTransformer(true, new ZoomOutPageTransformer());
    }

    @Override
    public void onBackPressed() {
        if (mNotesPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mNotesPager.setCurrentItem(mNotesPager.getCurrentItem() - 1);
        }
    }
}

