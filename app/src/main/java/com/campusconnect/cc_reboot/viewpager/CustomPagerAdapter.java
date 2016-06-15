package com.campusconnect.cc_reboot.viewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.campusconnect.cc_reboot.R;
import com.campusconnect.cc_reboot.fragment.NotesSliderPageFragment;

/**
 * Created by RK on 13/06/2016.
 */
public class CustomPagerAdapter extends PagerAdapter {

    private Context mContext;
    int images[];

    public CustomPagerAdapter(Context context, int mImg[]) {
        mContext = context;
        images = mImg;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View rootView = inflater.inflate(R.layout.notes_images, collection, false);
        ImageView notes_fullscreen = (ImageView) rootView.findViewById(R.id.iv_notes_fullscreen);
        notes_fullscreen.setImageResource(images[position]);
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

