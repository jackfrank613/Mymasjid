package com.tech_sim.mymasjidapp.activity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.utils.Constants;

public class AnnonceDetailer extends AppCompatActivity {

    public String type="";
    String audio="";
    String date="";
    String des="";
    TextView announcementType, annuncementDate, announcementDescrpto;
    private ImageView img_play,img_stop;
    SeekBar seekBar;
    MediaPlayer mediaPlayer;
    Chronometer chronometer;
    Button button;
    private boolean ischeck=true;
    String name="";
    String detail="";
    String datee="";
    String audios="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_detail);

        final Intent intent=getIntent();
        initXml();
        if(Constants.check_noti) {
            name = intent.getStringExtra("name");
            detail = intent.getStringExtra("detail");
            datee = intent.getStringExtra("date");
            audios = intent.getStringExtra("audio");
            announcementType.setText(name);
            announcementDescrpto.setText(detail);
            annuncementDate.setText(datee);
        }
        else {
            type=intent.getStringExtra("type");
            date=intent.getStringExtra("date");
            audio=intent.getStringExtra("audio");
            des=intent.getStringExtra("description");
            announcementType.setText(type);
            announcementDescrpto.setText(des);
            annuncementDate.setText(date);

        }
        img_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ischeck)
                {
                    img_play.setImageResource(R.mipmap.tt);
                    startPlay();
                    ischeck=false;
                }
                else {
                    img_play.setImageResource(R.mipmap.play);
                    ischeck=true;
                }

            }
        });
        img_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPlay();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_stop.setEnabled(false);
                Constants.check_noti=false;
//                Intent intent1=new Intent(AnnonceDetailer.this,UserAnnounceSeeActivity.class);
//                startActivity(intent1);
                finish();
            }
        });

    }
    public void initXml(){

        announcementType = findViewById(R.id.AnnouncementType);
        announcementDescrpto = findViewById(R.id.AnnouncementDescripto);

        announcementDescrpto.setVerticalScrollBarEnabled(true);
        announcementDescrpto.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        announcementDescrpto.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        announcementDescrpto.setMovementMethod(ScrollingMovementMethod.getInstance());
        annuncementDate = findViewById(R.id.AnnounceentDate);
        // seekBar=findViewById(R.id.SeekBarA);
        img_play=findViewById(R.id.playAnnouncement);
        img_stop=findViewById(R.id.st_play);
        chronometer=findViewById(R.id.EndTime);
        button=findViewById(R.id.btnAnnoncementcancel);
        img_stop.setEnabled(false);
    }

    public void startPlay(){
        try {
            mediaPlayer=new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            if(Constants.check_noti) {
                mediaPlayer.setDataSource(audios);
            }
            else {
                mediaPlayer.setDataSource(audio);
            }
            mediaPlayer.prepare();
            mediaPlayer.start();
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            Toast.makeText(AnnonceDetailer.this,"Start playing...",Toast.LENGTH_SHORT).show();
            img_stop.setEnabled(true);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    chronometer.stop();
                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void stopPlay(){
        try {
            mediaPlayer.stop();
            mediaPlayer.release();
            chronometer.stop();
            Toast.makeText(AnnonceDetailer.this, "Stop playing...", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }



}
