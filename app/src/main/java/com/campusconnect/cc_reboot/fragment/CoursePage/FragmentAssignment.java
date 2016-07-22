package com.campusconnect.cc_reboot.fragment.CoursePage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.campusconnect.cc_reboot.CoursePageActivity;
import com.campusconnect.cc_reboot.POJO.AssList;
import com.campusconnect.cc_reboot.POJO.ModelAssignmentList;
import com.campusconnect.cc_reboot.POJO.MyApi;
import com.campusconnect.cc_reboot.R;
import com.campusconnect.cc_reboot.adapter.AssignmentsListAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by RK on 05/06/2016.
 */
public class FragmentAssignment extends Fragment {

    ImageView no_assignment;
    RecyclerView assignments_list;
    AssignmentsListAdapter mAssignmentsAdapter;
    LinearLayoutManager mLayoutManager;
    ArrayList<AssList> mAssignments;
    Bundle fragArgs;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_assignment, container, false);

        fragArgs = getArguments();

        no_assignment = (ImageView) v.findViewById (R.id.iv_no_assignment);
        assignments_list = (RecyclerView) v.findViewById (R.id.rv_assignments);
        mAssignments = new ArrayList<>();

        BitmapFactory.Options bm_opts = new BitmapFactory.Options();
        bm_opts.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.no_value_assignments, bm_opts);
        no_assignment.setImageBitmap(bitmap);

        //Setting the recyclerView
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAssignmentsAdapter = new AssignmentsListAdapter(v.getContext(),mAssignments, fragArgs.getInt("CourseColor"));
        assignments_list.setLayoutManager(mLayoutManager);
        assignments_list.setItemAnimator(new DefaultItemAnimator());
        assignments_list.setAdapter(mAssignmentsAdapter);

        Retrofit retrofit = new Retrofit.
                Builder()
                .baseUrl(MyApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyApi myApi = retrofit.create(MyApi.class);
        MyApi.getAssignmentListRequest body = new MyApi.getAssignmentListRequest(CoursePageActivity.courseId);
        Call<ModelAssignmentList> call = myApi.getAssignmentList(body);
        call.enqueue(new Callback<ModelAssignmentList>() {
            @Override
            public void onResponse(Call<ModelAssignmentList> call, Response<ModelAssignmentList> response) {
                ModelAssignmentList modelAssignmentList = response.body();
                if(modelAssignmentList!=null) {
                    List<AssList> modelAssignmentAssList = modelAssignmentList.getAssList();
                    for (AssList i : modelAssignmentAssList) {
                        mAssignmentsAdapter.add(i);
                    }
                }
            }

            @Override
            public void onFailure(Call<ModelAssignmentList> call, Throwable t) {

            }
        });

        return v;
    }
}
