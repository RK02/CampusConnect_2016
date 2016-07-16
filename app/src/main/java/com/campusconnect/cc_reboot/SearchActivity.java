package com.campusconnect.cc_reboot;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.campusconnect.cc_reboot.POJO.AvailableCourseList;
import com.campusconnect.cc_reboot.POJO.CourseList;
import com.campusconnect.cc_reboot.POJO.Example;
import com.campusconnect.cc_reboot.POJO.ModelCourseSearch;
import com.campusconnect.cc_reboot.POJO.ModelNotesSearch;
import com.campusconnect.cc_reboot.POJO.MyApi;
import com.campusconnect.cc_reboot.POJO.NoteBookList;
import com.campusconnect.cc_reboot.POJO.SubscribedCourseList;
import com.campusconnect.cc_reboot.fragment.Home.FragmentCourses;
import com.campusconnect.cc_reboot.fragment.SearchPage.FragmentSearchCourse;
import com.campusconnect.cc_reboot.fragment.SearchPage.FragmentSearchNotes;
import com.campusconnect.cc_reboot.slidingtab.SlidingTabLayout_home;
import com.campusconnect.cc_reboot.viewpager.ViewPagerAdapter_home;
import com.campusconnect.cc_reboot.viewpager.ViewPagerAdapter_search;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadFactory;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by RK on 04/06/2016.
 */
public class SearchActivity extends AppCompatActivity {

    ViewPagerAdapter_search search_adapter;
    ViewPager search_pager;
    public static SlidingTabLayout_home search_tabs;
    CharSequence Titles[] = {"Courses", "Notes"};
    int Numboftabs = 2;
    public static EditText searchBar;
    Retrofit retrofit;
    MyApi myApi;
    Call<ModelCourseSearch> callCourse;
    Call<ModelNotesSearch> callNotes;
    @Bind(R.id.ib_search)
    ImageButton searchButton;
    private FirebaseAnalytics firebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        searchBar = (EditText) findViewById(R.id.et_search);
        retrofit = new Retrofit.
                Builder()
                .baseUrl(MyApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myApi = retrofit.create(MyApi.class);
        searchBar.setHint("Search Courses");
        search_pager = (ViewPager) findViewById(R.id.pager_home);
        search_tabs = (SlidingTabLayout_home) findViewById(R.id.tabs_home);
        search_adapter = new ViewPagerAdapter_search(getSupportFragmentManager(), Titles, Numboftabs, this);
        search_pager.setAdapter(search_adapter);
        search_pager.setCurrentItem(0);
        search_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.i("sw32pager",position+"");
                switch (position){
                    case 0:searchBar.setHint("Search Courses");break;
                    case 1:searchBar.setHint("Search Notes");break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        search_tabs.setDistributeEvenly(true);
        search_tabs.setViewPager(search_pager);
        searchBar.setImeActionLabel(">>", KeyEvent.KEYCODE_ENTER);
        searchBar.setInputType(InputType.TYPE_CLASS_TEXT);
        searchBar.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    if (searchBar.getText().toString().equals("")) {searchBar.setError("Enter search text");searchBar.requestFocus();return false;}
                    hideKeyboard(SearchActivity.this);
                    String searchString = searchBar.getText().toString();
                    searchapi(searchString);
                    return true;
                }
                return false;
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchBar.getText().toString().equals("")) {searchBar.setError("Enter search text");searchBar.requestFocus();return;}

                searchapi(searchBar.getText().toString());
            }
        });




    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    void searchCourse(final String searchString){
        FragmentSearchCourse.mCourseAdapter.clear();
        callCourse= myApi.searchCourse(new MyApi.courseSearch(searchString));
        callCourse.enqueue(new Callback<ModelCourseSearch>() {
            @Override
            public void onResponse(Call<ModelCourseSearch> call, Response<ModelCourseSearch> response) {

                ModelCourseSearch modelCourseSearch = response.body();
                if(modelCourseSearch!=null)
                {
                    List<CourseList> courseLists = modelCourseSearch.getCourseList();
                    for(CourseList x : courseLists)
                    {
                        FragmentSearchCourse.courses.add(x);
                        FragmentSearchCourse.courseNames.add(x.getCourseName());
                        FragmentSearchCourse.courseIds.add(x.getCourseId());
                        FragmentSearchCourse.mCourseAdapter.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void onFailure(Call<ModelCourseSearch> call, Throwable t) {

            }
        });
    }

    void searchNotes(String searchString) {
        FragmentSearchNotes.mSearchNotesAdapter.clear();
        callNotes= myApi.searchNotes(new MyApi.notesSearch(searchString));
        callNotes.enqueue(new Callback<ModelNotesSearch>() {
            @Override
            public void onResponse(Call<ModelNotesSearch> call, Response<ModelNotesSearch> response) {
                ModelNotesSearch modelNotesSearch = response.body();
                if(modelNotesSearch!=null)
                {
                    List<NoteBookList> noteBookLists = modelNotesSearch.getNoteBookList();
                    for(NoteBookList x : noteBookLists){
                        FragmentSearchNotes.noteBookLists.add(x);
                        FragmentSearchNotes.mSearchNotesAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<ModelNotesSearch> call, Throwable t) {

            }
        });
    }

    void searchapi(String searchString)
    {
        searchCourse(searchString);
        searchNotes(searchString);
        Bundle params = new Bundle();
        params.putString("searchstring",searchString);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        firebaseAnalytics.logEvent("search_event",params);
    }
}

