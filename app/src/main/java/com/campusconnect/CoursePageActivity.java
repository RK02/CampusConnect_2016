package com.campusconnect;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.campusconnect.POJO.ModelCoursePage;
import com.campusconnect.POJO.ModelStudentList;
import com.campusconnect.POJO.ModelSubscribe;
import com.campusconnect.POJO.MyApi;
import com.campusconnect.POJO.SubscribedCourseList;
import com.campusconnect.adapter.StudentsListAdapter;
import com.campusconnect.adapter.TimetableAdapter;
import com.campusconnect.fragment.Drawer.FragmentAbout;
import com.campusconnect.fragment.Drawer.FragmentAddCourse;
import com.campusconnect.fragment.Drawer.FragmentHome;
import com.campusconnect.fragment.Drawer.FragmentInvite;
import com.campusconnect.fragment.Drawer.FragmentRate;
import com.campusconnect.fragment.Drawer.FragmentTerms;
import com.campusconnect.fragment.Home.FragmentCourses;
import com.campusconnect.slidingtab.SlidingTabLayout_home;
import com.campusconnect.viewpager.ViewPagerAdapter_course;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;
import io.doorbell.android.Doorbell;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by RK on 04/06/2016.
 */
public class CoursePageActivity extends AppCompatActivity implements FloatingActionsMenu.OnFloatingActionsMenuUpdateListener, View.OnClickListener{

    @Bind(R.id.drawer)
    DrawerLayout drawerLayout;
    @Bind(R.id.navigation_view)
    NavigationView navigationView;
    @Bind(R.id.frame)
    FrameLayout fragment_frame;

    @Bind(R.id.ib_back)
    ImageButton back_button;
    @Bind(R.id.ib_sort)
    ImageButton sort_button;
    @Bind(R.id.ib_search)
    ImageButton search_button;
    @Bind(R.id.ib_notification)
    ImageButton notification_button;

    @Bind(R.id.tb_subscribe)
    ToggleButton subscribe_button;

    @Bind(R.id.tv_course_title)
    TextView course_title;
    @Bind(R.id.tv_course_details)
    TextView course_details;
    @Bind(R.id.tv_timetable)
    TextView course_timetable;
    @Bind(R.id.tv_prof_name)
    TextView course_prof;
    @Bind(R.id.tv_view_students)
    TextView course_view_students;

    @Bind(R.id.container_course_info)
    RelativeLayout course_info_container;

    @Bind(R.id.ib_edit_course)
    ImageButton editCourse;

    @Bind(R.id.container_fab)
    FrameLayout fab_menu_container;

    @Bind(R.id.fab_menu)
    FloatingActionsMenu fabMenu;

    @Bind(R.id.ib_flag_course)
    ImageButton flagCourse;

    //Flags
    boolean doubleBackToExitPressedOnce = false;
    boolean at_home=true;
    String frag_title="";
    private Toolbar toolbar;
    private Fragment fragment = null;
    Fragment homefrag;
    View headerView;
    ReportCourseDetailsDialog reportCourseDetailsDialog;
    public static TextView home_title;
    GoogleApiClient mGoogleApiClient;

    ViewPagerAdapter_course course_adapter;
    ViewPager course_pager;
    public static SlidingTabLayout_home course_tabs;
    CharSequence Titles[] = {"Notes", "Assignment", "Exam"};
    int Numboftabs = 3;

    static public String courseId;
    static public String courseTitle;
    int courseColor;
    int defaultTabPosition=0;
    private FirebaseAnalytics firebaseAnalytics;

    StudentsListAdapter mStudentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        ButterKnife.bind(this);
        editCourse.setVisibility(View.GONE);
        flagCourse.setOnClickListener(this);

        //Drawer stuff
        home_title = (TextView) findViewById(R.id.tv_title);
        toolbar = (Toolbar) findViewById (R.id.toolbar);
        setSupportActionBar(toolbar);
//        homefrag = new FragmentHome();
        //Setting up Header View

