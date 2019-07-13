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
import com.tech_sim.mymasjidapp.model.UserinfoModel;
import com.tech_sim.mymasjidapp.utils.Constants;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;
import com.tech_sim.mymasjidapp.utils.T;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ForgotPassword extends AppCompatActivity {
    private String type;
    private EditText et_email;
    private Button btn_cancel,btn_send;
    private Constants constants;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        constants=new Constants(this);
        final Intent intent=getIntent();
         type=intent.getStringExtra("type");
         btn_cancel=findViewById(R.id.btnChildcancel);
         btn_send=findViewById(R.id.btnChildUpdate);
         et_email=findViewById(R.id.pastePin);
         btn_cancel.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 finish();
             }
         });
         btn_send.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String email=et_email.getText().toString().trim();
                 sendEmail(email,type);
             }
         });
    }
    public void sendEmail(final String email, final String type)
    {
        String url= Constants.URL_Forgot_password;
        constants.kpHUD.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
               url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        constants.kpHUD.dismiss();
                        try{
                            JSONObject object=new JSONObject(response);
                            if(object.getString("error").equals("false"))
                            {
                                Toast.makeText(ForgotPassword.this,object.getString("result"),Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(ForgotPassword.this,object.getString("result"),Toast.LENGTH_SHORT).show();

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
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("type", type);
                return params;
            }
        };

        PreferenceManager.getInstance().addToRequestQueue(stringRequest);

    }
}
