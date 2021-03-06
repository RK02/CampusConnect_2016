package com.campusconnect;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.campusconnect.POJO.ModelFeed;
import com.campusconnect.POJO.MyApi;
import com.campusconnect.fragment.Drawer.FragmentAbout;
import com.campusconnect.fragment.Drawer.FragmentAddCourse;
import com.campusconnect.fragment.Drawer.FragmentHome;
import com.campusconnect.fragment.Drawer.FragmentInvite;
import com.campusconnect.fragment.Drawer.FragmentRate;
import com.campusconnect.fragment.Drawer.FragmentTerms;
import com.campusconnect.fragment.Home.FragmentCourses;
import com.campusconnect.slidingtab.SlidingTabLayout_home;
import com.campusconnect.viewpager.ViewPagerAdapter_profile;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.doorbell.android.Doorbell;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by RK on 04/06/2016.
 */
public class ProfilePageActivity extends AppCompatActivity implements FloatingActionsMenu.OnFloatingActionsMenuUpdateListener, View.OnClickListener{

    @Bind(R.id.drawer)
    DrawerLayout drawerLayout;
    @Bind(R.id.navigation_view)
    NavigationView navigationView;
    @Bind(R.id.frame)
    FrameLayout fragment_frame;

    @Bind(R.id.pager_course)
    ViewPager profile_pager;

    @Bind(R.id.profile_image)
    ImageView profile_image;
     @Bind(R.id.tv_username)
    TextView profile_name;


    @Bind(R.id.tabs_course)
    SlidingTabLayout_home profile_tabs;

    @Bind(R.id.ib_back)
    ImageButton back_button;

    @Bind(R.id.ib_edit_profile)
    ImageButton edit_profile_button;

    @Bind(R.id.container_fab)
    FrameLayout fab_menu_container;

    @Bind(R.id.fab_menu)
    FloatingActionsMenu fabMenu;

    ViewPagerAdapter_profile profile_adapter;
    CharSequence Titles[] = {"Bookmarked", "Uploads"};
    int Numboftabs = 2;

    int defaultTabPosition=0;

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
    FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        //Drawer stuff
        home_title = (TextView) findViewById(R.id.tv_title);
        toolbar = (Toolbar) findViewById (R.id.toolbar);
        setSupportActionBar(toolbar);
//        homefrag = new FragmentHome();
        //Setting up Header View
        headerView = getLayoutInflater().inflate(R.layout.header, navigationView, false);
        navigationView.addHeaderView(headerView);
        ImageView imageView = (ImageView) headerView.findViewById(R.id.profile_image);

        Picasso.with(ProfilePageActivity.this)
                .load(getSharedPreferences("CC",MODE_PRIVATE).getString("photourl","fakedesu")).error(R.mipmap.ic_launcher)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .placeholder(R.mipmap.ccnoti)
                .into(imageView);
        ((TextView)headerView.findViewById(R.id.tv_username)).setText(getSharedPreferences("CC",MODE_PRIVATE).getString("profileName","PLACEHOLDER"));

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

        //Setting FAB container's background to be fully transparent by default
        fab_menu_container.getBackground().setAlpha(0);

        profile_pager = (ViewPager) findViewById(R.id.pager_course);
        profile_tabs = (SlidingTabLayout_home) findViewById(R.id.tabs_course);
        profile_adapter = new ViewPagerAdapter_profile(getSupportFragmentManager(), Titles, Numboftabs, this);
        profile_pager.setAdapter(profile_adapter);
        profile_pager.setCurrentItem(defaultTabPosition);
        profile_tabs.setDistributeEvenly(true);
        profile_tabs.setViewPager(profile_pager);

        //Listener to define layouts for FAB expanded and collapsed modes
        fabMenu.setOnFloatingActionsMenuUpdateListener(this);

        Retrofit retrofit = new Retrofit.
                Builder()
                .baseUrl(MyApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyApi myApi = retrofit.create(MyApi.class);
        Call<ModelFeed> call = myApi.getFeed(getSharedPreferences("CC", Context.MODE_PRIVATE).getString("profileId",""));
        call.enqueue(new Callback<ModelFeed>() {
            @Override
            public void onResponse(Call<ModelFeed> call, Response<ModelFeed> response) {

                Log.i("sw32", "" + response.code());
                ModelFeed modelFeed = response.body();
                if (modelFeed != null) {
                    Picasso.with(ProfilePageActivity.this)
                            .load(modelFeed.getPhotoUrl())
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .into(profile_image);
                    profile_name.setText(modelFeed.getProfileName());
                }
            }

            @Override
            public void onFailure(Call<ModelFeed> call, Throwable t) {

            }
        });



        //OnClickListeners
        back_button.setOnClickListener(this);
        edit_profile_button.setOnClickListener(this);

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

        switch (view.getId()){

            case R.id.ib_back:
                finish();
                break;

            case R.id.ib_edit_profile:
                Intent intent_edit_profile = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(intent_edit_profile);
                break;

            default:
                break;
        }

    }

