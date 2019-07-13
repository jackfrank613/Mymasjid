package com.tech_sim.mymasjidapp.activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.adapter.QuestionAudioAdapter;
import com.tech_sim.mymasjidapp.model.UserQuestionModel;
import com.tech_sim.mymasjidapp.utils.Constants;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ManageAnsQuestionPanel extends AppCompatActivity {

//    CustomAdapterQuestion customAdapterQuestion;
//    ArrayList<AnswerItem> listAnswer;
    RecyclerView listView;
    Button btncancelQuestionlist;
    private Constants constants;
    private ArrayList<UserQuestionModel> arrayList;
    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.answer_question_layout);
        constants = new Constants(this);
        listView = findViewById(R.id.Questionlist);
        btncancelQuestionlist = findViewById(R.id.btncancelQuestionlist);
        getData();
        btncancelQuestionlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent=new Intent();
                resultIntent.putExtra("verification",true);
                setResult(RESULT_OK,resultIntent);
                finish();

            }
        });


    }

    public void getData() {
        final String masjid_id = PreferenceManager.getMasjid();

        constants.kpHUD.show();
        RequestQueue r = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_USER_Quetion,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        constants.kpHUD.dismiss();
                        arrayList = new ArrayList<>();
                        try {
                            //  Log.e("Response : ", response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray reader = jsonObject.getJSONArray("data");

                            //Log.e("Response ", response);

                            for (int i = 0; i < reader.length(); i++) {
                                JSONObject obj = reader.getJSONObject(i);
                                //  Log.e("Response ", String.valueOf(obj));
                                String f_name = obj.getString("firstname");
                                String l_name = obj.getString("lastname");
                                String name = f_name + l_name;
                                String question = obj.getString("question");
                                String date = obj.getString("date");
                                String audio = obj.getString("audio");
                                String user_id = obj.getString("user_id");
                                String id=obj.getString("id");
                                String status=obj.getString("status");
                                UserQuestionModel userQuestionModel = new UserQuestionModel(name, question, audio, date,id,status,user_id);
                                arrayList.add(userQuestionModel);
                            }
                            QuestionAudioAdapter adapter = new QuestionAudioAdapter(ManageAnsQuestionPanel.this, arrayList);
                            listView.setLayoutManager(new LinearLayoutManager(ManageAnsQuestionPanel.this));
                            listView.setAdapter(adapter);

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
}