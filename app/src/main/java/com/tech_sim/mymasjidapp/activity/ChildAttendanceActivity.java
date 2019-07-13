package com.tech_sim.mymasjidapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.adapter.MarkChildAdatper;
import com.tech_sim.mymasjidapp.model.MarkChildModel;
import com.tech_sim.mymasjidapp.utils.Constants;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChildAttendanceActivity extends AppCompatActivity {

    Button btnAttendanceCancel, btnAttendanceSubmit;
    ArrayList<MarkChildModel> AttendanceList;
    RecyclerView recyclerView;
    ArrayList<String> array;
    private Constants constants;

    private String value="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mark_attendance_layout);  constants=new Constants(this);
        recyclerView = findViewById(R.id.childAttendanceList);
        getData();
        btnAttendanceCancel = findViewById(R.id.btncanclAttendance);

        btnAttendanceSubmit = findViewById(R.id.btnSubmitAttendance);

        btnAttendanceCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnAttendanceSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(value!=null)
                {
                    sendRequest();
                }
                else {
                    Toast.makeText(ChildAttendanceActivity.this,"Please waiting child infromation",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    void getData() {
        String url="http://mymasjid.space/mobileApp/admin/getChildrenMasjid_for_Mark";
        final String masjid_id= PreferenceManager.getMasjid();
        constants.kpHUD.show();
        RequestQueue r = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        constants.kpHUD.dismiss();
                        AttendanceList = new ArrayList<MarkChildModel>();
                        try {//
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray reader = jsonObject.getJSONArray("data");
                            for (int i = 0; i < reader.length(); i++) {
                                JSONObject obj = reader.getJSONObject(i);
                                String ChildName = obj.getString("child_name");
                                String ChildID = obj.getString("id");
                                MarkChildModel markChildModel=new MarkChildModel(ChildID,ChildName);
                                AttendanceList.add(markChildModel);
                            }
                            MarkChildAdatper adatper=new MarkChildAdatper(ChildAttendanceActivity.this,AttendanceList);
                            recyclerView.setLayoutManager(new LinearLayoutManager(ChildAttendanceActivity.this));
                            recyclerView.setAdapter(adatper);
                            adatper.setAttendace(new MarkChildAdatper.Attendace() {
                                @Override
                                public void checkAttendance(String child_id) {
                                    value=child_id;
                                }
                            });

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

    private void sendRequest()
    {

        String url="http://mymasjid.space/mobileApp/admin/addChildrenAttendanceMasjid";
        final String masjid_id= PreferenceManager.getMasjid();
        constants.kpHUD.show();
        RequestQueue r = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        constants.kpHUD.dismiss();
                        AttendanceList = new ArrayList<MarkChildModel>();
                        try {//
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getString("error").equals("false"))
                            {
                                Toast.makeText(ChildAttendanceActivity.this,jsonObject.getString("result"),Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(ChildAttendanceActivity.this,Masjid.class);
                                startActivity(intent);
                                finish();

                            }
                            else {
                                Toast.makeText(ChildAttendanceActivity.this,jsonObject.getString("result"),Toast.LENGTH_SHORT).show();
                            }

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
                params.put("children",value);
                params.put("status","1");
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
