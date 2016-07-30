package com.campusconnect.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.campusconnect.EditCourseActivity;
import com.campusconnect.R;
import com.campusconnect.fragment.Drawer.FragmentAddCourse;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by RK on 27/06/2016.
 */
public class CourseColorsListAdapter extends
        RecyclerView.Adapter<CourseColorsListAdapter.CourseColorsListViewHolder> {

    Context context;
    View colorPicker;

    String courseColors[] = {"#9A519F","#AB6BAC","#C593C2",
                             "#EE5451","#E47373","#ED999A",
                             "#9BCB65","#ACD480","#C4DFA5",
                             "#25A599","#4CB5AB","#80CAC3"};

    public CourseColorsListAdapter(Context context, View mColorPicker) {
        this.context = context;
        this.colorPicker = mColorPicker;
    }

    @Override
    public int getItemCount() {
        return courseColors.length;
    }

    @Override
    public void onBindViewHolder(CourseColorsListViewHolder CourseColorsListViewHolder, int i) {

        ShapeDrawable courseColor_circle = new ShapeDrawable( new OvalShape() );
        courseColor_circle.getPaint().setColor(Color.parseColor(courseColors[i]));
        CourseColorsListViewHolder.course_color_blob.setBackground(courseColor_circle);

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

        public CourseColorsListViewHolder(final View v) {
            super(v);
            ButterKnife.bind(this,v);

            course_color_blob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ShapeDrawable courseColor_circle = new ShapeDrawable( new OvalShape() );
                    courseColor_circle.getPaint().setColor(Color.parseColor(courseColors[getAdapterPosition()]));
                    colorPicker.setBackground(courseColor_circle);
                    if(FragmentAddCourse.colorPickerDialog!=null)
                    FragmentAddCourse.colorPickerDialog.dismiss();
                    if(EditCourseActivity.colorPickerDialog!=null)
                    EditCourseActivity.colorPickerDialog.dismiss();
                }
            });

        }
    }

}
