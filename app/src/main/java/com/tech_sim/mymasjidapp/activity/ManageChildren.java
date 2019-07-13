package com.tech_sim.mymasjidapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.tech_sim.mymasjidapp.R;

public class ManageChildren extends AppCompatActivity {
    Button btnaddchild,btnviewchild,btnchildAttendance,btn_cancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_manage_layout);
        btnaddchild=findViewById(R.id.AddchildMasjid);
        btnviewchild=findViewById(R.id.ViewChild);
        btnchildAttendance=findViewById(R.id.ChildAttendance);
        btn_cancel=findViewById(R.id.cancel);

        btnaddchild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String statusOfChild = "Normal";
                Intent intent=new Intent(getApplicationContext(),ChildrenAddActivity.class);
                intent.putExtra("status", statusOfChild);
                startActivity(intent);
            }
        });
        btnviewchild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getApplicationContext(),ViewChildActivity.class);
                startActivity(intent);

            }
        });
        btnchildAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ChildAttendanceActivity.class);
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
    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }
}
