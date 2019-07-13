package com.tech_sim.mymasjidapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.utils.Constants;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActivityGolive extends AppCompatActivity {
    Button btnCancellink,btnSendlink;
    EditText addGoLiveURL;
    Constants constants;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_go_live);
        constants=new Constants(this);
        addGoLiveURL=findViewById(R.id.GoLiveURL);
        btnCancellink=findViewById(R.id.btncancelLink);
        btnSendlink=findViewById(R.id.btnsendLink);
        btnSendlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link=addGoLiveURL.getText().toString().trim();
                if(!link.equals(""))
                {
                    sendLink(link);
                }
                else {
                    Toast.makeText(ActivityGolive.this,"Please,enter the correct Youtube link",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnCancellink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    public void sendLink(final String link)
    {
        String url="http://mymasjid.space/mobileApp/admin/make_announcement_masjid";
        String tag="personal";
        final String masjid_id= PreferenceManager.getMasjid();
        constants.kpHUD.show();
        RequestQueue r = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
//                            Log.e("Response AD: ", response);
                            JSONObject obj = new JSONObject(response);
                            if(obj.getString("error").equals("false"))
                            {
                                Toast.makeText(ActivityGolive.this,obj.getString("result"),Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(ActivityGolive.this,Masjid.class);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Toast.makeText(ActivityGolive.this,obj.getString("result"),Toast.LENGTH_SHORT).show();

                            }

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(),"No Answer Given",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("detail", link);
                params.put("status", "1");
                params.put("masjid", masjid_id);
                params.put("announcement_type","Youtube");
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
