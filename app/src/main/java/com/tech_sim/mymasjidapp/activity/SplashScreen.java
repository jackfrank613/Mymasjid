package com.tech_sim.mymasjidapp.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.model.SalahTimeModel;
import com.tech_sim.mymasjidapp.model.SongModel;
import com.tech_sim.mymasjidapp.model.TimeModel;
import com.tech_sim.mymasjidapp.utils.Constants;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;
import com.tech_sim.mymasjidapp.utils.T;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashScreen extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    private String refreshToken = "";
    private Constants constants;
    private LinearLayout background;
    int duration = 3000;
    String refreshtoken;

    private ImageView imageView;
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
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_splash);
        constants = new Constants(SplashScreen.this);
        refreshToken = FirebaseInstanceId.getInstance().getToken();
        background = (LinearLayout) findViewById(R.id.splash);
        imageView=(ImageView)findViewById(R.id.splash_image);
        getAdvertise();
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(duration);
        background.startAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                final String password = PreferenceManager.getPassword();
                final String username = PreferenceManager.getEmail();
                if (PreferenceManager.getCheck()== 1) {
                    Intent intent1 = new Intent(SplashScreen.this, Masjid.class);
                    startActivity(intent1);
                    finish();
                //    constants.kpHUD.show();
//                    StringRequest stringRequest = new StringRequest(
//                            Request.Method.POST,
//                            Constants.URL_LOGIN_MASJID,
//                            new Response.Listener<String>() {
//                                @Override
//                                public void onResponse(String response) {
//                                    constants.kpHUD.dismiss();
//                                    try {
//                                        JSONObject obj = new JSONObject(response);
//                                        if (obj.getString("error").equals("false")) {
//                                            Toast.makeText(getApplicationContext(), obj.getString("result"), Toast.LENGTH_SHORT).show();
//                                            PreferenceManager.setMasjid(obj.getString("mosque_id"));
//
//
//                                        } else {
//                                            constants.kpHUD.dismiss();
//                                            Toast.makeText(getApplicationContext(),
//                                                    obj.getString("result"), Toast.LENGTH_SHORT).show();
//                                        }
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//
//                                }
//                            }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            constants.kpHUD.dismiss();
//                            Toast.makeText(getApplicationContext(),
//                                    error.getMessage(), Toast.LENGTH_LONG).show();
//                        }
//                    }) {
//                        @Override
//                        protected Map<String, String> getParams() throws AuthFailureError {
//                            Map<String, String> params = new HashMap<>();
//                            params.put("email", username);
//                            params.put("password", password);
//                            params.put("token", refreshToken);
//                            return params;
//                        }
//                    };
//
//                    PreferenceManager.getInstance().addToRequestQueue(stringRequest);
                }
                else if(PreferenceManager.getCheck() == 2)
                {
                    String masjidid=PreferenceManager.getMasjid();
                    String userid=PreferenceManager.getUserid();
                    getSalahTime(masjidid,userid,username);
//                    constants.kpHUD.show();
//                    StringRequest stringRequest = new StringRequest(
//                            Request.Method.POST,
//                            Constants.URL_LOGIN_USER,
//                            new Response.Listener<String>() {
//                                @Override
//                                public void onResponse(String response) {
//                                    constants.kpHUD.dismiss();
//                                    try {
//                                        JSONObject obj = new JSONObject(response);
//                                        if (obj.getString("error").equals("false")) {
//                                            PreferenceManager.setMasjid(obj.getString("masjid"));
//                                            String masjidid=obj.getString("masjid");
//                                            String userid=obj.getString("user_id");
//                                            PreferenceManager.setUserid(userid);
//
//                                        } else {
//                                            constants.kpHUD.dismiss();
//                                            Toast.makeText(getApplicationContext(),
//                                                    obj.getString("result"), Toast.LENGTH_SHORT).show();
//                                        }
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//
//                                }
//                            }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            constants.kpHUD.dismiss();
//                            Toast.makeText(getApplicationContext(),
//                                    error.getMessage(), Toast.LENGTH_LONG).show();
//                        }
//                    }) {
//                        @Override
//                        protected Map<String, String> getParams() throws AuthFailureError {
//                            Map<String, String> params = new HashMap<>();
//                            params.put("email", username);
//                            params.put("password", password);
//                            params.put("token", refreshToken);
//                            return params;
//                        }
//                    };
//
//                    PreferenceManager.getInstance().addToRequestQueue(stringRequest);

                }
                else if(PreferenceManager.getCheck()==3)
                {

                   goVisitActivity();

                }
                else {
                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }



            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    public void goVisitActivity(){
        String url="http://mymasjid.space/mobileApp/admin/getSalahMasjid";
        final String masjid=PreferenceManager.getMasjid();
        constants.kpHUD.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        constants.kpHUD.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getString("error").equals("false")) {
                                JSONArray array=jsonObject.getJSONArray("data");
                                for(int i=0;i<array.length();i++)
                                {
                                    JSONObject object=array.getJSONObject(i);
                                    String startTime=object.getString("start_time");
                                    String jmahTime=object.getString("jamaah_time");
                                    String endTime=object.getString("end_time");
                                    String slahName=object.getString("salah_name");
                                    TimeModel timeModel=new TimeModel(startTime,slahName);
                                    PreferenceManager.timeModels.add(timeModel);
                                    SalahTimeModel timeModel1=new SalahTimeModel(i,slahName,startTime,jmahTime,endTime);
                                    PreferenceManager.salahTimeModels.add(timeModel1);
                                }

                            }
                            else {
                                Log.i("error","not refund");
                            }
                            PreferenceManager.setCheck(3);
                            Intent intent1 = new Intent(SplashScreen.this, VisitorActivity.class);
                            startActivity(intent1);

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
                params.put("masjid",masjid);
                return params;
            }
        };
        PreferenceManager.getInstance().addToRequestQueue(stringRequest);
    }

    public void getSalahTime(final String masjid,final String userid,final String email){

        String url="http://mymasjid.space/mobileApp/admin/getSalahMasjid";
//        final String masjid=PreferenceManager.getMasjid();
//        constants.kpHUD.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    //    constants.kpHUD.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean expire=jsonObject.getBoolean("expire");
                            String user_type=jsonObject.getString("user_type");
                            if(jsonObject.getString("error").equals("false")) {
                                JSONArray array=jsonObject.getJSONArray("data");
                                for(int i=0;i<array.length();i++)
                                {
                                    JSONObject object=array.getJSONObject(i);
                                    String startTime=object.getString("start_time");
                                    String jmahTime=object.getString("jamaah_time");
                                    String endTime=object.getString("end_time");
                                    String slahName=object.getString("salah_name");
                                    TimeModel timeModel=new TimeModel(startTime,slahName);
                                    PreferenceManager.timeModels.add(timeModel);
                                    SalahTimeModel timeModel1=new SalahTimeModel(i,slahName,startTime,jmahTime,endTime);
                                    PreferenceManager.salahTimeModels.add(timeModel1);
                                }
                            }
                            else {
                               Log.i("error","not refund");
                            }
                            if(expire)
                            {
                                if(user_type.equals("Regular"))
                                {
                                    Toast.makeText(SplashScreen.this,"The expiration date has passed. You have to pay for app",Toast.LENGTH_SHORT).show();
                                    Intent intent2=new Intent(SplashScreen.this,RegularUserRepayActivity.class);
                                    intent2.putExtra("email",email);
                                    startActivity(intent2);
                                    finish();
                                }
                                else {
                                    Toast.makeText(SplashScreen.this,"You have to pay for app!", Toast.LENGTH_SHORT).show();
                                    Intent intent2=new Intent(SplashScreen.this,RegularUserRepayActivity.class);
                                    intent2.putExtra("email",email);
                                    startActivity(intent2);
                                    finish();
                                }
                            }
                            else {
                                //go to dashboard
                                Intent intent = new Intent(SplashScreen.this, UserDashboard.class);
                                startActivity(intent);
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              //  constants.kpHUD.dismiss();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("masjid",masjid);
                params.put("user_id",userid);
                return params;
            }
        };
        PreferenceManager.getInstance().addToRequestQueue(stringRequest);
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
                                String splash_image=object.getString("splash");
                                Glide.with(SplashScreen.this).load(splash_image).into(imageView);
                                getSong();
                            }
                            else {
                                Toast.makeText(SplashScreen.this,response.getString("result"),Toast.LENGTH_SHORT).show();
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
    public void getSong(){
        String url="http://mymasjid.space/mobileApp/admin/quran_athan";
        constants.kpHUD.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        constants.kpHUD.dismiss();
                        try {
                            JSONObject object1=new JSONObject(response);
                            if (object1.getString("error").equals("false"))
                            {
                                JSONObject object=object1.getJSONObject("result");
                                JSONObject jsonObject=object.getJSONObject("quran");
                                String image=jsonObject.getString("quran_image");
                                JSONObject object2=object.getJSONObject("athan");
                                String s_song=object2.getString("audio1_url");
                                String m_song=object2.getString("audio2_url");
                                String l_song=object2.getString("audio3_url");
                                SongModel songModel=new SongModel(s_song,m_song,l_song);
                                PreferenceManager.songModels.add(songModel);
                                PreferenceManager.setSong(s_song);

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
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                return params;
//            }
        };
        PreferenceManager.getInstance().addToRequestQueue(stringRequest);

    }
}
