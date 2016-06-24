package com.campusconnect.cc_reboot.fragment.Drawer;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.campusconnect.cc_reboot.CoursePageActivity;
import com.campusconnect.cc_reboot.HomeActivity2;
import com.campusconnect.cc_reboot.POJO.ModelAddCourse;
import com.campusconnect.cc_reboot.POJO.MyApi;
import com.campusconnect.cc_reboot.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by RK on 15/06/2016.
 */
public class FragmentAddCourse extends Fragment {

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
//
//    @Bind(R.id.et_startDate_Mon)
//    EditText startDateMon;
//    @Bind(R.id.et_endDate_Mon)
//    EditText endDateMon;
//
//    @Bind(R.id.et_startDate_Tue)
//    EditText startDateTue;
//    @Bind(R.id.et_endDate_Tue)
//    EditText endDateTue;
//
//    @Bind(R.id.et_startDate_Wed)
//    EditText startDateWed;
//    @Bind(R.id.et_endDate_Wed)
//    EditText endDateWed;
//
//    @Bind(R.id.et_startDate_Thurs)
//    EditText startDateThurs;
//    @Bind(R.id.et_endDate_Thurs)
//    EditText endDateThurs;
//
//    @Bind(R.id.et_startDate_Fri)
//    EditText startDateFri;
//    @Bind(R.id.et_endDate_Fri)
//    EditText endDateFri;
//
//    @Bind(R.id.et_startDate_Sat)
//    EditText startDateSat;
//    @Bind(R.id.et_endDate_Sat)
//    EditText endDateSat;

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

//    @Bind(R.id.container_timetable_timings_mon)
//    LinearLayout MonTimingsContainer;
//    @Bind(R.id.container_timetable_timings_tue)
//    LinearLayout TueTimingsContainer;
//    @Bind(R.id.container_timetable_timings_wed)
//    LinearLayout WedTimingsContainer;
//    @Bind(R.id.container_timetable_timings_thurs)
//    LinearLayout ThursTimingsContainer;
//    @Bind(R.id.container_timetable_timings_fri)
//    LinearLayout FriTimingsContainer;
//    @Bind(R.id.container_timetable_timings_sat)
//    LinearLayout SatTimingsContainer;
    ArrayList<ToggleButton> days = new ArrayList<>();
    String[] daysOfTheWeek = {"Mon","Tue","Wed","Thu","Fri","Sat"};
    HashMap<String, View> days_selected = new HashMap<>();
    LinearLayout events;




    @Bind(R.id.b_cancel)
    Button cancel;

    @Bind(R.id.b_create)
    Button create;

