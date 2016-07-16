package com.campusconnect.cc_reboot;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.campusconnect.cc_reboot.POJO.CourseList;
import com.campusconnect.cc_reboot.POJO.Example;
import com.campusconnect.cc_reboot.POJO.ModelCourseSubscribe;
import com.campusconnect.cc_reboot.POJO.ModelSubscribe;
import com.campusconnect.cc_reboot.POJO.MyApi;
import com.campusconnect.cc_reboot.adapter.AssignmentsListAdapter;
import com.campusconnect.cc_reboot.adapter.CourseSelectionListAdapter;
import com.campusconnect.cc_reboot.fragment.Home.FragmentCourses;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by RK on 20/06/2016.
 */
public class SelectCourseActivity extends AppCompatActivity{

    ArrayList<CourseList> courses;
    ArrayList<String> subbed;
    @Bind(R.id.rv_select_course)
    RecyclerView select_course_list;

    @Bind(R.id.b_registration_done)
    Button reg_done_button;

    CourseSelectionListAdapter mCourseSelectionAdapter;
    LinearLayoutManager mLayoutManager;
    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_course);
        courses = new ArrayList<>();
        ButterKnife.bind(this);





        //Setting the recyclerView
        mLayoutManager = new LinearLayoutManager(this);
        mCourseSelectionAdapter = new CourseSelectionListAdapter(this,courses);
        select_course_list.setLayoutManager(mLayoutManager);
        select_course_list.setItemAnimator(new DefaultItemAnimator());
        select_course_list.setAdapter(mCourseSelectionAdapter);

        Retrofit retrofit = new Retrofit.
                Builder()
                .baseUrl(FragmentCourses.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        MyApi myApi = retrofit.create(MyApi.class);
        MyApi.getCoursesRequest body = new MyApi.getCoursesRequest(getSharedPreferences("CC", Context.MODE_PRIVATE).getString("profileId",""));
        Call<ModelCourseSubscribe> call = myApi.getCourses(body);
        call.enqueue(new Callback<ModelCourseSubscribe>() {
            @Override
            public void onResponse(Call<ModelCourseSubscribe> call, Response<ModelCourseSubscribe> response) {
                ModelCourseSubscribe courseSubscribe = response.body();
                if(courseSubscribe!=null)
                {
                    Log.i("sw32","here");
                    courses.addAll(courseSubscribe.getCourseList());
                    mCourseSelectionAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ModelCourseSubscribe> call, Throwable t) {

            }
        });
        reg_done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subbed = mCourseSelectionAdapter.getSubbed();
                Retrofit retrofit = new Retrofit.
                        Builder()
                        .baseUrl(FragmentCourses.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                MyApi myApi = retrofit.create(MyApi.class);
                MyApi.subscribeCourseRequest body = new MyApi.subscribeCourseRequest(getSharedPreferences("CC", Context.MODE_PRIVATE).getString("profileId",""),subbed);
                Call<ModelSubscribe> call = myApi.subscribeCourse(body);
                call.enqueue(new Callback<ModelSubscribe>() {
                    @Override
                    public void onResponse(Call<ModelSubscribe> call, Response<ModelSubscribe> response) {

                        Intent intent_temp = new Intent(getApplicationContext(), HomeActivity2.class);
                        intent_temp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent_temp);
                        Bundle params = new Bundle();
                        params. putString("course_subscribe","success");
                        firebaseAnalytics.logEvent("course_subscribe",params);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<ModelSubscribe> call, Throwable t) {

                    }
                });
            }
        });

    }
}

