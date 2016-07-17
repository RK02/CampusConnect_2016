package com.campusconnect.cc_reboot.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.campusconnect.cc_reboot.POJO.ModelNotification;
import com.campusconnect.cc_reboot.POJO.SubscribedCourseList;
import com.campusconnect.cc_reboot.R;
import com.campusconnect.cc_reboot.auxiliary.ObservableScrollView;
import com.campusconnect.cc_reboot.auxiliary.ScrollViewListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by RK on 05/06/2016.
 */
public class NotificationAdapter extends
        RecyclerView.Adapter<NotificationAdapter.NotificationHolder> {

    Context context;
    ConnectivityManager cm;
    NetworkInfo activeNetwork;
    boolean isConnected;
    List<ModelNotification> notifications;

    public NotificationAdapter(Context context,List<ModelNotification> notifications) {
        this.context = context;
        this.notifications = notifications;
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public void clear() {
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(NotificationHolder notificationHolder, final int i) {
        ModelNotification notification = notifications.get(i);
        String type = notification.getType();
        switch(type){
            case "notes":break;
            case "assignment":break;
            case "exam":break;
            case "admin":break;
        }
        String message = notification.getText();
        String timestamp = notification.getTimeStamp();
        notificationHolder.notificationMessage.setText(message);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        int days = 0,hours=0,minutes=0,seconds=0;
        try {
            Calendar a = Calendar.getInstance();
            Calendar b = Calendar.getInstance();
            b.setTime(df.parse(timestamp));
            long difference = a.getTimeInMillis() - b.getTimeInMillis();
            days = (int) (difference/ (1000*60*60*24));
            hours = (int) (difference/ (1000*60*60));
            minutes = (int) (difference/ (1000*60));
            seconds = (int) (difference/1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        days = Math.abs(days);
        hours = Math.abs(hours);
        minutes = Math.abs(minutes);
        seconds = Math.abs(seconds);
        if(days==0) {if(hours==0) {if(minutes==0) {if(seconds==0) {notificationHolder.timestamp.setText("Just now");}
        else {if(seconds==1) notificationHolder.timestamp.setText(seconds + " second ago");
        else notificationHolder.timestamp.setText(seconds + " seconds ago");}}
        else {if(minutes==1) notificationHolder.timestamp.setText(minutes + " minute ago");
            notificationHolder.timestamp.setText(minutes + " minutes ago");}}
        else {if(hours==1)notificationHolder.timestamp.setText(hours + " hour ago");
        else notificationHolder.timestamp.setText(hours + " hours ago");}}
        else {if(days==1)notificationHolder.timestamp.setText(days + " day ago");
        else notificationHolder.timestamp.setText(days + " days ago");}

    }

    @Override
    public NotificationHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.card_layout_notification, viewGroup, false);
        return new NotificationHolder(itemView);
    }

    public class NotificationHolder extends RecyclerView.ViewHolder{


        @Bind(R.id.tv_notification)
        TextView notificationMessage;
        @Bind(R.id.tv_notification_timestamp)
        TextView timestamp;
        public NotificationHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            context = v.getContext();

        }

    }
}