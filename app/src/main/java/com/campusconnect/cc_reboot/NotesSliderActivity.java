package com.campusconnect.cc_reboot;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.campusconnect.cc_reboot.POJO.Note;
import com.campusconnect.cc_reboot.auxiliary.DepthPageTransformer;
import com.campusconnect.cc_reboot.auxiliary.ZoomOutPageTransformer;
import com.campusconnect.cc_reboot.viewpager.ScreenSlidePagerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RK on 04/06/2016.
 */
public class NotesSliderActivity extends AppCompatActivity {

    private ViewPager mNotesPager;
    private PagerAdapter mNotesPagerAdapter;
    ArrayList<String> Titles;
    int NumPages;
    public static ArrayList<ArrayList<String>> urls;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_slider);
        Titles = new ArrayList<>();
        urls = new ArrayList<>();

        for(int i =1 ; i<=NotePageActivity.jsonNoteList.length(); i++)
        {
            try {
                ArrayList<String> tempList = new ArrayList<>();
                Note temp = (Note)NotePageActivity.jsonNoteList.get(i+"");
                tempList.addAll(temp.getUrlList());
                urls.add(tempList);
                Titles.add("Class "+i);
                Log.i("sw32slider","here");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        NumPages = Titles.size();


        // Instantiate a ViewPager and a PagerAdapter.
        mNotesPager = (ViewPager) findViewById(R.id.pager);
        mNotesPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(),Titles,urls,NumPages,this);
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

