package com.campusconnect.cc_reboot.fragment;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.campusconnect.cc_reboot.AssignmentPageActivity;
import com.campusconnect.cc_reboot.NotePageActivity;
import com.campusconnect.cc_reboot.NotesSliderActivity;
import com.campusconnect.cc_reboot.R;
import com.campusconnect.cc_reboot.auxiliary.DepthPageTransformer;
import com.campusconnect.cc_reboot.auxiliary.ViewPagerDisable;
import com.campusconnect.cc_reboot.viewpager.CustomPagerAdapter;
import com.campusconnect.cc_reboot.viewpager.ScreenSlidePagerAdapter;
import com.campusconnect.cc_reboot.NotesSliderActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by RK on 13/06/2016.
 */
public class NotesSliderPageFragment extends Fragment implements View.OnTouchListener{

    @Bind(R.id.view_touch_handler)
    View touch_handling_view;

    String class_no, total_pages, curr_page;

    ArrayList<ArrayList<String>> urls = NotesSliderActivity.urls;
    int page_pos;
    int totalPages=0;
    ViewPagerDisable pager_img;
    Bundle fragArgs;


    public interface NotePageInfoToActivity{
        public void notePageInfo(String class_no, String curr_page, String total_pages);
        public void pageInfoVisibility(boolean flag);
    }
    NotePageInfoToActivity notePageInfoToActivity;

    //Variables to handle single tap and double tap
    long lastPressTime;
    static final int DOUBLE_PRESS_INTERVAL = 200;
    boolean mHasDoubleClicked = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_notes_slider_page, container, false);
        ButterKnife.bind(this,rootView);

        fragArgs = getArguments();
        pager_img = (ViewPagerDisable) rootView.findViewById(R.id.viewpager_images);

        page_pos = fragArgs.getInt("PagePos");
        for(ArrayList<String> a : urls)
        {
            totalPages+=a.size();
        }

        pager_img.setPageTransformer(true, new DepthPageTransformer());
        //class_no = fragArgs.getString("PageTitle");
        page_pos = fragArgs.getInt("PagePos");
        Log.i("sw32pagepos",page_pos+"");

        pager_img.setAdapter(new CustomPagerAdapter(getActivity(),urls.get(page_pos),page_pos));


        Log.i("sw32",page_pos+" : "+class_no);
        total_pages = Integer.toString(pager_img.getAdapter().getCount());
        curr_page = Integer.toString(1);
        notePageInfoToActivity.notePageInfo(class_no,curr_page,total_pages);

        pager_img.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int index) {
                // TODO Auto-generated method stub
                curr_page = index+1 +"";
                Log.i("sw32page",class_no);
                curr_page = Integer.toString(index+1);
                notePageInfoToActivity.notePageInfo(class_no,curr_page,total_pages);
            }
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }
            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });

        touch_handling_view.setOnTouchListener(this);

        return rootView;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        long pressTime = System.currentTimeMillis();

        //Double Tap
        if (pressTime - lastPressTime <= DOUBLE_PRESS_INTERVAL) {
            mHasDoubleClicked = true;
        }
        else {     // Single Tap/Swipe
            mHasDoubleClicked = false;
            Handler myHandler = new Handler() {
                public void handleMessage(Message m) {

                    if (!mHasDoubleClicked && ViewPagerDisable.getZoomState())
                        notePageInfoToActivity.pageInfoVisibility(true);

                }
            };
            Message m = new Message();
            myHandler.sendMessageDelayed(m,DOUBLE_PRESS_INTERVAL);
        }
        // record the last time the view was pressed.
        lastPressTime = pressTime;
        return false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            notePageInfoToActivity = (NotePageInfoToActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement notePageInfoToActivity");
        }
    }

}