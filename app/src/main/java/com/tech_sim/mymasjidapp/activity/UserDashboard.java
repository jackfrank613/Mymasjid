package com.tech_sim.mymasjidapp.activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.service.MasjidTime;
import com.tech_sim.mymasjidapp.service.MasjidTimeService;
import com.tech_sim.mymasjidapp.service.TimeEventBus;
import com.tech_sim.mymasjidapp.service.VolumeEventBus;
import com.tech_sim.mymasjidapp.utils.Constants;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiPredicate;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class UserDashboard extends AppCompatActivity implements View.OnClickListener {
    CardView crdSalah, crdAnnouncement, crdCharity, crdQuestion, crdAnswer, crdAddChild, crdComunity, crdlogin;
    CardView crdpayfee, crdquran, crdsettings;

    String pemenantUser = "";
    TextView massage;

    Button logout;
    AdView adView;
    String ad_link, ad_image;
    private Constants constants;
    ImageView advertise_image;
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "masjid_notification_channel";
    TextView city, txt_time, txt_name;
        Thread myThread;
    MediaPlayer player;
    private ImageView sound;
    private boolean check = true;
    private NotificationManager notificationManager;
    private String type;
    private int not_count = 0;
    private int answer_count = 0;
    private LinearLayout layout_ann, layout_answer;
    private TextView txt_ann, txt_answer;
    private int result_code = 1000;
    private int result_code1 = 2000;
    private RelativeLayout layout;
    private boolean is_hand_pause = false;
    private boolean get_sal = false;
    private boolean noti = true;
    private boolean get_song = false;
    private NotificationManager mNotificationManager;
    private String last_played_song_start_time = "";
    //  private String[] start_times=new String[7];
    //private String[] salah_name=new String[7];
    private ArrayList<String> start_times = new ArrayList<>();
    private ArrayList<String> salah_name = new ArrayList<>();
    private Disposable disposable;

    private VolumeEventBus bus = VolumeEventBus.getInstance();
    private Disposable volumeDispose;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        IntentFilter intentFilter = new IntentFilter("com.teachnologybeach.masjjidapp.message");
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, intentFilter);
        constants = new Constants(this);
