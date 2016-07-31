package com.campusconnect.fragment.CoursePage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.campusconnect.CoursePageActivity;
import com.campusconnect.POJO.ModelTest;
import com.campusconnect.POJO.ModelTestList;
import com.campusconnect.POJO.MyApi;
import com.campusconnect.R;
import com.campusconnect.adapter.ExamsListAdapter;

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
public class FragmentExam extends Fragment {

    ImageView no_exam;
    RecyclerView exams_list;
    ExamsListAdapter mExamsAdapter;
    LinearLayoutManager mLayoutManager;
    ArrayList<ModelTest> mModelTests;
    Bundle fragArgs;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_exam, container, false);

        fragArgs = getArguments();

        no_exam = (ImageView) v.findViewById (R.id.iv_no_exam);
        exams_list = (RecyclerView) v.findViewById (R.id.rv_exams);
        mModelTests = new ArrayList<>();

        BitmapFactory.Options bm_opts = new BitmapFactory.Options();
        bm_opts.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.no_value_exams, bm_opts);
        no_exam.setImageBitmap(bitmap);

        //Setting the recyclerView
        mLayoutManager = new LinearLayoutManager(getActivity());
        mExamsAdapter = new ExamsListAdapter(v.getContext(), mModelTests, fragArgs.getInt("CourseColor"));
        exams_list.setLayoutManager(mLayoutManager);
        exams_list.setItemAnimator(new DefaultItemAnimator());
        exams_list.setAdapter(mExamsAdapter);

        Retrofit retrofit = new Retrofit.
                Builder()
                .baseUrl(MyApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyApi myApi = retrofit.create(MyApi.class);
        MyApi.getTestListRequest body = new MyApi.getTestListRequest(CoursePageActivity.courseId);
        Call<ModelTestList> call = myApi.getTestList(body);
        call.enqueue(new Callback<ModelTestList>() {
            @Override
            public void onResponse(Call<ModelTestList> call, Response<ModelTestList> response) {
                ModelTestList testList = response.body();
                if(testList!=null) {
                    List<ModelTest> modelTests = testList.getExamList();
                    for (ModelTest modelTest : modelTests) {
                        mExamsAdapter.add(modelTest);
                    }
                    if(modelTests.isEmpty())
                    {
                        no_exam.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        no_exam.setVisibility(View.GONE);
                    }
                }
                else
                {
                    Log.i("sw32exam","null");
                }
            }

            @Override
            public void onFailure(Call<ModelTestList> call, Throwable t) {
            }
        });


        return v;
    }
}
