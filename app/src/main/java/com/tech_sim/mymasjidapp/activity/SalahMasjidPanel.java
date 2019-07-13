package com.tech_sim.mymasjidapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.model.SalahTimeModel;
import com.tech_sim.mymasjidapp.utils.Constants;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SalahMasjidPanel extends AppCompatActivity {
    Button btnAddSalah,btnViewSalah,btn_cancel;
    AdView adView;
    private Constants constants;
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
        setContentView(R.layout.activity_salah_majid_panel);
        btnAddSalah=findViewById(R.id.AddSalahTime);
        btnViewSalah=findViewById(R.id.ViewSalahTime);
        btn_cancel=findViewById(R.id.cancel);
        adView=findViewById(R.id.testAd);
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        constants=new Constants(this);

        btnAddSalah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String statusOfSalah = "Normal";
                Intent intent=new Intent(SalahMasjidPanel.this,AddSalahActivity.class);
                startActivity(intent);
            }
        });

        btnViewSalah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SalahMasjidPanel.this,ViewSalahTime.class);
                startActivity(intent);
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
