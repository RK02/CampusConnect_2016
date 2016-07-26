package com.campusconnect.cc_reboot;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.campusconnect.cc_reboot.POJO.ModelAssignment;
import com.campusconnect.cc_reboot.POJO.MyApi;
import com.campusconnect.cc_reboot.fragment.Drawer.FragmentAbout;
import com.campusconnect.cc_reboot.fragment.Drawer.FragmentAddCourse;
import com.campusconnect.cc_reboot.fragment.Drawer.FragmentFeedback;
import com.campusconnect.cc_reboot.fragment.Drawer.FragmentGifts;
import com.campusconnect.cc_reboot.fragment.Drawer.FragmentInvite;
import com.campusconnect.cc_reboot.fragment.Drawer.FragmentPointsInfo;
import com.campusconnect.cc_reboot.fragment.Drawer.FragmentRate;
import com.campusconnect.cc_reboot.fragment.Drawer.FragmentSettings;
import com.campusconnect.cc_reboot.fragment.Drawer.FragmentTerms;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.campusconnect.cc_reboot.fragment.Home.FragmentCourses;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.*;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by RK on 04/06/2016.
 */
public class AssignmentPageActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.drawer)
    DrawerLayout drawerLayout;
    @Bind(R.id.navigation_view)
    NavigationView navigationView;
    @Bind(R.id.frame)
    FrameLayout fragment_frame;

    @Bind(R.id.iv_assignment)
    ImageView assignment_last_page;

    @Bind(R.id.container_assignment)
    RelativeLayout assignments_container;

    @Bind(R.id.tv_assignment_name)
    TextView assignment_name;
    @Bind(R.id.tv_uploader)
    TextView uploader;
    @Bind(R.id.tv_date_posted)
    TextView date_posted;
    @Bind(R.id.tv_description)
    TextView description;
    @Bind(R.id.tv_views_count)
    TextView views;
    @Bind(R.id.tv_due_date)
    TextView dueDate;

    @Bind(R.id.ib_back)
    ImageButton back_button;
    @Bind(R.id.ib_edit_note)
    ImageButton edit_note_button;
    @Bind(R.id.ib_fullscreen)
    ImageButton fullscreen_button;
    @Bind(R.id.ib_share)
    ImageButton share_note_button;
    @Bind(R.id.ib_flag)
    ImageButton flag_button;

    @Bind(R.id.tb_remind_me)
    ToggleButton remind_button;

    String assignmentId;
    int courseColor;
    Intent intent;

    //Flags
    boolean doubleBackToExitPressedOnce = false;
    boolean at_home = true;
    String frag_title = "";
    private Toolbar toolbar;
    private Fragment fragment = null;
    Fragment homefrag;
    View headerView;
    String courseNamePlaceHolder = "";
    public static TextView home_title;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
        ButterKnife.bind(this);

        //Drawer stuff
        home_title = (TextView) findViewById(R.id.tv_title);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        homefrag = new FragmentHome();
        //Setting up Header View
        headerView = getLayoutInflater().inflate(R.layout.header, navigationView, false);
        navigationView.addHeaderView(headerView);
        ImageView imageView = (ImageView) headerView.findViewById(R.id.profile_image);

        Picasso.with(AssignmentPageActivity.this)
                .load(getSharedPreferences("CC",MODE_PRIVATE).getString("photourl","fakedesu")).error(R.mipmap.ic_launcher)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .placeholder(R.mipmap.ccnoti)
                .into(imageView);
        ((TextView)headerView.findViewById(R.id.tv_username)).setText(getSharedPreferences("CC",MODE_PRIVATE).getString("profileName","PLACEHOLDER"));
        ((TextView)headerView.findViewById(R.id.tv_points)).setText(FragmentCourses.profilePoints);

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
        assignments_container.setBackgroundColor(courseColor);

        assignmentId = getIntent().getStringExtra("assignmentId");

        Retrofit retrofit = new Retrofit.
                Builder()
                .baseUrl(MyApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyApi myApi = retrofit.create(MyApi.class);
        MyApi.getAssignmentRequest body = new MyApi.getAssignmentRequest(assignmentId, getSharedPreferences("CC", Context.MODE_PRIVATE).getString("profileId", ""));
        Call<ModelAssignment> call = myApi.getAssignment(body);
        call.enqueue(new Callback<ModelAssignment>() {
            @Override
            public void onResponse(Call<ModelAssignment> call, Response<ModelAssignment> response) {
                ModelAssignment assignment = response.body();
                assignment_name.setText(assignment.getAssignmentTitle());
                uploader.setText(assignment.getUploaderName());
                date_posted.setText(assignment.getLastUpdated().substring(0, 10));
                dueDate.setText(assignment.getDueDate());
                views.setText(assignment.getViews());
                description.setText(assignment.getAssignmentDesc());
            }

            @Override
            public void onFailure(Call<ModelAssignment> call, Throwable t) {

            }
        });

        //OnClickListeners
        back_button.setOnClickListener(this);
        edit_note_button.setOnClickListener(this);
        fullscreen_button.setOnClickListener(this);
        share_note_button.setOnClickListener(this);
        flag_button.setOnClickListener(this);
        assignment_last_page.setOnClickListener(this);

        remind_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    FirebaseMessaging.getInstance().subscribeToTopic(assignmentId);
                }
                else
                {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(assignmentId);
                }
            }
        });

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
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.ib_back:
                finish();
                break;

            case R.id.ib_edit_note:
                intent = new Intent(getApplicationContext(), EditNoteActivity.class);
                startActivity(intent);

                break;

            case R.id.ib_fullscreen:
