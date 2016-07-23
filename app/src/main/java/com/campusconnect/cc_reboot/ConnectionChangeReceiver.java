package com.campusconnect.cc_reboot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


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
            Intent networkNotFoundIntent = new Intent(context,NetworkNotFoundActivity.class);
            networkNotFoundIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(networkNotFoundIntent);
        }
        else
        {

        }

    }

}
