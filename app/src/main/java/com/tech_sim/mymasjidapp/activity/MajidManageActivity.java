package com.tech_sim.mymasjidapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.tech_sim.mymasjidapp.R;

public class MajidManageActivity extends AppCompatActivity {
    Button btnaddMasjidCommittee,btnviewMasjidComittee,btn_cancel;
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
        setContentView(R.layout.activity_manage_committee);
        btnaddMasjidCommittee=findViewById(R.id.AddMasjidCommittee);
        btnviewMasjidComittee=findViewById(R.id.ViewMasjidCommittee);
        btn_cancel=findViewById(R.id.cancel);

        btnaddMasjidCommittee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4=new Intent(MajidManageActivity.this,MasjidCommittee.class);
                startActivity(intent4);
            }
        });
        btnviewMasjidComittee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4=new Intent(MajidManageActivity.this,ViewMasjidComitte.class);
                startActivity(intent4);
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
