package com.tech_sim.mymasjidapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.tech_sim.mymasjidapp.R;

public class HepUs extends AppCompatActivity {
    Button userreg, trailreg;
    AdView adView;
//    @Override
//    public void onBackPressed() {
//        Intent setIntent = new Intent(Intent.ACTION_MAIN);
//        setIntent.addCategory(Intent.CATEGORY_HOME);
//        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(setIntent);
//    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpus);
        trailreg = findViewById(R.id.btnTrail);
        userreg = findViewById(R.id.btnRegister);
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        adView=findViewById(R.id.testAd);
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        userreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HepUs.this, ParmanatRegistartion.class);
                startActivity(intent);

            }
        });

        trailreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HepUs.this, TrialRegister.class);
                startActivity(intent);

            }
        });

    }
}
