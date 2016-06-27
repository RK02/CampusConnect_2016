package com.campusconnect.cc_reboot.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.campusconnect.cc_reboot.NotePageActivity;
import com.campusconnect.cc_reboot.POJO.NoteBookList;
import com.campusconnect.cc_reboot.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by RK on 27/06/2016.
 */
public class CourseColorsListAdapter extends
        RecyclerView.Adapter<CourseColorsListAdapter.CourseColorsListViewHolder> {

    Context context;
    View colorPicker;
    int sdk = android.os.Build.VERSION.SDK_INT;

    int color_drawables[] = { R.drawable.color_picker_v1, R.drawable.color_picker_v2, R.drawable.color_picker_v3,
            R.drawable.color_picker_r1, R.drawable.color_picker_r2, R.drawable.color_picker_r3,
            R.drawable.color_picker_g1, R.drawable.color_picker_g2, R.drawable.color_picker_g3,
            R.drawable.color_picker_b1, R.drawable.color_picker_b2, R.drawable.color_picker_b3 };

    public CourseColorsListAdapter(Context context, View mColorPicker) {
        this.context = context;
        this.colorPicker = mColorPicker;
    }

    @Override
    public int getItemCount() {
        return color_drawables.length;
    }

    @Override
    public void onBindViewHolder(CourseColorsListViewHolder CourseColorsListViewHolder, int i) {

        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            CourseColorsListViewHolder.course_color_blob.setBackgroundDrawable(context.getResources().getDrawable(color_drawables[i]) );
        } else {
            CourseColorsListViewHolder.course_color_blob.setBackground(context.getResources().getDrawable(color_drawables[i]));
        }

    }


    @Override
    public CourseColorsListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.card_layout_color_picker, viewGroup, false);

        return new CourseColorsListViewHolder(itemView);
    }

    public class CourseColorsListViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.b_color_blob)
        Button course_color_blob;

        public CourseColorsListViewHolder(View v) {
            super(v);
            ButterKnife.bind(this,v);

            course_color_blob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        colorPicker.setBackgroundDrawable(context.getResources().getDrawable(color_drawables[getAdapterPosition()]) );
                    } else {
                        colorPicker.setBackground(context.getResources().getDrawable(color_drawables[getAdapterPosition()]));
                    }
                }
            });

        }
    }

    public static final Drawable getDrawable(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 21) {
            return ContextCompat.getDrawable(context, id);
        } else {
            return context.getResources().getDrawable(id);
        }
    }
}
