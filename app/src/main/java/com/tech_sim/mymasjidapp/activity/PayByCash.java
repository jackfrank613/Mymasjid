package com.tech_sim.mymasjidapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class PayByCash extends AppCompatActivity implements View.OnClickListener{
    private EditText et_amount;
    private Button btn_cancel, btn_save;
    private Constants constants;
    private String child_id="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paybycash);
        constants=new Constants(this);
        Intent intent=getIntent();
        child_id=intent.getStringExtra("childid");
        initialXml();
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("test","cancel");
                finish();
            }
        });
    }
    public void initialXml(){
        et_amount=findViewById(R.id.Email);
        btn_cancel=findViewById(R.id.btncancel);
        btn_save=findViewById(R.id.btnsendCard);
        btn_save.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_cancel:
              //  finish();
                break;
            case R.id.btnsendCard:
                save_amount();
                break;
        }
    }
    public void save_amount()
    {
        String url= Constants.cash_url;
        final String amount=et_amount.getText().toString().trim();
        final String userid= PreferenceManager.getUserid();
        final String masjid=PreferenceManager.getMasjid();
        constants.kpHUD.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        constants.kpHUD.dismiss();
                        try {
                            JSONObject object1=new JSONObject(response);
                            if (object1.getString("error").equals("false"))
                            {
                               Toast.makeText(PayByCash.this,object1.getString("result"),Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(PayByCash.this,object1.getString("result"),Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                constants.kpHUD.dismiss();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("amount",amount);
                params.put("currencey","gbp");
                params.put("masjid",masjid);
                params.put("user_id",userid);
                params.put("children_id",child_id);
                return params;
            }
        };
        PreferenceManager.getInstance().addToRequestQueue(stringRequest);

    }

}
