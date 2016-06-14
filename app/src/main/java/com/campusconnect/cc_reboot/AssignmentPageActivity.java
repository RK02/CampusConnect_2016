package com.campusconnect.cc_reboot;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.campusconnect.cc_reboot.POJO.ModelAssignment;
import com.campusconnect.cc_reboot.POJO.MyApi;
import com.campusconnect.cc_reboot.fragment.Home.FragmentCourses;

import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by RK on 04/06/2016.
 */
public class AssignmentPageActivity extends AppCompatActivity {

    String assignmentId;
    @Bind(R.id.assignment_name)
    TextView assignment_name;
    @Bind(R.id.tv_uploader)
    TextView uploader;
    @Bind(R.id.tv_date_posted)
    TextView date_posted;
    @Bind(R.id.tv_description)
    TextView description;
    @Bind(R.id.tv_views_count)
    TextView views;
    @Bind(R.id.tv_due_date)
    TextView dueDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
        assignmentId = getIntent().getStringExtra("assignmentId");
        ButterKnife.bind(this);
        Retrofit retrofit = new Retrofit.
                Builder()
                .baseUrl(MyApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyApi myApi = retrofit.create(MyApi.class);
        MyApi.getAssignmentRequest body = new MyApi.getAssignmentRequest(assignmentId, FragmentCourses.profileId);
        Call<ModelAssignment> call  = myApi.getAssignment(body);
        call.enqueue(new Callback<ModelAssignment>() {
            @Override
            public void onResponse(Call<ModelAssignment> call, Response<ModelAssignment> response) {

                ModelAssignment assignment = response.body();
                assignment_name.setText(assignment.getAssignmentTitle());
                uploader.setText(assignment.getUploaderName());
                date_posted.setText(assignment.getLastUpdated());
                dueDate.setText(assignment.getDueDate());
                views.setText(assignment.getViews());
                description.setText(assignment.getAssignmentDesc());
            }

            @Override
            public void onFailure(Call<ModelAssignment> call, Throwable t) {

            }
        });


    }
}

