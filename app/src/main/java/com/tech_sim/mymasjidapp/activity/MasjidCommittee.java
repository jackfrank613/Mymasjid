package com.tech_sim.mymasjidapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class MasjidCommittee extends AppCompatActivity {

    Button btnAddComitteecancel, btnAddCommitteeUpdate;
    Spinner selectPost;
    EditText personName, personContect;
    private Constants constants;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masjid_committee);
        constants=new Constants(this);
        initXml();
        btnAddComitteecancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnAddCommitteeUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String post=selectPost.getSelectedItem().toString();
                String name=personName.getText().toString().trim();
                String contact=personContect.getText().toString().trim();
                if(!name.equals("")||!contact.equals(""))
                {
                    Update(post,name,contact);

                }
                else {
                    Toast.makeText(MasjidCommittee.this,"Please,enter the correct values",Toast.LENGTH_SHORT).show();
                }


            }
        });

    }
    public void initXml(){
        selectPost = findViewById(R.id.SelectPost);
        personName = findViewById(R.id.NameOfPerson);
        personContect = findViewById(R.id.ContectOfPerson);
        btnAddComitteecancel = findViewById(R.id.btnaddCommitteeCancel);
        btnAddCommitteeUpdate = findViewById(R.id.btnaddCommitteeUpdate);

    }
    public void Update(final String post, final String name, final String contact)
    {
        String url="http://mymasjid.space/mobileApp/admin/add_masjid_committee";
        final String masjid_id= PreferenceManager.getMasjid();
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
                                Toast.makeText(MasjidCommittee.this,object.getString("result"),Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(MasjidCommittee.this,object.getString("result"),Toast.LENGTH_SHORT).show();
                            }

                        }catch (Exception e)
                        {
                            e.printStackTrace();
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
                params.put("masjid", masjid_id);
                params.put("type",post);
                params.put("name",name);
                params.put("contactno",contact);
                params.put("status","1");
                return params;
            }
        };
        r.add(stringRequest);
    }
    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }
}
