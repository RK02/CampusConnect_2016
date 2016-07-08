package com.campusconnect.cc_reboot;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.campusconnect.cc_reboot.fragment.Home.FragmentCourses;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;



import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class AddEventActivity extends AppCompatActivity {

    EditText name;
    EditText description;
    AutoCompleteTextView course;
    EditText date;
    EditText dueDate;
    Button submit;
    Button upload;
    String courseName;
    String courseId;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        final int mode = getIntent().getIntExtra("Mode",3);
        course = (AutoCompleteTextView) findViewById(R.id.course);
        date = (EditText) findViewById(R.id.noteDate);
        dueDate = (EditText) findViewById(R.id.noteDueDate);
        description = (EditText) findViewById(R.id.noteDescription);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c.getTime());
        date.setText(formattedDate);
        dueDate.setText(formattedDate);
        date.setFocusable(false);
        if(getIntent().hasExtra("courseName"))
        {
            courseName = getIntent().getStringExtra("courseName");
            course.setText(courseName);
            course.setFocusable(false);
            description.setText(getIntent().getStringExtra("description")+"");

        }
        if(getIntent().hasExtra("courseId"))
        {
            courseName = getIntent().getStringExtra("courseTitle");
            courseId = getIntent().getStringExtra("courseId");
            if(!courseName.equals("")) {
                course.setText(courseName + "");
                course.setFocusable(false);
            }

        }
        else
        {
            ArrayList<String> temp = FragmentCourses.courseNames;
            Log.i("sw32",""+FragmentCourses.courseNames.size() + ":" + FragmentCourses.courseIds.size());
            ArrayAdapter<String> courseNames = new ArrayAdapter<>(AddEventActivity.this,android.R.layout.simple_list_item_1,FragmentCourses.courseNames);
            course.setAdapter(courseNames);
        }
        progressDialog = new ProgressDialog(this);
        name = (EditText) findViewById(R.id.noteName);
        description = (EditText) findViewById(R.id.noteDescription);
        upload = (Button) findViewById(R.id.uploadPhotos);
        submit = (Button) findViewById(R.id.submit);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mode!=3)
                {
                    startActivityForResult(new Intent(AddEventActivity.this,UploadPicturesActivity.class),1);
                }
                else
                {
                    Intent temp = new Intent();
                    temp.putExtra("description",description.getText().toString()+"");
                    temp.putExtra("courseName",courseName+"");
                    setResult(2,temp);
                    finish();
                }
            }
        });
        switch (mode)
        {
            case 1: name.setText("Exam");
                upload.setText("UPLOAD PHOTO");
                submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    courseId = FragmentCourses.courseIds.get(FragmentCourses.courseNames.indexOf(course.getText().toString()));
                    new doStuff().execute("exam",description.getText().toString(),date.getText().toString(),dueDate.getText().toString());
                }
            });break;
            case 2: name.setText("Assignment");
                upload.setText("UPLOAD PHOTO");
                submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    courseId = FragmentCourses.courseIds.get(FragmentCourses.courseNames.indexOf(course.getText().toString()));
                    new doStuff().execute("assignment",description.getText().toString(),date.getText().toString(),dueDate.getText().toString());
                }
            });break;
            case 3:
                name.setText("Note");
                upload.setText("UPLOAD MORE");
                dueDate.setVisibility(View.GONE);
                submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    courseId = FragmentCourses.courseIds.get(FragmentCourses.courseNames.indexOf(course.getText().toString()));
                    new doStuff().execute("notes",description.getText().toString(),date.getText().toString(),"");
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
            if(course.getText().toString().equals("")){course.setError("Select course");course.requestFocus();return;}
            if(name.getText().toString().equals("")){name.setError("Pick Type");name.requestFocus();return;}
            if(dueDate.getVisibility()==View.VISIBLE){
                if (dueDate.getText().toString().equals("")) {
                dueDate.setError("Enter due date");dueDate.requestFocus();return;}
                }
            progressDialog.setMessage("Uploading...");
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
                    .addFormDataPart("profileId", getSharedPreferences("CC",MODE_PRIVATE).getString("profileId",""))
                    .addFormDataPart("courseId",courseId)
                    .addFormDataPart("type",params[0])
                    .addFormDataPart("desc",params[1]+"")
                    .addFormDataPart("Title",courseName)
                    .addFormDataPart("date",params[2]);
            File file;
            int i=1;
            if(!params[3].equals(""))
            {
                body.addFormDataPart("dueDate",params[3]);
                body.addFormDataPart("dueTime","08:00:00");
            }
            if(UploadPicturesActivity.urls!=null) {
                for (String temp : UploadPicturesActivity.urls) {
                    Log.i("sw32", "test : " + temp);

                    Bitmap original = null;
                    try {
                        original = BitmapFactory.decodeStream(new FileInputStream(temp));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    file = new File(getFilesDir() + "/temp" + i + ".jpeg");
                    i++;
                    FileOutputStream out = null;
                    try {
                        out = new FileOutputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    int size = original.getRowBytes() * original.getHeight();
                    Log.i("sw32size", size + "");
                    if (size > 10000000)
                        original.compress(Bitmap.CompressFormat.JPEG, 20, out);
                    else
                        original.compress(Bitmap.CompressFormat.JPEG, 50, out);
                    body.addFormDataPart("file", "test.jpg", RequestBody.create(MediaType.parse("image/*"), file));
                }
            }
            else
            {
                //body.addFormDataPart("file", "test.jpg", RequestBody.create(MediaType.parse("image/*"), new File("http://www.epirusportal.gr/wp-content/uploads/default-no-image.png")));
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
            return null;
        }
            @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
                Intent intent = new Intent();
                intent.putExtra("courseId",courseId);
            setResult(1,intent);
            finish();
        }

    }
}
