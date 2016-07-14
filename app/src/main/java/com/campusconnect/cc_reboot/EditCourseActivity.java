package com.campusconnect.cc_reboot;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.campusconnect.cc_reboot.POJO.ModelAddCourse;
import com.campusconnect.cc_reboot.POJO.ModelBranchList;
import com.campusconnect.cc_reboot.POJO.MyApi;
import com.campusconnect.cc_reboot.adapter.CourseColorsListAdapter;
import com.campusconnect.cc_reboot.fragment.Drawer.FragmentAddCourse;
import com.campusconnect.cc_reboot.fragment.Home.FragmentCourses;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditCourseActivity extends AppCompatActivity {
    @Bind(R.id.sv_add_course)
    ScrollView addCourse_scroll;

    @Bind(R.id.view_course_color_picker)
    View courseColorPicker;
    public static ColorPickerDialog colorPickerDialog;

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
    AutoCompleteTextView courseBranch;

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
    @Bind(R.id.tv_timings_title)
    TextView timings_title;

    @Bind(R.id.chk_elective)
    CheckBox elective;
    @Bind(R.id.chk_branches)
    CheckBox branches;

    ArrayList<ToggleButton> days = new ArrayList<>();
    String[] daysOfTheWeek = {"Mon","Tue","Wed","Thu","Fri","Sat"};
    HashMap<String, View> days_selected = new HashMap<>();
    LinearLayout events;
    ArrayList<String> dates;
    ArrayList<String> branchNames;
    ArrayList<String> batchNames;
    ArrayList<String> sectionNames;
    ArrayList<String> startTimes;
    ArrayList<String> endTimes;
    String oldcoursename;
    String courseId;
    ShapeDrawable drawable;


    @Bind(R.id.b_cancel)
    Button cancel;

    @Bind(R.id.b_create)
    Button create;

    String profileId;
    String collegeId;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_course);
        ButterKnife.bind(this);
        create.setText("Update");
        Bundle extras = getIntent().getExtras();
        oldcoursename=extras.getString("courseName","");
        courseName.setText(oldcoursename);
        courseCode.setText(extras.getString("courseCode",""));
        courseProf.setText(extras.getString("prof"));
        courseSem.setText(extras.getString("sem",""));
        courseId = extras.getString("courseId","");
        if(extras.getString("e","").equals("1"))
        {
            elective.setChecked(true);
        }
        ShapeDrawable courseColor_circle = new ShapeDrawable( new OvalShape() );
        courseColor_circle.getPaint().setColor(Color.parseColor("#EE5451"));

        courseColorPicker.setBackground(courseColor_circle);
        courseColorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorPickerDialog = new ColorPickerDialog(EditCourseActivity.this);

                colorPickerDialog.show();

                Window window = colorPickerDialog.getWindow();
                window.setLayout(900, GridLayoutManager.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.CENTER_HORIZONTAL);
            }
        });

        try {
            dates = new ArrayList<>(extras.getStringArrayList("dates"));
            startTimes = new ArrayList<>(extras.getStringArrayList("startTimes"));
            endTimes = new ArrayList<>(extras.getStringArrayList("endTimes"));
            batchNames = new ArrayList<>(extras.getStringArrayList("batchNames"));
            branchNames = new ArrayList<>(extras.getStringArrayList("branchNames"));
            sectionNames = new ArrayList<>(extras.getStringArrayList("sectionNames"));
        }
        catch (Exception e)
        {
            Toast.makeText(this,"Oops! Something went wrong!",Toast.LENGTH_SHORT).show();
            finish();
        }
        String temp1 ="";
        for(String batch : batchNames)
        {
            temp1+=batch+",";
        }
        courseBatch.setText(temp1.substring(0, temp1.length()-1));
        temp1="";
        for(String branch : branchNames)
        {
            temp1+=branch+",";
        }
        courseBranch.setText(temp1.substring(0, temp1.length()-1));
        temp1="";
        for(String section: sectionNames)
        {
            temp1+=section+",";
        }
        courseSection.setText(temp1.substring(0, temp1.length()-1));

        Retrofit retrofit = new Retrofit.
                Builder()
                .baseUrl(MyApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyApi myApi = retrofit.create(MyApi.class);
        Call<ModelBranchList> call;


        SharedPreferences sharedPreferences = getSharedPreferences("CC", Context.MODE_PRIVATE);
        profileId = sharedPreferences.getString("profileId", "");
        collegeId = sharedPreferences.getString("collegeId", "");
        call = myApi.getBranches(collegeId);
        call.enqueue(new Callback<ModelBranchList>() {
            @Override
            public void onResponse(Call<ModelBranchList> call, Response<ModelBranchList> response) {
                final ModelBranchList modelBranchList = response.body();
                if (modelBranchList != null) {
                    courseBranch.setAdapter(new ArrayAdapter<String>(EditCourseActivity.this, android.R.layout.simple_list_item_1, modelBranchList.getBranchList()));
                    branches.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                String temp = "";
                                for (String branch : modelBranchList.getBranchList()) {
                                    temp += branch + ",";
                                }
                                temp = temp.substring(0, temp.lastIndexOf(","));
                                courseBranch.setText(temp);
                            } else {
                                courseBranch.setText(getSharedPreferences("CC",MODE_PRIVATE).getString("branchName"," "));
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ModelBranchList> call, Throwable t) {

            }

        });
        days.add(tbMonday);
        days.add(tbTuesday);
        days.add(tbWednesday);
        days.add(tbThursday);
        days.add(tbFriday);
        days.add(tbSaturday);
        events = (LinearLayout) findViewById(R.id.container_timetable_timings);
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
                    if (isChecked) {
                        View view = getLayoutInflater().inflate(R.layout.course_day, events, false);
                        ((TextView) view.findViewById(R.id.tv_day_title)).setText(buttonView.getText());
                        final EditText startTime = (EditText) view.findViewById(R.id.et_startTime);
                        final EditText endTime = (EditText) view.findViewById(R.id.et_endTime);
                        startTime.setFocusable(false);
                        endTime.setFocusable(false);
                        final TimePickerDialog startTimePickerDialog = new TimePickerDialog(EditCourseActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                Log.i("sw32time", minute+" :sw32minute");
                                if (minute < 10) {
                                    startTime.setText(hourOfDay + ":0" + minute);
                                    endTime.setText((hourOfDay+1) + ":0" + minute);
                                } else {
                                    startTime.setText(hourOfDay + ":" + minute);
                                    endTime.setText((hourOfDay+1) + ":" + minute);
                                }
                                if(hourOfDay <10) {
                                    startTime.setText("0"+startTime.getText().toString());
                                    endTime.setText("0" + endTime.getText().toString());
                                }



                            }
                        }, 8, 00, false);
                        final TimePickerDialog endTimePickerDialog = new TimePickerDialog(EditCourseActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                if (hourOfDay > Integer.parseInt(startTime.getText().toString().split(":")[0])) {
                                    Log.i("sw32time", minute+" :sw32minute");
                                    if (minute < 10) {
                                        endTime.setText(hourOfDay + ":0" + minute);
                                    }else
                                    {
                                        endTime.setText(hourOfDay + ":" + minute);
                                    }
                                    if(hourOfDay <10) endTime.setText("0"+endTime.getText().toString());
                                } else {
                                    Toast.makeText(EditCourseActivity.this, "End Time must be greater than start time", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, 9, 00, false);
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
                        if(events.getChildCount() == 0)
                        {
                            timings_title.setVisibility(View.VISIBLE);
                        }
                        events.addView(view);
                        days_selected.put(buttonView.getText().toString(), view);
                    } else {
                        events.removeView(days_selected.get(buttonView.getText().toString()));
                        if (events.getChildCount()==0)
                        {timings_title.setVisibility(View.GONE);}
                        days_selected.remove(buttonView.getText().toString());
                    }
                }
            });
        }
        int ii=0;
        for(String day : dates){
            int a = Integer.parseInt(day);
            ToggleButton temp = days.get(a-1);
            temp.setChecked(true);
            View v = days_selected.get(temp.getText().toString());
            EditText startTime = (EditText) v.findViewById(R.id.et_startTime);
            EditText endTime = (EditText) v.findViewById(R.id.et_endTime);
            startTime.setText(startTimes.get(ii));
            endTime.setText(endTimes.get(ii));
            ii++;
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCourse();
            }
        });


    }

    void updateCourse()
    {
        if(courseName.getText().toString().equals("")){courseName.setError("Enter Course Name");courseName.requestFocus();return;}
        if(courseCode.getText().toString().equals("")){courseCode.setError("Enter Course Code");courseCode.requestFocus();return;}
        if(courseProf.getText().toString().equals("")){courseProf.setError("Enter Course Professor");courseProf.requestFocus();return;}
        if(courseSem.getText().toString().equals("")){courseSem.setError("Enter Semester");courseSem.requestFocus();return;}
        if(courseBatch.getText().toString().equals("")){courseBatch.setError("Enter Batch");courseBatch.requestFocus();return;}
        if(courseBranch.getText().toString().equals("")){courseBranch.setError("Enter Branch");courseBranch.requestFocus();return;}
        if(days_selected.size()==0){Toast.makeText(EditCourseActivity.this,"Select appropriate times for this course",Toast.LENGTH_SHORT).show();return;}
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

        drawable = (ShapeDrawable)courseColorPicker.getBackground();

        MyApi myApi = retrofit.create(MyApi.class);
        int e;
        if(elective.isChecked()) e=1;
        else e=0;
        MyApi.editCourseRequest body = new MyApi.editCourseRequest(
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
                String.format("#%06X", (0xffffff & drawable.getPaint().getColor())),
                e+"",
                courseId
        );

        Call<ModelAddCourse> call = myApi.editCourse(body);
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        call.enqueue(new Callback<ModelAddCourse>() {
            @Override
            public void onResponse(Call<ModelAddCourse> call, Response<ModelAddCourse> response) {
                ModelAddCourse addCourse = response.body();
                Log.i("sw32",response.code()+"");
                FragmentCourses.courseNames.remove(oldcoursename);
                FragmentCourses.courseNames.add(courseName.getText().toString());
                try{
                    int index = FragmentCourses.courseIds.indexOf(courseId);
                    Log.i("sw32index", "indexed at : " + index);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                Intent data = new Intent();
                data.putExtra("courseColor",Color.parseColor(String.format("#%06X", (0xffffff & drawable.getPaint().getColor()))));
                data.putExtra("courseId",courseId);
                setResult(1,data);
                progressDialog.dismiss();
                finish();
            }

            @Override
            public void onFailure(Call<ModelAddCourse> call, Throwable t) {
                Log.i("sw32","addCourseFail");
            }
        });

    }

    public class ColorPickerDialog extends Dialog {

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

