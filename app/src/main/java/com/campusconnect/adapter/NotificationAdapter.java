package com.campusconnect.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.campusconnect.AssignmentPageActivity;
import com.campusconnect.CoursePageActivity;
import com.campusconnect.ExamPageActivity;
import com.campusconnect.NotePageActivity;
import com.campusconnect.POJO.ModelNotification;
import com.campusconnect.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
        final String type = notification.getType();
        final String key = notification.getId();
        Log.i("sw32Notifications",type + " : " + key);
        Intent intent = new Intent();
        switch(type){
            case "notes": intent = new Intent(context, NotePageActivity.class);intent.putExtra("noteBookId",key); break;
            case "assignment":intent = new Intent(context, AssignmentPageActivity.class);intent.putExtra("assignmentId",key);break;
            case "exam":intent = new Intent(context, ExamPageActivity.class);intent.putExtra("testId",key);break;
            case "admin":intent = new Intent(context, CoursePageActivity.class);intent.putExtra("courseId",key);break;
            case "rated":intent = new Intent(context, NotePageActivity.class);intent.putExtra("noteBookId",key);break;
            default: intent = null;
        }


        final Intent finalIntent = intent;
        notificationHolder.notificationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(key==null || finalIntent==null)
                {
                    Toast.makeText(context,"Oops! Something went wrong!",Toast.LENGTH_SHORT).show();
                }
                else {
                    context.startActivity(finalIntent);
                }
            }
        });
        String message = notification.getText();
        String timestamp = notification.getTimeStamp();
        String[] aa = timestamp.split("T");
        timestamp = aa[0] + " " + aa[1];
        Log.i("sw32timestamp",timestamp);
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

        @Bind(R.id.notification_card)
        View notificationCard;
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