package com.campusconnect.cc_reboot.fragment.Drawer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.campusconnect.cc_reboot.R;
import com.campusconnect.cc_reboot.adapter.CourseColorsListAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by RK on 15/06/2016.
 */
public class FragmentAddCourse extends Fragment implements View.OnClickListener{

    @Bind(R.id.et_courseName)
    EditText courseName;
    @Bind(R.id.et_courseCode)
    EditText courseCode;
    @Bind(R.id.et_courseProf)
    EditText courseProf;
    @Bind(R.id.et_courseSem)
    EditText courseSem;
    @Bind(R.id.et_courseBatch)
    EditText courseBatch;
    @Bind(R.id.et_courseSection)
    EditText courseSection;
    @Bind(R.id.et_courseBranch)
    EditText courseBranch;

    @Bind(R.id.et_startDate_Mon)
    EditText startDateMon;
    @Bind(R.id.et_endDate_Mon)
    EditText endDateMon;

    @Bind(R.id.et_startDate_Tue)
    EditText startDateTue;
    @Bind(R.id.et_endDate_Tue)
    EditText endDateTue;

    @Bind(R.id.et_startDate_Wed)
    EditText startDateWed;
    @Bind(R.id.et_endDate_Wed)
    EditText endDateWed;

    @Bind(R.id.et_startDate_Thurs)
    EditText startDateThurs;
    @Bind(R.id.et_endDate_Thurs)
    EditText endDateThurs;

    @Bind(R.id.et_startDate_Fri)
    EditText startDateFri;
    @Bind(R.id.et_endDate_Fri)
    EditText endDateFri;

    @Bind(R.id.et_startDate_Sat)
    EditText startDateSat;
    @Bind(R.id.et_endDate_Sat)
    EditText endDateSat;

    @Bind(R.id.tb_monday)
    ToggleButton tbMonday;
    @Bind(R.id.tb_tuesday)
    ToggleButton tbTuesday;
    @Bind(R.id.tb_wednesday)
    ToggleButton tbWednesday;
    @Bind(R.id.tb_thursday)
    ToggleButton tbThursday;
    @Bind(R.id.tb_friday)
    ToggleButton tbFriday;
    @Bind(R.id.tb_saturday)
    ToggleButton tbSaturday;

    @Bind(R.id.chk_elective)
    CheckBox elective;

    @Bind(R.id.container_timetable_timings_mon)
    LinearLayout MonTimingsContainer;
    @Bind(R.id.container_timetable_timings_tue)
    LinearLayout TueTimingsContainer;
    @Bind(R.id.container_timetable_timings_wed)
    LinearLayout WedTimingsContainer;
    @Bind(R.id.container_timetable_timings_thurs)
    LinearLayout ThursTimingsContainer;
    @Bind(R.id.container_timetable_timings_fri)
    LinearLayout FriTimingsContainer;
    @Bind(R.id.container_timetable_timings_sat)
    LinearLayout SatTimingsContainer;

    @Bind(R.id.view_course_color_picker)
    View courseColorPicker;

    @Bind(R.id.b_cancel)
    Button cancel;

    @Bind(R.id.b_create)
    Button create;

    Context context;
    ColorPickerDialog colorPickerDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_course,container,false);
        ButterKnife.bind(this,v);
        context = v.getContext();


        //OnClickListeners
        courseColorPicker.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.view_course_color_picker:

                colorPickerDialog = new ColorPickerDialog((Activity) context);

                colorPickerDialog.show();

                Window window = colorPickerDialog.getWindow();
                window.setLayout(900, GridLayoutManager.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.CENTER_HORIZONTAL);

                break;
        }

    }

    public class ColorPickerDialog extends Dialog{

        public Activity c;
        public Dialog d;

        RecyclerView course_colors_list;
        CourseColorsListAdapter mCourseColorsAdapter;
        GridLayoutManager mLayoutManager;

        public ColorPickerDialog(Activity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.color_picker_dialog);

            course_colors_list = (RecyclerView) findViewById (R.id.rv_course_colors);
            mLayoutManager = new GridLayoutManager(c,3);

            mCourseColorsAdapter = new CourseColorsListAdapter(c,courseColorPicker);
            course_colors_list.setLayoutManager(mLayoutManager);
            course_colors_list.setItemAnimator(new DefaultItemAnimator());
            course_colors_list.setAdapter(mCourseColorsAdapter);

        }

    }

}
