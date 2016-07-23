package com.campusconnect;

import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.campusconnect.POJO.SubscribedCourseList;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class AddEventActivity extends AppCompatActivity {

    EditText name;
    EditText description;
    AutoCompleteTextView course;
    EditText title;
    EditText dueDate;
    Button submit;
    Button upload;
    String courseName;
    String courseId;
    NotificationManager mNotifyManager;
    NotificationCompat.Builder mBuilder;
    private FirebaseAnalytics firebaseAnalytics;
    ArrayList<String> urls;
    ArrayList<String> uris;
    ArrayList<String> courseNamesList;
    ArrayList<String> courseIdsList;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.campusconnect.R.layout.activity_add_event);
        final int mode = getIntent().getIntExtra("Mode",3);
        course = (AutoCompleteTextView) findViewById(com.campusconnect.R.id.course);
        course.setHint("Pick Course");
        courseNamesList = new ArrayList<>();
        courseIdsList = new ArrayList<>();
        Calendar a = Calendar.getInstance();
        title = (EditText) findViewById(com.campusconnect.R.id.noteTitle);
        dueDate = (EditText) findViewById(com.campusconnect.R.id.noteDueDate);
        dueDate.setFocusable(false);
        datePickerDialog = new DatePickerDialog(AddEventActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if(dayOfMonth<10)
                dueDate.setText("0"+dayOfMonth + "-" + monthOfYear + "-" + year);
                else dueDate.setText(+dayOfMonth + "-" + monthOfYear + "-" + year);
                if(monthOfYear<10) dueDate.setText(dayOfMonth + "-0" + monthOfYear + "-" + year);
                else dueDate.setText(dayOfMonth + "-" + monthOfYear + "-" + year);
            }
        },a.get(Calendar.YEAR),a.get(Calendar.MONTH),a.get(Calendar.DAY_OF_MONTH));
        dueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        description = (EditText) findViewById(com.campusconnect.R.id.noteDescription);
         mNotifyManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
         mBuilder= new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("CampusConnect")
                .setContentText("Uploading...")
                .setSmallIcon(com.campusconnect.R.mipmap.ccnoti);
