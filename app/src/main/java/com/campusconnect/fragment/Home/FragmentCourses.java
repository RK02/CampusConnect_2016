package com.campusconnect.fragment.Home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.campusconnect.CoursePageActivity;
import com.campusconnect.POJO.AvailableCourseList;
import com.campusconnect.POJO.ModelFeed;
import com.campusconnect.POJO.MyApi;
import com.campusconnect.POJO.SubscribedCourseList;
import com.campusconnect.R;
import com.campusconnect.adapter.CourseListAdapter;
import com.campusconnect.adapter.TimetableAdapter;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by RK on 05/06/2016.
 */
public class FragmentCourses extends Fragment{

    ImageView no_course_view;
    RecyclerView course_list;
    Boolean resumeHasRun = false;
    CourseListAdapter mCourseAdapter;
    LinearLayoutManager mLayoutManager;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout cell_container;
    View course_indicator;
    Retrofit retrofit = new Retrofit.
            Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    MyApi myApi;
    Call<ModelFeed> call;
    ConnectivityManager cm;
    RecyclerView fragment_courses;
    NetworkInfo activeNetwork;
    private FirebaseAnalytics mFirebaseAnalytics;
    boolean isConnected;
    public static final String BASE_URL = "https://uploadnotes-2016.appspot.com/_ah/api/notesapi/v1/";
    public static final String uploadURL = "https://uploadnotes-2016.appspot.com/img";
    public static final String django = "https://campusconnect-2016.herokuapp.com";
    //public static final String django = "http://10.75.133.109:8000";

