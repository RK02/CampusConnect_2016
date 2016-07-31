package com.campusconnect;

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
    public static boolean connected=true;

    @Override
    public void onReceive( Context context, Intent intent )
    {

        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
            if (!isConnected) {
                if(MyApp.isActivityVisible()) {
                    Intent networkNotFoundIntent = new Intent(context, NetworkNotFoundActivity.class);
                    networkNotFoundIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(networkNotFoundIntent);
                }
                    connected = false;
            } else {
                connected = true;
            }
        }
    static void broadcast(Context context)
    {
        if(!connected){
            Intent networkNotFoundIntent = new Intent(context, NetworkNotFoundActivity.class);
            networkNotFoundIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(networkNotFoundIntent);
            connected=true;
        }
    }

    }


