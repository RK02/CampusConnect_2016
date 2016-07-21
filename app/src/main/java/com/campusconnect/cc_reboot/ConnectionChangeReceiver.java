package com.campusconnect.cc_reboot;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;


/**
 * Created by sarthak on 7/21/16.
 */
public class ConnectionChangeReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive( Context context, Intent intent )
    {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if (!isConnected)
        {

        }
        else
        {

        }

    }

}
