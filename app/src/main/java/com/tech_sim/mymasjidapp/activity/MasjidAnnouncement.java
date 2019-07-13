package com.tech_sim.mymasjidapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.adapter.AnnounceAudioAdapter;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;

import java.util.ArrayList;

public class MasjidAnnouncement extends AppCompatActivity {

    Button btncancelll,btn_back;
   // ArrayList<ViewAudioModel> audioModels=new ArrayList<>();
    RecyclerView listView;
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
        setContentView(R.layout.activity_announcement);
        listView = findViewById(R.id.announcement);
        AnnounceAudioAdapter announceAudioAdapter=new AnnounceAudioAdapter(this, PreferenceManager.viewAudioModels);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(announceAudioAdapter);
        btncancelll = findViewById(R.id.btncancdeeel);
        btncancelll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreferenceManager.viewAudioModels=new ArrayList<>();
                startActivity(new Intent(MasjidAnnouncement.this,MasjidAnnouncementActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                PreferenceManager.viewAudioModels=new ArrayList<>();
               finish();
        }
        return (super.onOptionsItemSelected(menuItem));
    }


}