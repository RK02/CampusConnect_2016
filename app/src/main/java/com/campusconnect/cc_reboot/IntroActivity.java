package com.campusconnect.cc_reboot;

import android.animation.ArgbEvaluator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.campusconnect.cc_reboot.adapter.IntroAdapter;
import com.campusconnect.cc_reboot.auxiliary.IntroPageTransformer;

/**
 * Created by RK on 18/07/2016.
 */
public class IntroActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    Integer[] colors = {Color.parseColor("#9BCB65"), Color.parseColor("#25A599"), Color.parseColor("#9BCB65"), Color.parseColor("#25A599")};
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_intro);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        // Set an Adapter on the ViewPager
        mViewPager.setAdapter(new IntroAdapter(getSupportFragmentManager()));

        // Set a PageTransformer
        mViewPager.setPageTransformer(false, new IntroPageTransformer());

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position < (4 -1) && position < (4 - 1)) {

                    mViewPager.setBackgroundColor((Integer) argbEvaluator.evaluate(positionOffset, colors[position], colors[position + 1]));

                } else {

                    mViewPager.setBackgroundColor(colors[colors.length - 1]);

                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
