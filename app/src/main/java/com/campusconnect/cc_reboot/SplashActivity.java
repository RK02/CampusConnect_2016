package com.campusconnect.cc_reboot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;

/**
 * Created by RK on 04/06/2016.
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

    }

    @Override
    public void onStart() {
        super.onStart();

        Branch branch = Branch.getInstance();
        branch.initSession(new Branch.BranchReferralInitListener() {
            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {
                if (error == null) {
                    // params are the deep linked params associated with the link that the user clicked before showing up
                    try {
                        Log.i("BranchConfigTest", "deep link data: " + referringParams.get("+clicked_branch_link").toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        String decider = referringParams.get("+clicked_branch_link").toString();
                        Log.i("sw32decider",decider);
                        if(!decider.equals("true"))
                        {
                            Log.i("sw32branchlinking","inheresplash");
                            Thread thread = new Thread() {
                                @Override
                                public void run() {
                                    super.run();
                                    try {
                                        sleep(2000);
                                        Intent next = new Intent(SplashActivity.this, GoogleSignInActivity.class);
                                        startActivity(next);
                                        finish();

                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            thread.start();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, this.getIntent().getData(), this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        this.setIntent(intent);
    }
}

