package com.tech_sim.mymasjidapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.rilixtech.Country;
import com.rilixtech.CountryCodePicker;
import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.model.Masjiidmodel;
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

public class ParmanatRegistartion extends AppCompatActivity implements View.OnClickListener{
    EditText prmtFirstName, prmtSurName, prmtEmail, prmtPass, prmtrePass, prmtSortCode, prmtBankAccount, prmtPostCode, prmtHouseNo;
    TextView prmtPrice;
    //    Spinner prmtSelectMosque, prmtSelectBank;
    RadioGroup prmtRadioGroup;
    ArrayList<String> masjidArray = new ArrayList<String>();
    ArrayList<String> masjidIDs = new ArrayList<String>();
    RadioButton prmtRadioOne, prmtRadioSix, prmtRadioOneYear;
    Button prmtBtnCancel, prmtBtnRegister, pmSearchMosque;
    String prmtPostCodeHolder;
    ProgressDialog prmtprogressDialog;
    public String status;



    public TextView user_masjid;
    ProgressDialog TrailprogressDialog;
    String masjid_name="";
    String masjid_id="";
    String refreshToken="";
    private Constants constants;
    CountryCodePicker ccp;
    EditText edtPhoneNumber;
    private ArrayList<UserinfoModel> userinfoModels;
    private int request_code=100;
    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_CANCELED) {
               // registerParmanentUser();
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parmanatregistartion);
        refreshToken= FirebaseInstanceId.getInstance().getToken();
        prmtFirstName = findViewById(R.id.PmFirstName);
        prmtSurName = findViewById(R.id.PmSurName);
        prmtEmail = findViewById(R.id.PmEmail);
        prmtPass = findViewById(R.id.PmPassword);
        prmtrePass = findViewById(R.id.PmRePass);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        edtPhoneNumber=findViewById(R.id.phone_number_edt);
//        prmtSortCode=findViewById(R.id.PmSOrtCode);
//        prmtBankAccount=findViewById(R.id.PmAccountNo);
        prmtPostCode = findViewById(R.id.PmPostCode);
        prmtHouseNo = findViewById(R.id.PmHouseNO);
        prmtPrice = findViewById(R.id.Pmprice);
        //  prmtSelectMosque =(Button) findViewById(R.id.PmSelectMosqS);
