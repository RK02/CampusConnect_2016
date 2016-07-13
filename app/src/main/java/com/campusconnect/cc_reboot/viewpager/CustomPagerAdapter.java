package com.campusconnect.cc_reboot.viewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.campusconnect.cc_reboot.NotesSliderActivity;
import com.campusconnect.cc_reboot.R;
import com.campusconnect.cc_reboot.auxiliary.PinchToZoom.GestureImageView;
import com.campusconnect.cc_reboot.fragment.NotesSliderPageFragment;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by RK on 13/06/2016.
 */
public class CustomPagerAdapter extends PagerAdapter {

    private Context mContext;
    ArrayList<String> images;
    private int mlevel;


    public CustomPagerAdapter(Context context, ArrayList<String> mImg, int level) {
        mContext = context;
        images = mImg;
        mlevel = level;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View rootView = inflater.inflate(R.layout.notes_images, collection, false);
        GestureImageView notes_fullscreen = (GestureImageView) rootView.findViewById(R.id.iv_notes_fullscreen);
        Picasso.with(mContext).
                load(NotesSliderActivity.urls.get(mlevel).get(position)).
                fit().
                error(R.mipmap.ic_pages_18).
                into(notes_fullscreen);
        collection.addView(rootView);
        return rootView;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public boolean isViewFromObject(View container, Object obj) {
        return container == obj;
    }

}

