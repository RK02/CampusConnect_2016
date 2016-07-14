package com.campusconnect.cc_reboot;

import android.app.ProgressDialog;
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

import com.campusconnect.cc_reboot.POJO.ModelTest;
import com.campusconnect.cc_reboot.POJO.MyApi;
import com.campusconnect.cc_reboot.fragment.Home.FragmentCourses;

import org.json.JSONException;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by RK on 04/06/2016.
 */
public class ExamPageActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.iv_exam)
    ImageView exam_last_page;

    @Bind(R.id.container_exam)
    RelativeLayout exam_container;

    @Bind(R.id.tv_exam_name)
    TextView testName;
    @Bind(R.id.tv_date_posted)
    TextView date;
    @Bind(R.id.tv_uploader)
    TextView uploader;
    @Bind(R.id.tv_description)
    TextView desc;
    @Bind(R.id.tv_views_count)
    TextView views;
    @Bind(R.id.tv_due_date)
    TextView due;

    @Bind(R.id.ib_edit)
    ImageButton edit_note_button;

    @Bind(R.id.ib_share)
    ImageButton share_note_button;

    @Bind(R.id.exam_remind)
    Button remind_button;

    private String testId;
    int courseColor;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        ButterKnife.bind(this);

        courseColor = getIntent().getIntExtra("CourseColor", Color.rgb(224,224,224));
        exam_container.setBackgroundColor(courseColor);



        //OnClickListeners
        edit_note_button.setOnClickListener(this);
        share_note_button.setOnClickListener(this);
        exam_last_page.setOnClickListener(this);
        remind_button.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.ib_edit_note:
                intent = new Intent(getApplicationContext(), EditNoteActivity.class);
                startActivity(intent);
                break;


            case R.id.ib_share:
                share_link();
                break;

            case R.id.iv_exam:
//                intent = new Intent(getApplicationContext(), NotesSliderActivity.class);
//                startActivity(intent);
                break;
            case R.id.tb_remind_me:

                break;

            default:
                break;
        }

    }
    void share_link()
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();
        BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .addContentMetadata("examId", testId);

        LinkProperties linkProperties = new LinkProperties()
                .setChannel("whatsapp")
                .setFeature("sharing")
                .addControlParameter("$desktop_url", "http://campusconnect-2016.herokuapp.com/exam?id=" + testId);

        final Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.setPackage("com.whatsapp");
        branchUniversalObject.generateShortUrl(this, linkProperties, new Branch.BranchLinkCreateListener() {
            @Override
            public void onLinkCreate(String url, BranchError error) {
                if (error == null) {
                    Log.i("MyApp", "got my Branch link to share: " + url);
                    sendIntent.putExtra(Intent.EXTRA_TEXT,url);
                    progressDialog.dismiss();
                    startActivityForResult(sendIntent,1);
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (Branch.isAutoDeepLinkLaunch(this)) {
            try {
                String autoDeeplinkedValue = Branch.getInstance().getLatestReferringParams().getString("examId");
                testId = autoDeeplinkedValue;
                Log.i("sw32Deep","Launched by Branch on auto deep linking!"
                        + "\n\n" + autoDeeplinkedValue);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
        {
            testId = getIntent().getStringExtra("testId");
        }
        Log.i("sw32test",testId);

        Retrofit retrofit = new Retrofit.
                Builder()
                .baseUrl(MyApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyApi myApi = retrofit.create(MyApi.class);
        MyApi.getTestRequest body = new MyApi.getTestRequest(testId, getSharedPreferences("CC", Context.MODE_PRIVATE).getString("profileId",""));
        Call<ModelTest> call = myApi.getTest(body);
        call.enqueue(new Callback<ModelTest>() {
            @Override
            public void onResponse(Call<ModelTest> call, Response<ModelTest> response) {
                ModelTest modelTest = response.body();
                testName.setText(modelTest.getExamTitle());
                desc.setText(modelTest.getExamDesc());
                uploader.setText(modelTest.getUploaderName());
                date.setText(modelTest.getLastUpdated());
                due.setText(modelTest.getDueDate());
                views.setText(modelTest.getViews());
            }

            @Override
            public void onFailure(Call<ModelTest> call, Throwable t) {

            }
        });

    }
}

