package com.campusconnect.auxiliary;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by RK on 23/06/2016.
 */
public class ViewPagerDisable extends ViewPager {

    private static boolean zoomed;

    public ViewPagerDisable(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.zoomed = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.zoomed) {
            return super.onTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.zoomed) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    public static void setPagingEnabled(boolean enabled) {
        zoomed = enabled;
    }

    public static boolean getZoomState() {
        return zoomed;
    }

}