//        prmtSelectBank=findViewById(R.id.PmSelectBank);
        prmtRadioGroup = findViewById(R.id.PmRadioButtonGroup);
        prmtBtnCancel = findViewById(R.id.PmbtnCancel);
        prmtBtnRegister = findViewById(R.id.PmbtnRegister);
        prmtFirstName.setText(Constants.firstname);
        prmtSurName.setText(Constants.surname);
        prmtPostCode.setText(Constants.postcode_list);
        prmtHouseNo.setText(Constants.house);
        prmtEmail.setText(Constants.email_code);
        prmtPass.setText(Constants.pssword_code);
        prmtrePass.setText(Constants.confirmpass);
        prmtPrice.setText(Constants.pay);
        edtPhoneNumber.setText(Constants.phone);
        user_masjid=findViewById(R.id.user_masjid);
        constants=new Constants(this);
        pmSearchMosque = findViewById(R.id.PmbtnSearchMosque);
        TrailprogressDialog=new ProgressDialog(this);
        prmtRadioOne = findViewById(R.id.PmRadioOneM);
        prmtRadioSix = findViewById(R.id.PmRadioSixM);
        prmtRadioOneYear = findViewById(R.id.PmRadioOneY);
        masjid_name=Constants.user_masjid_name;
        masjid_id=Constants.masjid_id;
        user_masjid.setText(Constants.user_masjid_name);
        prmtPostCode.setText(Constants.postcode_list);
        edtPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                   if(!edtPhoneNumber.getText().toString().equals(""))
                   {
                       String phone=edtPhoneNumber.getText().toString().trim();
                       Intent intent=new Intent(ParmanatRegistartion.this,PhoneVerify.class);
                       intent.putExtra("phone_number","+"+ccp.getSelectedCountryCode()+phone);
                       intent.putExtra("phone",phone);
                       intent.putExtra("code","+"+ccp.getSelectedCountryCode());
                       startActivity(intent);
                      // startActivityForResult(intent,request_code);
                   }
                }
            }
        });
         if(Constants.check_veryfiy)
         {
             edtPhoneNumber.setFocusable(false);
         }
        if(Constants.pay.equals("£ 1"))
        {
            prmtRadioOne.setChecked(true);
        }
        else if(Constants.pay.equals("£ 6"))
        {
            prmtRadioSix.setChecked(true);
        }
        else {
            prmtRadioOneYear.setChecked(true);
        }
        pmSearchMosque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetMajidfunction(prmtPostCode.getText().toString());
            }
        });
        prmtBtnRegister.setOnClickListener(this);
        prmtBtnCancel.setOnClickListener(this);

        prmtprogressDialog = new ProgressDialog(this);

        View.OnClickListener optionClickListiner = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (prmtRadioOne.isChecked()) {
                    prmtPrice.setText("£ 1");
                } else if (prmtRadioSix.isChecked()) {
                    prmtPrice.setText("£ 6");
                } else if (prmtRadioOneYear.isChecked()) {
                    prmtPrice.setText("£ 12");
                } else {
                    prmtPrice.setVisibility(View.GONE);
                }

            }
        };

        prmtRadioOne.setOnClickListener(optionClickListiner);
        prmtRadioSix.setOnClickListener(optionClickListiner);
        prmtRadioOneYear.setOnClickListener(optionClickListiner);
        prmtBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    public void GetMajidfunction(final String s){

        Constants.firstname=prmtFirstName.getText().toString();
        Constants.surname=prmtSurName.getText().toString();
        Constants.postcode_list=s;
        Constants.house=prmtHouseNo.getText().toString();
        Constants.email_code=prmtEmail.getText().toString();
        Constants.pssword_code=prmtPass.getText().toString();
        Constants.confirmpass=prmtrePass.getText().toString();
        Constants.pay=prmtPrice.getText().toString();
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
                                PreferenceManager.userMasjidmodels.add(masjiidmodel);
                            }
                            Intent intent=new Intent(ParmanatRegistartion.this,UserMasjidMapViewActivity.class);
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
                params.put("post_code",s);
                return params;
            }
        };
        PreferenceManager.getInstance().addToRequestQueue(stringRequest);
    }
    private void registerParmanentUser() {
        final String prmtFirstNameHolder = prmtFirstName.getText().toString().trim();
        final String prmtSurNameHolder = prmtSurName.getText().toString().trim();
        final String prmtEmailHolder = prmtEmail.getText().toString().trim();
        final String prmtPassHolder = prmtPass.getText().toString().trim();
        final String phone=edtPhoneNumber.getText().toString();
        final String phone_code=ccp.getSelectedCountryCode();
        final String ma_id=masjid_id;
        String days=prmtPrice.getText().toString();
        String[] array_days=days.split(" ");
        final String exp_month=array_days[1];
        prmtPostCodeHolder = prmtPostCode.getText().toString().trim();
        final String prmtHoudeNoHolder = prmtHouseNo.getText().toString().trim();
        final String prmtPakageSelection = ((RadioButton) findViewById(prmtRadioGroup.getCheckedRadioButtonId())).getText().toString();
   //     final String prmtSubscrpitionPrice = prmtPrice.getText().toString().trim();
        final int sttatus = 1;
        final String User_Type = "Regular";
        constants.kpHUD.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_REGISTER_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        userinfoModels=new ArrayList<>();
                        constants.kpHUD.dismiss();
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
                                PreferenceManager.setEmail(prmtEmailHolder);
                                PreferenceManager.setPassword(prmtPassHolder);
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
                                UserinfoModel userinfoModel=new UserinfoModel(userid,prmtFirstNameHolder,prmtSurNameHolder,prmtEmailHolder,prmtHoudeNoHolder,phone);
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

                params.put("firstname", prmtFirstNameHolder);
                params.put("lastname", prmtSurNameHolder);
                params.put("email", prmtEmailHolder);
                params.put("password", prmtPassHolder);
//                params.put("sort_code", prmtSortCodeHolder);
//                params.put("account_no", prmtBankAccountHolder);
                params.put("postcode", prmtPostCodeHolder);
                params.put("house_no", prmtHoudeNoHolder);
                params.put("status", String.valueOf(sttatus));
                params.put("user_type", User_Type);
//                params.put("masjid",prmtSelectMosqSHolder);
                params.put("masjid",ma_id);
                params.put("mobile_number",phone);
                params.put("mobile_code",phone_code);
                params.put("subscription_month", exp_month);
                params.put("subscription_amount", "");
                params.put("token",refreshToken);

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

    @Override
    public void onClick(View view) {
        if (view == prmtBtnRegister) {
            if (prmtPostCode.getText().toString().trim().equals("")) {
                prmtPostCode.setError("Please Enter Post Code");
            } else if (prmtEmail.getText().toString().trim().equals("")) {
                prmtEmail.setError("Please Enter Email");
             }
             else if(prmtPrice.getText().toString().trim().equals(""))
            {
                prmtEmail.setError("Please Enter Correct Amount");
            }
            else {
                final String prmtFirstNameHolder = prmtFirstName.getText().toString().trim();
                final String prmtSurNameHolder = prmtSurName.getText().toString().trim();
                final String prmtEmailHolder = prmtEmail.getText().toString().trim();
                final String prmtPassHolder = prmtPass.getText().toString().trim();
                final String phone=edtPhoneNumber.getText().toString();
                final String phone_code=ccp.getSelectedCountryCode();
                final String ma_id=masjid_id;
                String days=prmtPrice.getText().toString();
                String[] array_days=days.split(" ");
                final String exp_month=array_days[1];
                prmtPostCodeHolder = prmtPostCode.getText().toString().trim();
                final String prmtHoudeNoHolder = prmtHouseNo.getText().toString().trim();
                final String prmtPakageSelection = ((RadioButton) findViewById(prmtRadioGroup.getCheckedRadioButtonId())).getText().toString();
                //     final String prmtSubscrpitionPrice = prmtPrice.getText().toString().trim();
                final int sttatus = 1;
                final String User_Type = "Regular";

                    String days1=prmtPrice.getText().toString();
                    String[] array_days1=days1.split(" ");
                    final String exp_month1=array_days1[1];
                    Intent intent1 = new Intent(ParmanatRegistartion.this, PayFeeForApp.class);
                    intent1.putExtra("email",prmtEmailHolder);
                    intent1.putExtra("amount",exp_month1);
                    intent1.putExtra("first_name",prmtFirstNameHolder);
                    intent1.putExtra("second_name",prmtSurNameHolder);
                    intent1.putExtra("pass",prmtPassHolder);
                    intent1.putExtra("phone",phone);
                    intent1.putExtra("phone_code",phone_code);
                    intent1.putExtra("masjid",ma_id);
                    intent1.putExtra("exp",exp_month);
                    intent1.putExtra("house",prmtHoudeNoHolder);
                    intent1.putExtra("package",prmtPakageSelection);
                    intent1.putExtra("type",User_Type);
                    intent1.putExtra("status",String.valueOf(sttatus));
                    intent1.putExtra("token",refreshToken);
                    intent1.putExtra("postcode",prmtPostCodeHolder);
                    startActivity(intent1);

            }
        } else if (view == prmtBtnCancel) {
            finish();
        }
    }
    public void getSalahTime(final String masjid,final String userid){

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
                                Intent intent = new Intent(ParmanatRegistartion.this, UserDashboard.class);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(ParmanatRegistartion.this,"Not refund",Toast.LENGTH_SHORT).show();
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
