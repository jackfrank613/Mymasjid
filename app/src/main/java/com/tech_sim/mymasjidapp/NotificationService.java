package com.tech_sim.mymasjidapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.tech_sim.mymasjidapp.activity.UserDashboard;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {

    Timer timer;
    TimerTask timerTask;
    String TAG = "Timers";
    int Your_X_SECS = 5;
    private NotificationManager notificationManager;
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "masjid_notification_channel";
    private String last_played_song_start_time="";
    Context context;


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);

        startTimer();

        return START_STICKY;
    }


    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");


    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        stoptimertask();
        super.onDestroy();


    }

    //we are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler();


    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 500, 1000); //
        //timer.schedule(timerTask, 5000,1000); //
    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {

                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run() {

                        Calendar c = Calendar.getInstance();
                        int hrs = c.get(Calendar.HOUR_OF_DAY);//24
                        int min = c.get(Calendar.MINUTE);//59
                        Log.d("time",String.valueOf(hrs));
                        String format;
                        final int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
                        String current_time;
                        if (hrs == 0) {
                            hrs += 12;
                            format = "AM";
                        } else if (hrs == 12) {
                            format = "PM";
                        } else if (hrs > 12) {
                            hrs -= 12;
                            format = "PM";
                        } else {
                            format = "AM";
                        }

                        if(hrs>9&&min>9)
                        {
                            current_time=String.valueOf(hrs) + ":" + String.valueOf(min)+" "+format;
                        }
                        else if(hrs<9&&min<9) {
                            current_time="0"+String.valueOf(hrs) + ":" + "0"+String.valueOf(min)+" "+format;
                        }
                        else if(hrs<9&&min>9){
                            current_time="0"+String.valueOf(hrs) + ":" + String.valueOf(min)+" "+format;
                        }
                        else {
                            current_time=String.valueOf(hrs) + ":" +"0"+ String.valueOf(min)+" "+format;
                        }
                        int count= PreferenceManager.timeModels.size();
                        if(dayOfWeek==6)
                        {
                            for(int i=0;i<count;i++)
                            {
                                if(PreferenceManager.timeModels.get(i).getSalah_name().equals("Zohr"))
                                {
                                    PreferenceManager.timeModels.remove(i);
                                    break;
                                }
                            }
                        }
                        else {
                            for(int i=0;i<count;i++)
                            {
                                if(PreferenceManager.timeModels.get(i).getSalah_name().equals("Juma"))
                                {
                                    PreferenceManager.timeModels.remove(i);
                                    break;
                                }
                            }
                        }
                        int count1=PreferenceManager.timeModels.size();
                        for(int i=0;i<count1;i++) {

                            if(PreferenceManager.timeModels.get(i).getStart_time().equals(current_time))

                            {
                                last_played_song_start_time=PreferenceManager.timeModels.get(i).getStart_time();
                                if(PreferenceManager.timeModels.get(i).getSalah_name().equals("Tahajud"))
                                {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                                        createNotificationChannel();
                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
                                        Notification notification=builder.setContentTitle("Mosque")
                                                .setContentText("Tahajud salah started" + " " + PreferenceManager.timeModels.get(i).getStart_time())
                                                .setTicker("New SalahTime")
                                                .setAutoCancel(true)
                                                .setSmallIcon(R.drawable.salahicon)
                                                .build();
                                        NotificationManager notificationManager=(NotificationManager)(getApplicationContext()).getSystemService(Context.NOTIFICATION_SERVICE);
                                        notificationManager.notify(0,notification);
                                        //   voiceSong();

                                    }else {
                                        NotificationCompat.Builder builder=new NotificationCompat.Builder(getApplicationContext());
                                        Notification notification=builder.setContentTitle("Mosque")
                                                .setContentText("Tahajud salah started" + " " + PreferenceManager.timeModels.get(i).getStart_time())
                                                .setTicker("New SalahTime")
                                                .setAutoCancel(true)
                                                .setSmallIcon(R.drawable.salahicon)
                                                .build();
                                        NotificationManager notificationManager=(NotificationManager)(getApplicationContext()).getSystemService(Context.NOTIFICATION_SERVICE);
                                        notificationManager.notify(0,notification);
                                        //   voiceSong();
                                    }


                                }else if(dayOfWeek == 6){

                                    if(!PreferenceManager.timeModels.get(i).getSalah_name().equals("Zohr"))
                                    {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                                            createNotificationChannel();
                                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
                                            Notification notification=builder.setContentTitle("Mosque")
                                                    .setContentText(PreferenceManager.timeModels.get(i).getSalah_name()+" "+"salah started"+" "+PreferenceManager.timeModels.get(i).getStart_time())
                                                    .setTicker("New SalahTime")
                                                    .setAutoCancel(true)
                                                    .setSmallIcon(R.drawable.salahicon)
                                                    .build();
                                            NotificationManager notificationManager=(NotificationManager)(getApplicationContext()).getSystemService(Context.NOTIFICATION_SERVICE);
                                            notificationManager.notify(0,notification);
                                            voiceSong();

                                        }else {
                                            NotificationCompat.Builder builder=new NotificationCompat.Builder(getApplicationContext());
                                            Notification notification=builder.setContentTitle("Mosque")
                                                    .setContentText(PreferenceManager.timeModels.get(i).getSalah_name()+" "+"salah started"+" "+PreferenceManager.timeModels.get(i).getStart_time())
                                                    .setTicker("New SalahTime")
                                                    .setAutoCancel(true)
                                                    .setSmallIcon(R.drawable.salahicon)
                                                    .build();
                                            NotificationManager notificationManager=(NotificationManager)(getApplicationContext()).getSystemService(Context.NOTIFICATION_SERVICE);
                                            notificationManager.notify(0,notification);
                                            voiceSong();
                                        }

                                    }

                                } else {
                                    if(!PreferenceManager.timeModels.get(i).getSalah_name().equals("Juma"))
                                    {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                                        {
                                            createNotificationChannel();
                                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
                                            Notification notification=builder.setContentTitle("Mosque")
                                                    .setContentText(PreferenceManager.timeModels.get(i).getSalah_name()+" "+"salah started"+" "+PreferenceManager.timeModels.get(i).getStart_time())
                                                    .setTicker("New SalahTime")
                                                    .setAutoCancel(true)
                                                    .setSmallIcon(R.drawable.salahicon)
                                                    .build();
                                            NotificationManager notificationManager=(NotificationManager)(getApplicationContext()).getSystemService(Context.NOTIFICATION_SERVICE);
                                            notificationManager.notify(0,notification);
                                            voiceSong();

                                        }else {
                                            NotificationCompat.Builder builder=new NotificationCompat.Builder(getApplicationContext());
                                            Notification notification=builder.setContentTitle("Mosque")
                                                    .setContentText(PreferenceManager.timeModels.get(i).getSalah_name()+" "+"salah started"+" "+PreferenceManager.timeModels.get(i).getStart_time())
                                                    .setTicker("New SalahTime")
                                                    .setAutoCancel(true)
                                                    .setSmallIcon(R.drawable.salahicon)
                                                    .build();
                                            NotificationManager notificationManager=(NotificationManager)(getApplicationContext()).getSystemService(Context.NOTIFICATION_SERVICE);
                                            notificationManager.notify(0,notification);
                                            voiceSong();
                                        }

                                    }
                                }
                            }
                        }

                    }
                });
            }
        };
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            String description = getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_LOW);
            notificationChannel.setDescription(description);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
    private String playing_song_start_time="";
    MediaPlayer player;
    public void voiceSong()
    {
        if (playing_song_start_time.equals(last_played_song_start_time)) return;
        playing_song_start_time=last_played_song_start_time;
        if (player != null && player.isPlaying()) return;
        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            player.setDataSource(PreferenceManager.getSong());
            player.prepare();
            player.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}