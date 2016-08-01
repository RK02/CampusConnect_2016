package com.campusconnect;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;


import com.campusconnect.auxiliary.FastBlur;
import com.campusconnect.auxiliary.VerticalViewPager;
import com.campusconnect.adapter.IntroAdapter;
import com.campusconnect.auxiliary.IntroPageTransformer;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by RK on 18/07/2016.
 */
public class IntroActivity extends AppCompatActivity {

    private VerticalViewPager mVerticalViewPager;
    LinearLayout indicator_layout;
    ImageView swipe_up_arrow;
    View divider;

    Integer[] colors = {Color.parseColor("#25A599"), Color.parseColor("#9BCB65"), Color.parseColor("#25A599"), Color.parseColor("#9BCB65")};
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    TextView next, skip, finish;
    ToggleButton indicator_1, indicator_2, indicator_3, indicator_4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        divider = (View) findViewById(R.id.divider);
        swipe_up_arrow = (ImageView) findViewById(R.id.iv_swipe_up);
        skip = (TextView)findViewById(R.id.tv_skip_intro);
        next = (TextView)findViewById(R.id.tv_finish_intro);
        finish = (TextView)findViewById(R.id.tv_finish);
        mVerticalViewPager = (VerticalViewPager) findViewById(R.id.viewpager);
        indicator_layout = (LinearLayout) findViewById(R.id.container_indicator);

        indicator_1 = (ToggleButton) findViewById(R.id.indicator_1);
        indicator_2 = (ToggleButton) findViewById(R.id.indicator_2);
        indicator_3 = (ToggleButton) findViewById(R.id.indicator_3);
        indicator_4 = (ToggleButton) findViewById(R.id.indicator_4);

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
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish_intro();
            }
        });

        mVerticalViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                swipe_up_arrow.setAlpha(1.0f - positionOffset);
                if(position < (4 -1) && position < (4 - 1)) {

                    mVerticalViewPager.setBackgroundColor((Integer) argbEvaluator.evaluate(positionOffset, colors[position], colors[position + 1]));
                    indicator_layout.setBackgroundColor((Integer) argbEvaluator.evaluate(positionOffset, colors[position], colors[position + 1]));

                } else {

                    mVerticalViewPager.setBackgroundColor(colors[colors.length - 1]);
                    indicator_layout.setBackgroundColor(colors[colors.length - 1]);

                }
            }

            @Override
            public void onPageSelected(final int position) {
                uncheckIndicators();
                swipe_up_arrow.setVisibility(View.GONE);
                if(position==(4-1))
                {
                    indicator_4.setChecked(true);
//                    next.setText("FINISH");
                    divider.setVisibility(View.GONE);
                    skip.setVisibility(View.INVISIBLE);
                    skip.setClickable(false);
                    next.setVisibility(View.GONE);
                    finish.setVisibility(View.VISIBLE);

                    finish.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish_intro();
                        }
                    });
                }
                else
                {
                    if(position==0)
                        indicator_1.setChecked(true);
                    else if(position==1) {
                        indicator_2.setChecked(true);
                        swipe_up_arrow.setVisibility(View.GONE);
                    }
                    else
                        indicator_3.setChecked(true);


                    divider.setVisibility(View.VISIBLE);
                    skip.setVisibility(View.VISIBLE);
                    next.setVisibility(View.VISIBLE);
                    skip.setClickable(true);
                    finish.setVisibility(View.GONE);
//                    next.setText("NEXT");
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
        ConnectionChangeReceiver.broadcast(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyApp.activityPaused();
    }

    public void uncheckIndicators(){
        indicator_1.setChecked(false);
        indicator_2.setChecked(false);
        indicator_3.setChecked(false);
        indicator_4.setChecked(false);
    }
}
