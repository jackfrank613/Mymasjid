package com.tech_sim.mymasjidapp.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.tech_sim.mymasjidapp.activity.SplashScreen;


public abstract class ForeGroundService extends Service {


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String channelId;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelId = createNotificationChannel();
        } else {
            channelId = "";
        }

        Intent notificationIntent = new Intent(this, SplashScreen.class);
        notificationIntent.setAction("1");
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);


        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, channelId)
                .setContentTitle("My MasjidApp")
                .setTicker("Online")
                .setContentText("Online")


                .setContentIntent(pendingIntent)
                .setOngoing(true);

        startForeground(1221,
                notification.build());


        return START_STICKY;
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel() {
        String channelId = "my_service92211";
        String channelName = "My Background Service backgrund";
        NotificationChannel chan = new NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_HIGH);

        chan.setDescription(channelName);
        NotificationManager service = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        service.createNotificationChannel(chan);
        return channelId;
    }

}