    String profileId;
    String collegeId;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_course,container,false);
        ButterKnife.bind(this,v);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("CC", Context.MODE_PRIVATE);
        String branchName = sharedPreferences.getString("branchName","");
        String batchName = sharedPreferences.getString("batchName","");
        String sectionName = sharedPreferences.getString("sectionName","");
         profileId = sharedPreferences.getString("profileId","");
         collegeId = sharedPreferences.getString("collegeId","");
        courseBranch.setText(branchName);
        courseBatch.setText(batchName);
        courseSection.setText(sectionName);
        days.add((ToggleButton) v.findViewById(R.id.tb_monday));
        days.add((ToggleButton) v.findViewById(R.id.tb_tuesday));
        days.add((ToggleButton) v.findViewById(R.id.tb_wednesday));
        days.add((ToggleButton) v.findViewById(R.id.tb_thursday));
        days.add((ToggleButton) v.findViewById(R.id.tb_friday));
        days.add((ToggleButton) v.findViewById(R.id.tb_saturday));
        events = (LinearLayout) v.findViewById(R.id.container_timetable_timings);
        int i = 0;
        for (String day : daysOfTheWeek) {
            ToggleButton temp = days.get(i);
            temp.setTextOff(day);
            temp.setTextOn(day);
            temp.setText(day);
            i++;
            temp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                    {
                        View view = inflater.inflate(R.layout.course_day,events,false);
                        ((TextView)view.findViewById(R.id.tv_day_title)).setText(buttonView.getText());
                        final EditText startTime = (EditText) view.findViewById(R.id.et_startTime);
                        final EditText endTime = (EditText) view.findViewById(R.id.et_endTime);
                        startTime.setFocusable(false);endTime.setFocusable(false);
                        final TimePickerDialog startTimePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                if (minute < 10) {
                                    startTime.setText(hourOfDay + ":0" + minute);
                                } else {
                                    startTime.setText(hourOfDay + ":" + minute);
                                }


                            }
                        },8,00,false);
                        final TimePickerDialog endTimePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                if(hourOfDay>Integer.parseInt(startTime.getText().toString().split(":")[0])) {
                                    if (minute < 10) endTime.setText(hourOfDay + ":0" + minute);
                                    else {
                                        endTime.setText(hourOfDay + ":" + minute);
                                    }
                                }
                                else
                                {
                                    Toast.makeText(getActivity(),"Please Select a Valid Time",Toast.LENGTH_SHORT).show();
                                }
                            }
                        },8,00,false);
                        startTime.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startTimePickerDialog.show();
                            }
                        });
                        endTime.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                endTimePickerDialog.show();
                            }
                        });
                        events.addView(view);
                        days_selected.put(buttonView.getText().toString(),view);
                    }
                    else
                    {
                        events.removeView(days_selected.get(buttonView.getText().toString()));
                        days_selected.remove(buttonView.getText().toString());
                    }
                }
            });
        }
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create();
            }
        });
        return v;
    }
    void finish()
    {
        getActivity().onBackPressed();
    }

    void create()
    {
        Retrofit retrofit = new Retrofit.
                Builder()
                .baseUrl(MyApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        String[] temp =  courseBranch.getText().toString().split(",");
        ArrayList<String> branches = new ArrayList<>();
        for (String a : temp){
            branches.add(a);
        }
        temp =  courseBatch.getText().toString().split(",");
        ArrayList<String> batches = new ArrayList<>();
        for (String a : temp){
            batches.add(a);
        }

        temp = courseSection.getText().toString().split(",");
        ArrayList<String> sections = new ArrayList<>();
        for (String a : temp)
        {
            sections.add(a);
        }
        ArrayList<String> dates  = new ArrayList<>();
        ArrayList<String> startTimes = new ArrayList<>();
        ArrayList<String> endTimes = new ArrayList<>();

        for (String temp1 : days_selected.keySet()){
            String date = "";
            switch(temp1){
                case "Mon":date = "1";break;
                case "Tue":date = "2";break;
                case "Wed":date = "3";break;
                case "Thu":date = "4";break;
                case "Fri":date = "5";break;
                case "Sat":date = "6";break;
            }
            dates.add(date);
            startTimes.add(((EditText)days_selected.get(temp1).findViewById(R.id.et_startTime)).getText().toString());
            endTimes.add(((EditText)days_selected.get(temp1).findViewById(R.id.et_endTime)).getText().toString());
        }
        MyApi myApi = retrofit.create(MyApi.class);
        MyApi.addCourseRequest body = new MyApi.addCourseRequest(
                profileId, collegeId,
                courseName.getText().toString(),
                courseCode.getText().toString(),
                courseProf.getText().toString(),
                courseSem.getText().toString(),
                batches.subList(0,batches.size()),
                sections.subList(0,sections.size()),
                branches.subList(0,branches.size()),
                dates.subList(0,dates.size()),
                startTimes.subList(0,startTimes.size()),
                endTimes.subList(0,endTimes.size()),
                "RED"
                );


        Call<ModelAddCourse> call = myApi.addCourse(body);
        call.enqueue(new Callback<ModelAddCourse>() {
            @Override
            public void onResponse(Call<ModelAddCourse> call, Response<ModelAddCourse> response) {
                ModelAddCourse addCourse = response.body();
                Log.i("sw32",response.code()+"");
                String courseId = addCourse.getKey();
                finish();
                Intent intent = new Intent(getActivity(), CoursePageActivity.class);
                intent.putExtra("courseId",courseId);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<ModelAddCourse> call, Throwable t) {
                Log.i("sw32","addCourseFail");
            }
        });

    }
}
