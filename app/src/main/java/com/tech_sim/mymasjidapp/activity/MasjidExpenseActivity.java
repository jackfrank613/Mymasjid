package com.tech_sim.mymasjidapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.utils.Constants;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MasjidExpenseActivity extends AppCompatActivity {
    private TextView balance,expense;
    private CheckBox check_income,check_expense;
     private EditText total_amount;
     private EditText detail;
     private Constants constants;
     private Button btn_cancel,btn_update;
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
        setContentView(R.layout.masjid_expense_layout);
        constants=new Constants(this);
        initXml();
        expense=(TextView)findViewById(R.id.expenser);
        check_income=(CheckBox)findViewById(R.id.check2);
        check_expense=(CheckBox)findViewById(R.id.check1);
        getExpense();
        btn_cancel=(Button)findViewById(R.id.cancel);
        btn_update=(Button)findViewById(R.id.update);
        total_amount=(EditText)findViewById(R.id.addamount);
        detail=(EditText)findViewById(R.id.detail);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount=total_amount.getText().toString().trim();
                String ex_detail=detail.getText().toString().trim();
               if(check_expense.isChecked())
               {
                 sendExpense(amount,ex_detail);
               }
               if(check_income.isChecked())
               {
                   sendIncome(amount,ex_detail);
               }
            }
        });
    }
    public void getExpense(){
        String url="http://mymasjid.space/mobileApp/admin/get_expense_masjid";
        final String masjid_id=PreferenceManager.getMasjid();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        constants.kpHUD.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                                if (obj.getString("error").equals("false")) {
                              //  Toast.makeText(getApplicationContext(), obj.getString("result"), Toast.LENGTH_SHORT).show();
                                    JSONObject object1=obj.getJSONObject("result");
                              String expense_txt=object1.getString("expense");
                              String income_txt=object1.getString("incoming");
                              expense.setText(expense_txt);
                              float ex_price=Float.valueOf(expense_txt);
                              float income_price=Float.valueOf(income_txt);
                              balance.setText(String.valueOf(income_price-ex_price));

                            } else {

                                Toast.makeText(getApplicationContext(),
                                        obj.getString("result"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                constants.kpHUD.dismiss();
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("masjid", masjid_id);
                return params;
            }
        };

        PreferenceManager.getInstance().addToRequestQueue(stringRequest);


    }
    public void initXml(){
        balance=(TextView)findViewById(R.id.balancer);

    }
    private void sendExpense(final String amount, final String detail){
        String url="http://mymasjid.space/mobileApp/admin/expense_incoming_masjid";
        final String masjid_id=PreferenceManager.getMasjid();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        constants.kpHUD.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("error").equals("false")) {
                                  Toast.makeText(getApplicationContext(), obj.getString("result"), Toast.LENGTH_SHORT).show();
                                  finish();


                            } else {

                                Toast.makeText(getApplicationContext(),
                                        obj.getString("result"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                constants.kpHUD.dismiss();
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("masjid", masjid_id);
                params.put("type","expense");
                params.put("amount",amount);
                params.put("detail",detail);
                return params;
            }
        };

        PreferenceManager.getInstance().addToRequestQueue(stringRequest);
    }
    private void sendIncome(final String amount,final String detail){
        String url="http://mymasjid.space/mobileApp/admin/expense_incoming_masjid";
        final String masjid_id=PreferenceManager.getMasjid();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        constants.kpHUD.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("error").equals("false")) {
                                Toast.makeText(getApplicationContext(), obj.getString("result"), Toast.LENGTH_SHORT).show();
                               finish();

                            } else {

                                Toast.makeText(getApplicationContext(),
                                        obj.getString("result"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                constants.kpHUD.dismiss();
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("masjid", masjid_id);
                params.put("type","incoming");
                params.put("amount",amount);
                params.put("detail",detail);
                return params;
            }
        };

        PreferenceManager.getInstance().addToRequestQueue(stringRequest);

    }

}
