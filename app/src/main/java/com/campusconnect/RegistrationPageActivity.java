package com.campusconnect;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.campusconnect.POJO.CollegeList;
import com.campusconnect.POJO.ModelCollegeList;
import com.campusconnect.POJO.ModelSignUp;
import com.campusconnect.POJO.MyApi;
import com.campusconnect.fragment.Home.FragmentCourses;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by RK on 20/06/2016.
 */
public class RegistrationPageActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {

    @Bind(R.id.b_continue_to_course_selection)
    Button continue_to_course_selection_button;

    @Bind(R.id.et_name)
    EditText profileName;

    @Bind(R.id.et_college_name)
    TextView collegeName;

    @Bind(R.id.et_batch)
    TextView batchName;

    @Bind(R.id.et_specialisation)
    TextView branchName;

    @Bind(R.id.et_section)
    AutoCompleteTextView sectionName;

    @Bind(R.id.iv_profile_picture)
    ImageView profilePicture;

    @Bind(R.id.sv_registration)
    ScrollView scrollViewReg;
    CollegeNotFoundDialog getdetailsDialog;
    String personName;
    String personEmail;
    String personPhoto;
    String personId;
    List<CollegeList> colleges;
    ArrayList<String> collegeNames;
    ArrayList<String> collegeIds;
    ArrayAdapter<String> data;
    String profileId;
    String collegeId;
    String collegeNameString;
    int pos_college_selection;
    private FirebaseAnalytics mFirebaseAnalytics;
    String branchNameString;
    int pos_branch_name_selection;
    public ArrayAdapter<String> branchNameList;
    LayoutInflater inflater;
    AlertDialog.Builder builderBatchList;
    BranchNotFoundDialog branchNotFoundDialog;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        collegeNames = new ArrayList<>();
        collegeIds = new ArrayList<>();
        inflater = this.getLayoutInflater();
        Bundle temp = getIntent().getExtras();
        personName = temp.getString("personName");
        personEmail = temp.getString("personEmail");
        personPhoto = temp.getString("personPhoto");
        personId = temp.getString("personId");
        ButterKnife.bind(this);
        profileName.setText(personName);
        profileName.setFocusable(false);
        Picasso.with(RegistrationPageActivity.this)
                .load(personPhoto)
                .error(R.mipmap.ccnoti)
                .fit()
                .into(profilePicture);
        collegeName.setHintTextColor(Color.BLACK);
        Retrofit retrofit = new Retrofit.
                Builder()
                .baseUrl(FragmentCourses.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.logEvent("sign_up_start", new Bundle());
        MyApi myApi = retrofit.create(MyApi.class);
        Call<ModelCollegeList> call = myApi.getCollegeList();
        call.enqueue(new Callback<ModelCollegeList>() {
            @Override
            public void onResponse(Call<ModelCollegeList> call, Response<ModelCollegeList> response) {
                ModelCollegeList collegeList = response.body();
                if (collegeList != null) {
                    colleges = collegeList.getCollegeList();
                    for (CollegeList college : colleges) {
                        collegeNames.add(college.getCollegeName());
                        collegeIds.add(college.getCollegeId());

                    }
                    data = new ArrayAdapter<String>(RegistrationPageActivity.this, android.R.layout.simple_list_item_1, collegeNames);
                    data.add("Request to add college");
//                    collegeName.setAdapter(data);

                }
            }

            @Override
            public void onFailure(Call<ModelCollegeList> call, Throwable t) {

            }
        });
        branchName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderBranchList = new AlertDialog.Builder(RegistrationPageActivity.this);
                builderBranchList.setTitle("Select Your Specisalisation");
                builderBranchList.setNegativeButton(
                        "cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builderBranchList.setPositiveButton("Request New Branch", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        branchNotFoundDialog = new BranchNotFoundDialog(RegistrationPageActivity.this);
                        Window window = branchNotFoundDialog.getWindow();
                        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        branchNotFoundDialog.show();
                    }
                });
                builderBranchList.setAdapter(branchNameList,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("onClick", branchNameList.toString());
                                branchNameString = branchNameList.getItem(which);
                                pos_branch_name_selection = which;
                                if (pos_branch_name_selection != branchNameList.getCount() - 1) ;
                                branchName.setText(branchNameString);
                            }
                        });
                String temp = collegeName.getText().toString();
                int index = collegeNames.indexOf(temp);
                if (index >= 0) {
                    branchNameList = new ArrayAdapter<String>(RegistrationPageActivity.this, android.R.layout.simple_list_item_1, colleges.get(index).getBranchNames());
                    builderBranchList.show();
                }
            }
        });


        branchName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    String temp = collegeName.getText().toString();
                    int index = collegeNames.indexOf(temp);
                    if (index < 0) {
                        collegeName.setError("Select a valid college name");
                        collegeName.requestFocus();
                        return;
                    }
                    branchNameList = new ArrayAdapter<String>(RegistrationPageActivity.this, android.R.layout.simple_list_item_1, colleges.get(index).getBranchNames());
                    //     branchName.setAdapter(new ArrayAdapter<String>(RegistrationPageActivity.this, android.R.layout.simple_list_item_1, colleges.get(index).getBranchNames()));
                }
            }
        });

        batchName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builderBatchList = new AlertDialog.Builder(RegistrationPageActivity.this);
                //   builderBatchList.setTitle("Select Your Batch");

                inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View dialogView = inflater.inflate(R.layout.custom_dialog_batch,null);
                builderBatchList.setView(dialogView);
                RadioGroup radioGroup = (RadioGroup) dialogView.findViewById(R.id.radio_group);
                //to hide the soft keyboard
                View v = RegistrationPageActivity.this.getCurrentFocus();
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                final AlertDialog alertDialog = builderBatchList.create();

                builderBatchList.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alertDialog.show();
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        switch (i) {
                            case R.id.btn_2020:
                                alertDialog.dismiss();
                                batchName.setText("2013");
                                break;
                            case R.id.btn_2019:
                                alertDialog.dismiss();
                                batchName.setText("2014");
                                break;

                            case R.id.btn_2018:
                                alertDialog.dismiss();
                                batchName.setText("2015");
                                break;
                            case R.id.btn_2017:
                                alertDialog.dismiss();
                                batchName.setText("2016");
                                break;

                            case R.id.btn_2016:
                                alertDialog.dismiss();
                                batchName.setText("2017");
                                break;
                            case R.id.btn_2015:
                                alertDialog.dismiss();
                                batchName.setText("2018");
                                break;

                            case R.id.btn_2014:
                                alertDialog.dismiss();
                                batchName.setText("2019");
                                break;

                            case R.id.btn_2013:
                                alertDialog.dismiss();
                                batchName.setText("2020");
                                break;

                        }
                    }
                });

            }
        });


        continue_to_course_selection_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String temp = collegeName.getText().toString();
                int index = collegeNames.indexOf(temp);
                if (index < 0) {
                    collegeName.setError("Select a valid college name");
                    collegeName.requestFocus();
                    return;
                }
                if (profileName.getText().toString().equals("")) {
                    profileName.setError("Enter Name");
                    profileName.requestFocus();
                    return;
                }
                if (collegeName.getText().toString().equals("")) {
                    collegeName.setError("Enter College Name");
                    collegeName.requestFocus();
                    return;
                }
                if (batchName.getText().toString().equals("")) {
                    batchName.setError("Enter Batch Name");
                    batchName.requestFocus();
                    return;
                }
                if (branchName.getText().toString().equals("")) {
                    branchName.setError("Enter Branch Name");
                    branchName.requestFocus();
                    return;
                }
                collegeId = collegeIds.get(index);
                new sign_up().execute();
                Bundle params = new Bundle();
                params.putString("sign_up", "success");
                mFirebaseAnalytics.logEvent("sign_up", params);
            }
        });

        scrollViewReg.setOnTouchListener(this);
        collegeName.setOnClickListener(this);
    }

    public void SignUp() {
        Retrofit retrofit = new Retrofit.
                Builder()
                .baseUrl(FragmentCourses.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyApi myApi = retrofit.create(MyApi.class);

        MyApi.getProfileIdRequest request;
        request = new MyApi.getProfileIdRequest(profileName.getText().toString(), collegeId, batchName.getText().toString(), branchName.getText().toString(), sectionName.getText().toString(), personPhoto, personEmail, FirebaseInstanceId.getInstance().getToken(),personId);
        Call<ModelSignUp> call = myApi.getProfileId(request);
        call.enqueue(new Callback<ModelSignUp>() {
            @Override
            public void onResponse(Call<ModelSignUp> call, Response<ModelSignUp> response) {
                ModelSignUp signUp = response.body();
                if (signUp != null) {
                    profileId = signUp.getKey();
                    SharedPreferences sharedPreferences = getSharedPreferences("CC", MODE_PRIVATE);
                    sharedPreferences
                            .edit()
                            .putString("profileId", profileId)
                            .putString("collegeId", collegeId)
                            .putString("personId", personId)
                            .putString("collegeName", collegeName.getText().toString())
                            .putString("batchName", batchName.getText().toString())
                            .putString("branchName", branchName.getText().toString())
                            .putString("sectionName", sectionName.getText().toString())
                            .putString("profileName", personName)
                            .putString("email", personEmail)
                            .putString("photourl", personPhoto)
                            .apply();
                    Intent intent_temp = new Intent(getApplicationContext(), SelectCourseActivity.class);
                    intent_temp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent_temp);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ModelSignUp> call, Throwable t) {

            }
        });

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
            case R.id.sv_registration:

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                break;
        }
        return false;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.et_college_name:
                AlertDialog.Builder builderCollegeList = new AlertDialog.Builder(RegistrationPageActivity.this);
                builderCollegeList.setTitle("Select your college");
                builderCollegeList.setNegativeButton(
                        "cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builderCollegeList.setAdapter(
                        data,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                collegeNameString = data.getItem(which);
                                pos_college_selection = which;
                                Log.d("regestraion page", "called");
                                if (pos_college_selection != data.getCount() - 1){
                                    collegeName.setText(collegeNameString);
                                    collegeId = collegeIds.get(collegeNames.indexOf(collegeNameString));
                                }

                                else {
                                    getdetailsDialog = new CollegeNotFoundDialog((Activity) RegistrationPageActivity.this);
                                    Window window = getdetailsDialog.getWindow();
                                    window.setLayout(450, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    getdetailsDialog.show();
                                }
                            }
                        });
                builderCollegeList.show();
                break;
        }

    }
    private void showProgressDialog() {
        if (progressDialog == null) {
           progressDialog = new ProgressDialog(RegistrationPageActivity.this);
           progressDialog.setMessage("Loading. Please wait...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
        }
       progressDialog.show();
    }
    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();

    }

    class sign_up extends AsyncTask<String, String, String> {
       // ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(RegistrationPageActivity.this);
           // progressDialog.show();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            SignUp();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
           // progressDialog.dismiss();
           dismissProgressDialog();
        }
    }

    public class CollegeNotFoundDialog extends Dialog {
        public Activity c;
        public Dialog d;
        Context context;
        @Bind(R.id.b_submit)
        Button submit;
        @Bind(R.id.et_name)
        EditText client_name;
        @Bind(R.id.et_college_name)
        EditText college_name;
        @Bind(R.id.et_email_id)
        EditText email_ID;
        @Bind(R.id.et_phone_no)
        EditText phone_no;
        @Bind(R.id.et_location)
        EditText location;

        public CollegeNotFoundDialog(Activity a) {
            super(a);
// TODO Auto-generated constructor stub
            this.c = a;
            this.context = context;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_get_college_details);
            ButterKnife.bind(this);
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("onClick", "clicked");
                    if (client_name.getText().toString().equals("")) {
                        client_name.setText("Enter Name");
                        client_name.requestFocus();
                        return;
                    }
                    if (college_name.getText().toString().equals("")) {
                        college_name.setText("Enter College Name");
                        college_name.requestFocus();
                        return;
                    }
                    if (isValidEmail(email_ID.getText().toString())) {
                        Log.d("email id ", "valid");
                    } else {
                        email_ID.setText("Enter a valid Email Id");
                        email_ID.requestFocus();
                    }
                    if (isValidMobile(phone_no.getText().toString())) {
                        Log.d("phone nos", "valid");
                    } else {
                        phone_no.setText("Enter a valid nos");
                        phone_no.requestFocus();
                    }
                    if (location.getText().toString().equals("")) {
                        location.setText("Enter a location");
                        phone_no.requestFocus();
                        return;
                    }

                    MyApi.addCollegeRequest body = new MyApi.addCollegeRequest(
                            client_name.getText().toString(),
                            college_name.getText().toString(),
                            email_ID.getText().toString(),
                            phone_no.getText().toString(),
                            location.getText().toString()
                    );
                    Retrofit retrofit = new Retrofit.
                            Builder()
                            .baseUrl(MyApi.BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    MyApi myApi = retrofit.create(MyApi.class);
                    Call<Void> call = myApi.addCollege(body);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Log.d("College details", "success");
                            getdetailsDialog.dismiss();
                            Toast.makeText(RegistrationPageActivity.this,"Your Request has been Sent",Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.d("College details", "failed");
                        }
                    });
                }
            });
        }

        private boolean isValidEmail(String Email) {
            return Patterns.EMAIL_ADDRESS.matcher(personEmail).matches();
        }

        private boolean isValidMobile(String mobileNos) {
            return Patterns.PHONE.matcher(mobileNos).matches();
        }

    }

    public class BranchNotFoundDialog extends Dialog {
        Activity c;
        Context context;
        @Bind(R.id.b_submit)
        Button submit;
        @Bind(R.id.et_branch)
        EditText branch_name;
        //String collegeId;

        public BranchNotFoundDialog(Activity a) {
            super(a);
// TODO Auto-generated constructor stub
            this.c = a;
            this.context = context;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_branch_not_found);

            ButterKnife.bind(this);
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Retrofit retrofit = new Retrofit.
                            Builder()
                            .baseUrl(MyApi.BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    MyApi myApi = retrofit.create(MyApi.class);
                    if (collegeId != null) {
                        MyApi.addBranchRequest body = new MyApi.addBranchRequest(collegeId,
                                branch_name.getText().toString()
                        );
                        Call<Void> call = myApi.addBranch(body);
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Log.d("success", "called");
                                Log.d("CollegeId",collegeId);
                                branchNotFoundDialog.dismiss();
                                Toast.makeText(RegistrationPageActivity.this, "Your Request has been Sent", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.d("failed", "called");
                            }
                        });


                    }
                }
            });

        }
    }
}


