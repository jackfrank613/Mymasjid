package com.tech_sim.mymasjidapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tech_sim.mymasjidapp.R;

public class Expense extends AppCompatActivity implements View.OnClickListener{
    EditText addTotalExpence,expenceDetail;

Button btncancelexpense,btnsaveexpense;
ProgressDialog progressDialogExpence;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_new_expense);
        btncancelexpense=findViewById(R.id.btncancelexpense);
        btnsaveexpense=findViewById(R.id.btnsaveexpense);
        addTotalExpence=findViewById(R.id.AddTotalExpence);
        expenceDetail=findViewById(R.id.ExpenceDetail);
        btnsaveexpense.setOnClickListener(this);
        btncancelexpense.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btnsaveexpense) {
            if (addTotalExpence.getText().toString().trim().equals("")) {
                addTotalExpence.setError("Please Enter Total Amount of expense");
            }else if (expenceDetail.getText().toString().trim().equals("")) {
                expenceDetail.setError("Please Enter Description of expense");
            }else {

            }
        } else if (view == btncancelexpense) {

            finish();
        }
    }
    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }
}