//        Runnable runnable = new CountDownRunner();
//        myThread = new Thread(runnable);
//        myThread.start();
        startTimeCheckService();

        listenForNewTime();

        getAdvertise();
        massage = findViewById(R.id.massage);
        txt_name = findViewById(R.id.t_name);
        txt_time = findViewById(R.id.namaztime);
        city = findViewById(R.id.nameprayer);
        city.setText(PreferenceManager.getCity());
        crdSalah = findViewById(R.id.Salah);
        crdAnnouncement = findViewById(R.id.Announcement);
        layout_answer = findViewById(R.id.answer_layout);
        txt_answer = findViewById(R.id.answer_noti);
        crdCharity = findViewById(R.id.Charity);
        crdQuestion = findViewById(R.id.Qusetion);
        txt_ann = findViewById(R.id.ann_noti);
        crdAnswer = findViewById(R.id.Answer);
        crdAddChild = findViewById(R.id.AddChild);
        crdComunity = findViewById(R.id.Comunity);
        crdlogin = findViewById(R.id.btnlogout);
        crdpayfee = findViewById(R.id.CrdPayFee);
        layout_ann = findViewById(R.id.noti_one);
        crdsettings = findViewById(R.id.Settingsss);
        crdquran = findViewById(R.id.Quran);
        advertise_image = findViewById(R.id.advertiser);
        layout = findViewById(R.id.layoutsound);
        sound = findViewById(R.id.sound);
        // lay_athan=findViewById(R.layout.layout)

        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        adView = findViewById(R.id.testAd);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        crdSalah.setOnClickListener(this);
        crdAnnouncement.setOnClickListener(this);
        crdCharity.setOnClickListener(this);
        crdQuestion.setOnClickListener(this);
        crdAnswer.setOnClickListener(this);
        crdAddChild.setOnClickListener(this);
        crdComunity.setOnClickListener(this);
        crdlogin.setOnClickListener(this);
        crdpayfee.setOnClickListener(this);
        crdsettings.setOnClickListener(this);
        crdquran.setOnClickListener(this);
        registerReceiver(mMessageReceiver, new IntentFilter("fgsdgdfgg"));
        if (PreferenceManager.getCont1() <= 0) {
            layout_ann.setVisibility(View.INVISIBLE);
            PreferenceManager.count_announce = 0;
            PreferenceManager.setCount1(0);

        } else {
            layout_ann.setVisibility(View.VISIBLE);
            txt_ann.setText(String.valueOf(PreferenceManager.getCont1()));
        }
        if (PreferenceManager.getCont2() <= 0) {
            PreferenceManager.count_answer = 0;
            PreferenceManager.setCount2(0);
            layout_answer.setVisibility(View.INVISIBLE);
        } else {

            layout_answer.setVisibility(View.VISIBLE);
            txt_answer.setText(String.valueOf(PreferenceManager.getCont2()));
        }

        volumeDispose = bus.listen()
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {

                if(aBoolean){
                    showVolumeIcon();
                }else {
                    showMuteIcon();
                }
                check = aBoolean;
            }
        });

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check) {
                    bus.brodcast(false);
                } else {
                    bus.brodcast(true);
                }
            }
        });
        for (int i = 0; i < PreferenceManager.timeModels.size(); i++) {
            //  start_times[i]=PreferenceManager.timeModels.get(i).getStart_time();
            start_times.add(PreferenceManager.timeModels.get(i).getStart_time());
            //  salah_name[i]=PreferenceManager.timeModels.get(i).getSalah_name();
            salah_name.add(PreferenceManager.timeModels.get(i).getSalah_name());
        }

    }

    private void showVolumeIcon() {
        sound.setImageResource(R.drawable.newsound);
    }

    private void showMuteIcon() {
        sound.setImageResource(R.drawable.newmute);
    }

    private void listenForNewTime() {

        disposable = TimeEventBus.getInstance().listen()
                .distinctUntilChanged(new BiPredicate<MasjidTime, MasjidTime>() {
                    @Override
                    public boolean test(MasjidTime time, MasjidTime time2) throws Exception {
                        return time.salah_name.equals(time2.salah_name);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MasjidTime>() {
                    @Override
                    public void accept(MasjidTime time) throws Exception {

                        txt_time.setText(time.startTime);
                        txt_name.setText(time.salah_name);

                    }
                });

    }

    private void startTimeCheckService() {
        Intent intent = new Intent(this, MasjidTimeService.class);
            startService(intent);
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(mMessageReceiver);
        disposable.dispose();
        volumeDispose.dispose();
        super.onDestroy();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1000:
                    if (data.getBooleanExtra("verification", false)) {
                        if (PreferenceManager.getCont1() <= 0) {
                            PreferenceManager.count_announce = 0;
                            PreferenceManager.setCount1(0);
                            layout_ann.setVisibility(View.INVISIBLE);
                        } else {
                            layout_ann.setVisibility(View.VISIBLE);
                            txt_ann.setText(String.valueOf(PreferenceManager.getCont1()));
                        }
                    }
                    break;
                case 2000:
                    if (data.getBooleanExtra("verification", false)) {
                        if (PreferenceManager.getCont2() <= 0) {
                            PreferenceManager.count_answer = 0;
                            PreferenceManager.setCount2(0);
                            layout_answer.setVisibility(View.INVISIBLE);
                        } else {
                            layout_answer.setVisibility(View.VISIBLE);
                            txt_answer.setText(String.valueOf(PreferenceManager.getCont2()));
                        }
                    }
                    break;
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.Salah:

                //    getSalahTime();
                Intent intent = new Intent(UserDashboard.this, SalahActivity.class);
                startActivity(intent);

                break;

            case R.id.Announcement:
                Intent intent1 = new Intent(UserDashboard.this, UserAnnounceSeeActivity.class);
                intent1.putExtra("face_verification", false);
                startActivityForResult(intent1, result_code);
                break;
//
            case R.id.Charity:
                Intent intent2 = new Intent(UserDashboard.this, Charity.class);
                startActivity(intent2);
                break;
//
            case R.id.Qusetion:
                Intent intent3 = new Intent(UserDashboard.this, UserQuestionActivity.class);
                startActivity(intent3);
                break;
//
            case R.id.Answer:
                Intent intent4 = new Intent(UserDashboard.this, MasjidAnswerActivity.class);
                intent4.putExtra("face_verification", false);
                startActivityForResult(intent4, result_code1);
                break;
//
//                break;
//
            case R.id.AddChild:

                Intent intent5 = new Intent(UserDashboard.this, ManageChildActivit.class);
                startActivity(intent5);
                break;

            case R.id.Comunity:
                Intent intent6 = new Intent(UserDashboard.this, ViewMasjidCommit.class);
                startActivity(intent6);
                break;
            case R.id.CrdPayFee:
                Intent intent8 = new Intent(UserDashboard.this, PayFee.class);
                startActivity(intent8);
                break;
            case R.id.Settingsss:
                Intent intent10 = new Intent(UserDashboard.this, SettingUser.class);
                startActivity(intent10);
                break;
            case R.id.Quran:
                Intent intent11 = new Intent(UserDashboard.this, QuranActivity.class);
                startActivity(intent11);
                break;

            case R.id.btnlogout:
                PreferenceManager.setCount1(0);
                PreferenceManager.setCount2(0);
//                PreferenceManager.setSong("");
                getLogout();

                break;
            default:
                break;
        }

    }


    public void getAdvertise() {
        String url = "http://mymasjid.space/mobileApp/admin/banner";
        constants.kpHUD.show();
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        constants.kpHUD.dismiss();
                        try {
                            if (response.getString("error").equals("false")) {
                                JSONObject object = response.getJSONObject("result");
                                ad_link = object.getString("ad_link");
                                ad_image = object.getString("ad_image");
                                Glide.with(UserDashboard.this).load(ad_image).into(advertise_image);
                                moveTowebView();
                            } else {
                                Toast.makeText(UserDashboard.this, response.getString("result"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        constants.kpHUD.dismiss();
                        Log.d("Error.Response", "that is not");
                    }
                }
        );
        PreferenceManager.getInstance().addToRequestQueue(getRequest);

    }

    public void moveTowebView() {
        advertise_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(Registration.this,"move to webview",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(ad_link));
                startActivity(intent);
            }
        });
    }

    class CountDownRunner implements Runnable {
        // @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    timeWork();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                }
            }
        }
    }

