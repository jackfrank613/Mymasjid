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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.adapter.SlahTimeAdapter;
import com.tech_sim.mymasjidapp.model.MasjidSalahTimeModel;
import com.tech_sim.mymasjidapp.model.SalahTimeModel;
import com.tech_sim.mymasjidapp.utils.Constants;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewSalahTime extends AppCompatActivity {
    RecyclerView listView;
    ArrayList<MasjidSalahTimeModel> list;
    Button btncancelviewSalahTime,btn_back;
    Constants constants;
    private String url = Constants.URL_VIEW_SALAH_TIME_MASJID;
    int searchId = 1;
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
        setContentView(R.layout.view_salah_time);
        constants=new Constants(this);
        listView = findViewById(R.id.viewSalahTimeList);
        btncancelviewSalahTime = findViewById(R.id.btncancellViewSalahTime);
        getViewSalahTime();
        btncancelviewSalahTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    public void getViewSalahTime()
    {
        String url="http://mymasjid.space/mobileApp/admin/getSalahMasjid";
        final String Masjid= PreferenceManager.getMasjid();
        constants.kpHUD.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        constants.kpHUD.dismiss();
                        list=new ArrayList<>();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("error").equals("false")) {
                                //  Toast.makeText(getApplicationContext(), jsonObject.getString("result"), Toast.LENGTH_SHORT).show();
                                JSONArray array=jsonObject.getJSONArray("data");
                                for(int i=0;i<array.length();i++)
                                {
                                    JSONObject object=array.getJSONObject(i);
                                    String s_name=object.getString("salah_name");
                                    String s_time=object.getString("start_time");
                                    String e_time=object.getString("end_time");
                                    String j_time=object.getString("jamaah_time");
                                    int number=i+1;
                                      MasjidSalahTimeModel model=new MasjidSalahTimeModel(number,s_name,s_time,j_time,e_time);
                                      list.add(model);
                                }
                                SlahTimeAdapter slahTimeAdapter=new SlahTimeAdapter(ViewSalahTime.this,list);
                                listView.setLayoutManager(new LinearLayoutManager(ViewSalahTime.this));
                                listView.setAdapter(slahTimeAdapter);

                            }else {
                                Toast.makeText(getApplicationContext(),
                                        jsonObject.getString("result"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                constants.kpHUD.dismiss();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("masjid",Masjid);
                return params;
            }
        };
        PreferenceManager.getInstance().addToRequestQueue(stringRequest);
    }
}
