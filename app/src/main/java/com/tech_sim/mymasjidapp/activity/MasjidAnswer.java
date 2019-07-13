package com.tech_sim.mymasjidapp.activity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tech_sim.mymasjidapp.R;

public class MasjidAnswer  extends AppCompatActivity {
    private String masji_name;
    private String date;
    private String audio;
    private String answer;
    private TextView txt_name,txt_date,txt_detail;
    private Button button;
    private ImageView img_play,img_stop;
    private MediaPlayer mediaPlayer;
    private Chronometer chronometer;
    private boolean ischeck=true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.masjid_answer);
        Intent intent=getIntent();
        answer=intent.getStringExtra("answer");
        masji_name=intent.getStringExtra("masjidname");
        date=intent.getStringExtra("date");
        audio=intent.getStringExtra("audio");
        txt_name=(TextView)findViewById(R.id.masjidname);
        txt_date=(TextView)findViewById(R.id.answertime);
        txt_detail=(TextView)findViewById(R.id.answerdetail);
        button=(Button)findViewById(R.id.button_cancel);
        chronometer=(Chronometer)findViewById(R.id.EndTime);
        img_play=(ImageView)findViewById(R.id.play);
        img_stop=(ImageView)findViewById(R.id.stop);
        txt_name.setText(masji_name);
        txt_detail.setText(answer);
        txt_date.setText(date);
        img_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ischeck)
                {
                    Toast.makeText(MasjidAnswer.this,"Start playing",Toast.LENGTH_SHORT).show();
                    img_play.setImageResource(R.mipmap.tt);
                    startPlay();
                    ischeck=false;

                }
                else {
                    img_play.setImageResource(R.mipmap.play);
                    stopPlay();
                    ischeck=true;
                }

            }
        });
        img_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MasjidAnswer.this,"Stop playing",Toast.LENGTH_SHORT).show();
                stopPlay();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MasjidAnswer.this,MasjidAnswerActivity.class));
                finish();
            }
        });

    }
    public void startPlay()
    {
        try {
            mediaPlayer=new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(audio);
            mediaPlayer.prepare();
            mediaPlayer.start();
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void stopPlay(){
        try {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=null;
            chronometer.stop();
        }catch (Exception e)
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
