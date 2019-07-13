package com.tech_sim.mymasjidapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.utils.Constants;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class StudentActivity  extends AppCompatActivity {
    String childname;
    Button btn_cancel,btn_send;
    TextView txt_name;
    EditText et_detail;
    public Constants constants;
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
        setContentView(R.layout.student_layout);
        constants=new Constants(this);
        Intent intent=getIntent();
        childname=intent.getStringExtra("name");
        initxml();
        txt_name.setText(childname);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String detail=et_detail.getText().toString().trim();
                if(!detail.equals(""))
                {
                    sendReport(detail);
                }
                else {
                    Toast.makeText(StudentActivity.this,"Please,write the child here",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void initxml(){
        btn_cancel=(Button)findViewById(R.id.cancel);
        btn_send=(Button)findViewById(R.id.send);
        txt_name=(TextView)findViewById(R.id.AttendanceText);
        et_detail=(EditText)findViewById(R.id.answerdetail);
    }
    public void sendReport(final String detail) {
        String url = "http://mymasjid.space/mobileApp/admin/childReportMasjid";
        final String child_id = Constants.child_id;
        final String masjid_id = PreferenceManager.getMasjid();
        constants.kpHUD.show();
        RequestQueue r = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        constants.kpHUD.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            if(object.getString("error").equals("false"))
                            {
                                Toast.makeText(StudentActivity.this,object.getString("result"),Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(StudentActivity.this, Masjid.class);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Toast.makeText(StudentActivity.this,object.getString("result"),Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e)
                        {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                constants.kpHUD.dismiss();
                //dialog.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("masjid",masjid_id);
                params.put("child",child_id);
                params.put("detail",detail);
                return params;
            }
        };
        r.add(stringRequest);
    }
}