//                intent = new Intent(getApplicationContext(), NotesSliderActivity.class);
//                startActivity(intent);
                break;

            case R.id.ib_share:
                share_link();
                break;

            case R.id.ib_flag:
                Retrofit retrofit = new Retrofit.
                        Builder()
                        .baseUrl(MyApi.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                MyApi myApi = retrofit.create(MyApi.class);
                MyApi.reportRequest body= new MyApi.reportRequest(getSharedPreferences("CC",MODE_PRIVATE).getString("profileId",""),assignmentId,"");
                Call<Void> call = myApi.report(body);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(AssignmentPageActivity.this,"Thank you for the feedback. We will get back to you shortly",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });

                break;

            case R.id.iv_assignment:
//                intent = new Intent(getApplicationContext(), NotesSliderActivity.class);
//                startActivity(intent);
                break;



            default:
                break;
        }

    }

    //Function for fragment selection and commits
    public void displayView(int viewId) {
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        switch (viewId) {
            case R.id.item_timetable:
                at_home = true;
                Intent intent_home = new Intent(AssignmentPageActivity.this, HomeActivity2.class);
                startActivity(intent_home);
                finish();
                break;
            case R.id.item_add_course:
                fragment = new FragmentAddCourse();
                frag_title = "Add Course";
                at_home = false;
                break;
            case R.id.item_bookmark:
                Intent intent_profile = new Intent(AssignmentPageActivity.this, ProfilePageActivity.class);
                startActivity(intent_profile);
                fragment=null;
                frag_title = "Assignment";
                at_home = true;
                break;
            case R.id.item_getting_points:
                fragment = new FragmentPointsInfo();
                frag_title = "Getting Points";
                at_home = false;
                break;
            case R.id.item_invite:
                fragment = new FragmentInvite();
                frag_title = "Invite";
                at_home = false;
                break;
            case R.id.item_settings:
                fragment = new FragmentSettings();
                frag_title = "Settings";
                at_home = false;
                break;
            case R.id.item_logout:
                at_home = true;
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                Intent intent = new Intent(AssignmentPageActivity.this, GoogleSignInActivity.class);
                intent.putExtra("logout", "temp");
                FirebaseAuth.getInstance().signOut();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
            case R.id.item_t_and_c:
                fragment = new FragmentTerms();
                frag_title = "Terms and Conditions";
                at_home = false;
                break;
            case R.id.item_rate:
                fragment = new FragmentRate();
                frag_title = "Rate App";
                at_home = false;
                break;
            case R.id.item_feedback:
                fragment = new FragmentFeedback();
                frag_title = "Feedback";
                at_home = false;
                break;
            case R.id.item_about:
                fragment = new FragmentAbout();
                frag_title = "About Us";
                at_home = false;
                break;
            default:
                Toast.makeText(getApplicationContext(), "Something's Wrong", Toast.LENGTH_SHORT).show();
                break;
        }
        if (fragment != null) {
            home_title.setText(frag_title);
            //fragmentTransaction.remove(getSupportFragmentManager().findFragmentById(R.id.frame));
            Fragment temp = getSupportFragmentManager().findFragmentById(R.id.frame);

            if (temp == homefrag) {
                if (!at_home) {
                    fragmentTransaction.add(R.id.frame, fragment);
                    fragmentTransaction.commit();
                }
            } else {
                if (!at_home) {
                    fragmentTransaction.remove(temp);
                    fragmentTransaction.add(R.id.frame, fragment);
                    fragmentTransaction.commit();
                } else {
                    fragmentTransaction.remove(temp);
                    fragmentTransaction.commit();
                }
            }

        }
        else{
            Fragment temp = getSupportFragmentManager().findFragmentById(R.id.frame);
            if(temp!=null)
            {
                fragmentTransaction.remove(temp).commit();
                home_title.setText(frag_title);
            }

        }
    }

    @Override
    public void onBackPressed() {
//Go to home if the drawer is closed and the we are not on the HomeFragment (at_home flag checks for the latter)
        if (at_home == false && !drawerLayout.isDrawerOpen(GravityCompat.START)) {
//Unchecking all the drawer menu items before going back to home
            int size = navigationView.getMenu().size();
            for (int i = 0; i < size; i++) {
                navigationView.getMenu().getItem(i).setChecked(false);
            }
//Opening the HomeFragment
            frag_title = "Assignment";
            home_title.setText(frag_title);
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            fragmentTransaction.remove(getSupportFragmentManager().findFragmentById(R.id.frame));
            fragmentTransaction.commit();
            at_home = true;
        } else if (at_home == true && !drawerLayout.isDrawerOpen(GravityCompat.START)) {
//Implementation of "Click back again to exit"

            super.onBackPressed();
        } else
            drawerLayout.closeDrawers();
    }

    void share_link() {
        /*

        Share text
1) Hey, check out the notes for Course_name by Person_name on Campus Connect! link
2) Hey, check out the assignment for Course_name on Campus Connect! link
3) Hey, check out the exam for Course_name on Campus Connect! link
4) Hey, check out my notes for Course_name on Campus Connect! link

         */

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();
        BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .addContentMetadata("assignmentId", assignmentId);

        LinkProperties linkProperties = new LinkProperties()
                .setChannel("whatsapp")
                .setFeature("sharing")
                .addControlParameter("$desktop_url", "http://campusconnect-2016.herokuapp.com/assignment?id=" + assignmentId);

        final Intent sendIntent = new Intent();
        final String shareText = " Hey, check out the assignment for" + courseNamePlaceHolder+ "on Campus Connect!\n";
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        branchUniversalObject.generateShortUrl(this, linkProperties, new Branch.BranchLinkCreateListener() {
            @Override
            public void onLinkCreate(String url, BranchError error) {
                if (error == null) {
                    Log.i("MyApp", "got my Branch link to share: " +shareText +  url);
                    sendIntent.putExtra(Intent.EXTRA_TEXT,url);
                    progressDialog.dismiss();
                    startActivityForResult(Intent.createChooser(sendIntent, "Share with..."),1);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();


        if (Branch.isAutoDeepLinkLaunch(this)) {
            try {
                String autoDeeplinkedValue = Branch.getInstance().getLatestReferringParams().getString("assignmentId");
                assignmentId = autoDeeplinkedValue;
                Log.i("sw32Deep", "Launched by Branch on auto deep linking!"
                        + "\n\n" + autoDeeplinkedValue);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            assignmentId = getIntent().getStringExtra("assignmentId");
        }
        courseColor = getIntent().getIntExtra("CourseColor", Color.rgb(224, 224, 224));
        assignments_container.setBackgroundColor(courseColor);
        Retrofit retrofit = new Retrofit.
                Builder()
                .baseUrl(MyApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyApi myApi = retrofit.create(MyApi.class);
        MyApi.getAssignmentRequest body = new MyApi.getAssignmentRequest(assignmentId, getSharedPreferences("CC", Context.MODE_PRIVATE).getString("profileId", ""));
        Call<ModelAssignment> call = myApi.getAssignment(body);
        call.enqueue(new Callback<ModelAssignment>() {
            @Override
            public void onResponse(Call<ModelAssignment> call, Response<ModelAssignment> response) {
                ModelAssignment assignment = response.body();
                if(assignment!=null){
                courseNamePlaceHolder = assignment.getCourseName();
                assignment_name.setText(assignment.getAssignmentTitle());
                uploader.setText(assignment.getUploaderName());
                date_posted.setText(assignment.getLastUpdated().substring(0, 10));
                dueDate.setText(assignment.getDueDate());
                views.setText(assignment.getViews());
                description.setText(assignment.getAssignmentDesc());
                }
            }

            @Override
            public void onFailure(Call<ModelAssignment> call, Throwable t) {

            }
        });


    }
}


