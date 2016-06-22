package com.campusconnect.cc_reboot.auxiliary;

/**
 * Created by RK on 22/06/2016.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.campusconnect.cc_reboot.UploadPicturesActivity;

public class CustomGalleryImageView extends ImageView
{
    public CustomGalleryImageView(Context context)
    {
        super(context);
    }

    public CustomGalleryImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public CustomGalleryImageView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension((int)(UploadPicturesActivity.width/2), (int)(UploadPicturesActivity.height/2)); //Snap to width
    }
}
