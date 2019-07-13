package com.tech_sim.mymasjidapp.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.model.TimeModel;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class TimeChecker {

    private TimeEventBus bus;
    private Context context;

    private final int NOTIFICATION_ID = 1;

    private String channel = "masjid_notification_channel";
    private MediaPlayer player;
    private boolean found = false;
    private Disposable disposable;

    public TimeChecker(TimeEventBus bus, Context context) {
        this.bus = bus;
        this.context = context;



        listenForMuteRequest();
    }




    public void checkMasjidTimeAndBroadcastStream() {


//        Calendar c = Calendar.getInstance();
//        final int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
//        String current_time = getCurrentTime(c);


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

        if (hrs > 9 && min > 9) {
            current_time = String.valueOf(hrs) + ":" + String.valueOf(min) + " " + format;
        } else if (hrs < 9 && min < 9) {
            current_time = "0" + String.valueOf(hrs) + ":" + "0" + String.valueOf(min) + " " + format;
        } else if (hrs < 9 && min > 9) {
            current_time = "0" + String.valueOf(hrs) + ":" + String.valueOf(min) + " " + format;
        } else {
            current_time = String.valueOf(hrs) + ":" + "0" + String.valueOf(min) + " " + format;
        }


        int count = PreferenceManager.timeModels.size();
        if (dayOfWeek == 6) {
            removeTime(count, "Zohr");
        } else {
            removeTime(count, "Juma");
        }


        int count1 = PreferenceManager.timeModels.size();
        SimpleDateFormat parser = new SimpleDateFormat("hh:mm a",Locale.ENGLISH);
        try {
            Date date2 = parser.parse(current_time);
            for (int i = 0; i < count1; i++) {
                // get_sal=false;
                TimeModel timeModel = PreferenceManager.timeModels.get(i);
                String start_time = timeModel.getStart_time();
                Date date1 = parser.parse(start_time);
                String nextTime = PreferenceManager.timeModels.get((i + 1) % count1).getStart_time();
                Date date3 = parser.parse(nextTime);

                if(i == count1-1){
                    if(date2.before(date3)){
                        String salah_name = PreferenceManager.timeModels.get((i + 1) % count1).getSalah_name();
                        bus.brodcast(new MasjidTime(nextTime, salah_name));
                        found = true;
                    }

                } else if (date2.before(date3) && date2.after(date1)) {
                    String salah_name = PreferenceManager.timeModels.get((i + 1) % count1).getSalah_name();
                    bus.brodcast(new MasjidTime(nextTime, salah_name));
                    found = true;
                }




                if (start_time.equals(current_time)) {

                    bus.brodcast(new MasjidTime(start_time, timeModel.getSalah_name()));

                    if (timeModel.getSalah_name().equals("Tahajud")) {


                        // android version >oreo
                        String tahajud = "Tahajud salah started" + " " + start_time;
                        showNotification(context, tahajud);
                        voiceSong(start_time);
                        playing_song_start_time = start_time;

                    } else {
                        String jumaText = timeModel.getSalah_name() + " " + "salah started" + " " + start_time;
                        if (dayOfWeek == 6) {

                            if (!timeModel.getSalah_name().equals("Zohr")) {
                                showNotification(context, jumaText);
                                voiceSong(start_time);
                                playing_song_start_time = start_time;
                            }

                        } else {
                            if (!timeModel.getSalah_name().equals("Juma")) {
                                showNotification(context, jumaText);
                                voiceSong(start_time);
                                playing_song_start_time = start_time;
                            }
                        }
                    }
                }


            }

            if (!found) {
                if (PreferenceManager.timeModels.size() != 0) {
                    TimeModel timeModel = PreferenceManager.timeModels.get(0);
                    bus.brodcast(new MasjidTime(timeModel.getStart_time(),timeModel.getSalah_name()));
                }

            }


        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void showNotification(Context context, String sixDays) {

        NotificationCompat.Builder builder;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            createNotificationChannel();
            builder = new NotificationCompat.Builder(context, channel);
        }else {
            builder = new NotificationCompat.Builder(context);
        }



        Notification notification = builder.setContentTitle("My Masjid")
                .setContentText(sixDays)
                .setTicker("New SalahTime")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.salahicon)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(11, notification);
    }

    private void removeTime(int count, String time) {
        for (int i = 0; i < count; i++) {

            if (PreferenceManager.timeModels.get(i).getSalah_name().equals(time)) {
                PreferenceManager.timeModels.remove(i);
                break;
            }
        }
    }

    private String getCurrentTime(Calendar c) {
        int hrs = c.get(Calendar.HOUR_OF_DAY);//24
        int min = c.get(Calendar.MINUTE);//59
        String format;

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

        if (hrs > 9 && min > 9) {
            current_time = String.valueOf(hrs) + ":" + String.valueOf(min) + " " + format;
        } else if (hrs < 9 && min < 9) {
            current_time = "0" + String.valueOf(hrs) + ":" + "0" + String.valueOf(min) + " " + format;
        } else if (hrs < 9 && min > 9) {
            current_time = "0" + String.valueOf(hrs) + ":" + String.valueOf(min) + " " + format;
        } else {
            current_time = String.valueOf(hrs) + ":" + "0" + String.valueOf(min) + " " + format;
        }
        return current_time;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.app_name);
            String description = context.getString(R.string.app_name);
            NotificationChannel notificationChannel = new NotificationChannel(channel, name, NotificationManager.IMPORTANCE_LOW);
            notificationChannel.setDescription(description);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }


    private void listenForMuteRequest() {

        disposable = VolumeEventBus.getInstance().listen()
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {

                        setVolume(aBoolean);

                    }
                });

    }

    private void setVolume(Boolean aBoolean) {

        if(aBoolean){
            if (player != null) {
                player.setVolume(1.0f, 1.0f);
            }

        }else {
            if (player != null) {
                player.setVolume(0.0f, 0.0f);
            }

        }
    }

    private String playing_song_start_time = "";

    private void voiceSong(String last_played_song_start_time) {
        if (playing_song_start_time.equals(last_played_song_start_time)) return;

        if (player != null && player.isPlaying()) {
            return;
        }
        if(player!=null){
            player.reset();
            player.release();
        }

        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            player.setDataSource(PreferenceManager.getSong());
            player.prepare();
            player.start();
            setVolume(VolumeEventBus.getInstance().lastBrodcastValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dispose(){
        disposable.dispose();
    }

}

