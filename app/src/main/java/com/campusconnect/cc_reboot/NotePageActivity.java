package com.campusconnect.cc_reboot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.campusconnect.cc_reboot.POJO.ModelNoteBook;
import com.campusconnect.cc_reboot.POJO.ModelNoteBookList;
import com.campusconnect.cc_reboot.POJO.MyApi;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.campusconnect.cc_reboot.POJO.*;
import com.campusconnect.cc_reboot.fragment.Home.FragmentCourses;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;


/**
 * Created by RK on 04/06/2016.
 */
public class NotePageActivity extends AppCompatActivity {


    @Bind(R.id.iv_notes)
    ImageView notes_container;

    String noteBookId;
    Retrofit retrofit;
    ImageView lastPage;
    TextView views;
    TextView rating;
    TextView pages;
    TextView courseName;
    TextView uploader;
    TextView description;
    TextView datePosted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        ButterKnife.bind(this);

        notes_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_temp = new Intent(v.getContext(), NotesSliderActivity.class);
                startActivity(intent_temp);
            }
        });

        lastPage = (ImageView) findViewById(R.id.iv_notes);
        noteBookId = getIntent().getStringExtra("noteBookId");
        views = (TextView) findViewById(R.id.tv_views_count);
        rating = (TextView) findViewById(R.id.tv_rating);
        pages = (TextView) findViewById(R.id.tv_pages);
        courseName = (TextView) findViewById(R.id.course_name);
        uploader = (TextView) findViewById(R.id.tv_uploader);
        description=(TextView) findViewById(R.id.tv_description);
        datePosted = (TextView) findViewById(R.id.tv_date_posted);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
// add your other interceptors …

// add logging as last interceptor
        httpClient.addInterceptor(logging);
        retrofit = new Retrofit.
                Builder()
                .baseUrl(MyApi.BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyApi myApi = retrofit.create(MyApi.class);
        MyApi.getNoteBookRequest request = new MyApi.getNoteBookRequest(noteBookId, FragmentCourses.profileId);
        Call<ModelNoteBook> call = myApi.getNoteBook(request);
        call.enqueue(new Callback<ModelNoteBook>() {
            @Override
            public void onResponse(Call<ModelNoteBook> call, Response<ModelNoteBook> response) {
                ModelNoteBook noteBook = response.body();
                Log.i("sw32",""+response.code());
                List<Note> noteList = noteBook.getNotes();
                List<String> urls = noteList.get(noteList.size()-1).getUrlList();
                String last = urls.get(urls.size()-1);
                Picasso.with(NotePageActivity.this).load(last).into(lastPage);
                courseName.setText(noteBook.getCourseName());
                views.setText(noteBook.getViews());
                rating.setText(noteBook.getTotalRating());
                pages.setText(noteBook.getPages());
                uploader.setText(noteBook.getUploaderName());
                datePosted.setText(noteBook.getLastUpdated());

            }

            @Override
            public void onFailure(Call<ModelNoteBook> call, Throwable t) {

            }
        });





    }
}

