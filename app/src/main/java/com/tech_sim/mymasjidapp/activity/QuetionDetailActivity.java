package com.tech_sim.mymasjidapp.activity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tech_sim.mymasjidapp.R;


public class QuetionDetailActivity extends AppCompatActivity {
    private TextView txt_name,txt_date,txt_question;
    private Button btn_cancel,btn_answer;
    private String name;
    private String date,question;
    private String audio;
    private Chronometer chronometer;
    private ImageView img_player,img_stop;
    private boolean isChecking=true;
    private MediaPlayer mediaPlayer;
    private String user_id;
    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_deatil);
        final Intent intent=getIntent();
        name=intent.getStringExtra("name");
        date=intent.getStringExtra("date");
        question=intent.getStringExtra("question");
        audio=intent.getStringExtra("audio");
        user_id=intent.getStringExtra("user_id");
        initXml();
        txt_name.setText(name);
        txt_date.setText(date);
        txt_question.setText(question);
        img_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isChecking)
                {
                    img_player.setImageResource(R.mipmap.tt);
                    startPlayer();
                    isChecking=false;
                }
                else {
                    img_player.setImageResource(R.mipmap.play);
                    stopPlayer();
                    isChecking=true;
                }

            }
        });
        img_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perfectStop();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               finish();
            }
        });
        btn_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(QuetionDetailActivity.this,GiveAnswerActivity.class);
                intent1.putExtra("user_id",user_id);
                startActivity(intent1);
            }
        });
    }
    public void initXml(){
        txt_name=(TextView)findViewById(R.id.name);
        txt_date=(TextView)findViewById(R.id.date);
        txt_question=(TextView)findViewById(R.id.question);
        btn_answer=(Button)findViewById(R.id.b_answer);
        btn_cancel=(Button)findViewById(R.id.b_cancel);
        chronometer=(Chronometer)findViewById(R.id.EndTime);
        img_player=(ImageView)findViewById(R.id.player);
        img_stop=(ImageView)findViewById(R.id.stop);
    }

    public void startPlayer(){
          mediaPlayer=new MediaPlayer();
          try{
              mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
              mediaPlayer.setDataSource(audio);
              mediaPlayer.prepare();
              mediaPlayer.start();
              chronometer.start();
          }catch (Exception e)
          {
              e.printStackTrace();
          }
    }
    public void stopPlayer(){
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            chronometer.stop();
        }
        else {
            Toast.makeText(QuetionDetailActivity.this,"Please,waiting...",Toast.LENGTH_SHORT).show();
        }

    }
    public void perfectStop(){
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=null;
            chronometer.stop();
        }
        else {
            Toast.makeText(QuetionDetailActivity.this,"Please,waiting...",Toast.LENGTH_SHORT).show();
        }
    }


}
