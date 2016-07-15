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

import com.campusconnect.cc_reboot.POJO.SubscribedCourseList;
import com.campusconnect.cc_reboot.R;
import com.campusconnect.cc_reboot.auxiliary.ObservableScrollView;
import com.campusconnect.cc_reboot.auxiliary.ScrollViewListener;

import java.util.ArrayList;
import java.util.HashMap;

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

    public NotificationAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public void clear() {
//        mCourses.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(NotificationHolder notificationHolder, final int i) {

    }

    @Override
    public NotificationHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.card_layout_notification, viewGroup, false);
        return new NotificationHolder(itemView);
    }

    public class NotificationHolder extends RecyclerView.ViewHolder{

        public NotificationHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            context = v.getContext();

        }

    }
}