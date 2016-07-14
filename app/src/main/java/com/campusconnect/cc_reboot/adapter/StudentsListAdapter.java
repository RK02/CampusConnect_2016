package com.campusconnect.cc_reboot.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.campusconnect.cc_reboot.CoursePageActivity;
import com.campusconnect.cc_reboot.POJO.SubscribedCourseList;
import com.campusconnect.cc_reboot.R;
import com.campusconnect.cc_reboot.auxiliary.CircularImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by RK on 13/07/2016.
 */
public class StudentsListAdapter extends
        RecyclerView.Adapter<StudentsListAdapter.StudentsListViewHolder> {

    Context context;

    public StudentsListAdapter(Context context) {
        this.context = context;
    }


    @Override
    public int getItemCount() {
        return 5;
    }


    @Override
    public void onBindViewHolder(StudentsListViewHolder studentsListViewHolder, final int i) {

        //Add your code here


        if(i==getItemCount()-1) {
            studentsListViewHolder.divider.setVisibility(View.GONE);
            studentsListViewHolder.student_card_all.setPadding(16,16,16,16);
        }

    }


    @Override
    public StudentsListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.card_layout_student, viewGroup, false);
        return new StudentsListViewHolder(itemView);
    }

    public class StudentsListViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.student_card)
        CardView student_card;

        @Bind(R.id.iv_student_profile_picture)
        CircularImageView student_profile_pic;

        @Bind(R.id.tv_student_name)
        TextView student_name;
        @Bind(R.id.tv_student_hobbies)
        TextView student_hobbies;

        @Bind(R.id.tb_make_admin)
        ToggleButton make_admin_button;

        @Bind(R.id.divider)
        View divider;

        @Bind(R.id.container_student_card_all)
        RelativeLayout student_card_all;

        public StudentsListViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

        }
    }
}
