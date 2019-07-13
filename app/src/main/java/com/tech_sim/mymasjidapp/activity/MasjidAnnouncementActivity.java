package com.tech_sim.mymasjidapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.model.ViewAudioModel;
import com.tech_sim.mymasjidapp.utils.Constants;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MasjidAnnouncementActivity  extends AppCompatActivity {

    Button btnAddAnnouncement,btnViewAnnouncement,btn_cancel;
    AdView adView;
    private Constants constants;

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.masjid_announcement_layout);
        constants=new Constants(this);

        btnAddAnnouncement=findViewById(R.id.AddAnnouncement);
        btnViewAnnouncement=findViewById(R.id.ViewAnnouncement);
        btn_cancel=findViewById(R.id.cancel);
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        adView=findViewById(R.id.testAd);
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        btnAddAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(MasjidAnnouncementActivity.this,NewAnnouncementActivity.class);
                startActivity(intent);

            }
        });

        btnViewAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();

            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent =new Intent(MasjidAnnouncementActivity.this,Masjid.class);
               startActivity(intent);
               finish();
            }
        });
    }
    void getData() {
        final String masjid_id= PreferenceManager.getMasjid();
        constants.kpHUD.show();
        RequestQueue r = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_VIEW_ANOUNCEMNT_MASJID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        constants.kpHUD.dismiss();
                        try {
                            //  Log.e("Response : ", response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray reader = jsonObject.getJSONArray("data");

                            //Log.e("Response ", response);

                            for (int i = 0; i < reader.length(); i++) {
                                JSONObject obj = reader.getJSONObject(i);

                                //  Log.e("Response ", String.valueOf(obj));
                                String AnnouncementType = obj.getString("announcement_type");
                                String AnnouncementDate = obj.getString("date");
                                String AnnouncementDescription = obj.getString("detail");
                                String AnnouncementMasjid = obj.getString("masjid");
                                String AnnouncmeetID = obj.getString("id");
                                String AnnouncementAudio=obj.getString("audio");
                                String is_check=obj.getString("status");
                                ViewAudioModel viewAudioModel=new ViewAudioModel(AnnouncmeetID,AnnouncementType,AnnouncementDescription,AnnouncementAudio,AnnouncementDate,is_check);
                                PreferenceManager.viewAudioModels.add(viewAudioModel);
                            }
                            Intent intent=new Intent(MasjidAnnouncementActivity.this,MasjidAnnouncement.class);
                            startActivity(intent);

                        } catch (Exception e) {
                            // dialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                constants.kpHUD.dismiss();
                //dialog.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("masjid", masjid_id);
                return params;
            }
        };
        r.add(stringRequest);
    }
    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }
}
