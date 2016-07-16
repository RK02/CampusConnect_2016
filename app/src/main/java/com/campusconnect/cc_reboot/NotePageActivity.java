package com.campusconnect.cc_reboot;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;

import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.campusconnect.cc_reboot.POJO.ModelNoteBook;
import com.campusconnect.cc_reboot.POJO.MyApi;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.campusconnect.cc_reboot.POJO.*;
import com.campusconnect.cc_reboot.adapter.StudentsListAdapter;
import com.campusconnect.cc_reboot.fragment.Drawer.FragmentAbout;
import com.campusconnect.cc_reboot.fragment.Drawer.FragmentAddCourse;
import com.campusconnect.cc_reboot.fragment.Drawer.FragmentFeedback;
import com.campusconnect.cc_reboot.fragment.Drawer.FragmentGifts;
import com.campusconnect.cc_reboot.fragment.Drawer.FragmentInvite;
import com.campusconnect.cc_reboot.fragment.Drawer.FragmentPointsInfo;
import com.campusconnect.cc_reboot.fragment.Drawer.FragmentRate;
import com.campusconnect.cc_reboot.fragment.Drawer.FragmentSettings;
import com.campusconnect.cc_reboot.fragment.Drawer.FragmentTerms;
import com.campusconnect.cc_reboot.fragment.Home.FragmentCourses;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

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
public class NotePageActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.drawer)
    DrawerLayout drawerLayout;
    @Bind(R.id.navigation_view)
    NavigationView navigationView;
    @Bind(R.id.frame)
    FrameLayout fragment_frame;

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
    @Bind(R.id.b_rate)
    Button rate_note_button;

    String noteBookId;
    int courseColor;
    Retrofit retrofit;
    List<Note> noteList;
    RateNoteDialog rateNoteDialog;
    public static JSONObject jsonNoteList;
    public static ArrayList<String> descriptions;
    public static ArrayList<String> dates;
    Intent intent;

    //Flags
    boolean doubleBackToExitPressedOnce = false;
    boolean at_home=true;
    String frag_title="";
    private Toolbar toolbar;
    private Fragment fragment = null;
    Fragment homefrag;
    View headerView;
    public static TextView home_title;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        ButterKnife.bind(this);

        //Drawer stuff
        home_title = (TextView) findViewById(R.id.tv_title);
        toolbar = (Toolbar) findViewById (R.id.toolbar);
        setSupportActionBar(toolbar);
//        homefrag = new FragmentHome();
        //Setting up Header View
        headerView = getLayoutInflater().inflate(R.layout.header, navigationView, false);
        navigationView.addHeaderView(headerView);
        ImageView view = (ImageView) headerView.findViewById(R.id.profile_image);

        //Unchecking all the drawer menu items before going back to home in case the app crashes
        int size = navigationView.getMenu().size();
        for (int i = 0; i < size; i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }
//Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
//Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked())
                    menuItem.setChecked(false);
                else
                    menuItem.setChecked(true);
//Closing drawer on item click
                drawerLayout.closeDrawers();
//Fragment selection and commits
                displayView(menuItem.getItemId());
                return true;
            }
        });
