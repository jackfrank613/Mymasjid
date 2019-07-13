package com.tech_sim.mymasjidapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.model.SalahTimeModel;
import com.tech_sim.mymasjidapp.model.TimeModel;
import com.tech_sim.mymasjidapp.model.UserinfoModel;
import com.tech_sim.mymasjidapp.utils.Constants;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;
import com.tech_sim.mymasjidapp.utils.VolleyMultipartRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PayFeeForApp extends AppCompatActivity {
    private EditText et_amount,et_card,et_cvc,et_month,et_year;
    private String email="";
    private String amount="";
    private String f_name,s_name,pass,phone,phone_code,masjid,exp,house,packager,type,status,token,post;
    private Button btn_cancel,btn_pay;
    private Constants constants;
    private ArrayList<UserinfoModel> userinfoModels;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_layout);
        constants=new Constants(this);
        Intent intent=getIntent();
        email=intent.getStringExtra("email");
        amount=intent.getStringExtra("amount");
        f_name=intent.getStringExtra("first_name");
        s_name=intent.getStringExtra("second_name");
        pass=intent.getStringExtra("pass");
        phone=intent.getStringExtra("phone");
        phone_code=intent.getStringExtra("phone_code");
        masjid=intent.getStringExtra("masjid");
        exp=intent.getStringExtra("exp");
        house=intent.getStringExtra("house");
        packager=intent.getStringExtra("package");
        type=intent.getStringExtra("type");
        status=intent.getStringExtra("status");
        token=intent.getStringExtra("token");
        post=intent.getStringExtra("postcode");
        initXml();
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.check_pay=1;
                finish();
            }
        });
        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String card=et_card.getText().toString().trim();
                String cvccode=et_cvc.getText().toString().trim();
                String month=et_month.getText().toString().trim();
                String year=et_year.getText().toString().trim();
                if(amount.equals("")||card.equals("")||cvccode.equals("")||month.equals("")||year.equals(""))
                {
                    Toast.makeText(PayFeeForApp.this,"Please enter the correct values.",Toast.LENGTH_SHORT).show();
                    }
                else {
                    payFunction(email,amount,card,cvccode,month,year);
                }
            }
        });

    }
    public void initXml()
    {
        et_card=(EditText)findViewById(R.id.cardnmuber);
        et_cvc=(EditText)findViewById(R.id.cvvcode);
        et_month=(EditText)findViewById(R.id.month);
        et_year=(EditText)findViewById(R.id.year);
        btn_cancel=(Button)findViewById(R.id.btn_cancel);
        btn_pay=(Button)findViewById(R.id.btn_pay);
    }
    public void payFunction(final String email, final String amount, final String card, final String cvcard, final String month,final String year)
    {
       String url=Constants.User_pay_url;
       String tag="PAY";
        constants.kpHUD.show();
        VolleyMultipartRequest req = new VolleyMultipartRequest(com.android.volley.Request.Method.POST,
                url, new com.android.volley.Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
//                constants.kpHUD.dismiss();
                String responseStr=new String(response.data);
                try {
                    JSONObject responseobject=new JSONObject(responseStr);
                    if (responseobject.getString("error").equals("false")) {
                 //       Toast.makeText(PayFeeForApp.this, responseobject.getString("result"), Toast.LENGTH_SHORT).show();
                        Constants.check_pay=2;
                        registerParmanentUser();

                    }
                    else {
                        Toast.makeText(PayFeeForApp.this, responseobject.getString("result"), Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException ignored){}
            }
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                constants.kpHUD.dismiss();
                Log.d("Error.Response","that is not");
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                HashMap<String,String> params=new HashMap<String,String>();
                params.put("card_num",card);
                params.put("cvc",cvcard);
                params.put("exp_year",year);
                params.put("exp_month",month);
                params.put("amount",amount);
                params.put("email",email);
                return params;
            }

        };
        // add the request object to the queue to be executed
        PreferenceManager.getInstance().addToRequestQueue(req,tag);


    }
    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }
    private void registerParmanentUser() {
//        constants.kpHUD.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_REGISTER_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        userinfoModels=new ArrayList<>();
//                        constants.kpHUD.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("error").equals("false")) {

                                Toast.makeText(getApplicationContext(), jsonObject.getString("result"), Toast.LENGTH_LONG).show();

                                status = "hide";
                                String masjid=jsonObject.getString("masjid");
                                String userid=jsonObject.getString("user_id");
                                PreferenceManager.setUserid(userid);
                                PreferenceManager.setMasjid(masjid);
                                PreferenceManager.setCheck(2);
                                PreferenceManager.setLoginMethod("user");
                                PreferenceManager.setEmail(email);
                                PreferenceManager.setPassword(pass);
                                Constants.firstname="";
                                Constants.surname="";
                                Constants.pay="";
                                Constants.email_code="";
                                Constants.pssword_code="";
                                Constants.confirmpass="";
                                Constants.house="";
                                Constants.pay="";
                                Constants.phone="";
                                Constants.check_veryfiy=false;
                                UserinfoModel userinfoModel=new UserinfoModel(userid,f_name,s_name,email,house,phone);
                                userinfoModels.add(userinfoModel);
                                setList("info",userinfoModels);
//                                Intent intent1 = new Intent(ParmanatRegistartion.this, UserDashboard.class);
////                                intent1.putExtra("status", status);
////                                startActivity(intent1);
                                getSalahTime(masjid,userid);

////'
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

                params.put("firstname", f_name);
                params.put("lastname", s_name);
                params.put("email", email);
                params.put("password", pass);
//                params.put("sort_code", prmtSortCodeHolder);
//                params.put("account_no", prmtBankAccountHolder);
                params.put("postcode", post);
                params.put("house_no", house);
                params.put("status", String.valueOf(status));
                params.put("user_type", type);
//                params.put("masjid",prmtSelectMosqSHolder);
                params.put("masjid",masjid);
                params.put("mobile_number",phone);
                params.put("mobile_code",phone_code);
                params.put("subscription_month", exp);
                params.put("subscription_amount", amount);
                params.put("token",token);
                params.put("IMEI",PreferenceManager.getImei());
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
    public void getSalahTime(final String masjid,final String userid){

        String url="http://mymasjid.space/mobileApp/admin/getSalahMasjid";
//        final String masjid=PreferenceManager.getMasjid();
//        constants.kpHUD.show();
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
                                Intent intent = new Intent(PayFeeForApp.this, UserDashboard.class);
                                startActivity(intent);
                            }
                            else {
                                Intent intent = new Intent(PayFeeForApp.this, UserDashboard.class);
                                startActivity(intent);
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
                params.put("user_id",userid);
                return params;
            }
        };
        PreferenceManager.getInstance().addToRequestQueue(stringRequest);
    }
}