// Start a lengthy operation in a background thread
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        List<SubscribedCourseList> temp = SubscribedCourseList.listAll(SubscribedCourseList.class);
        for(SubscribedCourseList course : temp)
        {
            courseNamesList.add(course.getCourseName());
            courseIdsList.add(course.getCourseId());
        }
        if(getIntent().hasExtra("courseName"))
        {
            courseName = getIntent().getStringExtra("courseName");
            Log.i("sw32upload",courseName + " this");
            course.setText(courseName);
            course.setFocusable(false);
            String desc = getIntent().getStringExtra("description")+"";
            Log.i("sw32upload",desc + " this");
            description.setText(desc);

        }
        if(getIntent().hasExtra("courseId"))
        {
            courseName = getIntent().getStringExtra("courseTitle");
            courseId = getIntent().getStringExtra("courseId");
            course.setFocusable(false);
            if(!courseName.equals("")) {
                course.setText(courseName + "");
            }

        }
        else
        {
            final ArrayAdapter<String> courseNames = new ArrayAdapter<>(AddEventActivity.this,android.R.layout.simple_list_item_1,courseNamesList);
            final AlertDialog.Builder builderCourseList = new AlertDialog.Builder(AddEventActivity.this);
            builderCourseList.setTitle("Select your course");
            builderCourseList.setNegativeButton(
                    "cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builderCourseList.setAdapter(
                    courseNames,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String courseName = courseNames.getItem(which);
                            course.setText(courseName);
                        }
                    });
            course.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    builderCourseList.show();
                }
            });

        }
        name = (EditText) findViewById(com.campusconnect.R.id.noteName);
        description = (EditText) findViewById(com.campusconnect.R.id.noteDescription);
        upload = (Button) findViewById(com.campusconnect.R.id.uploadPhotos);
        submit = (Button) findViewById(com.campusconnect.R.id.submit);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mode!=3)
                {
                    Intent intent = new Intent(AddEventActivity.this,UploadPicturesActivity.class);
                    if(uris==null){urls = new ArrayList<>();uris = new ArrayList<>();}
                    intent.putStringArrayListExtra("urls",urls);
                    intent.putStringArrayListExtra("uris",uris);
                    startActivityForResult(intent,1);
                }
                else
                {

                    Intent temp = new Intent();
                    temp.putStringArrayListExtra("urls",urls);
                    temp.putStringArrayListExtra("uris",uris);
                    temp.putExtra("description",description.getText().toString()+"");
                    temp.putExtra("courseName",course.getText().toString()+"");
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
                    int index = courseNamesList.indexOf(course.getText().toString());
                    for(String course : courseNamesList)
                    {
                        Log.i("sw32courses", course +"");
                    }
                    Log.i("sw32checking",course.getText().toString()+"  :  " + index);
                    if(index<0){
                        course.setError("Select valid course");
                        course.requestFocus();
                        return;
                    }
                    else {courseId = courseIdsList.get(index);
                        new doStuff().execute("exam",description.getText().toString(),title.getText().toString(),dueDate.getText().toString());
                    }
                }
            });break;
            case 2: name.setText("Assignment");
                upload.setText("UPLOAD PHOTO");
                submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = courseNamesList.indexOf(course.getText().toString());
                    if(index<0){
                        course.setError("Select valid course");
                        course.requestFocus();
                        return;
                    }
                    else {courseId =courseIdsList.get(index);
                        new doStuff().execute("assignment",description.getText().toString(),title.getText().toString(),dueDate.getText().toString());
                    }
                }
            });break;
            case 3:
                name.setText("Note");
                upload.setText("UPLOAD MORE");
                dueDate.setVisibility(View.GONE);
                submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = courseNamesList.indexOf(course.getText().toString());
                    if(index<0){
                        course.setError("Select valid course");
                        course.requestFocus();
                        return;
                    }
                    else {
                        courseId = courseIdsList.get(index);
                        new doStuff().execute("notes",description.getText().toString(),title.getText().toString(),"");
                    }

                }
            });break;
        }
        name.setFocusable(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1)
        {
            if(resultCode==0){
                finish();
            }
            if(resultCode==1)
            {
                urls = data.getStringArrayListExtra("urls");
                uris = data.getStringArrayListExtra("uris");
            }
        }
    }

    class doStuff extends AsyncTask<String, String, String> {

        List<String> urls;
        String type;
        String profileId;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            firebaseAnalytics.logEvent("picture_upload_started",new Bundle());
            if(course.getText().toString().equals("")){course.setError("Select course");course.requestFocus();return;}
            if(name.getText().toString().equals("")){name.setError("Pick Type");name.requestFocus();return;}
            if(dueDate.getVisibility()==View.VISIBLE){
                if (dueDate.getText().toString().equals("")) {
                dueDate.setError("Enter due date");dueDate.requestFocus();return;}
                }
            profileId = getSharedPreferences("CC",MODE_PRIVATE).getString("profileId","");
            urls =  getIntent().getStringArrayListExtra("urls");
            Intent intent = new Intent();
            intent.putExtra("courseId",courseId);
            intent.putExtra("uploadNotesActivity","success");
            setResult(1,intent);
            finish();
            mBuilder.setProgress(0,0,true);
            mBuilder.setSmallIcon(com.campusconnect.R.mipmap.ccnoti);
            mBuilder.setOngoing(true);
            mBuilder.setContentIntent(null);
            mNotifyManager.notify(1,mBuilder.build());
        }

        @Override
        protected String doInBackground(String... params) {

            String path;
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody;
            type = params[0];
            MultipartBody.Builder body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("profileId",profileId )
                    .addFormDataPart("courseId",courseId)
                    .addFormDataPart("type",type)
                    .addFormDataPart("desc",params[1]+"")
                    .addFormDataPart("title",params[2]+"")
                    .addFormDataPart("date","");
            File file;
            int i=1;
            if(!params[3].equals(""))
            {
                body.addFormDataPart("dueDate",params[3]);
                body.addFormDataPart("dueTime","12:00");
            }

            if(urls!=null)
            {
                for (String temp : urls) {
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
                        original.compress(Bitmap.CompressFormat.JPEG, 40, out);
                    else
                        original.compress(Bitmap.CompressFormat.JPEG, 80, out);
                    body.addFormDataPart("file", "test.jpg", RequestBody.create(MediaType.parse("image/*"), file));
                }
            }
            requestBody = body.build();
            Request request = new Request.Builder()
                    //.url("https://uploadnotes-2016.appspot.com/img")
                    .url("http://campusconnect.pythonanywhere.com/android")
                    .post(requestBody)
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Response res = response;
                ResponseBody responseBody=null;
                       if(res!=null)
                       {
                          responseBody  = res.body();
                       }
                String jsonresponse=null;
                        if(responseBody!=null)
                        {
                            jsonresponse = responseBody.string();
                        }
                else
                        {
                            mBuilder.setContentText("Upload failed!");
                            mBuilder.setProgress(0,0,false);
                            mNotifyManager.notify(1,mBuilder.build());
                        }
                Log.i("sw32response",res + " :////");
                if(jsonresponse!=null){
                JSONObject jsonObject = new JSONObject(jsonresponse);
                switch (type) {
                    case "notes":
                        return jsonObject.getString("noteBookId");
                    case "assignment":
                        return jsonObject.getString("assignmentId");
                    case "exam":
                        return jsonObject.getString("examId");
                }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
            @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
                mBuilder.setContentText("Uploading completed! Check it out!");
                mBuilder.setOngoing(false);
                Log.i("sw32notificationlog",s + "   ;;;");
                Intent intent;
                switch (type){
                    case "notes":intent = new Intent(getApplicationContext(), NotePageActivity.class); intent.putExtra("noteBookId",s); break;
                    case "assignment":intent = new Intent(getApplicationContext(), AssignmentPageActivity.class); intent.putExtra("assignmentId",s);break;
                    case "exam": intent = new Intent(getApplicationContext(), ExamPageActivity.class); intent.putExtra("testId",s);break;
                    default: intent = new Intent(getApplicationContext(), HomeActivity2.class);break;
                }
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0 /* Request code */, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(pendingIntent);
                mBuilder.setAutoCancel(true);
                mBuilder.setProgress(0,0,false);
            mNotifyManager.notify(1,mBuilder.build());
                Toast.makeText(getApplicationContext(),"Your upload has completed",Toast.LENGTH_SHORT).show();
        }
    }
}
