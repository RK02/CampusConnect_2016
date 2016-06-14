package com.campusconnect.cc_reboot.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.campusconnect.cc_reboot.AssignmentPageActivity;
import com.campusconnect.cc_reboot.R;
import com.campusconnect.cc_reboot.auxiliary.DepthPageTransformer;
import com.campusconnect.cc_reboot.viewpager.CustomPagerAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by RK on 13/06/2016.
 */
public class NotesSliderPageFragment extends Fragment {

    @Bind(R.id.tv_book_indicator)
    TextView book_title;
    @Bind(R.id.tv_page_indicator)
    TextView page_number;
    @Bind(R.id.container_page_indicator)
    RelativeLayout page_indicator_cont;

    int page_pos;
    ViewPager pager_img;
    Bundle fragArgs;

    int img_1[] = { R.mipmap.ic_due_18,
            R.mipmap.ic_launcher,
            R.mipmap.ic_share_24,
            R.mipmap.ic_sort_24 };

    int img_2[] = { R.mipmap.ic_notifications_24,
            R.mipmap.ic_notifications_none_24,
            R.mipmap.ic_pages_18 };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_notes_slider_page, container, false);
        ButterKnife.bind(this,rootView);

        fragArgs = getArguments();
        pager_img = (ViewPager) rootView.findViewById(R.id.viewpager_images);


        page_pos = fragArgs.getInt("PagePos");

        pager_img.setPageTransformer(true, new DepthPageTransformer());

        if(page_pos%2!=0)
            pager_img.setAdapter(new CustomPagerAdapter(getActivity(),img_1));
        else
            pager_img.setAdapter(new CustomPagerAdapter(getActivity(),img_2));

        book_title.setText(fragArgs.getString("PageTitle"));
        page_number.setText(1+"/"+pager_img.getAdapter().getCount());

        pager_img.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int index) {
                // TODO Auto-generated method stub
                page_number.setText(index+1+"/"+pager_img.getAdapter().getCount());
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


        return rootView;
    }
}
