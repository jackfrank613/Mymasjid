package com.tech_sim.mymasjidapp;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tech_sim.mymasjidapp.activity.RegularUserRepayActivity;
import com.tech_sim.mymasjidapp.activity.SplashScreen;
import com.tech_sim.mymasjidapp.activity.UserDashboard;
import com.tech_sim.mymasjidapp.model.SalahTimeModel;
import com.tech_sim.mymasjidapp.model.TimeModel;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ScheduleService extends Service {
    private AlarmManager alarmManager;
    private Timer timer;
    private Context mContext;
    MediaPlayer player;
    private boolean flag=true;
    private Broadcast broadcast;
    private IntentFilter intentFilter;
    private String data="";
    private boolean isFlag;
    private String data1="";
    private ArrayList<String> salahtime=new ArrayList<>();
    private ArrayList<String> name=new ArrayList<>();
    private String song="";
    private String temp="";
    private String last_played_song_start_time="";
    private NotificationManager notificationManager;
//    private MyInterface myInterface;
    private static final int NOTIFY_ID=9906;
    public static final String EXTRA_RESULT_CODE="resultCode";
    public static final String EXTRA_RESULT_INTENT="resultIntent";
    private static final String CHANNEL_ID = "masjid_notification_channel";
    private int resultCode;
    private Intent resultDataIntent;
    MediaPlayer mediaPlayer;
    // Custom Binder
//    public MyBinder mLocalbinder = new MyBinder();
//    // Custom interface Callback which is declared in this Service
//    private CallBack mCallBack;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

    }
    @SuppressLint("NewApi")
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        if(Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();
            startForeground(1, notification);
        }

    }
    private class mainTask extends TimerTask
    {
        public void run()
        {
            timeWork();
            Log.e("song",song);

        }
    }
    @Override
    public void onTaskRemoved(Intent rootIntent) {
//        Intent restartServiceIntent=new Intent(getApplicationContext(),this.getClass());
//        restartServiceIntent.setPackage(getPackageName());
//        startService(restartServiceIntent);
        super.onTaskRemoved(rootIntent);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // super.onStartCommand(intent, flags, startId);
        Log.e("Apploging","start");
        Bundle b=intent.getExtras();
        salahtime = b.getStringArrayList("array");
        name=b.getStringArrayList("name");
        song=b.getString("song");
        broadcast=new Broadcast();
        intentFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction("android.intent.action.BOOT_COMPLETED");
        intentFilter.addAction("android.intent.action.LOCKED_BOOT_COMPLETED");
        intentFilter.addAction("android.intent.action.QUICKBOOT_POWERON");
        // Set broadcast receiver priority.
        intentFilter.setPriority(100);
        registerReceiver(broadcast, intentFilter);


        Bundle bundle=null;
        if (intent!=null){
            bundle=intent.getExtras();
        }

        if (bundle!=null){
            if (bundle.containsKey(EXTRA_RESULT_CODE))
                resultCode=bundle.getInt(EXTRA_RESULT_CODE, 1337);
            resultDataIntent =intent.getParcelableExtra(EXTRA_RESULT_INTENT);
        }
        if (resultDataIntent==null) {
            timer=new Timer();
            timer.scheduleAtFixedRate(new mainTask(), 10000, 10000);
        }
//        timer=new Timer();
//        timer.scheduleAtFixedRate(new mainTask(), 0, 500);

        return START_STICKY;
    }
  @Override
  public void onDestroy() {
    super.onDestroy();
    Log.e("Apploging","Apploggingservice destroyed");
    unregisterReceiver(broadcast);
    broadcast = null;
    Intent restartServiceIntent=new Intent(getApplicationContext(),this.getClass());
    restartServiceIntent.setPackage(getPackageName());
    startService(restartServiceIntent);
    Log.i("EXIT", "ondestroy! service restarted!");

    //  Toast.makeText(this, "Service Destroy", Toast.LENGTH_LONG).show();
}
public class Broadcast extends BootBroadcast{
    public Broadcast(){}
}
    public void timeWork(){
                Calendar c = Calendar.getInstance();
                int hrs = c.get(Calendar.HOUR_OF_DAY);//24
                int min = c.get(Calendar.MINUTE);//59
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
                int count=salahtime.size();
                if(dayOfWeek==6)
                {
                    for(int i=0;i<count;i++)
                    {
                        if(name.get(i).equals("Zohr"))
                        {
                            salahtime.remove(i);
                            name.remove(i);
                            break;
                        }
                    }
                }
                else {
                    for(int i=0;i<count;i++)
                    {
                        if(name.get(i).equals("Juma"))
                        {
                            salahtime.remove(i);
                            name.remove(i);
                            break;
                        }
                    }
                }
                int count1=salahtime.size();
          for(int i=0;i<count1;i++)
              {
                        if(salahtime.get(i).equals(current_time))
                            {
                            last_played_song_start_time=salahtime.get(i);
                            if(name.get(i).equals("Tahajud"))
                            {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                                    createNotificationChannel();
                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(ScheduleService.this, CHANNEL_ID);
                                    Notification notification=builder.setContentTitle("My Masjid")
                                            .setContentText("Tahajud salah started" + " " + salahtime.get(i))
                                            .setTicker("New SalahTime")
                                            .setAutoCancel(true)
                                            .setSmallIcon(R.drawable.salahicon)
                                            .build();
                                    NotificationManager notificationManager=(NotificationManager)(ScheduleService.this).getSystemService(Context.NOTIFICATION_SERVICE);
                                    notificationManager.notify(0,notification);
                                }else {
                                    NotificationCompat.Builder builder=new NotificationCompat.Builder(ScheduleService.this);
                                    Notification notification=builder.setContentTitle("My Masjid")
                                            .setContentText("Tahajud salah started" + " " + salahtime.get(i))
                                            .setTicker("New SalahTime")
                                            .setAutoCancel(true)
                                            .setSmallIcon(R.drawable.salahicon)
                                            .build();
                                    NotificationManager notificationManager=(NotificationManager)(ScheduleService.this).getSystemService(Context.NOTIFICATION_SERVICE);
                                    notificationManager.notify(0,notification);

                                }


                            }else if(dayOfWeek == 6){

                                if(!name.get(i).equals("Zohr"))
                                {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                                        createNotificationChannel();
                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(ScheduleService.this, CHANNEL_ID);
                                        Notification notification=builder.setContentTitle("My Masjid")
                                                .setContentText(name.get(i)+" "+"salah started"+" "+salahtime.get(i))
                                                .setTicker("New SalahTime")
                                                .setAutoCancel(true)
                                                .setSmallIcon(R.drawable.salahicon)
                                             //  .setSound(Uri.parse(song))
                                                .build();
                                        NotificationManager notificationManager=(NotificationManager)(ScheduleService.this).getSystemService(Context.NOTIFICATION_SERVICE);
                                        notificationManager.notify(0,notification);
                                      voiceSong();

                                    }else {
                                        NotificationCompat.Builder builder=new NotificationCompat.Builder(ScheduleService.this);
                                        Notification notification=builder.setContentTitle("My Masjid")
                                                .setContentText(name.get(i)+" "+"salah started"+" "+salahtime.get(i))
                                                .setTicker("New SalahTime")
                                                .setAutoCancel(true)
                                                .setSmallIcon(R.drawable.salahicon)
                                               //.setSound(Uri.parse(song))
                                                .build();
                                        NotificationManager notificationManager=(NotificationManager)(ScheduleService.this).getSystemService(Context.NOTIFICATION_SERVICE);
                                        notificationManager.notify(0,notification);
                                     voiceSong();
                                    }

                                }

                            } else {
                                if(!name.get(i).equals("Juma"))
                                {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                                    {
                                        createNotificationChannel();
                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(ScheduleService.this, CHANNEL_ID);
                                        Notification notification=builder.setContentTitle("My Masjid")
                                                .setContentText(name.get(i)+" "+"salah started"+" "+salahtime.get(i))
                                                .setTicker("New SalahTime")
                                                .setAutoCancel(true)
                                                .setSmallIcon(R.drawable.salahicon)
                                               // .setSound(Uri.parse(song))
                                                .build();
                                        NotificationManager notificationManager=(NotificationManager)(ScheduleService.this).getSystemService(Context.NOTIFICATION_SERVICE);
                                        notificationManager.notify(0,notification);
                                       voiceSong();

                                    }else {
                                        NotificationCompat.Builder builder=new NotificationCompat.Builder(ScheduleService.this);
                                        Notification notification=builder.setContentTitle("My Masjid")
                                                .setContentText(name.get(i)+" "+"salah started"+" "+salahtime.get(i))
                                                .setTicker("New SalahTime")
                                                .setAutoCancel(true)
                                                .setSmallIcon(R.drawable.salahicon)
                                              //  .setSound(Uri.parse(song))
                                                .build();
                                        NotificationManager notificationManager=(NotificationManager)(ScheduleService.this).getSystemService(Context.NOTIFICATION_SERVICE);
                                        notificationManager.notify(0,notification);
                                       voiceSong();
                                    }

                                }
                            }
                        }
                    }


    }
    private String playing_song_start_time="";
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
//            AudioAttributes audioAttributes = new AudioAttributes.Builder()
//                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
//                    .build();
//            notificationChannel.setSound(Uri.parse(song),audioAttributes);
            notificationChannel.setLightColor(Color.RED);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
