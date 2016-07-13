package com.campusconnect.cc_reboot;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.campusconnect.cc_reboot.POJO.CollegeList;
import com.campusconnect.cc_reboot.POJO.Example;
import com.campusconnect.cc_reboot.POJO.ModelCollegeList;
import com.campusconnect.cc_reboot.POJO.ModelSignUp;
import com.campusconnect.cc_reboot.POJO.MyApi;
import com.campusconnect.cc_reboot.fragment.Home.FragmentCourses;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
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
public class RegistrationPageActivity extends AppCompatActivity{

    @Bind(R.id.b_continue_to_course_selection)
    Button continue_to_course_selection_button;

    @Bind(R.id.et_name)
    EditText profileName;

    @Bind(R.id.et_college_name)
    AutoCompleteTextView collegeName;

    @Bind(R.id.et_batch)
    EditText batchName;

    @Bind(R.id.et_specialisation)
    AutoCompleteTextView branchName;

    @Bind(R.id.et_section)
    AutoCompleteTextView sectionName;

    @Bind(R.id.iv_profile_picture)
    ImageView profilePicture;

    String personName;
    String personEmail;
    String personPhoto;
    String personId;
    List<CollegeList> colleges;
    ArrayList<String> collegeNames;
    ArrayList<String> collegeIds;
    String profileId;
    String collegeId;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        collegeNames = new ArrayList<>();
        collegeIds= new ArrayList<>();
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
       // mFirebaseAnalytics.logEvent();
        MyApi myApi = retrofit.create(MyApi.class);
        Call<ModelCollegeList> call = myApi.getCollegeList();
        call.enqueue(new Callback<ModelCollegeList>() {
            @Override
            public void onResponse(Call<ModelCollegeList> call, Response<ModelCollegeList> response) {
                ModelCollegeList collegeList = response.body();
                if (collegeList != null) {
                    colleges = collegeList.getCollegeList();
                    for(CollegeList college : colleges)
                    {
                        collegeNames.add(college.getCollegeName());
                        collegeIds.add(college.getCollegeId());

                    }
                    ArrayAdapter<String> data = new ArrayAdapter<String>(RegistrationPageActivity.this,android.R.layout.simple_list_item_1,collegeNames);
                    collegeName.setAdapter(data);
                }
            }

            @Override
            public void onFailure(Call<ModelCollegeList> call, Throwable t) {

            }
        });
        branchName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                                @Override
                                                public void onFocusChange(View v, boolean hasFocus) {
                                                    if (hasFocus) {
                                                        String temp = collegeName.getText().toString();
                                                        int index = collegeNames.indexOf(temp);
                                                        if(index < 0 ) {collegeName.setError("Select a valid college name");collegeName.requestFocus(); return;}
                                                        branchName.setAdapter(new ArrayAdapter<String>(RegistrationPageActivity.this, android.R.layout.simple_list_item_1, colleges.get(index).getBranchNames()));
                                                    }
                                                }
                                            });

        continue_to_course_selection_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = collegeName.getText().toString();
                int index = collegeNames.indexOf(temp);
                if(index < 0 ){collegeName.setError("Select a valid college name");collegeName.requestFocus();return; }
                if(profileName.getText().toString().equals("")){profileName.setError("Enter Name");profileName.requestFocus();return;}
                if(collegeName.getText().toString().equals("")){collegeName.setError("Enter College Name");collegeName.requestFocus();return;}
                if(batchName.getText().toString().equals("")){batchName.setError("Enter Batch Name");batchName.requestFocus();return;}
                if(branchName.getText().toString().equals("")){branchName.setError("Enter Branch Name");branchName.requestFocus();return;}
                collegeId = collegeIds.get(index);
                new sign_up().execute();
            }
        });
    }
    public void SignUp()
    {
        Retrofit retrofit = new Retrofit.
                Builder()
                .baseUrl(FragmentCourses.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyApi myApi = retrofit.create(MyApi.class);

        MyApi.getProfileIdRequest request;
        request= new MyApi.getProfileIdRequest(profileName.getText().toString(),collegeId,batchName.getText().toString(),branchName.getText().toString(),sectionName.getText().toString(),personPhoto,personEmail,FirebaseInstanceId.getInstance().getToken());
        Call<ModelSignUp> call = myApi.getProfileId(request);
        call.enqueue(new Callback<ModelSignUp>() {
            @Override
            public void onResponse(Call<ModelSignUp> call, Response<ModelSignUp> response) {
                ModelSignUp signUp = response.body();
                if(signUp!=null)
                {
                    profileId = signUp.getKey();
                    new mobile_register().execute(personId,profileId,batchName.getText().toString(),branchName.getText().toString(),sectionName.getText().toString(),collegeName.getText().toString());
                    SharedPreferences sharedPreferences = getSharedPreferences("CC",MODE_PRIVATE);
                    sharedPreferences
                            .edit()
                            .putString("profileId",profileId)
                            .putString("collegeId",collegeId)
                            .putString("personId",personId)
                            .putString("collegeName",collegeName.getText().toString())
                            .putString("batchName",batchName.getText().toString())
                            .putString("branchName",branchName.getText().toString())
                            .putString("sectionName",sectionName.getText().toString())
                            .putString("profileName",personName)
                            .putString("email",personEmail)
                            .putString("photourl",personPhoto)
                            .apply();
                    Intent intent_temp = new Intent(getApplicationContext(), SelectCourseActivity.class);
                    intent_temp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent_temp);
                }
            }

            @Override
            public void onFailure(Call<ModelSignUp> call, Throwable t) {

            }
        });

    }

    class sign_up extends AsyncTask<String,String,String>
    {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(RegistrationPageActivity.this);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            SignUp();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
        }
    }




    class mobile_register extends AsyncTask<String,String,String>{

        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(profileName.getText().toString().equals("")){profileName.setError("Enter Name");profileName.requestFocus();return;}
            if(collegeName.getText().toString().equals("")){collegeName.setError("Enter College Name");collegeName.requestFocus();return;}
            if(batchName.getText().toString().equals("")){batchName.setError("Enter Batch Name");batchName.requestFocus();return;}
            if(branchName.getText().toString().equals("")){branchName.setError("Enter Branch Name");branchName.requestFocus();return;}
            progressDialog = new ProgressDialog(RegistrationPageActivity.this);
            progressDialog.show();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
        }
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection;
            URL url;
            JSONObject jsonObject = new JSONObject();
            String response;

            try {
                url = new URL(FragmentCourses.django+"/mobile_sign_up");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.connect();
                DataOutputStream os = new DataOutputStream(connection.getOutputStream());
                jsonObject.put("gprofileId",params[0]);
                jsonObject.put("profileId",params[1]);
                jsonObject.put("profileName",personName);
                jsonObject.put("collegeName",params[5]);
                jsonObject.put("collegeId",collegeId);
                jsonObject.put("branchName",params[3]);
                jsonObject.put("sectionName",params[4]);
                jsonObject.put("batchName",params[2]);
                jsonObject.put("imageUrl",personPhoto);
                jsonObject.put("email",personEmail);
                jsonObject.put("gcmId", FirebaseInstanceId.getInstance().getToken());
                Log.i("sw32",params[0] +":"+params[1]+":"+personName );
                os.write(jsonObject.toString().getBytes());
                os.flush();
                os.close();
                InputStream is = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                response = sb.toString();
                br.close();
                return response;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

