package com.campusconnect.cc_reboot;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.campusconnect.cc_reboot.POJO.CollegeList;
import com.campusconnect.cc_reboot.POJO.ModelCollegeList;
import com.campusconnect.cc_reboot.POJO.ModelEditProfile;
import com.campusconnect.cc_reboot.POJO.ModelSignUp;
import com.campusconnect.cc_reboot.POJO.MyApi;
import com.campusconnect.cc_reboot.fragment.Home.FragmentCourses;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by RK on 04/06/2016.
 */
public class EditProfileActivity extends AppCompatActivity {

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
    EditText sectionName;

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
    File newProfilePicture;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);
        collegeNames = new ArrayList<>();
        collegeIds= new ArrayList<>();
        personName = getSharedPreferences("CC",MODE_PRIVATE).getString("profileName","");
        personEmail = getSharedPreferences("CC",MODE_PRIVATE).getString("email","");
        personPhoto = getSharedPreferences("CC",MODE_PRIVATE).getString("photourl","");
        personId = getSharedPreferences("CC",MODE_PRIVATE).getString("personId","");
        collegeName.setText(getSharedPreferences("CC",MODE_PRIVATE).getString("collegeName",""));
        branchName.setText(getSharedPreferences("CC",MODE_PRIVATE).getString("branchName",""));
        batchName.setText(getSharedPreferences("CC",MODE_PRIVATE).getString("batchName",""));
        sectionName.setText(getSharedPreferences("CC",MODE_PRIVATE).getString("sectionName",""));
        collegeName.setFocusable(false);
        profileName.setText(personName);
        profileName.setFocusable(false);
        Picasso.with(EditProfileActivity.this)
                .load(personPhoto)
                .fit()
                .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                .into(profilePicture);
        Retrofit retrofit = new Retrofit.
                Builder()
                .baseUrl(FragmentCourses.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
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
                    ArrayAdapter<String> data = new ArrayAdapter<>(EditProfileActivity.this,android.R.layout.simple_list_item_1,collegeNames);
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
                    branchName.setAdapter(new ArrayAdapter<String>(EditProfileActivity.this, android.R.layout.simple_list_item_1, colleges.get(index).getBranchNames()));
                }
            }
        });
        continue_to_course_selection_button.setText("Done");
        continue_to_course_selection_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = collegeName.getText().toString();
                int index = collegeNames.indexOf(temp);
                if(index < 0 ){collegeName.setError("Select a valid college name");collegeName.requestFocus();return; }
                collegeId = collegeIds.get(index);
                new sign_up().execute();
            }
        });
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 69);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 69:{
                if(data!=null) {
                    uri = data.getData();
                    Picasso.with(EditProfileActivity.this)
                            .load(uri.toString())
                            .fit()
                            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                            .into(profilePicture);
                    break;
                }
            }
        }
    }

    public void SignUp()
    {
        if(uri!=null) {
            newProfilePicture = new File(uri.getPath());
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody;
            MultipartBody.Builder body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("profileId", getSharedPreferences("CC", MODE_PRIVATE).getString("profileId", ""));
            Bitmap original = null;
            try {
                original = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int size = original.getRowBytes() * original.getHeight();
            Log.i("sw32size", size + "");
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(getFilesDir() + "/temp" + "profile" + ".jpeg");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (size > 10000000)
                original.compress(Bitmap.CompressFormat.JPEG, 20, out);
            else
                original.compress(Bitmap.CompressFormat.JPEG, 50, out);
            body.addFormDataPart("file", "profile.jpg", RequestBody.create(MediaType.parse("image/*"), new File(getFilesDir() + "/temp" + "profile" + ".jpeg")));


            requestBody = body.build();
            Request request1 = new Request.Builder()
                    .url("https://uploadnotes-2016.appspot.com/changePic")
                    .post(requestBody)
                    .build();
            try {
                okhttp3.Response response = client.newCall(request1).execute();
                String url = response.body().string();
                Log.i("sw32",url);
                JSONObject json = new JSONObject(url);
                personPhoto = json.getString("url");
                Log.i("sw32",personPhoto);
                getSharedPreferences("CC",MODE_PRIVATE).edit().putString("photourl",personPhoto).commit();
                Log.i("sw32",getSharedPreferences("CC",MODE_PRIVATE).getString("photourl","fakedesu"));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Retrofit retrofit = new Retrofit.
                Builder()
                .baseUrl(FragmentCourses.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyApi myApi = retrofit.create(MyApi.class);

        MyApi.editProfileRequest request;
        request= new MyApi.editProfileRequest(getSharedPreferences("CC",MODE_PRIVATE).getString("profileId",""),
                profileName.getText().toString(),
                batchName.getText().toString(),
                branchName.getText().toString(),
                sectionName.getText().toString(),
                getSharedPreferences("CC",MODE_PRIVATE).getString("photourl","fakedesu"));



        Call<ModelEditProfile> call = myApi.editProfile(request);
        call.enqueue(new Callback<ModelEditProfile>() {
            @Override
            public void onResponse(Call<ModelEditProfile> call, Response<ModelEditProfile> response) {
//                new mobile_register().execute(personId,
//                        profileId,
//                        batchName.getText().toString(),
//                        branchName.getText().toString(),
//                        sectionName.getText().toString(),
//                        collegeName.getText().toString());
                Toast.makeText(EditProfileActivity.this,"Success!",Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPreferences = getSharedPreferences("CC",MODE_PRIVATE);
                sharedPreferences
                        .edit()
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
                finish();
            }

            @Override
            public void onFailure(Call<ModelEditProfile> call, Throwable t) {

            }
        });

    }

    class sign_up extends AsyncTask<String,String,String>
    {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(EditProfileActivity.this);
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
            progressDialog = new ProgressDialog(EditProfileActivity.this);
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
                url = new URL(FragmentCourses.django+"/mobile_update");
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

