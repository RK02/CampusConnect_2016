package com.campusconnect.cc_reboot.fragment.Home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.campusconnect.cc_reboot.HomeActivity;
import com.campusconnect.cc_reboot.POJO.*;

import com.campusconnect.cc_reboot.R;
import com.campusconnect.cc_reboot.adapter.CourseListAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by RK on 05/06/2016.
 */
public class FragmentCourses extends Fragment{

    RecyclerView course_list;
    CourseListAdapter mCourseAdapter;
    LinearLayoutManager mLayoutManager;
    Retrofit retrofit = new Retrofit.
            Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    MyApi myApi = retrofit.create(MyApi.class);
    Call<Example> call;
    public static final String BASE_URL = "https://uploadnotes-2016.appspot.com/_ah/api/notesapi/v1/";
    public static final String uploadURL = "https://uploadnotes-2016.appspot.com/img";
    public static final String django = "https://campusconnect-2016.herokuapp.com";
    public static  String profileName = "";
    public static  String profilePoints = "";
    public static ArrayList<SubscribedCourseList> sCourseList;
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_courses, container, false);

        course_list = (RecyclerView) v.findViewById (R.id.rv_courses);
        final ArrayList<SubscribedCourseList> courses = new ArrayList<>();
        //Setting the recyclerView
        mLayoutManager = new LinearLayoutManager(v.getContext());
        mCourseAdapter = new CourseListAdapter(v.getContext(),courses);
        course_list.setLayoutManager(mLayoutManager);
        course_list.setItemAnimator(new DefaultItemAnimator());
        course_list.setAdapter(mCourseAdapter);
                return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        sCourseList = new ArrayList<>();
        call= myApi.getFeed(getActivity().getSharedPreferences("CC", Context.MODE_PRIVATE).getString("profileId",""));
        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                Log.i("sw32",""+response.code());
                Example example = response.body();
                if(example!=null) {
                    mCourseAdapter.clear();
                    profileName = example.getProfileName();
                    profilePoints = example.getPoints();
                    List<AvailableCourseList> availableCourseList = example.getAvailableCourseList();
                     List<SubscribedCourseList> subscribedCourseList = example.getSubscribedCourseList();

                    for (SubscribedCourseList x : subscribedCourseList) {
                        sCourseList.add(x);
                        mCourseAdapter.add(x);
                    }
                }
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Log.i("sw32","fail");

            }
        });


    }
}