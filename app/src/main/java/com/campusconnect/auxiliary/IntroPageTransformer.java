package com.campusconnect.auxiliary;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.campusconnect.R;

/**
 * Created by RK on 18/07/2016.
 */
public class IntroPageTransformer implements ViewPager.PageTransformer {

    @Override
    public void transformPage(View page, float position) {

        int pagePosition = (int) page.getTag();

        int pageHeight = page.getHeight();
        float pageHeightTimesPosition = pageHeight * position;
        float absPosition = Math.abs(position);

        if (position <= -1.0f || position >= 1.0f) {

        } else if (position == 0.0f) {

        } else {

            View title = page.findViewById(R.id.title);
            title.setAlpha(1.0f - absPosition);
            title.setTranslationX(-pageHeightTimesPosition);
            title.setTranslationY(pageHeightTimesPosition*1.2f);

            View description = page.findViewById(R.id.description);
            description.setTranslationX(-pageHeightTimesPosition);
            description.setTranslationY(pageHeightTimesPosition*1.0f);
            description.setAlpha(1.0f - absPosition);

            View computer = page.findViewById(R.id.iv_walkthrough);
            computer.setAlpha(1.0f - absPosition);
            computer.setTranslationX(-pageHeightTimesPosition);
            computer.setTranslationY(pageHeightTimesPosition*1.4f);

            if (pagePosition == 0 && computer != null) {
            }

            if (position < 0) {

            } else {

            }
        }
    }

}
