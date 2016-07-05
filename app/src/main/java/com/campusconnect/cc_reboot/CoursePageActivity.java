package com.campusconnect.cc_reboot;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
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
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;

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
    static public String courseTitle;
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
                    courseTitle = course_title.getText().toString();
                    course_prof.setText(modelCoursePage.getProfessorName());
                    course_details.setText(modelCoursePage.getDescription());
                    if(modelCoursePage.getIsSubscribed().equals("1")) subscribe_button.setChecked(true);
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
                if(subscribe_button.isChecked())
                {
                    Retrofit retrofit = new Retrofit.
                            Builder()
                            .baseUrl(MyApi.BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    MyApi myApi = retrofit.create(MyApi.class);
                    MyApi.subscribeCourseRequest body = new MyApi.subscribeCourseRequest(getSharedPreferences("CC", Context.MODE_PRIVATE).getString("profileId",""),new String[]{courseId});
                    Call<ModelSubscribe> call = myApi.subscribeCourse(body);
                    call.enqueue(new Callback<ModelSubscribe>() {
                        @Override
                        public void onResponse(Call<ModelSubscribe> call, Response<ModelSubscribe> response) {
                            finish();
                            FirebaseMessaging.getInstance().subscribeToTopic(courseId);
                        }

                        @Override
                        public void onFailure(Call<ModelSubscribe> call, Throwable t) {

                        }
                    });
                }
                else
                {

                    new unsub().execute();
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(courseId);

                }

                break;

            default:
                break;
        }

    }
    class unsub extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection;
            try {
                URL url = new URL(MyApi.BASE_URL+"unsubscribeCourse");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.connect();
                DataOutputStream os = new DataOutputStream(connection.getOutputStream());
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("profileId",getSharedPreferences("CC",MODE_PRIVATE).getString("profileId",""));
                jsonObject.put("courseId",courseId);
                SubscribedCourseList.find(SubscribedCourseList.class,"course_id = ?",courseId).get(0).delete();
                os.write(jsonObject.toString().getBytes());
                os.flush();
                os.close();
                Log.i("sw32",connection.getResponseMessage() +":" +connection.getResponseCode());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            finish();
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

