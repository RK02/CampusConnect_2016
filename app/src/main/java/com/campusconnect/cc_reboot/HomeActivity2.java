package com.campusconnect.cc_reboot;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.campusconnect.cc_reboot.POJO.CustomNotification;
import com.campusconnect.cc_reboot.POJO.ModelNotification;
import com.campusconnect.cc_reboot.POJO.ModelNotificationList;
import com.campusconnect.cc_reboot.POJO.MyApi;
import com.campusconnect.cc_reboot.adapter.NotificationAdapter;
import com.campusconnect.cc_reboot.adapter.TimetableAdapter;
import com.campusconnect.cc_reboot.auxiliary.AlphaAdapter_reverse;
import com.campusconnect.cc_reboot.auxiliary.AlphaInAnimationAdapter;
import com.campusconnect.cc_reboot.auxiliary.FastBlur;
import com.campusconnect.cc_reboot.auxiliary.NetworkDownDialog;
import com.campusconnect.cc_reboot.auxiliary.ScaleAdapter_reverse;
import com.campusconnect.cc_reboot.auxiliary.ScaleInAnimationAdapter;
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

import java.util.ArrayList;
import java.util.List;

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
public class HomeActivity2 extends AppCompatActivity implements FloatingActionsMenu.OnFloatingActionsMenuUpdateListener, View.OnClickListener{

    AlphaInAnimationAdapter alphaAdapter;
    ScaleInAnimationAdapter scaleAdapter;
    ScaleAdapter_reverse scale_back;
    AlphaAdapter_reverse alpha_back;

    NotificationAdapter mNotificationAdapter;
    LinearLayoutManager mLayoutManager;

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
    @Bind(R.id.ib_close)
    ImageButton close_button;

    public static TextView home_title;
    @Bind(R.id.container_fab)
    FrameLayout fab_menu_container;
    @Bind(R.id.fab_menu)
    FloatingActionsMenu fabMenu;

    @Bind(R.id.rv_notification)
    RecyclerView notification_list;
    @Bind(R.id.tv_title_notification)
    TextView notification_title;
    @Bind(R.id.blur)
    ImageView bk_blur;
    @Bind(R.id.notification_layout)
    FrameLayout layout_notification;
    List<ModelNotification> notifications;

    Interpolator interpolator_notification = new DecelerateInterpolator();

    //Flags
    boolean doubleBackToExitPressedOnce = false;
    boolean at_home=true;
    String frag_title="";
    private Toolbar toolbar;
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


