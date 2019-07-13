package com.tech_sim.mymasjidapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.model.ViewComitteeModel;
import com.tech_sim.mymasjidapp.utils.Constants;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewMasjidCommit extends AppCompatActivity {
    Button btnViewCommiteCancel;
    RecyclerView masjidComiteList;
    ArrayList<ViewComitteeModel> CommiteeList;
    private Constants constants;
    private TextView txt_pres,text_vice,txt_trea,txt_secrete,txt_legal;
    private TextView p_phone,v_phone,t_phone,s_phone,l_phone;
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
        setContentView(R.layout.activity_view_masjid_commit);
        constants=new Constants(this);
        initxml();
        getData();
        btnViewCommiteCancel=findViewById(R.id.ViewCommettiCancel);
        btnViewCommiteCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
//                startActivity(new Intent(ViewMasjidComitte.this,ManageCommittee.class));
            }
        });


    }
    public void initxml(){
        txt_pres=findViewById(R.id.p_txt);
        txt_trea=findViewById(R.id.t_txt);
        txt_legal=findViewById(R.id.l_txt);
        txt_secrete=findViewById(R.id.s_txt);
        text_vice=findViewById(R.id.v_txt);
        p_phone=findViewById(R.id.p_phone);
        v_phone=findViewById(R.id.v_phone);
        s_phone=findViewById(R.id.s_phone);
        l_phone=findViewById(R.id.l_phone);
        t_phone=findViewById(R.id.t_phone);
    }

    void getData() {
        String url="http://mymasjid.space/mobileApp/admin/getMasjidCommittee";
        final String masjid_id= PreferenceManager.getMasjid();
        constants.kpHUD.show();
        RequestQueue r = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        constants.kpHUD.dismiss();
                        CommiteeList = new ArrayList<>();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray reader = jsonObject.getJSONArray("data");
                            Log.e("Response ", response);
                            for (int i = 0; i < reader.length(); i++) {
                                JSONObject obj = reader.getJSONObject(i);
                                String PostComite = obj.getString("type");
                                String PostHolderCommite = obj.getString("name");
                                String PostHolderNumber = obj.getString("contact_no");
                                String CommiteeMasjid = obj.getString("masjid");
                                if(PostComite.equals("President"))
                                {
                                    txt_pres.setText(PostHolderCommite);
                                    p_phone.setText(PostHolderNumber);
                                }
                                else if(PostComite.equals("Vice President"))
                                {
                                    text_vice.setText(PostHolderCommite);
                                    v_phone.setText(PostHolderNumber);
                                }
                                else if(PostComite.equals("Treasurer"))
                                {
                                    txt_trea.setText(PostHolderCommite);
                                    t_phone.setText(PostHolderNumber);
                                }
                                else if(PostComite.equals("Secretary"))
                                {
                                    txt_secrete.setText(PostHolderCommite);
                                    s_phone.setText(PostHolderNumber);
                                }
                                else {
                                    txt_legal.setText(PostHolderCommite);
                                    l_phone.setText(PostHolderNumber);
                                }
                            }


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
                params.put("masjid", masjid_id);
                return params;
            }
        };
        r.add(stringRequest);
    }
}
