package com.tech_sim.mymasjidapp.activity;

import android.app.ProgressDialog;
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
import com.google.android.gms.ads.AdView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.rilixtech.CountryCodePicker;
import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.model.Masjiidmodel;
import com.tech_sim.mymasjidapp.model.SalahTimeModel;
import com.tech_sim.mymasjidapp.model.TimeModel;
import com.tech_sim.mymasjidapp.utils.Constants;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TrialRegister extends AppCompatActivity implements View.OnClickListener{
    EditText trailFirstname, trailSurname, trailEmail, trailPassword, trailRePassword, trailPostcode, trailHouseNo;
    Button trailSelectMosqS;
    TextView trailmajid;
    Button trailbtncancel, trailbtnregister;
    public String status;
    AdView adView;
    ProgressDialog TrailprogressDialog;
    public String masjid_name="";
    public String masjid_id="";
    public Constants kHpp;
    private String refreshToken="";
    CountryCodePicker ccp;
    EditText edtPhoneNumber;
    private Constants constants;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial);
        constants=new Constants(this);
        refreshToken= FirebaseInstanceId.getInstance().getToken();
        trailFirstname = findViewById(R.id.TrailfirstName);
        trailSurname = findViewById(R.id.TrailsurName);
        trailEmail = findViewById(R.id.TrailEmail);
        trailPassword = findViewById(R.id.Trailpassword);
        trailPostcode = findViewById(R.id.TrailpostCode);
        trailRePassword=findViewById(R.id.TrailREpassword);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        edtPhoneNumber=findViewById(R.id.phone_number_edt);
        trailHouseNo = findViewById(R.id.TrailHouseNo);
        trailSelectMosqS =(Button) findViewById(R.id.trailSelectMoqS);
        trailbtncancel = findViewById(R.id.btnTrailcancel);
        trailmajid=(TextView) findViewById(R.id.Trailmasjidtext);
        trailbtnregister = findViewById(R.id.btnTrailregister);
        TrailprogressDialog = new ProgressDialog(this);
        trailbtncancel.setOnClickListener(this);
        trailbtnregister.setOnClickListener(this);
        kHpp=new Constants(this);
        masjid_name=Constants.masid_name;
        trailPostcode.setText(Constants.search);
        trailmajid.setText(Constants.masid_name);
        masjid_id=Constants.masjid_id;
         trailFirstname.setText(Constants.firstname);
         trailSurname.setText(Constants.surname);
         trailEmail.setText(Constants.email_code);
         trailPassword.setText(Constants.pssword_code);
         trailRePassword.setText(Constants.confirmpass);
         trailHouseNo.setText(Constants.house);
         edtPhoneNumber.setText(Constants.phone);
         edtPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    if(!edtPhoneNumber.getText().toString().equals(""))
                    {
                        String phone=edtPhoneNumber.getText().toString().trim();
                        Intent intent=new Intent(TrialRegister.this,PhoneVerify.class);
                        intent.putExtra("phone_number","+"+ccp.getSelectedCountryCode()+phone);
                        intent.putExtra("phone",phone);
                        intent.putExtra("code","+"+ccp.getSelectedCountryCode());
                        startActivity(intent);
                    }
                }
            }
        });
        if(Constants.check_veryfiy)
        {
            edtPhoneNumber.setFocusable(false);
        }
        trailSelectMosqS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!trailPostcode.getText().toString().equals(""))
                {
                    Constants.search=trailPostcode.getText().toString();
                    SearchMasjid(trailPostcode.getText().toString());
                }
                else {

                }

            }
        });
        trailbtnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!trailmajid.getText().toString().equals("")) {
                        registerTrailUser();

                }
                else {
                    Toast.makeText(TrialRegister.this,"After input your postcode,Please,search Masjid using Search Masjid",Toast.LENGTH_SHORT).show();
                }
            }
        });
        trailbtncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onClick(View v) {

    }

    public void SearchMasjid(final String post)
    {
        Constants.firstname=trailFirstname.getText().toString();
        Constants.surname=trailSurname.getText().toString();
        Constants.email_code=trailEmail.getText().toString();
        Constants.pssword_code=trailPassword.getText().toString();
        Constants.confirmpass=trailRePassword.getText().toString();
        Constants.house=trailHouseNo.getText().toString();
        Constants.phone=edtPhoneNumber.getText().toString();
       constants.kpHUD.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_SEARCH_MASJID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        constants.kpHUD.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            // Toast.makeText(getApplicationContext(), jsonObject.getString("result"), Toast.LENGTH_LONG).show();
                            JSONArray m_jArry = jsonObject.getJSONArray("result");
                            for(int j=0;j<m_jArry.length();j++)
                            {
                                JSONObject object=m_jArry.getJSONObject(j);
                                String id=object.getString("id");
                                String masji_name=object.getString("name");
                                String masji_address=object.getString("address");
                                String latitude=object.getString("latitude");
                                String city=object.getString("city");
                                String longitude=object.getString("longitude");
                                Masjiidmodel masjiidmodel=new Masjiidmodel(id,masji_name,masji_address,longitude,latitude,city);
                                PreferenceManager.masjiidmodels.add(masjiidmodel);
                            }
                            Intent intent=new Intent(TrialRegister.this,MasjidMapviewActivity.class);
                            startActivity(intent);

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
                params.put("post_code", post);
                return params;
            }
        };
        PreferenceManager.getInstance().addToRequestQueue(stringRequest);
    }
    private void registerTrailUser() {
        final String tuFirstNameHolder = trailFirstname.getText().toString().trim();
        final String tuSurNameHolder = trailSurname.getText().toString().trim();
        final String tuEmailHolder = trailEmail.getText().toString().trim();
        final String tuPassHolder = trailPassword.getText().toString().trim();
        final String tuPostCode = trailPostcode.getText().toString().trim();
        final String tuHouseNo = trailHouseNo.getText().toString().trim();
        final String phone=edtPhoneNumber.getText().toString().trim();
        final String code=ccp.getSelectedCountryCode();

//        final String tuSelectMosqS=trailSelectMosqS.getSelectedItem().toString();
        final int status = 1;
        final String User_Type = "Trial";
        constants.kpHUD.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_REGISTER_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        constants.kpHUD.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getString("error").equals("false"))
                            {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("result"), Toast.LENGTH_LONG).show();
                                String masjid=jsonObject.getString("masjid");
                                String userid=jsonObject.getString("user_id");
                                PreferenceManager.setUserid(userid);
                                PreferenceManager.setMasjid(masjid);
                                PreferenceManager.setCheck(2);
                                PreferenceManager.setLoginMethod("user");
                                PreferenceManager.setEmail(tuEmailHolder);
                                PreferenceManager.setPassword(tuPassHolder);
                                Constants.search="";
                                Constants.firstname="";
                                Constants.surname="";
                                Constants.email_code="";
                                Constants.pssword_code="";
                                Constants.confirmpass="";
                                Constants.house="";
                                Constants.phone="";
                                getSalahTime(masjid);

                            }
                            else {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("result"), Toast.LENGTH_LONG).show();

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
                params.put("firstname", tuFirstNameHolder);
                params.put("lastname", tuSurNameHolder);
                params.put("email", tuEmailHolder);
                params.put("password", tuPassHolder);
                params.put("postcode", tuPostCode);
                params.put("house_no", tuHouseNo);
                params.put("status", String.valueOf(status));
                params.put("user_type", User_Type);
                params.put("mobile_number",phone);
                params.put("mobile_code",code);
                params.put("subscription_amount", User_Type);
                params.put("masjid",masjid_id);
                params.put("token",refreshToken);
                params.put("IMEI",PreferenceManager.getImei());
                return params;
            }
        };
        PreferenceManager.getInstance().addToRequestQueue(stringRequest);
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
    }
    public void getSalahTime(final String masjid){

        String url="http://mymasjid.space/mobileApp/admin/getSalahMasjid";
//        final String masjid=PreferenceManager.getMasjid();
        constants.kpHUD.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        constants.kpHUD.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
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
                                startActivity(new Intent(TrialRegister.this, UserDashboard.class));
                                finish();

                            }
                            else {
                               // Toast.makeText(TrialRegister.this,"Not refund",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(TrialRegister.this, UserDashboard.class));
                                finish();
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
                params.put("masjid",masjid);
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
