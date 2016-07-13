package com.campusconnect.cc_reboot;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.campusconnect.cc_reboot.POJO.CustomNotification;
import com.campusconnect.cc_reboot.fragment.Drawer.FragmentAbout;
import com.campusconnect.cc_reboot.fragment.Drawer.FragmentAddCourse;
import com.campusconnect.cc_reboot.fragment.Drawer.FragmentFeedback;
import com.campusconnect.cc_reboot.fragment.Drawer.FragmentGifts;
import com.campusconnect.cc_reboot.fragment.Drawer.FragmentHome;
import com.campusconnect.cc_reboot.fragment.Drawer.FragmentInvite;
import com.campusconnect.cc_reboot.fragment.Drawer.FragmentPointsInfo;
import com.campusconnect.cc_reboot.fragment.Drawer.FragmentRate;
import com.campusconnect.cc_reboot.fragment.Drawer.FragmentSettings;
import com.campusconnect.cc_reboot.fragment.Drawer.FragmentTerms;
import com.campusconnect.cc_reboot.fragment.Home.FragmentCourses;
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
/**
 * Created by RK on 04/06/2016.
 */
public class HomeActivity2 extends AppCompatActivity implements FloatingActionsMenu.OnFloatingActionsMenuUpdateListener, View.OnClickListener{
    //Data binding
    @Bind(R.id.drawer)
    DrawerLayout drawerLayout;
    @Bind(R.id.navigation_view)
    NavigationView navigationView;
    @Bind(R.id.frame)
    FrameLayout fragment_frame;
    @Bind(R.id.ib_menu)
    ImageButton menu_button;
    @Bind(R.id.ib_search)
    ImageButton search_button;
    @Bind(R.id.ib_notification)
    ImageButton notification_button;
    public static TextView home_title;
    @Bind(R.id.container_fab)
    FrameLayout fab_menu_container;
    @Bind(R.id.fab_menu)
    FloatingActionsMenu fabMenu;
    //Flags
    boolean doubleBackToExitPressedOnce = false;
    boolean at_home=true;
    String frag_title="";
    private Toolbar toolbar;
    private LinearLayout acb_home;
    private Fragment fragment = null;
    GoogleApiClient mGoogleApiClient;
    Fragment homefrag;
    View headerView;
    private FirebaseAnalytics firebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_true);
        ButterKnife.bind(this);
//Setting FAB container's background to be fully transparent by default
        home_title = (TextView) findViewById(R.id.tv_title);
        if(getIntent().getExtras()!=null){
            String type = getIntent().getExtras().getString("type");
            String id = getIntent().getExtras().getString("id");
            Log.i("sw32notif", type + "::" +id);
        }

        fab_menu_container.getBackground().setAlpha(0);
        toolbar = (Toolbar) findViewById (R.id.toolbar);
        setSupportActionBar(toolbar);

        homefrag = new FragmentHome();
        //Setting up Header View
        headerView = getLayoutInflater().inflate(R.layout.header, navigationView, false);
        navigationView.addHeaderView(headerView);
        ImageView view = (ImageView) headerView.findViewById(R.id.profile_image);
        Picasso.with(HomeActivity2.this)
                .load(getSharedPreferences("CC",MODE_PRIVATE).getString("photourl","fakedesu")).error(R.mipmap.ic_launcher)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .placeholder(R.mipmap.ccnoti)
                .into(view);
        ((TextView)headerView.findViewById(R.id.tv_username)).setText(getSharedPreferences("CC",MODE_PRIVATE).getString("profileName","PLACEHOLDER"));
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        //Setting Home Fragment as default

//Setting Home Fragment as default
        frag_title = "Home";
        home_title.setText(frag_title);
        final android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, homefrag);
        fragmentTransaction.commit();
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
        //Listener to define layouts for FAB expanded and collapsed modes
        fabMenu.setOnFloatingActionsMenuUpdateListener(this);
        //OnClickListener Header View
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_temp = new Intent(getApplicationContext(), ProfilePageActivity.class);
                startActivity(intent_temp);
            }
        });

        //OnClickListeners
        menu_button.setOnClickListener(this);
        search_button.setOnClickListener(this);
        notification_button.setOnClickListener(this);
        ((TextView)headerView.findViewById(R.id.tv_points)).setText(FragmentCourses.profilePoints);
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

        if(getIntent().hasExtra("pendingIntentAction"))
        {
            CustomNotification.deleteAll(CustomNotification.class);
        }
        ImageView view = (ImageView) headerView.findViewById(R.id.profile_image);
        Picasso.with(HomeActivity2.this).
                load(getSharedPreferences("CC",MODE_PRIVATE).getString("photourl","fakedesu")).error(R.mipmap.ic_launcher)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .placeholder(R.mipmap.ccnoti)
                .into(view);
        ((TextView)headerView.findViewById(R.id.tv_points)).setText(FragmentCourses.profilePoints);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ib_menu:
                if(drawerLayout.isDrawerOpen(GravityCompat.START))
                    drawerLayout.closeDrawer(Gravity.LEFT);
                else
                    drawerLayout.openDrawer(Gravity.LEFT);
                break;
            case R.id.ib_search:
                Intent intent_search = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent_search);
                break;
            case R.id.ib_notification:
                Intent intent_notification = new Intent(getApplicationContext(), NotificationActivity.class);
                startActivity(intent_notification);
                break;
            default:
                Toast.makeText(this,"Something's wrong",Toast.LENGTH_SHORT);
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
                Intent exam = new Intent(HomeActivity2.this,AddEventActivity.class);
                exam.putExtra("Mode",1);
                startActivity(exam);
                fabMenu.collapse();
            }
        });
        fabMenu.findViewById(R.id.fab_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent assignment = new Intent(HomeActivity2.this,AddEventActivity.class);
                assignment.putExtra("Mode",2);
                startActivity(assignment);
                fabMenu.collapse();
            }
        });
        fabMenu.findViewById(R.id.fab_others).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity2.this,UploadPicturesActivity.class));
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
    //Function for fragment selection and commits
    public void displayView(int viewId){
        switch (viewId) {
            case R.id.item_timetable:
                at_home=true;
                fragment = homefrag;
                frag_title = "Home";
                break;
            case R.id.item_add_course:
                fragment = new FragmentAddCourse();
                frag_title = "Add Course";
                at_home=false;
                break;
            case R.id.item_gifts:
                fragment = new FragmentGifts();
                frag_title = "Gifts";
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
                Intent intent = new Intent(HomeActivity2.this,GoogleSignInActivity.class);
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
            if(at_home==false)
                fabMenu.setVisibility(View.GONE);
            else
                fabMenu.setVisibility(View.VISIBLE);

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
            fabMenu.setVisibility(View.VISIBLE);
            frag_title="Home";
            home_title.setText(frag_title);
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            fragmentTransaction.remove(getSupportFragmentManager().findFragmentById(R.id.frame));
            fragmentTransaction.commit();
            at_home = true;
        }else if(at_home==true && !drawerLayout.isDrawerOpen(GravityCompat.START)){
//Implementation of "Click back again to exit"
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
//To keep track of how long the user has been waiting
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
        else
            drawerLayout.closeDrawers();
    }
}