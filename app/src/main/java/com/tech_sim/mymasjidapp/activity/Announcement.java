package com.tech_sim.mymasjidapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.adapter.AnnounceAudioAdapter;
import com.tech_sim.mymasjidapp.model.ViewAudioModel;
import com.tech_sim.mymasjidapp.utils.Constants;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Announcement extends AppCompatActivity {

    Button btncancelll,btn_back;
    private ArrayList<ViewAudioModel> userAnnounceModels;
    public RecyclerView recyclerView;
    private Constants constants;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);
        constants=new Constants(this);
        btncancelll = findViewById(R.id.btncancdeeel);
        recyclerView=findViewById(R.id.announcement);
        getData();
        btncancelll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(Announcement.this,UserDashboard.class));
               finish();
            }
        });

    }
    public void getData() {
        final  String masjid_id= PreferenceManager.getMasjid();
        final String user_id=PreferenceManager.getUserid();
        constants.kpHUD.show();
        RequestQueue r = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_Salah_audio,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        constants.kpHUD.dismiss();
                        userAnnounceModels=new ArrayList<>();
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
                                String is_check=obj.getString("is_see");
                                ViewAudioModel viewAudioModel=new ViewAudioModel(AnnouncmeetID,AnnouncementType,AnnouncementDescription,AnnouncementAudio,AnnouncementDate,is_check);
                                viewAudioModel.setCheck(false);
                                userAnnounceModels.add(viewAudioModel);
                            }
                            AnnounceAudioAdapter adapter=new AnnounceAudioAdapter(Announcement.this,userAnnounceModels);
                            recyclerView.setLayoutManager(new LinearLayoutManager(Announcement.this));
                            recyclerView.setAdapter(adapter);

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
