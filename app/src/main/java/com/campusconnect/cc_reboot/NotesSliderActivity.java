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
import com.campusconnect.cc_reboot.auxiliary.DepthPageTransformer;
import com.campusconnect.cc_reboot.auxiliary.ZoomOutPageTransformer;
import com.campusconnect.cc_reboot.fragment.NotesSliderPageFragment;
import com.campusconnect.cc_reboot.viewpager.ScreenSlidePagerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

    @Bind(R.id.container_note_page_info)
    RelativeLayout note_page_info;

    @Bind(R.id.ib_trial)
    ImageButton trial_button;

    private ViewPager mNotesPager;
    private PagerAdapter mNotesPagerAdapter;
    ArrayList<String> Titles;
    int NumPages;
    public static ArrayList<ArrayList<String>> urls;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_slider);
        ButterKnife.bind(this);

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

                if(page_description.getVisibility()==View.GONE)
                    page_description.setVisibility(View.VISIBLE);
                else
                    page_description.setVisibility(View.GONE);

                Animation fadeOut = new AlphaAnimation(1, 0.2f);
                fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
                fadeOut.setStartOffset(2000);
                fadeOut.setDuration(500);
                fadeOut.setFillAfter(true);
                note_page_info.startAnimation(fadeOut);
                break;

        }

    }

    @Override
    public void notePageInfo(String class_no, String curr_page, String total_pages) {
        book_title.setText(class_no);
        page_number.setText(curr_page+"/"+total_pages);
    }

    @Override
    public void pageInfoVisibility(boolean flag) {
        if(flag==true){
            Animation fadeOut = new AlphaAnimation(1, 0.2f);
            fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
            fadeOut.setStartOffset(2000);
            fadeOut.setDuration(500);
            fadeOut.setFillAfter(true);
            note_page_info.startAnimation(fadeOut);
            trial_button.startAnimation(fadeOut);
        }
    }

}