    public static  String profileName = "";
    public static  String profilePoints = "";
    public static ArrayList<String> courseNames;
    public static ArrayList<String> courseIds;
    public static HashMap<String,ArrayList<String>> timeTableViews;

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_courses, container, false);
        myApi = retrofit.create(MyApi.class);
        fragment_courses = (RecyclerView) v.findViewById(R.id.rv_courses);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPage();
            }
        });
        courseNames = new ArrayList<>();
        courseIds = new ArrayList<>();
        no_course_view = (ImageView) v.findViewById(R.id.iv_no_course);
        course_list = (RecyclerView) v.findViewById (R.id.rv_courses);

        BitmapFactory.Options bm_opts = new BitmapFactory.Options();
        bm_opts.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.err_network_down, bm_opts);
        no_course_view.setImageBitmap(bitmap);

        ArrayList<SubscribedCourseList> courses = new ArrayList<>();
        timeTableViews = new HashMap<>();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
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
        Log.i("sw32call","create");
        if(isConnected) {
            swipeRefreshLayout.post(new Runnable() {
                @Override public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                    // directly call onRefresh() method
                    refreshPage();
                }
            });
        }else{
            resumePage();
            Toast.makeText(getActivity(),"Check your connection and try again",Toast.LENGTH_SHORT).show();
        }

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(!resumeHasRun)
        {
            resumeHasRun = true;
            return;
        }
        Log.i("sw32call","onresume");
        cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = cm.getActiveNetworkInfo();
        isConnected= activeNetwork != null && activeNetwork.isConnected();
        List<SubscribedCourseList> aa = SubscribedCourseList.listAll(SubscribedCourseList.class);
        if(!isConnected)
        {


        }
        else {

            if (aa.size() < courseIds.size()) {
                courseNames.clear();
                courseIds.clear();
                mCourseAdapter.clear();
                for (SubscribedCourseList x : aa) {
                    courseNames.add(x.getCourseName());
                    courseIds.add(x.getCourseId());
                    mCourseAdapter.add(x);
                    x.save();
                    FirebaseMessaging.getInstance().subscribeToTopic(x.getCourseId());
                }
            } else if (aa.size() > courseIds.size()) {
                Log.i("sw32onresume", aa.size() + " : " + courseIds.size());
                refreshPage();
            }
        }


    }

    void resumePage()
    {

        List<SubscribedCourseList> aa = SubscribedCourseList.listAll(SubscribedCourseList.class);
        courseNames.clear();
        courseIds.clear();
        mCourseAdapter.clear();
        for (SubscribedCourseList x : aa) {
            courseNames.add(x.getCourseName());
            courseIds.add(x.getCourseId());
            mCourseAdapter.add(x);
            x.save();
            FirebaseMessaging.getInstance().subscribeToTopic(x.getCourseId());
        }
    }

    void refreshPage(){

        isConnected= activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        Log.i("sw32","callonrefresh");
        if(isConnected){
            SubscribedCourseList.deleteAll(SubscribedCourseList.class);
            for(String key : timeTableViews.keySet())
            {
                ArrayList<String> viewIds = timeTableViews.get(key);
                for(String viewId : viewIds)
                {
                    LinearLayout a = ((LinearLayout)TimetableAdapter.itemView.findViewById(Integer.parseInt(viewId)));
                    if(a!=null) {
                        a.removeAllViews();
                        a.setBackgroundColor(Color.rgb(223, 223, 223));
                    }
                }
            }

        call= myApi.getFeed(getActivity().getSharedPreferences("CC", Context.MODE_PRIVATE).getString("profileId",""));
        call.enqueue(new Callback<ModelFeed>() {
            @Override
            public void onResponse(Call<ModelFeed> call, Response<ModelFeed> response) {
                ModelFeed modelFeed = response.body();
                new FragmentTimetable();
                if(response.code() == 503){
                    BitmapFactory.Options bm_opts = new BitmapFactory.Options();
                    bm_opts.inScaled = false;
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.error_five_zero_three, bm_opts);
                    no_course_view.setImageBitmap(bitmap);
                    no_course_view.setVisibility(View.VISIBLE);
                }
                if(modelFeed !=null) {
                    courseNames.clear();
                    courseIds.clear();
                    timeTableViews = new HashMap<>();
                    mCourseAdapter.clear();
                    profileName = modelFeed.getProfileName();
                    profilePoints = modelFeed.getPoints();
                    List<AvailableCourseList> availableCourseList = modelFeed.getAvailableCourseList();
                    List<SubscribedCourseList> subscribedCourseList = modelFeed.getSubscribedCourseList();
                    if(subscribedCourseList.isEmpty())
                    {
                        no_course_view.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        no_course_view.setVisibility(View.GONE);
                        fragment_courses.setBackgroundColor(getResources().getColor(R.color.ColorRecyclerBackground));
                    }
                    for (final SubscribedCourseList x : subscribedCourseList) {
                        courseNames.add(x.getCourseName());
                        courseIds.add(x.getCourseId());
                        mCourseAdapter.add(x);
                        x.save();
                        int i = x.getDate().size()-1;
                        while(i>=0) {
                            int start = Integer.parseInt(x.getStartTime().get(i).substring(0, 2));
                            int end = Integer.parseInt(x.getEndTime().get(i).substring(0, 2));
                            String date = x.getDate().get(i);
                            for (int ii = start; ii < end; ii++) {
                                View cell = LayoutInflater.from(getContext()).inflate(R.layout.timetable_cell_layout, cell_container, false);
                                if (cell != null){
                                    String viewId = date + "" + (ii - 6);
                                if (timeTableViews.containsKey(x.getCourseId())) {
                                    timeTableViews.get(x.getCourseId()).add(viewId);
                                } else {
                                    ArrayList<String> temp = new ArrayList<>();
                                    temp.add(viewId);
                                    timeTableViews.put(x.getCourseId(), temp);
                                }
                                cell_container = (LinearLayout) TimetableAdapter.itemView.findViewById(Integer.parseInt(viewId));
                                if (cell_container != null) {
                                    cell_container.setBackgroundColor(Color.WHITE);
                                    ((View) cell.findViewById(R.id.course_indicator)).setBackgroundColor(Color.parseColor(x.getColour()));
                                    ((TextView) cell.findViewById(R.id.cellText)).setText(x.getCourseCode());
                                    cell.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent coursePage = new Intent(getActivity(), CoursePageActivity.class);
                                            coursePage.putExtra("courseId", x.getCourseId());
                                            coursePage.putExtra("courseColor", Color.parseColor(x.getColour()));
                                            startActivity(coursePage);
                                        }
                                    });
                                    cell_container.removeAllViews();
                                    cell_container.addView(cell);
                                }
                            }
                            }
                            i--;
                        }
                        FirebaseMessaging.getInstance().subscribeToTopic(x.getCourseId());
                    }
                    Log.i("sw32",""+subscribedCourseList.size() + " : " + subscribedCourseList.isEmpty());

                    swipeRefreshLayout.setRefreshing(false);
                }

            }
            @Override
            public void onFailure(Call<ModelFeed> call, Throwable t) {
                Toast.makeText(getActivity(),"Oops! Something went wrong!",Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }else
        {
            Toast.makeText(getActivity(),"Check your connection and try again",Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}