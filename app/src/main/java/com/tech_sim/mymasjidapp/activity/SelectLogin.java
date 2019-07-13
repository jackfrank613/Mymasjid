package com.tech_sim.mymasjidapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.tech_sim.mymasjidapp.R;

public class SelectLogin extends AppCompatActivity {
    public String SelectionStatus;
    TextView textView;

    Button btnloginMasjid,btnloginUser;
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
        setContentView(R.layout.activity_selectlogin);

        btnloginMasjid=findViewById(R.id.MasjidLogin);
        btnloginUser=findViewById(R.id.UserLogin);
        textView=findViewById(R.id.registerhere);
        textView.setText(Html.fromHtml(getString(R.string.registerhere)));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SelectLogin.this,RegistrationMainScreen.class));
            }
        });
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        adView = findViewById(R.id.testAd);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);
        btnloginMasjid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectionStatus = "masjid";
                Log.e("Masjid","Required  Login");
                Intent intent = new Intent(SelectLogin.this, Login.class);
                intent.putExtra("selectionstatus", SelectionStatus);
                startActivity(intent);
            }
        });

        btnloginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectionStatus="User";
                Intent intent = new Intent(SelectLogin.this, Login.class);
                intent.putExtra("selectionstatus", SelectionStatus);
                startActivity(intent);
            }
        });

    }
}