//Initializing ActionBarToggle
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
                fragment_frame.setTranslationX((drawerLayout.getWidth() * slideOffset) / 4);
                fragment_frame.setTranslationX((drawerLayout.getWidth() * slideOffset) / 4);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        //calling sync state is necessary or else the hamburger icon won't show up
        actionBarDrawerToggle.syncState();
        //OnClickListener Header View
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_temp = new Intent(getApplicationContext(), ProfilePageActivity.class);
                startActivity(intent_temp);
            }
        });
        //Drawer ends

        courseColor = getIntent().getIntExtra("CourseColor", Color.rgb(224, 224, 224));
        notes_container.setBackgroundColor(courseColor);



        descriptions = new ArrayList<>();
        dates = new ArrayList<>();

        //OnClickListeners
        edit_note_button.setOnClickListener(this);
        fullscreen_button.setOnClickListener(this);
        share_note_button.setOnClickListener(this);
        notes_last_page.setOnClickListener(this);
        bookmark_note_button.setOnClickListener(this);
        rate_note_button.setOnClickListener(this);

        //GoogleSignIn stuff
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Branch.isAutoDeepLinkLaunch(this)) {
            try {
                String autoDeeplinkedValue = Branch.getInstance().getLatestReferringParams().getString("noteBookId");
                noteBookId = autoDeeplinkedValue;
                Log.i("sw32Deep","Launched by Branch on auto deep linking!"
                        + "\n\n" + autoDeeplinkedValue);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
        {
            noteBookId = getIntent().getStringExtra("noteBookId");
        }
        retrofit = new Retrofit.
                Builder()
                .baseUrl(MyApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyApi myApi = retrofit.create(MyApi.class);
        MyApi.getNoteBookRequest request = new MyApi.getNoteBookRequest(noteBookId, getSharedPreferences("CC", Context.MODE_PRIVATE).getString("profileId", ""));
        Call<ModelNoteBook> call = myApi.getNoteBook(request);
        call.enqueue(new Callback<ModelNoteBook>() {
            @Override
            public void onResponse(Call<ModelNoteBook> call, Response<ModelNoteBook> response) {
                ModelNoteBook noteBook = response.body();
                if (noteBook.getBookmarkStatus().equals("0")) {
                    bookmark_note_button.setChecked(false);
                } else {
                    bookmark_note_button.setChecked(true);
                }
                noteList = noteBook.getNotes();
                jsonNoteList = new JSONObject();
                for (Note a : noteList) {
                    try {
                        jsonNoteList.put(a.getClassNumber(), a);
                        Log.i("sw32page", a.getClassNumber());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                List<String> urls = noteList.get(noteList.size() - 1).getUrlList();
                for (Note temp : noteList) {
                    descriptions.add(temp.getDescription());
                    dates.add(temp.getDate());
                }

                String last = urls.get(urls.size() - 1);
                Picasso.with(NotePageActivity.this)
                        .load(last)
                        .fit()
                        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                        .into(notes_last_page);
                courseName.setText(noteBook.getCourseName());
                views.setText(noteBook.getViews());
                rating.setText(noteBook.getTotalRating());
                pages.setText(noteBook.getPages());
                uploader.setText(noteBook.getUploaderName());

                String time = noteBook.getLastUpdated();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                int days = 0, hours = 0, minutes = 0, seconds = 0;
                try {
                    Calendar a = Calendar.getInstance();
                    Calendar b = Calendar.getInstance();
                    b.setTime(df.parse(time));
                    long difference = a.getTimeInMillis() - b.getTimeInMillis();
                    days = (int) (difference / (1000 * 60 * 60 * 24));
                    hours = (int) (difference / (1000 * 60 * 60));
                    minutes = (int) (difference / (1000 * 60));
                    seconds = (int) (difference / 1000);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (days == 0) {
                    if (hours == 0) {
                        if (minutes == 0) {
                            if (seconds == 0) {
                                lastPosted.setText("Just now");
                            } else {
                                if (seconds == 1) lastPosted.setText(seconds + " second ago");
                                else lastPosted.setText(seconds + " seconds ago");
                            }
                        } else {
                            if (minutes == 1) lastPosted.setText(minutes + " minute ago");
                            lastPosted.setText(minutes + " minutes ago");
                        }
                    } else {
                        if (hours == 1) lastPosted.setText(hours + " hour ago");
                        else lastPosted.setText(hours + " hours ago");
                    }
                } else {
                    if (days == 1) lastPosted.setText(days + " day ago");
                    else lastPosted.setText(days + " days ago");
                }
            }

            @Override
            public void onFailure(Call<ModelNoteBook> call, Throwable t) {
            }
        });

    }
    @Override
    public void onNewIntent(Intent intent) {
        this.setIntent(intent);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.ib_edit_note:
                intent = new Intent(getApplicationContext(), EditNoteActivity.class);
                startActivity(intent);
                break;

            case R.id.ib_fullscreen:
                intent = new Intent(getApplicationContext(), NotesSliderActivity.class);
                startActivity(intent);
                break;

            case R.id.ib_share:
                share_link();
                break;

            case R.id.iv_notes:
                intent = new Intent(getApplicationContext(), NotesSliderActivity.class);
                startActivity(intent);
                break;

            case R.id.tb_bookmark:
                new bookmarkNote().execute();
                break;

            case R.id.b_rate:
                rateNoteDialog = new RateNoteDialog(this);
                rateNoteDialog.show();
                Window window = rateNoteDialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                break;

            default:
                break;
        }

    }
    void share_link()
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();
        BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .addContentMetadata("noteBookId", noteBookId);

        LinkProperties linkProperties = new LinkProperties()
                .setChannel("whatsapp")
                .setFeature("sharing")
                .addControlParameter("$desktop_url", "http://campusconnect-2016.herokuapp.com/notebook?id=" + noteBookId);

        final Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.setPackage("com.whatsapp");
        branchUniversalObject.generateShortUrl(this, linkProperties, new Branch.BranchLinkCreateListener() {
            @Override
            public void onLinkCreate(String url, BranchError error) {
                if (error == null) {
                    Log.i("MyApp", "got my Branch link to share: " + url);
                    sendIntent.putExtra(Intent.EXTRA_TEXT,url);
                    progressDialog.dismiss();
                    startActivityForResult(sendIntent,1);
                }
            }
        });
    }

    class bookmarkNote extends AsyncTask<String, String, String> {

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
            try {
                url = new URL(FragmentCourses.BASE_URL + "bookmarkNoteBook");
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.connect();
                DataOutputStream os = new DataOutputStream(connection.getOutputStream());
                body.put("profileId", getSharedPreferences("CC", MODE_PRIVATE).getString("profileId", ""));
                body.put("noteBookId", noteBookId);
                os.write(body.toString().getBytes());
                os.flush();
                os.close();
                int a = connection.getResponseCode();
                Log.i("sw32bookmark", a + "");
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
            } else {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(noteBookId);
            }
            progressDialog.dismiss();
        }
    }

    public class RateNoteDialog extends Dialog {

        public Activity c;
        public Dialog d;
        public String id;

        @Bind(R.id.ratingBar)
        RatingBar rating;

        @Bind(R.id.b_submit_rating)
        Button submit_rate;

        public RateNoteDialog(Activity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
            this.c = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.rate_note_dialog);
            ButterKnife.bind(this);

            LayerDrawable stars = (LayerDrawable) rating.getProgressDrawable();
            LayerDrawable progress = (LayerDrawable)rating.getProgressDrawable();
//            DrawableCompat.setTint(progress.getDrawable(0),Color.WHITE);
            DrawableCompat.setTint(progress.getDrawable(1),Color.YELLOW);
            DrawableCompat.setTint(progress.getDrawable(2),Color.YELLOW);
//            stars.getDrawable(2).setColorFilter(Color.rgb(255,247,151), PorterDuff.Mode.SRC_ATOP);

            submit_rate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Retrofit retrofit = new Retrofit.
                            Builder()
                            .baseUrl(MyApi.BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    MyApi myApi = retrofit.create(MyApi.class);
                    MyApi.rateNoteBook body = new MyApi.rateNoteBook(getSharedPreferences("CC",MODE_PRIVATE).getString("profileId","fakedesu"),noteBookId,rating.getRating());
                    Call<ModelRate> call = myApi.rate(body);
                    try {
                        call.enqueue(new Callback<ModelRate>() {
                            @Override
                            public void onResponse(Call<ModelRate> call, Response<ModelRate> response) {

                            }

                            @Override
                            public void onFailure(Call<ModelRate> call, Throwable t) {

                            }
                        });
                        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(NotePageActivity.this);
                        Bundle params = new Bundle();
                        params.putString("rated_notes",rating.getRating()+" stars");
                        firebaseAnalytics.logEvent("notes_rate",params);
                        rateNoteDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            rating.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    submit_rate.setVisibility(View.VISIBLE);
                    return false;
                }
            });

            rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                }
            });

        }
    }

    //Function for fragment selection and commits
    public void displayView(int viewId){
        switch (viewId) {
            case R.id.item_timetable:
                at_home=true;
                Intent intent_home = new Intent(NotePageActivity.this,HomeActivity2.class);
                startActivity(intent_home);
                break;
            case R.id.item_add_course:
                fragment = new FragmentAddCourse();
                frag_title = "Add Course";
                at_home=false;
                break;
            case R.id.item_bookmark:
                Intent intent_profile = new Intent(NotePageActivity.this,ProfilePageActivity.class);
                startActivity(intent_profile);
                at_home=false;
                break;
            case R.id.item_getting_points:
                fragment = new FragmentPointsInfo();
                frag_title = "Getting Points";
                at_home=false;
                break;
            case R.id.item_invite:
                fragment = new FragmentInvite();
                frag_title = "Invite";
                at_home=false;
                break;
            case R.id.item_settings:
                fragment = new FragmentSettings();
                frag_title = "Settings";
                at_home=false;
                break;
            case R.id.item_logout:
                at_home=true;
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                Intent intent = new Intent(NotePageActivity.this,GoogleSignInActivity.class);
                intent.putExtra("logout","temp");
                FirebaseAuth.getInstance().signOut();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.item_t_and_c:
                fragment = new FragmentTerms();
                frag_title = "Terms and Conditions";
                at_home=false;
                break;
            case R.id.item_rate:
                fragment = new FragmentRate();
                frag_title = "Rate App";
                at_home=false;
                break;
            case R.id.item_feedback:
                fragment = new FragmentFeedback();
                frag_title = "Feedback";
                at_home=false;
                break;
            case R.id.item_about:
                fragment = new FragmentAbout();
                frag_title = "About Us";
                at_home=false;
                break;
            default:
                Toast.makeText(getApplicationContext(), "Something's Wrong", Toast.LENGTH_SHORT).show();
                break;
        }
        if (fragment != null) {
            home_title.setText(frag_title);
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            //fragmentTransaction.remove(getSupportFragmentManager().findFragmentById(R.id.frame));
            Fragment temp  = getSupportFragmentManager().findFragmentById(R.id.frame);

            if(temp==homefrag) {
                if(!at_home) {
                    fragmentTransaction.add(R.id.frame, fragment);
                    fragmentTransaction.commit();
                }
            }
            else
            {
                if(!at_home) {
                    fragmentTransaction.remove(temp);
                    fragmentTransaction.add(R.id.frame, fragment);
                    fragmentTransaction.commit();
                }
                else
                {
                    fragmentTransaction.remove(temp);
                    fragmentTransaction.commit();
                }
            }

        }
    }

    @Override
    public void onBackPressed() {
//Go to home if the drawer is closed and the we are not on the HomeFragment (at_home flag checks for the latter)
        if(at_home==false && !drawerLayout.isDrawerOpen(GravityCompat.START)) {
//Unchecking all the drawer menu items before going back to home
            int size = navigationView.getMenu().size();
            for (int i = 0; i < size; i++) {
                navigationView.getMenu().getItem(i).setChecked(false);
            }
//Opening the HomeFragment
            frag_title="Note";
            home_title.setText(frag_title);
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            fragmentTransaction.remove(getSupportFragmentManager().findFragmentById(R.id.frame));
            fragmentTransaction.commit();
            at_home = true;
        }else if(at_home==true && !drawerLayout.isDrawerOpen(GravityCompat.START)){
//Implementation of "Click back again to exit"

            super.onBackPressed();
        }
        else
            drawerLayout.closeDrawers();
    }
}

