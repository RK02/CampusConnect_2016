package com.campusconnect;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;


import com.campusconnect.auxiliary.VerticalViewPager;
import com.campusconnect.adapter.IntroAdapter;
import com.campusconnect.auxiliary.IntroPageTransformer;

/**
 * Created by RK on 18/07/2016.
 */
public class IntroActivity extends AppCompatActivity {

    private VerticalViewPager mVerticalViewPager;
    Integer[] colors = {Color.parseColor("#9BCB65"), Color.parseColor("#25A599"), Color.parseColor("#9BCB65"), Color.parseColor("#25A599")};
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    TextView next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_intro);
        next = (TextView)findViewById(R.id.tv_finish_intro);
        mVerticalViewPager = (VerticalViewPager) findViewById(R.id.viewpager);

        // Set an Adapter on the ViewPager
        mVerticalViewPager.setAdapter(new IntroAdapter(getSupportFragmentManager()));

        // Set a PageTransformer
        mVerticalViewPager.setPageTransformer(false, new IntroPageTransformer());
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVerticalViewPager.setCurrentItem(1);
            }
        });

        mVerticalViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position < (4 -1) && position < (4 - 1)) {

                    mVerticalViewPager.setBackgroundColor((Integer) argbEvaluator.evaluate(positionOffset, colors[position], colors[position + 1]));

                } else {

                    mVerticalViewPager.setBackgroundColor(colors[colors.length - 1]);

                }
            }

            @Override
            public void onPageSelected(final int position) {
                if(position==(4-1))
                {
                    next.setText("FINISH");
                    next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish_intro();
                        }
                    });
                }
                else
                {
                    next.setText("NEXT");
                    next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mVerticalViewPager.setCurrentItem(position+1);
                        }
                    });
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    void finish_intro()
    {
        Intent intent = new Intent(IntroActivity.this, GoogleSignInActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onResume() {
        super.onResume();
        MyApp.activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyApp.activityPaused();
    }

}
