package com.tech_sim.mymasjidapp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.utils.Constants;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;
import com.tech_sim.mymasjidapp.utils.VolleyMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PayFee extends AppCompatActivity {
    Button btnPayCard,btnPayCash,btnCancel,btn_history;
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
        setContentView(R.layout.activity_pay);
        constants=new Constants(this);
        btnPayCard=findViewById(R.id.PayCard);
        btnPayCash=findViewById(R.id.PayCash);
        btnCancel=findViewById(R.id.cancel);
        btn_history=findViewById(R.id.Payhistory);

        btnPayCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.pay_check=true;
                Intent intent=new Intent(PayFee.this,ChildPayfeeActivity.class);
                startActivity(intent);

            }
        });
        btnPayCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Constants.pay_check=false;
                Intent intent1=new Intent(PayFee.this,ChildPayfeeActivity.class);
                startActivity(intent1);

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent3=new Intent(PayFee.this,UserDashboard.class);
//                startActivity(intent3);
                finish();
            }
        });
        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(PayFee.this,HistoryPayFeeActivity.class);
                startActivity(intent1);
            }
        });
    }

}
