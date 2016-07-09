package com.campusconnect.cc_reboot.fragment.Home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.Toast;

import com.campusconnect.cc_reboot.CoursePageActivity;
import com.campusconnect.cc_reboot.HomeActivity;
import com.campusconnect.cc_reboot.HomeActivity2;
import com.campusconnect.cc_reboot.POJO.*;

import com.campusconnect.cc_reboot.R;
import com.campusconnect.cc_reboot.adapter.CourseListAdapter;
import com.google.firebase.messaging.FirebaseMessaging;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
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
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout cell_container;
    Retrofit retrofit = new Retrofit.
            Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    MyApi myApi;
    Call<Example> call;
    ConnectivityManager cm;
    NetworkInfo activeNetwork;
    boolean isConnected;
    public static final String BASE_URL = "https://uploadnotes-2016.appspot.com/_ah/api/notesapi/v1/";
    public static final String uploadURL = "https://uploadnotes-2016.appspot.com/img";
    public static final String django = "https://campusconnect-2016.herokuapp.com";
    public static  String profileName = "";
    public static  String profilePoints = "";
    public static ArrayList<String> courseNames;
    public static ArrayList<String> courseIds;

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_courses, container, false);
        SubscribedCourseList a = new SubscribedCourseList();
        a.save();a.delete();
        myApi = retrofit.create(MyApi.class);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                refreshPage();
            }
        });
        swipeRefreshLayout.setRefreshing(true);



        courseNames = new ArrayList<>();
        courseIds = new ArrayList<>();
        course_list = (RecyclerView) v.findViewById (R.id.rv_courses);
        final ArrayList<SubscribedCourseList> courses = new ArrayList<>();
        //Setting the recyclerView

        mLayoutManager = new LinearLayoutManager(v.getContext());
        mCourseAdapter = new CourseListAdapter(v.getContext(),courses);
        course_list.setLayoutManager(mLayoutManager);
        course_list.setItemAnimator(new DefaultItemAnimator());
        course_list.setAdapter(mCourseAdapter);
        call= myApi.getFeed(getActivity().getSharedPreferences("CC", Context.MODE_PRIVATE).getString("profileId",""));
        cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork =  cm.getActiveNetworkInfo();
        isConnected= activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if(isConnected) {
            swipeRefreshLayout.setRefreshing(true);
            call.enqueue(new Callback<Example>() {
                @Override
                public void onResponse(Call<Example> call, Response<Example> response) {
                    Log.i("sw32", "" + response.code());
                    Example example = response.body();
                    if (example != null) {
                        mCourseAdapter.clear();
                        profileName = example.getProfileName();
                        profilePoints = example.getPoints();
                        Log.i("sw32","calloncreate");
                        List<AvailableCourseList> availableCourseList = example.getAvailableCourseList();
                        List<SubscribedCourseList> subscribedCourseList = example.getSubscribedCourseList();
                        for (final SubscribedCourseList x : subscribedCourseList) {
                            courseNames.add(x.getCourseName());
                            courseIds.add(x.getCourseId());
                            mCourseAdapter.add(x);
                            x.save();
                            int i = x.getDate().size()-1;
                            //new FragmentTimetable();
                            while(i>=0) {
                                View cell = LayoutInflater.from(getContext()).inflate(R.layout.timetable_cell_layout, cell_container, false);
                                cell_container = (LinearLayout) FragmentTimetable.v.findViewById(Integer.parseInt(x.getDate().get(i) + "" + (Integer.parseInt(x.getStartTime().get(i).substring(0, 2)) - 6)));
                                cell_container.setBackgroundColor(Color.parseColor(x.getColour()));
                                ((TextView)cell.findViewById(R.id.cellText)).setText(x.getCourseName());
                                cell.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent coursePage = new Intent(getActivity(), CoursePageActivity.class);
                                        coursePage.putExtra("courseId",x.getCourseId());
                                        coursePage.putExtra("courseColor",Color.parseColor(x.getColour()));
                                        startActivity(coursePage);
                                    }
                                });
                                cell_container.removeAllViews();
                                cell_container.addView(cell);
                                i--;
                            }
                            FirebaseMessaging.getInstance().subscribeToTopic(x.getCourseId());
                        }

                    }
                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<Example> call, Throwable t) {
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getActivity(),"Oops! Something went wrong!",Toast.LENGTH_SHORT).show();
                }

            });
        }else{
            Toast.makeText(getActivity(),"Check your connection and try again",Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        }

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = cm.getActiveNetworkInfo();
        isConnected= activeNetwork != null && activeNetwork.isConnected();

        List<SubscribedCourseList> aa = SubscribedCourseList.listAll(SubscribedCourseList.class);
        if(aa.size() > courseIds.size())
        {
            courseNames.clear();
            courseIds.clear();
            mCourseAdapter.clear();
            for (SubscribedCourseList x : aa) {
                courseNames.add(x.getCourseName());
                courseIds.add(x.getCourseId());
                mCourseAdapter.add(x);
                Log.i("sw32cache",x.getCourseName());
                x.save();
                FirebaseMessaging.getInstance().subscribeToTopic(x.getCourseId());
            }
        }
        else if (aa.size() < courseIds.size()){swipeRefreshLayout.setRefreshing(true);refreshPage();}


    }

    void refreshPage(){
        cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = cm.getActiveNetworkInfo();
        isConnected= activeNetwork != null && activeNetwork.isConnected();
        //clearTimetable();
        Log.i("sw32","callonrefresh");
        if(isConnected){
        courseNames.clear();
        courseIds.clear();
        mCourseAdapter.clear();
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
                    for (final SubscribedCourseList x : subscribedCourseList) {
                        courseNames.add(x.getCourseName());
                        courseIds.add(x.getCourseId());
                        mCourseAdapter.add(x);
                        x.save();
                        int i = x.getDate().size()-1;
                        while(i>=0) {
                            View cell = LayoutInflater.from(getContext()).inflate(R.layout.timetable_cell_layout, cell_container, false);
                            cell_container = (LinearLayout) FragmentTimetable.v.findViewById(Integer.parseInt(x.getDate().get(i) + "" + (Integer.parseInt(x.getStartTime().get(i).substring(0, 2)) - 6)));
                            cell_container.setBackgroundColor(Color.parseColor(x.getColour()));
                            ((TextView)cell.findViewById(R.id.cellText)).setText(x.getCourseName());
                            cell.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent coursePage = new Intent(getActivity(), CoursePageActivity.class);
                                    coursePage.putExtra("courseId",x.getCourseId());
                                    coursePage.putExtra("courseColor",Color.parseColor(x.getColour()));
                                    startActivity(coursePage);
                                }
                            });
                            cell_container.removeAllViews();
                            cell_container.addView(cell);
                            i--;
                        }
                        FirebaseMessaging.getInstance().subscribeToTopic(x.getCourseId());
                    }
                    swipeRefreshLayout.setRefreshing(false);
                }

            }
            @Override
            public void onFailure(Call<Example> call, Throwable t) {
            }
        });

    }else
        {
            Toast.makeText(getActivity(),"Check your connection and try again",Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}