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
import com.tech_sim.mymasjidapp.adapter.ViewChildrenAdapter;
import com.tech_sim.mymasjidapp.model.ChildItem;
import com.tech_sim.mymasjidapp.utils.Constants;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AttendanceHistory extends AppCompatActivity {


    String ChildID,ChildName ;
    //    CustomAdapterAttendaceHistory customAdapterAttendance;
    ArrayList<ChildItem> listAttendanceHistory;
    RecyclerView listViewAtHistory;
    Button btnClose;
    private Constants constants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_history);
        constants=new Constants(this);
        listViewAtHistory = findViewById(R.id.childAttendanceList);
        getData();
        btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
//                startActivity(new Intent(AttendanceHistory.this,Masjid.class));
            }
        });

    }

    void getData() {
      final String Masjid= PreferenceManager.getMasjid();
         String url="http://mymasjid.space/mobileApp/admin/getChildrenMasjid";
         constants.kpHUD.show();
        RequestQueue r = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        constants.kpHUD.dismiss();
                        listAttendanceHistory = new ArrayList<>();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray reader = jsonObject.getJSONArray("data");

                            for (int i = 0; i < reader.length(); i++) {
                                JSONObject obj = reader.getJSONObject(i);
                                ChildName = obj.getString("child_name");
                                String FatherName = obj.getString("father_name");
                                String Age = obj.getString("age");
                                String ContectNo = obj.getString("contact_no");
                                String ClassDays = obj.getString("class_days");
                                String ChildMasjid = obj.getString("masjid");
                                ChildID = obj.getString("id");
                                String pin=obj.getString("childNumber");
                                String DateC = obj.getString("date");
                                ChildItem childItem=new ChildItem(ChildName, FatherName, Age, ContectNo, ClassDays, ChildMasjid, ChildID, DateC,pin);
                                childItem.setCheck(false);
                                listAttendanceHistory.add(childItem);

                            }
                            ViewChildrenAdapter viewChildrenAdapter=new ViewChildrenAdapter(AttendanceHistory.this,listAttendanceHistory);
                            listViewAtHistory.setLayoutManager(new LinearLayoutManager(AttendanceHistory.this));
                            listViewAtHistory.setAdapter(viewChildrenAdapter);

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
                params.put("masjid", Masjid);

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
