package com.campusconnect.cc_reboot;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.campusconnect.cc_reboot.POJO.SubscribedCourseList;
import com.orm.SugarApp;
import com.orm.SugarContext;

import io.branch.referral.Branch;

/**
 * Created by sarthak on 7/5/16.
 */
public class MyApp extends SugarApp {
        protected void attachBaseContext(Context base) {
            super.attachBaseContext(base);
            MultiDex.install(this);

        }

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
        Branch.getAutoInstance(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }
}

