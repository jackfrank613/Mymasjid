package com.tech_sim.mymasjidapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.utils.Constants;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;
import com.tech_sim.mymasjidapp.utils.VolleyMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class UserQuestionActivity extends AppCompatActivity implements View.OnClickListener {
    EditText questionDescriptionUser;
    Button btncancel,btnsend;
    SeekBar seekBar;
    ProgressDialog progressDialogQuestionUser;
    ImageView QPlay,QRecording;
    private boolean isRecording=true;
    private MediaPlayer mediaPlayer;
    private MediaRecorder mediaRecorder;
    private Context context;
    private String FILE_NAME;
    public static  final int RequestPermissionCode=1;
    private Chronometer chronometer;
    private int lastProgress = 0;
    private Handler mHandler = new Handler();
    private boolean isPlaying=true;
    private Constants constants;
    String resultQ,mAddQuestionDescriptionHolder,MasjidID,UserId;
    int status;
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
        setContentView(R.layout.activity_user_question);
        context=getApplicationContext();
        constants=new Constants(this);
        QPlay = findViewById(R.id.QPlay);
        QRecording = findViewById(R.id.QRecording);
        btncancel = findViewById(R.id.btnQcacel);
        btnsend = findViewById(R.id.btnQsend);
        questionDescriptionUser = findViewById(R.id.QuestiondescriptionUser);
        chronometer=findViewById(R.id.textTimeLast);
        if(checkPermission()) {
            Log.d("recorder","dfsdfd");
        }
        else {
            requestPermission();
        }
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(UserQuestionActivity.this,UserDashboard.class));
                finish();
            }
        });
        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description=questionDescriptionUser.getText().toString().trim();
                if(mediaRecorder!=null) {
                    sendRequest(description);
                }
                else {
                    Toast.makeText(UserQuestionActivity.this,"Please,record your voice",Toast.LENGTH_SHORT).show();
                }
            }
        });
        QRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRecording)
                {
                    QRecording.setImageResource(R.mipmap.tt);
                    startRecording();
                    isRecording=false;
                }
                else {
                    QRecording.setImageResource(R.mipmap.recording);
                    stopRecording();
                    isRecording=true;
                }
            }
        });
        QPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlaying)
                {
                    QPlay.setImageResource(R.mipmap.stop);
                    startPlay();
                    isPlaying=false;
                    //    seekBar.setProgress(0);
                }
                else {
                    QPlay.setImageResource(R.mipmap.play);
                    stopPlay();
                    isPlaying=true;
                }

            }
        });

    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.QRecording:

                break;
        }

    }
    public void stopPlay(){

        if(mediaPlayer!=null)
        {
            try {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer=null;
                // chronometer.stop();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(UserQuestionActivity.this,"Please, record your voice.",Toast.LENGTH_SHORT).show();
        }

    }
    public void startPlay(){
        mediaPlayer=new MediaPlayer();
        try {
            mediaPlayer.setDataSource(FILE_NAME);
            mediaPlayer.prepare();
            mediaPlayer.start();
            Toast.makeText(UserQuestionActivity.this,"Start playing...",Toast.LENGTH_SHORT).show();

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // imageViewPlay.setImageResource(R.drawable.ic_play);
//                isPlaying = false;
                chronometer.stop();

            }
        });
    }public void startRecording()
    {

            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            File root = android.os.Environment.getExternalStorageDirectory();
            File file = new File(root.getAbsolutePath() + "/VoiceRecorderSimplifiedCoding/Audios");
            if (!file.exists()) {
                file.mkdirs();
            }
            FILE_NAME = root.getAbsolutePath() + "/VoiceRecorderSimplifiedCoding/Audios/" +
                    String.valueOf(System.currentTimeMillis() + ".wav");
            mediaRecorder.setOutputFile(FILE_NAME);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            try {
                mediaRecorder.prepare();

            } catch (IOException e) {
                Log.e("TAG", "prepare() failed:" + e);

            } catch (IllegalStateException e) {
                Log.e("TAG", "prepare() failed:" + e);
            }
            try {
                mediaRecorder.start();
                Toast.makeText(UserQuestionActivity.this, "Started Recording", Toast.LENGTH_SHORT).show();

            } catch (IllegalStateException e) {
                Log.e("TAG", "start() failed:" + e);
            }

        lastProgress = 0;
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();


    }
    public void stopRecording(){
        try{
            mediaRecorder.stop();
            chronometer.stop();
            Toast.makeText(UserQuestionActivity.this,"Stopped Recording",Toast.LENGTH_SHORT).show();
        }catch (NullPointerException e)
        {
            Log.i("TAG","stop() error:"+e);
        }catch (IllegalStateException e){
            Log.i("TAG","stop() error:"+e);
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;
                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(context, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context,"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(UserQuestionActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }
    public void sendRequest(final String des)
    {
        if (mediaRecorder!=null)mediaRecorder.release();
        mediaRecorder=null;
        File file = new File(FILE_NAME);
        int size = (int) file.length();
        final byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String url="http://mymasjid.space/mobileApp/admin/make_question_user";
        String tag="personal";
        final String masjid= PreferenceManager.getMasjid();
        final String user_id=PreferenceManager.getUserid();
        constants.kpHUD.show();
        VolleyMultipartRequest req = new VolleyMultipartRequest(com.android.volley.Request.Method.POST,
                url, new com.android.volley.Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                constants.kpHUD.dismiss();
                String responseStr=new String(response.data);
                try {
                    JSONObject responseobject=new JSONObject(responseStr);
                    if (responseobject.getString("error").equals("false")) {
                        String  message=responseobject.getString("result");
                        //   btn_save.setEnabled(true);
                        Toast.makeText(UserQuestionActivity.this,message,Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(UserQuestionActivity.this, UserDashboard.class);
                        startActivity(intent);

                        try {
                            finalize();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }
                }catch (JSONException ignored){}
            }
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                constants.kpHUD.dismiss();
//                btn_save.setEnabled(true);
                Log.d("Error.Response","that is not");
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                HashMap<String,String> params=new HashMap<String,String>();
                params.put("question",des);
                params.put("masjid",masjid);
                params.put("status","1");
                params.put("user_id",user_id);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                params.put("audio", new DataPart("audio"+System.currentTimeMillis()+".wav", bytes, "audio/x-wav"));
                return params;
            }

        };
        // add the request object to the queue to be executed
        PreferenceManager.getInstance().addToRequestQueue(req,tag);

    }

}
