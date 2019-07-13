package com.tech_sim.mymasjidapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class Visitors extends AppCompatActivity implements View.OnClickListener {

    EditText msjidSearch;
    Button btn_next;
    TextView txt_masjid;
    private Constants constants;
    private Button search_btn;
    private Button cancel_btn;
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
        setContentView(R.layout.activity_visitor);
        initXml();
    }
    public void initXml(){
        txt_masjid=(TextView)findViewById(R.id.txt_masjid);
        btn_next=(Button)findViewById(R.id.btn_next);
        msjidSearch=(EditText)findViewById(R.id.SelectMoasque);
        cancel_btn=(Button)findViewById(R.id.c_next);
        search_btn=(Button)findViewById(R.id.search_next);
        txt_masjid.setText(Constants.visitor_masjidname);
        msjidSearch.setText(Constants.search);
        constants=new Constants(this);
        btn_next.setOnClickListener(this);
        cancel_btn.setOnClickListener(this);
        search_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_next:
                if(!msjidSearch.getText().toString().equals("")) {
                    Constants.search = "";
                    getMajidInformation();
                }
                else {
                    Toast.makeText(Visitors.this,"Please, enter the correct values",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.c_next:
                startActivity(new Intent(Visitors.this, RegistrationMainScreen.class));

                break;
            case R.id.search_next:
                if(!msjidSearch.equals("")) {
                    GetMasjidfunction(msjidSearch.getText().toString().trim());
                }
                else {
                    Toast.makeText(getApplicationContext(),"Please enter the correct Post code",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    public void GetMasjidfunction(final String mas)
    {
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
                                PreferenceManager.visitorMasjidmodels.add(masjiidmodel);
                            }
                            Constants.search=mas;
                            Intent intent=new Intent(Visitors.this,VisitorMasjidMapView.class);
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
                params.put("post_code",mas);
                return params;
            }
        };
        PreferenceManager.getInstance().addToRequestQueue(stringRequest);

    }
    public void getMajidInformation(){
        String url="http://mymasjid.space/mobileApp/admin/getSalahMasjid";
        final String masjid=Constants.masjid_id;
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

                            }
                            else {
                                Log.i("error" ,"Not refund");

                            }
                            PreferenceManager.setCheck(3);
                            Intent intent1 = new Intent(Visitors.this, VisitorActivity.class);
                            startActivity(intent1);

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
}
