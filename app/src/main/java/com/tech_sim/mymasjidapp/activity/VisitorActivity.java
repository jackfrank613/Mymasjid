package com.tech_sim.mymasjidapp.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdView;
import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.model.SongModel;
import com.tech_sim.mymasjidapp.utils.Constants;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class VisitorActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView ad_image;
    CardView card_salah,card_charity;
    CardView logout;
    AdView adView;
    Constants constants;
    TextView txt_city,txt_time,txt_name;
    private SimpleDateFormat dateFormat;
    Thread myThread = null;
    MediaPlayer player;

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
        setContentView(R.layout.activity_visitor_screen);
        initXml();
        constants=new Constants(this);
        Runnable runnable = new CountDownRunner();
        myThread = new Thread(runnable);
        myThread.start();
        txt_city.setText(PreferenceManager.getCity());
        getAdvertise();
        getSong();


    }
    public void initXml(){
        ad_image=findViewById(R.id.advertiser);
        card_salah=findViewById(R.id.Salah);
        card_charity=findViewById(R.id.charity);
        adView=findViewById(R.id.testAd);
        logout=findViewById(R.id.btnlogout);
        txt_city=findViewById(R.id.nameprayer);
        txt_time=findViewById(R.id.namaztime);
        txt_name=findViewById(R.id.salahname);
        card_salah.setOnClickListener(this);
        card_charity.setOnClickListener(this);
        logout.setOnClickListener(this);
    }
    public void getAdvertise(){
        String url="http://mymasjid.space/mobileApp/admin/banner";
        //  constants.kpHUD.show();
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST,url,new JSONObject(),
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        //  constants.kpHUD.dismiss();
                        try {
                            if (response.getString("error").equals("false"))
                            {
                                JSONObject object=response.getJSONObject("result");
                                String link=object.getString("ad_link");
                                String image=object.getString("ad_image");
                                Glide.with(VisitorActivity.this).load(image).into(ad_image);
                                moveTowebView(link);
                            }
                            else {
                                Toast.makeText(VisitorActivity.this,response.getString("result"),Toast.LENGTH_SHORT).show();
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  constants.kpHUD.dismiss();
                        Log.d("Error.Response","that is not");
                    }
                }
        );
        PreferenceManager.getInstance().addToRequestQueue(getRequest);

    }
    public void moveTowebView(final String link){
        ad_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(Registration.this,"move to webview",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(link));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.Salah:
                startActivity(new Intent(VisitorActivity.this, SalahActivity.class));
                break;
            case R.id.charity:
                startActivity(new Intent(VisitorActivity.this, VistorCharity.class));

                break;
            case R.id.btnlogout:
                logFunction();
                break;
        }

    }
    public void logFunction(){
        Constants.visitor_masjidname="";
        PreferenceManager.timeModels=new ArrayList<>();
        PreferenceManager.songModels=new ArrayList<>();
        PreferenceManager.salahTimeModels=new ArrayList<>();
        PreferenceManager.setCheck(-1);
        startActivity(new Intent(VisitorActivity.this,RegistrationMainScreen.class));
        finish();
    }

    class CountDownRunner implements Runnable {
        // @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    doWork();
                    Thread.sleep(1000*30);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                }
            }
        }
    }
    public void doWork() {
        runOnUiThread(new Runnable() {
            public void run() {
                Calendar c = Calendar.getInstance();
                int hrs = c.get(Calendar.HOUR_OF_DAY);//24
                int min = c.get(Calendar.MINUTE);//59
                String current_time=String.valueOf(hrs)+":"+String.valueOf(min);
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                try {
                    Date d1 = sdf.parse(current_time);
                    Log.i("time",String.valueOf(d1));
                    for(int i=0;i<PreferenceManager.timeModels.size();i++)
                    {
                        Date d2=sdf.parse(PreferenceManager.timeModels.get(i).getStart_time());
                        if(d2.after(d1))
                        {
                            Log.i("TAG","after");
                            txt_time.setText(PreferenceManager.timeModels.get(i).getStart_time());
                            txt_name.setText(PreferenceManager.timeModels.get(i).getSalah_name());
                        }
                        if(d2.before(d1))
                        {
                            txt_time.setText(PreferenceManager.timeModels.get(i).getStart_time());
                            txt_name.setText(PreferenceManager.timeModels.get(i).getSalah_name());
                            Log.i("TAG","before");
                        }
                        if(d2.equals(d1))
                        {
                            Log.i("TAG","same");
                            voiceSong();
                        }

                    }


                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    public void voiceSong()
    {
        player=new MediaPlayer();
        try{
            player.setDataSource(PreferenceManager.songModels.get(0).getS_song());
            player.prepare();
            player.start();

        }catch (Exception e)
        {
            e.printStackTrace();

        }
    }
    public void getSong(){
        String url="http://mymasjid.space/mobileApp/admin/quran_athan";
        //  constants.kpHUD.show();
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST,url,new JSONObject(),
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        //     constants.kpHUD.dismiss();
                        try {
                            if (response.getString("error").equals("false"))
                            {
                                JSONObject jsonobject=response.getJSONObject("result");
                                JSONObject object=jsonobject.getJSONObject("athan");
                                String s_song=object.getString("audio1_url");
                                String m_song=object.getString("audio2_url");
                                String l_song=object.getString("audio3_url");
                                SongModel songModel=new SongModel(s_song,m_song,l_song);
                                PreferenceManager.songModels.add(songModel);
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //   constants.kpHUD.dismiss();
                        Log.d("Error.Response","that is not");
                    }
                }
        );
        PreferenceManager.getInstance().addToRequestQueue(getRequest);
    }
}
