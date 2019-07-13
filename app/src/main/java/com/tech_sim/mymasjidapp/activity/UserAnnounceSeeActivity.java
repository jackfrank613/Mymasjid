package com.tech_sim.mymasjidapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.adapter.AnnounceAudioAdapter;
import com.tech_sim.mymasjidapp.adapter.UserAnnounce;
import com.tech_sim.mymasjidapp.model.ViewAudioModel;
import com.tech_sim.mymasjidapp.utils.Constants;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserAnnounceSeeActivity extends AppCompatActivity{
    Button btncancelll,btn_back;
    private ArrayList<ViewAudioModel> userAnnounceModels;
    public RecyclerView recyclerView;
    private Constants constants;
    String typer;

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getData();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);
        constants=new Constants(this);
        Intent intent=getIntent();
        typer=intent.getStringExtra("type");
        btncancelll = findViewById(R.id.btncancdeeel);
        recyclerView=findViewById(R.id.announcement);
        getData();
        btncancelll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("verification",true);
                setResult(RESULT_OK,resultIntent);
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
                            UserAnnounce adapter=new UserAnnounce(UserAnnounceSeeActivity.this,userAnnounceModels);
                            recyclerView.setLayoutManager(new LinearLayoutManager(UserAnnounceSeeActivity.this));
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
                params.put("user_id",user_id);
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

