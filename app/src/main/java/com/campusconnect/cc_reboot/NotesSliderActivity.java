package com.campusconnect.cc_reboot;

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

import com.campusconnect.cc_reboot.POJO.Note;
import com.campusconnect.cc_reboot.auxiliary.ViewPagerDisable;
import com.campusconnect.cc_reboot.auxiliary.ZoomOutPageTransformer;
import com.campusconnect.cc_reboot.fragment.NotesSliderPageFragment;
import com.campusconnect.cc_reboot.viewpager.ScreenSlidePagerAdapter;

import org.json.JSONException;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.http.Body;

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

    private ViewPagerDisable mNotesPager;
    private PagerAdapter mNotesPagerAdapter;
    ArrayList<String> Titles;
    ArrayList<String> pages;
    ArrayList<String> descriptions;
    ArrayList<String> dates;
    int NumPages;
    public static ArrayList<ArrayList<String>> urls;

    String curr, class_, total;

    int class_pos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_slider);
        ButterKnife.bind(this);

        Titles = new ArrayList<>();
        urls = new ArrayList<>();
        pages = new ArrayList<>();
        dates = new ArrayList<>();
        descriptions = new ArrayList<>();


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


        // Instantiate a ViewPager and a PagerAdapter.
        mNotesPager = (ViewPagerDisable) findViewById(R.id.pager);
        mNotesPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(),Titles,urls,NumPages,this);
        mNotesPager.setAdapter(mNotesPagerAdapter);
        mNotesPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mNotesPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.i("sw32externviewpager",position+"");
                page_description.setText(descriptions.get(position));
                page_date.setText(dates.get(position));

                class_pos=position+1;

                book_title.setText("Class "+class_pos);
//                page_number.setText(curr+"/"+total);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mNotesPager.setCurrentItem(NotePageActivity.jsonNoteList.length()-1);
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
        if (mNotesPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mNotesPager.setCurrentItem(mNotesPager.getCurrentItem() - 1);
        }
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
    public void notePageInfo(String class_no, String curr_page, String total_pages) {
        class_ = class_no;
        curr = curr_page;
        total = total_pages;
        book_title.setText(class_no);
        page_number.setText(curr_page+"/"+total_pages);
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

}

