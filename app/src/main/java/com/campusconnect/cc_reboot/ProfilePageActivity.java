package com.campusconnect.cc_reboot;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.campusconnect.cc_reboot.POJO.AvailableCourseList;
import com.campusconnect.cc_reboot.POJO.Example;
import com.campusconnect.cc_reboot.POJO.ModelCoursePage;
import com.campusconnect.cc_reboot.POJO.MyApi;
import com.campusconnect.cc_reboot.POJO.SubscribedCourseList;
import com.campusconnect.cc_reboot.fragment.Home.FragmentCourses;
import com.campusconnect.cc_reboot.slidingtab.SlidingTabLayout_home;
import com.campusconnect.cc_reboot.viewpager.ViewPagerAdapter_course;
import com.campusconnect.cc_reboot.viewpager.ViewPagerAdapter_profile;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

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
public class ProfilePageActivity extends AppCompatActivity implements FloatingActionsMenu.OnFloatingActionsMenuUpdateListener, View.OnClickListener{

    @Bind(R.id.pager_course)
    ViewPager profile_pager;

    @Bind(R.id.profile_image)
    ImageView profile_image;
     @Bind(R.id.tv_username)
    TextView profile_name;

    @Bind(R.id.tv_points)
    TextView profile_points;

    @Bind(R.id.tabs_course)
    SlidingTabLayout_home profile_tabs;

    @Bind(R.id.ib_gifts)
    ImageButton gifts_button;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

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
        Call<Example> call = myApi.getFeed(getSharedPreferences("CC", Context.MODE_PRIVATE).getString("profileId",""));
        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {

                Log.i("sw32", "" + response.code());
                Example example = response.body();
                if (example != null) {
                    Picasso.with(ProfilePageActivity.this)
                            .load(example.getPhotoUrl())
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .into(profile_image);
                    profile_name.setText(example.getProfileName());
                    profile_points.setText(example.getPoints());
                }
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {

            }
        });



        //OnClickListeners
        gifts_button.setOnClickListener(this);
        edit_profile_button.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.ib_gifts:
                Intent intent_gifts = new Intent(getApplicationContext(), GiftsActivity.class);
                startActivity(intent_gifts);
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
        Picasso.with(ProfilePageActivity.this)
                .load(getSharedPreferences("CC",MODE_PRIVATE).getString("photourl","fakedesu"))
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .error(R.mipmap.ccnoti)
                .placeholder(R.mipmap.ccnoti)
                .into(profile_image);
    }
}

