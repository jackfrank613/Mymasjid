package com.tech_sim.mymasjidapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.tech_sim.mymasjidapp.R;

public class ManageExpenseActivity extends AppCompatActivity {

    private Button btn_add;
    private Button btn_view;
    private Button btn_expense;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_expense_layout);
        btn_add=findViewById(R.id.add_btn);
        btn_view=findViewById(R.id.view_btn);
        btn_expense=findViewById(R.id.view_cancel);
        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent1=new Intent(ManageExpenseActivity.this,ViewExpense.class);
              startActivity(intent1);

            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ManageExpenseActivity.this,MasjidExpenseActivity.class);
                startActivity(intent);
            }
        });
        btn_expense.setOnClickListener(new View.OnClickListener() {
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