        course_timetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(1);
                finish();
            }
        });

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
        //Listener to define layouts for FAB expanded and collapsed modes
        fabMenu.setOnFloatingActionsMenuUpdateListener(this);
        //OnClickListener Header View
        ImageView imageView = (ImageView) headerView.findViewById(R.id.profile_image);

        Picasso.with(CoursePageActivity.this)
                .load(getSharedPreferences("CC",MODE_PRIVATE).getString("photourl","fakedesu")).error(R.mipmap.ccnoti)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .placeholder(R.mipmap.ccnoti)
                .into(imageView);
        ((TextView)headerView.findViewById(R.id.tv_username)).setText(getSharedPreferences("CC",MODE_PRIVATE).getString("profileName","PLACEHOLDER"));
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

        defaultTabPosition = getIntent().getIntExtra("TAB",0);
        courseColor = getIntent().getIntExtra("courseColor", Color.rgb(224,224,224));

        course_info_container.setBackgroundColor(courseColor);
        courseId = getIntent().getStringExtra("courseId");


        course_pager = (ViewPager) findViewById(R.id.pager_course);
        course_pager.setOffscreenPageLimit(3);
        course_tabs = (SlidingTabLayout_home) findViewById(R.id.tabs_course);
        course_adapter = new ViewPagerAdapter_course(getSupportFragmentManager(), Titles, Numboftabs, courseColor, this);
        course_pager.setAdapter(course_adapter);
        course_pager.setCurrentItem(defaultTabPosition);
        course_tabs.setDistributeEvenly(true);
        course_tabs.setViewPager(course_pager);


        //Listener to define layouts for FAB expanded and collapsed modes
        fabMenu.setOnFloatingActionsMenuUpdateListener(this);

        //OnClickListeners
        back_button.setOnClickListener(this);
        sort_button.setVisibility(View.GONE);
        sort_button.setOnClickListener(this);
        search_button.setOnClickListener(this);
        notification_button.setOnClickListener(this);
        subscribe_button.setOnClickListener(this);
        course_view_students.setOnClickListener(this);

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case 69:
            {
                if(resultCode==1)
                {
                    if(data!=null)
                    {
                        courseColor = data.getIntExtra("courseColor",0);
                        courseId = data.getStringExtra("courseId");
                    }
                    onResume();
                }
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        course_details.setText("");
        course_info_container.setBackgroundColor(courseColor);
        course_adapter = new ViewPagerAdapter_course(getSupportFragmentManager(), Titles, Numboftabs, courseColor, this);
        Retrofit retrofit = new Retrofit.
                Builder()
                .baseUrl(MyApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyApi myapi = retrofit.create(MyApi.class);


        MyApi.getCourseRequest getCourseRequest = new MyApi.getCourseRequest(getSharedPreferences("CC", Context.MODE_PRIVATE).getString("profileId",""),courseId);

        Call<ModelCoursePage> call = myapi.getCourse(getCourseRequest);
        call.enqueue(new Callback<ModelCoursePage>() {
            @Override
            public void onResponse(Call<ModelCoursePage> call, Response<ModelCoursePage> response) {
                if(getIntent().hasExtra("uploadNotesActivity")) {
                    firebaseAnalytics = FirebaseAnalytics.getInstance(CoursePageActivity.this);
                    firebaseAnalytics.logEvent("course_page_launched", new Bundle());
                }
                final ModelCoursePage modelCoursePage = response.body();
                if(modelCoursePage != null) {
                    courseTitle = modelCoursePage.getCourseName();
                    course_title.setText(courseTitle);
                    course_prof.setText(modelCoursePage.getProfessorName());
                    if(modelCoursePage.getIsAdmin().equals("1"))
                    {
                        editCourse.setVisibility(View.VISIBLE);
                        editCourse.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(CoursePageActivity.this,EditCourseActivity.class);
                                intent.putExtra("editCourse","");
                                intent.putExtra("courseName",courseTitle);
                                intent.putExtra("courseCode",modelCoursePage.getCourseCode());
                                intent.putStringArrayListExtra("dates",new ArrayList<>(modelCoursePage.getDate()));
                                intent.putStringArrayListExtra("startTimes",new ArrayList<>(modelCoursePage.getStartTime()));
                                intent.putStringArrayListExtra("endTimes",new ArrayList<>(modelCoursePage.getEndTime()));
                                intent.putStringArrayListExtra("batchNames",new ArrayList<String>(modelCoursePage.getBatchNames()));
                                intent.putStringArrayListExtra("branchNames",new ArrayList<String>(modelCoursePage.getBranchNames()));
                                intent.putStringArrayListExtra("sectionNames",new ArrayList<String>(modelCoursePage.getSectionNames()));
                                intent.putExtra("prof",modelCoursePage.getProfessorName());
                                intent.putExtra("sem",modelCoursePage.getSemester());
                                intent.putExtra("e",modelCoursePage.getElective());
                                intent.putExtra("courseId",courseId);
                                startActivityForResult(intent,69);
                            }
                        });
                    }

                    if(modelCoursePage.getIsSubscribed().equals("1")) subscribe_button.setChecked(true);
                }
            }

            @Override
            public void onFailure(Call<ModelCoursePage> call, Throwable t) {

            }
        });

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.ib_back:
                finish();
                break;

            case R.id.ib_sort:
                Intent intent_sort = new Intent(getApplicationContext(), SortActivity.class);
                startActivity(intent_sort);
                break;

            case R.id.ib_search:
                Intent intent_search = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent_search);
                break;

            case R.id.ib_notification:
                Intent intent_notification = new Intent(getApplicationContext(), NotificationActivity.class);
                startActivity(intent_notification);
                break;




            case R.id.tb_subscribe:
                if(subscribe_button.isChecked())
                {
                    Retrofit retrofit = new Retrofit.
                            Builder()
                            .baseUrl(MyApi.BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    MyApi myApi = retrofit.create(MyApi.class);
                    ArrayList<String> temp = new ArrayList<String>();
                    temp.add(courseId);
                    MyApi.subscribeCourseRequest body = new MyApi.subscribeCourseRequest(getSharedPreferences("CC", Context.MODE_PRIVATE).getString("profileId",""),temp);
                    Call<ModelSubscribe> call = myApi.subscribeCourse(body);
                    call.enqueue(new Callback<ModelSubscribe>() {
                        @Override
                        public void onResponse(Call<ModelSubscribe> call, Response<ModelSubscribe> response) {
                            FirebaseMessaging.getInstance().subscribeToTopic(courseId);
                            SubscribedCourseList subscribedCourseList = new SubscribedCourseList();
                            subscribedCourseList.setCourseId(courseId);
                            subscribedCourseList.setCourseName(courseTitle);
                            subscribedCourseList.save();
                        }

                        @Override
                        public void onFailure(Call<ModelSubscribe> call, Throwable t) {

                        }
                    });
                }
                else
                {

                    new unsub().execute();
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(courseId);
                    for (String viewId : FragmentCourses.timeTableViews.get(courseId)) {
                        LinearLayout a = ((LinearLayout) TimetableAdapter.itemView.findViewById(Integer.parseInt(viewId)));
                        a.removeAllViews();
                        a.setBackgroundColor(Color.rgb(223, 223, 223));
                    }
                    FragmentCourses.timeTableViews.remove(courseId);
                    SubscribedCourseList.find(SubscribedCourseList.class,"course_id = ?",courseId).get(0).delete();
                    finish();
                }

                break;

            case R.id.tv_view_students:

                ViewStudentsDialog viewStudentsDialog = new ViewStudentsDialog(this);
                viewStudentsDialog.show();
                Window window = viewStudentsDialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                break;
            case R.id.ib_flag_course:
                reportCourseDetailsDialog = new ReportCourseDetailsDialog(this);
                reportCourseDetailsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                Window getReportsDetailsDialogWindow =  reportCourseDetailsDialog.getWindow();
                getReportsDetailsDialogWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                reportCourseDetailsDialog.show();
                break;

            default:
                break;
        }

    }
    class unsub extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection;
            try {
                URL url = new URL(MyApi.BASE_URL+"unsubscribeCourse");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.connect();
                DataOutputStream os = new DataOutputStream(connection.getOutputStream());
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("profileId",getSharedPreferences("CC",MODE_PRIVATE).getString("profileId",""));
                jsonObject.put("courseId",courseId);
                os.write(jsonObject.toString().getBytes());
                os.flush();
                os.close();
                Log.i("sw32",connection.getResponseMessage() +":" +connection.getResponseCode());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    //Layout definition when FAB is expanded
    @Override
    public void onMenuExpanded() {
        final String courseTitle = course_title.getText().toString();
        fabMenu.findViewById(R.id.fab_event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent exam = new Intent(CoursePageActivity.this,AddEventActivity.class);
                exam.putExtra("Mode",1);
                exam.putExtra("courseTitle",courseTitle);
                exam.putExtra("courseId",courseId);
                startActivity(exam);
                fabMenu.collapse();
            }
        });
        fabMenu.findViewById(R.id.fab_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent assignment = new Intent(CoursePageActivity.this,AddEventActivity.class);
                assignment.putExtra("Mode",2);
                assignment.putExtra("courseTitle",courseTitle);
                assignment.putExtra("courseId",courseId);
                startActivity(assignment);
                fabMenu.collapse();

            }
        });
        fabMenu.findViewById(R.id.fab_others).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notes = new Intent(CoursePageActivity.this,UploadPicturesActivity.class);
                notes.putExtra("courseTitle",courseTitle);
                notes.putExtra("courseId",courseId);
                startActivity(notes);
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



    public class ViewStudentsDialog extends Dialog {

        public Activity c;
        public Dialog d;
        public String id;
        Context context;

        @Bind(R.id.cross_button)
        LinearLayout close;
        @Bind(R.id.rv_students_list)
        RecyclerView students_list;


        public ViewStudentsDialog(Activity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
            this.context = context;
            this.id=id;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.view_students_dialog_box);
            ButterKnife.bind(this);

            students_list.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(c);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            students_list.setLayoutManager(llm);
            students_list.setItemAnimator(new DefaultItemAnimator());

            Retrofit retrofit = new Retrofit.
                    Builder()
                    .baseUrl(MyApi.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            MyApi myApi = retrofit.create(MyApi.class);
            final String profileId=getSharedPreferences("CC", Context.MODE_PRIVATE).getString("profileId","");
            MyApi.getStudentListRequest body = new MyApi.getStudentListRequest(profileId,courseId);
            Call<ModelStudentList> call = myApi.getStudentList(body);
            call.enqueue(new Callback<ModelStudentList>() {
                @Override
                public void onResponse(Call<ModelStudentList> call, Response<ModelStudentList> response) {
                    ModelStudentList studentList = response.body();
                    if(studentList!=null) {
                        Boolean isAdmin=false;
                        if(studentList.getIsAdmin().equals("1")) isAdmin = true;
                        studentList.getStudentList();
                        mStudentsAdapter = new StudentsListAdapter(CoursePageActivity.this,studentList.getStudentList(),isAdmin,courseId,profileId);
                        students_list.setAdapter(mStudentsAdapter);
                    }
                }

                @Override
                public void onFailure(Call<ModelStudentList> call, Throwable t) {

                }
            });

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }

            });
        }

    }

    //Function for fragment selection and commits
    public void displayView(int viewId){
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (viewId) {
            case R.id.item_timetable:
                at_home=true;
//                Intent intent_home = new Intent(CoursePageActivity.this,HomeActivity2.class);
//                startActivity(intent_home);
                finish();
                break;
            case R.id.item_add_course:
                fragment = new FragmentAddCourse();
                frag_title = "Add Course";
                at_home=false;
                break;
            case R.id.item_bookmark:
                Intent intent_profile = new Intent(CoursePageActivity.this,ProfilePageActivity.class);
                startActivity(intent_profile);
                fragment = null;
                frag_title = "Course";
                at_home=true;
                break;

            case R.id.item_invite:
                fragment = null;
                frag_title = "Course";
                at_home=true;
                BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
                        .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC);

                LinkProperties linkProperties = new LinkProperties()
                        .setChannel("Invite")
                        .setFeature("Invite")
                        .addControlParameter("$desktop_url", "http://campusconnect.cc")
                        .addControlParameter("$android_url", "bit.ly/campusconnectandroid");
                final Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");

                final String shareText = " Hey, check out this cool app called Campus Connect!\n";
                branchUniversalObject.generateShortUrl(this, linkProperties, new Branch.BranchLinkCreateListener() {
                    @Override
                    public void onLinkCreate(String url, BranchError error) {
                        if (error == null) {
                            sendIntent.putExtra(Intent.EXTRA_TEXT,shareText+url);
                            Log.i("MyApp", "got my Branch link to share: " + url);
                            startActivityForResult(Intent.createChooser(sendIntent, "Invite through..."),666);
                        }
                    }
                });
                break;

            case R.id.item_logout:
                at_home=true;
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                Intent intent = new Intent(CoursePageActivity.this,GoogleSignInActivity.class);
                intent.putExtra("logout","temp");
                FirebaseAuth.getInstance().signOut();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
            case R.id.item_t_and_c:
                frag_title = "Course";
                at_home=true;
                fragment = null;
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://campusconnect.cc/faq#terms"));
                startActivity(browserIntent);
                break;
            case R.id.item_rate:
                fragment = null;
                frag_title = "Course";
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
                        Toast.makeText(CoursePageActivity.this, "Thanks for writing to us!", Toast.LENGTH_LONG).show();
                    }
                });
                fragment = null;
                frag_title = "Course";
                at_home=true;
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
            //android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            //fragmentTransaction.remove(getSupportFragmentManager().findFragmentById(R.id.frame));
            Fragment temp  = getSupportFragmentManager().findFragmentById(R.id.frame);

            if(temp==homefrag) {
                if(!at_home) {
                    fragmentTransaction.add(R.id.frame, fragment);
                    fragmentTransaction.commit();
                }
            }
            else
            { if(temp!=null) {
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
            frag_title="Course";
            home_title.setText(frag_title);
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            fragmentTransaction.remove(getSupportFragmentManager().findFragmentById(R.id.frame));
            fragmentTransaction.commit();
            at_home = true;
        }else if(at_home==true && !drawerLayout.isDrawerOpen(GravityCompat.START)){
//Implementation of "Click back again to exit"
            setResult(2);
           super.onBackPressed();
        }
        else
            drawerLayout.closeDrawers();
    }

    public class ReportCourseDetailsDialog extends  Dialog implements View.OnClickListener{

        public Activity c;
        @Bind(R.id.btn_submit)
        Button btn_submit;
        @Bind(R.id.radio_group)
        RadioGroup radioGroup;

        @Bind(R.id.btn_radio_inapproriate)
        RadioButton btn_radio_inapproriate;

        @Bind(R.id.btn_radio_falseContent)
        RadioButton btn_radio_falseContent;

        @Bind(R.id.btn_radio_other)
        RadioButton btn_radio_other;

        @Bind(R.id.btn_radio_copyrighted)
        RadioButton btn_radio_copyrighted;

        @Bind(R.id.et_feedback)
        EditText et_feedback;

        public ReportCourseDetailsDialog(Activity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
            this.c = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_get_reports_course);
            ButterKnife.bind(this);
            radioGroup.clearCheck();
            btn_submit.setOnClickListener(this);
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    switch (i){
                        case R.id.btn_radio_inapproriate:
                            btn_radio_other.setChecked(false);
                            btn_radio_copyrighted.setChecked(false);
                            et_feedback.setVisibility(View.GONE);
                            break;
                        case R.id.btn_radio_falseContent:
                            btn_radio_inapproriate.setChecked(false);
                            btn_radio_other.setChecked(false);
                            btn_radio_copyrighted.setChecked(false);
                            et_feedback.setVisibility(View.GONE);
                            break;
                        case R.id.btn_radio_copyrighted:
                            btn_radio_inapproriate.setChecked(false);
                            btn_radio_other.setChecked(false);
                            btn_radio_falseContent.setChecked(false);
                            et_feedback.setVisibility(View.GONE);
                            break;
                        case R.id.btn_radio_other:
                            btn_radio_inapproriate.setChecked(false);
                            btn_radio_falseContent.setChecked(false);
                            btn_radio_copyrighted.setChecked(false);
                            et_feedback.setVisibility(View.VISIBLE);
                            break;
                    }
                }
            });

        }

        @Override
        public void onClick(View view) {
            if (view == btn_submit){

                Retrofit retrofit = new Retrofit.
                        Builder()
                        .baseUrl(MyApi.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                MyApi myApi = retrofit.create(MyApi.class);
                MyApi.reportRequest body= new MyApi.reportRequest(getSharedPreferences("CC",MODE_PRIVATE).getString("profileId",""),courseId,"");
                Call<Void> call = myApi.report(body);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(CoursePageActivity.this,"Thank you for the feedback. We will get back to you shortly",Toast.LENGTH_SHORT).show();
                        reportCourseDetailsDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.d("dialog get reports","failed");
                    }
                });
            }
        }
        }
    }




