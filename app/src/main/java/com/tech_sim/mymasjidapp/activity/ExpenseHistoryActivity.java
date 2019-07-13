package com.tech_sim.mymasjidapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tech_sim.mymasjidapp.R;

public class ExpenseHistoryActivity extends AppCompatActivity {
    private TextView expense_name,expense_amount,expense_detail;
    private Button btn_back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_list_expense);
        Intent intent=getIntent();
        String type=intent.getStringExtra("name");
        String amount=intent.getStringExtra("amount");
        String detail=intent.getStringExtra("detail");
        initxml();
        expense_name.setText(type);
        expense_detail.setText(detail);
        expense_amount.setText(amount+"£");
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void initxml(){
        expense_amount=(TextView)findViewById(R.id.e_amount);
        expense_name=(TextView)findViewById(R.id.e_name);
        expense_detail=(TextView)findViewById(R.id.e_detail);
        btn_back=(Button)findViewById(R.id.btn_cancel);
    }
    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }
}
