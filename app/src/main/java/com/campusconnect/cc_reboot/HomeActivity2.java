package com.campusconnect.cc_reboot;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.campusconnect.cc_reboot.fragment.Drawer.FragmentAbout;
import com.campusconnect.cc_reboot.fragment.Drawer.FragmentAddCourse;
import com.campusconnect.cc_reboot.fragment.Drawer.FragmentGifts;
import com.campusconnect.cc_reboot.fragment.Drawer.FragmentHome;
import com.campusconnect.cc_reboot.fragment.Drawer.FragmentInvite;
import com.campusconnect.cc_reboot.fragment.Drawer.FragmentPointsInfo;
import com.campusconnect.cc_reboot.fragment.Drawer.FragmentRate;
import com.campusconnect.cc_reboot.fragment.Drawer.FragmentSettings;

/**
 * Created by RK on 04/06/2016.
 */
public class HomeActivity2 extends AppCompatActivity {

    int pos_home=1;

    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private FrameLayout fragment_frame;
    private RelativeLayout header;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_true);

        fragment_frame = (FrameLayout) findViewById(R.id.frame);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        View headerView = getLayoutInflater().inflate(R.layout.header, navigationView, false);
        navigationView.addHeaderView(headerView);

        final android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, new FragmentHome());
        fragmentTransaction.commit();

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                switch (menuItem.getItemId()) {


                    case R.id.item_add:
                        FragmentAddCourse fragment_add_course = new FragmentAddCourse();
                        android.support.v4.app.FragmentTransaction fragmentTransaction_item_add = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction_item_add.replace(R.id.frame, fragment_add_course);
                        fragmentTransaction_item_add.commit();
                        pos_home=0;
                        return true;

                    case R.id.item_getting_points:
                        FragmentPointsInfo fragment_points_info = new FragmentPointsInfo();
                        android.support.v4.app.FragmentTransaction fragmentTransaction_points_info = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction_points_info.replace(R.id.frame, fragment_points_info);
                        fragmentTransaction_points_info.commit();
                        pos_home=0;
                        return true;

                    case R.id.item_gifts:
                        FragmentGifts fragment_gifts = new FragmentGifts();
                        android.support.v4.app.FragmentTransaction fragmentTransaction_gifts = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction_gifts.replace(R.id.frame, fragment_gifts);
                        fragmentTransaction_gifts.commit();
                        pos_home=0;
                        return true;

                    case R.id.item_invite:
                        FragmentInvite fragment_invite = new FragmentInvite();
                        android.support.v4.app.FragmentTransaction fragmentTransaction_invite = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction_invite.replace(R.id.frame, fragment_invite);
                        fragmentTransaction_invite.commit();
                        pos_home=0;
                        return true;

                    case R.id.item_about:
                        FragmentAbout fragment_about = new FragmentAbout();
                        android.support.v4.app.FragmentTransaction fragmentTransaction_about = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction_about.replace(R.id.frame, fragment_about);
                        fragmentTransaction_about.commit();
                        pos_home=0;
                        return true;

                    case R.id.item_rate:
                        FragmentRate fragment_rate = new FragmentRate();
                        android.support.v4.app.FragmentTransaction fragmentTransaction_rate = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction_rate.replace(R.id.frame, fragment_rate);
                        fragmentTransaction_rate.commit();
                        pos_home=0;
                        return true;

                    case R.id.item_settings:
                        FragmentSettings fragment_settings = new FragmentSettings();
                        android.support.v4.app.FragmentTransaction fragmentTransaction_settings = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction_settings.replace(R.id.frame, fragment_settings);
                        fragmentTransaction_settings.commit();
                        pos_home=0;
                        return true;
                    case R.id.item_logout:
                        Toast.makeText(getApplicationContext(), "LOGOUT", Toast.LENGTH_SHORT).show();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame, new FragmentHome());
                        fragmentTransaction.commit();
                        pos_home=1;
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(), "Something's Wrong", Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

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

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_temp = new Intent(v.getContext(), ProfilePageActivity.class);
                startActivity(intent_temp);
            }
        });



    }


    @Override
    public void onBackPressed() {
        if(pos_home==0 && !drawerLayout.isDrawerOpen(GravityCompat.START)) {
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, new FragmentHome());
            fragmentTransaction.commit();
            pos_home = 1;
        }
        else
            drawerLayout.closeDrawers();
    }
}