        Retrofit retrofit = new Retrofit.
                Builder()
                .baseUrl(MyApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyApi myApi = retrofit.create(MyApi.class);
        Call<ModelNotificationList> call = myApi.getNotifications(getSharedPreferences("CC",MODE_PRIVATE).getString("profileId",""));
        call.enqueue(new Callback<ModelNotificationList>() {
            @Override
            public void onResponse(Call<ModelNotificationList> call, Response<ModelNotificationList> response) {
                if(response!=null) {
                    Log.i("sw32notifications",response.code()+"");
                    ModelNotificationList modelNotificationList = response.body();
                    if(modelNotificationList!=null)
                           notifications= modelNotificationList.getNotificationList();

                    layout_notification.setVisibility(View.GONE);
                    mNotificationAdapter = new NotificationAdapter(HomeActivity2.this, notifications);

                    mLayoutManager = new LinearLayoutManager(HomeActivity2.this);

                    notification_list.setLayoutManager(mLayoutManager);
                    notification_list.setItemAnimator(new DefaultItemAnimator());

                    alphaAdapter = new AlphaInAnimationAdapter(mNotificationAdapter);
                    scaleAdapter = new ScaleInAnimationAdapter(
                            alphaAdapter);
                    scale_back = new ScaleAdapter_reverse(
                            mNotificationAdapter);
                    alpha_back = new AlphaAdapter_reverse(scale_back);
                }
            }

            @Override
            public void onFailure(Call<ModelNotificationList> call, Throwable t) {

            }
        });

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

                if (layout_notification.getVisibility() == View.VISIBLE) {

                    some_function();
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                }else{
                    layout_notification.setVisibility(View.VISIBLE);
                    notification_title.setVisibility(View.VISIBLE);
                    close_button.setVisibility(View.VISIBLE);
//                    notificationCount = 0;
//                    tv_notification_count.setText("");
//                    tv_notification_count.setVisibility(View.INVISIBLE);

                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    fragment_frame.setVisibility(View.GONE);

                    Animation animation = new TranslateAnimation(0, 0, -100, 0);
                    animation.setInterpolator(interpolator_notification);
                    animation.setDuration(650);
                    notification_title.startAnimation(animation);


                    Animation animate_close_button = new TranslateAnimation(
                            -100, 0, 0, 0);
                    animate_close_button
                            .setInterpolator(interpolator_notification);
                    animate_close_button.setStartOffset(500);
                    animate_close_button.setDuration(650);
                    animate_close_button.setFillAfter(true);
                    close_button.startAnimation(animate_close_button);

                    Animation fadeOut = new AlphaAnimation(1, 0);
                    fadeOut.setInterpolator(new DecelerateInterpolator());
                    fadeOut.setDuration(300);
                    fadeOut.setFillAfter(true);

                    fragment_frame.startAnimation(fadeOut);

                    Animation fadeIn = new AlphaAnimation(0, 1);
                    fadeIn.setInterpolator(new DecelerateInterpolator());
                    fadeIn.setFillAfter(true);
                    fadeIn.setStartOffset(100);
                    fadeIn.setDuration(300);


//                    notification_title.setVisibility(View.VISIBLE);

                    home_title.setVisibility(View.GONE);
                    bk_blur.setVisibility(View.VISIBLE);
                    menu_button.setVisibility(View.GONE);
                    bk_blur.startAnimation(fadeIn);
                    //notification_clicked = true;

                    applyBlur();

                    Handler handler_new = new Handler();
                    handler_new.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            notification_list.setVisibility(View.VISIBLE);
                            alphaAdapter.setStartPosition(-1);
                            scaleAdapter.setStartPosition(-1);
                            notification_list.setAdapter(scaleAdapter);

                        }
                    }, 500);




                    close_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            some_function();
                            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                        }
                    });
                }

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
                Bundle params = new Bundle();
                params.putString("event","notes");
                firebaseAnalytics.logEvent("notes_upload_start",params);
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
            case R.id.item_bookmark:
                Intent intent_profile = new Intent(HomeActivity2.this,ProfilePageActivity.class);
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
//                NetworkDownDialog networkdownDialog = new NetworkDownDialog((Activity) HomeActivity2.this,fragment_frame);
//                Window window = networkdownDialog.getWindow();
//                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                networkdownDialog.show();
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

    //Notification animation
    void some_function()
    {
        AnimationSet animation_recycler = new AnimationSet(true);
        Animation fly_down = new TranslateAnimation(0, 0, 0, notification_list.getHeight()>>1);
        Animation fade_out = new AlphaAnimation(1, 0);
        fly_down.setInterpolator(interpolator_notification);
        fade_out.setInterpolator(interpolator_notification);
        fly_down.setDuration(200);
        fade_out.setDuration(200);
        animation_recycler.addAnimation(fly_down);
        animation_recycler.addAnimation(fade_out);
        notification_list.setAnimation(animation_recycler);

        Animation animation = new TranslateAnimation(0, 0, 0, -100);
        animation.setInterpolator(interpolator_notification);
        animation.setDuration(650);
        animation.setFillAfter(true);
        notification_title.startAnimation(animation);  //This is the title text
        notification_title.setVisibility(View.GONE);

        Animation animate_back_button = new TranslateAnimation(
                0, -100, 0, 0);
        animate_back_button
                .setInterpolator(interpolator_notification);
        animate_back_button.setDuration(650);
        animate_back_button.setFillAfter(true);
        close_button.startAnimation(animate_back_button);
        close_button.setVisibility(View.GONE);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new DecelerateInterpolator());
        fadeOut.setDuration(300);

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setFillAfter(true);
        fadeIn.setStartOffset(100);
        fadeIn.setDuration(300);

        notification_list.setVisibility(View.GONE);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                layout_notification.setVisibility(View.GONE);

                Animation fadeOut = new AlphaAnimation(1, 0);
                fadeOut.setInterpolator(new DecelerateInterpolator());
                fadeOut.setDuration(300);
                bk_blur.startAnimation(fadeOut);
                bk_blur.setVisibility(View.GONE);

                Animation fadeIn = new AlphaAnimation(0, 1);
                fadeIn.setInterpolator(new DecelerateInterpolator());
                fadeIn.setStartOffset(100);
                fadeIn.setDuration(300);
                fragment_frame.startAnimation(fadeIn);
                fragment_frame.setVisibility(View.VISIBLE);

                Animation animation_hamburger= new TranslateAnimation(0, 0, -100, 0);
                animation_hamburger.setInterpolator(interpolator_notification);
                animation_hamburger.setDuration(650);
                menu_button.startAnimation(animation_hamburger);
                menu_button.setVisibility(View.VISIBLE);

                Animation animation_location= new TranslateAnimation(0, 0, -100, 0);
                animation_location.setInterpolator(interpolator_notification);
                animation_location.setStartOffset(150);
                animation_location.setDuration(650);

                home_title.startAnimation(animation_location);
                home_title.setVisibility(View.VISIBLE);

            }
        }, 200);
    }

    private void applyBlur() {
        fragment_frame.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        fragment_frame.getViewTreeObserver().removeOnPreDrawListener(
                                this);
                        fragment_frame.buildDrawingCache();

                        Bitmap bmp = fragment_frame.getDrawingCache();
                        blur(bmp, bk_blur);
                        return true;
                    }
                });
    }

    private void blur(Bitmap bkg, ImageView view) {
        long startMs = System.currentTimeMillis();
        float scaleFactor;
        float radius;

        scaleFactor = 10;
        radius = 10;

        Bitmap overlay = Bitmap.createBitmap(
                (int) (view.getMeasuredWidth() / scaleFactor),
                (int) (view.getMeasuredHeight() / scaleFactor),
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft() / scaleFactor, -view.getTop()
                / scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);

        overlay = FastBlur.doBlur(overlay, (int) radius, true);

        view.setImageDrawable(new BitmapDrawable(getResources(), overlay));
    }

}