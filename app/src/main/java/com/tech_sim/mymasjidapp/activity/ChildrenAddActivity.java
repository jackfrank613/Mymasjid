package com.tech_sim.mymasjidapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.rilixtech.CountryCodePicker;
import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.utils.Constants;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;
import com.tech_sim.mymasjidapp.utils.VolleyMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChildrenAddActivity extends AppCompatActivity {

    EditText childName, fatherName, childAge, parentContectNo;
    Button addMasjidChildCancel, addMasjidChildUpdate;
    ProgressDialog progressDialogAddChild;
    CheckBox cbSunday, cbMonday, cbTuesday, cbWenesday, cbThrusady, cbFriday, cbSaturday;

    public ArrayList<String> classdays;

    public ArrayList<String> classDin;
    private String getday;
    String classdaysValue;
    String ChildStatus,ChildName,FatherName,Age,ContectNo,ClassDays,ChildID;
    private Constants constants;
    private String days[];
    private String class_days="";
    private CountryCodePicker picker;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child_layout);
        constants=new Constants(this);
        days=new String[]{"Sunday","Monday","Tuesday","Wenesday","Thrusday","Friday","Saturday"};
        addMasjidChildCancel = findViewById(R.id.btnaddChildMasjidCancel);
        addMasjidChildUpdate = findViewById(R.id.btnaddChildMasjidUpdate);
        childName = findViewById(R.id.ChildNameMasjid);
        fatherName = findViewById(R.id.FatherNameMasjid);
        childAge = findViewById(R.id.ChildAgeMasjid);
        picker=findViewById(R.id.ccp);
        parentContectNo = findViewById(R.id.ChildParentContectNo);
        cbSunday = findViewById(R.id.checkboxSunday);
        cbFriday = findViewById(R.id.checkboxFriday);
        cbMonday = findViewById(R.id.checkboxMonday);
        cbSaturday = findViewById(R.id.checkboxSaturday);
        cbThrusady = findViewById(R.id.checkboxThrusday);
        cbTuesday = findViewById(R.id.checkboxTuesday);
        cbWenesday = findViewById(R.id.checkboxWensday);
        cbFriday.setChecked(false);
        cbMonday.setChecked(false);
        cbSaturday.setChecked(false);
        cbWenesday.setChecked(false);
        cbThrusady.setChecked(false);
        cbTuesday.setChecked(false);
        cbSunday.setChecked(false);


        addMasjidChildUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String childname=childName.getText().toString().trim();
                String fathername=fatherName.getText().toString().trim();
                String childage=childAge.getText().toString().trim();
                String code=picker.getSelectedCountryCode();
                String phoneno=parentContectNo.getText().toString().trim();
                String phone=code+phoneno;
                if(cbSunday.isChecked())
                {
                    class_days+=cbSunday.getText().toString()+",";
                }
                if(cbFriday.isChecked())
                {
                    class_days+=cbFriday.getText().toString()+",";
                }
                if(cbMonday.isChecked())
                {
                    class_days+=cbMonday.getText().toString()+",";
                }
                if(cbSaturday.isChecked())
                {
                    class_days+=cbSaturday.getText().toString()+",";
                }
                if(cbThrusady.isChecked())
                {
                    class_days+=cbThrusady.getText().toString()+",";

                }
                if(cbTuesday.isChecked())
                {
                    class_days+=cbTuesday.getText().toString()+",";
                }
                if(cbWenesday.isChecked())
                {
                    class_days+=cbWenesday.getText().toString()+",";
                }
                if(!childage.equals("")||!childname.equals("")||!phoneno.equals("")||!fathername.equals(""))
                {
                    sendUpadate(childname,fathername,childage,phone,class_days);
                }
                else {
                    Toast.makeText(ChildrenAddActivity.this,"Please,enter the correct values",Toast.LENGTH_SHORT).show();
                }
            }
        });
        addMasjidChildCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    public void sendUpadate(final String child, final String father, final String age, final String phone,final String classDays)
    {

        String tag="childrequest";
        String url="http://mymasjid.space/mobileApp/admin/add_child_masjid";
        final String masjid_id= PreferenceManager.getMasjid();
        constants.kpHUD.show();
        VolleyMultipartRequest req = new VolleyMultipartRequest(com.android.volley.Request.Method.POST,
                url, new com.android.volley.Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                constants.kpHUD.dismiss();
                String responseStr=new String(response.data);
                try {
                    JSONObject responseobject=new JSONObject(responseStr);
                    if (responseobject.getString("error").equals("false")) {
                        String  message=responseobject.getString("result");
                        Toast.makeText(ChildrenAddActivity.this,message,Toast.LENGTH_SHORT).show();
                        class_days="";
                        Intent intent=new Intent(ChildrenAddActivity.this,Masjid.class);
                        startActivity(intent);
                        try {
                            finalize();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }
                    else {
                        String  message=responseobject.getString("result");
                        Toast.makeText(ChildrenAddActivity.this,message,Toast.LENGTH_SHORT).show();
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
                params.put("childname",child);
                params.put("fathername",father);
                params.put("age",age);
                params.put("contactno",phone);
                params.put("class_days",classDays);
                params.put("masjid",masjid_id);
                params.put("status","1");
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
}
