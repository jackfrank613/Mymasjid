package com.tech_sim.mymasjidapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.model.SalahTimeModel;
import com.tech_sim.mymasjidapp.model.TimeModel;
import com.tech_sim.mymasjidapp.model.UserinfoModel;
import com.tech_sim.mymasjidapp.utils.Constants;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Login  extends BaseActivity implements View.OnClickListener{
    EditText editTextname, editTextpassword;
    Button buttonLogin, buttoncancel;
    public String status;
    String loginAllUser;
    private ProgressDialog progressDialog;
    private LinearLayout layout;
    AdView adView;
    private Constants constants;
    private String resfreshToken="";
    private ArrayList<UserinfoModel> userinfoModels;
    private RelativeLayout forgot_layout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
       // onTokenRefresh();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        resfreshToken= FirebaseInstanceId.getInstance().getToken();
        editTextname = findViewById(R.id.name);
        editTextpassword = findViewById(R.id.password);
        buttoncancel = findViewById(R.id.buttonLoginCancel);
        buttonLogin = findViewById(R.id.buttonLogin);
        layout=findViewById(R.id.banner);
        forgot_layout=findViewById(R.id.forgot);
        forgot_layout.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait....");
        buttonLogin.setOnClickListener(this);
        buttoncancel.setOnClickListener(this);
        constants=new Constants(this);
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        adView = findViewById(R.id.testAd);
        final AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);
        attachKeyboardListeners();
    }

    @Override
    public void onClick(View v) {
        loginAllUser = getIntent().getExtras().getString("selectionstatus");
        switch (v.getId())
        {
            case R.id.buttonLogin:
                if(loginAllUser.equals("masjid"))
                {
                    MasjidLogin();
                }
                else {
                    UserLogin();
                }
                break;
            case R.id.buttonLoginCancel:
                finish();
                break;
            case R.id.forgot:
                Intent intent=new Intent(Login.this,ForgotPassword.class);
                if(loginAllUser.equals("masjid"))
                {
                    intent.putExtra("type","masjid");
                }
                else {
                    intent.putExtra("type","user");
                }
                startActivity(intent);
                break;
        }


    }
    private void MasjidLogin() {
        final String password = editTextpassword.getText().toString().trim();
        final String username = editTextname.getText().toString().trim();
        constants.kpHUD.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_LOGIN_MASJID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        constants.kpHUD.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);

                            if (obj.getString("error").equals("false")) {
                                Toast.makeText(getApplicationContext(), obj.getString("result"), Toast.LENGTH_SHORT).show();
                                String masjid=obj.getString("mosque_id");
                                PreferenceManager.setMasjid(masjid);
                                PreferenceManager.setCheck(1);
                                PreferenceManager.setLoginMethod("masjid");
                                PreferenceManager.setEmail(username);
                                PreferenceManager.setPassword(password);
                                Intent intent1 = new Intent(Login.this, Masjid.class);
                                startActivity(intent1);

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
                params.put("email", username);
                params.put("password", password);
                params.put("token",resfreshToken);
                params.put("IMEI",PreferenceManager.getImei());
                return params;
            }
        };

        PreferenceManager.getInstance().addToRequestQueue(stringRequest);
    }

    private void UserLogin() {
        final String password = editTextpassword.getText().toString().trim();
        final String username = editTextname.getText().toString().trim();
        constants.kpHUD.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_LOGIN_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        constants.kpHUD.dismiss();
                        userinfoModels=new ArrayList<>();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("error").equals("false")) {
                                Toast.makeText(getApplicationContext(), obj.getString("result"), Toast.LENGTH_SHORT).show();
                                String masjid=obj.getString("masjid");
                                String userid=obj.getString("user_id");
                                String firstname=obj.getString("firstname");
                                String lastname=obj.getString("lastname");
                                String house_no=obj.getString("house_no");
                                String email=obj.getString("email");
                                String phone=obj.getString("mobile_number");
                                PreferenceManager.setUserid(userid);
                                PreferenceManager.setMasjid(masjid);
                                PreferenceManager.setCheck(2);
                                PreferenceManager.setLoginMethod("user");
                                PreferenceManager.setEmail(username);
                                PreferenceManager.setPassword(password);
                                status = "hide";
                                UserinfoModel userinfoModel=new UserinfoModel(userid,firstname,lastname,email,house_no,phone);
                                userinfoModels.add(userinfoModel);
                                setList("info",userinfoModels);
                                getSalahTime(masjid,userid);




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
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", username);
                params.put("password", password);
                params.put("token",resfreshToken);
                params.put("IMEI",PreferenceManager.getImei());
                return params;
            }
        };

        PreferenceManager.getInstance().addToRequestQueue(stringRequest);
    }
    public void getSalahTime(final String masjid,final String userid){

        String url="http://mymasjid.space/mobileApp/admin/getSalahMasjid";
//        final String masjid=PreferenceManager.getMasjid();
//        constants.kpHUD.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //    constants.kpHUD.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean expire=jsonObject.getBoolean("expire");
                            String user_type=jsonObject.getString("user_type");
                            if(jsonObject.getString("error").equals("false")) {
                                JSONArray array=jsonObject.getJSONArray("data");
                                for(int i=0;i<array.length();i++)
                                {
                                    JSONObject object=array.getJSONObject(i);
                                    String startTime=object.getString("start_time");
                                    String jmahTime=object.getString("jamaah_time");
                                    String endTime=object.getString("end_time");
                                    String slahName=object.getString("salah_name");
                                    TimeModel timeModel=new TimeModel(startTime,slahName);
                                    PreferenceManager.timeModels.add(timeModel);
                                    SalahTimeModel timeModel1=new SalahTimeModel(i,slahName,startTime,jmahTime,endTime);
                                    PreferenceManager.salahTimeModels.add(timeModel1);
                                }

                            }
                            else {

                               // Toast.makeText(Login.this,"Not refund",Toast.LENGTH_SHORT).show();
                            }
                            Intent intent1 = new Intent(Login.this, UserDashboard.class);
                            intent1.putExtra("status", status);
                            startActivity(intent1);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  constants.kpHUD.dismiss();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("masjid",masjid);
                params.put("user_id",userid);
                return params;
            }
        };
        PreferenceManager.getInstance().addToRequestQueue(stringRequest);
    }
    public void setList(String key, ArrayList<UserinfoModel> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        PreferenceManager.set(key,json);
    }
//    @Override
//    public void onBackPressed() {
//        Intent setIntent = new Intent(Intent.ACTION_MAIN);
//        setIntent.addCategory(Intent.CATEGORY_HOME);
//        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(setIntent);
//    }
    @Override
    protected void onShowKeyboard(int keyboardHeight) {
        // do things when keyboard is shown
        layout.setVisibility(View.GONE);
    }

    @Override
    protected void onHideKeyboard() {
        // do things when keyboard is hidden
        layout.setVisibility(View.VISIBLE);
    }

}
