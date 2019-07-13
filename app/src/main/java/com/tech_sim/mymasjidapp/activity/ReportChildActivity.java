package com.tech_sim.mymasjidapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tech_sim.mymasjidapp.R;

public class ReportChildActivity extends AppCompatActivity {
    private TextView age,f_name,contact,childname,pin_txt;
    private CheckBox sun,mon,tu,we,th,fr,sa;
    private String child_age,child_name,father_name,contactno,classdays,pin;
    private String days[]=new String[50];
    private Button btn_cancel,btn_report;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_child_activity_layout);
        final Intent intent=getIntent();
        child_age=intent.getStringExtra("age");
        child_name=intent.getStringExtra("name");
        father_name=intent.getStringExtra("father");
        contactno=intent.getStringExtra("contact");
        classdays=intent.getStringExtra("classdays");
        pin=intent.getStringExtra("pin_number");
        initXml();
        childname.setText(child_name);
        age.setText(child_age);
        pin_txt.setText(pin);
        f_name.setText(father_name);
        contact.setText(contactno);
        days=classdays.split(",");
        int count=days.length;

        for(int i=0;i<days.length;i++)
        { if(sun.getText().toString().equals(days[i]))
            {
                sun.setChecked(true);

            }
            else if(mon.getText().toString().equals(days[i]))
            {
                mon.setChecked(true);
            }
            else if(fr.getText().toString().equals(days[i]))
            {
                fr.setChecked(true);

            }
            else if(tu.getText().toString().equals(days[i]))
            {
                tu.setChecked(true);
            }
            else if(sa.getText().toString().equals(days[i]))
            {
                sa.setChecked(true);
            }
            else if(th.getText().toString().equals(days[i]))
            {
                th.setChecked(true);
            }
            else if(we.getText().toString().equals(days[i])) {
                we.setChecked(true);
            }
        }
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(ReportChildActivity.this,StudentActivity.class);
                intent1.putExtra("name",child_name);
                startActivity(intent1);
            }
        });

    }
    public void initXml(){
        age=(TextView)findViewById(R.id.age);
        f_name=(TextView)findViewById(R.id.f_name);
        contact=(TextView)findViewById(R.id.phone);
        childname=(TextView)findViewById(R.id.AttendanceText);
        sun=(CheckBox)findViewById(R.id.sunday);
        mon=(CheckBox)findViewById(R.id.monday);
        tu=(CheckBox)findViewById(R.id.tuseday);
        we=(CheckBox)findViewById(R.id.wenesday);
        th=(CheckBox)findViewById(R.id.thursday);
        fr=(CheckBox)findViewById(R.id.friday);
        sa=(CheckBox)findViewById(R.id.saturday);
        btn_cancel=findViewById(R.id.btncanclAttendance);
        btn_report=findViewById(R.id.btnSubmitAttendance);
        pin_txt=findViewById(R.id.pin);
    }
    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }
}
