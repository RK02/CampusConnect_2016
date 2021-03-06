package com.campusconnect;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.campusconnect.POJO.ModelEditProfile;
import com.campusconnect.POJO.MyApi;
import com.campusconnect.fragment.Home.FragmentCourses;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
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
public class EditProfileActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener{

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
    EditText sectionName;

    @Bind(R.id.iv_profile_picture)
    ImageView profilePicture;

    @Bind(R.id.sv_registration)
    ScrollView scrollViewReg;
    BranchNotFoundDialog branchNotFoundDialog;
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
    File newProfilePicture;
    Uri uri;

    String collegeNameString;
    int pos_college_selection;
    String branchNameString;
    int pos_branch_name_selection;
    AlertDialog.Builder  builderBatchList;
    LayoutInflater inflater;
   public ArrayAdapter<String> branchNameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);

        collegeNames = new ArrayList<>();
        collegeIds = new ArrayList<>();
        personName = getSharedPreferences("CC", MODE_PRIVATE).getString("profileName", "");
        personEmail = getSharedPreferences("CC", MODE_PRIVATE).getString("email", "");
        personPhoto = getSharedPreferences("CC", MODE_PRIVATE).getString("photourl", "");
        personId = getSharedPreferences("CC", MODE_PRIVATE).getString("personId", "");
        collegeName.setText(getSharedPreferences("CC", MODE_PRIVATE).getString("collegeName", ""));
        branchName.setText(getSharedPreferences("CC", MODE_PRIVATE).getString("branchName", ""));
        batchName.setText(getSharedPreferences("CC", MODE_PRIVATE).getString("batchName", ""));
        sectionName.setText(getSharedPreferences("CC", MODE_PRIVATE).getString("sectionName", ""));
        collegeName.setFocusable(false);
        profileName.setText(personName);
        profileName.setFocusable(false);
        Picasso.with(EditProfileActivity.this)
                .load(personPhoto)
                .fit()
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
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
                    for (CollegeList college : colleges) {
                        collegeNames.add(college.getCollegeName());
                        collegeIds.add(college.getCollegeId());

                    }
                    data = new ArrayAdapter<>(EditProfileActivity.this, android.R.layout.simple_list_item_1, collegeNames);
                    data.add("Request new college");
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
                AlertDialog.Builder builderBranchList = new AlertDialog.Builder(EditProfileActivity.this);
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
                branchNotFoundDialog = new BranchNotFoundDialog((Activity)EditProfileActivity.this);
                        Window window = branchNotFoundDialog.getWindow();
                       window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                        branchNotFoundDialog.show();
                    }
                });
                builderBranchList.setAdapter(branchNameList,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("onClick",branchNameList.toString());
                                branchNameString = branchNameList.getItem(which);
                                pos_branch_name_selection = which;
                                if (pos_branch_name_selection != branchNameList.getCount() - 1) ;
                                branchName.setText(branchNameString);
                            }
                        });
                String temp = collegeName.getText().toString();
                int index = collegeNames.indexOf(temp);
                if(index>=0) {
                    branchNameList = new ArrayAdapter<String>(EditProfileActivity.this, android.R.layout.simple_list_item_1, colleges.get(index).getBranchNames());
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
                    branchNameList = new ArrayAdapter<String>(EditProfileActivity.this, android.R.layout.simple_list_item_1, colleges.get(index).getBranchNames());
                    // branchName.setAdapter(new ArrayAdapter<String>(EditProfileActivity.this, android.R.layout.simple_list_item_1, colleges.get(index).getBranchNames()));
                }
            }
        });


        batchName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builderBatchList = new AlertDialog.Builder(EditProfileActivity.this);
                //   builderBatchList.setTitle("Select Your Batch");
                    inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
              final View dialogView= inflater.inflate(R.layout.custom_dialog_batch,null);
                builderBatchList.setView(dialogView);
                RadioGroup radioGroup = (RadioGroup)dialogView.findViewById(R.id.radio_group);
                final AlertDialog alertDialog = builderBatchList.create();
                //to hide the soft keyboard
                View v = EditProfileActivity.this.getCurrentFocus();
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
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
                        switch (i){
                            case R.id.btn_2020:
                                alertDialog.dismiss();
                                batchName.setText("2020");
                                break;
                            case R.id.btn_2019:
                                alertDialog.dismiss();
                                batchName.setText("2019");
                                break;

                            case R.id.btn_2018:
                                alertDialog.dismiss();
                                batchName.setText("2018");
                                break;
                            case R.id.btn_2017:
                                alertDialog.dismiss();
                                batchName.setText("2017");
                                break;

                            case R.id.btn_2016:
                                alertDialog.dismiss();
                                batchName.setText("2016");
                                break;
                            case R.id.btn_2015:
                            alertDialog.dismiss();
                                batchName.setText("2015");
                                break;

                            case R.id.btn_2014:
                                alertDialog.dismiss();
                                batchName.setText("2014");
                                break;

                            case R.id.btn_2013:
                                alertDialog.dismiss();
                                batchName.setText("2013");
                                break;

                        }
                    }
                });

            }
        });



        continue_to_course_selection_button.setText("Done");
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
                collegeId = collegeIds.get(index);
                new sign_up().execute();
            }
        });
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 69);
            }
        });

        scrollViewReg.setOnTouchListener(this);
        collegeName.setOnClickListener(this);
//        batchName.setOnTouchListener(this);
//        branchName.setOnTouchListener(this);

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

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.et_college_name:
                AlertDialog.Builder builderCollegeList = new AlertDialog.Builder(EditProfileActivity.this);
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
                                pos_college_selection=which;
                                if(pos_college_selection!=data.getCount()-1)
                                    collegeName.setText(collegeNameString);
                                else{
                                    CollegeNotFoundDialog getdetailsDialog = new CollegeNotFoundDialog((Activity) EditProfileActivity.this);
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

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()){
            case R.id.sv_registration:

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                break;
                case R.id.et_batch:
                if (view.hasFocus()) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            view.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }

                break;
        }
        return false;
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
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        MyApp.activityResumed();
        ConnectionChangeReceiver.broadcast(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyApp.activityPaused();
    }
    public class  BranchNotFoundDialog extends Dialog{
        Activity c;
        Context context;
        @Bind(R.id.b_submit)
        Button submit;
        @Bind(R.id.et_branch)
        EditText branch_name;
        String collegeId;

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
            String temp = collegeName.getText().toString();
            int index = collegeNames.indexOf(temp);
            collegeId = collegeIds.get(index);
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Retrofit retrofit = new Retrofit.
                            Builder()
                            .baseUrl(MyApi.BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    MyApi myApi = retrofit.create(MyApi.class);
                    MyApi.addBranchRequest body = new MyApi.addBranchRequest(collegeId,
                            branch_name.getText().toString()
                    );
                    Call<Void> call = myApi.addBranch(body);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                          Log.d("sucess","called");
                            branchNotFoundDialog.dismiss();
                            Toast.makeText(EditProfileActivity.this,"Your Request has been Sent",Toast.LENGTH_LONG).show();

                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.d("failed","called");
                        }
                    });


                }
            });

        }
    }












}

