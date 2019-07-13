package com.tech_sim.mymasjidapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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

public class PhoneVerify extends AppCompatActivity implements View.OnClickListener {

    private TextView txt_phone,resend_code;
    private Button btn_verifycode,btn_cancel,btn_verify;
    private EditText et_verify;
    String p_number="";
    private Constants constants;
    String phone="";
    String code="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verify);
        Intent intent=getIntent();
        p_number=intent.getStringExtra("phone_number");
        phone=intent.getStringExtra("phone");
        code=intent.getStringExtra("code");
        initXml();
        txt_phone.setText(p_number);

    }
    public void initXml(){
        txt_phone=findViewById(R.id.phone);
        resend_code=findViewById(R.id.resend);
//        btn_cancel=findViewById(R.id.cancel);
        btn_verify=findViewById(R.id.send);
        btn_verifycode=findViewById(R.id.verify);
        et_verify=findViewById(R.id.code);
        btn_verifycode.setOnClickListener(this);
        btn_verify.setOnClickListener(this);
//        btn_cancel.setOnClickListener(this);
        constants=new Constants(this);
        btn_verifycode.setEnabled(false);
//        btn_cancel.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
//            case R.id.cancel:
//                finish();
//                break;
            case R.id.send:
                 sendVerifyCode();
                break;
            case R.id.verify:
                String verify_code=et_verify.getText().toString().trim();
                if(!verify_code.equals("")) {
                    sendVerify(verify_code);
                }
                else {
                    Toast.makeText(PhoneVerify.this,"Please,waiting...",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void sendVerifyCode(){
        constants.kpHUD.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,Constants.URL_Phone_Verify,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        constants.kpHUD.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("error").equals("false")) {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("result"), Toast.LENGTH_LONG).show();
                                btn_verifycode.setEnabled(true);

                            } else {

                                Toast.makeText(getApplicationContext(),
                                        jsonObject.getString("result"), Toast.LENGTH_LONG).show();
                                btn_verifycode.setEnabled(false);
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
                params.put("phone_number",phone);
                params.put("phone_code", code);
                return params;
            }
        };
        PreferenceManager.getInstance().addToRequestQueue(stringRequest);
    }

    public void sendVerify(final String verify_code){
        String url="http://mymasjid.space/mobileApp/admin/verify_check";
        constants.kpHUD.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        constants.kpHUD.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("error").equals("false")) {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("result"), Toast.LENGTH_LONG).show();
                                Constants.check_veryfiy=true;
                                finish();

                            } else {

                                Toast.makeText(getApplicationContext(),
                                        jsonObject.getString("result"), Toast.LENGTH_LONG).show();
                             //   finish();
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
                params.put("phone_number",phone);
                params.put("phone_code", code);
                params.put("verify_code", verify_code);
                return params;
            }
        };
        PreferenceManager.getInstance().addToRequestQueue(stringRequest);

    }
    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }
}
