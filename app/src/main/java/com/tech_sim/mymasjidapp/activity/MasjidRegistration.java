package com.tech_sim.mymasjidapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.utils.Constants;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MasjidRegistration extends AppCompatActivity {
    private Button btnMasjidcancel, btnMasjidregister;
    public String status, email;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private EditText masjidAdress, masjidName, masjidEmail, masjidPass, masjidPostCode, masjidRePass;
    private EditText masjid_web;
    private Constants constants;
    private String refreshToken="";
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
        setContentView(R.layout.activity_masjid_registration);
        refreshToken = FirebaseInstanceId.getInstance().getToken();
        btnMasjidcancel = findViewById(R.id.MasjidRegCancel);
        btnMasjidregister = findViewById(R.id.MasjidRegistration);
        masjidAdress = findViewById(R.id.MasjidAdresss);
        masjidName = findViewById(R.id.MasjidName);
        masjidEmail = findViewById(R.id.MasjidEmail);
        masjidPass = findViewById(R.id.MasjidPass);
        masjidRePass = findViewById(R.id.MasjidRePass);
        masjidPostCode = findViewById(R.id.MasjidPostCode);
        masjid_web=findViewById(R.id.weblink);
        email = masjidEmail.getText().toString().trim();
        constants=new Constants(this);
        btnMasjidregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerMosque();
            }
        });
        btnMasjidcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private void registerMosque() {
        final String mNameHolder = masjidName.getText().toString().trim();
        final String mEmailHolder = masjidEmail.getText().toString().trim();
        final String mPassHolder = masjidPass.getText().toString().trim();
        final String mPostCode = masjidPostCode.getText().toString().trim();
        final String mAdress = masjidAdress.getText().toString().trim();
        final String web=masjid_web.getText().toString().trim();
        final int status = 1;
        constants.kpHUD.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        constants.kpHUD.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("error").equals("false")) {
                                String masjid=jsonObject.getString("masjid_id");
                                PreferenceManager.setMasjid(masjid);
                                PreferenceManager.setCheck(1);
                                PreferenceManager.setLoginMethod("masjid");
                                PreferenceManager.setEmail(mEmailHolder);
                                PreferenceManager.setPassword(mPassHolder);
                                Toast.makeText(getApplicationContext(), jsonObject.getString("result"), Toast.LENGTH_LONG).show();
                                startActivity(new Intent(MasjidRegistration.this, Masjid.class));
                            } else {

                                Toast.makeText(getApplicationContext(),
                                        jsonObject.getString("result"), Toast.LENGTH_LONG).show();
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
                params.put("name", mNameHolder);
                params.put("email", mEmailHolder);
                params.put("password", mPassHolder);
                params.put("postcode", mPostCode);
                params.put("address", mAdress);
                params.put("status", String.valueOf(status));
                params.put("token",refreshToken);
                params.put("website_url",web);
                //    params.put("IMEI",PreferenceManager.imei);
                return params;
            }
        };
        PreferenceManager.getInstance().addToRequestQueue(stringRequest);
    }
}
