package com.campusconnect.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.campusconnect.EditCourseActivity;
import com.campusconnect.R;
import com.campusconnect.fragment.Drawer.FragmentAddCourse;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by RK on 27/06/2016.
 */
public class CourseColorsListAdapter extends BaseAdapter {

    Context context;
    View colorPicker;
    private LayoutInflater mInflater;

    String courseColors[] = {"#9A519F","#AB6BAC","#C593C2",
                             "#EE5451","#E47373","#ED999A",
                             "#9BCB65","#ACD480","#C4DFA5",
                             "#25A599","#4CB5AB","#80CAC3"};

    public CourseColorsListAdapter(Context context, View mColorPicker) {
        this.context = context;
        colorPicker = mColorPicker;
        mInflater = (LayoutInflater.from(context));
    }

    public int getCount() {
        return courseColors.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.card_layout_color_picker, null);
            holder.course_color_blob = (Button) convertView.findViewById(R.id.b_color_blob);

            ShapeDrawable courseColor_circle = new ShapeDrawable( new OvalShape() );
            courseColor_circle.getPaint().setColor(Color.parseColor(courseColors[position]));
            holder.course_color_blob.setBackground(courseColor_circle);

            holder.course_color_blob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ShapeDrawable courseColor_circle = new ShapeDrawable( new OvalShape() );
                    courseColor_circle.getPaint().setColor(Color.parseColor(courseColors[position]));
                    colorPicker.setBackground(courseColor_circle);
                    if(FragmentAddCourse.colorPickerDialog!=null)
                        FragmentAddCourse.colorPickerDialog.dismiss();
                    if(EditCourseActivity.colorPickerDialog!=null)
                        EditCourseActivity.colorPickerDialog.dismiss();
                }
            });

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    class ViewHolder {
        Button course_color_blob;
    }
}
