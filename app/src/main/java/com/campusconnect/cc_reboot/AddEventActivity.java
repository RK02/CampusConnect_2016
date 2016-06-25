package com.campusconnect.cc_reboot;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.campusconnect.cc_reboot.fragment.Home.FragmentCourses;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.http.Multipart;

public class AddEventActivity extends AppCompatActivity {

    EditText name;
    EditText description;
    EditText course;
    Button submit;
    Button upload;
    String courseName;
    String courseId;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        int mode = getIntent().getIntExtra("Mode",3);
        course = (EditText) findViewById(R.id.course);
        if(getIntent().hasExtra("courseId"))
        {
            courseName = getIntent().getStringExtra("courseTitle");
            courseId = getIntent().getStringExtra("courseId");
            course.setText(courseName);
            course.setEnabled(false);
        }
        progressDialog = new ProgressDialog(this);
        name = (EditText) findViewById(R.id.noteName);
        description = (EditText) findViewById(R.id.noteDate);
        course.setFocusable(false);
        upload = (Button) findViewById(R.id.uploadPhotos);
        submit = (Button) findViewById(R.id.submit);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AddEventActivity.this,UploadPicturesActivity.class),1);
            }
        });
        switch (mode)
        {
            case 1: name.setHint("Exam name");submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new doStuff().execute("exam");
                }
            });break;
            case 2: name.setHint("Assignment name");submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new doStuff().execute("assignment");
                }
            });break;
            case 3: name.setHint("Note name");upload.setVisibility(View.GONE);submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new doStuff().execute("notes");
                }
            });break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1)
        {
            if(resultCode==0){
                finish();
            }
        }
    }

    class doStuff extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Uploading notes...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String path;
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody;
            MultipartBody.Builder body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                  //  .addFormDataPart("file","test.jpg",
                    //      RequestBody.create(MediaType.parse("image/*"), new File(path)))
                    .addFormDataPart("profileId", getSharedPreferences("CC",MODE_PRIVATE).getString("profileId",""))
                    .addFormDataPart("courseId",courseId)
                    .addFormDataPart("type",params[0])
                   .addFormDataPart("desc","This is a desc")
                    .addFormDataPart("value","this is a value?")
                    ;

            for(Bitmap temp : UploadPicturesActivity.images_paths.keySet())
            {
               path =  UploadPicturesActivity.images_paths.get(temp);
                Log.i("sw32path",path);
                body.addFormDataPart("file", "test.jpg", RequestBody.create(MediaType.parse("image/*"),new File(path)));

            }
            requestBody = body.build();
            Request request = new Request.Builder()
                    .url("https://uploadnotes-2016.appspot.com/img")
                    .post(requestBody)
                    .build();
            try {
                client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            client.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    Log.i("sw32",response.message());
//                    Log.i("sw32",response.request()+"");
//
//                }
//            });
            Log.i("sw32","file");
            return null;
        }
            @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            setResult(1);
            finish();
        }

    }
}
