package com.campusconnect.cc_reboot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.campusconnect.cc_reboot.fragment.Home.FragmentCourses;
import com.campusconnect.cc_reboot.slidingtab.SlidingTabLayout_home;
import com.campusconnect.cc_reboot.viewpager.ViewPagerAdapter_course;
import com.campusconnect.cc_reboot.POJO.*;

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
public class CoursePageActivity extends AppCompatActivity {

    @Bind(R.id.ib_sort)
    ImageButton sort_button;
    @Bind(R.id.ib_search)
    ImageButton search_button;
    @Bind(R.id.ib_notification)
    ImageButton notification_button;

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

    ViewPagerAdapter_course course_adapter;
    ViewPager course_pager;
    public static SlidingTabLayout_home course_tabs;
    CharSequence Titles[] = {"Notes", "Assignment", "Exam"};
    int Numboftabs = 3;
    static public String courseId;

    int defaultTabPosition=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        ButterKnife.bind(this);

        defaultTabPosition = getIntent().getIntExtra("TAB",0);
        courseId = getIntent().getStringExtra("courseId");

        course_pager = (ViewPager) findViewById(R.id.pager_course);
        course_tabs = (SlidingTabLayout_home) findViewById(R.id.tabs_course);
        course_adapter = new ViewPagerAdapter_course(getSupportFragmentManager(), Titles, Numboftabs, this);
        course_pager.setAdapter(course_adapter);
        course_pager.setCurrentItem(defaultTabPosition);
        course_tabs.setDistributeEvenly(true);
        course_tabs.setViewPager(course_pager);

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_temp = new Intent(v.getContext(), SearchActivity.class);
                startActivity(intent_temp);
            }
        });

        Retrofit retrofit = new Retrofit.
                Builder()
                .baseUrl(MyApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyApi myapi = retrofit.create(MyApi.class);

        MyApi.getCourseRequest getCourseRequest = new MyApi.getCourseRequest(FragmentCourses.profileId,courseId);

        Call<ModelCoursePage> call = myapi.getCourse(getCourseRequest);
        call.enqueue(new Callback<ModelCoursePage>() {
            @Override
            public void onResponse(Call<ModelCoursePage> call, Response<ModelCoursePage> response) {
                ModelCoursePage modelCoursePage = response.body();
                course_title.setText(modelCoursePage.getCourseName());
                course_details.setText(modelCoursePage.getDescription());
            }

            @Override
            public void onFailure(Call<ModelCoursePage> call, Throwable t) {

            }
        });
    }
}

