package com.campusconnect.cc_reboot;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;

import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.campusconnect.cc_reboot.POJO.ModelNoteBook;
import com.campusconnect.cc_reboot.POJO.ModelNoteBookList;
import com.campusconnect.cc_reboot.POJO.MyApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.campusconnect.cc_reboot.POJO.*;
import com.campusconnect.cc_reboot.fragment.Home.FragmentCourses;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;





/**
 * Created by RK on 04/06/2016.
 */
public class NotePageActivity extends AppCompatActivity implements View.OnClickListener{


    @Bind(R.id.iv_notes)
    ImageView notes_last_page;

    @Bind(R.id.container_notes)
    RelativeLayout notes_container;

    @Bind(R.id.tv_views_count)
    TextView views;
    @Bind(R.id.tv_rating)
    TextView rating;
    @Bind(R.id.tv_pages)
    TextView pages;
    @Bind(R.id.tv_note_name)
    TextView courseName;
    @Bind(R.id.tv_uploader)
    TextView uploader;
    @Bind(R.id.tv_last_updated)
    TextView lastPosted;

    @Bind(R.id.ib_edit_note)
    ImageButton edit_note_button;
    @Bind(R.id.ib_fullscreen)
    ImageButton fullscreen_button;
    @Bind(R.id.ib_share)
    ImageButton share_note_button;

    @Bind(R.id.tb_bookmark)
    ToggleButton bookmark_note_button;
    @Bind(R.id.b_rent)
    Button rent_note_button;

    String noteBookId;
    int courseColor;
    Retrofit retrofit;
    List<Note> noteList;
    public static JSONObject jsonNoteList;
    public static ArrayList<String> descriptions;
    public static ArrayList<String> dates;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        ButterKnife.bind(this);

        courseColor = getIntent().getIntExtra("CourseColor", Color.rgb(224,224,224));
        notes_container.setBackgroundColor(courseColor);

        noteBookId = getIntent().getStringExtra("noteBookId");

        descriptions = new ArrayList<>();
        dates = new ArrayList<>();


        retrofit = new Retrofit.
                Builder()
                .baseUrl(MyApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyApi myApi = retrofit.create(MyApi.class);
        MyApi.getNoteBookRequest request = new MyApi.getNoteBookRequest(noteBookId, getSharedPreferences("CC", Context.MODE_PRIVATE).getString("profileId",""));
        Call<ModelNoteBook> call = myApi.getNoteBook(request);
        call.enqueue(new Callback<ModelNoteBook>() {
            @Override
            public void onResponse(Call<ModelNoteBook> call, Response<ModelNoteBook> response) {
                ModelNoteBook noteBook = response.body();
                if(noteBook.getBookmarkStatus().equals("0")){bookmark_note_button.setChecked(false);}else{bookmark_note_button.setChecked(true);}
                noteList = noteBook.getNotes();
                jsonNoteList = new JSONObject();
                for(Note a : noteList)
                {
                    try {
                        jsonNoteList.put(a.getClassNumber(),a);
                        Log.i("sw32page",a.getClassNumber());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                List<String> urls = noteList.get(noteList.size()-1).getUrlList();
                for(Note temp : noteList)
                {
                    descriptions.add(temp.getDescription());
                    dates.add(temp.getDate());
                }

                String last = urls.get(urls.size()-1);
                Picasso.with(NotePageActivity.this)
                        .load(last)
                        .fit()
                        .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                        .into(notes_last_page);
                courseName.setText(noteBook.getCourseName());
                views.setText(noteBook.getViews());
                rating.setText(noteBook.getTotalRating());
                pages.setText(noteBook.getPages());
                uploader.setText(noteBook.getUploaderName());

                String time = noteBook.getLastUpdated();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                int days = 0,hours=0,minutes=0,seconds=0;
                try {
                    Calendar a = Calendar.getInstance();
                    Calendar b = Calendar.getInstance();
                    b.setTime(df.parse(time));
                    long difference = a.getTimeInMillis() - b.getTimeInMillis();
                    days = (int) (difference/ (1000*60*60*24));
                    hours = (int) (difference/ (1000*60*60));
                    minutes = (int) (difference/ (1000*60));
                    seconds = (int) (difference/1000);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(days==0) {if(hours==0) {if(minutes==0) {if(seconds==0) {lastPosted.setText("Just now");}
                else {if(seconds==1) lastPosted.setText(seconds + " second ago");
                else lastPosted.setText(seconds + " seconds ago");}}
                else {if(minutes==1) lastPosted.setText(minutes + " minute ago");
                    lastPosted.setText(minutes + " minutes ago");}}
                else {if(hours==1)lastPosted.setText(hours + " hour ago");
                else lastPosted.setText(hours + " hours ago");}}
                else {if(days==1)lastPosted.setText(days + " day ago");
                else lastPosted.setText(days + " days ago");}
            }
            @Override
            public void onFailure(Call<ModelNoteBook> call, Throwable t) {
            }
        });
        //OnClickListeners
        edit_note_button.setOnClickListener(this);
        fullscreen_button.setOnClickListener(this);
        share_note_button.setOnClickListener(this);
        notes_last_page.setOnClickListener(this);
        bookmark_note_button.setOnClickListener(this);
        rent_note_button.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.ib_edit_note:
                intent = new Intent(getApplicationContext(), EditNoteActivity.class);
                startActivity(intent);
                break;

            case R.id.ib_fullscreen:
                intent = new Intent(getApplicationContext(), NotesSliderActivity.class);
                startActivity(intent);
                break;

            case R.id.ib_share:

                break;

            case R.id.iv_notes:
                intent = new Intent(getApplicationContext(), NotesSliderActivity.class);
                startActivity(intent);
                break;

            case R.id.tb_bookmark:
                new bookmarkNote().execute();
                break;

            case R.id.b_rent:
                intent = new Intent(getApplicationContext(), RentNoteActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }

    }

    class bookmarkNote extends AsyncTask<String,String,String>{

        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(NotePageActivity.this);
            progressDialog.setTitle("Please wait...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection;
            URL url;
            JSONObject body = new JSONObject();
            try{
                url = new URL(FragmentCourses.BASE_URL+"bookmarkNoteBook");
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type","application/json");
                connection.connect();
                DataOutputStream os = new DataOutputStream(connection.getOutputStream());
                body.put("profileId",getSharedPreferences("CC",MODE_PRIVATE).getString("profileId",""));
                body.put("noteBookId",noteBookId);
                os.write(body.toString().getBytes());
                os.flush();
                os.close();
                int a = connection.getResponseCode();
                Log.i("sw32bookmark",a+"");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (bookmark_note_button.isChecked()) {
                FirebaseMessaging.getInstance().subscribeToTopic(noteBookId);
            }
            else {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(noteBookId);
            }
            progressDialog.dismiss();
        }
    }
}

