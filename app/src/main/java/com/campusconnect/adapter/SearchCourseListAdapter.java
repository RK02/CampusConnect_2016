package com.campusconnect.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.campusconnect.CoursePageActivity;
import com.campusconnect.POJO.CourseList;
import com.campusconnect.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by RK on 05/06/2016.
 */
public class SearchCourseListAdapter extends
        RecyclerView.Adapter<SearchCourseListAdapter.SearchNotesListViewHolder> {

    Context context;
    ArrayList<CourseList> courseLists;

    public SearchCourseListAdapter(Context context,ArrayList<CourseList> courseLists) {
        this.context = context;
        this.courseLists = courseLists;
    }

    @Override
    public int getItemCount() {
        return courseLists.size();
    }

    @Override
    public void onBindViewHolder(SearchNotesListViewHolder searchNotesListViewHolder, int i) {
        final CourseList a =  courseLists.get(i);

        searchNotesListViewHolder.notebooks.setText(a.getNotesCount());
        searchNotesListViewHolder.semester.setText(a.getSemester());
        searchNotesListViewHolder.students.setText(a.getStudentCount());
        searchNotesListViewHolder.course_title.setText(a.getCourseName());
        searchNotesListViewHolder.professor.setText(a.getProfessorName());
        searchNotesListViewHolder.section.setText(a.getSectionNames().get(0));
        searchNotesListViewHolder.search_course_card.setCardBackgroundColor(Color.parseColor(a.getColour()));
        searchNotesListViewHolder.search_course_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_temp = new Intent(v.getContext(), CoursePageActivity.class);
                ViewGroup viewGroup = (ViewGroup) v.getParent();
                int index = viewGroup.indexOfChild(v);
                String id = courseLists.get(index).getCourseId();
                intent_temp.putExtra("courseId",id);
                intent_temp.putExtra("courseColor",Color.parseColor(a.getColour()));
                context.startActivity(intent_temp);
            }
        });
    }

    @Override
    public SearchNotesListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.card_layout_search_course, viewGroup, false);

        return new SearchNotesListViewHolder(itemView);
    }

    public void clear()
    {
        courseLists.clear();
        notifyDataSetChanged();
    }

    public class SearchNotesListViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.search_course_card)
        CardView search_course_card;

        @Bind(R.id.tv_course_title)
        TextView course_title;
        @Bind(R.id.tv_prof_name)
        TextView professor;
        @Bind(R.id.tv_sem)
        TextView semester;
        @Bind(R.id.tv_date_course_created)
        TextView date;
        @Bind(R.id.tv_section)
        TextView section;

        @Bind(R.id.tv_views_count)
        TextView notebooks;
        @Bind(R.id.tv_exam_date)
        TextView students;


        public SearchNotesListViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);



        }
    }
}