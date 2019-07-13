package com.tech_sim.mymasjidapp.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.activity.Announcement;
import com.tech_sim.mymasjidapp.activity.AnnouncementDetail;
import com.tech_sim.mymasjidapp.activity.ManageAnsQuestionPanel;
import com.tech_sim.mymasjidapp.activity.MasjidAnswer;
import com.tech_sim.mymasjidapp.activity.MasjidAnswerActivity;
import com.tech_sim.mymasjidapp.utils.Constants;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;


/**
 * Created by NgocTri on 8/9/2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "delivery_notification_channel";
    private NotificationManager notificationManager;
    private Context context;
    private String name;
    private String detail;
    private String date;
    private String audio;
    private String question;
    private String answer;
    private int i=0,j=0,k=0;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        context=getApplicationContext();
        // Check if message contains a data payload.
      if(remoteMessage.getData()!=null)
      {
          String sort=remoteMessage.getData().get("sort");
          if(sort.equals("announcement"))
          {
              PreferenceManager.count_announce++;
             name=remoteMessage.getData().get("name");
             detail=remoteMessage.getData().get("detail");
             date=remoteMessage.getData().get("date");
             audio=remoteMessage.getData().get("audio");
             shownotification(name,detail,"masji");
             broadcastIntent("announce", PreferenceManager.count_announce);
             PreferenceManager.setCount1( PreferenceManager.count_announce);
          }
          if(sort.equals("question"))
          {
              PreferenceManager.count_question++;
              question=remoteMessage.getData().get("question");
              date=remoteMessage.getData().get("date");
              audio=remoteMessage.getData().get("audio");
              shownotification("Dear Masjid",question,"user");
              broadcastIntent("question",  PreferenceManager.count_question);
              PreferenceManager.setCount3(PreferenceManager.count_question);


          }
          if(sort.equals("answer"))
          {
              PreferenceManager.count_answer++;
              answer=remoteMessage.getData().get("answer");
              date=remoteMessage.getData().get("date");
              audio=remoteMessage.getData().get("audio");
              shownotification("Dear User",question,"answer");
              broadcastIntent("answer",  PreferenceManager.count_answer);
              PreferenceManager.setCount2( PreferenceManager.count_answer);


          }
      }
    }
    public void broadcastIntent(String type,int count) {
        Intent intent = new Intent();
        intent.setAction("com.teachnologybeach.masjjidapp.message");
        intent.putExtra("type",type);
        intent.putExtra("count",count);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
    private void shownotification(String title,String body,String check) {
        //Intent to be showed.
        Constants.check_noti=true;
        Intent p_intent=new Intent();
        Intent intent = new Intent(this, AnnouncementDetail.class);
        intent.putExtra("name",name);
        intent.putExtra("detail",detail);
        intent.putExtra("date",date);
        intent.putExtra("audio",audio);
        Intent intent1=new Intent(this, ManageAnsQuestionPanel.class);
        Intent intent2=new Intent(this, MasjidAnswerActivity.class);
        if(check.equals("masji"))
        {
          p_intent=intent;
        }
        else if(check.equals("user"))
        {
            p_intent=intent1;
        }
        else if(check.equals("answer")) {
            p_intent=intent2;
        }
        p_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0/*Request code*/, p_intent, PendingIntent.FLAG_ONE_SHOT);
        //Set sound of notification
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel();
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setVibrate(new long[]{0, 100, 100, 100, 100, 100})
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setSmallIcon(R.drawable.salahicon)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setSound(notificationSound)
                    .setContentIntent(pendingIntent);
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }else {
            NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(this)
                    .setVibrate(new long[]{0, 100, 100, 100, 100, 100})
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setSmallIcon(R.drawable.salahicon)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setSound(notificationSound)
                    .setContentIntent(pendingIntent);
            notificationManager.notify(0 /*ID of notification*/, notifiBuilder.build());
        }

    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            String description = getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationChannel.setDescription(description);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}