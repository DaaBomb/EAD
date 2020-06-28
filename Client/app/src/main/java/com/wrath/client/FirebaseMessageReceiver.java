package com.wrath.client;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.wrath.client.dto.NotificationDetails;
import com.wrath.client.dto.User;

import java.util.Random;


public class FirebaseMessageReceiver extends FirebaseMessagingService {

    Gson gson = new Gson();
    String user;
    User userObj;
    SharedPreferences sharedPreferences;

    @Override
    public void onNewToken(@NonNull String s) {
        sharedPreferences = getSharedPreferences("swarm", MODE_PRIVATE);
        sharedPreferences.edit().putString("token", s).apply();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //handle when receive notification via data event
        sharedPreferences = getSharedPreferences("swarm", MODE_PRIVATE);
        user = sharedPreferences.getString("user", "{}");
        userObj = gson.fromJson(user, User.class);
        String messageString = remoteMessage.getData().get("message");
        NotificationDetails notificationDetails = gson.fromJson(messageString, NotificationDetails.class);
        if (remoteMessage.getData().size() > 0) {
            if (    remoteMessage.getData().get("title").equals("Security approval") &&
                    notificationDetails.getSociety_id().equals(userObj.getAddress().getSociety_id()) &&
                    notificationDetails.getBlock_visiting().equals(userObj.getAddress().getBlockname()) &&
                    notificationDetails.getFlatnum_visiting().equals(userObj.getAddress().getFlatnum()))
                    showNotification(remoteMessage.getData().get("title"), notificationDetails.getVisitor_name() + " is requesting your approval" +" ("+ notificationDetails.getBlock_visiting() +"-"+notificationDetails.getFlatnum_visiting()+")",notificationDetails);
            else if(remoteMessage.getData().get("title").equals("Resident approval") &&
                    notificationDetails.getSociety_id().equals(userObj.getAddress().getSociety_id()) &&
                    userObj.getProfession()!=null &&
                    userObj.getProfession().equals("security")
            )
                showNotification(remoteMessage.getData().get("title"),notificationDetails.getVisitor_name() + (notificationDetails.getConfirmed()?" is allowed to go inside":" is not allowed to go inside"),notificationDetails);
        }

        //handle when receive notification
        if (remoteMessage.getNotification() != null) {
            showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(),notificationDetails);
        }
    }

    private RemoteViews getCustomDesign(String title, String message) {
        RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification);
        remoteViews.setTextViewText(R.id.title, title);
        remoteViews.setTextViewText(R.id.message, message);
        remoteViews.setImageViewResource(R.id.icon, R.drawable.bee);
        return remoteViews;
    }

    public void showNotification(String title, String message, NotificationDetails notificationDetails) {
        Intent intent = new Intent(this, MainActivity.class);
        String channel_id = "swarm_channel";
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setAction("INTENT_ACTION_SEND_MESSAGE");

        Bundle extras = new Bundle();
        extras.putString("title", title);
        extras.putString("message", message);
        extras.putString("notificationDetails", gson.toJson(notificationDetails));
        intent.putExtras(extras);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, new Random().nextInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channel_id)
                .setSmallIcon(R.drawable.bee)
                .setSound(uri)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder = builder.setContent(getCustomDesign(title, message));
        } else {
            builder = builder.setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.bee);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channel_id, "swarm", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setSound(uri, null);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(0, builder.build());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}