package com.campusconnect;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.campusconnect.POJO.Note;
import com.campusconnect.auxiliary.ViewPagerDisable;
import com.campusconnect.auxiliary.ZoomOutPageTransformer;
import com.campusconnect.fragment.NotesSliderPageFragment;
import com.campusconnect.viewpager.CustomPagerAdapter;
import com.campusconnect.viewpager.ScreenSlidePagerAdapter;

import org.json.JSONException;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by RK on 04/06/2016.
 */
public class NotesSliderActivity extends AppCompatActivity  implements NotesSliderPageFragment.NotePageInfoToActivity, View.OnClickListener{

    @Bind(R.id.tv_book_indicator)
    TextView book_title;
    @Bind(R.id.tv_page_indicator)
    TextView page_number;
    @Bind(R.id.tv_note_page_description)
    TextView page_description;
    @Bind(R.id.tv_note_page_date)
    TextView page_date;

    @Bind(R.id.container_note_page_info)
    RelativeLayout note_page_info;

    @Bind(R.id.ib_trial)
    ImageButton trial_button;

    private ViewPagerDisable mNotesPager, mChildPager;
    private PagerAdapter mNotesPagerAdapter;
    ArrayList<String> Titles;
    ArrayList<String> pages;
    ArrayList<String> descriptions;
    ArrayList<String> dates;
    ArrayList<Integer> book_indicator_helper_array;
    ArrayList<Integer> page_indicator_helper_array;
    int NumPages;
    public static ArrayList<ArrayList<String>> urls;
    ArrayList<String> urls_single;

    String curr, class_, total_pages;

    int class_pos, prev_class_pos, k=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_slider);
        ButterKnife.bind(this);

        Titles = new ArrayList<>();
        urls = new ArrayList<>();
        urls_single = new ArrayList<>();
        pages = new ArrayList<>();
        dates = new ArrayList<>();
        descriptions = new ArrayList<>();
        book_indicator_helper_array = new ArrayList<>();
        page_indicator_helper_array = new ArrayList<>();

        for(int i =1 ; i<=NotePageActivity.jsonNoteList.length(); i++)
        {
            try {
                ArrayList<String> tempList = new ArrayList<>();
                Note temp = (Note)NotePageActivity.jsonNoteList.get(i+"");
                pages.add(temp.getUrlList().size()+"");
                tempList.addAll(temp.getUrlList());
                dates.add(temp.getDate());
                descriptions.add(temp.getDescription());
                urls.add(tempList);
                Titles.add("Class "+i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        NumPages = Titles.size();


        for(int i=0; i<NumPages; i++) {
            k=0;
            for (int j = 0; j < urls.get(i).size(); j++) {
                urls_single.add(urls.get(i).get(j));
                book_indicator_helper_array.add(i + 1);
                page_indicator_helper_array.add(++k);
            }
        }


        // Instantiate a ViewPager and a PagerAdapter.
        mNotesPager = (ViewPagerDisable) findViewById(R.id.pager);
//        mNotesPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(),Titles,urls,NumPages,this,mNotesPager);
//        mNotesPager.setAdapter(mNotesPagerAdapter);

        mNotesPager.setAdapter(new CustomPagerAdapter(this,urls_single));

        ///Setting the initial page stats
        mNotesPager.setCurrentItem(urls_single.size()-urls.get(NumPages-1).size());
        prev_class_pos = book_indicator_helper_array.get(urls_single.size()-1);
        total_pages = urls.get(book_indicator_helper_array.get(urls_single.size()-1)-1).size()+"";
        book_title.setText("Class "+book_indicator_helper_array.get(urls_single.size()-1).toString());
        page_number.setText(""+1+"/"+total_pages);

        mNotesPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                total_pages = urls.get(book_indicator_helper_array.get(position)-1).size()+"";
                k=page_indicator_helper_array.get(position);
                book_title.setText("Class "+book_indicator_helper_array.get(position).toString());
                page_number.setText(""+k+"/"+total_pages);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//            mNotesPager.setPageTransformer(true, new ZoomOutPageTransformer());

        //Hiding the notepage info
        Animation fadeOut = new AlphaAnimation(1, 0.4f);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setStartOffset(1200);
        fadeOut.setDuration(500);
        fadeOut.setFillAfter(true);

        note_page_info.startAnimation(fadeOut);
        trial_button.startAnimation(fadeOut);

        //OnClickListeners
        trial_button.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
//        if (mNotesPager.getCurrentItem() == 0) {
            super.onBackPressed();
//        } else {
//            mNotesPager.setCurrentItem(mNotesPager.getCurrentItem() - 1);
//        }
    }



    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.ib_trial:

                if(page_description.getVisibility()==View.GONE) {
                    page_description.setVisibility(View.VISIBLE);
                    trial_button.setImageResource(R.mipmap.ic_close_36_black);

                    Animation fadeIn = new AlphaAnimation(0.4f, 1);
                    fadeIn.setInterpolator(new AccelerateInterpolator()); //and this
                    fadeIn.setDuration(500);
                    fadeIn.setFillAfter(true);

                    note_page_info.startAnimation(fadeIn);
                    trial_button.startAnimation(fadeIn);
                }
                else {
                    page_description.setVisibility(View.GONE);
                    trial_button.setImageResource(R.mipmap.ic_info_36_black);

                    Animation fadeOut = new AlphaAnimation(1, 0.4f);
                    fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
                    fadeOut.setStartOffset(1200);
                    fadeOut.setDuration(500);
                    fadeOut.setFillAfter(true);

                    note_page_info.startAnimation(fadeOut);
                    trial_button.startAnimation(fadeOut);
                }
                break;

        }

    }

    @Override
    public void notePageInfo(String class_no, String curr_page, String total_pages, ViewPagerDisable child) {
//        class_ = class_no;
//        curr = curr_page;
//        total = total_pages;
//        book_title.setText(class_no);
//        mChildPager = child;
//        page_number.setText(curr_page+"/"+total_pages);
//        page_description.setText(descriptions.get(Integer.parseInt(class_no.split(" ")[1])));
    }

    @Override
    public void pageInfoVisibility(boolean flag) {
        if(flag==true){
            Animation fadeOut = new AlphaAnimation(1, 0.4f);
            fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
            fadeOut.setStartOffset(1200);
            fadeOut.setDuration(500);
            fadeOut.setFillAfter(true);
            note_page_info.startAnimation(fadeOut);
            trial_button.startAnimation(fadeOut);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        MyApp.activityResumed();
        ConnectionChangeReceiver.broadcast(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyApp.activityPaused();
    }

}

