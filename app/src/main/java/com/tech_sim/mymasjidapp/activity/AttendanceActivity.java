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
import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.adapter.AttendanceDetailAdapter;
import com.tech_sim.mymasjidapp.model.AttendanceDetailModel;
import com.tech_sim.mymasjidapp.utils.Constants;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AttendanceActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    Constants constants;
    ArrayList<AttendanceDetailModel> attendanceDetailModels;
    String child = "";
    private Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_detail_view);
        constants = new Constants(this);
        Intent intent = getIntent();
        child = intent.getStringExtra("child");
        recyclerView = findViewById(R.id.childAttendanceList);
        callApi(child);
        button = (Button) findViewById(R.id.btncanclAttendance);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    public void callApi(final String childid) {

        String url = "http://mymasjid.space/mobileApp/admin/getChildAttendanceHistory";
        final String masjid = PreferenceManager.getMasjid();
        constants.kpHUD.show();
        RequestQueue r = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        constants.kpHUD.dismiss();
                        attendanceDetailModels = new ArrayList<>();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("data");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                String date = object.getString("date");
                                String status = object.getString("status");
                                AttendanceDetailModel detailModel = new AttendanceDetailModel(date, status);
                                attendanceDetailModels.add(detailModel);
                            }
                            AttendanceDetailAdapter attendanceDetailAdapter = new AttendanceDetailAdapter(AttendanceActivity.this, attendanceDetailModels);
                            recyclerView.setLayoutManager(new LinearLayoutManager(AttendanceActivity.this));
                            recyclerView.setAdapter(attendanceDetailAdapter);


                        } catch (Exception e) {
                            e.printStackTrace();
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
                params.put("masjid", masjid);
                params.put("children", childid);
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
