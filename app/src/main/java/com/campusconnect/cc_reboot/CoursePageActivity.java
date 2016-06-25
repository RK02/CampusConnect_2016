package com.campusconnect.cc_reboot;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.campusconnect.cc_reboot.fragment.Home.FragmentCourses;
import com.campusconnect.cc_reboot.slidingtab.SlidingTabLayout_home;
import com.campusconnect.cc_reboot.viewpager.ViewPagerAdapter_course;
import com.campusconnect.cc_reboot.POJO.*;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by RK on 04/06/2016.
 */
public class CoursePageActivity extends AppCompatActivity implements FloatingActionsMenu.OnFloatingActionsMenuUpdateListener, View.OnClickListener{

    @Bind(R.id.ib_sort)
    ImageButton sort_button;
    @Bind(R.id.ib_search)
    ImageButton search_button;
    @Bind(R.id.ib_notification)
    ImageButton notification_button;

    @Bind(R.id.tb_subscribe)
    ToggleButton subscribe_button;

    @Bind(R.id.tv_course_title)
    TextView course_title;
    @Bind(R.id.tv_course_details)
    TextView course_details;
    @Bind(R.id.tv_timetable)
    TextView course_timetable;
    @Bind(R.id.tv_prof_name)
    TextView course_prof;
    @Bind(R.id.tv_view_students)
    TextView course_view_students;

    @Bind(R.id.container_course_info)
    RelativeLayout course_info_container;

    @Bind(R.id.container_fab)
    FrameLayout fab_menu_container;

    @Bind(R.id.fab_menu)
    FloatingActionsMenu fabMenu;

    ViewPagerAdapter_course course_adapter;
    ViewPager course_pager;
    public static SlidingTabLayout_home course_tabs;
    CharSequence Titles[] = {"Notes", "Assignment", "Exam"};
    int Numboftabs = 3;

    static public String courseId;
    int courseColor;
    int defaultTabPosition=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        ButterKnife.bind(this);

        //Setting FAB container's background to be fully transparent by default
        fab_menu_container.getBackground().setAlpha(0);

        defaultTabPosition = getIntent().getIntExtra("TAB",0);
        courseId = getIntent().getStringExtra("courseId");
        courseColor = getIntent().getIntExtra("courseColor", Color.rgb(224,224,224));

        course_info_container.setBackgroundColor(courseColor);

        course_pager = (ViewPager) findViewById(R.id.pager_course);
        course_tabs = (SlidingTabLayout_home) findViewById(R.id.tabs_course);
        course_adapter = new ViewPagerAdapter_course(getSupportFragmentManager(), Titles, Numboftabs, courseColor, this);
        course_pager.setAdapter(course_adapter);
        course_pager.setCurrentItem(defaultTabPosition);
        course_tabs.setDistributeEvenly(true);
        course_tabs.setViewPager(course_pager);

        Retrofit retrofit = new Retrofit.
                Builder()
                .baseUrl(MyApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyApi myapi = retrofit.create(MyApi.class);

        MyApi.getCourseRequest getCourseRequest = new MyApi.getCourseRequest(getSharedPreferences("CC", Context.MODE_PRIVATE).getString("profileId",""),courseId);

        Call<ModelCoursePage> call = myapi.getCourse(getCourseRequest);
        call.enqueue(new Callback<ModelCoursePage>() {
            @Override
            public void onResponse(Call<ModelCoursePage> call, Response<ModelCoursePage> response) {
                ModelCoursePage modelCoursePage = response.body();
                if(modelCoursePage != null) {
                    course_title.setText(modelCoursePage.getCourseName());
                    course_prof.setText(modelCoursePage.getProfessorName());
                    course_details.setText(modelCoursePage.getDescription());
                }
            }

            @Override
            public void onFailure(Call<ModelCoursePage> call, Throwable t) {

            }
        });

        //Listener to define layouts for FAB expanded and collapsed modes
        fabMenu.setOnFloatingActionsMenuUpdateListener(this);

        //OnClickListeners
        sort_button.setOnClickListener(this);
        search_button.setOnClickListener(this);
        notification_button.setOnClickListener(this);
        subscribe_button.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.ib_sort:
                Intent intent_sort = new Intent(getApplicationContext(), SortActivity.class);
                startActivity(intent_sort);
                break;

            case R.id.ib_search:
                Intent intent_search = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent_search);
                break;

            case R.id.ib_notification:
                Intent intent_notification = new Intent(getApplicationContext(), NotificationActivity.class);
                startActivity(intent_notification);
                break;

            case R.id.tb_subscribe:

                break;

            default:
                break;
        }

    }

    //Layout definition when FAB is expanded
    @Override
    public void onMenuExpanded() {
        final String courseTitle = course_title.getText().toString();
        fabMenu.findViewById(R.id.fab_event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent exam = new Intent(CoursePageActivity.this,AddEventActivity.class);
                exam.putExtra("Mode",1);
                exam.putExtra("courseTitle",courseTitle);
                exam.putExtra("courseId",courseId);
                startActivity(exam);
                fabMenu.collapse();
            }
        });
        fabMenu.findViewById(R.id.fab_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent assignment = new Intent(CoursePageActivity.this,AddEventActivity.class);
                assignment.putExtra("Mode",2);
                assignment.putExtra("courseTitle",courseTitle);
                assignment.putExtra("courseId",courseId);
                startActivity(assignment);
                fabMenu.collapse();

            }
        });
        fabMenu.findViewById(R.id.fab_others).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notes = new Intent(CoursePageActivity.this,UploadPicturesActivity.class);
                notes.putExtra("courseTitle",courseTitle);
                notes.putExtra("courseId",courseId);
                startActivity(notes);
                fabMenu.collapse();

            }
        });
        fab_menu_container.getBackground().setAlpha(230);
        fab_menu_container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                fabMenu.collapse();
                return true;
            }
        });
    }

    //Layout definition when FAB is collapsed
    @Override
    public void onMenuCollapsed() {
        fab_menu_container.getBackground().setAlpha(0);
        fab_menu_container.setOnTouchListener(null);
    }
}

