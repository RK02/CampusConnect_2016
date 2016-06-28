package com.campusconnect.cc_reboot;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
public class AssignmentPageActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.iv_assignment)
    ImageView assignment_last_page;

    @Bind(R.id.container_assignment)
    RelativeLayout assignments_container;

    @Bind(R.id.tv_assignment_name)
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

    @Bind(R.id.ib_edit_note)
    ImageButton edit_note_button;
    @Bind(R.id.ib_fullscreen)
    ImageButton fullscreen_button;
    @Bind(R.id.ib_share)
    ImageButton share_note_button;

    @Bind(R.id.tb_remind_me)
    Button remind_button;

    String assignmentId;
    int courseColor;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
        ButterKnife.bind(this);

        courseColor = getIntent().getIntExtra("CourseColor", Color.rgb(224,224,224));
        assignments_container.setBackgroundColor(courseColor);

        assignmentId = getIntent().getStringExtra("assignmentId");

        Retrofit retrofit = new Retrofit.
                Builder()
                .baseUrl(MyApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyApi myApi = retrofit.create(MyApi.class);
        MyApi.getAssignmentRequest body = new MyApi.getAssignmentRequest(assignmentId, getSharedPreferences("CC", Context.MODE_PRIVATE).getString("profileId",""));
        Call<ModelAssignment> call  = myApi.getAssignment(body);
        call.enqueue(new Callback<ModelAssignment>() {
            @Override
            public void onResponse(Call<ModelAssignment> call, Response<ModelAssignment> response) {

                ModelAssignment assignment = response.body();
                assignment_name.setText(assignment.getAssignmentTitle());
                uploader.setText(assignment.getUploaderName());
                date_posted.setText(assignment.getLastUpdated().substring(0,10));
                dueDate.setText(assignment.getDueDate());
                views.setText(assignment.getViews());
                description.setText(assignment.getAssignmentDesc());
            }

            @Override
            public void onFailure(Call<ModelAssignment> call, Throwable t) {

            }
        });

        //OnClickListeners
        edit_note_button.setOnClickListener(this);
        fullscreen_button.setOnClickListener(this);
        share_note_button.setOnClickListener(this);
        assignment_last_page.setOnClickListener(this);
        remind_button.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.ib_edit_note:
                intent = new Intent(getApplicationContext(), EditNoteActivity.class);
                startActivity(intent);
                break;

            case R.id.ib_fullscreen:
//                intent = new Intent(getApplicationContext(), NotesSliderActivity.class);
//                startActivity(intent);
                break;

            case R.id.ib_share:

                break;

            case R.id.iv_assignment:
//                intent = new Intent(getApplicationContext(), NotesSliderActivity.class);
//                startActivity(intent);
                break;

            case R.id.tb_remind_me:

                break;

            default:
                break;
        }

    }
}

