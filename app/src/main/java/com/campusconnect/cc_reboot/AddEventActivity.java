package com.campusconnect.cc_reboot;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AddEventActivity extends AppCompatActivity {

    EditText name;
    EditText description;
    EditText course;
    String[] categories = {"Arts", "CA and CS", "Commerce", "eBooks", "Engg and tech", "IAS", "JEE"};
    Button submit;
    Button upload;
    String courseName;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        int mode = getIntent().getIntExtra("Mode",3);
        course = (EditText) findViewById(R.id.course);
        if(getIntent().hasExtra("courseId"))
        {
            courseName = getIntent().getStringExtra("courseId");
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
                    new doStuff().execute();
                }
            });break;
            case 2: name.setHint("Assignment name");submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new doStuff().execute();
                }
            });break;
            case 3: name.setHint("Note name");upload.setVisibility(View.GONE);submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new doStuff().execute();
                }
            });break;
        }
        course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alb = new AlertDialog.Builder(AddEventActivity.this);
                alb.setTitle("Choose...").setSingleChoiceItems(categories, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText temp = (EditText) findViewById(R.id.course);
                        temp.setText(categories[which]);
                        dialog.dismiss();
                    }
                });
                alb.create().show();

            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1)
        {
            if(resultCode==0){
                Log.i("sw32","here111");
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


            HttpURLConnection connection = null;
            JSONObject jsonObject = new JSONObject();
            try {
                //send to django server
                URL url = new URL("");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                // connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                // connection.setRequestProperty("uploaded_file", fileName);
                connection.connect();
                DataOutputStream os = new DataOutputStream(connection.getOutputStream());


                os.close();
                int response = connection.getResponseCode();
                return "" + response + "\n" + jsonObject.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } //catch (JSONException e) {
            //e.printStackTrace();
            //}
            finally {
                if (connection != null)
                    connection.disconnect();
            }

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
