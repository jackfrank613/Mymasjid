package com.tech_sim.mymasjidapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.tech_sim.mymasjidapp.adapter.AnswerAdapter;
import com.tech_sim.mymasjidapp.model.MasjidAnswerModel;
import com.tech_sim.mymasjidapp.utils.Constants;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MasjidAnswerActivity extends AppCompatActivity{

        ArrayList<MasjidAnswerModel> listAnswer;
        RecyclerView listViewAns;
        Button btnAnnouncementCancel;
        String question="";
        String masjid_name="";
        String date="";
        String an_audio="";
        String an_answer="";
        String an_date="";
        String audio="";
        String status="";
        String state_id="";
        private Constants constants;
        AdView adView;

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getData();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.majid_answer_activity);
        constants=new Constants(this);
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        adView=findViewById(R.id.testAd);
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        listViewAns = findViewById(R.id.answerlist);
        btnAnnouncementCancel=findViewById(R.id.btncancelAns);
        getData();
        btnAnnouncementCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("verification",true);
                setResult(RESULT_OK,resultIntent);
                finish();
            }
        });

    }
    void getData() {
        final String user_id= PreferenceManager.getUserid();
        String url="http://mymasjid.space/mobileApp/admin/get_questions_user";
        constants.kpHUD.show();
        RequestQueue r = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        constants.kpHUD.dismiss();
                        listAnswer = new ArrayList<>();
                        try {
//                            Log.e("Response : ", response)
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray reader = jsonObject.getJSONArray("data");
                            for (int i = 0; i < reader.length(); i++) {
                                JSONObject obj = reader.getJSONObject(i);
                                String answer=obj.getString("answer");
                                if(answer.equals(""))
                                {
                                    an_answer="";
                                    an_date="";
                                    an_audio="";
                                    question=obj.getString("question");
                                    audio=obj.getString("audio");
                                    date=obj.getString("date");
                                    status=obj.getString("status");
                                    state_id=obj.getString("id");
                                    masjid_name=obj.getString("masjid_name");

                                }
                                else {
                                    JSONObject object=new JSONObject(answer);
                                    an_answer=object.getString("answer");
                                    an_audio=object.getString("audio");
                                    an_date=object.getString("date");
                                    question=obj.getString("question");
                                    state_id=object.getString("id");
                                    status=object.getString("status");
                                    audio=obj.getString("audio");
                                    date=obj.getString("date");
                                    masjid_name=obj.getString("masjid_name");
                                }
                                MasjidAnswerModel masjidAnswerModel=new MasjidAnswerModel(masjid_name,date,an_audio,question,an_answer,an_date,status,state_id);
                                listAnswer.add(masjidAnswerModel);
                            }
                            AnswerAdapter adapter=new AnswerAdapter(MasjidAnswerActivity.this,listAnswer);
                            adapter.notifyItemChanged(listAnswer.size());
                            listViewAns.setLayoutManager(new LinearLayoutManager(MasjidAnswerActivity.this));
//                            listViewAns.smoothScrollToPosition(listAnswer.size()-1);
                            listViewAns.setAdapter(adapter);


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
                params.put("user_id", user_id);
                return params;
            }
        };
        r.add(stringRequest);
    }
//    @Override
//    public void onBackPressed() {
//        Intent setIntent = new Intent(Intent.ACTION_MAIN);
//        setIntent.addCategory(Intent.CATEGORY_HOME);
//        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(setIntent);
//    }
}