    //Layout definition when FAB is expanded
    @Override
    public void onMenuExpanded() {
        Bundle params = new Bundle();
        params.putString("open_menu","yay");
        firebaseAnalytics.logEvent("fab_pressed_event", params);

        fabMenu.findViewById(R.id.fab_event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent exam = new Intent(ProfilePageActivity.this,AddEventActivity.class);
                exam.putExtra("Mode",1);
                startActivity(exam);
                fabMenu.collapse();
            }
        });
        fabMenu.findViewById(R.id.fab_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent assignment = new Intent(ProfilePageActivity.this,AddEventActivity.class);
                assignment.putExtra("Mode",2);
                startActivity(assignment);
                fabMenu.collapse();
            }
        });
        fabMenu.findViewById(R.id.fab_others).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle params = new Bundle();
                params.putString("event","notes");
                firebaseAnalytics.logEvent("notes_upload_start",params);
                startActivity(new Intent(ProfilePageActivity.this,UploadPicturesActivity.class));
                fabMenu.collapse();
            }
        });
        fab_menu_container.getBackground().setAlpha(230);
        fab_menu_container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                fabMenu.collapse();
                return true;
            }
        });
    }

    //Layout definition when FAB is collapsed
    @Override
    public void onMenuCollapsed() {
        fab_menu_container.getBackground().setAlpha(0);
        fab_menu_container.setOnTouchListener(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApp.activityResumed();
        ConnectionChangeReceiver.broadcast(this);
        Picasso.with(ProfilePageActivity.this)
                .load(getSharedPreferences("CC",MODE_PRIVATE).getString("photourl","fakedesu"))
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .error(R.mipmap.ccnoti)
                .placeholder(R.mipmap.ccnoti)
                .into(profile_image);
    }

    //Function for fragment selection and commits
    public void displayView(int viewId){
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (viewId) {
            case R.id.item_timetable:
                at_home=true;
                Intent intent_home = new Intent(ProfilePageActivity.this,HomeActivity2.class);
                startActivity(intent_home);
                finish();
                break;
            case R.id.item_add_course:
                fragment = new FragmentAddCourse();
                frag_title = "Add Course";
                at_home=false;
                break;
            case R.id.item_bookmark:
                Intent intent_profile = new Intent(ProfilePageActivity.this,ProfilePageActivity.class);
                startActivity(intent_profile);
                frag_title = "Profile";
                fragment = null;
                at_home=true;
                break;
            case R.id.item_invite:
                fragment = new FragmentInvite();
                frag_title = "Invite";
                at_home=false;
                break;
            case R.id.item_logout:
                at_home=true;
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                Intent intent = new Intent(ProfilePageActivity.this,GoogleSignInActivity.class);
                intent.putExtra("logout","temp");
                FirebaseAuth.getInstance().signOut();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.item_t_and_c:
                frag_title = "Profile";
                at_home=true;
                fragment = null;
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://campusconnect.cc/faq#terms"));
                startActivity(browserIntent);
                break;
            case R.id.item_rate:
                fragment = null;
                frag_title = "Profile";
                //final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                final String appPackageName = "com.campusconnect";
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                at_home=true;
                break;
            case R.id.item_feedback:
                Doorbell doorbellDialog = new Doorbell(this, 2764, "czPslyxNo9JTzQog5JcrWBlRbHVSQKyqnieLG8QDVZNK1hesEJtPD9E0MRuBbeW0");
                doorbellDialog.setEmail(getSharedPreferences("CC",MODE_PRIVATE).getString("email","")); // Prepopulate the email address field
                doorbellDialog.setName(getSharedPreferences("CC",MODE_PRIVATE).getString("profileName","")); // Set the name of the user (if known)
                doorbellDialog.show();
                doorbellDialog.addProperty("loggedIn", true); // Optionally add some properties
                doorbellDialog.setEmailFieldVisibility(View.GONE); // Hide the email field, since we've filled it in already
                doorbellDialog.setPoweredByVisibility(View.GONE);
                doorbellDialog.setMessageHint("Feel free to tell us anything!");
                doorbellDialog.setOnFeedbackSentCallback(new io.doorbell.android.callbacks.OnFeedbackSentCallback() {
                    @Override
                    public void handle(String message) {
                        // Show the message in a different way, or use your own message!
                        Toast.makeText(ProfilePageActivity.this, "Thanks for writing to us!", Toast.LENGTH_LONG).show();
                    }
                });
                fragment = null;
                frag_title = "Profile";
                at_home=true;
                break;

            default:
                Toast.makeText(getApplicationContext(), "Something's Wrong", Toast.LENGTH_SHORT).show();
                break;
        }
        if (fragment != null) {
            home_title.setText(frag_title);

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
        else
        {
            Fragment temp  = getSupportFragmentManager().findFragmentById(R.id.frame);
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
        if(at_home==false && !drawerLayout.isDrawerOpen(GravityCompat.START)) {
//Unchecking all the drawer menu items before going back to home
            int size = navigationView.getMenu().size();
            for (int i = 0; i < size; i++) {
                navigationView.getMenu().getItem(i).setChecked(false);
            }
//Opening the HomeFragment
            fabMenu.setVisibility(View.VISIBLE);
            frag_title="Profile";
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


    @Override
    protected void onPause() {
        super.onPause();
        MyApp.activityPaused();
    }
}

