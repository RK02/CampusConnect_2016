package com.campusconnect.cc_reboot.fragment.Home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import com.campusconnect.cc_reboot.R;
import com.campusconnect.cc_reboot.auxiliary.ObservableScrollView;
import com.campusconnect.cc_reboot.auxiliary.ScrollViewListener;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by RK on 05/06/2016.
 */
public class FragmentTimetable extends Fragment implements ScrollViewListener{

    @Bind(R.id.scroll_horizontal_header)
    ObservableScrollView header_scroll_horizontal;
    @Bind(R.id.scroll_horizontal_body)
    ObservableScrollView body_scroll_horizontal;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_timetable, container, false);
        ButterKnife.bind(this,v);

        header_scroll_horizontal.setScrollViewListener(this);
        body_scroll_horizontal.setScrollViewListener(this);

        return v;
    }

    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        if(scrollView == header_scroll_horizontal) {
            body_scroll_horizontal.scrollTo(x, y);
        } else if(scrollView == body_scroll_horizontal) {
            header_scroll_horizontal.scrollTo(x, y);
        }
    }
}
