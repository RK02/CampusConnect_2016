package com.campusconnect.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.campusconnect.POJO.CourseList;
import com.campusconnect.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by RK on 05/06/2016.
 */
public class CourseSelectionListAdapter extends
        RecyclerView.Adapter<CourseSelectionListAdapter.CourseSelectionListViewHolder> {

    Context context;
    ArrayList<CourseList> courses;
    ArrayList<String> subbed = new ArrayList<>();

    public CourseSelectionListAdapter(Context context, ArrayList<CourseList> courses) {
        this.context = context;
        this.courses = courses;
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public ArrayList<String> getSubbed()
    {
        return subbed;
    }

    @Override
    public void onBindViewHolder(final CourseSelectionListViewHolder courseSelectionListViewHolder, int i) {
        final int a = i;
        courseSelectionListViewHolder.course_title.setText(courses.get(i).getCourseName());
        courseSelectionListViewHolder.course_prof.setText(courses.get(i).getProfessorName());
        courseSelectionListViewHolder.course_section.setText(courses.get(i).getSectionNames().toString());
        courseSelectionListViewHolder.course_subscribe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    subbed.add(courses.get(a).getCourseId());
                    courseSelectionListViewHolder.course_subscribe.setBackgroundColor(context.getResources().getColor(R.color.ColorPrimary));

                }
                else {
                    subbed.remove(courses.get(a).getCourseId());
                    courseSelectionListViewHolder.course_subscribe.setBackgroundColor(context.getResources().getColor(R.color.ColorKeyStatContainer));
                }
            }
        });
        courseSelectionListViewHolder.course_subscribe.setChecked(true);
    }

    @Override
    public CourseSelectionListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.card_layout_course_selection, viewGroup, false);
        return new CourseSelectionListViewHolder(itemView);
    }

    public class CourseSelectionListViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.course_select_card)
        CardView course_select_card;

        @Bind(R.id.tv_course_title)
        TextView course_title;
        @Bind(R.id.tv_prof_name)
        TextView course_prof;
        @Bind(R.id.tv_sem)
        TextView course_sem;
        @Bind(R.id.tv_date_course_created)
        TextView course_created;
        @Bind(R.id.tv_section)
        TextView course_section;

        @Bind(R.id.tb_subscribe_course)
        ToggleButton course_subscribe;

        public CourseSelectionListViewHolder(View v) {
            super(v);
            ButterKnife.bind(this,v);

        }
    }
}