//    public void doWork() {
//        runOnUiThread(new Runnable() {
//            public void run() {
//                Calendar c = Calendar.getInstance();
//                int hrs = c.get(Calendar.HOUR_OF_DAY);//24
//                int min = c.get(Calendar.MINUTE);//59
//                String format;
//                final int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
//                String current_time;
//                if (hrs == 0) {
//                    hrs += 12;
//                    format = "AM";
//                } else if (hrs == 12) {
//                    format = "PM";
//                } else if (hrs > 12) {
//                    hrs -= 12;
//                    format = "PM";
//                } else {
//                    format = "AM";
//                }
//
//                if (hrs > 9 && min > 9) {
//                    current_time = String.valueOf(hrs) + ":" + String.valueOf(min) + " " + format;
//                } else if (hrs < 9 && min < 9) {
//                    current_time = "0" + String.valueOf(hrs) + ":" + "0" + String.valueOf(min) + " " + format;
//                } else if (hrs < 9 && min > 9) {
//                    current_time = "0" + String.valueOf(hrs) + ":" + String.valueOf(min) + " " + format;
//                } else {
//                    current_time = String.valueOf(hrs) + ":" + "0" + String.valueOf(min) + " " + format;
//                }
//
//                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
//                int count = PreferenceManager.timeModels.size();
//                try {
//                    Date d1 = sdf.parse(current_time);
//                    Log.i("time", String.valueOf(d1));
//                    if (dayOfWeek == 6) {
//                        for (int i = 0; i < count; i++) {
//                            if (PreferenceManager.timeModels.get(i).getSalah_name().equals("Zohr")) {
//                                PreferenceManager.timeModels.remove(i);
//                                break;
//                            }
//                        }
//                    } else {
//                        for (int i = 0; i < count; i++) {
//                            if (PreferenceManager.timeModels.get(i).getSalah_name().equals("Juma")) {
//                                PreferenceManager.timeModels.remove(i);
//                                break;
//                            }
//                        }
//                    }
//                    int count1 = PreferenceManager.timeModels.size();
//                    for (int i = 0; i < PreferenceManager.timeModels.size(); i++) {
//                        Date d2 = sdf.parse(PreferenceManager.timeModels.get(i).getStart_time());
//                        Date d3 = sdf.parse(PreferenceManager.timeModels.get((i + 1) % count1).getStart_time());
//                        if (d1.before(d3) && d1.after(d2)) {
//                            Log.i("TAG", "after");
//                            txt_time.setText(PreferenceManager.timeModels.get((i + 1) % count1).getStart_time());
//                            txt_name.setText(PreferenceManager.timeModels.get((i + 1) % count1).getSalah_name());
//                            get_sal = true;
//                        }
//                        if (PreferenceManager.timeModels.get(i).getStart_time().equals(current_time)) {
//                            last_played_song_start_time = PreferenceManager.timeModels.get(i).getStart_time();
//                            if (PreferenceManager.timeModels.get(i).getSalah_name().equals("Tahajud")) {
//                                Log.i("TAG", "same");
//                                Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//                                    createNotificationChannel();
//                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(UserDashboard.this, CHANNEL_ID)
//                                            .setSmallIcon(R.drawable.salahicon)
//                                            .setContentTitle("Masjid App")
//                                            .setContentText("Tahajud salah started" + " " + PreferenceManager.timeModels.get(i).getStart_time())
//                                            .setAutoCancel(true);
//                                    notificationManager.notify(NOTIFICATION_ID, builder.build());
//                                    noti = false;
//
//
//                                } else {
//                                    notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                                    NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(UserDashboard.this)
//                                            .setSmallIcon(R.drawable.salahicon)
//                                            .setContentTitle("Masjid App")
//                                            .setContentText("Tahajud salah started" + " " + PreferenceManager.timeModels.get(i).getStart_time())
//                                            .setAutoCancel(true);
//                                    notificationManager.notify(0 /*ID of notification*/, notifiBuilder.build());
//                                }
//                            } else if (dayOfWeek == 6) {
//
//                                if (!PreferenceManager.timeModels.get(i).getSalah_name().equals("Zohr")) {
//                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//                                        voiceSong();
//                                        createNotificationChannel();
//                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(UserDashboard.this, CHANNEL_ID)
//                                                .setSmallIcon(R.drawable.salahicon)
//                                                .setContentTitle("Masjid App")
//                                                .setContentText(PreferenceManager.timeModels.get(i).getSalah_name() + " " + "salah started" + " " + PreferenceManager.timeModels.get(i).getStart_time())
//                                                .setAutoCancel(true);
//                                        notificationManager.notify(NOTIFICATION_ID, builder.build());
//
//
//                                    } else {
//                                        voiceSong();
////                                            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
////                                            NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(UserDashboard.this)
////                                                    .setSmallIcon(R.drawable.salahicon)
////                                                    .setContentTitle("Masjid App")
////                                                    .setContentText(PreferenceManager.timeModels.get(i).getSalah_name()+" "+"salah started"+" "+PreferenceManager.timeModels.get(i).getStart_time())
////                                                    .setAutoCancel(true);
////                                            notificationManager.notify(0 /*ID of notification*/, notifiBuilder.build());
//                                        NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(UserDashboard.this)
//                                                .setSmallIcon(R.drawable.salahicon)
//                                                .setContentTitle("Masjid App")
//                                                .setContentText(PreferenceManager.timeModels.get(i).getSalah_name() + " " + "salah started" + " " + PreferenceManager.timeModels.get(i).getStart_time())
//                                                .setAutoCancel(true);
//                                        notificationManager.notify(0 /*ID of notification*/, notifiBuilder.build());
//
//                                    }
//                                }
//                            } else {
//                                if (!PreferenceManager.timeModels.get(i).getSalah_name().equals("Juma")) {
//                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//                                        voiceSong();
//                                        createNotificationChannel();
//                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(UserDashboard.this, CHANNEL_ID);
//
//                                        notificationManager.notify(NOTIFICATION_ID, builder.build());
//
//                                    } else {
//
//                                        voiceSong();
//                                        //                                          notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
////                                            NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(UserDashboard.this)
////                                                    .setSmallIcon(R.drawable.salahicon)
////                                                    .setContentTitle("Masjid App")
////                                                    .setContentText(PreferenceManager.timeModels.get(i).getSalah_name()+" "+"salah started"+" "+PreferenceManager.timeModels.get(i).getStart_time())
////                                                    .setAutoCancel(true);
////                                            notificationManager.notify(0 /*ID of notification*/, notifiBuilder.build());
//                                        Notification notification = new NotificationCompat.Builder(UserDashboard.this)
//                                                .setSmallIcon(R.drawable.salahicon)
//                                                .setContentTitle("Masjid App")
//                                                .setContentText(PreferenceManager.timeModels.get(i).getSalah_name() + " " + "salah started" + " " + PreferenceManager.timeModels.get(i).getStart_time())
//                                                .setAutoCancel(true).build();
//
//                                        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//                                        notificationManager.notify(0, notification);
//
//                                    }
//                                }
//                            }
//                        }
//
//                    }
//                    if (get_sal == false) {
//                        if (PreferenceManager.timeModels.size() != 0) {
//                            txt_time.setText(PreferenceManager.timeModels.get(0).getStart_time());
//                            txt_name.setText(PreferenceManager.timeModels.get(0).getSalah_name());
//                        }
//                    }
//
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
//    }

    private String playing_song_start_time = "";

    public void voiceSong() {
        if (playing_song_start_time.equals(last_played_song_start_time)) return;
        playing_song_start_time = last_played_song_start_time;
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

    public void timeWork() {
        runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
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
                    for (int i = 0; i < count; i++) {
                        if (PreferenceManager.timeModels.get(i).getSalah_name().equals("Zohr")) {
                            PreferenceManager.timeModels.remove(i);
                            break;
                        }
                    }
                } else {
                    for (int i = 0; i < count; i++) {
                        if (PreferenceManager.timeModels.get(i).getSalah_name().equals("Juma")) {
                            PreferenceManager.timeModels.remove(i);
                            break;
                        }
                    }
                }
                int count1 = PreferenceManager.timeModels.size();
                SimpleDateFormat parser = new SimpleDateFormat("hh:mm a");
                try {
                    Date date2 = parser.parse(current_time);
                    for (int i = 0; i < count1; i++) {
                        // get_sal=false;
                        Date date1 = parser.parse(PreferenceManager.timeModels.get(i).getStart_time());
                        Date date3 = parser.parse(PreferenceManager.timeModels.get((i + 1) % count1).getStart_time());
                        if (date2.before(date3) && date2.after(date1)) {
                            txt_time.setText(PreferenceManager.timeModels.get((i + 1) % count1).getStart_time());
                            txt_name.setText(PreferenceManager.timeModels.get((i + 1) % count1).getSalah_name());
                            get_sal = true;
                        }

//                        if (PreferenceManager.timeModels.get(i).getStart_time().equals(current_time)) {
//                            last_played_song_start_time = PreferenceManager.timeModels.get(i).getStart_time();
//                            if (PreferenceManager.timeModels.get(i).getSalah_name().equals("Tahajud")) {
//                                // android version >oreo
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                    createNotificationChannel();
//                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(UserDashboard.this, CHANNEL_ID);
//                                    Notification notification = builder.setContentTitle("My Masjid")
//                                            .setContentText("Tahajud salah started" + " " + PreferenceManager.timeModels.get(i).getStart_time())
//                                            .setTicker("New SalahTime")
//                                            .setAutoCancel(true)
//                                            .setSmallIcon(R.drawable.salahicon)
//                                            .build();
//                                    NotificationManager notificationManager = (NotificationManager) (UserDashboard.this).getSystemService(Context.NOTIFICATION_SERVICE);
//                                    notificationManager.notify(0, notification);
//
//
//                                } else {
//                                    //android version<oreo
//                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(UserDashboard.this);
//                                    Notification notification = builder.setContentTitle("My Masjid")
//                                            .setContentText("Tahajud salah started" + " " + PreferenceManager.timeModels.get(i).getStart_time())
//                                            .setTicker("New SalahTime")
//                                            .setAutoCancel(true)
//                                            .setSmallIcon(R.drawable.salahicon)
//                                            .build();
//                                    NotificationManager notificationManager = (NotificationManager) (UserDashboard.this).getSystemService(Context.NOTIFICATION_SERVICE);
//                                    notificationManager.notify(0, notification);
//
//                                }
//
//
//                            } else if (dayOfWeek == 6) {
//
//                                if (!PreferenceManager.timeModels.get(i).getSalah_name().equals("Zohr")) {
//                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                        createNotificationChannel();
//                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(UserDashboard.this, CHANNEL_ID);
//                                        Notification notification = builder.setContentTitle("My Masjid")
//                                                .setContentText(PreferenceManager.timeModels.get(i).getSalah_name() + " " + "salah started" + " " + PreferenceManager.timeModels.get(i).getStart_time())
//                                                .setTicker("New SalahTime")
//                                                .setAutoCancel(true)
//                                                .setSmallIcon(R.drawable.salahicon)
////                                                .setSound(Uri.parse(PreferenceManager.getSong()))
//                                                .build();
//                                        NotificationManager notificationManager = (NotificationManager) (UserDashboard.this).getSystemService(Context.NOTIFICATION_SERVICE);
//                                        notificationManager.notify(0, notification);
//                                        voiceSong();
//
//                                    } else {
//                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(UserDashboard.this);
//                                        Notification notification = builder.setContentTitle("My Masjid")
//                                                .setContentText(PreferenceManager.timeModels.get(i).getSalah_name() + " " + "salah started" + " " + PreferenceManager.timeModels.get(i).getStart_time())
//                                                .setTicker("New SalahTime")
//                                                .setAutoCancel(true)
//                                                .setSmallIcon(R.drawable.salahicon)
////                                                .setSound(Uri.parse(PreferenceManager.getSong()))
//                                                .build();
//                                        NotificationManager notificationManager = (NotificationManager) (UserDashboard.this).getSystemService(Context.NOTIFICATION_SERVICE);
//                                        notificationManager.notify(0, notification);
//                                        voiceSong();
//                                    }
//
//                                }
//
//                            } else {
//                                if (!PreferenceManager.timeModels.get(i).getSalah_name().equals("Juma")) {
//                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                        createNotificationChannel();
//                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(UserDashboard.this, CHANNEL_ID);
//                                        Notification notification = builder.setContentTitle("My Masjid")
//                                                .setContentText(PreferenceManager.timeModels.get(i).getSalah_name() + " " + "salah started" + " " + PreferenceManager.timeModels.get(i).getStart_time())
//                                                .setTicker("New SalahTime")
//                                                .setAutoCancel(true)
//                                                .setSmallIcon(R.drawable.salahicon)
////                                                .setSound(Uri.parse(PreferenceManager.getSong()))
//                                                .build();
//                                        NotificationManager notificationManager = (NotificationManager) (UserDashboard.this).getSystemService(Context.NOTIFICATION_SERVICE);
//                                        notificationManager.notify(0, notification);
//                                        voiceSong();
//
//                                    } else {
//                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(UserDashboard.this);
//                                        Notification notification = builder.setContentTitle("My Masjid")
//                                                .setContentText(PreferenceManager.timeModels.get(i).getSalah_name() + " " + "salah started" + " " + PreferenceManager.timeModels.get(i).getStart_time())
//                                                .setTicker("New SalahTime")
//                                                .setAutoCancel(true)
//                                                .setSmallIcon(R.drawable.salahicon)
////                                                .setSound(Uri.parse(PreferenceManager.getSong()))
//                                                .build();
//                                        NotificationManager notificationManager = (NotificationManager) (UserDashboard.this).getSystemService(Context.NOTIFICATION_SERVICE);
//                                        notificationManager.notify(0, notification);
//                                        voiceSong();
//                                    }
//
//                                }
//                            }
//                        }
                    }
                    if (!get_sal) {
                        if (PreferenceManager.timeModels.size() != 0) {
                            txt_time.setText(PreferenceManager.timeModels.get(0).getStart_time());
                            txt_name.setText(PreferenceManager.timeModels.get(0).getSalah_name());
                        }

                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//
//            Intent intent = new Intent(UserDashboard.this, ScheduleService.class);
//            Bundle b = new Bundle();
//            b.putStringArrayList("array", start_times);
//            b.putStringArrayList("name", salah_name);
//            b.putString("song", PreferenceManager.getSong());
//            intent.putExtras(b);
//            startService(intent);
//        } else {
//            Log.d("oldphone", "i will kill you.");
//        }
    }

    public void getLogout() {
        String url = "http://mymasjid.space/mobileApp/admin/user_logout";
        final String user_id = PreferenceManager.getUserid();
        constants.kpHUD.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        constants.kpHUD.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("error").equals("false")) {
                                PreferenceManager.salahTimeModels = new ArrayList<>();
                                PreferenceManager.setCheck(-1);
                                Intent intent7 = new Intent(UserDashboard.this, RegistrationMainScreen.class);
                                startActivity(intent7);
                                finish();
                            } else {
                                Toast.makeText(UserDashboard.this, "Not refund", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                constants.kpHUD.dismiss();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", user_id);
                return params;
            }
        };
        PreferenceManager.getInstance().addToRequestQueue(stringRequest);


    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }

    private BroadcastReceiver onNotice = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Update your RecyclerView here using notifyItemInserted(position);
            Bundle bundle = intent.getExtras();
            type = bundle.getString("type");
            if (type.equals("announce")) {
                not_count = bundle.getInt("count");
                layout_ann.setVisibility(View.VISIBLE);
                txt_ann.setText(String.valueOf(not_count));
            } else if (type.equals("answer")) {
                answer_count = bundle.getInt("count");
                layout_answer.setVisibility(View.VISIBLE);
                txt_answer.setText(String.valueOf(answer_count));
            }
        }


    };

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
//            AudioAttributes audioAttributes = new AudioAttributes.Builder()
//                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
//                    .build();
//            notificationChannel.setSound(Uri.parse(PreferenceManager.getSong()),audioAttributes);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }

    };


}
