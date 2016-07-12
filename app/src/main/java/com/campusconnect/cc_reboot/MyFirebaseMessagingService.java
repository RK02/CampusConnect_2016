/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.campusconnect.cc_reboot;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.campusconnect.cc_reboot.POJO.CustomNotification;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    NotificationCompat.Builder notificationBuilder;
    NotificationCompat.InboxStyle inboxStyle;
    Uri defaultSoundUri;
    @Override
    public void onCreate() {
        defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        inboxStyle = new NotificationCompat.InboxStyle();
        super.onCreate();
        notificationBuilder = new NotificationCompat.Builder(this);


    }

    private static final String TAG = "MyFirebaseMsgService";
    final static String CC = "CCNOTIFS";
    int notifyId =1;
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        Log.d("sw32message", "From: " + remoteMessage.getFrom());
        Map<String,String> data = remoteMessage.getData();
        String messageId = remoteMessage.getMessageId();
        String message = data.get("message");
        String title = data.get("title");
        String type = data.get("type");
        String id = data.get("id");
        CustomNotification customNotification = new CustomNotification();
        customNotification.setMessageId(messageId);
        customNotification.setMessageBody(message);
        customNotification.setTitle(title);
        customNotification.setType(type);
        customNotification.save();
        Log.d("sw32message", "Notification Message Body: " + message);
        sendNotification(title,message,type,id);
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String title, String messageBody,String type, String id) {
        Intent intent = new Intent(this,HomeActivity2.class);
        Log.i("sw32notification",id + ":"+type+ ":" + messageBody);
//        switch(type){
//            case "notes":intent = new Intent(this,NotePageActivity.class); intent.putExtra("noteBookId",id); break;
//            case "assignment":intent = new Intent(this,AssignmentPageActivity.class); intent.putExtra("assignmentId",id);break;
//            case "exam":intent = new Intent(this,ExamPageActivity.class); intent.putExtra("testId",id);break;
//        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setAction(Long.toString(System.currentTimeMillis()));
        intent.putExtra("pendingIntentAction","Clear Notifications");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        inboxStyle = new NotificationCompat.InboxStyle();
        int i=0;
        for(CustomNotification customNotification : new ArrayList<>(CustomNotification.listAll(CustomNotification.class)))
        {
            Log.i("sw32", "here notification");
            i++;
            inboxStyle.addLine(customNotification.getTitle() + " - " + customNotification.getMessageBody());
        }
        inboxStyle.setBigContentTitle(i + " New notifications");
        inboxStyle.setSummaryText("Campus Connect");
        notificationBuilder
                .setSmallIcon(R.mipmap.ccnoti)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.ccnoti))
                .setContentTitle("Campus Connect")
                .setStyle(inboxStyle)
                .setGroup(CC)
                .setGroupSummary(true)
                .setSound(defaultSoundUri)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify( notifyId /*(int) System.currentTimeMillis()*/ /* ID of notification */, notificationBuilder.build());

    }
}