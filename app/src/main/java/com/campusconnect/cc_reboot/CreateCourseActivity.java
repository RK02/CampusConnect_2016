package com.campusconnect.cc_reboot;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;
import java.util.ArrayList;
import java.util.HashMap;

public class CreateCourseActivity extends Activity{

    ArrayList<ToggleButton> days = new ArrayList<>();
    String[] daysOfTheWeek = {"Mon","Tue","Wed","Thu","Fri","Sat"};
    HashMap<Integer, View> testing = new HashMap<>();
    LinearLayout events;
    Button done;
    Bundle bundle;
    private int DAY_OF_WEEK;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);
        done = (Button) findViewById(R.id.done);
        days.add((ToggleButton) findViewById(R.id.mon));
        days.add((ToggleButton) findViewById(R.id.tue));
        days.add((ToggleButton) findViewById(R.id.wed));
        days.add((ToggleButton) findViewById(R.id.thu));
        days.add((ToggleButton) findViewById(R.id.fri));
        days.add((ToggleButton) findViewById(R.id.sat));
        events = (LinearLayout) findViewById(R.id.list_events);
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
                        View view = getLayoutInflater().inflate(R.layout.day_event,events,false);
                        ((TextView)view.findViewById(R.id.dayLabel)).setText(buttonView.getText());
                        final EditText startTime = (EditText) view.findViewById(R.id.startTime);
                        final EditText endTime = (EditText) view.findViewById(R.id.endTime);
                        startTime.setFocusable(false);endTime.setFocusable(false);
                        final TimePickerDialog startTimePickerDialog = new TimePickerDialog(CreateCourseActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                if (minute < 10) {
                                    startTime.setText(hourOfDay + ":0" + minute);
                                } else {
                                    startTime.setText(hourOfDay + ":" + minute);
                                }


                            }
                        },8,00,false);
                        final TimePickerDialog endTimePickerDialog = new TimePickerDialog(CreateCourseActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
                                    Toast.makeText(CreateCourseActivity.this,"Please Select a Valid Time",Toast.LENGTH_SHORT).show();
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
                        testing.put(buttonView.getId(),view);
                    }
                    else
                    {
                        events.removeView(testing.get(buttonView.getId()));
                        testing.remove(buttonView.getId());
                    }
                }
            });
        }

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> startTime= new ArrayList<>();
                ArrayList<String> endTime = new ArrayList<>();
                ArrayList<Integer> days = new ArrayList<>();

                if (!testing.keySet().isEmpty()){
                    for(Integer x : testing.keySet()){
                        View temp = testing.get(x);
                        String day =  ((TextView)temp.findViewById(R.id.dayLabel)).getText().toString();
                        switch(day){
                            case("Sun"):DAY_OF_WEEK =1;break;
                            case("Mon"):DAY_OF_WEEK =2;break;
                            case("Tue"):DAY_OF_WEEK =3;break;
                            case("Wed"):DAY_OF_WEEK =4;break;
                            case("Thu"):DAY_OF_WEEK =5;break;
                            case("Fri"):DAY_OF_WEEK =6;break;
                            case("Sat"):DAY_OF_WEEK =7;break;
                        }
                        startTime.add(((EditText)temp.findViewById(R.id.startTime)).getText().toString());
                        endTime.add(((EditText)temp.findViewById(R.id.endTime)).getText().toString());
                        days.add(DAY_OF_WEEK);
                    }
                    Intent i = new Intent();
                    i.putExtra("startTimes",startTime);
                    i.putExtra("endTimes",endTime);
                    i.putExtra("days",days);
                    i.putExtra("subjectName",((EditText)findViewById(R.id.input_subject)).getText().toString());
                    i.putExtra("abbreviation",((EditText)findViewById(R.id.input_abrv)).getText().toString());
                    i.putExtra("place",((EditText)findViewById(R.id.input_place)).getText().toString());
                    i.putExtra("type",((EditText)findViewById(R.id.input_type)).getText().toString());
                    i.putExtra("teacher",((EditText)findViewById(R.id.input_teacher)).getText().toString());
                    setResult(200,i);
                    finish();
                }
            }
        });
    }
}
