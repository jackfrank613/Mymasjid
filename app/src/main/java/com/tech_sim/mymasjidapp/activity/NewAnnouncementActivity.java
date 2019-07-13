package com.tech_sim.mymasjidapp.activity;

import android.app.Activity;
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
import android.widget.Spinner;
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
import java.util.Random;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class NewAnnouncementActivity extends AppCompatActivity implements View.OnClickListener {
    private Chronometer chronometer;
    private ImageView imageViewRecord, imageViewPlay, imageViewStop;
    private SeekBar seekBar;
    private int RECORD_AUDIO_REQUEST_CODE = 123;
    private MediaRecorder mRecorder;
    private MediaPlayer mediaPlayer;
    private String fileNames = null;
    private int lastProgress = 0;
    private Handler mHandler = new Handler();
    private boolean isPlaying = false;
    private boolean isRecording = true;
    private String FILE_NAME="";
    String result;
    private Button btn_save, btn_cancel;
    private static final int SELECT_AUDIO = 2;
    private EditText edt_announce;
    private Spinner spinner;
    String AudioSavePathInDevice = null;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    Random random ;
    MediaRecorder mediaRecorder;
    public static final int RequestPermissionCode = 1;
    private Constants constants;
    private boolean check;
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
        setContentView(R.layout.voice_track_layout);
        constants=new Constants(this);
        initXml();
       if(checkPermission())
       {
           Log.e("TAG", "recording");
       }
       else {
               requestPermission();

       }

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description=edt_announce.getText().toString().trim();
                String type=spinner.getSelectedItem().toString();
                sendRecorde(description,type);
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void initXml() {

        chronometer = (Chronometer) findViewById(R.id.endtime);
        chronometer.setBase(SystemClock.elapsedRealtime());
        imageViewRecord = (ImageView) findViewById(R.id.recording);
        imageViewStop = (ImageView) findViewById(R.id.stop);
        imageViewPlay = (ImageView) findViewById(R.id.play);
        btn_save = (Button) findViewById(R.id.btn_send);
        seekBar = (SeekBar) findViewById(R.id.SeekBar);
        btn_cancel = (Button) findViewById(R.id.btnAddAnnouncementcacel);
        edt_announce = (EditText) findViewById(R.id.AddAnnouncementDescripition);
        spinner = (Spinner) findViewById(R.id.SelectAnnouncementType);
        imageViewRecord.setOnClickListener(this);
        imageViewStop.setOnClickListener(this);
        imageViewPlay.setOnClickListener(this);
        imageViewPlay.setEnabled(false);
    }
    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.recording:
                if(isRecording)
                {
                    imageViewRecord.setImageResource(R.mipmap.tt);
                    startRecording();
                    isRecording=false;
                    imageViewPlay.setEnabled(true);
                }
                else {
                    imageViewRecord.setImageResource(R.mipmap.recording);
                    stopRecording();
                    isRecording=true;
                }
                break;
            case R.id.play:
                startPlaying();
                break;
            case R.id.stop:
                stopPlaying();
                break;
        }

    }


    private void startRecording() {

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
                Toast.makeText(NewAnnouncementActivity.this, "Started Recording", Toast.LENGTH_SHORT).show();

            } catch (IllegalStateException e) {
                Log.e("TAG", "start() failed:" + e);
            }


        lastProgress = 0;
        seekBar.setProgress(0);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

    }
    private void stopPlaying() {
        if(mediaPlayer!=null)
        {
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
        else {
            Toast.makeText(NewAnnouncementActivity.this,"Please, record your voice.",Toast.LENGTH_SHORT).show();
        }


    }

    private void stopRecording() {

        try{
            mediaRecorder.stop();
            chronometer.stop();
            Toast.makeText(NewAnnouncementActivity.this,"Stopped Recording",Toast.LENGTH_SHORT).show();
        }catch (NullPointerException e)
        {
            Log.i("TAG","stop() error:"+e);
        }catch (IllegalStateException e){
            Log.i("TAG","stop() error:"+e);
        }


    }

    private void startPlaying() {
        mediaPlayer=new MediaPlayer();
        try {
            mediaPlayer.setDataSource(FILE_NAME);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        seekBar.setProgress(lastProgress);
        mediaPlayer.seekTo(lastProgress);
        seekBar.setMax(mediaPlayer.getDuration());
        seekUpdation();
        chronometer.start();

        /** once the audio is complete, timer is stopped here**/
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // imageViewPlay.setImageResource(R.drawable.ic_play);
//                isPlaying = false;
                chronometer.stop();

            }
        });

        /** moving the track as per the seekBar's position**/
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    //here the track's progress is being changed as per the progress bar
                    mediaPlayer.seekTo(progress);
                    //timer is being updated as per the progress of the seekbar
                    chronometer.setBase(SystemClock.elapsedRealtime() -mediaPlayer.getCurrentPosition());
                    lastProgress = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            seekUpdation();
        }
    };

    private void seekUpdation() {
        if (mediaPlayer!= null) {
            int mCurrentPosition = mediaPlayer.getCurrentPosition();
            seekBar.setProgress(mCurrentPosition);
            lastProgress = mCurrentPosition;
        }
        mHandler.postDelayed(runnable, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                // Great! User has recorded and saved the audio file
                result = data.getStringExtra("result");
//                Toast.makeText(AddAnnouncement.this, "Saved: " + result, Toast.LENGTH_LONG).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Oops! User has canceled the recording / back button
            }

        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }    @Override
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
                        Toast.makeText(NewAnnouncementActivity.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(NewAnnouncementActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(NewAnnouncementActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }
    public void sendRecorde(final String description, final String type)
    {
        if (mediaRecorder!=null)mediaRecorder.release();
        mediaRecorder=null;
        String url="http://mymasjid.space/mobileApp/admin/make_announcement_masjid";
        String tag="personal";
        final String masjid_id= PreferenceManager.getMasjid();
        constants.kpHUD.show();
      if(FILE_NAME.equals("")) {
          VolleyMultipartRequest req = new VolleyMultipartRequest(com.android.volley.Request.Method.POST,
                  url, new com.android.volley.Response.Listener<NetworkResponse>() {
              @Override
              public void onResponse(NetworkResponse response) {
                  constants.kpHUD.dismiss();
                  String responseStr = new String(response.data);
                  try {
                      JSONObject responseobject = new JSONObject(responseStr);
                      if (responseobject.getString("error").equals("false")) {
                          String message = responseobject.getString("result");
                          //   btn_save.setEnabled(true);
                          Toast.makeText(NewAnnouncementActivity.this, message, Toast.LENGTH_SHORT).show();
                          Intent intent = new Intent(NewAnnouncementActivity.this, MasjidAnnouncementActivity.class);
                          startActivity(intent);

                          try {
                              finalize();
                          } catch (Throwable throwable) {
                              throwable.printStackTrace();
                          }
                      }
                  } catch (JSONException ignored) {
                  }
              }
          }, new com.android.volley.Response.ErrorListener() {

              @Override
              public void onErrorResponse(VolleyError error) {
                  constants.kpHUD.dismiss();
                  btn_save.setEnabled(true);
                  Log.d("Error.Response", "that is not");
              }
          }) {

              @Override
              protected Map<String, String> getParams() {
                  // Posting params to register url
                  HashMap<String, String> params = new HashMap<String, String>();
                  params.put("detail", description);
                  params.put("announcement_type", type);
                  params.put("status", "1");
                  params.put("masjid", masjid_id);
                  return params;
              }

//              @Override
//              protected Map<String, DataPart> getByteData() {
//                  Map<String, DataPart> params = new HashMap<>();
//                  // file name could found file base or direct access from real path
//                  // for now just get bitmap data from ImageView
//                  params.put("audio", new DataPart("audio.wav", bytes, "audio/x-wav"));
//                  return params;
//              }

          };
          // add the request object to the queue to be executed
          PreferenceManager.getInstance().addToRequestQueue(req, tag);
      }
      else {
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
          VolleyMultipartRequest req = new VolleyMultipartRequest(com.android.volley.Request.Method.POST,
                  url, new com.android.volley.Response.Listener<NetworkResponse>() {
              @Override
              public void onResponse(NetworkResponse response) {
                  constants.kpHUD.dismiss();
                  String responseStr = new String(response.data);
                  try {
                      JSONObject responseobject = new JSONObject(responseStr);
                      if (responseobject.getString("error").equals("false")) {
                          String message = responseobject.getString("result");
                          //   btn_save.setEnabled(true);
                          Toast.makeText(NewAnnouncementActivity.this, message, Toast.LENGTH_SHORT).show();
                          Intent intent = new Intent(NewAnnouncementActivity.this, MasjidAnnouncementActivity.class);
                          startActivity(intent);

                          try {
                              finalize();
                          } catch (Throwable throwable) {
                              throwable.printStackTrace();
                          }
                      }
                  } catch (JSONException ignored) {
                  }
              }
          }, new com.android.volley.Response.ErrorListener() {

              @Override
              public void onErrorResponse(VolleyError error) {
                  constants.kpHUD.dismiss();
                  btn_save.setEnabled(true);
                  Log.d("Error.Response", "that is not");
              }
          }) {

              @Override
              protected Map<String, String> getParams() {
                  // Posting params to register url
                  HashMap<String, String> params = new HashMap<String, String>();
                  params.put("detail", description);
                  params.put("announcement_type", type);
                  params.put("status", "1");
                  params.put("masjid", masjid_id);
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
          PreferenceManager.getInstance().addToRequestQueue(req, tag);
      }
    }
}
